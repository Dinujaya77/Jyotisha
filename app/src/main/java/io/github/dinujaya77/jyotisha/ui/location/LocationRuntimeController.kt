package io.github.dinujaya77.jyotisha.ui.location

import io.github.dinujaya77.jyotisha.data.location.LocationSelectionOperations
import io.github.dinujaya77.jyotisha.data.location.LocationSelectionState
import io.github.dinujaya77.jyotisha.domain.location.ForegroundLocationPermission
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

/** Immutable UI-facing location work state; calculation publication remains assigned to M7. */
internal data class LocationRuntimeState(
    val selection: LocationSelectionState? = null,
    val acquisition: LocationAcquisition = LocationAcquisition.IDLE,
)

internal enum class LocationAcquisition {
    IDLE,
    RESTORING,
    REQUEST_PERMISSION,
    PERMISSION_UNAVAILABLE,
    ACQUIRING,
    ERROR,
}

/**
 * Bounded M4 bridge from explicit UI actions to the approved repository. It uses one executor
 * for DataStore/provider work and posts immutable snapshots back to the Compose main thread.
 */
internal class LocationRuntimeController(
    private val repository: LocationSelectionOperations,
    private val permission: () -> ForegroundLocationPermission,
    private val mainExecutor: Executor,
    private val onState: (LocationRuntimeState) -> Unit,
    private val worker: ExecutorService = Executors.newSingleThreadExecutor(),
    private val afterCurrentGenerationCheck: (() -> Unit)? = null,
) {
    private val generation = AtomicLong()
    private val operationStartLock = Any()

    @Volatile
    private var lastState = LocationRuntimeState()

    /** Local-only restore/reconciliation; it never starts permission or provider work. */
    fun restore() = submit(LocationAcquisition.RESTORING) { repository.restore(permission()) }

    fun useCurrentLocation() {
        if (permission() == ForegroundLocationPermission.NONE) {
            publish(lastState.copy(acquisition = LocationAcquisition.REQUEST_PERMISSION))
        } else {
            submit(LocationAcquisition.ACQUIRING) { repository.refreshCurrentLocation(permission()) }
        }
    }

    fun onPermissionResult() {
        if (permission() == ForegroundLocationPermission.NONE) {
            // Platform rationale signals cannot reliably distinguish a permanent denial.  Keep
            // a neutral recovery state and wait for a user-triggered retry or Settings action.
            submit(
                acquisition = LocationAcquisition.PERMISSION_UNAVAILABLE,
                completedAcquisition = LocationAcquisition.PERMISSION_UNAVAILABLE,
            ) { repository.restore(permission()) }
        } else {
            useCurrentLocation()
        }
    }

    /** The optional approximate-to-precise request is only exposed after approximate access. */
    fun requestPrecisePermissionUpgrade() {
        if (permission() == ForegroundLocationPermission.APPROXIMATE) {
            publish(lastState.copy(acquisition = LocationAcquisition.REQUEST_PERMISSION))
        }
    }

    fun selectTown(townStableId: String) = submit(
        acquisition = LocationAcquisition.IDLE,
        supersedesForegroundRequest = true,
    ) {
        repository.selectManualTown(townStableId)
    }

    fun selectDefault() = submit(
        acquisition = LocationAcquisition.IDLE,
        supersedesForegroundRequest = true,
    ) { repository.selectDefault() }

    fun reset() = submit(
        acquisition = LocationAcquisition.IDLE,
        supersedesForegroundRequest = true,
    ) { repository.resetLocationData() }

    fun cancelForRouteExit() {
        synchronized(operationStartLock) {
            generation.incrementAndGet()
            repository.cancelCurrentLocationRequest()
        }
        publish(lastState.copy(acquisition = LocationAcquisition.IDLE))
    }

    /** Genuine backgrounding owns foreground cancellation; a retained controller can resume. */
    fun onBackgrounded() = cancelForRouteExit()

    /** Resume reconciles permission/privacy and restored state without silently acquiring. */
    fun onForegrounded() = restore()

    fun close() {
        cancelForRouteExit()
        worker.shutdownNow()
    }

    private fun submit(
        acquisition: LocationAcquisition,
        completedAcquisition: LocationAcquisition = LocationAcquisition.IDLE,
        supersedesForegroundRequest: Boolean = false,
        operation: suspend () -> LocationSelectionState,
    ) {
        val token = synchronized(operationStartLock) {
            generation.incrementAndGet().also {
                if (supersedesForegroundRequest) {
                    // The repository owns provider cancellation.  This must happen on the
                    // caller thread before the replacement selection is queued behind an
                    // active refresh.
                    repository.cancelCurrentLocationRequest()
                }
            }
        }
        publish(lastState.copy(acquisition = acquisition))
        worker.execute {
            synchronized(operationStartLock) {
                if (generation.get() != token) return@execute
                afterCurrentGenerationCheck?.invoke()
                if (generation.get() != token) return@execute
                operation.startCoroutine(object : Continuation<LocationSelectionState> {
                    override val context = EmptyCoroutineContext

                    override fun resumeWith(result: Result<LocationSelectionState>) {
                        if (generation.get() == token) {
                            val selection = result.getOrNull()
                            publish(
                                if (selection == null) {
                                    LocationRuntimeState(
                                        selection = lastState.selection,
                                        acquisition = LocationAcquisition.ERROR,
                                    )
                                } else {
                                    LocationRuntimeState(selection, completedAcquisition)
                                },
                            )
                        }
                    }
                })
            }
        }
    }

    private fun publish(state: LocationRuntimeState) {
        lastState = state
        mainExecutor.execute { onState(state) }
    }
}
