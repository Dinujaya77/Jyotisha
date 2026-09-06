package io.github.dinujaya77.jyotisha

import org.junit.Assert.assertEquals
import org.junit.Test

class ShellNavigationTest {
    @Test
    fun initialDestinationIsDashboard() {
        assertEquals(ShellState(), ShellState())
    }

    @Test
    fun topLevelSwitchingIsBoundedAndRepeatedSelectionIsANoOp() {
        val timeline = reduceShellState(
            ShellState(),
            ShellAction.SelectTopLevel(TopLevelDestination.Timeline),
        )

        assertEquals(ShellState(topLevel = TopLevelDestination.Timeline), timeline)
        assertEquals(
            timeline,
            reduceShellState(
                timeline,
                ShellAction.SelectTopLevel(TopLevelDestination.Timeline),
            ),
        )
        assertEquals(
            ShellState(topLevel = TopLevelDestination.Method),
            reduceShellState(
                timeline,
                ShellAction.SelectTopLevel(TopLevelDestination.Method),
            ),
        )
    }

    @Test
    fun selectingTopLevelFromChildReturnsToRequestedRootWithoutHistory() {
        val locationFromDashboard = reduceShellState(ShellState(), ShellAction.OpenLocation)
        val settingsFromTimeline = reduceShellState(
            ShellState(topLevel = TopLevelDestination.Timeline),
            ShellAction.OpenSettings,
        )
        val aboutThroughSettingsFromMethod = reduceShellState(
            reduceShellState(
                ShellState(topLevel = TopLevelDestination.Method),
                ShellAction.OpenSettings,
            ),
            ShellAction.OpenAbout,
        )
        val cases = listOf(
            Triple(
                locationFromDashboard,
                TopLevelDestination.Timeline,
                ShellState(topLevel = TopLevelDestination.Timeline),
            ),
            Triple(
                settingsFromTimeline,
                TopLevelDestination.Dashboard,
                ShellState(),
            ),
            Triple(
                aboutThroughSettingsFromMethod,
                TopLevelDestination.Method,
                ShellState(topLevel = TopLevelDestination.Method),
            ),
        )

        cases.forEach { (childState, destination, expectedRoot) ->
            val action = ShellAction.SelectTopLevel(destination)
            val firstResult = reduceShellState(childState, action)

            assertEquals(expectedRoot, firstResult)
            assertEquals(firstResult, reduceShellState(childState, action))
            assertEquals(firstResult, reduceShellState(firstResult, action))
            assertEquals(
                if (destination == TopLevelDestination.Dashboard) expectedRoot else ShellState(),
                reduceShellState(firstResult, ShellAction.Back),
            )
        }
    }

    @Test
    fun eachChildOpensOnlyFromAnApprovedParentAndBackReturnsThere() {
        val dashboard = ShellState()
        val location = reduceShellState(dashboard, ShellAction.OpenLocation)
        assertEquals(ChildDestination.Location, location.child)
        assertEquals(dashboard, reduceShellState(location, ShellAction.Back))

        val method = ShellState(topLevel = TopLevelDestination.Method)
        val settings = reduceShellState(method, ShellAction.OpenSettings)
        assertEquals(ChildDestination.Settings, settings.child)
        assertEquals(method, reduceShellState(settings, ShellAction.Back))

        val about = reduceShellState(settings, ShellAction.OpenAbout)
        assertEquals(ChildDestination.About, about.child)
        assertEquals(settings, reduceShellState(about, ShellAction.Back))

        val locationFromSettings = reduceShellState(settings, ShellAction.OpenLocation)
        assertEquals(LocationParent.Settings, locationFromSettings.locationParent)
        assertEquals(settings, reduceShellState(locationFromSettings, ShellAction.Back))
    }

    @Test
    fun locationFirstUseLinksPreserveTheLocationRouteForBackOrCancel() {
        val location = reduceShellState(ShellState(), ShellAction.OpenLocation)
        val about = reduceShellState(location, ShellAction.OpenAboutFromLocation)

        assertEquals(ChildDestination.About, about.child)
        assertEquals(location, reduceShellState(about, ShellAction.Back))
        assertEquals(ShellState(), reduceShellState(location, ShellAction.Back))
    }

    @Test
    fun backFromNonDefaultTopLevelReturnsDashboard() {
        TopLevelDestination.entries
            .filterNot { it == TopLevelDestination.Dashboard }
            .forEach { destination ->
                assertEquals(
                    ShellState(),
                    reduceShellState(ShellState(topLevel = destination), ShellAction.Back),
                )
            }
    }

    @Test
    fun invalidChildTransitionsCannotCreateAnInvalidDestination() {
        val dashboard = ShellState()
        assertEquals(dashboard, reduceShellState(dashboard, ShellAction.OpenAbout))

        val about = reduceShellState(
            reduceShellState(dashboard, ShellAction.OpenSettings),
            ShellAction.OpenAbout,
        )
        assertEquals(about, reduceShellState(about, ShellAction.OpenLocation))
        assertEquals(about, reduceShellState(about, ShellAction.OpenSettings))
    }

    @Test
    fun reducerIsDeterministicForTheSameStateAndAction() {
        val state = ShellState(topLevel = TopLevelDestination.Method)
        val action = ShellAction.OpenSettings

        assertEquals(
            reduceShellState(state, action),
            reduceShellState(state, action),
        )
    }

    @Test
    fun onlyLogicalLocationRouteExitOwnsCancellation() {
        val location = reduceShellState(ShellState(), ShellAction.OpenLocation)

        assertEquals(
            true,
            isLocationRouteExit(
                location,
                reduceShellState(location, ShellAction.SelectTopLevel(TopLevelDestination.Method)),
            ),
        )
        assertEquals(
            true,
            isLocationRouteExit(
                location,
                reduceShellState(location, ShellAction.OpenAboutFromLocation),
            ),
        )
        assertEquals(
            true,
            isLocationRouteExit(location, ShellState(child = ChildDestination.Settings)),
        )
        assertEquals(false, isLocationRouteExit(location, location))
        // Compact/expanded recomposition and configuration recreation retain the same route.
        assertEquals(false, isLocationRouteExit(location, location.copy()))
        assertEquals(
            false,
            isLocationRouteExit(
                location,
                reduceShellState(
                    reduceShellState(location, ShellAction.OpenAboutFromLocation),
                    ShellAction.Back,
                ),
            ),
        )
    }
}
