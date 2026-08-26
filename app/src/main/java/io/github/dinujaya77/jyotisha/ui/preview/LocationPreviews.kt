package io.github.dinujaya77.jyotisha.ui.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.dinujaya77.jyotisha.ui.components.LabelledValuePresentation
import io.github.dinujaya77.jyotisha.ui.components.PresentationStatusKind
import io.github.dinujaya77.jyotisha.ui.components.ProvenanceStatusPresentation
import io.github.dinujaya77.jyotisha.ui.components.StatusPresentation
import io.github.dinujaya77.jyotisha.ui.components.UnavailablePanelPresentation
import io.github.dinujaya77.jyotisha.ui.firstuse.FirstUseStaticScreen
import io.github.dinujaya77.jyotisha.ui.location.CurrentLocationPresentation
import io.github.dinujaya77.jyotisha.ui.location.LocationAction
import io.github.dinujaya77.jyotisha.ui.location.LocationActionPresentation
import io.github.dinujaya77.jyotisha.ui.location.LocationCallbacks
import io.github.dinujaya77.jyotisha.ui.location.LocationPresentation
import io.github.dinujaya77.jyotisha.ui.location.LocationScreen
import io.github.dinujaya77.jyotisha.ui.theme.JyotishaTheme

internal enum class LocationPreviewState {
    Precise,
    Approximate,
    Saved,
    Manual,
    Default,
    PermissionDenied,
    ServicesDisabled,
    Timeout,
    Invalid,
    Stale,
}

@Immutable
internal data class LocationStateFixture(
    val state: LocationPreviewState,
    val label: String,
    val presentation: LocationPresentation,
    val synthetic: Boolean = true,
)

private val unavailableTownCatalogue = UnavailablePanelPresentation(
    heading = "Town catalogue unavailable — synthetic preview",
    reason = "No approved town rows are included in this synthetic state fixture.",
    supportingText = "Town selection stays disabled; current-location and default paths remain separate.",
)

private fun action(
    action: LocationAction,
    label: String,
    enabled: Boolean = true,
) = LocationActionPresentation(action, label, enabled)

private fun selectedLocation(
    source: String,
    provenance: String,
    selected: Boolean = true,
    caution: String? = null,
) = ProvenanceStatusPresentation(
    heading = "Selected location — synthetic preview",
    details = listOf(
        LabelledValuePresentation(
            "Selection",
            if (selected) "Selected — synthetic fixture" else "Not selected — synthetic fixture",
        ),
        LabelledValuePresentation("Source", source),
        LabelledValuePresentation("Provenance", provenance),
    ),
    status = caution?.let {
        StatusPresentation("Attention", it, PresentationStatusKind.Caution)
    },
)

private fun availableCurrentLocation(
    state: String,
    detail: String,
    caution: Boolean = false,
) = CurrentLocationPresentation.Available(
    ProvenanceStatusPresentation(
        heading = "Current-location status — synthetic preview",
        details = listOf(
            LabelledValuePresentation("State", state),
            LabelledValuePresentation("Detail", detail),
        ),
        status = StatusPresentation(
            label = if (caution) "Attention" else "Available",
            message = if (caution) detail else "Synthetic presentation evidence only.",
            kind = if (caution) {
                PresentationStatusKind.Caution
            } else {
                PresentationStatusKind.Information
            },
        ),
    ),
)

private fun unavailableCurrentLocation(
    heading: String,
    reason: String,
    supportingText: String,
) = CurrentLocationPresentation.Unavailable(
    UnavailablePanelPresentation(heading, reason, supportingText),
)

private fun fixture(
    state: LocationPreviewState,
    currentLocation: CurrentLocationPresentation,
    currentAction: LocationActionPresentation,
    selectedLocation: ProvenanceStatusPresentation,
    selected: Boolean,
    defaultSelected: Boolean = false,
) = LocationStateFixture(
    state = state,
    label = "Synthetic ${state.name} location presentation",
    presentation = LocationPresentation(
        currentLocation = currentLocation,
        currentActions = listOf(currentAction),
        selectedLocation = selectedLocation,
        selected = selected,
        townCatalogue = unavailableTownCatalogue,
        townAction = action(
            LocationAction.ChooseTown,
            "Choose a town — unavailable synthetic catalogue",
            enabled = false,
        ),
        defaultExplanation = "The Colombo default is shown only as a synthetic recovery-path presentation.",
        defaultAction = action(
            LocationAction.UseDefault,
            if (defaultSelected) {
                "Synthetic Colombo default selected"
            } else {
                "Use synthetic Colombo default"
            },
            enabled = !defaultSelected,
        ),
    ),
)

internal val locationStateFixtures = listOf(
    fixture(
        LocationPreviewState.Precise,
        availableCurrentLocation(
            "Precise location available",
            "Fresh precise synthetic device state; no coordinate is displayed.",
        ),
        action(LocationAction.UseCurrentLocation, "Refresh synthetic precise location"),
        selectedLocation("Precise", "Fresh synthetic device location"),
        selected = true,
    ),
    fixture(
        LocationPreviewState.Approximate,
        availableCurrentLocation(
            "Approximate location available",
            "Approximate synthetic device state with precision labelled in text.",
        ),
        action(LocationAction.UseCurrentLocation, "Refresh synthetic approximate location"),
        selectedLocation("Approximate", "Fresh synthetic approximate device location"),
        selected = true,
    ),
    fixture(
        LocationPreviewState.Saved,
        availableCurrentLocation(
            "Saved device location available",
            "A synthetic saved-device source is selected and may be refreshed explicitly.",
        ),
        action(LocationAction.UseCurrentLocation, "Refresh synthetic saved device location"),
        selectedLocation("Saved device location", "Synthetic saved source; age 2 hours"),
        selected = true,
    ),
    fixture(
        LocationPreviewState.Manual,
        availableCurrentLocation(
            "Current location alternative available",
            "The manually selected synthetic source remains active until an explicit change.",
        ),
        action(LocationAction.UseCurrentLocation, "Use synthetic current location"),
        selectedLocation("Manually selected", "Synthetic reviewed-town presentation"),
        selected = true,
    ),
    fixture(
        LocationPreviewState.Default,
        availableCurrentLocation(
            "Current location alternative available",
            "The synthetic source remains clearly labelled as Default location.",
        ),
        action(LocationAction.UseCurrentLocation, "Use synthetic current location"),
        selectedLocation("Default location", "Synthetic Colombo default presentation"),
        selected = true,
        defaultSelected = true,
    ),
    fixture(
        LocationPreviewState.PermissionDenied,
        unavailableCurrentLocation(
            "Current location permission denied — synthetic preview",
            "Current location remains unavailable after the synthetic permission denial.",
            "Permission is requested again only after the user activates the labelled action.",
        ),
        action(LocationAction.RequestPermission, "Request location permission — synthetic"),
        selectedLocation("None", "No synthetic fallback selected", selected = false),
        selected = false,
    ),
    fixture(
        LocationPreviewState.ServicesDisabled,
        unavailableCurrentLocation(
            "Location services disabled — synthetic preview",
            "Device location is unavailable while location services are off.",
            "System Settings opens only after the labelled user action; default remains available.",
        ),
        action(LocationAction.OpenSystemSettings, "Open location settings — synthetic"),
        selectedLocation("None", "No synthetic fallback selected", selected = false),
        selected = false,
    ),
    fixture(
        LocationPreviewState.Timeout,
        unavailableCurrentLocation(
            "Current location timed out — synthetic preview",
            "The synthetic provider attempt ended without a usable result.",
            "Retry is explicit; late results are not represented as selected.",
        ),
        action(LocationAction.RetryCurrentLocation, "Retry synthetic current location"),
        selectedLocation(
            "Saved device location",
            "Synthetic retained fallback; age 4 hours",
            caution = "Current-location refresh failed; retained source remains labelled.",
        ),
        selected = true,
    ),
    fixture(
        LocationPreviewState.Invalid,
        unavailableCurrentLocation(
            "Invalid location rejected — synthetic preview",
            "The unusable synthetic value is rejected without displaying private coordinates.",
            "Choose another source; no invalid value becomes selected.",
        ),
        action(LocationAction.UseCurrentLocation, "Choose another synthetic current location"),
        selectedLocation("None", "Invalid synthetic value rejected", selected = false),
        selected = false,
    ),
    fixture(
        LocationPreviewState.Stale,
        availableCurrentLocation(
            "Saved device location stale",
            "The synthetic saved source is older than the approved freshness threshold.",
            caution = true,
        ),
        action(LocationAction.RetryCurrentLocation, "Refresh stale synthetic location"),
        selectedLocation(
            "Saved device location",
            "Synthetic saved source; stale age 25 hours",
            caution = "Stale — refresh is available and the source is not presented as a town.",
        ),
        selected = true,
    ),
)

internal class LocationStatePreviewProvider : PreviewParameterProvider<LocationStateFixture> {
    override val values: Sequence<LocationStateFixture> = locationStateFixtures.asSequence()
}

@Preview(name = "Location synthetic state matrix", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun LocationStatePreview(
    @PreviewParameter(LocationStatePreviewProvider::class) fixture: LocationStateFixture,
) {
    JyotishaTheme {
        LocationScreen(
            presentation = fixture.presentation,
            callbacks = LocationCallbacks(onAction = {}, onBack = {}),
            innerPadding = PaddingValues(),
        )
    }
}

@Preview(name = "Location runtime unavailable", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun LocationUnavailablePreview() {
    JyotishaTheme {
        LocationScreen(onBack = {}, innerPadding = PaddingValues())
    }
}

@Preview(name = "First-use isolated fixture", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun FirstUseFixturePreview() {
    JyotishaTheme {
        FirstUseStaticScreen(openMethod = {}, openAbout = {}, innerPadding = PaddingValues())
    }
}
