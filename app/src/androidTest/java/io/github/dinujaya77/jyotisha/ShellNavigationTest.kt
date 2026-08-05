package io.github.dinujaya77.jyotisha

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
        composeRule.onNodeWithTag("screen_dashboard").assertIsDisplayed()
        composeRule.onNodeWithTag("nav_timeline").performClick()
        composeRule.onNodeWithTag("screen_timeline").assertIsDisplayed()
        composeRule.onNodeWithTag("nav_method").performClick()
        composeRule.onNodeWithTag("screen_method").assertIsDisplayed()
        composeRule.activityRule.scenario.onActivity { it.onBackPressedDispatcher.onBackPressed() }
        composeRule.onNodeWithTag("screen_dashboard").assertIsDisplayed()
    }

    @Test
    fun childRoutesReturnToTheirBoundedParents() {
        composeRule.onNodeWithTag("action_location").performClick()
        composeRule.onNodeWithTag("screen_location").assertIsDisplayed()
        composeRule.onNodeWithTag("action_back").performClick()
        composeRule.onNodeWithTag("screen_dashboard").assertIsDisplayed()

        composeRule.onNodeWithTag("action_settings").performClick()
        composeRule.onNodeWithTag("screen_settings").assertIsDisplayed()
        composeRule.onNodeWithTag("action_location").performClick()
        composeRule.onNodeWithTag("action_back").performClick()
        composeRule.onNodeWithTag("screen_settings").assertIsDisplayed()
        composeRule.onNodeWithTag("action_about").performClick()
        composeRule.onNodeWithTag("screen_about").assertIsDisplayed()
        composeRule.onNodeWithTag("action_back_to_settings").performClick()
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
        composeRule.onNodeWithTag("action_settings").performClick()
        composeRule.onNodeWithTag("action_location").performClick()

        composeRule.activityRule.scenario.recreate()
        composeRule.onNodeWithTag("screen_location").assertIsDisplayed()
        composeRule.onNodeWithTag("action_back").performClick()
        composeRule.onNodeWithTag("screen_settings").assertIsDisplayed()
        composeRule.onNodeWithTag("action_back").performClick()
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
        } finally {
            val landscapeActivity = composeRule.activity
            landscapeActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            composeRule.waitUntil(timeoutMillis = 10_000) {
                composeRule.activity !== landscapeActivity &&
                    composeRule.activity.resources.configuration.orientation ==
                    Configuration.ORIENTATION_PORTRAIT
            }
        }
    }
}
