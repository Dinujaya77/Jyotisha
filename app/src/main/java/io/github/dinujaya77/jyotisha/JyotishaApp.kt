package io.github.dinujaya77.jyotisha

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import io.github.dinujaya77.jyotisha.ui.dashboard.DashboardCallbacks
import io.github.dinujaya77.jyotisha.ui.dashboard.DashboardScreen
import io.github.dinujaya77.jyotisha.ui.dashboard.RuntimeDashboardPresentation

private const val TIMELINE_SCREEN_TAG = "screen_timeline"
private const val METHOD_SCREEN_TAG = "screen_method"
private const val LOCATION_SCREEN_TAG = "screen_location"
private const val SETTINGS_SCREEN_TAG = "screen_settings"
private const val ABOUT_SCREEN_TAG = "screen_about"

@Composable
fun JyotishaApp() {
    var state by rememberSaveable(stateSaver = ShellState.Saver) {
        mutableStateOf(ShellState())
    }
    val dispatch: (ShellAction) -> Unit = { action -> state = reduceShellState(state, action) }

    BackHandler(enabled = state.canHandleBack) {
        dispatch(ShellAction.Back)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                TopLevelDestination.entries.forEach { destination ->
                    val selected = state.topLevel == destination
                    NavigationBarItem(
                        selected = selected,
                        onClick = { dispatch(ShellAction.SelectTopLevel(destination)) },
                        icon = {
                            Text(
                                text = stringResource(destination.shortLabel),
                                modifier = Modifier.clearAndSetSemantics { },
                            )
                        },
                        label = { Text(stringResource(destination.label)) },
                        modifier = Modifier.testTag("nav_${destination.name.lowercase()}"),
                    )
                }
            }
        },
    ) { innerPadding ->
        ShellContent(
            state = state,
            innerPadding = innerPadding,
            dispatch = dispatch,
        )
    }
}

@Composable
private fun ShellContent(
    state: ShellState,
    innerPadding: PaddingValues,
    dispatch: (ShellAction) -> Unit,
) {
    when (state.child) {
        ChildDestination.Location -> PlaceholderScreen(
            title = R.string.screen_location_title,
            tag = LOCATION_SCREEN_TAG,
            innerPadding = innerPadding,
            actions = listOf(
                ScreenAction(R.string.action_back, "action_back") { dispatch(ShellAction.Back) },
            ),
        )

        ChildDestination.Settings -> PlaceholderScreen(
            title = R.string.screen_settings_title,
            tag = SETTINGS_SCREEN_TAG,
            innerPadding = innerPadding,
            actions = listOf(
                ScreenAction(R.string.action_location, "action_location") { dispatch(ShellAction.OpenLocation) },
                ScreenAction(R.string.action_about, "action_about") { dispatch(ShellAction.OpenAbout) },
                ScreenAction(R.string.action_back, "action_back") { dispatch(ShellAction.Back) },
            ),
        )

        ChildDestination.About -> PlaceholderScreen(
            title = R.string.screen_about_title,
            tag = ABOUT_SCREEN_TAG,
            innerPadding = innerPadding,
            actions = listOf(
                ScreenAction(R.string.action_back_to_settings, "action_back_to_settings") {
                    dispatch(ShellAction.Back)
                },
            ),
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

            TopLevelDestination.Timeline -> PlaceholderScreen(
                title = R.string.screen_timeline_title,
                tag = TIMELINE_SCREEN_TAG,
                innerPadding = innerPadding,
            )

            TopLevelDestination.Method -> PlaceholderScreen(
                title = R.string.screen_method_title,
                tag = METHOD_SCREEN_TAG,
                innerPadding = innerPadding,
                actions = listOf(
                    ScreenAction(R.string.action_settings, "action_settings") {
                        dispatch(ShellAction.OpenSettings)
                    },
                ),
            )
        }
    }
}

private data class ScreenAction(
    @StringRes val label: Int,
    val testTag: String,
    val onClick: () -> Unit,
)

@Composable
private fun PlaceholderScreen(
    @StringRes title: Int,
    tag: String,
    innerPadding: PaddingValues,
    actions: List<ScreenAction> = emptyList(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .testTag(tag),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(title),
            modifier = Modifier.semantics { heading() },
        )
        Text(stringResource(R.string.shell_placeholder_body))
        actions.forEach { action ->
            Button(
                onClick = action.onClick,
                modifier = Modifier.testTag(action.testTag),
            ) {
                Text(stringResource(action.label))
            }
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
