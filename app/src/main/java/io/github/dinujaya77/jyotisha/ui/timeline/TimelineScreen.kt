package io.github.dinujaya77.jyotisha.ui.timeline

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import io.github.dinujaya77.jyotisha.R
import io.github.dinujaya77.jyotisha.ui.components.AnchorRowPresentation
import io.github.dinujaya77.jyotisha.ui.components.CelestialAnchorRow
import io.github.dinujaya77.jyotisha.ui.components.CelestialPrimaryTextAction
import io.github.dinujaya77.jyotisha.ui.components.CelestialProvenanceStatus
import io.github.dinujaya77.jyotisha.ui.components.CelestialTimelineRow
import io.github.dinujaya77.jyotisha.ui.components.CelestialUnavailablePanel
import io.github.dinujaya77.jyotisha.ui.components.ProvenanceStatusPresentation
import io.github.dinujaya77.jyotisha.ui.components.TimelineRowPresentation
import io.github.dinujaya77.jyotisha.ui.components.UnavailablePanelPresentation
import io.github.dinujaya77.jyotisha.ui.screens.CelestialScreenLayout
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveColors
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveType

internal const val TIMELINE_SCREEN_TAG = "screen_timeline"

@Immutable
internal sealed interface TimelinePresentation {
    @Immutable
    data class Unavailable(
        val reason: UnavailablePanelPresentation,
    ) : TimelinePresentation

    @Immutable
    data class Success(
        val context: ProvenanceStatusPresentation,
        val anchors: AnchorRowPresentation,
        val rows: List<TimelineRowPresentation>,
    ) : TimelinePresentation {
        init {
            require(rows.size == 24)
            require(rows.map { it.position } == (1..24).toList())
            require(rows.take(12).all { it.groupLabel == "Day" })
            require(rows.drop(12).all { it.groupLabel == "Night" })
            require(rows.count { it.currentLabel != null } <= 1)
        }
    }
}

@Immutable
internal data class TimelineCallbacks(
    val openDashboard: () -> Unit,
    val openMethod: () -> Unit,
    val goToCurrentHora: () -> Unit,
)

@Composable
internal fun RuntimeTimelinePresentation(): TimelinePresentation.Unavailable =
    TimelinePresentation.Unavailable(
        reason = UnavailablePanelPresentation(
            heading = stringResource(R.string.timeline_unavailable_heading),
            reason = stringResource(R.string.timeline_unavailable_reason),
            supportingText = stringResource(R.string.timeline_unavailable_supporting),
        ),
    )

@Composable
internal fun TimelineScreen(
    presentation: TimelinePresentation,
    callbacks: TimelineCallbacks,
    innerPadding: PaddingValues,
) {
    CelestialScreenLayout(
        title = stringResource(R.string.screen_timeline_title),
        testTag = TIMELINE_SCREEN_TAG,
        innerPadding = innerPadding,
    ) {
        when (presentation) {
            is TimelinePresentation.Unavailable -> CelestialUnavailablePanel(
                presentation = presentation.reason,
                primaryActionLabel = stringResource(R.string.action_dashboard),
                onPrimaryAction = callbacks.openDashboard,
                secondaryActionLabel = stringResource(R.string.action_method),
                onSecondaryAction = callbacks.openMethod,
            )

            is TimelinePresentation.Success -> TimelineLedger(presentation, callbacks)
        }
    }
}

@Composable
private fun TimelineLedger(
    presentation: TimelinePresentation.Success,
    callbacks: TimelineCallbacks,
) {
    CelestialProvenanceStatus(presentation.context)
    CelestialAnchorRow(presentation.anchors)
    CelestialPrimaryTextAction(
        label = stringResource(R.string.timeline_action_go_to_current),
        onClick = callbacks.goToCurrentHora,
    )
    TimelineGroup(
        heading = stringResource(R.string.timeline_day_heading),
        rows = presentation.rows.take(12),
    )
    TimelineGroup(
        heading = stringResource(R.string.timeline_night_heading),
        rows = presentation.rows.drop(12),
    )
}

@Composable
private fun TimelineGroup(
    heading: String,
    rows: List<TimelineRowPresentation>,
) {
    Text(
        text = heading,
        modifier = Modifier.semantics { heading() },
        style = MaterialTheme.celestialArchiveType.title,
        color = MaterialTheme.celestialArchiveColors.contentPrimary,
    )
    rows.forEach { row -> CelestialTimelineRow(row) }
}
