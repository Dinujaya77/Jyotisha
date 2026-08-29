package io.github.dinujaya77.jyotisha.data.location

import io.github.dinujaya77.jyotisha.domain.location.DeviceLocationFix
import io.github.dinujaya77.jyotisha.domain.location.DeviceFixUsability
import io.github.dinujaya77.jyotisha.domain.location.ForegroundLocationPermission
import io.github.dinujaya77.jyotisha.domain.location.LocationPolicy
import io.github.dinujaya77.jyotisha.domain.location.LocationProvenance
import io.github.dinujaya77.jyotisha.domain.location.LocationSource
import io.github.dinujaya77.jyotisha.domain.location.PermissionPrecision
import io.github.dinujaya77.jyotisha.domain.location.SavedFixAge
import io.github.dinujaya77.jyotisha.domain.location.SelectedLocation
import io.github.dinujaya77.jyotisha.domain.location.SelectedLocationMode
import io.github.dinujaya77.jyotisha.platform.location.DeviceLocationProvider
import io.github.dinujaya77.jyotisha.platform.location.DeviceLocationRequest
import io.github.dinujaya77.jyotisha.platform.location.DeviceLocationResult
import io.github.dinujaya77.jyotisha.platform.location.DeviceLocationUnavailableReason
import java.time.Clock
import java.time.ZoneId
import java.util.concurrent.atomic.AtomicLong
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Small substitution boundary around the approved active-selection record.  It intentionally
 * exposes no location history, Flow, scheduler, or calculation state.
 */
interface LocationSelectionPersistence {
    suspend fun read(): PersistedLocationSelectionRead
    suspend fun selectDevice(selection: PersistedLocationSelection.Device): Boolean
    suspend fun selectDeviceIfCurrent(
        selection: PersistedLocationSelection.Device,
        mayWrite: () -> Boolean,
    ): Boolean
    suspend fun selectManualTown(townId: String, selectedAtEpochMillis: Long): Boolean
    suspend fun selectDefault(datasetVersion: String?): Boolean
    suspend fun resetLocationData()
}

/** Device zones are deliberately resolved at use time and are never persisted with a fix. */
fun interface ZoneSource {
    fun currentZoneId(): String
}

/**
 * Settings-facing location state. Town names remain resource-backed: callers resolve
 * [selectedTown]'s resource key instead of retaining a localized name in persisted data.
 */
data class LocationSelectionState(
    val selectedLocation: SelectedLocation,
    val selectedTown: TownRecord?,
    val selectedMode: SelectedLocationMode,
    val fallback: LocationFallback,
    val isFirstUse: Boolean,
    val warning: LocationSelectionWarning? = null,
)

enum class LocationFallback {
    CURRENT_DEVICE,
    SAVED_DEVICE,
    MANUAL,
    DEFAULT,
}

sealed interface LocationSelectionWarning {
    data object STORAGE_RECOVERED : LocationSelectionWarning
    data object SAVED_DEVICE_STALE : LocationSelectionWarning
    data object DEVICE_DATA_REMOVED_FOR_PERMISSION : LocationSelectionWarning
    data object INVALID_STORED_SELECTION : LocationSelectionWarning
    data object INVALID_TOWN_SELECTION : LocationSelectionWarning
    data object INVALID_CURRENT_LOCATION : LocationSelectionWarning
    data object DEVICE_ZONE_UNAVAILABLE : LocationSelectionWarning
    data object LOCATION_UNCHANGED : LocationSelectionWarning
    data class CURRENT_LOCATION_UNAVAILABLE(
        val reason: DeviceLocationUnavailableReason,
    ) : LocationSelectionWarning
}

/**
 * Exactly-one-source selection coordinator for M4.  Each public selection delegates to one
 * DataStore transaction; manual/default writes remove a device payload in that same edit.
 * UI/lifecycle publication and calculation assembly remain assigned to later milestones.
 */
class LocationSelectionRepository(
    private val persistence: LocationSelectionPersistence,
    private val deviceLocationProvider: DeviceLocationProvider,
    private val clock: Clock = Clock.systemUTC(),
    private val zoneSource: ZoneSource = ZoneSource { ZoneId.systemDefault().id },
) {
    private val selectionGeneration = AtomicLong()
    private val requestLock = Any()
    private var activeForegroundRequest: ActiveForegroundRequest? = null

    /** Restores a locally saved selection, reconciling privacy on a permission downgrade. */
    suspend fun restore(
        currentPermission: ForegroundLocationPermission,
    ): LocationSelectionState = resolve(persistence.read(), currentPermission)

    /** Selects a bundled public town and atomically deletes any persisted device payload. */
    suspend fun selectManualTown(townStableId: String): LocationSelectionState {
        val town = TownCatalog.findByStableId(townStableId)
            ?: return resolve(persistence.read(), currentPermission = null).withWarning(
                LocationSelectionWarning.INVALID_TOWN_SELECTION,
            )
        val cancelledRequest = detachForegroundRequest()
        return try {
            val selectedAtEpochMillis = clock.millis()
            if (!persistence.selectManualTown(town.stableId, selectedAtEpochMillis)) {
                resolve(persistence.read(), currentPermission = null).withWarning(
                    LocationSelectionWarning.INVALID_TOWN_SELECTION,
                )
            } else {
                manualState(town, selectedAtEpochMillis)
            }
        } finally {
            cancelledRequest?.complete(ForegroundRequestResult.Cancelled)
        }
    }

    /** Selects the labelled Colombo default and atomically removes a device payload. */
    suspend fun selectDefault(): LocationSelectionState {
        val cancelledRequest = detachForegroundRequest()
        return try {
            val persisted = persistence.selectDefault(TownCatalog.provenance.datasetVersion)
            if (persisted) {
                defaultState(isFirstUse = false)
            } else {
                resolve(persistence.read(), currentPermission = null).withWarning(
                    LocationSelectionWarning.INVALID_STORED_SELECTION,
                )
            }
        } finally {
            cancelledRequest?.complete(ForegroundRequestResult.Cancelled)
        }
    }

    /** Deletes the complete record. Absence resolves locally to the labelled default. */
    suspend fun resetLocationData(): LocationSelectionState {
        val cancelledRequest = detachForegroundRequest()
        return try {
            persistence.resetLocationData()
            defaultState(isFirstUse = true)
        } finally {
            cancelledRequest?.complete(ForegroundRequestResult.Cancelled)
        }
    }

    /** Cancels a foreground request without creating a retrieval-failure warning. */
    fun cancelCurrentLocationRequest() {
        invalidateForegroundRequest()
    }

    /**
     * Performs one foreground request. Failure preserves the restored manual/default/saved state;
     * it never creates a history entry or a replacement device record.
     */
    suspend fun refreshCurrentLocation(
        currentPermission: ForegroundLocationPermission,
    ): LocationSelectionState {
        val requestGeneration = startForegroundRequest()
        val prior = restore(currentPermission)
        return when (val result = awaitCurrentLocation(currentPermission, requestGeneration)) {
            ForegroundRequestResult.Cancelled -> restore(currentPermission)

            is ForegroundRequestResult.Result -> when (val value = result.value) {
                is DeviceLocationResult.Unavailable -> prior.withWarning(
                    LocationSelectionWarning.CURRENT_LOCATION_UNAVAILABLE(value.reason),
                )

                is DeviceLocationResult.Success -> {
                    if (selectionGeneration.get() != requestGeneration) {
                        restore(currentPermission)
                    } else {
                    adoptCurrentFix(value.fix, prior, currentPermission, requestGeneration)
                    }
                }
            }
        }
    }

    private suspend fun adoptCurrentFix(
        candidate: DeviceLocationFix,
        prior: LocationSelectionState,
        currentPermission: ForegroundLocationPermission,
        requestGeneration: Long,
    ): LocationSelectionState {
        if (LocationPolicy.deviceFixUsability(candidate) != DeviceFixUsability.USABLE) {
            return prior.withWarning(LocationSelectionWarning.INVALID_CURRENT_LOCATION)
        }
        val deviceZone = validDeviceZoneId()
        if (deviceZone == null) {
            return prior.withWarning(LocationSelectionWarning.DEVICE_ZONE_UNAVAILABLE)
        }

        val saved = (persistence.read().selection as? PersistedLocationSelection.Device)
            ?.toFix()
        if (selectionGeneration.get() != requestGeneration) {
            return restore(currentPermission)
        }
        val decision = LocationPolicy.replacementDecision(saved, candidate, clock.millis())
        if (!decision.adoptCandidate && saved != null) {
            return if (selectionGeneration.get() == requestGeneration) {
                savedDeviceState(saved, deviceZone).withWarning(LocationSelectionWarning.LOCATION_UNCHANGED)
            } else {
                restore(currentPermission)
            }
        }

        val selection = PersistedLocationSelection.Device(
            coordinates = candidate.coordinates,
            accuracyMeters = candidate.horizontalAccuracyMeters,
            permissionPrecision = candidate.permissionPrecision,
            acquiredAtEpochMillis = candidate.acquisitionEpochMillis,
            source = LocationSource.CURRENT_DEVICE,
        )
        if (!persistence.selectDeviceIfCurrent(selection) {
                selectionGeneration.get() == requestGeneration
            }
        ) {
            return if (selectionGeneration.get() == requestGeneration) {
                prior.withWarning(LocationSelectionWarning.INVALID_CURRENT_LOCATION)
            } else {
                restore(currentPermission)
            }
        }
        return currentDeviceState(candidate, deviceZone)
    }

    private suspend fun resolve(
        read: PersistedLocationSelectionRead,
        currentPermission: ForegroundLocationPermission?,
    ): LocationSelectionState {
        val recoveryWarning = read.recoveryWarning?.let { LocationSelectionWarning.STORAGE_RECOVERED }
        return when (val selection = read.selection) {
            is PersistedLocationSelection.Device -> {
                if (currentPermission != null && LocationPolicy.mustDeleteSavedDeviceFix(
                        savedPrecision = selection.permissionPrecision,
                        currentPermission = currentPermission,
                        selectedMode = SelectedLocationMode.CURRENT_DEVICE,
                    )
                ) {
                    persistence.resetLocationData()
                    defaultState(
                        isFirstUse = true,
                        warning = LocationSelectionWarning.DEVICE_DATA_REMOVED_FOR_PERMISSION,
                    )
                } else {
                    val fix = selection.toFix()
                    if (LocationPolicy.savedFixAge(fix.acquisitionEpochMillis, clock.millis()) == SavedFixAge.INVALID) {
                        persistence.resetLocationData()
                        defaultState(
                            isFirstUse = true,
                            warning = LocationSelectionWarning.INVALID_STORED_SELECTION,
                        )
                    } else {
                        val deviceZone = validDeviceZoneId()
                        if (deviceZone == null) {
                            persistence.resetLocationData()
                            defaultState(
                                isFirstUse = true,
                                warning = LocationSelectionWarning.DEVICE_ZONE_UNAVAILABLE,
                            )
                        } else {
                            savedDeviceState(fix, deviceZone).withWarning(
                                recoveryWarning ?: if (
                                    LocationPolicy.savedFixAge(
                                        fix.acquisitionEpochMillis,
                                        clock.millis(),
                                    ) == SavedFixAge.STALE
                                ) {
                                    LocationSelectionWarning.SAVED_DEVICE_STALE
                                } else {
                                    null
                                },
                            )
                        }
                    }
                }
            }

            is PersistedLocationSelection.ManualTown -> {
                val town = TownCatalog.findByStableId(selection.townId)
                if (town == null || selection.selectedAtEpochMillis > clock.millis()) {
                    persistence.resetLocationData()
                    defaultState(false, LocationSelectionWarning.INVALID_STORED_SELECTION)
                } else {
                    manualState(town, selection.selectedAtEpochMillis).withWarning(recoveryWarning)
                }
            }

            is PersistedLocationSelection.Default -> {
                if (selection.datasetVersion != null &&
                    selection.datasetVersion != TownCatalog.provenance.datasetVersion
                ) {
                    persistence.selectDefault(TownCatalog.provenance.datasetVersion)
                    defaultState(false, LocationSelectionWarning.INVALID_STORED_SELECTION)
                } else {
                    defaultState(
                        isFirstUse = selection.datasetVersion == null && recoveryWarning == null,
                        warning = recoveryWarning,
                    )
                }
            }
        }
    }

    private fun currentDeviceState(
        fix: DeviceLocationFix,
        zoneId: String,
    ) = LocationSelectionState(
        selectedLocation = SelectedLocation(
            coordinates = fix.coordinates,
            provenance = LocationProvenance(
                source = LocationSource.CURRENT_DEVICE,
                zoneId = zoneId,
                horizontalAccuracyMeters = fix.horizontalAccuracyMeters,
                permissionPrecision = fix.permissionPrecision,
                acquisitionEpochMillis = fix.acquisitionEpochMillis,
            ),
        ),
        selectedTown = null,
        selectedMode = SelectedLocationMode.CURRENT_DEVICE,
        fallback = LocationFallback.CURRENT_DEVICE,
        isFirstUse = false,
    )

    private fun savedDeviceState(
        fix: DeviceLocationFix,
        zoneId: String,
    ) = LocationSelectionState(
            selectedLocation = SelectedLocation(
                coordinates = fix.coordinates,
                provenance = LocationProvenance(
                    source = LocationSource.SAVED_DEVICE,
                    zoneId = zoneId,
                    horizontalAccuracyMeters = fix.horizontalAccuracyMeters,
                    permissionPrecision = fix.permissionPrecision,
                    acquisitionEpochMillis = fix.acquisitionEpochMillis,
                ),
            ),
            selectedTown = null,
            selectedMode = SelectedLocationMode.CURRENT_DEVICE,
            fallback = LocationFallback.SAVED_DEVICE,
            isFirstUse = false,
        )

    private fun manualState(town: TownRecord, selectedAtEpochMillis: Long) = LocationSelectionState(
        selectedLocation = SelectedLocation(
            coordinates = town.coordinates(),
            provenance = LocationProvenance(
                source = LocationSource.MANUAL,
                zoneId = LocationPolicy.ASIA_COLOMBO_ZONE_ID,
                selectionEpochMillis = selectedAtEpochMillis,
                datasetVersion = town.sourceDatasetVersion,
            ),
        ),
        selectedTown = town,
        selectedMode = SelectedLocationMode.MANUAL,
        fallback = LocationFallback.MANUAL,
        isFirstUse = false,
    )

    private fun defaultState(
        isFirstUse: Boolean,
        warning: LocationSelectionWarning? = null,
    ): LocationSelectionState {
        val town = TownCatalog.defaultTown
        return LocationSelectionState(
            selectedLocation = SelectedLocation(
                coordinates = town.coordinates(),
                provenance = LocationProvenance(
                    source = LocationSource.DEFAULT,
                    zoneId = LocationPolicy.ASIA_COLOMBO_ZONE_ID,
                    datasetVersion = TownCatalog.provenance.datasetVersion,
                ),
            ),
            selectedTown = town,
            selectedMode = SelectedLocationMode.DEFAULT,
            fallback = LocationFallback.DEFAULT,
            isFirstUse = isFirstUse,
            warning = warning,
        )
    }

    private suspend fun awaitCurrentLocation(
        permission: ForegroundLocationPermission,
        requestGeneration: Long,
    ): ForegroundRequestResult = suspendCoroutine { continuation ->
        val active = ActiveForegroundRequest(requestGeneration) { result -> continuation.resume(result) }
        synchronized(requestLock) {
            activeForegroundRequest = active
        }
        deviceLocationProvider.requestCurrentLocation(DeviceLocationRequest(permission)) { result ->
            completeForegroundRequest(requestGeneration, ForegroundRequestResult.Result(result))
        }
    }

    private fun startForegroundRequest(): Long {
        invalidateForegroundRequest()
        return selectionGeneration.get()
    }

    private fun invalidateForegroundRequest() {
        detachForegroundRequest()?.complete(ForegroundRequestResult.Cancelled)
    }

    private fun detachForegroundRequest(): ActiveForegroundRequest? {
        selectionGeneration.incrementAndGet()
        val active = synchronized(requestLock) {
            activeForegroundRequest.also { activeForegroundRequest = null }
        }
        if (active != null) {
            deviceLocationProvider.cancelActiveRequest()
        }
        return active
    }

    private fun completeForegroundRequest(
        generation: Long,
        result: ForegroundRequestResult,
    ) {
        val active = synchronized(requestLock) {
            activeForegroundRequest?.takeIf { it.generation == generation }?.also {
                activeForegroundRequest = null
            }
        }
        active?.complete(result)
    }

    private fun validDeviceZoneId(): String? = try {
        zoneSource.currentZoneId()
            .takeIf { it.isNotBlank() }
            ?.let(ZoneId::of)
            ?.id
    } catch (_: RuntimeException) {
        null
    }

    private fun PersistedLocationSelection.Device.toFix() = DeviceLocationFix(
        coordinates = coordinates,
        horizontalAccuracyMeters = accuracyMeters,
        permissionPrecision = permissionPrecision,
        acquisitionEpochMillis = acquiredAtEpochMillis,
    )

    private fun TownRecord.coordinates() = io.github.dinujaya77.jyotisha.domain.location.GeoCoordinates(
        latitudeDegrees = latitudeE6 / 1_000_000.0,
        longitudeDegrees = longitudeE6 / 1_000_000.0,
    )

    private fun LocationSelectionState.withWarning(
        warning: LocationSelectionWarning?,
    ) = copy(warning = warning)

    private data class ActiveForegroundRequest(
        val generation: Long,
        val complete: (ForegroundRequestResult) -> Unit,
    )

    private sealed interface ForegroundRequestResult {
        data object Cancelled : ForegroundRequestResult
        data class Result(val value: DeviceLocationResult) : ForegroundRequestResult
    }
}
