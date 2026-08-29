package io.github.dinujaya77.jyotisha.ui.location

import io.github.dinujaya77.jyotisha.data.location.LocationSelectionRepository
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
    REQUEST_PERMISSION,
    ACQUIRING,
}

/**
 * Bounded M4 bridge from explicit UI actions to the approved repository. It uses one executor
 * for DataStore/provider work and posts immutable snapshots back to the Compose main thread.
 */
internal class LocationRuntimeController(
    private val repository: LocationSelectionRepository,
    private val permission: () -> ForegroundLocationPermission,
    private val mainExecutor: Executor,
    private val onState: (LocationRuntimeState) -> Unit,
    private val worker: ExecutorService = Executors.newSingleThreadExecutor(),
) {
    private val generation = AtomicLong()

    @Volatile
    private var lastState = LocationRuntimeState()

    fun restore() = submit(LocationAcquisition.IDLE) { repository.restore(permission()) }

    fun useCurrentLocation() {
        if (permission() == ForegroundLocationPermission.NONE) {
            publish(lastState.copy(acquisition = LocationAcquisition.REQUEST_PERMISSION))
        } else {
            submit(LocationAcquisition.ACQUIRING) { repository.refreshCurrentLocation(permission()) }
        }
    }

    fun onPermissionResult() {
        if (permission() == ForegroundLocationPermission.NONE) {
            restore()
        } else {
            useCurrentLocation()
        }
    }

    fun selectTown(townStableId: String) = submit(LocationAcquisition.IDLE) {
        repository.selectManualTown(townStableId)
    }

    fun selectDefault() = submit(LocationAcquisition.IDLE) { repository.selectDefault() }

    fun reset() = submit(LocationAcquisition.IDLE) { repository.resetLocationData() }

    fun cancelForRouteExit() {
        generation.incrementAndGet()
        repository.cancelCurrentLocationRequest()
    }

    fun close() {
        cancelForRouteExit()
        worker.shutdownNow()
    }

    private fun submit(
        acquisition: LocationAcquisition,
        operation: suspend () -> LocationSelectionState,
    ) {
        val token = generation.incrementAndGet()
        publish(lastState.copy(acquisition = acquisition))
        worker.execute {
            operation.startCoroutine(object : Continuation<LocationSelectionState> {
                override val context = EmptyCoroutineContext

                override fun resumeWith(result: Result<LocationSelectionState>) {
                    if (generation.get() == token) {
                        result.getOrNull()?.let { selection ->
                            publish(LocationRuntimeState(selection = selection))
                        }
                    }
                }
            })
        }
    }

    private fun publish(state: LocationRuntimeState) {
        lastState = state
        mainExecutor.execute { onState(state) }
    }
}
