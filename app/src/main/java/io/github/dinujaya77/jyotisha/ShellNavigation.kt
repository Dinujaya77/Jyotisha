package io.github.dinujaya77.jyotisha

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

enum class TopLevelDestination {
    Dashboard,
    Timeline,
    Method,
}

enum class ChildDestination {
    Location,
    Settings,
    About,
}

enum class LocationParent {
    TopLevel,
    Settings,
}

data class ShellState(
    val topLevel: TopLevelDestination = TopLevelDestination.Dashboard,
    val child: ChildDestination? = null,
    val locationParent: LocationParent = LocationParent.TopLevel,
    val aboutReturnsToLocation: Boolean = false,
) {
    val canHandleBack: Boolean
        get() = child != null || topLevel != TopLevelDestination.Dashboard

    companion object {
        val Saver: Saver<ShellState, Any> = listSaver(
            save = { state ->
                listOf(
                    state.topLevel.ordinal,
                    state.child?.ordinal ?: -1,
                    state.locationParent.ordinal,
                    state.aboutReturnsToLocation,
                )
            },
            restore = { values ->
                ShellState(
                    topLevel = TopLevelDestination.entries.getOrElse(values[0] as Int) {
                        TopLevelDestination.Dashboard
                    },
                    child = (values[1] as Int).takeIf { it >= 0 }?.let {
                        ChildDestination.entries.getOrNull(it)
                    },
                    locationParent = LocationParent.entries.getOrElse(values[2] as Int) {
                        LocationParent.TopLevel
                    },
                    aboutReturnsToLocation = values.getOrNull(3) as? Boolean ?: false,
                )
            },
        )
    }
}

sealed interface ShellAction {
    data class SelectTopLevel(val destination: TopLevelDestination) : ShellAction
    data object OpenLocation : ShellAction
    data object OpenSettings : ShellAction
    data object OpenAbout : ShellAction
    data object OpenAboutFromLocation : ShellAction
    data object Back : ShellAction
}

fun reduceShellState(state: ShellState, action: ShellAction): ShellState = when (action) {
    is ShellAction.SelectTopLevel -> {
        if (state.child == null && state.topLevel == action.destination) state
        else ShellState(topLevel = action.destination)
    }

    ShellAction.OpenLocation -> when (state.child) {
        null -> state.copy(
            child = ChildDestination.Location,
            locationParent = LocationParent.TopLevel,
        )

        ChildDestination.Settings -> state.copy(
            child = ChildDestination.Location,
            locationParent = LocationParent.Settings,
        )

        ChildDestination.Location,
        ChildDestination.About,
        -> state
    }

    ShellAction.OpenSettings -> {
        if (state.child == null) state.copy(child = ChildDestination.Settings) else state
    }

    ShellAction.OpenAbout -> {
        if (state.child == ChildDestination.Settings) state.copy(child = ChildDestination.About)
        else state
    }

    ShellAction.OpenAboutFromLocation -> {
        if (state.child == ChildDestination.Location) {
            state.copy(child = ChildDestination.About, aboutReturnsToLocation = true)
        } else {
            state
        }
    }

    ShellAction.Back -> when (state.child) {
        ChildDestination.Location -> when (state.locationParent) {
            LocationParent.TopLevel -> state.copy(child = null)
            LocationParent.Settings -> state.copy(
                child = ChildDestination.Settings,
                locationParent = LocationParent.TopLevel,
            )
        }

        ChildDestination.About -> if (state.aboutReturnsToLocation) {
            state.copy(child = ChildDestination.Location, aboutReturnsToLocation = false)
        } else {
            state.copy(child = ChildDestination.Settings)
        }
        ChildDestination.Settings -> state.copy(child = null)
        null -> if (state.topLevel == TopLevelDestination.Dashboard) {
            state
        } else {
            ShellState()
        }
    }
}
