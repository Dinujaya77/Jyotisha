package io.github.dinujaya77.jyotisha.domain.location

data class GeoCoordinates(
    val latitudeDegrees: Double,
    val longitudeDegrees: Double,
)

enum class PermissionPrecision {
    PRECISE,
    APPROXIMATE,
}

enum class ForegroundLocationPermission {
    NONE,
    APPROXIMATE,
    PRECISE,
}

enum class LocationSource {
    CURRENT_DEVICE,
    SAVED_DEVICE,
    MANUAL,
    DEFAULT,
}

enum class SelectedLocationMode {
    CURRENT_DEVICE,
    MANUAL,
    DEFAULT,
}

data class DeviceLocationFix(
    val coordinates: GeoCoordinates,
    val horizontalAccuracyMeters: Double,
    val permissionPrecision: PermissionPrecision,
    val acquisitionEpochMillis: Long,
    val elapsedRealtimeMillis: Long? = null,
)

data class LocationProvenance(
    val source: LocationSource,
    val zoneId: String,
    val displayName: String? = null,
    val horizontalAccuracyMeters: Double? = null,
    val permissionPrecision: PermissionPrecision? = null,
    val acquisitionEpochMillis: Long? = null,
    val selectionEpochMillis: Long? = null,
    val datasetVersion: String? = null,
)

data class SelectedLocation(
    val coordinates: GeoCoordinates,
    val provenance: LocationProvenance,
)
