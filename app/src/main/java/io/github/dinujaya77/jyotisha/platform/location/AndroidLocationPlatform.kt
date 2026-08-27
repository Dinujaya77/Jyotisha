package io.github.dinujaya77.jyotisha.platform.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.os.CancellationSignal
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.core.location.LocationManagerCompat
import androidx.core.content.ContextCompat
import io.github.dinujaya77.jyotisha.domain.location.DeviceLocationFix
import io.github.dinujaya77.jyotisha.domain.location.ForegroundLocationPermission
import io.github.dinujaya77.jyotisha.domain.location.GeoCoordinates
import io.github.dinujaya77.jyotisha.domain.location.PermissionPrecision
import java.util.concurrent.Executor

class AndroidDeviceLocationProvider private constructor(
    platform: LocationPlatform,
    timeoutScheduler: LocationTimeoutScheduler,
) : DeviceLocationProvider by OneShotDeviceLocationProvider(platform, timeoutScheduler) {
    companion object {
        fun create(context: Context): AndroidDeviceLocationProvider {
            val applicationContext = context.applicationContext
            val locationManager = applicationContext.getSystemService(LocationManager::class.java)
            val handler = Handler(Looper.getMainLooper())
            return AndroidDeviceLocationProvider(
                platform = AndroidLocationPlatform(
                    context = applicationContext,
                    locationManager = locationManager,
                    callbackExecutor = Executor { runnable -> handler.post(runnable) },
                ),
                timeoutScheduler = HandlerLocationTimeoutScheduler(handler),
            )
        }
    }
}

private class AndroidLocationPlatform(
    private val context: Context,
    private val locationManager: LocationManager,
    private val callbackExecutor: Executor,
) : LocationPlatform {
    override fun stateFor(permission: ForegroundLocationPermission): LocationPlatformState {
        val criteria = Criteria().apply {
            accuracy = when (permission) {
                ForegroundLocationPermission.PRECISE -> Criteria.ACCURACY_FINE
                ForegroundLocationPermission.APPROXIMATE,
                ForegroundLocationPermission.NONE,
                -> Criteria.ACCURACY_COARSE
            }
            powerRequirement = Criteria.POWER_LOW
        }
        val enabledProviders = locationManager.getProviders(true).toSet()
        return LocationPlatformState(
            apiLevel = android.os.Build.VERSION.SDK_INT,
            locationEnabled = LocationManagerCompat.isLocationEnabled(locationManager),
            enabledProviders = enabledProviders,
            bestProvider = locationManager.getBestProvider(criteria, true),
        )
    }

    override fun createCancellation(): LocationPlatformCancellation =
        AndroidLocationPlatformCancellation(CancellationSignal())

    override fun requestCurrentLocation(
        provider: String,
        permission: ForegroundLocationPermission,
        cancellation: LocationPlatformCancellation,
        onLocation: (DeviceLocationFix?) -> Unit,
    ) {
        val signal = (cancellation as AndroidLocationPlatformCancellation).signal
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        if (!ForegroundPermissionGate.hasRequiredGrant(permission, fineGranted, coarseGranted)) {
            throw SecurityException("A platform request requires the matching foreground location grant.")
        }
        // The immediate ContextCompat check above establishes the exact permission for this call.
        @SuppressLint("MissingPermission")
        LocationManagerCompat.getCurrentLocation(
            locationManager,
            provider,
            signal,
            callbackExecutor,
        ) { location ->
            onLocation(
                location?.let {
                    DeviceLocationFix(
                        coordinates = GeoCoordinates(it.latitude, it.longitude),
                        horizontalAccuracyMeters = if (it.hasAccuracy()) {
                            it.accuracy.toDouble()
                        } else {
                            Double.NaN
                        },
                        permissionPrecision = when (permission) {
                            ForegroundLocationPermission.PRECISE -> PermissionPrecision.PRECISE
                            ForegroundLocationPermission.APPROXIMATE -> PermissionPrecision.APPROXIMATE
                            ForegroundLocationPermission.NONE -> error(
                                "A platform request requires a foreground location grant",
                            )
                        },
                        acquisitionEpochMillis = it.time,
                        elapsedRealtimeMillis = it.elapsedRealtimeNanos / 1_000_000L,
                    )
                },
            )
        }
    }

    override fun elapsedRealtimeMillis(): Long = SystemClock.elapsedRealtime()
}

internal object ForegroundPermissionGate {
    fun hasRequiredGrant(
        permission: ForegroundLocationPermission,
        fineGranted: Boolean,
        coarseGranted: Boolean,
    ): Boolean = when (permission) {
        ForegroundLocationPermission.PRECISE -> fineGranted
        ForegroundLocationPermission.APPROXIMATE -> coarseGranted
        ForegroundLocationPermission.NONE -> false
    }
}

private class AndroidLocationPlatformCancellation(
    val signal: CancellationSignal,
) : LocationPlatformCancellation {
    override fun cancel() {
        signal.cancel()
    }
}

private class HandlerLocationTimeoutScheduler(
    private val handler: Handler,
) : LocationTimeoutScheduler {
    override fun schedule(delayMillis: Long, action: () -> Unit): TimeoutHandle {
        val runnable = Runnable(action)
        handler.postDelayed(runnable, delayMillis)
        return object : TimeoutHandle {
            override fun cancel() {
                handler.removeCallbacks(runnable)
            }
        }
    }
}
