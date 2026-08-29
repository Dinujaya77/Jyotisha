package io.github.dinujaya77.jyotisha

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShellNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun launcherAndTopLevelNavigationUseStableDestinations() {
        composeRule.onNodeWithTag("navigation_bottom").assertIsDisplayed()
        composeRule.onAllNodesWithTag("navigation_rail").assertCountEquals(0)
        composeRule.onNodeWithTag("screen_dashboard").assertIsDisplayed()
        composeRule.onNodeWithTag("nav_dashboard").assertIsSelected()
        composeRule.onNodeWithTag("nav_timeline").performClick()
        composeRule.onNodeWithTag("screen_timeline").assertIsDisplayed()
        composeRule.onNodeWithTag("nav_method").performClick()
        composeRule.onNodeWithTag("screen_method").assertIsDisplayed()
        composeRule.activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
        composeRule.onNodeWithTag("screen_dashboard").assertIsDisplayed()
    }

    @Test
    fun childRoutesReturnToTheirBoundedParents() {
        composeRule.onNodeWithTag("action_location").performScrollTo().performClick()
        assertSettledLiveLocationRoute()
        composeRule.onNodeWithTag("action_back").performScrollTo().performClick()
        composeRule.onNodeWithTag("screen_dashboard").assertIsDisplayed()

        composeRule.onNodeWithTag("action_settings").performScrollTo().performClick()
        composeRule.onNodeWithTag("screen_settings").assertIsDisplayed()
        composeRule.onNodeWithTag("action_location").performScrollTo().performClick()
        assertSettledLiveLocationRoute()
        composeRule.onNodeWithTag("action_back").performScrollTo().performClick()
        composeRule.onNodeWithTag("screen_settings").assertIsDisplayed()
        composeRule.onNodeWithTag("action_about").performScrollTo().performClick()
        composeRule.onNodeWithTag("screen_about").assertIsDisplayed()
        composeRule.onNodeWithTag("action_back_to_settings").performScrollTo().performClick()
        composeRule.onNodeWithTag("screen_settings").assertIsDisplayed()
    }

    @Test
    fun recreationRetainsDestinationState() {
        composeRule.onNodeWithTag("nav_method").performClick()
        composeRule.activityRule.scenario.recreate()
        composeRule.onNodeWithTag("screen_method").assertIsDisplayed()
    }

    @Test
    fun recreationRetainsNestedChildAndExactOrigin() {
        composeRule.onNodeWithTag("nav_method").performClick()
        composeRule.onNodeWithTag("action_settings").performScrollTo().performClick()
        composeRule.onNodeWithTag("action_location").performScrollTo().performClick()

        composeRule.activityRule.scenario.recreate()
        assertSettledLiveLocationRoute()
        composeRule.onNodeWithTag("action_back").performScrollTo().performClick()
        composeRule.onNodeWithTag("screen_settings").assertIsDisplayed()
        composeRule.onNodeWithTag("action_back").performScrollTo().performClick()
        composeRule.onNodeWithTag("screen_method").assertIsDisplayed()
    }

    @Test
    fun landscapeRecreationRetainsDestinationState() {
        composeRule.onNodeWithTag("nav_timeline").performClick()
        val portraitActivity = composeRule.activity
        try {
            portraitActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            composeRule.waitUntil(timeoutMillis = 10_000) {
                composeRule.activity !== portraitActivity &&
                    composeRule.activity.resources.configuration.orientation ==
                    Configuration.ORIENTATION_LANDSCAPE
            }
            composeRule.onNodeWithTag("screen_timeline").assertIsDisplayed()
            composeRule.onNodeWithTag("navigation_rail").assertIsDisplayed()
            composeRule.onAllNodesWithTag("navigation_bottom").assertCountEquals(0)
            composeRule.onNodeWithTag("nav_timeline").assertIsSelected()
        } finally {
            val landscapeActivity = composeRule.activity
            landscapeActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            composeRule.waitUntil(timeoutMillis = 10_000) {
                composeRule.activity !== landscapeActivity &&
                    composeRule.activity.resources.configuration.orientation ==
                    Configuration.ORIENTATION_PORTRAIT
            }
        }
        composeRule.onNodeWithTag("navigation_bottom").assertIsDisplayed()
        composeRule.onAllNodesWithTag("navigation_rail").assertCountEquals(0)
    }

    /**
     * A route is initially composed before the asynchronous local restore completes.  Verify
     * only one of the approved settled contracts, rather than treating that transient shell as
     * the normal-selection contract.
     */
    private fun assertSettledLiveLocationRoute() {
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithTag("screen_first_use").fetchSemanticsNodes().isNotEmpty() ||
                composeRule.onAllNodes(hasTestTag("location_selected") and isSelected())
                    .fetchSemanticsNodes()
                    .isNotEmpty()
        }
        if (composeRule.onAllNodesWithTag("screen_first_use").fetchSemanticsNodes().isNotEmpty()) {
            composeRule.onNodeWithTag("screen_first_use").assertIsDisplayed()
            composeRule.onNodeWithTag("action_first_use_choose_town").assertIsDisplayed()
            composeRule.onNodeWithTag("action_first_use_default").assertIsDisplayed()
        } else {
            composeRule.onNodeWithTag("screen_location").assertIsDisplayed()
            composeRule.onNodeWithTag("location_selected").assertIsSelected()
            composeRule.onNodeWithTag("action_use_current").assertIsDisplayed()
        }
    }
}
