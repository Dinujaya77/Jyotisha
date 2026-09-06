package io.github.dinujaya77.jyotisha.data.location

import io.github.dinujaya77.jyotisha.domain.location.DeviceLocationFix
import io.github.dinujaya77.jyotisha.domain.location.ForegroundLocationPermission
import io.github.dinujaya77.jyotisha.domain.location.GeoCoordinates
import io.github.dinujaya77.jyotisha.domain.location.LocationSource
import io.github.dinujaya77.jyotisha.domain.location.PermissionPrecision
import io.github.dinujaya77.jyotisha.platform.location.DeviceLocationProvider
import io.github.dinujaya77.jyotisha.platform.location.DeviceLocationRequest
import io.github.dinujaya77.jyotisha.platform.location.DeviceLocationResult
import io.github.dinujaya77.jyotisha.platform.location.DeviceLocationUnavailableReason
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.startCoroutine
import kotlin.coroutines.suspendCoroutine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** T-FR-004–007 / T-NFR-OFFLINE: M4's exactly-one-source repository state machine. */
class LocationSelectionRepositoryTest {
    @Test
    fun onboardingResolvesAbsentRecordToLabelledColomboDefault() = runSuspend {
        val repository = repository(selection = PersistedLocationSelection.Default())

        val state = repository.restore(ForegroundLocationPermission.NONE)

        assertTrue(state.isFirstUse)
        assertEquals(LocationFallback.DEFAULT, state.fallback)
        assertEquals(LocationSource.DEFAULT, state.selectedLocation.provenance.source)
        assertEquals(TownCatalog.defaultTown, state.selectedTown)
        assertEquals(TownCatalog.provenance.datasetVersion, state.selectedLocation.provenance.datasetVersion)
    }

    @Test
    fun manualAndDefaultSelectionAtomicallyReplaceDeviceAndSurviveRepositoryRestart() = runSuspend {
        val persistence = FakePersistence(deviceSelection())
        val firstRepository = repository(persistence = persistence)

        val manual = firstRepository.selectManualTown("geonames:1241622")
        assertEquals(LocationFallback.MANUAL, manual.fallback)
        assertEquals("geonames:1241622", (persistence.selection as PersistedLocationSelection.ManualTown).townId)

        val restarted = repository(persistence = persistence).restore(ForegroundLocationPermission.NONE)
        assertEquals(LocationFallback.MANUAL, restarted.fallback)
        assertEquals("geonames:1241622", restarted.selectedTown?.stableId)

        val default = firstRepository.selectDefault()
        assertEquals(LocationFallback.DEFAULT, default.fallback)
        assertEquals(
            TownCatalog.provenance.datasetVersion,
            (persistence.selection as PersistedLocationSelection.Default).datasetVersion,
        )
    }

    @Test
    fun manualSelectionKeepsAsiaColomboWhenTheDeviceZoneChanges() = runSuspend {
        val state = repository(
            selection = PersistedLocationSelection.ManualTown("geonames:1241622", NOW),
            zoneId = "Europe/London",
        ).restore(ForegroundLocationPermission.NONE)

        assertEquals(LocationFallback.MANUAL, state.fallback)
        assertEquals("Asia/Colombo", state.selectedLocation.provenance.zoneId)
    }

    @Test
    fun invalidManualTownDoesNotDeleteAnActiveDevicePayload() = runSuspend {
        val persistence = FakePersistence(deviceSelection())

        val state = repository(persistence = persistence).selectManualTown("not-a-catalog-town")

        assertEquals(LocationFallback.SAVED_DEVICE, state.fallback)
        assertEquals(LocationSelectionWarning.INVALID_TOWN_SELECTION, state.warning)
        assertTrue(persistence.selection is PersistedLocationSelection.Device)
    }

    @Test
    fun preciseSavedDeviceIsDeletedOnCoarsePermissionDowngrade() = runSuspend {
        val persistence = FakePersistence(deviceSelection(precision = PermissionPrecision.PRECISE))
        val state = repository(persistence = persistence).restore(ForegroundLocationPermission.APPROXIMATE)

        assertEquals(LocationFallback.DEFAULT, state.fallback)
        assertEquals(LocationSelectionWarning.DEVICE_DATA_REMOVED_FOR_PERMISSION, state.warning)
        assertEquals(PersistedLocationSelection.Default(), persistence.selection)
        assertEquals(1, persistence.resetCount)
    }

    @Test
    fun preciseSavedDeviceIsDeletedWhenRefreshRunsAfterPermissionRevocation() = runSuspend {
        val persistence = FakePersistence(deviceSelection(precision = PermissionPrecision.PRECISE))
        val state = repository(persistence = persistence).refreshCurrentLocation(ForegroundLocationPermission.NONE)

        assertEquals(PersistedLocationSelection.Default(), persistence.selection)
        assertEquals(LocationFallback.DEFAULT, state.fallback)
    }

    @Test
    fun futureManualTimestampIsMinimizedToDefault() = runSuspend {
        val persistence = FakePersistence(
            PersistedLocationSelection.ManualTown("geonames:1241622", NOW + 1L),
        )

        val state = repository(persistence = persistence).restore(ForegroundLocationPermission.NONE)

        assertEquals(LocationFallback.DEFAULT, state.fallback)
        assertEquals(LocationSelectionWarning.INVALID_STORED_SELECTION, state.warning)
        assertEquals(PersistedLocationSelection.Default(), persistence.selection)
    }

    @Test
    fun providerFailureInAirplaneStyleStatePreservesActiveManualSelection() = runSuspend {
        val persistence = FakePersistence(PersistedLocationSelection.ManualTown("geonames:1241622", NOW))
        val repository = repository(
            persistence = persistence,
            providerResult = DeviceLocationResult.Unavailable(
                DeviceLocationUnavailableReason.PROVIDER_UNAVAILABLE,
            ),
        )

        val state = repository.refreshCurrentLocation(ForegroundLocationPermission.PRECISE)

        assertEquals(LocationFallback.MANUAL, state.fallback)
        assertEquals(LocationSource.MANUAL, state.selectedLocation.provenance.source)
        assertEquals(
            LocationSelectionWarning.CURRENT_LOCATION_UNAVAILABLE(
                DeviceLocationUnavailableReason.PROVIDER_UNAVAILABLE,
            ),
            state.warning,
        )
        assertTrue(persistence.selection is PersistedLocationSelection.ManualTown)
    }

    @Test
    fun failedRefreshOfStaleSavedDeviceRetainsQualityAdvisoryAndFailure() = runSuspend {
        val stale = deviceSelection(acquiredAtEpochMillis = NOW - 25L * 60L * 60L * 1000L)
        val state = repository(
            selection = stale,
            providerResult = DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.TIMEOUT),
        ).refreshCurrentLocation(ForegroundLocationPermission.PRECISE)

        assertEquals(LocationFallback.SAVED_DEVICE, state.fallback)
        assertEquals(setOf(LocationSelectionWarning.SAVED_DEVICE_STALE), state.advisories)
        assertEquals(
            LocationSelectionWarning.CURRENT_LOCATION_UNAVAILABLE(DeviceLocationUnavailableReason.TIMEOUT),
            state.warning,
        )
        assertEquals(stale.acquiredAtEpochMillis, state.selectedLocation.provenance.acquisitionEpochMillis)
        assertEquals(stale.accuracyMeters, state.selectedLocation.provenance.horizontalAccuracyMeters)
    }

    @Test
    fun staleLowAccuracySavedDeviceRetainsBothAdvisoriesAndRefreshFailure() = runSuspend {
        val staleLowAccuracy = deviceSelection(
            accuracy = 10_001.0,
            acquiredAtEpochMillis = NOW - 25L * 60L * 60L * 1000L,
        )

        val state = repository(
            selection = staleLowAccuracy,
            providerResult = DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.TIMEOUT),
        ).refreshCurrentLocation(ForegroundLocationPermission.PRECISE)

        assertEquals(LocationFallback.SAVED_DEVICE, state.fallback)
        assertEquals(
            setOf(
                LocationSelectionWarning.SAVED_DEVICE_STALE,
                LocationSelectionWarning.LOW_ACCURACY,
            ),
            state.advisories,
        )
        assertEquals(
            LocationSelectionWarning.CURRENT_LOCATION_UNAVAILABLE(DeviceLocationUnavailableReason.TIMEOUT),
            state.warning,
        )
        assertEquals(staleLowAccuracy.acquiredAtEpochMillis, state.selectedLocation.provenance.acquisitionEpochMillis)
        assertEquals(staleLowAccuracy.accuracyMeters, state.selectedLocation.provenance.horizontalAccuracyMeters)
    }

    @Test
    fun successfulForegroundRefreshSelectsOneCurrentDevicePayload() = runSuspend {
        val persistence = FakePersistence(PersistedLocationSelection.ManualTown("geonames:1241622", NOW))
        val candidate = fix(latitude = 6.94, longitude = 79.85)

        val state = repository(
            persistence = persistence,
            providerResult = DeviceLocationResult.Success(candidate),
            zoneId = "Asia/Colombo",
        ).refreshCurrentLocation(ForegroundLocationPermission.PRECISE)

        assertEquals(LocationFallback.CURRENT_DEVICE, state.fallback)
        assertEquals(LocationSource.CURRENT_DEVICE, state.selectedLocation.provenance.source)
        assertEquals("Asia/Colombo", state.selectedLocation.provenance.zoneId)
        assertEquals(LocationSource.CURRENT_DEVICE, (persistence.selection as PersistedLocationSelection.Device).source)
    }

    @Test
    fun futureCurrentLocationCandidateIsRejectedBeforeItCanBePersisted() = runSuspend {
        val persistence = FakePersistence(PersistedLocationSelection.Default())
        val state = repository(
            persistence = persistence,
            providerResult = DeviceLocationResult.Success(
                fix(latitude = 6.94, longitude = 79.85, acquiredAtEpochMillis = NOW + 1L),
            ),
        ).refreshCurrentLocation(ForegroundLocationPermission.PRECISE)

        assertEquals(LocationFallback.DEFAULT, state.fallback)
        assertEquals(LocationSelectionWarning.INVALID_CURRENT_LOCATION, state.warning)
        assertEquals(PersistedLocationSelection.Default(), persistence.selection)
    }

    @Test
    fun nonMaterialRefreshRetainsSavedCoordinatesAndAcquisitionTime() = runSuspend {
        val saved = deviceSelection(latitude = 6.94, longitude = 79.85, accuracy = 100.0)
        val candidate = fix(latitude = 6.9401, longitude = 79.8501, accuracy = 75.0)
        val persistence = FakePersistence(saved)

        val state = repository(
            persistence = persistence,
            providerResult = DeviceLocationResult.Success(candidate),
        ).refreshCurrentLocation(ForegroundLocationPermission.PRECISE)

        assertEquals(LocationFallback.SAVED_DEVICE, state.fallback)
        assertEquals(LocationSelectionWarning.LOCATION_UNCHANGED, state.warning)
        assertEquals(saved.coordinates, (persistence.selection as PersistedLocationSelection.Device).coordinates)
        assertEquals(saved.acquiredAtEpochMillis, persistence.selection.let {
            (it as PersistedLocationSelection.Device).acquiredAtEpochMillis
        })
    }

    @Test
    fun laterManualSelectionCancelsAndInvalidatesAnInFlightForegroundResult() {
        val persistence = FakePersistence(PersistedLocationSelection.Default())
        val provider = DeferredProvider()
        val repository = LocationSelectionRepository(
            persistence = persistence,
            deviceLocationProvider = provider,
            clock = Clock.fixed(Instant.ofEpochMilli(NOW), ZoneOffset.UTC),
            zoneSource = ZoneSource { "Asia/Colombo" },
        )
        val completed = CountDownLatch(1)
        var refreshResult: Result<LocationSelectionState>? = null

        val refresh: suspend () -> LocationSelectionState = {
            repository.refreshCurrentLocation(ForegroundLocationPermission.PRECISE)
        }
        refresh.startCoroutine(
            object : Continuation<LocationSelectionState> {
                override val context = EmptyCoroutineContext

                override fun resumeWith(result: Result<LocationSelectionState>) {
                    refreshResult = result
                    completed.countDown()
                }
            },
        )
        assertTrue("foreground request was not started", provider.requested.await(5, TimeUnit.SECONDS))

        runSuspend { repository.selectManualTown("geonames:1241622") }
        assertEquals(1, provider.cancelCount)

        provider.deliver(DeviceLocationResult.Success(fix(latitude = 6.94, longitude = 79.85)))
        assertTrue("cancelled refresh did not complete", completed.await(5, TimeUnit.SECONDS))
        assertEquals(LocationFallback.MANUAL, refreshResult?.getOrThrow()?.fallback)
        assertTrue(persistence.selection is PersistedLocationSelection.ManualTown)
    }

    @Test
    fun cancellationBeforeProviderRegistrationCannotStartAProviderRequestAfterRestoreCompletes() {
        val persistence = DeferredReadPersistence(PersistedLocationSelection.Default())
        val provider = DeferredProvider()
        val repository = LocationSelectionRepository(
            persistence = persistence,
            deviceLocationProvider = provider,
            clock = Clock.fixed(Instant.ofEpochMilli(NOW), ZoneOffset.UTC),
            zoneSource = ZoneSource { "Asia/Colombo" },
        )
        val completed = CountDownLatch(1)
        val refresh: suspend () -> LocationSelectionState = {
            repository.refreshCurrentLocation(ForegroundLocationPermission.PRECISE)
        }
        refresh.startCoroutine(object : Continuation<LocationSelectionState> {
            override val context = EmptyCoroutineContext
            override fun resumeWith(result: Result<LocationSelectionState>) = completed.countDown()
        })
        assertTrue("restore did not begin", persistence.readStarted.await(5, TimeUnit.SECONDS))

        runSuspend { repository.selectManualTown("geonames:1241622") }
        persistence.releaseRead()

        assertTrue("cancelled refresh did not complete", completed.await(5, TimeUnit.SECONDS))
        assertEquals(0, provider.requestCount)
        assertTrue(persistence.selection is PersistedLocationSelection.ManualTown)
    }

    @Test
    fun cancellationDuringProviderPreflightCannotStartPlatformAcquisition() {
        val provider = PreflightProvider()
        val repository = LocationSelectionRepository(
            persistence = FakePersistence(PersistedLocationSelection.Default()),
            deviceLocationProvider = provider,
            clock = Clock.fixed(Instant.ofEpochMilli(NOW), ZoneOffset.UTC),
            zoneSource = ZoneSource { "Asia/Colombo" },
        )
        val completed = CountDownLatch(1)

        val refreshThread = startRefresh(repository, completed)
        assertTrue("provider preflight did not begin", provider.preflightEntered.await(5, TimeUnit.SECONDS))

        repository.cancelCurrentLocationRequest()
        provider.releasePreflight()

        assertTrue("cancelled refresh did not complete", completed.await(5, TimeUnit.SECONDS))
        refreshThread.join(5_000)
        assertEquals(1, provider.cancelCount)
        assertEquals(0, provider.platformRequestCount)
    }

    @Test
    fun manualSupersessionDuringProviderPreflightCannotStartOrPublishOlderRequest() {
        val persistence = FakePersistence(PersistedLocationSelection.Default())
        val provider = PreflightProvider()
        val repository = LocationSelectionRepository(
            persistence = persistence,
            deviceLocationProvider = provider,
            clock = Clock.fixed(Instant.ofEpochMilli(NOW), ZoneOffset.UTC),
            zoneSource = ZoneSource { "Asia/Colombo" },
        )
        val completed = CountDownLatch(1)

        val refreshThread = startRefresh(repository, completed)
        assertTrue("provider preflight did not begin", provider.preflightEntered.await(5, TimeUnit.SECONDS))

        val manual = runSuspend { repository.selectManualTown("geonames:1241622") }
        provider.releasePreflight()

        assertTrue("superseded refresh did not complete", completed.await(5, TimeUnit.SECONDS))
        refreshThread.join(5_000)
        assertEquals(LocationFallback.MANUAL, manual.fallback)
        assertEquals(1, provider.cancelCount)
        assertEquals(0, provider.platformRequestCount)
        assertTrue(persistence.selection is PersistedLocationSelection.ManualTown)
    }

    @Test
    fun invalidDeviceZoneDoesNotPublishSavedCoordinatesWithAnInvalidZone() = runSuspend {
        val state = repository(
            selection = deviceSelection(),
            zoneId = "not a zone",
        ).restore(ForegroundLocationPermission.PRECISE)

        assertEquals(LocationFallback.DEFAULT, state.fallback)
        assertEquals(LocationSelectionWarning.DEVICE_ZONE_UNAVAILABLE, state.warning)
    }

    @Test
    fun throwingZoneSourceFallsBackWithoutPublishingDeviceCoordinates() = runSuspend {
        val state = LocationSelectionRepository(
            persistence = FakePersistence(deviceSelection()),
            deviceLocationProvider = FakeProvider(
                DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.TIMEOUT),
            ),
            clock = Clock.fixed(Instant.ofEpochMilli(NOW), ZoneOffset.UTC),
            zoneSource = ZoneSource { throw IllegalStateException("zone unavailable") },
        ).restore(ForegroundLocationPermission.PRECISE)

        assertEquals(LocationFallback.DEFAULT, state.fallback)
        assertEquals(LocationSelectionWarning.DEVICE_ZONE_UNAVAILABLE, state.warning)
    }

    @Test
    fun invalidStoredDeviceZoneDeletesOnlyLocationRecordBeforeDefaultFallback() = runSuspend {
        val persistence = FakePersistence(deviceSelection())

        val state = repository(persistence = persistence, zoneId = "not a zone")
            .restore(ForegroundLocationPermission.PRECISE)

        assertEquals(LocationFallback.DEFAULT, state.fallback)
        assertEquals(LocationSelectionWarning.DEVICE_ZONE_UNAVAILABLE, state.warning)
        assertEquals(PersistedLocationSelection.Default(), persistence.selection)
        assertEquals(1, persistence.resetCount)
    }

    @Test
    fun lateCallbackCannotCommitDeviceAfterManualSelectionDuringDeferredStoreWrite() {
        val persistence = DeferredDeviceWritePersistence(PersistedLocationSelection.Default())
        val provider = DeferredProvider()
        val repository = LocationSelectionRepository(
            persistence = persistence,
            deviceLocationProvider = provider,
            clock = Clock.fixed(Instant.ofEpochMilli(NOW), ZoneOffset.UTC),
            zoneSource = ZoneSource { "Asia/Colombo" },
        )
        val completed = CountDownLatch(1)
        var refreshResult: Result<LocationSelectionState>? = null
        val refresh: suspend () -> LocationSelectionState = {
            repository.refreshCurrentLocation(ForegroundLocationPermission.PRECISE)
        }
        refresh.startCoroutine(object : Continuation<LocationSelectionState> {
            override val context = EmptyCoroutineContext

            override fun resumeWith(result: Result<LocationSelectionState>) {
                refreshResult = result
                completed.countDown()
            }
        })
        assertTrue("foreground request was not started", provider.requested.await(5, TimeUnit.SECONDS))

        provider.deliver(DeviceLocationResult.Success(fix(latitude = 6.94, longitude = 79.85)))
        assertTrue("device write was not deferred", persistence.deviceWriteStarted.await(5, TimeUnit.SECONDS))

        runSuspend { repository.selectManualTown("geonames:1241622") }
        persistence.releaseDeviceWrite()

        assertTrue("refresh did not complete", completed.await(5, TimeUnit.SECONDS))
        assertEquals(LocationFallback.MANUAL, refreshResult?.getOrThrow()?.fallback)
        assertTrue(persistence.selection is PersistedLocationSelection.ManualTown)
    }

    private fun repository(
        selection: PersistedLocationSelection = PersistedLocationSelection.Default(),
        persistence: FakePersistence = FakePersistence(selection),
        providerResult: DeviceLocationResult = DeviceLocationResult.Unavailable(
            DeviceLocationUnavailableReason.TIMEOUT,
        ),
        zoneId: String = "Asia/Colombo",
    ) = LocationSelectionRepository(
        persistence = persistence,
        deviceLocationProvider = FakeProvider(providerResult),
        clock = Clock.fixed(Instant.ofEpochMilli(NOW), ZoneOffset.UTC),
        zoneSource = ZoneSource { zoneId },
    )

    private fun deviceSelection(
        latitude: Double = 6.94,
        longitude: Double = 79.85,
        accuracy: Double = 15.0,
        precision: PermissionPrecision = PermissionPrecision.PRECISE,
        acquiredAtEpochMillis: Long = NOW,
    ) = PersistedLocationSelection.Device(
        coordinates = GeoCoordinates(latitude, longitude),
        accuracyMeters = accuracy,
        permissionPrecision = precision,
        acquiredAtEpochMillis = acquiredAtEpochMillis,
        source = LocationSource.CURRENT_DEVICE,
    )

    private fun fix(
        latitude: Double,
        longitude: Double,
        accuracy: Double = 15.0,
        acquiredAtEpochMillis: Long = NOW,
    ) = DeviceLocationFix(
        coordinates = GeoCoordinates(latitude, longitude),
        horizontalAccuracyMeters = accuracy,
        permissionPrecision = PermissionPrecision.PRECISE,
        acquisitionEpochMillis = acquiredAtEpochMillis,
        elapsedRealtimeMillis = 1_000L,
    )

    private open class FakePersistence(
        var selection: PersistedLocationSelection,
        var recoveryWarning: LocationSelectionRecoveryWarning? = null,
    ) : LocationSelectionPersistence {
        var resetCount = 0

        override suspend fun read() = PersistedLocationSelectionRead(selection, recoveryWarning).also {
            recoveryWarning = null
        }

        override suspend fun selectDevice(selection: PersistedLocationSelection.Device): Boolean {
            this.selection = selection
            return true
        }

        override suspend fun selectDeviceIfCurrent(
            selection: PersistedLocationSelection.Device,
            mayWrite: () -> Boolean,
        ): Boolean {
            if (!mayWrite()) return false
            this.selection = selection
            return true
        }

        override suspend fun selectManualTown(townId: String, selectedAtEpochMillis: Long): Boolean {
            selection = PersistedLocationSelection.ManualTown(townId, selectedAtEpochMillis)
            return true
        }

        override suspend fun selectDefault(datasetVersion: String?): Boolean {
            selection = PersistedLocationSelection.Default(datasetVersion)
            return true
        }

        override suspend fun resetLocationData() {
            resetCount += 1
            selection = PersistedLocationSelection.Default()
        }
    }

    private class FakeProvider(
        private val result: DeviceLocationResult,
    ) : DeviceLocationProvider {
        override fun requestCurrentLocation(
            request: DeviceLocationRequest,
            onResult: (DeviceLocationResult) -> Unit,
        ) = onResult(result)

        override fun cancelActiveRequest() = Unit
    }

    private class DeferredProvider : DeviceLocationProvider {
        val requested = CountDownLatch(1)
        var cancelCount = 0
        var requestCount = 0
        private var callback: ((DeviceLocationResult) -> Unit)? = null

        override fun requestCurrentLocation(
            request: DeviceLocationRequest,
            onResult: (DeviceLocationResult) -> Unit,
        ) {
            requestCount += 1
            callback = onResult
            requested.countDown()
        }

        override fun cancelActiveRequest() {
            cancelCount += 1
        }

        fun deliver(result: DeviceLocationResult) {
            callback?.invoke(result)
        }
    }

    /** Forces the repository/provider ownership handoff without a timing-based race. */
    private class PreflightProvider : DeviceLocationProvider {
        val preflightEntered = CountDownLatch(1)
        var cancelCount = 0
        var platformRequestCount = 0
        private val release = CountDownLatch(1)

        override fun requestCurrentLocation(
            request: DeviceLocationRequest,
            onResult: (DeviceLocationResult) -> Unit,
        ) = error("The repository must use the cancellable startup handshake.")

        override fun requestCurrentLocation(
            request: DeviceLocationRequest,
            onResult: (DeviceLocationResult) -> Unit,
            mayStart: () -> Boolean,
        ) {
            preflightEntered.countDown()
            check(release.await(5, TimeUnit.SECONDS))
            if (mayStart()) platformRequestCount += 1
        }

        override fun cancelActiveRequest() {
            cancelCount += 1
        }

        fun releasePreflight() = release.countDown()
    }

    private fun startRefresh(
        repository: LocationSelectionRepository,
        completed: CountDownLatch,
    ): Thread {
        val refresh: suspend () -> LocationSelectionState = {
            repository.refreshCurrentLocation(ForegroundLocationPermission.PRECISE)
        }
        return Thread {
            refresh.startCoroutine(object : Continuation<LocationSelectionState> {
                override val context = EmptyCoroutineContext
                override fun resumeWith(result: Result<LocationSelectionState>) = completed.countDown()
            })
        }.also(Thread::start)
    }

    private class DeferredReadPersistence(
        selection: PersistedLocationSelection,
    ) : FakePersistence(selection) {
        val readStarted = CountDownLatch(1)
        private var pendingRead: (() -> Unit)? = null
        private var deferNextRead = true

        override suspend fun read(): PersistedLocationSelectionRead {
            if (!deferNextRead) return PersistedLocationSelectionRead(selection)
            deferNextRead = false
            return suspendCoroutine { continuation ->
                pendingRead = { continuation.resume(PersistedLocationSelectionRead(selection)) }
                readStarted.countDown()
            }
        }

        fun releaseRead() {
            requireNotNull(pendingRead).invoke()
            pendingRead = null
        }
    }

    private class DeferredDeviceWritePersistence(
        selection: PersistedLocationSelection,
    ) : FakePersistence(selection) {
        val deviceWriteStarted = CountDownLatch(1)
        private var pendingWrite: (() -> Unit)? = null

        override suspend fun selectDeviceIfCurrent(
            selection: PersistedLocationSelection.Device,
            mayWrite: () -> Boolean,
        ): Boolean = suspendCoroutine { continuation ->
            pendingWrite = {
                val wrote = mayWrite()
                if (wrote) this.selection = selection
                continuation.resume(wrote)
            }
            deviceWriteStarted.countDown()
        }

        fun releaseDeviceWrite() {
            requireNotNull(pendingWrite).invoke()
            pendingWrite = null
        }
    }

    private fun <T> runSuspend(block: suspend () -> T): T {
        val completed = CountDownLatch(1)
        var result: Result<T>? = null
        block.startCoroutine(object : Continuation<T> {
            override val context = EmptyCoroutineContext

            override fun resumeWith(outcome: Result<T>) {
                result = outcome
                completed.countDown()
            }
        })
        assertTrue("suspend operation did not complete", completed.await(5, TimeUnit.SECONDS))
        return requireNotNull(result).getOrThrow()
    }

    private companion object {
        const val NOW = 1_800_000_000_000L
    }
}
