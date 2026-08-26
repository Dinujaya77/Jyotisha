package io.github.dinujaya77.jyotisha.ui.preview

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

/** V1-M2-04 compiled semantics smoke; screenshots and manual evidence remain separate gates. */
class ComponentPreviewSmokeTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun stateMatrixKeepsExplicitStatesAndActionSemantics() {
        composeRule.setContent {
            ComponentPreview(componentPreviewFixture("compact-state-matrix"))
        }

        composeRule.onNodeWithText("Loading").assertIsDisplayed()
        composeRule.onNodeWithText("Synthetic error").assertIsDisplayed()
        composeRule.onNodeWithText("Synthetic result unavailable").assertIsDisplayed()
        composeRule.onNodeWithText("Enabled action").assertHasClickAction()
        composeRule.onNodeWithText("Disabled action").assertIsNotEnabled()
    }

    @Test
    fun reducedMotionFixtureExposesImmediateEndStateText() {
        composeRule.setContent {
            ComponentPreview(componentPreviewFixture("compact-reduced-motion-end-state"))
        }

        composeRule.onNodeWithText("Immediate end state").assertIsDisplayed()
        composeRule.onNodeWithText("Suppressed").assertIsDisplayed()
        composeRule.onNodeWithText("End-state action").assertHasClickAction()
    }
}
