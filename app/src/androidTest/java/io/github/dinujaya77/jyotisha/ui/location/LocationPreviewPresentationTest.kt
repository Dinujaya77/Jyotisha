package io.github.dinujaya77.jyotisha.ui.location

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import io.github.dinujaya77.jyotisha.ui.preview.locationStateFixtures
import io.github.dinujaya77.jyotisha.ui.theme.JyotishaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LocationPreviewPresentationTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun v1M303_allSyntheticStatesRenderSemanticsAndDispatchMappedActions() {
        val activeFixture = mutableStateOf(locationStateFixtures.first())
        var observedAction: LocationAction? = null
        var backCount = 0
        composeRule.setContent {
            JyotishaTheme {
                LocationScreen(
                    presentation = activeFixture.value.presentation,
                    callbacks = LocationCallbacks(
                        onAction = { observedAction = it },
                        onBack = { backCount += 1 },
                    ),
                    innerPadding = PaddingValues(),
                )
            }
        }

        locationStateFixtures.forEach { fixture ->
            composeRule.runOnIdle {
                observedAction = null
                activeFixture.value = fixture
            }
            composeRule.waitForIdle()

            val currentStateText = when (val current = fixture.presentation.currentLocation) {
                is CurrentLocationPresentation.Available -> current.status.details.first().value
                is CurrentLocationPresentation.Unavailable -> current.status.heading
            }
            composeRule.onNodeWithText(currentStateText).performScrollTo().assertIsDisplayed()
            composeRule.onNodeWithTag("location_selected")
                .performScrollTo()
                .assert(
                    SemanticsMatcher.expectValue(
                        SemanticsProperties.Selected,
                        fixture.presentation.selected,
                    ),
                )

            val currentAction = fixture.presentation.currentActions.single()
            composeRule.onNodeWithTag(
                locationActionTestTag(currentAction.action, currentAction.enabled),
            )
                .performScrollTo()
                .assertIsEnabled()
                .performClick()
            composeRule.runOnIdle {
                assertEquals(currentAction.action, observedAction)
            }

            composeRule.onNodeWithTag("action_choose_town_disabled")
                .performScrollTo()
                .assertIsNotEnabled()
            val defaultAction = fixture.presentation.defaultAction
            val defaultNode = composeRule.onNodeWithTag(
                locationActionTestTag(defaultAction.action, defaultAction.enabled),
            ).performScrollTo()
            if (defaultAction.enabled) {
                defaultNode
                    .assertIsEnabled()
                    .performClick()
                composeRule.runOnIdle {
                    assertEquals(LocationAction.UseDefault, observedAction)
                }
            } else {
                defaultNode.assertIsNotEnabled()
            }
        }

        composeRule.onNodeWithTag("action_back")
            .performScrollTo()
            .performClick()
        composeRule.runOnIdle {
            assertEquals(1, backCount)
        }
    }
}
