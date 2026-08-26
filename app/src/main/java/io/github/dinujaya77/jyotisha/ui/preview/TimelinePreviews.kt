package io.github.dinujaya77.jyotisha.ui.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.dinujaya77.jyotisha.ui.components.AnchorRowPresentation
import io.github.dinujaya77.jyotisha.ui.components.LabelledValuePresentation
import io.github.dinujaya77.jyotisha.ui.components.ProvenanceStatusPresentation
import io.github.dinujaya77.jyotisha.ui.components.TimelineRowPresentation
import io.github.dinujaya77.jyotisha.ui.theme.JyotishaTheme
import io.github.dinujaya77.jyotisha.ui.timeline.TimelineCallbacks
import io.github.dinujaya77.jyotisha.ui.timeline.TimelinePresentation
import io.github.dinujaya77.jyotisha.ui.timeline.TimelineScreen

@Preview(name = "M3 Timeline synthetic 24 rows", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun TimelineSyntheticPreview() {
    JyotishaTheme {
        TimelineScreen(
            presentation = syntheticTimelineFixture(),
            callbacks = TimelineCallbacks({}, {}, {}),
            innerPadding = PaddingValues(),
        )
    }
}

internal fun syntheticTimelineFixture(): TimelinePresentation.Success {
    val rulers = listOf("Sun", "Venus", "Mercury", "Moon", "Saturn", "Jupiter", "Mars")
    val rows = (1..24).map { position ->
        val day = position <= 12
        val ordinal = if (day) position else position - 12
        val startHour = (position + 5) % 24
        val endHour = (startHour + 1) % 24
        TimelineRowPresentation(
            position = position,
            groupLabel = if (day) "Day" else "Night",
            ordinalLabel = "Hora $ordinal",
            primaryValue = rulers[(position - 1) % rulers.size],
            start = LabelledValuePresentation("Start", "%02d:00".format(startHour)),
            end = LabelledValuePresentation("End", "%02d:00".format(endHour)),
            currentLabel = if (position == 4) "Current" else null,
            spokenSummary = "${if (day) "Day" else "Night"} Hora $ordinal, " +
                "${rulers[(position - 1) % rulers.size]}, " +
                "%02d:00 to %02d:00".format(startHour, endHour) +
                if (position == 4) ", Current" else "",
        )
    }
    return TimelinePresentation.Success(
        context = ProvenanceStatusPresentation(
            heading = "Synthetic Hora-day context",
            details = listOf(
                LabelledValuePresentation("Date range", "Preview sunrise to following sunrise"),
                LabelledValuePresentation("Location", "Synthetic preview town"),
                LabelledValuePresentation("Source", "Deterministic preview fixture"),
            ),
        ),
        anchors = AnchorRowPresentation(
            heading = "Synthetic calculated astronomical anchors",
            anchors = listOf(
                LabelledValuePresentation("Sunrise", "06:00"),
                LabelledValuePresentation("Sunset", "18:00"),
                LabelledValuePresentation("Following sunrise", "06:00"),
            ),
        ),
        rows = rows,
    )
}
