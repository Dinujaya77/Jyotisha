package io.github.dinujaya77.jyotisha.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import io.github.dinujaya77.jyotisha.ui.components.AnchorRowPresentation
import io.github.dinujaya77.jyotisha.ui.components.CelestialAnchorRow
import io.github.dinujaya77.jyotisha.ui.components.CelestialPrimaryTextAction
import io.github.dinujaya77.jyotisha.ui.components.CelestialProvenanceStatus
import io.github.dinujaya77.jyotisha.ui.components.CelestialSecondaryTextAction
import io.github.dinujaya77.jyotisha.ui.components.CelestialTimelineRow
import io.github.dinujaya77.jyotisha.ui.components.CelestialTimingFolio
import io.github.dinujaya77.jyotisha.ui.components.CelestialUnavailablePanel
import io.github.dinujaya77.jyotisha.ui.components.LabelledValuePresentation
import io.github.dinujaya77.jyotisha.ui.components.PresentationStatusKind
import io.github.dinujaya77.jyotisha.ui.components.ProvenanceStatusPresentation
import io.github.dinujaya77.jyotisha.ui.components.StatusPresentation
import io.github.dinujaya77.jyotisha.ui.components.TimelineRowPresentation
import io.github.dinujaya77.jyotisha.ui.components.TimingFolioPresentation
import io.github.dinujaya77.jyotisha.ui.components.UnavailablePanelPresentation
import io.github.dinujaya77.jyotisha.ui.theme.BackgroundWash
import io.github.dinujaya77.jyotisha.ui.theme.CelestialArchiveSpacing
import io.github.dinujaya77.jyotisha.ui.theme.JyotishaTheme
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveColors
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveType

@Preview(name = "M2 Compact Light", widthDp = 320, heightDp = 720, showBackground = true)
@Composable
private fun CompactLightComponentsPreview() {
    ComponentPreview(componentPreviewFixture("compact-light-components"))
}

@Preview(name = "M2 Compact Dark", widthDp = 320, heightDp = 720, showBackground = true)
@Composable
private fun CompactDarkComponentsPreview() {
    ComponentPreview(componentPreviewFixture("compact-dark-components"))
}

@Preview(name = "M2 Compact Landscape", widthDp = 640, heightDp = 320, showBackground = true)
@Composable
private fun CompactLandscapeComponentsPreview() {
    ComponentPreview(componentPreviewFixture("compact-landscape-components"))
}

@Preview(name = "M2 Medium", widthDp = 720, heightDp = 900, showBackground = true)
@Composable
private fun MediumComponentsPreview() {
    ComponentPreview(componentPreviewFixture("medium-components"))
}

@Preview(name = "M2 Expanded", widthDp = 1000, heightDp = 900, showBackground = true)
@Composable
private fun ExpandedComponentsPreview() {
    ComponentPreview(componentPreviewFixture("expanded-components"))
}

@Preview(
    name = "M2 Compact Long English 200%",
    widthDp = 320,
    heightDp = 720,
    fontScale = 2f,
    showBackground = true,
)
@Composable
private fun CompactLongEnglishPreview() {
    ComponentPreview(componentPreviewFixture("compact-long-english-200-percent"))
}

@Preview(name = "M2 State Matrix", widthDp = 320, heightDp = 900, showBackground = true)
@Composable
private fun StateMatrixPreview() {
    ComponentPreview(componentPreviewFixture("compact-state-matrix"))
}

@Preview(name = "M2 Reduced Motion End State", widthDp = 320, heightDp = 720, showBackground = true)
@Composable
private fun ReducedMotionEndStatePreview() {
    ComponentPreview(componentPreviewFixture("compact-reduced-motion-end-state"))
}

@Composable
internal fun ComponentPreview(fixture: ComponentPreviewFixture) {
    val currentDensity = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(currentDensity.density, fixture.fontScale),
    ) {
        JyotishaTheme(darkTheme = fixture.darkTheme) {
            ComponentPreviewContent(fixture)
        }
    }
}

@Composable
internal fun ComponentPreviewContent(fixture: ComponentPreviewFixture) {
    val colors = MaterialTheme.celestialArchiveColors
    val washModifier = when (val wash = previewBackgroundWash(fixture)) {
        BackgroundWash.None -> Modifier.background(colors.archiveBackground)
        is BackgroundWash.Static -> Modifier.background(
            Brush.linearGradient(listOf(wash.start, wash.end)),
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(washModifier)
            .verticalScroll(rememberScrollState())
            .padding(previewGutter(fixture.viewport)),
    ) {
        Text(
            text = previewHeading(fixture),
            modifier = Modifier.semantics { heading() },
            style = MaterialTheme.celestialArchiveType.headline,
            color = colors.contentPrimary,
        )
        PreviewGap()
        when (fixture.scenario) {
            ComponentPreviewScenario.ComponentCombination,
            ComponentPreviewScenario.LongEnglish,
            -> ComponentCombination(fixture)
            ComponentPreviewScenario.StateMatrix -> StateMatrixCombination(fixture)
            ComponentPreviewScenario.ReducedMotionEndState -> ReducedMotionCombination(fixture)
        }
    }
}

@Composable
private fun ComponentCombination(fixture: ComponentPreviewFixture) {
    CelestialTimingFolio(
        presentation = timingFolio(fixture.longEnglish),
        actionLabel = if (fixture.longEnglish) {
            "Open the complete synthetic timing ledger for detailed verification"
        } else {
            "Open synthetic ledger"
        },
        onAction = {},
    )
    PreviewGap()
    CelestialAnchorRow(anchorPresentation(fixture.longEnglish))
    PreviewGap()
    CelestialProvenanceStatus(provenancePresentation(fixture.longEnglish))
    PreviewGap()
    TimelineExamples(fixture)
}

@Composable
private fun StateMatrixCombination(fixture: ComponentPreviewFixture) {
    if (fixture.loadingState) {
        CelestialProvenanceStatus(
            ProvenanceStatusPresentation(
                heading = "Synthetic loading presentation",
                details = listOf(LabelledValuePresentation("Context", "Deterministic fixture")),
                status = StatusPresentation(
                    label = "Loading",
                    message = "Preparing a synthetic component result",
                    kind = PresentationStatusKind.Information,
                    loading = true,
                ),
            ),
        )
        PreviewGap()
    }
    if (fixture.errorState) {
        CelestialUnavailablePanel(
            presentation = UnavailablePanelPresentation(
                heading = "Synthetic error",
                reason = "The test result could not be prepared",
                supportingText = "No time or calculated value is shown in this fixture.",
                kind = PresentationStatusKind.Error,
            ),
            primaryActionLabel = "Retry synthetic result",
            onPrimaryAction = {},
        )
        PreviewGap()
    }
    if (fixture.unavailableState) {
        CelestialUnavailablePanel(
            presentation = UnavailablePanelPresentation(
                heading = "Synthetic result unavailable",
                reason = "Required fixture input is intentionally absent",
            ),
            secondaryActionLabel = "Review fixture details",
            onSecondaryAction = {},
        )
        PreviewGap()
    }
    TimelineExamples(fixture)
    PreviewGap()
    if (fixture.enabledAction) {
        CelestialPrimaryTextAction(
            label = "Enabled action",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
    if (fixture.disabledAction) {
        PreviewGap()
        CelestialSecondaryTextAction(
            label = "Disabled action",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
        )
    }
}

@Composable
private fun ReducedMotionCombination(fixture: ComponentPreviewFixture) {
    CelestialProvenanceStatus(
        ProvenanceStatusPresentation(
            heading = "Reduced-motion verification",
            details = listOf(
                LabelledValuePresentation("Transition", "Immediate end state"),
                LabelledValuePresentation("Decorative wash", "Suppressed"),
            ),
            status = StatusPresentation(
                label = "Stable state",
                message = "Text, outline, focus meaning, and actions remain present",
                kind = PresentationStatusKind.Information,
            ),
        ),
    )
    PreviewGap()
    TimelineExamples(fixture)
    PreviewGap()
    CelestialPrimaryTextAction(
        label = "End-state action",
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun TimelineExamples(fixture: ComponentPreviewFixture) {
    if (fixture.selectedTimeline) {
        CelestialTimelineRow(timelineRow(position = 1, current = true))
    }
    if (fixture.selectedTimeline && fixture.unselectedTimeline) PreviewGap()
    if (fixture.unselectedTimeline) {
        CelestialTimelineRow(timelineRow(position = 2, current = false))
    }
}

@Composable
private fun PreviewGap() {
    Spacer(Modifier.height(CelestialArchiveSpacing.spaceLg))
}

private fun previewHeading(fixture: ComponentPreviewFixture): String =
    "Component verification — ${fixture.id}"

private fun timingFolio(longEnglish: Boolean) = TimingFolioPresentation(
    heading = if (longEnglish) {
        "Synthetic timing summary with deliberately expanded English labels"
    } else {
        "Synthetic timing summary"
    },
    primaryValue = "Archive ruler",
    start = LabelledValuePresentation("Start", "06:10"),
    end = LabelledValuePresentation("End", "07:15"),
    remaining = LabelledValuePresentation("Remaining", "42 minutes"),
    next = LabelledValuePresentation(
        "Next",
        if (longEnglish) "Following synthetic ruler begins at 07:15 local display time" else "Next ruler at 07:15",
    ),
    spokenSummary = "Synthetic timing summary, Archive ruler, from 06:10 to 07:15, " +
        "42 minutes remaining, next ruler at 07:15.",
)

private fun anchorPresentation(longEnglish: Boolean) = AnchorRowPresentation(
    heading = if (longEnglish) {
        "Synthetic labelled anchors with expanded verification copy"
    } else {
        "Synthetic labelled anchors"
    },
    anchors = listOf(
        LabelledValuePresentation("First anchor", "06:10"),
        LabelledValuePresentation("Second anchor", "18:20"),
    ),
)

private fun provenancePresentation(longEnglish: Boolean) = ProvenanceStatusPresentation(
    heading = "Synthetic provenance and status",
    details = listOf(
        LabelledValuePresentation("Display context", "Clearly synthetic fixture data"),
        LabelledValuePresentation(
            "Source",
            if (longEnglish) {
                "Deterministic preview-only source with intentionally extended English wording"
            } else {
                "Deterministic preview fixture"
            },
        ),
    ),
    status = StatusPresentation(
        label = "Available",
        message = "Explicit text and outline convey this state",
        kind = PresentationStatusKind.Active,
    ),
)

private fun timelineRow(position: Int, current: Boolean) = TimelineRowPresentation(
    position = position,
    groupLabel = "Synthetic group",
    ordinalLabel = "row $position",
    primaryValue = if (current) "Current archive ruler" else "Upcoming archive ruler",
    start = LabelledValuePresentation("Start", if (position == 1) "06:10" else "07:15"),
    end = LabelledValuePresentation("End", if (position == 1) "07:15" else "08:20"),
    currentLabel = if (current) "Current" else null,
    spokenSummary = "Synthetic group row $position, archive ruler, " +
        if (current) "Current" else "Not current",
)
