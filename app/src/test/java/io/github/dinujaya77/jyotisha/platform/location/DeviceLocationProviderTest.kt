package io.github.dinujaya77.jyotisha.platform.location

import io.github.dinujaya77.jyotisha.domain.location.DeviceLocationFix
import io.github.dinujaya77.jyotisha.domain.location.ForegroundLocationPermission
import io.github.dinujaya77.jyotisha.domain.location.GeoCoordinates
import io.github.dinujaya77.jyotisha.domain.location.LocationPolicy
import io.github.dinujaya77.jyotisha.domain.location.PermissionPrecision
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DeviceLocationProviderTest {
    @Test
    fun api31ProviderMatrixPrefersFusedThenPrecisionSpecificProvider() {
        val all = state(apiLevel = 31, providers = setOf("fused", "gps", "network"))
        assertEquals(
            "fused",
            OneShotDeviceLocationProvider.selectProvider(all, ForegroundLocationPermission.PRECISE),
        )
        assertEquals(
            "gps",
            OneShotDeviceLocationProvider.selectProvider(
                all.copy(enabledProviders = setOf("gps", "network")),
                ForegroundLocationPermission.PRECISE,
            ),
        )
        assertEquals(
            "network",
            OneShotDeviceLocationProvider.selectProvider(
                all.copy(enabledProviders = setOf("gps", "network")),
                ForegroundLocationPermission.APPROXIMATE,
            ),
        )
    }

    @Test
    fun api26To30UsesEnabledBestProviderAndNeverPassive() {
        assertEquals(
            "network",
            OneShotDeviceLocationProvider.selectProvider(
                state(apiLevel = 30, providers = setOf("network"), bestProvider = "network"),
                ForegroundLocationPermission.APPROXIMATE,
            ),
        )
        assertEquals(
            null,
            OneShotDeviceLocationProvider.selectProvider(
                state(apiLevel = 26, providers = setOf("passive"), bestProvider = "passive"),
                ForegroundLocationPermission.PRECISE,
            ),
        )
        assertEquals(
            null,
            OneShotDeviceLocationProvider.selectProvider(
                state(apiLevel = 30, providers = setOf("gps"), bestProvider = "network"),
                ForegroundLocationPermission.PRECISE,
            ),
        )
    }

    @Test
    fun deniedDisabledAndProviderAbsentCompleteWithoutPlatformRequest() {
        val denied = fixture()
        denied.provider.requestCurrentLocation(request(ForegroundLocationPermission.NONE), denied.results::add)
        assertUnavailable(denied.results, DeviceLocationUnavailableReason.PERMISSION_DENIED)
        assertEquals(0, denied.platform.requestCount)

        val disabled = fixture(state = state(locationEnabled = false))
        disabled.provider.requestCurrentLocation(request(), disabled.results::add)
        assertUnavailable(disabled.results, DeviceLocationUnavailableReason.SERVICES_DISABLED)
        assertEquals(0, disabled.platform.requestCount)

        val absent = fixture(state = state(providers = emptySet(), bestProvider = null))
        absent.provider.requestCurrentLocation(request(), absent.results::add)
        assertUnavailable(absent.results, DeviceLocationUnavailableReason.PROVIDER_UNAVAILABLE)
        assertEquals(0, absent.platform.requestCount)
    }

    @Test
    fun successfulPreciseAndApproximateResultsKeepPermissionDerivedPrecision() {
        val precise = fixture()
        precise.provider.requestCurrentLocation(request(), precise.results::add)
        precise.platform.deliver(fix(PermissionPrecision.PRECISE))
        assertEquals(PermissionPrecision.PRECISE, precise.success().permissionPrecision)

        val approximate = fixture(
            state = state(providers = setOf("network"), bestProvider = "network"),
        )
        approximate.provider.requestCurrentLocation(
            request(ForegroundLocationPermission.APPROXIMATE),
            approximate.results::add,
        )
        approximate.platform.deliver(fix(PermissionPrecision.APPROXIMATE, accuracy = 25.0))
        assertEquals(PermissionPrecision.APPROXIMATE, approximate.success().permissionPrecision)
    }

    @Test
    fun timeoutIsExactlyTwentySecondsCancelsAndIgnoresLateResult() {
        val fixture = fixture()
        fixture.provider.requestCurrentLocation(request(), fixture.results::add)
        assertEquals(LocationPolicy.CURRENT_LOCATION_TIMEOUT_MILLIS, fixture.scheduler.delayMillis)

        fixture.scheduler.run()
        assertUnavailable(fixture.results, DeviceLocationUnavailableReason.TIMEOUT)
        assertTrue(fixture.platform.lastCancellation!!.cancelled)

        fixture.platform.deliver(fix())
        assertEquals(1, fixture.results.size)
    }

    @Test
    fun explicitCancellationIsSilentAndLateResultIsIgnored() {
        val fixture = fixture()
        fixture.provider.requestCurrentLocation(request(), fixture.results::add)
        fixture.provider.cancelActiveRequest()
        assertTrue(fixture.platform.lastCancellation!!.cancelled)
        assertTrue(fixture.scheduler.cancelled)
        assertTrue(fixture.results.isEmpty())

        fixture.platform.deliver(fix())
        assertTrue(fixture.results.isEmpty())
    }

    @Test
    fun supersedingRequestCancelsFirstAndOnlySecondCanComplete() {
        val fixture = fixture()
        val firstResults = mutableListOf<DeviceLocationResult>()
        fixture.provider.requestCurrentLocation(request(), firstResults::add)
        val firstCallback = fixture.platform.lastCallback!!
        val firstCancellation = fixture.platform.lastCancellation!!

        fixture.provider.requestCurrentLocation(request(), fixture.results::add)
        assertTrue(firstCancellation.cancelled)
        firstCallback(fix())
        assertTrue(firstResults.isEmpty())

        fixture.platform.deliver(fix())
        assertEquals(1, fixture.results.size)
    }

    @Test
    fun duplicatePlatformCallbacksHaveExactlyOneWinner() {
        val fixture = fixture()
        fixture.provider.requestCurrentLocation(request(), fixture.results::add)
        val callback = fixture.platform.lastCallback!!
        callback(fix())
        callback(fix())
        assertEquals(1, fixture.results.size)
    }

    @Test
    fun nullInvalidAndOldFixesMapToExplicitUnavailableResults() {
        val nullFix = fixture()
        nullFix.provider.requestCurrentLocation(request(), nullFix.results::add)
        nullFix.platform.deliver(null)
        assertUnavailable(nullFix.results, DeviceLocationUnavailableReason.NULL_RESULT)

        val invalid = fixture()
        invalid.provider.requestCurrentLocation(request(), invalid.results::add)
        invalid.platform.deliver(fix(accuracy = Double.NaN))
        assertUnavailable(invalid.results, DeviceLocationUnavailableReason.INVALID_RESULT)

        val old = fixture()
        old.provider.requestCurrentLocation(request(), old.results::add)
        old.platform.deliver(
            fix(elapsedRealtimeMillis = old.platform.nowElapsed - 120_001L),
        )
        assertUnavailable(old.results, DeviceLocationUnavailableReason.OLD_RESULT)
    }

    @Test
    fun platformSecurityAndInvalidProviderExceptionsAreContained() {
        val stateSecurity = fixture()
        stateSecurity.platform.stateFailure = SecurityException("synthetic")
        stateSecurity.provider.requestCurrentLocation(request(), stateSecurity.results::add)
        assertUnavailable(stateSecurity.results, DeviceLocationUnavailableReason.SECURITY_EXCEPTION)

        val requestInvalid = fixture()
        requestInvalid.platform.requestFailure = IllegalArgumentException("synthetic")
        requestInvalid.provider.requestCurrentLocation(request(), requestInvalid.results::add)
        assertUnavailable(requestInvalid.results, DeviceLocationUnavailableReason.INVALID_PROVIDER)
    }

    private fun fixture(state: LocationPlatformState = state()): Fixture {
        val platform = FakeLocationPlatform(state)
        val scheduler = FakeScheduler()
        return Fixture(
            provider = OneShotDeviceLocationProvider(platform, scheduler),
            platform = platform,
            scheduler = scheduler,
        )
    }

    private fun state(
        apiLevel: Int = 36,
        locationEnabled: Boolean = true,
        providers: Set<String> = setOf("fused", "gps", "network"),
        bestProvider: String? = "gps",
    ) = LocationPlatformState(apiLevel, locationEnabled, providers, bestProvider)

    private fun request(
        permission: ForegroundLocationPermission = ForegroundLocationPermission.PRECISE,
    ) = DeviceLocationRequest(permission)

    private fun fix(
        precision: PermissionPrecision = PermissionPrecision.PRECISE,
        accuracy: Double = 25.0,
        elapsedRealtimeMillis: Long = 900_000L,
    ) = DeviceLocationFix(
        coordinates = GeoCoordinates(7.0, 80.0),
        horizontalAccuracyMeters = accuracy,
        permissionPrecision = precision,
        acquisitionEpochMillis = 1_000_000L,
        elapsedRealtimeMillis = elapsedRealtimeMillis,
    )

    private fun assertUnavailable(
        results: List<DeviceLocationResult>,
        reason: DeviceLocationUnavailableReason,
    ) {
        assertEquals(
            DeviceLocationResult.Unavailable(reason),
            results.single(),
        )
    }

    private data class Fixture(
        val provider: OneShotDeviceLocationProvider,
        val platform: FakeLocationPlatform,
        val scheduler: FakeScheduler,
        val results: MutableList<DeviceLocationResult> = mutableListOf(),
    ) {
        fun success(): DeviceLocationFix = (results.single() as DeviceLocationResult.Success).fix
    }

    private class FakeCancellation : LocationPlatformCancellation {
        var cancelled = false
        override fun cancel() {
            cancelled = true
        }
    }

    private class FakeLocationPlatform(
        private val state: LocationPlatformState,
    ) : LocationPlatform {
        val nowElapsed = 1_000_000L
        var requestCount = 0
        var stateFailure: RuntimeException? = null
        var requestFailure: RuntimeException? = null
        var lastCancellation: FakeCancellation? = null
        var lastCallback: ((DeviceLocationFix?) -> Unit)? = null

        override fun stateFor(permission: ForegroundLocationPermission): LocationPlatformState {
            stateFailure?.let { throw it }
            return state
        }

        override fun createCancellation(): LocationPlatformCancellation = FakeCancellation().also {
            lastCancellation = it
        }

        override fun requestCurrentLocation(
            provider: String,
            permission: ForegroundLocationPermission,
            cancellation: LocationPlatformCancellation,
            onLocation: (DeviceLocationFix?) -> Unit,
        ) {
            requestCount += 1
            lastCallback = onLocation
            requestFailure?.let { throw it }
        }

        override fun elapsedRealtimeMillis(): Long = nowElapsed

        fun deliver(fix: DeviceLocationFix?) {
            lastCallback!!.invoke(fix)
        }
    }

    private class FakeScheduler : LocationTimeoutScheduler {
        var delayMillis: Long? = null
        var action: (() -> Unit)? = null
        var cancelled = false

        override fun schedule(delayMillis: Long, action: () -> Unit): TimeoutHandle {
            this.delayMillis = delayMillis
            this.action = action
            return object : TimeoutHandle {
                override fun cancel() {
                    cancelled = true
                }
            }
        }

        fun run() {
            assertFalse(cancelled)
            action!!.invoke()
        }
    }
}
