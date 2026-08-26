package io.github.dinujaya77.jyotisha.platform.location

import android.Manifest
import io.github.dinujaya77.jyotisha.domain.location.ForegroundLocationPermission
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationPermissionPolicyTest {
    @Test
    fun permissionClassificationUsesGrantsNotReportedAccuracy() {
        assertEquals(
            ForegroundLocationPermission.PRECISE,
            LocationPermissionPolicy.classify(LocationPermissionGrants(true, true)),
        )
        assertEquals(
            ForegroundLocationPermission.PRECISE,
            LocationPermissionPolicy.classify(LocationPermissionGrants(false, true)),
        )
        assertEquals(
            ForegroundLocationPermission.APPROXIMATE,
            LocationPermissionPolicy.classify(LocationPermissionGrants(true, false)),
        )
        assertEquals(
            ForegroundLocationPermission.NONE,
            LocationPermissionPolicy.classify(LocationPermissionGrants(false, false)),
        )
    }

    @Test
    fun preciseRequestIsJointAndApproximateRequestIsCoarseOnly() {
        assertEquals(
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            LocationPermissionPolicy.permissionsToRequest(preciseAccessSought = true),
        )
        assertEquals(
            listOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            LocationPermissionPolicy.permissionsToRequest(preciseAccessSought = false),
        )
    }
}
