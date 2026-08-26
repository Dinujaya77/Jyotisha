package io.github.dinujaya77.jyotisha.ui.components

import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

/** V1-M2-03: FR-001–003, FR-009–012; NFR-005; T-FR-011; T-NFR-A11Y. */
class PresentationModelsTest {

    @Test
    fun timelinePresentationRecognizesStrictChronologyAndOneCurrentRow() {
        val first = timelineRow(position = 1, currentLabel = "Current")
        val second = timelineRow(position = 2)

        assertTrue(timelineRowsAreChronological(listOf(first, second)))
        assertFalse(timelineRowsAreChronological(listOf(second, first)))
        assertTrue(timelineHasAtMostOneCurrentRow(listOf(first, second)))
        assertFalse(
            timelineHasAtMostOneCurrentRow(
                listOf(first, second.copy(currentLabel = "Current")),
            ),
        )
    }

    @Test
    fun presentationModelsRejectMissingEssentialAccessibleText() {
        assertThrows(IllegalArgumentException::class.java) {
            LabelledValuePresentation(label = "", value = "06:00")
        }
        assertThrows(IllegalArgumentException::class.java) {
            timelineRow(position = 1).copy(spokenSummary = "")
        }
        assertThrows(IllegalArgumentException::class.java) {
            UnavailablePanelPresentation(heading = "Unavailable", reason = "")
        }
    }

    @Test
    fun unavailablePanelAcceptsOnlyUnavailableOrErrorMeaning() {
        assertTrue(
            UnavailablePanelPresentation(
                heading = "Unavailable",
                reason = "No result",
            ).kind == PresentationStatusKind.Unavailable,
        )
        assertThrows(IllegalArgumentException::class.java) {
            UnavailablePanelPresentation(
                heading = "Unavailable",
                reason = "No result",
                kind = PresentationStatusKind.Active,
            )
        }
    }

    @Test
    fun actionLabelsMustContainAccessibleVisibleText() {
        assertTrue(requireActionLabel("Retry") == "Retry")
        assertThrows(IllegalArgumentException::class.java) { requireActionLabel("   ") }
    }

    private fun timelineRow(position: Int, currentLabel: String? = null) =
        TimelineRowPresentation(
            position = position,
            groupLabel = "Day",
            ordinalLabel = "Hora $position",
            primaryValue = "Ruler",
            start = LabelledValuePresentation("Start", "06:00"),
            end = LabelledValuePresentation("End", "07:00"),
            currentLabel = currentLabel,
            spokenSummary = "Day Hora $position, Ruler, 06:00 to 07:00",
        )
}
