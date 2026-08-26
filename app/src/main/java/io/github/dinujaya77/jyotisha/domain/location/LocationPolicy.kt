package io.github.dinujaya77.jyotisha.domain.location

import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

object LocationPolicy {
    const val CURRENT_LOCATION_TIMEOUT_MILLIS = 20_000L
    const val FRESH_RETURNED_FIX_MAX_AGE_MILLIS = 2L * 60L * 1_000L
    const val SAVED_FIX_STALE_AGE_MILLIS = 24L * 60L * 60L * 1_000L
    const val MATERIAL_MOVEMENT_METERS = 10_000.0
    const val MINIMUM_ACCURACY_IMPROVEMENT_METERS = 50.0
    const val MINIMUM_ACCURACY_IMPROVEMENT_FRACTION = 0.25
    const val LOW_ACCURACY_WARNING_METERS = 10_000.0
    const val MAXIMUM_USABLE_ACCURACY_METERS = 20_000.0
    const val ASIA_COLOMBO_ZONE_ID = "Asia/Colombo"

    private const val EARTH_MEAN_RADIUS_METERS = 6_371_008.8

    fun deviceFixUsability(fix: DeviceLocationFix): DeviceFixUsability = when {
        !fix.coordinates.isValid() -> DeviceFixUsability.INVALID_COORDINATES
        !fix.horizontalAccuracyMeters.isFinite() || fix.horizontalAccuracyMeters < 0.0 -> {
            DeviceFixUsability.INVALID_ACCURACY
        }

        fix.horizontalAccuracyMeters > MAXIMUM_USABLE_ACCURACY_METERS -> {
            DeviceFixUsability.ACCURACY_EXCEEDS_CEILING
        }

        fix.acquisitionEpochMillis < 0L ||
            fix.elapsedRealtimeMillis?.let { it < 0L } == true -> {
            DeviceFixUsability.INVALID_TIMESTAMP
        }

        else -> DeviceFixUsability.USABLE
    }

    fun returnedFixFreshness(
        fixElapsedRealtimeMillis: Long?,
        nowElapsedRealtimeMillis: Long,
    ): ReturnedFixFreshness {
        if (fixElapsedRealtimeMillis == null ||
            fixElapsedRealtimeMillis < 0L ||
            nowElapsedRealtimeMillis < 0L ||
            fixElapsedRealtimeMillis > nowElapsedRealtimeMillis
        ) {
            return ReturnedFixFreshness.INVALID_TIMESTAMP
        }

        val ageMillis = nowElapsedRealtimeMillis - fixElapsedRealtimeMillis
        return if (ageMillis <= FRESH_RETURNED_FIX_MAX_AGE_MILLIS) {
            ReturnedFixFreshness.FRESH
        } else {
            ReturnedFixFreshness.OLD
        }
    }

    fun savedFixAge(
        acquisitionEpochMillis: Long,
        nowEpochMillis: Long,
    ): SavedFixAge {
        if (acquisitionEpochMillis < 0L ||
            nowEpochMillis < 0L ||
            acquisitionEpochMillis > nowEpochMillis
        ) {
            return SavedFixAge.INVALID
        }

        val ageMillis = nowEpochMillis - acquisitionEpochMillis
        return if (ageMillis >= SAVED_FIX_STALE_AGE_MILLIS) {
            SavedFixAge.STALE
        } else {
            SavedFixAge.CURRENT
        }
    }

    fun replacementDecision(
        savedFix: DeviceLocationFix?,
        candidateFix: DeviceLocationFix,
        nowEpochMillis: Long,
    ): ReplacementDecision {
        if (deviceFixUsability(candidateFix) != DeviceFixUsability.USABLE) {
            return ReplacementDecision(false, ReplacementReason.CANDIDATE_UNUSABLE)
        }
        if (savedFix == null) {
            return ReplacementDecision(true, ReplacementReason.NO_SAVED_FIX)
        }
        if (deviceFixUsability(savedFix) != DeviceFixUsability.USABLE) {
            return ReplacementDecision(true, ReplacementReason.SAVED_FIX_UNUSABLE)
        }
        if (savedFixAge(savedFix.acquisitionEpochMillis, nowEpochMillis) != SavedFixAge.CURRENT) {
            return ReplacementDecision(true, ReplacementReason.SAVED_FIX_STALE_OR_INVALID)
        }

        if (distanceMeters(savedFix.coordinates, candidateFix.coordinates) >= MATERIAL_MOVEMENT_METERS) {
            return ReplacementDecision(true, ReplacementReason.MATERIAL_MOVEMENT)
        }

        val improvementMeters = savedFix.horizontalAccuracyMeters - candidateFix.horizontalAccuracyMeters
        val improvementFraction = if (savedFix.horizontalAccuracyMeters > 0.0) {
            improvementMeters / savedFix.horizontalAccuracyMeters
        } else {
            0.0
        }
        if (improvementMeters >= MINIMUM_ACCURACY_IMPROVEMENT_METERS &&
            improvementFraction >= MINIMUM_ACCURACY_IMPROVEMENT_FRACTION
        ) {
            return ReplacementDecision(true, ReplacementReason.MEANINGFUL_ACCURACY_IMPROVEMENT)
        }

        return ReplacementDecision(false, ReplacementReason.NON_MATERIAL_REFRESH)
    }

    fun accuracyAdvisory(fix: DeviceLocationFix): AccuracyAdvisory {
        if (deviceFixUsability(fix) != DeviceFixUsability.USABLE) {
            return AccuracyAdvisory.UNUSABLE
        }
        val approximate = fix.permissionPrecision == PermissionPrecision.APPROXIMATE
        val lowAccuracy = fix.horizontalAccuracyMeters > LOW_ACCURACY_WARNING_METERS
        return when {
            approximate && lowAccuracy -> AccuracyAdvisory.APPROXIMATE_AND_LOW_ACCURACY
            approximate -> AccuracyAdvisory.APPROXIMATE
            lowAccuracy -> AccuracyAdvisory.LOW_ACCURACY
            else -> AccuracyAdvisory.NONE
        }
    }

    fun selectFallback(availability: FallbackAvailability): FallbackSelection = when {
        availability.freshPrecise -> FallbackSelection.FRESH_PRECISE
        availability.freshApproximate -> FallbackSelection.FRESH_APPROXIMATE
        availability.savedDevice -> FallbackSelection.SAVED_DEVICE
        availability.activeManual -> FallbackSelection.ACTIVE_MANUAL
        availability.defaultLocation -> FallbackSelection.DEFAULT
        else -> FallbackSelection.UNAVAILABLE
    }

    fun shouldRecommendManualFallback(context: ManualFallbackContext): Boolean {
        if (context.currentFixAccuracyMeters?.let {
                it.isFinite() && it > LOW_ACCURACY_WARNING_METERS
            } == true
        ) {
            return true
        }
        if (!context.hasUsableCurrentFix && !context.hasUsableSavedFix) {
            return true
        }
        return context.acquisitionFailed && context.savedFixAge != SavedFixAge.CURRENT
    }

    fun mustDeleteSavedDeviceFix(
        savedPrecision: PermissionPrecision,
        currentPermission: ForegroundLocationPermission,
        selectedMode: SelectedLocationMode,
    ): Boolean {
        if (selectedMode != SelectedLocationMode.CURRENT_DEVICE) return true
        return when (savedPrecision) {
            PermissionPrecision.PRECISE -> currentPermission != ForegroundLocationPermission.PRECISE
            PermissionPrecision.APPROXIMATE -> currentPermission == ForegroundLocationPermission.NONE
        }
    }

    fun selectedLocationIsValid(location: SelectedLocation): Boolean {
        if (!location.coordinates.isValid() || location.provenance.zoneId.isBlank()) return false
        val provenance = location.provenance
        return when (provenance.source) {
            LocationSource.CURRENT_DEVICE,
            LocationSource.SAVED_DEVICE,
            -> provenance.horizontalAccuracyMeters?.let {
                it.isFinite() && it in 0.0..MAXIMUM_USABLE_ACCURACY_METERS
            } == true &&
                provenance.permissionPrecision != null &&
                provenance.acquisitionEpochMillis?.let { it >= 0L } == true

            LocationSource.MANUAL ->
                provenance.zoneId == ASIA_COLOMBO_ZONE_ID &&
                    provenance.selectionEpochMillis?.let { it >= 0L } == true

            LocationSource.DEFAULT ->
                provenance.zoneId == ASIA_COLOMBO_ZONE_ID &&
                    !provenance.datasetVersion.isNullOrBlank()
        }
    }

    fun distanceMeters(first: GeoCoordinates, second: GeoCoordinates): Double {
        if (!first.isValid() || !second.isValid()) return Double.NaN
        val latitude1 = Math.toRadians(first.latitudeDegrees)
        val latitude2 = Math.toRadians(second.latitudeDegrees)
        val latitudeDelta = latitude2 - latitude1
        val longitudeDelta = Math.toRadians(second.longitudeDegrees - first.longitudeDegrees)
        val haversine = sin(latitudeDelta / 2.0).let { it * it } +
            cos(latitude1) * cos(latitude2) *
            sin(longitudeDelta / 2.0).let { it * it }
        return 2.0 * EARTH_MEAN_RADIUS_METERS * asin(sqrt(min(1.0, haversine)))
    }

    private fun GeoCoordinates.isValid(): Boolean =
        latitudeDegrees.isFinite() &&
            longitudeDegrees.isFinite() &&
            latitudeDegrees in -90.0..90.0 &&
            longitudeDegrees in -180.0..180.0
}

enum class DeviceFixUsability {
    USABLE,
    INVALID_COORDINATES,
    INVALID_ACCURACY,
    ACCURACY_EXCEEDS_CEILING,
    INVALID_TIMESTAMP,
}

enum class ReturnedFixFreshness {
    FRESH,
    OLD,
    INVALID_TIMESTAMP,
}

enum class SavedFixAge {
    CURRENT,
    STALE,
    INVALID,
}

enum class ReplacementReason {
    NO_SAVED_FIX,
    SAVED_FIX_UNUSABLE,
    SAVED_FIX_STALE_OR_INVALID,
    MATERIAL_MOVEMENT,
    MEANINGFUL_ACCURACY_IMPROVEMENT,
    NON_MATERIAL_REFRESH,
    CANDIDATE_UNUSABLE,
}

data class ReplacementDecision(
    val adoptCandidate: Boolean,
    val reason: ReplacementReason,
)

enum class AccuracyAdvisory {
    NONE,
    APPROXIMATE,
    LOW_ACCURACY,
    APPROXIMATE_AND_LOW_ACCURACY,
    UNUSABLE,
}

data class FallbackAvailability(
    val freshPrecise: Boolean = false,
    val freshApproximate: Boolean = false,
    val savedDevice: Boolean = false,
    val activeManual: Boolean = false,
    val defaultLocation: Boolean = false,
)

enum class FallbackSelection {
    FRESH_PRECISE,
    FRESH_APPROXIMATE,
    SAVED_DEVICE,
    ACTIVE_MANUAL,
    DEFAULT,
    UNAVAILABLE,
}

data class ManualFallbackContext(
    val hasUsableCurrentFix: Boolean,
    val hasUsableSavedFix: Boolean,
    val savedFixAge: SavedFixAge? = null,
    val acquisitionFailed: Boolean = false,
    val currentFixAccuracyMeters: Double? = null,
)
