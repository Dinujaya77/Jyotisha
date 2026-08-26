package io.github.dinujaya77.jyotisha.ui.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.dinujaya77.jyotisha.ui.components.AnchorRowPresentation
import io.github.dinujaya77.jyotisha.ui.components.LabelledValuePresentation
import io.github.dinujaya77.jyotisha.ui.components.PresentationStatusKind
import io.github.dinujaya77.jyotisha.ui.components.ProvenanceStatusPresentation
import io.github.dinujaya77.jyotisha.ui.components.StatusPresentation
import io.github.dinujaya77.jyotisha.ui.components.TimingFolioPresentation
import io.github.dinujaya77.jyotisha.ui.dashboard.DashboardCallbacks
import io.github.dinujaya77.jyotisha.ui.dashboard.DashboardPresentation
import io.github.dinujaya77.jyotisha.ui.dashboard.DashboardScreen
import io.github.dinujaya77.jyotisha.ui.theme.JyotishaTheme

@Preview(name = "Dashboard synthetic success", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun DashboardSyntheticSuccessPreview() {
    JyotishaTheme {
        DashboardScreen(
            presentation = syntheticDashboardSuccessFixture(),
            callbacks = DashboardCallbacks({}, {}, {}, {}),
            innerPadding = PaddingValues(),
        )
    }
}

internal fun syntheticDashboardSuccessFixture(): DashboardPresentation.Success =
    DashboardPresentation.Success(
        location = ProvenanceStatusPresentation(
            heading = "Synthetic location context",
            details = listOf(
                LabelledValuePresentation("Location", "Preview town"),
                LabelledValuePresentation("Source", "Synthetic preview fixture"),
            ),
        ),
        seasonalHora = TimingFolioPresentation(
            heading = "Seasonal Planetary Hora — synthetic preview",
            primaryValue = "Sun",
            start = LabelledValuePresentation("Start", "06:12"),
            end = LabelledValuePresentation("End", "07:14"),
            remaining = LabelledValuePresentation("Remaining", "42 minutes"),
            next = LabelledValuePresentation("Next", "Venus at 07:14"),
            spokenSummary = "Synthetic Seasonal Planetary Hora, Sun, from 06:12 to 07:14, 42 minutes remaining, next Venus at 07:14.",
        ),
        solarAnchors = AnchorRowPresentation(
            heading = "Calculated astronomical anchors — synthetic preview",
            anchors = listOf(
                LabelledValuePresentation("Sunrise", "06:12"),
                LabelledValuePresentation("Sunset", "18:18"),
            ),
        ),
        calculationStatus = ProvenanceStatusPresentation(
            heading = "Synthetic calculation status",
            details = listOf(
                LabelledValuePresentation(
                    "Calculated",
                    "Synthetic 2026-08-02 12:34:00",
                ),
                LabelledValuePresentation(
                    "Active zone",
                    "Asia/Colombo — synthetic fixture",
                ),
                LabelledValuePresentation(
                    "Profile",
                    "CP-001-v1.0 — synthetic fixture",
                ),
            ),
        ),
        rahuStatus = StatusPresentation(
            label = "Daytime Rahu",
            message = "Calculation method not approved",
            kind = PresentationStatusKind.Unavailable,
        ),
    )
