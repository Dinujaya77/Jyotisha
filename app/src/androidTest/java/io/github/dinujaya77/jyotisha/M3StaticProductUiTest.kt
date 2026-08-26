package io.github.dinujaya77.jyotisha

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class M3StaticProductUiTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun v1M301AndV1M302_runtimeShowsUnavailableStatesWithoutSyntheticValues() {
        composeRule.onNodeWithText("Seasonal Planetary Hora not yet calculated").assertIsDisplayed()
        composeRule.onNodeWithText("Calculated astronomical anchors unavailable").assertIsDisplayed()
        composeRule.onAllNodesWithText("Synthetic location context").assertCountEquals(0)

        composeRule.onNodeWithTag("nav_timeline").performClick()
        composeRule.onNodeWithText("24-Hora timeline unavailable").assertIsDisplayed()
        composeRule.onAllNodesWithText("Day — 12 Horas").assertCountEquals(0)
        composeRule.onAllNodesWithText("Synthetic Hora-day context").assertCountEquals(0)
    }

    @Test
    fun v1M303_locationAndSettingsRemainReadOnlyAndBounded() {
        composeRule.onNodeWithTag("action_location").performClick()
        composeRule.onNodeWithTag("screen_location").assertIsDisplayed()
        composeRule.onNodeWithTag("action_use_current_disabled").assertIsNotEnabled()
        composeRule.onNodeWithTag("action_choose_town_disabled").assertIsNotEnabled()
        composeRule.onNodeWithTag("action_use_default_disabled").assertIsNotEnabled()
        composeRule.onNodeWithTag("action_back").performScrollTo().performClick()

        composeRule.onNodeWithTag("action_settings").performScrollTo().performClick()
        composeRule.onNodeWithText("Celestial Archive light and dark appearance follows the device. Dynamic colour and theme packs are not used.")
            .assertIsDisplayed()
        composeRule.onNodeWithTag("action_reset_location_disabled").assertIsNotEnabled()
    }

    @Test
    fun v1M304_methodAndAboutUseApprovedUnavailableAndPrivacyContent() {
        composeRule.onNodeWithTag("nav_method").performClick()
        composeRule.onNodeWithText("Seasonal Planetary Hora method").assertIsDisplayed()
        composeRule.onNodeWithText("Context unavailable").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Calculation method not approved")
            .performScrollTo()
            .assertIsDisplayed()
        composeRule.onNodeWithTag("action_settings").performScrollTo().performClick()
        composeRule.onNodeWithTag("action_about").performScrollTo().performClick()
        composeRule.onNodeWithTag("screen_about").assertIsDisplayed()
        composeRule.onNodeWithText("Purpose and boundaries").assertIsDisplayed()
        composeRule.onNodeWithText("Release-owned information").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithTag("action_method").performScrollTo().performClick()
        composeRule.onNodeWithTag("screen_method").assertIsDisplayed()
        composeRule.onNodeWithTag("action_settings").performScrollTo().performClick()
        composeRule.onNodeWithTag("action_about").performScrollTo().performClick()
        composeRule.onNodeWithTag("action_back_to_settings").performScrollTo().performClick()
        composeRule.onNodeWithTag("screen_settings").assertIsDisplayed()
    }
}
