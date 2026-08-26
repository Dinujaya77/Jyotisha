package io.github.dinujaya77.jyotisha.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import io.github.dinujaya77.jyotisha.ui.theme.JyotishaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/** V1-M2-03: A11Y-AUTO-002/003/005/006/007; T-NFR-A11Y. */
class CelestialArchiveComponentsTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun timingFolioMergesSummaryAndKeepsActionSeparate() {
        var clicks = 0
        val summary = "Timing summary, Ruler, from 06:00 to 07:00, 30 minutes remaining"
        composeRule.setContent {
            JyotishaTheme {
                CelestialTimingFolio(
                    presentation = folio(summary),
                    actionLabel = "Open timeline",
                    onAction = { clicks++ },
                )
            }
        }

        composeRule.onNodeWithContentDescription(summary).assertIsDisplayed()
        composeRule.onNodeWithText("Ruler").assertIsNotDisplayed()
        composeRule.onNodeWithText("Open timeline")
            .assertHasClickAction()
            .assertHeightIsAtLeast(48.dp)
            .assertWidthIsAtLeast(48.dp)
            .performClick()
        assertEquals(1, clicks)
    }

    @Test
    fun timelineCurrentStateUsesSelectedSemanticsAndExplicitText() {
        composeRule.setContent {
            JyotishaTheme(darkTheme = true) {
                Column {
                    CelestialTimelineRow(timelineRow(position = 1, currentLabel = "Current"))
                    CelestialTimelineRow(timelineRow(position = 2))
                }
            }
        }

        composeRule.onNodeWithContentDescription("Day row 1").assertIsSelected()
        composeRule.onNodeWithContentDescription("Day row 2").assertIsNotSelected()
        composeRule.onNodeWithText("Current").assertIsDisplayed()
    }

    @Test
    fun loadingStatusHasProgressSemanticsAndPlainText() {
        composeRule.setContent {
            JyotishaTheme {
                CelestialProvenanceStatus(
                    ProvenanceStatusPresentation(
                        heading = "Calculation details",
                        details = listOf(LabelledValuePresentation("Location", "Selected town")),
                        status = StatusPresentation(
                            label = "Loading",
                            message = "Preparing result",
                            kind = PresentationStatusKind.Information,
                            loading = true,
                        ),
                    ),
                )
            }
        }

        composeRule.onNodeWithText("Preparing result").assertIsDisplayed()
        composeRule.onNode(
            SemanticsMatcher.expectValue(
                SemanticsProperties.ProgressBarRangeInfo,
                ProgressBarRangeInfo.Indeterminate,
            ),
        ).assertIsDisplayed()
    }

    @Test
    fun unavailablePanelShowsNoFabricatedTimingAndActionsRemainSeparate() {
        var primaryClicks = 0
        composeRule.setContent {
            JyotishaTheme {
                CelestialUnavailablePanel(
                    presentation = UnavailablePanelPresentation(
                        heading = "Result unavailable",
                        reason = "A result could not be produced",
                    ),
                    primaryActionLabel = "Try again",
                    onPrimaryAction = { primaryClicks++ },
                )
            }
        }

        composeRule.onNodeWithText("Result unavailable").assertIsDisplayed()
        composeRule.onNodeWithText("06:00").assertIsNotDisplayed()
        composeRule.onNodeWithText("Try again").performClick()
        assertEquals(1, primaryClicks)
    }

    @Test
    fun disabledTextActionsRetainMinimumTargetAndDisabledSemantics() {
        composeRule.setContent {
            JyotishaTheme {
                Column {
                    CelestialPrimaryTextAction("Primary", {}, enabled = false)
                    CelestialSecondaryTextAction("Secondary", {}, enabled = false)
                }
            }
        }

        listOf("Primary", "Secondary").forEach { label ->
            composeRule.onNodeWithText(label)
                .assertIsNotEnabled()
                .assertHeightIsAtLeast(48.dp)
                .assertWidthIsAtLeast(48.dp)
        }
    }

    private fun folio(summary: String) = TimingFolioPresentation(
        heading = "Timing summary",
        primaryValue = "Ruler",
        start = LabelledValuePresentation("Start", "06:00"),
        end = LabelledValuePresentation("End", "07:00"),
        remaining = LabelledValuePresentation("Remaining", "30 minutes"),
        next = LabelledValuePresentation("Next", "Next ruler at 07:00"),
        spokenSummary = summary,
    )

    private fun timelineRow(position: Int, currentLabel: String? = null) =
        TimelineRowPresentation(
            position = position,
            groupLabel = "Day",
            ordinalLabel = "Hora $position",
            primaryValue = "Ruler",
            start = LabelledValuePresentation("Start", "06:00"),
            end = LabelledValuePresentation("End", "07:00"),
            currentLabel = currentLabel,
            spokenSummary = "Day row $position",
        )
}
