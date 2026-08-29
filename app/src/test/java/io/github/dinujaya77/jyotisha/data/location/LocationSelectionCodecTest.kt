package io.github.dinujaya77.jyotisha.data.location

import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.dinujaya77.jyotisha.domain.location.GeoCoordinates
import io.github.dinujaya77.jyotisha.domain.location.LocationPolicy
import io.github.dinujaya77.jyotisha.domain.location.LocationSource
import io.github.dinujaya77.jyotisha.domain.location.PermissionPrecision
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** T-FR-007 / V1-M4-03: pure codec, migration, invalid-state, and minimization coverage. */
class LocationSelectionCodecTest {
    @Test
    fun deviceWriteReplacesManualPayloadAtomically() {
        val preferences = mutablePreferencesOf()
        LocationSelectionCodec.writeManualTown(
            preferences,
            PersistedLocationSelection.ManualTown("public-town-id", 20L),
        )

        val device = deviceSelection()
        LocationSelectionCodec.writeDevice(preferences, device)

        assertEquals(device, LocationSelectionCodec.decode(preferences).selection)
        assertNull(preferences[LocationSelectionPreferences.townId])
        assertNull(preferences[LocationSelectionPreferences.selectedAtEpochMillis])
    }

    @Test
    fun manualAndDefaultWritesDeleteTheDevicePayload() {
        val preferences = mutablePreferencesOf()
        LocationSelectionCodec.writeDevice(preferences, deviceSelection())

        LocationSelectionCodec.writeManualTown(
            preferences,
            PersistedLocationSelection.ManualTown("public-town-id", 20L),
        )
        assertNull(preferences[LocationSelectionPreferences.latitudeDegrees])
        assertNull(preferences[LocationSelectionPreferences.deviceSource])

        LocationSelectionCodec.writeDefault(preferences)
        assertEquals(PersistedLocationSelection.Default(), LocationSelectionCodec.decode(preferences).selection)
        assertEquals(
            setOf(
                LocationSelectionPreferences.schemaVersion,
                LocationSelectionPreferences.activeMode,
            ),
            preferences.asMap().keys,
        )
    }

    @Test
    fun unknownNewerSchemaInvalidStateAndMixedPayloadResetToDefaultWithOneWarning() {
        val newer = mutablePreferencesOf().apply {
            this[LocationSelectionPreferences.schemaVersion] =
                LocationSelectionCodec.CURRENT_SCHEMA_VERSION + 1
            this[LocationSelectionPreferences.activeMode] = "DEVICE"
        }
        val invalid = mutablePreferencesOf().apply {
            LocationSelectionCodec.writeDevice(this, deviceSelection().copy(acquiredAtEpochMillis = -1L))
        }
        val mixedDeviceAndManual = mutablePreferencesOf().apply {
            LocationSelectionCodec.writeDevice(this, deviceSelection())
            this[LocationSelectionPreferences.townId] = "must-not-coexist"
            this[LocationSelectionPreferences.selectedAtEpochMillis] = 11L
        }
        val mixedManualAndDevice = mutablePreferencesOf().apply {
            LocationSelectionCodec.writeManualTown(
                this,
                PersistedLocationSelection.ManualTown("public-town-id", 20L),
            )
            this[LocationSelectionPreferences.latitudeDegrees] = 6.9
        }

        listOf(newer, invalid, mixedDeviceAndManual, mixedManualAndDevice).forEach { preferences ->
            val decoded = LocationSelectionCodec.decode(preferences)
            assertEquals(PersistedLocationSelection.Default(), decoded.selection)
            assertEquals(LocationSelectionRecoveryWarning.RESET_TO_DEFAULT, decoded.recoveryWarning)
            assertTrue(decoded.requiresRewrite)
        }
    }

    @Test
    fun legacySchemaMigrationIsPureAndDropsPayloadRatherThanWideningRetention() {
        val legacy = mutablePreferencesOf().apply {
            this[LocationSelectionPreferences.schemaVersion] = 0L
            this[LocationSelectionPreferences.activeMode] = "DEVICE"
            this[stringPreferencesKey("legacy_coordinate")] = "not-retained"
        }

        val migrated = LocationSelectionCodec.normalized(legacy)

        assertEquals(
            PersistedLocationSelection.Default(TownCatalog.provenance.datasetVersion),
            LocationSelectionCodec.decode(migrated).selection,
        )
        assertEquals(
            LocationSelectionRecoveryWarning.RESET_TO_DEFAULT,
            LocationSelectionCodec.decode(migrated).recoveryWarning,
        )
        assertFalse(migrated.asMap().containsKey(stringPreferencesKey("legacy_coordinate")))
    }

    @Test
    fun inputValidationRejectsNonFiniteCoordinatesAndInvalidManualIds() {
        assertFalse(
            LocationSelectionCodec.isValid(
                deviceSelection().copy(coordinates = GeoCoordinates(Double.NaN, 1.0)),
            ),
        )
        assertFalse(
            LocationSelectionCodec.isValid(PersistedLocationSelection.ManualTown(" ", 1L)),
        )
        assertFalse(LocationSelectionCodec.isValid(PersistedLocationSelection.Default(" ")))
    }

    @Test
    fun deviceAccuracyAcceptsTheM4_01CeilingAndRejectsJustOverIt() {
        val ceiling = LocationPolicy.MAXIMUM_USABLE_ACCURACY_METERS

        assertTrue(LocationSelectionCodec.isValid(deviceSelection().copy(accuracyMeters = ceiling)))
        assertFalse(LocationSelectionCodec.isValid(deviceSelection().copy(accuracyMeters = ceiling + 0.001)))
    }

    @Test
    fun pureResetSeamDeletesTheRecordAndRestoresTheImplicitDefault() {
        val preferences = mutablePreferencesOf()
        LocationSelectionCodec.writeDevice(preferences, deviceSelection())

        LocationSelectionCodec.clearRecord(preferences)

        assertTrue(preferences.asMap().isEmpty())
        assertEquals(PersistedLocationSelection.Default(), LocationSelectionCodec.decode(preferences).selection)
    }

    @Test
    fun corruptionReplacementIsCurrentDefaultWithOnceOnlyRecoveryWarning() {
        val replacement = LocationSelectionCodec.corruptionReplacementPreferences()
        val decoded = LocationSelectionCodec.decode(replacement)

        assertEquals(LocationSelectionCodec.CURRENT_SCHEMA_VERSION, replacement[LocationSelectionPreferences.schemaVersion])
        assertEquals(PersistedLocationSelection.Default(TownCatalog.provenance.datasetVersion), decoded.selection)
        assertEquals(LocationSelectionRecoveryWarning.RESET_TO_DEFAULT, decoded.recoveryWarning)
        assertFalse(decoded.requiresRewrite)
        assertEquals(
            TownCatalog.provenance.datasetVersion,
            replacement[LocationSelectionPreferences.defaultDatasetVersion],
        )
    }

    @Test
    fun defaultProvenanceCanBeStoredWhenAnApprovedCatalogSuppliesIt() {
        val preferences = mutablePreferencesOf()
        val selection = PersistedLocationSelection.Default("synthetic-approved-provenance")

        LocationSelectionCodec.writeDefault(preferences, selection)

        assertEquals(selection, LocationSelectionCodec.decode(preferences).selection)
        assertEquals(
            "synthetic-approved-provenance",
            preferences[LocationSelectionPreferences.defaultDatasetVersion],
        )
    }

    private fun deviceSelection() = PersistedLocationSelection.Device(
        coordinates = GeoCoordinates(6.9, 79.8),
        accuracyMeters = 25.0,
        permissionPrecision = PermissionPrecision.PRECISE,
        acquiredAtEpochMillis = 10L,
        source = LocationSource.CURRENT_DEVICE,
    )
}
