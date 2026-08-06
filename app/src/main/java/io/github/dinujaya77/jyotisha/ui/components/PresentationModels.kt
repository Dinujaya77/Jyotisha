package io.github.dinujaya77.jyotisha.ui.components

import androidx.compose.runtime.Immutable

@Immutable
data class LabelledValuePresentation(
    val label: String,
    val value: String,
) {
    init {
        require(label.isNotBlank())
        require(value.isNotBlank())
    }
}

/** Display-ready, domain-neutral content for one coherent timing summary. */
@Immutable
data class TimingFolioPresentation(
    val heading: String,
    val primaryValue: String,
    val start: LabelledValuePresentation,
    val end: LabelledValuePresentation,
    val remaining: LabelledValuePresentation,
    val next: LabelledValuePresentation,
    val spokenSummary: String,
) {
    init {
        require(heading.isNotBlank())
        require(primaryValue.isNotBlank())
        require(spokenSummary.isNotBlank())
    }
}

@Immutable
data class AnchorRowPresentation(
    val heading: String,
    val anchors: List<LabelledValuePresentation>,
) {
    init {
        require(heading.isNotBlank())
        require(anchors.isNotEmpty())
    }
}

enum class PresentationStatusKind {
    Active,
    Caution,
    Error,
    Information,
    Unavailable,
}

@Immutable
data class StatusPresentation(
    val label: String,
    val message: String,
    val kind: PresentationStatusKind,
    val loading: Boolean = false,
) {
    init {
        require(label.isNotBlank())
        require(message.isNotBlank())
    }
}

@Immutable
data class ProvenanceStatusPresentation(
    val heading: String,
    val details: List<LabelledValuePresentation>,
    val status: StatusPresentation? = null,
) {
    init {
        require(heading.isNotBlank())
        require(details.isNotEmpty())
    }
}

/** Display-ready row. Position is presentation order only; no interval membership is calculated. */
@Immutable
data class TimelineRowPresentation(
    val position: Int,
    val groupLabel: String,
    val ordinalLabel: String,
    val primaryValue: String,
    val start: LabelledValuePresentation,
    val end: LabelledValuePresentation,
    val currentLabel: String? = null,
    val spokenSummary: String,
) {
    init {
        require(position > 0)
        require(groupLabel.isNotBlank())
        require(ordinalLabel.isNotBlank())
        require(primaryValue.isNotBlank())
        require(currentLabel == null || currentLabel.isNotBlank())
        require(spokenSummary.isNotBlank())
    }
}

internal fun timelineRowsAreChronological(rows: List<TimelineRowPresentation>): Boolean =
    rows.zipWithNext().all { (first, second) -> first.position < second.position }

internal fun timelineHasAtMostOneCurrentRow(rows: List<TimelineRowPresentation>): Boolean =
    rows.count { it.currentLabel != null } <= 1

internal fun requireActionLabel(label: String): String {
    require(label.isNotBlank())
    return label
}

@Immutable
data class UnavailablePanelPresentation(
    val heading: String,
    val reason: String,
    val supportingText: String? = null,
    val kind: PresentationStatusKind = PresentationStatusKind.Unavailable,
) {
    init {
        require(heading.isNotBlank())
        require(reason.isNotBlank())
        require(supportingText == null || supportingText.isNotBlank())
        require(kind == PresentationStatusKind.Unavailable || kind == PresentationStatusKind.Error)
    }
}
