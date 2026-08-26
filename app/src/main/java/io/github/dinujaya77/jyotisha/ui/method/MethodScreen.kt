package io.github.dinujaya77.jyotisha.ui.method

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import io.github.dinujaya77.jyotisha.R
import io.github.dinujaya77.jyotisha.ui.components.CelestialPrimaryTextAction
import io.github.dinujaya77.jyotisha.ui.components.CelestialProvenanceStatus
import io.github.dinujaya77.jyotisha.ui.components.CelestialUnavailablePanel
import io.github.dinujaya77.jyotisha.ui.components.LabelledValuePresentation
import io.github.dinujaya77.jyotisha.ui.components.PresentationStatusKind
import io.github.dinujaya77.jyotisha.ui.components.ProvenanceStatusPresentation
import io.github.dinujaya77.jyotisha.ui.components.StatusPresentation
import io.github.dinujaya77.jyotisha.ui.components.UnavailablePanelPresentation
import io.github.dinujaya77.jyotisha.ui.screens.CelestialScreenLayout
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveColors
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveType

internal const val METHOD_SCREEN_TAG = "screen_method"

internal enum class MethodSection {
    SeasonalHora,
    SolarConvention,
    CurrentContext,
    Diagnostics,
    ValidationSources,
    RahuStatus,
    PrivacyAbout,
}

/** UI-006 governs visual, reading, semantic, focus, and traversal order. */
internal val approvedMethodSectionOrder = listOf(
    MethodSection.SeasonalHora,
    MethodSection.SolarConvention,
    MethodSection.CurrentContext,
    MethodSection.Diagnostics,
    MethodSection.ValidationSources,
    MethodSection.RahuStatus,
    MethodSection.PrivacyAbout,
)

@Immutable
internal data class MethodCallbacks(
    val openSettings: () -> Unit,
)

@Composable
internal fun MethodScreen(
    callbacks: MethodCallbacks,
    innerPadding: PaddingValues,
) {
    CelestialScreenLayout(
        title = stringResource(R.string.screen_method_title),
        testTag = METHOD_SCREEN_TAG,
        innerPadding = innerPadding,
    ) {
        approvedMethodSectionOrder.forEach { section ->
            when (section) {
                MethodSection.SeasonalHora -> MethodTextSection(
                    heading = stringResource(R.string.method_hora_heading),
                    body = stringResource(R.string.method_hora_body),
                )
                MethodSection.SolarConvention -> MethodTextSection(
                    heading = stringResource(R.string.method_solar_heading),
                    body = stringResource(R.string.method_solar_body),
                )
                MethodSection.CurrentContext -> CurrentContextSection()
                MethodSection.Diagnostics -> DiagnosticsSection()
                MethodSection.ValidationSources -> MethodTextSection(
                    heading = stringResource(R.string.method_validation_heading),
                    body = stringResource(R.string.method_validation_body),
                )
                MethodSection.RahuStatus -> RahuStatusSection()
                MethodSection.PrivacyAbout -> PrivacyAboutSection(callbacks)
            }
        }
    }
}

@Composable
private fun CurrentContextSection() {
    CelestialProvenanceStatus(
        ProvenanceStatusPresentation(
            heading = stringResource(R.string.method_context_heading),
            details = listOf(
                LabelledValuePresentation(
                    stringResource(R.string.method_context_location_label),
                    stringResource(R.string.value_unavailable),
                ),
                LabelledValuePresentation(
                    stringResource(R.string.method_context_zone_label),
                    stringResource(R.string.value_unavailable),
                ),
                LabelledValuePresentation(
                    stringResource(R.string.method_context_date_label),
                    stringResource(R.string.value_unavailable),
                ),
                LabelledValuePresentation(
                    stringResource(R.string.method_context_calculated_label),
                    stringResource(R.string.value_not_yet_calculated),
                ),
                LabelledValuePresentation(
                    stringResource(R.string.method_context_versions_label),
                    stringResource(R.string.method_context_versions_unavailable),
                ),
            ),
            status = StatusPresentation(
                label = stringResource(R.string.method_context_status_label),
                message = stringResource(R.string.method_context_status_body),
                kind = PresentationStatusKind.Unavailable,
            ),
        ),
    )
}

@Composable
private fun DiagnosticsSection() {
    CelestialUnavailablePanel(
        presentation = UnavailablePanelPresentation(
            heading = stringResource(R.string.method_diagnostics_heading),
            reason = stringResource(R.string.method_diagnostics_reason),
            supportingText = stringResource(R.string.method_diagnostics_supporting),
        ),
    )
}

@Composable
private fun RahuStatusSection() {
    CelestialProvenanceStatus(
        ProvenanceStatusPresentation(
            heading = stringResource(R.string.method_rahu_heading),
            details = listOf(
                LabelledValuePresentation(
                    stringResource(R.string.method_rahu_status_label),
                    stringResource(R.string.method_rahu_unapproved_value),
                ),
            ),
            status = StatusPresentation(
                label = stringResource(R.string.method_rahu_unavailable_label),
                message = stringResource(R.string.method_rahu_body),
                kind = PresentationStatusKind.Unavailable,
            ),
        ),
    )
}

@Composable
private fun PrivacyAboutSection(callbacks: MethodCallbacks) {
    MethodTextSection(
        heading = stringResource(R.string.method_privacy_heading),
        body = stringResource(R.string.method_privacy_body),
    )
    CelestialPrimaryTextAction(
        label = stringResource(R.string.method_action_open_privacy_about),
        onClick = callbacks.openSettings,
        modifier = Modifier.testTag("action_settings"),
    )
}

@Composable
private fun MethodTextSection(heading: String, body: String) {
    Text(
        text = heading,
        modifier = Modifier.semantics { heading() },
        style = MaterialTheme.celestialArchiveType.title,
        color = MaterialTheme.celestialArchiveColors.contentPrimary,
    )
    SelectionContainer {
        Text(
            text = body,
            style = MaterialTheme.celestialArchiveType.body,
            color = MaterialTheme.celestialArchiveColors.contentSecondary,
        )
    }
}
