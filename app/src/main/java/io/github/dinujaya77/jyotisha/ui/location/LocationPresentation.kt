package io.github.dinujaya77.jyotisha.ui.location

import androidx.compose.runtime.Immutable
import io.github.dinujaya77.jyotisha.data.location.TownRecord
import io.github.dinujaya77.jyotisha.ui.components.ProvenanceStatusPresentation
import io.github.dinujaya77.jyotisha.ui.components.UnavailablePanelPresentation

internal enum class LocationAction {
    UseCurrentLocation,
    RequestPermission,
    OpenSystemSettings,
    RetryCurrentLocation,
    ImprovePrecision,
    ChooseTown,
    UseDefault,
}

@Immutable
internal data class LocationActionPresentation(
    val action: LocationAction,
    val label: String,
    val enabled: Boolean,
) {
    init {
        require(label.isNotBlank())
    }
}

@Immutable
internal sealed interface CurrentLocationPresentation {
    @Immutable
    data class Available(val status: ProvenanceStatusPresentation) : CurrentLocationPresentation

    @Immutable
    data class Unavailable(val status: UnavailablePanelPresentation) : CurrentLocationPresentation
}

@Immutable
internal data class LocationPresentation(
    val currentLocation: CurrentLocationPresentation,
    val currentActions: List<LocationActionPresentation>,
    val selectedLocation: ProvenanceStatusPresentation,
    val selected: Boolean,
    val townCatalogue: UnavailablePanelPresentation,
    val townAction: LocationActionPresentation,
    val defaultExplanation: String,
    val defaultAction: LocationActionPresentation,
    val towns: List<TownSelectionPresentation> = emptyList(),
    val townSearchEnabled: Boolean = false,
    val townSearchQuery: String = "",
    val noMatchingTowns: Boolean = false,
) {
    init {
        require(currentActions.isNotEmpty())
        require(currentActions.none { it.action == LocationAction.ChooseTown })
        require(currentActions.none { it.action == LocationAction.UseDefault })
        require(townAction.action == LocationAction.ChooseTown)
        require(defaultAction.action == LocationAction.UseDefault)
        require(defaultExplanation.isNotBlank())
    }
}

@Immutable
internal data class TownSelectionPresentation(
    val town: TownRecord,
    val label: String,
    val selected: Boolean,
)

@Immutable
internal data class LocationCallbacks(
    val onAction: (LocationAction) -> Unit,
    val onBack: () -> Unit,
    val onTownSelected: (String) -> Unit = {},
    val onTownSearchChanged: (String) -> Unit = {},
)

internal fun locationActionTestTag(action: LocationAction, enabled: Boolean): String = when (action) {
    LocationAction.UseCurrentLocation -> if (enabled) {
        "action_use_current"
    } else {
        "action_use_current_disabled"
    }
    LocationAction.RequestPermission -> "action_request_location_permission"
    LocationAction.OpenSystemSettings -> "action_open_location_settings"
    LocationAction.RetryCurrentLocation -> "action_retry_current_location"
    LocationAction.ImprovePrecision -> "action_improve_location_precision"
    LocationAction.ChooseTown -> if (enabled) {
        "action_choose_town"
    } else {
        "action_choose_town_disabled"
    }
    LocationAction.UseDefault -> if (enabled) {
        "action_use_default"
    } else {
        "action_use_default_disabled"
    }
}
