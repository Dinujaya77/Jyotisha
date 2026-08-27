package io.github.dinujaya77.jyotisha.platform.location

import io.github.dinujaya77.jyotisha.domain.location.ForegroundLocationPermission
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** V1-M4-02: the platform-call permission gate must match the requested precision. */
class ForegroundPermissionGateTest {
    @Test
    fun preciseRequiresFineGrant() {
        assertTrue(
            ForegroundPermissionGate.hasRequiredGrant(
                ForegroundLocationPermission.PRECISE,
                fineGranted = true,
                coarseGranted = true,
            ),
        )
        assertFalse(
            ForegroundPermissionGate.hasRequiredGrant(
                ForegroundLocationPermission.PRECISE,
                fineGranted = false,
                coarseGranted = true,
            ),
        )
    }

    @Test
    fun approximateRequiresCoarseGrant() {
        assertTrue(
            ForegroundPermissionGate.hasRequiredGrant(
                ForegroundLocationPermission.APPROXIMATE,
                fineGranted = false,
                coarseGranted = true,
            ),
        )
        assertFalse(
            ForegroundPermissionGate.hasRequiredGrant(
                ForegroundLocationPermission.APPROXIMATE,
                fineGranted = true,
                coarseGranted = false,
            ),
        )
    }

    @Test
    fun noneAndRevokedGrantsCannotReachThePlatformCall() {
        assertFalse(
            ForegroundPermissionGate.hasRequiredGrant(
                ForegroundLocationPermission.NONE,
                fineGranted = true,
                coarseGranted = true,
            ),
        )
        assertFalse(
            ForegroundPermissionGate.hasRequiredGrant(
                ForegroundLocationPermission.APPROXIMATE,
                fineGranted = false,
                coarseGranted = false,
            ),
        )
    }
}
