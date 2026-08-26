package io.github.dinujaya77.jyotisha

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
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
import io.github.dinujaya77.jyotisha.ui.location.LocationScreen
import io.github.dinujaya77.jyotisha.ui.method.MethodCallbacks
import io.github.dinujaya77.jyotisha.ui.method.MethodScreen
import io.github.dinujaya77.jyotisha.ui.settings.SettingsScreen
import io.github.dinujaya77.jyotisha.ui.timeline.RuntimeTimelinePresentation
import io.github.dinujaya77.jyotisha.ui.timeline.TimelineCallbacks
import io.github.dinujaya77.jyotisha.ui.timeline.TimelineScreen

@Composable
fun JyotishaApp() {
    var state by rememberSaveable(stateSaver = ShellState.Saver) {
        mutableStateOf(ShellState())
    }
    val dispatch: (ShellAction) -> Unit = { action -> state = reduceShellState(state, action) }

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
) {
    when (state.child) {
        ChildDestination.Location -> LocationScreen(
            onBack = { dispatch(ShellAction.Back) },
            innerPadding = innerPadding,
        )

        ChildDestination.Settings -> SettingsScreen(
            openLocation = { dispatch(ShellAction.OpenLocation) },
            openMethod = {
                dispatch(ShellAction.SelectTopLevel(TopLevelDestination.Method))
            },
            openAbout = { dispatch(ShellAction.OpenAbout) },
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
