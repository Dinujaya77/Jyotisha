package io.github.dinujaya77.jyotisha.platform.location

import android.Manifest
import io.github.dinujaya77.jyotisha.domain.location.ForegroundLocationPermission

data class LocationPermissionGrants(
    val coarseGranted: Boolean,
    val fineGranted: Boolean,
)

object LocationPermissionPolicy {
    fun classify(grants: LocationPermissionGrants): ForegroundLocationPermission = when {
        grants.fineGranted -> ForegroundLocationPermission.PRECISE
        grants.coarseGranted -> ForegroundLocationPermission.APPROXIMATE
        else -> ForegroundLocationPermission.NONE
    }

    fun permissionsToRequest(preciseAccessSought: Boolean): List<String> =
        if (preciseAccessSought) {
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        } else {
            listOf(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
}
