package io.github.dinujaya77.jyotisha.data.location

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.dinujaya77.jyotisha.domain.location.GeoCoordinates
import io.github.dinujaya77.jyotisha.domain.location.LocationPolicy
import io.github.dinujaya77.jyotisha.domain.location.LocationSource
import io.github.dinujaya77.jyotisha.domain.location.PermissionPrecision
import java.io.File

/**
 * The one persisted active location selection. M4-04 supplies town/default catalogue data;
 * this task deliberately keeps only the approved stable identifiers and no coordinates for it.
 */
sealed interface PersistedLocationSelection {
    data class Device(
        val coordinates: GeoCoordinates,
        val accuracyMeters: Double,
        val permissionPrecision: PermissionPrecision,
        val acquiredAtEpochMillis: Long,
        val source: LocationSource,
    ) : PersistedLocationSelection

    data class ManualTown(
        val townId: String,
        val selectedAtEpochMillis: Long,
    ) : PersistedLocationSelection

    /**
     * M4-04 has not approved a catalogue/default version yet, so provenance is intentionally
     * absent until supplied by that task. A later approved value is retained without coordinates.
     */
    data class Default(
        val datasetVersion: String? = null,
    ) : PersistedLocationSelection
}

/** A recovery warning is returned once, without retaining an error history. */
enum class LocationSelectionRecoveryWarning {
    RESET_TO_DEFAULT,
}

data class PersistedLocationSelectionRead(
    val selection: PersistedLocationSelection,
    val recoveryWarning: LocationSelectionRecoveryWarning? = null,
)

/**
 * Application-scoped DataStore boundary for ADR-005. It stores one discriminated record and
 * deliberately has no schedule, zone, town-coordinate, or location-history fields.
 */
class LocationSelectionStore private constructor(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun read(): PersistedLocationSelectionRead {
        val decoded = LocationSelectionCodec.decode(dataStore.updateData { it })
        val recovered = if (decoded.requiresRewrite) {
            dataStore.edit { preferences ->
                LocationSelectionCodec.writeDefault(
                    preferences = preferences,
                    recoveryWarning = LocationSelectionRecoveryWarning.RESET_TO_DEFAULT,
                )
            }
            decoded.copy(
                selection = PersistedLocationSelection.Default(),
                recoveryWarning = LocationSelectionRecoveryWarning.RESET_TO_DEFAULT,
                requiresRewrite = false,
            )
        } else {
            decoded
        }

        // A warning is a one-time recovery signal, not persisted error history.
        if (recovered.recoveryWarning != null) {
            dataStore.edit { it.remove(LocationSelectionPreferences.recoveryWarning) }
        }
        return PersistedLocationSelectionRead(recovered.selection, recovered.recoveryWarning)
    }

    suspend fun selectDevice(selection: PersistedLocationSelection.Device): Boolean {
        if (!LocationSelectionCodec.isValid(selection)) return false
        dataStore.edit { LocationSelectionCodec.writeDevice(it, selection) }
        return true
    }

    suspend fun selectManualTown(townId: String, selectedAtEpochMillis: Long): Boolean {
        val selection = PersistedLocationSelection.ManualTown(townId, selectedAtEpochMillis)
        if (!LocationSelectionCodec.isValid(selection)) return false
        dataStore.edit { LocationSelectionCodec.writeManualTown(it, selection) }
        return true
    }

    suspend fun selectDefault(datasetVersion: String? = null): Boolean {
        val selection = PersistedLocationSelection.Default(datasetVersion)
        if (!LocationSelectionCodec.isValid(selection)) return false
        dataStore.edit { LocationSelectionCodec.writeDefault(it, selection) }
        return true
    }

    /** Deletes the entire record. The absence of a record resolves to the labelled default. */
    suspend fun resetLocationData() {
        dataStore.edit { LocationSelectionCodec.clearRecord(it) }
    }

    companion object {
        private val applicationStoreLock = Any()

        @Volatile
        private var applicationStore: LocationSelectionStore? = null

        fun applicationScoped(context: Context): LocationSelectionStore {
            applicationStore?.let { return it }
            return synchronized(applicationStoreLock) {
                applicationStore ?: create(locationDataStoreFile(context.applicationContext)).also {
                    applicationStore = it
                }
            }
        }

        internal fun create(file: File): LocationSelectionStore = LocationSelectionStore(
            PreferenceDataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler {
                    LocationSelectionCodec.corruptionReplacementPreferences()
                },
                migrations = listOf(LocationSelectionDataMigration),
                produceFile = { file },
            ),
        )

        fun locationDataStoreFile(context: Context): File {
            check(!context.isDeviceProtectedStorage) {
                "Location selection storage must use credential-protected application storage."
            }
            val directory = File(context.noBackupFilesDir, LOCATION_DATA_DIRECTORY_NAME)
            check(directory.exists() || directory.mkdirs()) {
                "Unable to create the location selection no-backup directory."
            }
            return File(directory, LOCATION_DATA_FILE_NAME)
        }
    }
}

internal const val LOCATION_DATA_DIRECTORY_NAME = "location-data"
internal const val LOCATION_DATA_FILE_NAME = "location.preferences_pb"

/** Pure, versioned normalization: old/unknown/invalid records are minimized to the default. */
internal object LocationSelectionDataMigration : DataMigration<Preferences> {
    override suspend fun shouldMigrate(currentData: Preferences): Boolean =
        currentData.asMap().isNotEmpty() && LocationSelectionCodec.decode(currentData).requiresRewrite

    override suspend fun migrate(currentData: Preferences): Preferences =
        LocationSelectionCodec.normalized(currentData)

    override suspend fun cleanUp() = Unit
}

internal object LocationSelectionPreferences {
    val schemaVersion = longPreferencesKey("location_schema_version")
    val activeMode = stringPreferencesKey("location_active_mode")
    val latitudeDegrees = doublePreferencesKey("location_device_latitude_degrees")
    val longitudeDegrees = doublePreferencesKey("location_device_longitude_degrees")
    val accuracyMeters = doublePreferencesKey("location_device_accuracy_meters")
    val permissionPrecision = stringPreferencesKey("location_device_permission_precision")
    val acquiredAtEpochMillis = longPreferencesKey("location_device_acquired_at_epoch_millis")
    val deviceSource = stringPreferencesKey("location_device_source")
    val townId = stringPreferencesKey("location_manual_town_id")
    val selectedAtEpochMillis = longPreferencesKey("location_manual_selected_at_epoch_millis")
    val defaultDatasetVersion = stringPreferencesKey("location_default_dataset_version")
    val recoveryWarning = stringPreferencesKey("location_recovery_warning")

    val selectionKeys = setOf(
        schemaVersion,
        activeMode,
        latitudeDegrees,
        longitudeDegrees,
        accuracyMeters,
        permissionPrecision,
        acquiredAtEpochMillis,
        deviceSource,
        townId,
        selectedAtEpochMillis,
        defaultDatasetVersion,
    )
}

internal object LocationSelectionCodec {
    const val CURRENT_SCHEMA_VERSION = 1L

    private enum class StoredMode {
        DEVICE,
        MANUAL_TOWN,
        DEFAULT,
    }

    fun decode(preferences: Preferences): DecodedLocationSelection {
        val recoveryWarning = preferences[LocationSelectionPreferences.recoveryWarning]
            ?.takeIf { it == LocationSelectionRecoveryWarning.RESET_TO_DEFAULT.name }
            ?.let { LocationSelectionRecoveryWarning.RESET_TO_DEFAULT }
        val schemaVersion = preferences[LocationSelectionPreferences.schemaVersion]
        val hasNonRecoveryValues = preferences.asMap().keys.any {
            it != LocationSelectionPreferences.recoveryWarning
        }

        if (schemaVersion == null && !hasNonRecoveryValues) {
            return DecodedLocationSelection(PersistedLocationSelection.Default(), recoveryWarning)
        }
        if (schemaVersion != CURRENT_SCHEMA_VERSION) {
            return invalid(recoveryWarning)
        }

        return when (preferences[LocationSelectionPreferences.activeMode]) {
            StoredMode.DEVICE.name -> decodeDevice(preferences, recoveryWarning)
            StoredMode.MANUAL_TOWN.name -> decodeManualTown(preferences, recoveryWarning)
            StoredMode.DEFAULT.name -> decodeDefault(preferences, recoveryWarning)
            else -> invalid(recoveryWarning)
        }
    }

    fun normalized(preferences: Preferences): Preferences {
        val decoded = decode(preferences)
        if (!decoded.requiresRewrite) return preferences
        return corruptionReplacementPreferences()
    }

    fun writeDevice(preferences: MutablePreferences, selection: PersistedLocationSelection.Device) {
        clearSelection(preferences)
        preferences[LocationSelectionPreferences.schemaVersion] = CURRENT_SCHEMA_VERSION
        preferences[LocationSelectionPreferences.activeMode] = StoredMode.DEVICE.name
        preferences[LocationSelectionPreferences.latitudeDegrees] = selection.coordinates.latitudeDegrees
        preferences[LocationSelectionPreferences.longitudeDegrees] = selection.coordinates.longitudeDegrees
        preferences[LocationSelectionPreferences.accuracyMeters] = selection.accuracyMeters
        preferences[LocationSelectionPreferences.permissionPrecision] = selection.permissionPrecision.name
        preferences[LocationSelectionPreferences.acquiredAtEpochMillis] = selection.acquiredAtEpochMillis
        preferences[LocationSelectionPreferences.deviceSource] = selection.source.name
    }

    fun writeManualTown(preferences: MutablePreferences, selection: PersistedLocationSelection.ManualTown) {
        clearSelection(preferences)
        preferences[LocationSelectionPreferences.schemaVersion] = CURRENT_SCHEMA_VERSION
        preferences[LocationSelectionPreferences.activeMode] = StoredMode.MANUAL_TOWN.name
        preferences[LocationSelectionPreferences.townId] = selection.townId
        preferences[LocationSelectionPreferences.selectedAtEpochMillis] = selection.selectedAtEpochMillis
    }

    fun writeDefault(
        preferences: MutablePreferences,
        selection: PersistedLocationSelection.Default = PersistedLocationSelection.Default(),
        recoveryWarning: LocationSelectionRecoveryWarning? = null,
    ) {
        clearSelection(preferences)
        preferences[LocationSelectionPreferences.schemaVersion] = CURRENT_SCHEMA_VERSION
        preferences[LocationSelectionPreferences.activeMode] = StoredMode.DEFAULT.name
        selection.datasetVersion?.let {
            preferences[LocationSelectionPreferences.defaultDatasetVersion] = it
        }
        if (recoveryWarning != null) {
            preferences[LocationSelectionPreferences.recoveryWarning] = recoveryWarning.name
        }
    }

    fun isValid(selection: PersistedLocationSelection): Boolean = when (selection) {
        is PersistedLocationSelection.Device ->
            selection.coordinates.latitudeDegrees.isFinite() &&
                selection.coordinates.longitudeDegrees.isFinite() &&
                selection.coordinates.latitudeDegrees in -90.0..90.0 &&
                selection.coordinates.longitudeDegrees in -180.0..180.0 &&
                selection.accuracyMeters.isFinite() &&
                selection.accuracyMeters in 0.0..LocationPolicy.MAXIMUM_USABLE_ACCURACY_METERS &&
                selection.acquiredAtEpochMillis >= 0L &&
                selection.source in setOf(LocationSource.CURRENT_DEVICE, LocationSource.SAVED_DEVICE)

        is PersistedLocationSelection.ManualTown ->
            selection.townId.isNotBlank() && selection.selectedAtEpochMillis >= 0L

        is PersistedLocationSelection.Default ->
            selection.datasetVersion == null || selection.datasetVersion.isNotBlank()
    }

    private fun decodeDevice(
        preferences: Preferences,
        warning: LocationSelectionRecoveryWarning?,
    ): DecodedLocationSelection {
        val latitude = preferences[LocationSelectionPreferences.latitudeDegrees]
        val longitude = preferences[LocationSelectionPreferences.longitudeDegrees]
        val accuracy = preferences[LocationSelectionPreferences.accuracyMeters]
        val precision = preferences[LocationSelectionPreferences.permissionPrecision]
            ?.let { runCatching { PermissionPrecision.valueOf(it) }.getOrNull() }
        val acquiredAt = preferences[LocationSelectionPreferences.acquiredAtEpochMillis]
        val source = preferences[LocationSelectionPreferences.deviceSource]
            ?.let { runCatching { LocationSource.valueOf(it) }.getOrNull() }
        val selection = if (latitude != null && longitude != null && accuracy != null && precision != null &&
            acquiredAt != null && source != null
        ) {
            PersistedLocationSelection.Device(
                GeoCoordinates(latitude, longitude),
                accuracy,
                precision,
                acquiredAt,
                source,
            )
        } else {
            return invalid(warning)
        }
        return if (hasOnlyPayloadKeys(preferences, devicePayloadKeys) && isValid(selection)) {
            DecodedLocationSelection(selection, warning)
        } else {
            invalid(warning)
        }
    }

    private fun decodeManualTown(
        preferences: Preferences,
        warning: LocationSelectionRecoveryWarning?,
    ): DecodedLocationSelection {
        val townId = preferences[LocationSelectionPreferences.townId] ?: return invalid(warning)
        val selectedAt = preferences[LocationSelectionPreferences.selectedAtEpochMillis] ?: return invalid(warning)
        val selection = PersistedLocationSelection.ManualTown(townId, selectedAt)
        return if (hasOnlyPayloadKeys(preferences, manualTownPayloadKeys) && isValid(selection)) {
            DecodedLocationSelection(selection, warning)
        } else {
            invalid(warning)
        }
    }

    private fun decodeDefault(
        preferences: Preferences,
        warning: LocationSelectionRecoveryWarning?,
    ): DecodedLocationSelection {
        val selection = PersistedLocationSelection.Default(
            preferences[LocationSelectionPreferences.defaultDatasetVersion],
        )
        return if (hasOnlyPayloadKeys(preferences, defaultPayloadKeys) && isValid(selection)) {
            DecodedLocationSelection(selection, warning)
        } else {
            invalid(warning)
        }
    }

    /** Used by DataStore corruption recovery and pure migration normalization. */
    fun corruptionReplacementPreferences(): Preferences = preferencesOf(
        LocationSelectionPreferences.schemaVersion to CURRENT_SCHEMA_VERSION,
        LocationSelectionPreferences.activeMode to StoredMode.DEFAULT.name,
        LocationSelectionPreferences.recoveryWarning to LocationSelectionRecoveryWarning.RESET_TO_DEFAULT.name,
    )

    private fun hasOnlyPayloadKeys(
        preferences: Preferences,
        payloadKeys: Set<Preferences.Key<*>>,
    ): Boolean = preferences.asMap().keys.all { key ->
        key == LocationSelectionPreferences.recoveryWarning || key in payloadKeys
    }

    private val devicePayloadKeys = setOf(
        LocationSelectionPreferences.schemaVersion,
        LocationSelectionPreferences.activeMode,
        LocationSelectionPreferences.latitudeDegrees,
        LocationSelectionPreferences.longitudeDegrees,
        LocationSelectionPreferences.accuracyMeters,
        LocationSelectionPreferences.permissionPrecision,
        LocationSelectionPreferences.acquiredAtEpochMillis,
        LocationSelectionPreferences.deviceSource,
    )

    private val manualTownPayloadKeys = setOf(
        LocationSelectionPreferences.schemaVersion,
        LocationSelectionPreferences.activeMode,
        LocationSelectionPreferences.townId,
        LocationSelectionPreferences.selectedAtEpochMillis,
    )

    private val defaultPayloadKeys = setOf(
        LocationSelectionPreferences.schemaVersion,
        LocationSelectionPreferences.activeMode,
        LocationSelectionPreferences.defaultDatasetVersion,
    )

    private fun invalid(warning: LocationSelectionRecoveryWarning?) = DecodedLocationSelection(
        selection = PersistedLocationSelection.Default(),
        recoveryWarning = warning ?: LocationSelectionRecoveryWarning.RESET_TO_DEFAULT,
        requiresRewrite = true,
    )

    private fun clearSelection(preferences: MutablePreferences) {
        // This is a dedicated DataStore, so deleting all keys prevents an obsolete payload from
        // surviving a mode switch or a future replacement-file artifact.
        clearRecord(preferences)
    }

    /** Pure reset seam used by the DataStore transaction and T-FR-007 deletion coverage. */
    fun clearRecord(preferences: MutablePreferences) {
        preferences.clear()
    }
}

internal data class DecodedLocationSelection(
    val selection: PersistedLocationSelection,
    val recoveryWarning: LocationSelectionRecoveryWarning? = null,
    val requiresRewrite: Boolean = false,
)
