package io.github.dinujaya77.jyotisha.ui.dashboard

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
import io.github.dinujaya77.jyotisha.ui.components.CelestialProvenanceStatus
import io.github.dinujaya77.jyotisha.ui.components.CelestialTimingFolio
import io.github.dinujaya77.jyotisha.ui.components.CelestialUnavailablePanel
import io.github.dinujaya77.jyotisha.ui.components.PresentationStatusKind
import io.github.dinujaya77.jyotisha.ui.components.ProvenanceStatusPresentation
import io.github.dinujaya77.jyotisha.ui.components.StatusPresentation
import io.github.dinujaya77.jyotisha.ui.components.TimingFolioPresentation
import io.github.dinujaya77.jyotisha.ui.components.UnavailablePanelPresentation
import io.github.dinujaya77.jyotisha.ui.screens.CelestialScreenLayout
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveColors
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveType

internal const val DASHBOARD_SCREEN_TAG = "screen_dashboard"

@Immutable
internal sealed interface DashboardPresentation {
    @Immutable
    data class Unavailable(
        val location: UnavailablePanelPresentation,
        val seasonalHora: UnavailablePanelPresentation,
        val solarAnchors: UnavailablePanelPresentation,
        val calculationStatus: ProvenanceStatusPresentation,
        val rahuStatus: StatusPresentation,
    ) : DashboardPresentation

    @Immutable
    data class Success(
        val location: ProvenanceStatusPresentation,
        val seasonalHora: TimingFolioPresentation,
        val solarAnchors: AnchorRowPresentation,
        val calculationStatus: ProvenanceStatusPresentation,
        val rahuStatus: StatusPresentation,
    ) : DashboardPresentation
}

@Immutable
internal data class DashboardCallbacks(
    val openLocation: () -> Unit,
    val openSettings: () -> Unit,
    val openTimeline: () -> Unit,
    val openMethod: () -> Unit,
)

@Composable
internal fun RuntimeDashboardPresentation(): DashboardPresentation.Unavailable =
    DashboardPresentation.Unavailable(
        location = UnavailablePanelPresentation(
            heading = stringResource(R.string.dashboard_location_unavailable_heading),
            reason = stringResource(R.string.dashboard_location_unavailable_reason),
            supportingText = stringResource(R.string.dashboard_location_unavailable_supporting),
        ),
        seasonalHora = UnavailablePanelPresentation(
            heading = stringResource(R.string.dashboard_hora_unavailable_heading),
            reason = stringResource(R.string.dashboard_hora_unavailable_reason),
            supportingText = stringResource(R.string.dashboard_hora_unavailable_supporting),
        ),
        solarAnchors = UnavailablePanelPresentation(
            heading = stringResource(R.string.dashboard_solar_unavailable_heading),
            reason = stringResource(R.string.dashboard_solar_unavailable_reason),
            supportingText = stringResource(R.string.dashboard_solar_unavailable_supporting),
        ),
        calculationStatus = ProvenanceStatusPresentation(
            heading = stringResource(R.string.dashboard_calculation_status_heading),
            details = listOf(
                io.github.dinujaya77.jyotisha.ui.components.LabelledValuePresentation(
                    stringResource(R.string.dashboard_calculation_last_label),
                    stringResource(R.string.value_not_yet_calculated),
                ),
                io.github.dinujaya77.jyotisha.ui.components.LabelledValuePresentation(
                    stringResource(R.string.dashboard_calculation_zone_label),
                    stringResource(R.string.value_unavailable),
                ),
            ),
            status = StatusPresentation(
                label = stringResource(R.string.dashboard_calculation_profile_label),
                message = stringResource(R.string.dashboard_calculation_profile_unavailable),
                kind = PresentationStatusKind.Unavailable,
            ),
        ),
        rahuStatus = StatusPresentation(
            label = stringResource(R.string.dashboard_rahu_heading),
            message = stringResource(R.string.dashboard_rahu_unapproved),
            kind = PresentationStatusKind.Unavailable,
        ),
    )

@Composable
internal fun DashboardScreen(
    presentation: DashboardPresentation,
    callbacks: DashboardCallbacks,
    innerPadding: PaddingValues,
) {
    CelestialScreenLayout(
        title = stringResource(R.string.screen_dashboard_title),
        testTag = DASHBOARD_SCREEN_TAG,
        innerPadding = innerPadding,
    ) {
        when (presentation) {
            is DashboardPresentation.Unavailable -> UnavailableDashboard(presentation, callbacks)
            is DashboardPresentation.Success -> SuccessDashboard(presentation, callbacks)
        }
    }
}

@Composable
private fun UnavailableDashboard(
    presentation: DashboardPresentation.Unavailable,
    callbacks: DashboardCallbacks,
) {
    CelestialUnavailablePanel(
        presentation = presentation.location,
        primaryActionLabel = stringResource(R.string.action_choose_location),
        onPrimaryAction = callbacks.openLocation,
        secondaryActionLabel = stringResource(R.string.action_settings),
        onSecondaryAction = callbacks.openSettings,
    )
    CelestialUnavailablePanel(
        presentation = presentation.seasonalHora,
        primaryActionLabel = stringResource(R.string.action_view_all_horas),
        onPrimaryAction = callbacks.openTimeline,
    )
    CelestialUnavailablePanel(
        presentation = presentation.solarAnchors,
        secondaryActionLabel = stringResource(R.string.action_method),
        onSecondaryAction = callbacks.openMethod,
    )
    StatusSection(presentation.rahuStatus)
    CelestialProvenanceStatus(presentation.calculationStatus)
}

@Composable
private fun SuccessDashboard(
    presentation: DashboardPresentation.Success,
    callbacks: DashboardCallbacks,
) {
    CelestialProvenanceStatus(presentation.location)
    CelestialTimingFolio(
        presentation = presentation.seasonalHora,
        actionLabel = stringResource(R.string.action_view_all_horas),
        onAction = callbacks.openTimeline,
    )
    CelestialAnchorRow(presentation.solarAnchors)
    StatusSection(presentation.rahuStatus)
    CelestialProvenanceStatus(presentation.calculationStatus)
}

@Composable
private fun StatusSection(status: StatusPresentation) {
    Text(
        text = status.label,
        modifier = Modifier.semantics { heading() },
        style = MaterialTheme.celestialArchiveType.title,
        color = MaterialTheme.celestialArchiveColors.contentPrimary,
    )
    Text(
        text = status.message,
        style = MaterialTheme.celestialArchiveType.body,
        color = MaterialTheme.celestialArchiveColors.contentSecondary,
    )
}
