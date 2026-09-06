package io.github.dinujaya77.jyotisha.platform.location

import io.github.dinujaya77.jyotisha.domain.location.DeviceFixUsability
import io.github.dinujaya77.jyotisha.domain.location.DeviceLocationFix
import io.github.dinujaya77.jyotisha.domain.location.ForegroundLocationPermission
import io.github.dinujaya77.jyotisha.domain.location.LocationPolicy
import io.github.dinujaya77.jyotisha.domain.location.ReturnedFixFreshness

data class DeviceLocationRequest(
    val permission: ForegroundLocationPermission,
)

sealed interface DeviceLocationResult {
    data class Success(val fix: DeviceLocationFix) : DeviceLocationResult
    data class Unavailable(val reason: DeviceLocationUnavailableReason) : DeviceLocationResult
}

enum class DeviceLocationUnavailableReason {
    PERMISSION_DENIED,
    SERVICES_DISABLED,
    PROVIDER_UNAVAILABLE,
    TIMEOUT,
    NULL_RESULT,
    SECURITY_EXCEPTION,
    INVALID_PROVIDER,
    INVALID_RESULT,
    OLD_RESULT,
}

interface DeviceLocationProvider {
    fun requestCurrentLocation(
        request: DeviceLocationRequest,
        onResult: (DeviceLocationResult) -> Unit,
    )

    /**
     * Starts a request only while its caller still owns it.  The production implementation
     * evaluates [mayStart] at the platform registration boundary; the default preserves the
     * small test/provider seam for implementations which do not have a separate preflight.
     */
    fun requestCurrentLocation(
        request: DeviceLocationRequest,
        onResult: (DeviceLocationResult) -> Unit,
        mayStart: () -> Boolean,
    ) {
        if (mayStart()) requestCurrentLocation(request, onResult)
    }

    fun cancelActiveRequest()
}

internal interface LocationPlatformCancellation {
    fun cancel()
}

internal data class LocationPlatformState(
    val apiLevel: Int,
    val locationEnabled: Boolean,
    val enabledProviders: Set<String>,
    val bestProvider: String?,
)

internal interface LocationPlatform {
    fun stateFor(permission: ForegroundLocationPermission): LocationPlatformState
    fun createCancellation(): LocationPlatformCancellation
    fun requestCurrentLocation(
        provider: String,
        permission: ForegroundLocationPermission,
        cancellation: LocationPlatformCancellation,
        onLocation: (DeviceLocationFix?) -> Unit,
    )

    fun elapsedRealtimeMillis(): Long
}

internal interface TimeoutHandle {
    fun cancel()
}

internal interface LocationTimeoutScheduler {
    fun schedule(delayMillis: Long, action: () -> Unit): TimeoutHandle
}

internal class OneShotDeviceLocationProvider(
    private val platform: LocationPlatform,
    private val timeoutScheduler: LocationTimeoutScheduler,
) : DeviceLocationProvider {
    private val lock = Any()
    private var nextToken = 0L
    private var activeRequest: ActiveRequest? = null

    override fun requestCurrentLocation(
        request: DeviceLocationRequest,
        onResult: (DeviceLocationResult) -> Unit,
    ) = requestCurrentLocation(request, onResult) { true }

    override fun requestCurrentLocation(
        request: DeviceLocationRequest,
        onResult: (DeviceLocationResult) -> Unit,
        mayStart: () -> Boolean,
    ) {
        cancelActiveRequest()
        if (request.permission == ForegroundLocationPermission.NONE) {
            onResult(DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.PERMISSION_DENIED))
            return
        }

        // Register cancellation before provider preflight.  Lifecycle or supersession work can
        // otherwise cancel while stateFor() is running and leave nothing to cancel.
        val cancellation = platform.createCancellation()
        val token = synchronized(lock) {
            nextToken += 1L
            activeRequest = ActiveRequest(nextToken, cancellation, onResult)
            nextToken
        }
        if (!mayStart()) {
            abandon(token)
            return
        }
        val state = try {
            platform.stateFor(request.permission)
        } catch (_: SecurityException) {
            complete(token, DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.SECURITY_EXCEPTION))
            return
        } catch (_: IllegalArgumentException) {
            complete(token, DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.INVALID_PROVIDER))
            return
        }
        if (!isActive(token)) return
        if (!state.locationEnabled) {
            complete(token, DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.SERVICES_DISABLED))
            return
        }
        val provider = selectProvider(state, request.permission)
        if (provider == null) {
            complete(token, DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.PROVIDER_UNAVAILABLE))
            return
        }
        val timeout = timeoutScheduler.schedule(LocationPolicy.CURRENT_LOCATION_TIMEOUT_MILLIS) {
            complete(
                token,
                DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.TIMEOUT),
            )
        }
        synchronized(lock) {
            val active = activeRequest?.takeIf { it.token == token }
            if (active == null) {
                timeout.cancel()
            } else {
                active.timeout = timeout
            }
        }

        val started = try {
            synchronized(lock) {
                if (activeRequest?.token != token || !mayStart()) {
                    false
                } else {
                    // Holding the same lock as cancellation makes this ownership transition
                    // atomic: cancellation either prevents this call or cancels an already
                    // registered platform request immediately after this block.
                    platform.requestCurrentLocation(provider, request.permission, cancellation) { fix ->
                        complete(token, classifyPlatformResult(fix))
                    }
                    true
                }
            }
        } catch (_: SecurityException) {
            complete(
                token,
                DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.SECURITY_EXCEPTION),
            )
            return
        } catch (_: IllegalArgumentException) {
            complete(
                token,
                DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.INVALID_PROVIDER),
            )
            return
        }
        if (!started) abandon(token)
    }

    override fun cancelActiveRequest() {
        val request = synchronized(lock) {
            activeRequest.also { activeRequest = null }
        }
        request?.timeout?.cancel()
        request?.cancellation?.cancel()
    }

    private fun classifyPlatformResult(fix: DeviceLocationFix?): DeviceLocationResult {
        if (fix == null) {
            return DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.NULL_RESULT)
        }
        if (LocationPolicy.deviceFixUsability(fix) != DeviceFixUsability.USABLE) {
            return DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.INVALID_RESULT)
        }
        return when (
            LocationPolicy.returnedFixFreshness(
                fix.elapsedRealtimeMillis,
                platform.elapsedRealtimeMillis(),
            )
        ) {
            ReturnedFixFreshness.FRESH -> DeviceLocationResult.Success(fix)
            ReturnedFixFreshness.OLD -> {
                DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.OLD_RESULT)
            }

            ReturnedFixFreshness.INVALID_TIMESTAMP -> {
                DeviceLocationResult.Unavailable(DeviceLocationUnavailableReason.INVALID_RESULT)
            }
        }
    }

    private fun complete(token: Long, result: DeviceLocationResult) {
        val request = synchronized(lock) {
            activeRequest?.takeIf { it.token == token }?.also { activeRequest = null }
        } ?: return
        request.timeout?.cancel()
        request.cancellation.cancel()
        request.onResult(result)
    }

    /** Ends a request rejected by the caller-owned start gate without publishing a result. */
    private fun abandon(token: Long) {
        val request = synchronized(lock) {
            activeRequest?.takeIf { it.token == token }?.also { activeRequest = null }
        } ?: return
        request.timeout?.cancel()
        request.cancellation.cancel()
    }

    private fun isActive(token: Long): Boolean = synchronized(lock) {
        activeRequest?.token == token
    }

    private data class ActiveRequest(
        val token: Long,
        val cancellation: LocationPlatformCancellation,
        val onResult: (DeviceLocationResult) -> Unit,
        var timeout: TimeoutHandle? = null,
    )

    companion object {
        private const val API_31 = 31
        internal const val FUSED_PROVIDER = "fused"
        internal const val GPS_PROVIDER = "gps"
        internal const val NETWORK_PROVIDER = "network"
        internal const val PASSIVE_PROVIDER = "passive"

        internal fun selectProvider(
            state: LocationPlatformState,
            permission: ForegroundLocationPermission,
        ): String? {
            if (state.apiLevel >= API_31) {
                if (FUSED_PROVIDER in state.enabledProviders) return FUSED_PROVIDER
                return when (permission) {
                    ForegroundLocationPermission.PRECISE -> GPS_PROVIDER
                        .takeIf { it in state.enabledProviders }

                    ForegroundLocationPermission.APPROXIMATE -> NETWORK_PROVIDER
                        .takeIf { it in state.enabledProviders }

                    ForegroundLocationPermission.NONE -> null
                }
            }
            return state.bestProvider
                ?.takeIf { it != PASSIVE_PROVIDER && it in state.enabledProviders }
        }
    }
}
