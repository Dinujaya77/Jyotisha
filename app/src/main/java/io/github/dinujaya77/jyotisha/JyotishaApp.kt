package io.github.dinujaya77.jyotisha

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import io.github.dinujaya77.jyotisha.ui.about.AboutScreen
import io.github.dinujaya77.jyotisha.ui.dashboard.DashboardCallbacks
import io.github.dinujaya77.jyotisha.ui.dashboard.DashboardScreen
import io.github.dinujaya77.jyotisha.ui.dashboard.RuntimeDashboardPresentation
import io.github.dinujaya77.jyotisha.ui.location.LocationRuntimeController
import io.github.dinujaya77.jyotisha.ui.location.LocationRuntimeState
import io.github.dinujaya77.jyotisha.ui.location.RuntimeLocationScreen
import io.github.dinujaya77.jyotisha.ui.method.MethodCallbacks
import io.github.dinujaya77.jyotisha.ui.method.MethodScreen
import io.github.dinujaya77.jyotisha.ui.settings.SettingsScreen
import io.github.dinujaya77.jyotisha.ui.timeline.RuntimeTimelinePresentation
import io.github.dinujaya77.jyotisha.ui.timeline.TimelineCallbacks
import io.github.dinujaya77.jyotisha.ui.timeline.TimelineScreen
import androidx.core.content.ContextCompat
import io.github.dinujaya77.jyotisha.data.location.LocationSelectionRepository
import io.github.dinujaya77.jyotisha.data.location.LocationSelectionStore
import io.github.dinujaya77.jyotisha.domain.location.ForegroundLocationPermission
import io.github.dinujaya77.jyotisha.platform.location.AndroidDeviceLocationProvider
import io.github.dinujaya77.jyotisha.platform.location.LocationPermissionGrants
import io.github.dinujaya77.jyotisha.platform.location.LocationPermissionPolicy
import java.util.concurrent.Executor

@Composable
fun JyotishaApp() {
    var state by rememberSaveable(stateSaver = ShellState.Saver) {
        mutableStateOf(ShellState())
    }
    val dispatch: (ShellAction) -> Unit = { action -> state = reduceShellState(state, action) }
    val context = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current
    var locationRuntimeState by remember { mutableStateOf(LocationRuntimeState()) }
    val locationController = remember(context) {
        LocationRuntimeController(
            repository = LocationSelectionRepository(
                persistence = LocationSelectionStore.applicationScoped(context),
                deviceLocationProvider = AndroidDeviceLocationProvider.create(context),
            ),
            permission = { foregroundLocationPermission(context) },
            mainExecutor = Executor { runnable -> Handler(Looper.getMainLooper()).post(runnable) },
            onState = { locationRuntimeState = it },
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        locationController.onPermissionResult()
    }
    DisposableEffect(locationController) {
        // Local restore is allowed at launch; it does not request a permission or provider fix.
        locationController.restore()
        onDispose(locationController::close)
    }
    DisposableEffect(locationController, lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> locationController.onBackgrounded()
                Lifecycle.Event.ON_RESUME -> locationController.onForegrounded()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    BackHandler(enabled = state.canHandleBack) {
        dispatch(ShellAction.Back)
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        if (maxWidth < 600.dp) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = { CompactNavigationBar(state, dispatch) },
            ) { innerPadding ->
                ShellContent(
                    state = state,
                    innerPadding = innerPadding,
                    dispatch = dispatch,
                    locationController = locationController,
                    locationRuntimeState = locationRuntimeState,
                    requestLocationPermission = {
                        permissionLauncher.launch(
                            LocationPermissionPolicy.permissionsToRequest(preciseAccessSought = true)
                                .toTypedArray(),
                        )
                    },
                    requestPreciseLocationPermission = {
                        permissionLauncher.launch(
                            LocationPermissionPolicy.permissionsToRequest(preciseAccessSought = true)
                                .toTypedArray(),
                        )
                    },
                )
            }
        } else {
            Row(
                Modifier
                    .fillMaxSize()
                    .semantics { isTraversalGroup = true },
            ) {
                AdaptiveNavigationRail(state, dispatch)
                Scaffold(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .semantics { traversalIndex = 0f },
                ) { innerPadding ->
                    ShellContent(
                        state = state,
                        innerPadding = innerPadding,
                        dispatch = dispatch,
                        locationController = locationController,
                        locationRuntimeState = locationRuntimeState,
                        requestLocationPermission = {
                            permissionLauncher.launch(
                                LocationPermissionPolicy.permissionsToRequest(preciseAccessSought = true)
                                    .toTypedArray(),
                            )
                        },
                        requestPreciseLocationPermission = {
                            permissionLauncher.launch(
                                LocationPermissionPolicy.permissionsToRequest(preciseAccessSought = true)
                                    .toTypedArray(),
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactNavigationBar(
    state: ShellState,
    dispatch: (ShellAction) -> Unit,
) {
    NavigationBar(modifier = Modifier.testTag("navigation_bottom")) {
        TopLevelDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = state.topLevel == destination,
                onClick = { dispatch(ShellAction.SelectTopLevel(destination)) },
                icon = { DestinationIcon(destination) },
                label = { Text(stringResource(destination.label)) },
                modifier = Modifier.testTag("nav_${destination.name.lowercase()}"),
            )
        }
    }
}

@Composable
private fun AdaptiveNavigationRail(
    state: ShellState,
    dispatch: (ShellAction) -> Unit,
) {
    NavigationRail(
        modifier = Modifier
            .testTag("navigation_rail")
            .semantics { traversalIndex = 1f },
    ) {
        TopLevelDestination.entries.forEach { destination ->
            NavigationRailItem(
                selected = state.topLevel == destination,
                onClick = { dispatch(ShellAction.SelectTopLevel(destination)) },
                icon = { DestinationIcon(destination) },
                label = { Text(stringResource(destination.label)) },
                alwaysShowLabel = true,
                modifier = Modifier.testTag("nav_${destination.name.lowercase()}"),
            )
        }
    }
}

@Composable
private fun DestinationIcon(destination: TopLevelDestination) {
    Text(
        text = stringResource(destination.shortLabel),
        modifier = Modifier.clearAndSetSemantics { },
    )
}

@Composable
private fun ShellContent(
    state: ShellState,
    innerPadding: PaddingValues,
    dispatch: (ShellAction) -> Unit,
    locationController: LocationRuntimeController,
    locationRuntimeState: LocationRuntimeState,
    requestLocationPermission: () -> Unit,
    requestPreciseLocationPermission: () -> Unit,
) {
    when (state.child) {
        ChildDestination.Location -> RuntimeLocationRoute(
            controller = locationController,
            state = locationRuntimeState,
            requestLocationPermission = requestLocationPermission,
            requestPreciseLocationPermission = requestPreciseLocationPermission,
            onOpenMethod = {
                dispatch(ShellAction.SelectTopLevel(TopLevelDestination.Method))
            },
            onOpenAbout = { dispatch(ShellAction.OpenAboutFromLocation) },
            onBack = { dispatch(ShellAction.Back) },
            innerPadding = innerPadding,
        )

        ChildDestination.Settings -> SettingsScreen(
            openLocation = { dispatch(ShellAction.OpenLocation) },
            openMethod = {
                dispatch(ShellAction.SelectTopLevel(TopLevelDestination.Method))
            },
            openAbout = { dispatch(ShellAction.OpenAbout) },
            locationSummary = locationSummary(locationRuntimeState),
            onResetLocation = locationController::reset,
            onBack = { dispatch(ShellAction.Back) },
            innerPadding = innerPadding,
        )

        ChildDestination.About -> AboutScreen(
            openMethod = {
                dispatch(ShellAction.SelectTopLevel(TopLevelDestination.Method))
            },
            onBackToSettings = { dispatch(ShellAction.Back) },
            innerPadding = innerPadding,
        )

        null -> when (state.topLevel) {
            TopLevelDestination.Dashboard -> DashboardScreen(
                presentation = RuntimeDashboardPresentation(),
                callbacks = DashboardCallbacks(
                    openLocation = { dispatch(ShellAction.OpenLocation) },
                    openSettings = { dispatch(ShellAction.OpenSettings) },
                    openTimeline = {
                        dispatch(ShellAction.SelectTopLevel(TopLevelDestination.Timeline))
                    },
                    openMethod = {
                        dispatch(ShellAction.SelectTopLevel(TopLevelDestination.Method))
                    },
                ),
                innerPadding = innerPadding,
            )

            TopLevelDestination.Timeline -> TimelineScreen(
                presentation = RuntimeTimelinePresentation(),
                callbacks = TimelineCallbacks(
                    openDashboard = {
                        dispatch(ShellAction.SelectTopLevel(TopLevelDestination.Dashboard))
                    },
                    openMethod = {
                        dispatch(ShellAction.SelectTopLevel(TopLevelDestination.Method))
                    },
                    goToCurrentHora = {},
                ),
                innerPadding = innerPadding,
            )

            TopLevelDestination.Method -> MethodScreen(
                callbacks = MethodCallbacks(
                    openSettings = { dispatch(ShellAction.OpenSettings) },
                ),
                innerPadding = innerPadding,
            )
        }
    }
}

@Composable
private fun RuntimeLocationRoute(
    controller: LocationRuntimeController,
    state: LocationRuntimeState,
    requestLocationPermission: () -> Unit,
    requestPreciseLocationPermission: () -> Unit,
    onOpenMethod: () -> Unit,
    onOpenAbout: () -> Unit,
    onBack: () -> Unit,
    innerPadding: PaddingValues,
) {
    val context = LocalContext.current
    DisposableEffect(controller) {
        onDispose { controller.cancelForRouteExit() }
    }
    RuntimeLocationScreen(
        state = state,
        onUseCurrent = controller::useCurrentLocation,
        onRequestPermission = requestLocationPermission,
        onRequestPrecisePermission = {
            controller.requestPrecisePermissionUpgrade()
            requestPreciseLocationPermission()
        },
        onTownSelected = controller::selectTown,
        onUseDefault = controller::selectDefault,
        onOpenMethod = onOpenMethod,
        onOpenAbout = onOpenAbout,
        onOpenSystemSettings = { reason -> openRequestedLocationSettings(context, reason) },
        onBack = onBack,
        innerPadding = innerPadding,
    )
}

private fun openRequestedLocationSettings(
    context: Context,
    reason: io.github.dinujaya77.jyotisha.platform.location.DeviceLocationUnavailableReason,
) {
    val intent = when (reason) {
        io.github.dinujaya77.jyotisha.platform.location.DeviceLocationUnavailableReason.SERVICES_DISABLED ->
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)

        else -> Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    }
    context.startActivity(intent)
}

@Composable
private fun locationSummary(state: LocationRuntimeState): String = when (state.selection?.fallback) {
    io.github.dinujaya77.jyotisha.data.location.LocationFallback.CURRENT_DEVICE ->
        stringResource(R.string.location_status_current)
    io.github.dinujaya77.jyotisha.data.location.LocationFallback.SAVED_DEVICE ->
        stringResource(R.string.location_status_saved)
    io.github.dinujaya77.jyotisha.data.location.LocationFallback.MANUAL ->
        stringResource(state.selection.selectedTown!!.englishNameResourceKey)
    io.github.dinujaya77.jyotisha.data.location.LocationFallback.DEFAULT,
    null,
    -> stringResource(R.string.location_status_default)
}

private fun foregroundLocationPermission(context: Context): ForegroundLocationPermission =
    LocationPermissionPolicy.classify(
        LocationPermissionGrants(
            coarseGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED,
            fineGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED,
        ),
    )

private val TopLevelDestination.label: Int
    @StringRes get() = when (this) {
        TopLevelDestination.Dashboard -> R.string.navigation_dashboard
        TopLevelDestination.Timeline -> R.string.navigation_timeline
        TopLevelDestination.Method -> R.string.navigation_method
    }

private val TopLevelDestination.shortLabel: Int
    @StringRes get() = when (this) {
        TopLevelDestination.Dashboard -> R.string.navigation_dashboard_short
        TopLevelDestination.Timeline -> R.string.navigation_timeline_short
        TopLevelDestination.Method -> R.string.navigation_method_short
    }
