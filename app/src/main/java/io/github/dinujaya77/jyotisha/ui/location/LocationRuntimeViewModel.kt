package io.github.dinujaya77.jyotisha.ui.location

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.dinujaya77.jyotisha.data.location.LocationSelectionRepository
import io.github.dinujaya77.jyotisha.data.location.LocationSelectionStore
import io.github.dinujaya77.jyotisha.domain.location.ForegroundLocationPermission
import io.github.dinujaya77.jyotisha.platform.location.AndroidDeviceLocationProvider
import java.util.concurrent.Executor

/** Activity-retained owner for M4 foreground work; it deliberately has no Activity reference. */
internal class LocationRuntimeViewModel(
    applicationContext: Context,
    private val permission: () -> ForegroundLocationPermission,
) : ViewModel() {
    var state by mutableStateOf(LocationRuntimeState())
        private set

    val controller = LocationRuntimeController(
        repository = LocationSelectionRepository(
            persistence = LocationSelectionStore.applicationScoped(applicationContext),
            deviceLocationProvider = AndroidDeviceLocationProvider.create(applicationContext),
        ),
        permission = permission,
        mainExecutor = Executor { runnable -> Handler(Looper.getMainLooper()).post(runnable) },
        onState = { state = it },
    )

    init {
        // One initial local reconciliation for this Activity-retained owner; recreation must not
        // supersede an already active one-shot request.
        controller.restore()
    }

    override fun onCleared() {
        controller.close()
    }

    companion object {
        fun factory(
            applicationContext: Context,
            permission: () -> ForegroundLocationPermission,
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                LocationRuntimeViewModel(applicationContext, permission) as T
        }
    }
}
