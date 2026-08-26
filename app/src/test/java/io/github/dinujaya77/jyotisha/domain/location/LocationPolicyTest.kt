package io.github.dinujaya77.jyotisha.domain.location

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationPolicyTest {
    private val nowEpochMillis = 2_000_000_000L

    @Test
    fun deviceUsabilityRejectsNonfiniteOutOfRangeAccuracyAndInvalidTime() {
        val invalidCoordinates = listOf(
            GeoCoordinates(Double.NaN, 80.0),
            GeoCoordinates(Double.POSITIVE_INFINITY, 80.0),
            GeoCoordinates(90.000_001, 80.0),
            GeoCoordinates(7.0, -180.000_001),
        )
        invalidCoordinates.forEach { coordinates ->
            assertEquals(
                DeviceFixUsability.INVALID_COORDINATES,
                LocationPolicy.deviceFixUsability(fix(coordinates = coordinates)),
            )
        }

        listOf(Double.NaN, Double.NEGATIVE_INFINITY, -0.001).forEach { accuracy ->
            assertEquals(
                DeviceFixUsability.INVALID_ACCURACY,
                LocationPolicy.deviceFixUsability(fix(accuracyMeters = accuracy)),
            )
        }
        assertEquals(
            DeviceFixUsability.ACCURACY_EXCEEDS_CEILING,
            LocationPolicy.deviceFixUsability(fix(accuracyMeters = 20_000.001)),
        )
        assertEquals(
            DeviceFixUsability.INVALID_TIMESTAMP,
            LocationPolicy.deviceFixUsability(fix(acquisitionEpochMillis = -1L)),
        )
        assertEquals(
            DeviceFixUsability.INVALID_TIMESTAMP,
            LocationPolicy.deviceFixUsability(fix(elapsedRealtimeMillis = -1L)),
        )
    }

    @Test
    fun deviceUsabilityIncludesCoordinateAndAccuracyBoundaries() {
        listOf(
            fix(coordinates = GeoCoordinates(-90.0, -180.0), accuracyMeters = 0.0),
            fix(coordinates = GeoCoordinates(90.0, 180.0), accuracyMeters = 20_000.0),
        ).forEach { candidate ->
            assertEquals(DeviceFixUsability.USABLE, LocationPolicy.deviceFixUsability(candidate))
        }
    }

    @Test
    fun returnedFixFreshnessIncludesTwoMinutesAndRejectsFutureOrMissingMonotonicTime() {
        val nowElapsed = 500_000L
        assertEquals(
            ReturnedFixFreshness.FRESH,
            LocationPolicy.returnedFixFreshness(nowElapsed - 120_000L, nowElapsed),
        )
        assertEquals(
            ReturnedFixFreshness.OLD,
            LocationPolicy.returnedFixFreshness(nowElapsed - 120_001L, nowElapsed),
        )
        assertEquals(
            ReturnedFixFreshness.INVALID_TIMESTAMP,
            LocationPolicy.returnedFixFreshness(nowElapsed + 1L, nowElapsed),
        )
        assertEquals(
            ReturnedFixFreshness.INVALID_TIMESTAMP,
            LocationPolicy.returnedFixFreshness(null, nowElapsed),
        )
    }

    @Test
    fun savedFixBecomesStaleAtExactlyTwentyFourHoursAndFutureIsInvalid() {
        val day = LocationPolicy.SAVED_FIX_STALE_AGE_MILLIS
        assertEquals(SavedFixAge.CURRENT, LocationPolicy.savedFixAge(nowEpochMillis - day + 1L, nowEpochMillis))
        assertEquals(SavedFixAge.STALE, LocationPolicy.savedFixAge(nowEpochMillis - day, nowEpochMillis))
        assertEquals(SavedFixAge.INVALID, LocationPolicy.savedFixAge(nowEpochMillis + 1L, nowEpochMillis))
        assertEquals(SavedFixAge.INVALID, LocationPolicy.savedFixAge(-1L, nowEpochMillis))
    }

    @Test
    fun replacementUsesExactMovementBoundary() {
        val saved = fix(coordinates = GeoCoordinates(0.0, 0.0), accuracyMeters = 100.0)
        val below = fix(
            coordinates = coordinatesAtEquatorialDistance(9_999.0),
            accuracyMeters = 100.0,
        )
        val at = fix(
            coordinates = coordinatesAtEquatorialDistance(10_000.0),
            accuracyMeters = 100.0,
        )

        assertFalse(LocationPolicy.replacementDecision(saved, below, nowEpochMillis).adoptCandidate)
        assertEquals(
            ReplacementReason.MATERIAL_MOVEMENT,
            LocationPolicy.replacementDecision(saved, at, nowEpochMillis).reason,
        )
    }

    @Test
    fun replacementRequiresBothFiftyMetersAndTwentyFivePercentAccuracyImprovement() {
        val saved = fix(accuracyMeters = 200.0)
        assertEquals(
            ReplacementReason.MEANINGFUL_ACCURACY_IMPROVEMENT,
            LocationPolicy.replacementDecision(saved, fix(accuracyMeters = 150.0), nowEpochMillis).reason,
        )
        assertFalse(
            LocationPolicy.replacementDecision(saved, fix(accuracyMeters = 150.001), nowEpochMillis)
                .adoptCandidate,
        )
        assertFalse(
            LocationPolicy.replacementDecision(
                fix(accuracyMeters = 300.0),
                fix(accuracyMeters = 250.0),
                nowEpochMillis,
            ).adoptCandidate,
        )
        assertFalse(
            LocationPolicy.replacementDecision(
                fix(accuracyMeters = 160.0),
                fix(accuracyMeters = 120.0),
                nowEpochMillis,
            ).adoptCandidate,
        )
    }

    @Test
    fun replacementAdoptsWithoutSavedFixAndWhenSavedIsStaleButRejectsBadCandidate() {
        assertEquals(
            ReplacementReason.NO_SAVED_FIX,
            LocationPolicy.replacementDecision(null, fix(), nowEpochMillis).reason,
        )
        val stale = fix(acquisitionEpochMillis = nowEpochMillis - LocationPolicy.SAVED_FIX_STALE_AGE_MILLIS)
        assertEquals(
            ReplacementReason.SAVED_FIX_STALE_OR_INVALID,
            LocationPolicy.replacementDecision(stale, fix(), nowEpochMillis).reason,
        )
        assertEquals(
            ReplacementReason.CANDIDATE_UNUSABLE,
            LocationPolicy.replacementDecision(fix(), fix(accuracyMeters = Double.NaN), nowEpochMillis).reason,
        )
    }

    @Test
    fun advisorySeparatesPermissionPrecisionFromReportedAccuracy() {
        assertEquals(AccuracyAdvisory.NONE, LocationPolicy.accuracyAdvisory(fix(accuracyMeters = 10_000.0)))
        assertEquals(
            AccuracyAdvisory.LOW_ACCURACY,
            LocationPolicy.accuracyAdvisory(fix(accuracyMeters = 10_000.001)),
        )
        assertEquals(
            AccuracyAdvisory.APPROXIMATE,
            LocationPolicy.accuracyAdvisory(
                fix(accuracyMeters = 25.0, precision = PermissionPrecision.APPROXIMATE),
            ),
        )
        assertEquals(
            AccuracyAdvisory.APPROXIMATE_AND_LOW_ACCURACY,
            LocationPolicy.accuracyAdvisory(
                fix(accuracyMeters = 20_000.0, precision = PermissionPrecision.APPROXIMATE),
            ),
        )
        assertEquals(
            AccuracyAdvisory.UNUSABLE,
            LocationPolicy.accuracyAdvisory(fix(accuracyMeters = 20_000.001)),
        )
    }

    @Test
    fun fallbackOrderIsPreciseApproximateSavedManualDefault() {
        val all = FallbackAvailability(
            freshPrecise = true,
            freshApproximate = true,
            savedDevice = true,
            activeManual = true,
            defaultLocation = true,
        )
        assertEquals(FallbackSelection.FRESH_PRECISE, LocationPolicy.selectFallback(all))
        assertEquals(
            FallbackSelection.FRESH_APPROXIMATE,
            LocationPolicy.selectFallback(all.copy(freshPrecise = false)),
        )
        assertEquals(
            FallbackSelection.SAVED_DEVICE,
            LocationPolicy.selectFallback(all.copy(freshPrecise = false, freshApproximate = false)),
        )
        assertEquals(
            FallbackSelection.ACTIVE_MANUAL,
            LocationPolicy.selectFallback(
                all.copy(freshPrecise = false, freshApproximate = false, savedDevice = false),
            ),
        )
        assertEquals(
            FallbackSelection.DEFAULT,
            LocationPolicy.selectFallback(FallbackAvailability(defaultLocation = true)),
        )
        assertEquals(FallbackSelection.UNAVAILABLE, LocationPolicy.selectFallback(FallbackAvailability()))
    }

    @Test
    fun permissionDowngradeAndModeSwitchDeleteOnlyProhibitedSavedDeviceFixes() {
        assertTrue(
            LocationPolicy.mustDeleteSavedDeviceFix(
                PermissionPrecision.PRECISE,
                ForegroundLocationPermission.APPROXIMATE,
                SelectedLocationMode.CURRENT_DEVICE,
            ),
        )
        assertTrue(
            LocationPolicy.mustDeleteSavedDeviceFix(
                PermissionPrecision.APPROXIMATE,
                ForegroundLocationPermission.NONE,
                SelectedLocationMode.CURRENT_DEVICE,
            ),
        )
        assertTrue(
            LocationPolicy.mustDeleteSavedDeviceFix(
                PermissionPrecision.APPROXIMATE,
                ForegroundLocationPermission.APPROXIMATE,
                SelectedLocationMode.MANUAL,
            ),
        )
        assertFalse(
            LocationPolicy.mustDeleteSavedDeviceFix(
                PermissionPrecision.APPROXIMATE,
                ForegroundLocationPermission.PRECISE,
                SelectedLocationMode.CURRENT_DEVICE,
            ),
        )
    }

    @Test
    fun manualFallbackRecommendationMatchesFailureStalenessAndUncertaintyRules() {
        assertTrue(
            LocationPolicy.shouldRecommendManualFallback(
                ManualFallbackContext(false, false, acquisitionFailed = true),
            ),
        )
        assertTrue(
            LocationPolicy.shouldRecommendManualFallback(
                ManualFallbackContext(
                    hasUsableCurrentFix = false,
                    hasUsableSavedFix = true,
                    savedFixAge = SavedFixAge.STALE,
                    acquisitionFailed = true,
                ),
            ),
        )
        assertTrue(
            LocationPolicy.shouldRecommendManualFallback(
                ManualFallbackContext(
                    hasUsableCurrentFix = true,
                    hasUsableSavedFix = false,
                    currentFixAccuracyMeters = 10_000.001,
                ),
            ),
        )
        assertFalse(
            LocationPolicy.shouldRecommendManualFallback(
                ManualFallbackContext(
                    hasUsableCurrentFix = true,
                    hasUsableSavedFix = false,
                    currentFixAccuracyMeters = 10_000.0,
                ),
            ),
        )
    }

    private fun fix(
        coordinates: GeoCoordinates = GeoCoordinates(7.0, 80.0),
        accuracyMeters: Double = 25.0,
        precision: PermissionPrecision = PermissionPrecision.PRECISE,
        acquisitionEpochMillis: Long = nowEpochMillis - 1_000L,
        elapsedRealtimeMillis: Long? = 100_000L,
    ) = DeviceLocationFix(
        coordinates = coordinates,
        horizontalAccuracyMeters = accuracyMeters,
        permissionPrecision = precision,
        acquisitionEpochMillis = acquisitionEpochMillis,
        elapsedRealtimeMillis = elapsedRealtimeMillis,
    )

    private fun coordinatesAtEquatorialDistance(distanceMeters: Double): GeoCoordinates {
        val longitudeDegrees = Math.toDegrees(distanceMeters / 6_371_008.8)
        return GeoCoordinates(0.0, longitudeDegrees)
    }
}
