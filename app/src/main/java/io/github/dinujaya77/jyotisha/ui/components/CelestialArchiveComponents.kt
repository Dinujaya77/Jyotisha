package io.github.dinujaya77.jyotisha.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import io.github.dinujaya77.jyotisha.ui.theme.CelestialArchiveElevation
import io.github.dinujaya77.jyotisha.ui.theme.CelestialArchiveInteraction
import io.github.dinujaya77.jyotisha.ui.theme.CelestialArchiveSpacing
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveColors
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveType

@Composable
fun CelestialTimingFolio(
    presentation: TimingFolioPresentation,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
    actionEnabled: Boolean = true,
) {
    Column(modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clearAndSetSemantics {
                heading()
                contentDescription = presentation.spokenSummary
                },
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.celestialArchiveColors.archiveSurface,
            border = BorderStroke(
                CelestialArchiveInteraction.focusOutlineWidth,
                MaterialTheme.celestialArchiveColors.outline,
            ),
            tonalElevation = CelestialArchiveElevation.elevationGrouped,
        ) {
            Column(Modifier.padding(CelestialArchiveSpacing.spaceLg)) {
                Text(
                    text = presentation.heading,
                    style = MaterialTheme.celestialArchiveType.label,
                    color = MaterialTheme.celestialArchiveColors.contentSecondary,
                )
                Spacer(Modifier.height(CelestialArchiveSpacing.spaceSm))
                Text(
                    text = presentation.primaryValue,
                    style = MaterialTheme.celestialArchiveType.display,
                    color = MaterialTheme.celestialArchiveColors.contentPrimary,
                )
                Spacer(Modifier.height(CelestialArchiveSpacing.spaceMd))
                LabelledValue(presentation.start, emphasizeValue = true)
                LabelledValue(presentation.end, emphasizeValue = true)
                LabelledValue(presentation.remaining)
                LabelledValue(presentation.next)
            }
        }
        Spacer(Modifier.height(CelestialArchiveSpacing.spaceSm))
        CelestialPrimaryTextAction(
            label = actionLabel,
            onClick = onAction,
            modifier = Modifier.fillMaxWidth(),
            enabled = actionEnabled,
        )
    }
}

@Composable
fun CelestialAnchorRow(
    presentation: AnchorRowPresentation,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth()) {
        SectionHeading(presentation.heading)
        Spacer(Modifier.height(CelestialArchiveSpacing.spaceSm))
        presentation.anchors.forEach { anchor ->
            LabelledValue(anchor, emphasizeValue = true)
        }
    }
}

@Composable
fun CelestialProvenanceStatus(
    presentation: ProvenanceStatusPresentation,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth()) {
        SectionHeading(presentation.heading)
        Spacer(Modifier.height(CelestialArchiveSpacing.spaceSm))
        presentation.details.forEach { LabelledValue(it) }
        presentation.status?.let {
            Spacer(Modifier.height(CelestialArchiveSpacing.spaceSm))
            StatusBlock(it)
        }
    }
}

@Composable
fun CelestialTimelineRow(
    presentation: TimelineRowPresentation,
    modifier: Modifier = Modifier,
) {
    val isCurrent = presentation.currentLabel != null
    val colors = MaterialTheme.celestialArchiveColors
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = presentation.spokenSummary
                selected = isCurrent
            },
        shape = MaterialTheme.shapes.medium,
        color = if (isCurrent) colors.statusActiveContainer else colors.archiveSurface,
        border = BorderStroke(
            CelestialArchiveInteraction.focusOutlineWidth,
            if (isCurrent) colors.statusActiveContent else colors.outline,
        ),
        tonalElevation = if (isCurrent) {
            CelestialArchiveElevation.elevationGrouped
        } else {
            CelestialArchiveElevation.elevationBase
        },
    ) {
        Column(Modifier.padding(CelestialArchiveSpacing.spaceLg)) {
            Text(
                text = "${presentation.groupLabel} ${presentation.ordinalLabel}",
                style = MaterialTheme.celestialArchiveType.label,
                color = if (isCurrent) colors.statusActiveContent else colors.contentSecondary,
            )
            Text(
                text = presentation.primaryValue,
                style = MaterialTheme.celestialArchiveType.title,
                color = if (isCurrent) colors.statusActiveContent else colors.contentPrimary,
            )
            LabelledValue(presentation.start)
            LabelledValue(presentation.end)
            presentation.currentLabel?.let { currentLabel ->
                Text(
                    text = currentLabel,
                    style = MaterialTheme.celestialArchiveType.label,
                    color = colors.statusActiveContent,
                )
            }
        }
    }
}

@Composable
fun CelestialPrimaryTextAction(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    requireActionLabel(label)
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(
            minWidth = CelestialArchiveInteraction.touchTargetMinimum,
            minHeight = CelestialArchiveInteraction.touchTargetMinimum,
        ),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.celestialArchiveColors.actionPrimary,
            contentColor = MaterialTheme.celestialArchiveColors.onActionPrimary,
        ),
    ) {
        Text(text = label, style = MaterialTheme.celestialArchiveType.label)
    }
}

@Composable
fun CelestialSecondaryTextAction(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    requireActionLabel(label)
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(
            minWidth = CelestialArchiveInteraction.touchTargetMinimum,
            minHeight = CelestialArchiveInteraction.touchTargetMinimum,
        ),
        enabled = enabled,
        border = BorderStroke(
            CelestialArchiveInteraction.focusOutlineWidth,
            MaterialTheme.celestialArchiveColors.outline,
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.celestialArchiveColors.actionSecondary,
        ),
    ) {
        Text(text = label, style = MaterialTheme.celestialArchiveType.label)
    }
}

@Composable
fun CelestialUnavailablePanel(
    presentation: UnavailablePanelPresentation,
    modifier: Modifier = Modifier,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null,
    secondaryActionLabel: String? = null,
    onSecondaryAction: (() -> Unit)? = null,
) {
    require((primaryActionLabel == null) == (onPrimaryAction == null))
    require((secondaryActionLabel == null) == (onSecondaryAction == null))
    primaryActionLabel?.let(::requireActionLabel)
    secondaryActionLabel?.let(::requireActionLabel)
    val status = StatusPresentation(
        label = presentation.heading,
        message = presentation.reason,
        kind = presentation.kind,
    )
    Column(modifier.fillMaxWidth()) {
        StatusBlock(status, heading = true)
        presentation.supportingText?.let { text ->
            Spacer(Modifier.height(CelestialArchiveSpacing.spaceSm))
            Text(
                text = text,
                style = MaterialTheme.celestialArchiveType.body,
                color = MaterialTheme.celestialArchiveColors.contentSecondary,
            )
        }
        primaryActionLabel?.let { label ->
            Spacer(Modifier.height(CelestialArchiveSpacing.spaceSm))
            CelestialPrimaryTextAction(
                label = label,
                onClick = requireNotNull(onPrimaryAction),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        secondaryActionLabel?.let { label ->
            Spacer(Modifier.height(CelestialArchiveSpacing.spaceSm))
            CelestialSecondaryTextAction(
                label = label,
                onClick = requireNotNull(onSecondaryAction),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun LabelledValue(
    presentation: LabelledValuePresentation,
    emphasizeValue: Boolean = false,
) {
    Column(Modifier.padding(vertical = CelestialArchiveSpacing.spaceXs)) {
        Text(
            text = presentation.label,
            style = MaterialTheme.celestialArchiveType.label,
            color = MaterialTheme.celestialArchiveColors.contentSecondary,
        )
        Text(
            text = presentation.value,
            style = if (emphasizeValue) {
                MaterialTheme.celestialArchiveType.timePrimary
            } else {
                MaterialTheme.celestialArchiveType.body
            },
            color = MaterialTheme.celestialArchiveColors.contentPrimary,
        )
    }
}

@Composable
private fun SectionHeading(text: String) {
    Text(
        text = text,
        modifier = Modifier.semantics { heading() },
        style = MaterialTheme.celestialArchiveType.title,
        color = MaterialTheme.celestialArchiveColors.contentPrimary,
    )
}

@Immutable
private data class StatusColors(
    val container: Color,
    val content: Color,
)

@Composable
private fun StatusBlock(
    presentation: StatusPresentation,
    heading: Boolean = false,
) {
    val colors = statusColors(presentation.kind)
    val semanticsModifier = Modifier.semantics(mergeDescendants = true) {
        if (heading) heading()
        if (presentation.loading) progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
        if (presentation.kind == PresentationStatusKind.Error) error(presentation.message)
    }
    Surface(
        modifier = semanticsModifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = colors.container,
        contentColor = colors.content,
        border = BorderStroke(CelestialArchiveInteraction.focusOutlineWidth, colors.content),
        tonalElevation = CelestialArchiveElevation.elevationBase,
    ) {
        Column(Modifier.padding(CelestialArchiveSpacing.spaceMd)) {
            Text(text = presentation.label, style = MaterialTheme.celestialArchiveType.label)
            Text(text = presentation.message, style = MaterialTheme.celestialArchiveType.body)
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = CelestialArchiveSpacing.spaceSm)
                    .clearAndSetSemantics { },
                color = colors.content,
            )
        }
    }
}

@Composable
private fun statusColors(kind: PresentationStatusKind): StatusColors {
    val colors = MaterialTheme.celestialArchiveColors
    return when (kind) {
        PresentationStatusKind.Active -> StatusColors(
            colors.statusActiveContainer,
            colors.statusActiveContent,
        )
        PresentationStatusKind.Caution -> StatusColors(
            colors.statusCautionContainer,
            colors.statusCautionContent,
        )
        PresentationStatusKind.Error -> StatusColors(
            colors.statusErrorContainer,
            colors.statusErrorContent,
        )
        PresentationStatusKind.Information -> StatusColors(
            colors.statusInfoContainer,
            colors.statusInfoContent,
        )
        PresentationStatusKind.Unavailable -> StatusColors(
            colors.statusUnavailableContainer,
            colors.statusUnavailableContent,
        )
    }
}
