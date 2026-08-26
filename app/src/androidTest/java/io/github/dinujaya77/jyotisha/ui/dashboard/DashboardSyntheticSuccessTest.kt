package io.github.dinujaya77.jyotisha.ui.dashboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import io.github.dinujaya77.jyotisha.ui.preview.syntheticDashboardSuccessFixture
import io.github.dinujaya77.jyotisha.ui.theme.JyotishaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DashboardSyntheticSuccessTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun v1M301_successFixtureRendersContextAndConsumesEveryCallback() {
        val actions = mutableListOf<String>()
        composeRule.setContent {
            JyotishaTheme {
                DashboardScreen(
                    presentation = syntheticDashboardSuccessFixture(),
                    callbacks = DashboardCallbacks(
                        openLocation = { actions += "location" },
                        openSettings = { actions += "settings" },
                        openTimeline = { actions += "timeline" },
                        openMethod = { actions += "method" },
                    ),
                    innerPadding = PaddingValues(),
                )
            }
        }

        composeRule.onNodeWithText("Synthetic location context").assertIsDisplayed()
        composeRule.onNodeWithText("Synthetic 2026-08-02 12:34:00")
            .performScrollTo()
            .assertIsDisplayed()
        composeRule.onNodeWithText("Asia/Colombo — synthetic fixture")
            .performScrollTo()
            .assertIsDisplayed()
        composeRule.onNodeWithText("CP-001-v1.0 — synthetic fixture")
            .performScrollTo()
            .assertIsDisplayed()

        composeRule.onNodeWithTag("action_location").performScrollTo().performClick()
        composeRule.onNodeWithTag("action_settings").performScrollTo().performClick()
        composeRule.onNodeWithText("View all 24 Horas").performScrollTo().performClick()
        composeRule.onNodeWithTag("action_method").performScrollTo().performClick()
        composeRule.runOnIdle {
            assertEquals(listOf("location", "settings", "timeline", "method"), actions)
        }
    }
}
