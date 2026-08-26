package io.github.dinujaya77.jyotisha.domain.location

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationProvenanceTest {
    private val coordinates = GeoCoordinates(7.0, 80.0)

    @Test
    fun deviceProvenanceRequiresAccuracyPermissionTimeAndExplicitZone() {
        val valid = SelectedLocation(
            coordinates,
            LocationProvenance(
                source = LocationSource.CURRENT_DEVICE,
                zoneId = "Asia/Colombo",
                horizontalAccuracyMeters = 25.0,
                permissionPrecision = PermissionPrecision.PRECISE,
                acquisitionEpochMillis = 1_000L,
            ),
        )
        assertTrue(LocationPolicy.selectedLocationIsValid(valid))
        assertFalse(LocationPolicy.selectedLocationIsValid(valid.copy(provenance = valid.provenance.copy(zoneId = ""))))
        assertFalse(
            LocationPolicy.selectedLocationIsValid(
                valid.copy(provenance = valid.provenance.copy(permissionPrecision = null)),
            ),
        )
        assertFalse(
            LocationPolicy.selectedLocationIsValid(
                valid.copy(provenance = valid.provenance.copy(horizontalAccuracyMeters = Double.NaN)),
            ),
        )
    }

    @Test
    fun manualAndDefaultRequireAsiaColomboAndSourceSpecificProvenance() {
        val manual = SelectedLocation(
            coordinates,
            LocationProvenance(
                source = LocationSource.MANUAL,
                zoneId = LocationPolicy.ASIA_COLOMBO_ZONE_ID,
                displayName = "Synthetic manual location",
                selectionEpochMillis = 1_000L,
            ),
        )
        val default = SelectedLocation(
            coordinates,
            LocationProvenance(
                source = LocationSource.DEFAULT,
                zoneId = LocationPolicy.ASIA_COLOMBO_ZONE_ID,
                displayName = "Synthetic default location",
                datasetVersion = "synthetic-test-v1",
            ),
        )
        assertTrue(LocationPolicy.selectedLocationIsValid(manual))
        assertTrue(LocationPolicy.selectedLocationIsValid(default))
        assertFalse(
            LocationPolicy.selectedLocationIsValid(
                manual.copy(provenance = manual.provenance.copy(zoneId = "UTC")),
            ),
        )
        assertFalse(
            LocationPolicy.selectedLocationIsValid(
                default.copy(provenance = default.provenance.copy(datasetVersion = null)),
            ),
        )
    }
}
