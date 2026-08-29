package io.github.dinujaya77.jyotisha.ui.location

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.annotation.StringRes
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import io.github.dinujaya77.jyotisha.data.location.LocationFallback
import io.github.dinujaya77.jyotisha.data.location.LocationSelectionWarning
import io.github.dinujaya77.jyotisha.data.location.TownCatalog
import io.github.dinujaya77.jyotisha.platform.location.DeviceLocationUnavailableReason
import io.github.dinujaya77.jyotisha.R
import io.github.dinujaya77.jyotisha.ui.components.CelestialPrimaryTextAction
import io.github.dinujaya77.jyotisha.ui.components.CelestialProvenanceStatus
import io.github.dinujaya77.jyotisha.ui.components.CelestialSecondaryTextAction
import io.github.dinujaya77.jyotisha.ui.components.CelestialUnavailablePanel
import io.github.dinujaya77.jyotisha.ui.components.LabelledValuePresentation
import io.github.dinujaya77.jyotisha.ui.components.ProvenanceStatusPresentation
import io.github.dinujaya77.jyotisha.ui.components.UnavailablePanelPresentation
import io.github.dinujaya77.jyotisha.ui.screens.CelestialScreenLayout
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveColors
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveType

internal const val LOCATION_SCREEN_TAG = "screen_location"

@Composable
internal fun LocationScreen(
    onBack: () -> Unit,
    innerPadding: PaddingValues,
) {
    LocationScreen(
        presentation = RuntimeLocationPresentation(),
        callbacks = LocationCallbacks(onAction = {}, onBack = onBack),
        innerPadding = innerPadding,
    )
}

@Composable
internal fun RuntimeLocationPresentation(): LocationPresentation = LocationPresentation(
    currentLocation = CurrentLocationPresentation.Unavailable(
        UnavailablePanelPresentation(
            heading = stringResource(R.string.location_device_unavailable_heading),
            reason = stringResource(R.string.location_device_unavailable_reason),
            supportingText = stringResource(R.string.location_device_unavailable_supporting),
        ),
    ),
    currentActions = listOf(
        LocationActionPresentation(
            action = LocationAction.UseCurrentLocation,
            label = stringResource(R.string.location_action_use_current),
            enabled = false,
        ),
    ),
    selectedLocation = ProvenanceStatusPresentation(
        heading = stringResource(R.string.location_selected_heading),
        details = listOf(
            LabelledValuePresentation(
                label = stringResource(R.string.location_selected_status_label),
                value = stringResource(R.string.location_selected_none),
            ),
        ),
    ),
    selected = false,
    townCatalogue = UnavailablePanelPresentation(
        heading = stringResource(R.string.location_town_unavailable_heading),
        reason = stringResource(R.string.location_town_unavailable_reason),
        supportingText = stringResource(R.string.location_town_unavailable_supporting),
    ),
    townAction = LocationActionPresentation(
        action = LocationAction.ChooseTown,
        label = stringResource(R.string.location_action_choose_town),
        enabled = false,
    ),
    defaultExplanation = stringResource(R.string.location_default_explanation),
    defaultAction = LocationActionPresentation(
        action = LocationAction.UseDefault,
        label = stringResource(R.string.location_action_use_default),
        enabled = false,
    ),
)

@Composable
internal fun LocationScreen(
    presentation: LocationPresentation,
    callbacks: LocationCallbacks,
    innerPadding: PaddingValues,
) {
    CelestialScreenLayout(
        title = stringResource(R.string.screen_location_title),
        testTag = LOCATION_SCREEN_TAG,
        innerPadding = innerPadding,
    ) {
        LocationSectionHeading(stringResource(R.string.location_use_current_heading))
        when (val currentLocation = presentation.currentLocation) {
            is CurrentLocationPresentation.Available -> {
                CelestialProvenanceStatus(currentLocation.status)
            }
            is CurrentLocationPresentation.Unavailable -> {
                CelestialUnavailablePanel(currentLocation.status)
            }
        }
        presentation.currentActions.forEach { action ->
            LocationTextAction(action, callbacks)
        }

        CelestialProvenanceStatus(
            presentation = presentation.selectedLocation,
            modifier = Modifier
                .testTag("location_selected")
                .semantics { selected = presentation.selected },
        )

        LocationSectionHeading(stringResource(R.string.location_town_heading))
        if (presentation.townSearchEnabled) {
            OutlinedTextField(
                value = presentation.townSearchQuery,
                onValueChange = callbacks.onTownSearchChanged,
                label = { Text(stringResource(R.string.location_town_search_label)) },
                modifier = Modifier.testTag("location_town_search"),
            )
        }
        if (presentation.noMatchingTowns) {
            Text(
                text = stringResource(R.string.location_town_no_matches),
                style = MaterialTheme.celestialArchiveType.body,
                color = MaterialTheme.celestialArchiveColors.contentSecondary,
                modifier = Modifier.testTag("location_town_no_matches"),
            )
        } else if (presentation.towns.isEmpty()) {
            CelestialUnavailablePanel(presentation.townCatalogue)
            LocationTextAction(presentation.townAction, callbacks)
        } else {
            presentation.towns.forEach { town ->
                CelestialSecondaryTextAction(
                    label = town.label,
                    onClick = { callbacks.onTownSelected(town.town.stableId) },
                    modifier = Modifier
                        .testTag("town_${town.town.geonamesId}")
                        .semantics { selected = town.selected },
                )
            }
        }

        LocationSectionHeading(stringResource(R.string.location_default_heading))
        Text(
            text = presentation.defaultExplanation,
            style = MaterialTheme.celestialArchiveType.body,
            color = MaterialTheme.celestialArchiveColors.contentSecondary,
        )
        LocationTextAction(presentation.defaultAction, callbacks)
        CelestialPrimaryTextAction(
            label = stringResource(R.string.action_back),
            onClick = callbacks.onBack,
            modifier = Modifier.testTag("action_back"),
        )
    }
}

@Composable
internal fun RuntimeLocationScreen(
    state: LocationRuntimeState,
    onUseCurrent: () -> Unit,
    onRequestPermission: () -> Unit,
    onTownSelected: (String) -> Unit,
    onUseDefault: () -> Unit,
    onOpenMethod: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenSystemSettings: (DeviceLocationUnavailableReason) -> Unit,
    onBack: () -> Unit,
    innerPadding: PaddingValues,
) {
    var townQuery by androidx.compose.runtime.saveable.rememberSaveable { androidx.compose.runtime.mutableStateOf("") }
    var showTownList by androidx.compose.runtime.saveable.rememberSaveable { androidx.compose.runtime.mutableStateOf(false) }
    val catalogueTowns = TownCatalog.towns.map { town ->
        town to stringResource(town.englishNameResourceKey)
    }
    val filteredTowns = catalogueTowns.filter { (_, label) ->
        label.contains(townQuery, ignoreCase = true)
    }
    val selection = state.selection
    val unavailableReason = (selection?.warning as? LocationSelectionWarning.CURRENT_LOCATION_UNAVAILABLE)
        ?.reason
    val action = when {
        state.acquisition == LocationAcquisition.REQUEST_PERMISSION -> LocationAction.RequestPermission
        unavailableReason in setOf(
            DeviceLocationUnavailableReason.SERVICES_DISABLED,
            DeviceLocationUnavailableReason.PERMISSION_DENIED,
            DeviceLocationUnavailableReason.SECURITY_EXCEPTION,
        ) -> LocationAction.OpenSystemSettings
        unavailableReason != null -> LocationAction.RetryCurrentLocation
        else -> LocationAction.UseCurrentLocation
    }
    if (selection?.isFirstUse == true && !showTownList) {
        FirstUseRuntimeContent(
            action = action,
            acquiring = state.acquisition == LocationAcquisition.ACQUIRING,
            onUseCurrent = onUseCurrent,
            onRequestPermission = onRequestPermission,
            onChooseTown = { showTownList = true },
            onUseDefault = onUseDefault,
            onOpenMethod = onOpenMethod,
            onOpenAbout = onOpenAbout,
            onOpenSystemSettings = { unavailableReason?.let(onOpenSystemSettings) },
            onBack = onBack,
            innerPadding = innerPadding,
        )
        return
    }
    val selectedDescription = when (selection?.fallback) {
        LocationFallback.CURRENT_DEVICE -> deviceStatusLabel(
            saved = false,
            precise = selection.selectedLocation.provenance.permissionPrecision ==
                io.github.dinujaya77.jyotisha.domain.location.PermissionPrecision.PRECISE,
        )
        LocationFallback.SAVED_DEVICE -> deviceStatusLabel(
            saved = true,
            precise = selection.selectedLocation.provenance.permissionPrecision ==
                io.github.dinujaya77.jyotisha.domain.location.PermissionPrecision.PRECISE,
        )
        LocationFallback.MANUAL -> stringResource(selection.selectedTown!!.englishNameResourceKey)
        LocationFallback.DEFAULT -> stringResource(R.string.location_status_default)
        null -> stringResource(R.string.location_selected_none)
    }
    val currentStatus = when {
        state.acquisition == LocationAcquisition.ACQUIRING -> CurrentLocationPresentation.Unavailable(
            UnavailablePanelPresentation(
                heading = stringResource(R.string.location_acquiring_heading),
                reason = stringResource(R.string.location_acquiring_reason),
                supportingText = stringResource(R.string.location_acquiring_supporting),
            ),
        )
        selection?.fallback == LocationFallback.CURRENT_DEVICE ||
            selection?.fallback == LocationFallback.SAVED_DEVICE -> CurrentLocationPresentation.Available(
            ProvenanceStatusPresentation(
                heading = stringResource(R.string.location_current_active_heading),
                details = listOf(
                    LabelledValuePresentation(
                        label = stringResource(R.string.location_selected_status_label),
                        value = selectedDescription,
                    ),
                ),
            ),
        )
        else -> CurrentLocationPresentation.Unavailable(
            UnavailablePanelPresentation(
                heading = stringResource(R.string.location_device_unavailable_heading),
                reason = stringResource(
                    if (selection?.warning is LocationSelectionWarning.CURRENT_LOCATION_UNAVAILABLE) {
                        R.string.location_current_failed_reason
                    } else {
                        R.string.location_device_available_reason
                    },
                ),
                supportingText = stringResource(R.string.location_device_available_supporting),
            ),
        )
    }
    val warningText = selection?.warning?.let { warning ->
        when (warning) {
            LocationSelectionWarning.SAVED_DEVICE_STALE -> stringResource(R.string.location_warning_stale)
            LocationSelectionWarning.DEVICE_DATA_REMOVED_FOR_PERMISSION ->
                stringResource(R.string.location_warning_permission_changed)
            is LocationSelectionWarning.CURRENT_LOCATION_UNAVAILABLE ->
                stringResource(R.string.location_warning_current_failed)
            LocationSelectionWarning.STORAGE_RECOVERED,
            LocationSelectionWarning.INVALID_STORED_SELECTION,
            LocationSelectionWarning.INVALID_TOWN_SELECTION,
            LocationSelectionWarning.INVALID_CURRENT_LOCATION,
            LocationSelectionWarning.DEVICE_ZONE_UNAVAILABLE,
            LocationSelectionWarning.LOCATION_UNCHANGED,
            -> stringResource(R.string.location_warning_recovered)
        }
    }
    LocationScreen(
        presentation = LocationPresentation(
            currentLocation = currentStatus,
            currentActions = listOf(
                LocationActionPresentation(
                    action = action,
                    label = stringResource(currentLocationActionLabel(action)),
                    enabled = state.acquisition != LocationAcquisition.ACQUIRING,
                ),
            ),
            selectedLocation = ProvenanceStatusPresentation(
                heading = stringResource(R.string.location_selected_heading),
                details = listOf(
                    LabelledValuePresentation(
                        label = stringResource(R.string.location_selected_status_label),
                        value = selectedDescription,
                    ),
                ) + listOfNotNull(warningText?.let {
                    LabelledValuePresentation(
                        label = stringResource(R.string.location_selected_warning_label),
                        value = it,
                    )
                }),
            ),
            selected = selection != null,
            townCatalogue = UnavailablePanelPresentation(
                heading = stringResource(R.string.location_town_unavailable_heading),
                reason = stringResource(R.string.location_town_unavailable_reason),
                supportingText = stringResource(R.string.location_town_unavailable_supporting),
            ),
            townAction = LocationActionPresentation(
                action = LocationAction.ChooseTown,
                label = stringResource(R.string.location_action_choose_town),
                enabled = false,
            ),
            defaultExplanation = stringResource(R.string.location_default_runtime_explanation),
            defaultAction = LocationActionPresentation(
                action = LocationAction.UseDefault,
                label = stringResource(R.string.location_action_use_default),
                enabled = true,
            ),
            towns = filteredTowns.map { (town, label) ->
                TownSelectionPresentation(
                    town = town,
                    label = label,
                    selected = selection?.selectedTown?.stableId == town.stableId,
                )
            },
            townSearchEnabled = true,
            townSearchQuery = townQuery,
            noMatchingTowns = filteredTowns.isEmpty(),
        ),
        callbacks = LocationCallbacks(
            onAction = { selectedAction ->
                when (selectedAction) {
                    LocationAction.UseCurrentLocation,
                    LocationAction.RetryCurrentLocation,
                    -> onUseCurrent()
                    LocationAction.RequestPermission -> onRequestPermission()
                    LocationAction.OpenSystemSettings -> unavailableReason?.let(onOpenSystemSettings)
                    LocationAction.UseDefault -> onUseDefault()
                    else -> Unit
                }
            },
            onBack = onBack,
            onTownSelected = onTownSelected,
            onTownSearchChanged = { townQuery = it },
        ),
        innerPadding = innerPadding,
    )
}

@Composable
private fun FirstUseRuntimeContent(
    action: LocationAction,
    acquiring: Boolean,
    onUseCurrent: () -> Unit,
    onRequestPermission: () -> Unit,
    onChooseTown: () -> Unit,
    onUseDefault: () -> Unit,
    onOpenMethod: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenSystemSettings: () -> Unit,
    onBack: () -> Unit,
    innerPadding: PaddingValues,
) {
    CelestialScreenLayout(
        title = stringResource(R.string.first_use_heading),
        testTag = "screen_first_use",
        innerPadding = innerPadding,
    ) {
        Text(
            text = stringResource(R.string.first_use_rationale),
            style = MaterialTheme.celestialArchiveType.body,
            color = MaterialTheme.celestialArchiveColors.contentPrimary,
        )
        Text(
            text = stringResource(R.string.first_use_privacy),
            style = MaterialTheme.celestialArchiveType.body,
            color = MaterialTheme.celestialArchiveColors.contentSecondary,
        )
        CelestialPrimaryTextAction(
            label = stringResource(currentLocationActionLabel(action)),
            onClick = when (action) {
                LocationAction.RequestPermission -> onRequestPermission
                LocationAction.OpenSystemSettings -> onOpenSystemSettings
                else -> onUseCurrent
            },
            modifier = Modifier.testTag(locationActionTestTag(action, !acquiring)),
            enabled = !acquiring,
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.location_action_choose_town),
            onClick = onChooseTown,
            modifier = Modifier.testTag("action_first_use_choose_town"),
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.first_use_continue_default),
            onClick = onUseDefault,
            modifier = Modifier.testTag("action_first_use_default"),
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.action_method),
            onClick = onOpenMethod,
            modifier = Modifier.testTag("action_first_use_method"),
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.action_about),
            onClick = onOpenAbout,
            modifier = Modifier.testTag("action_first_use_about"),
        )
        CelestialPrimaryTextAction(
            label = stringResource(R.string.action_back),
            onClick = onBack,
            modifier = Modifier.testTag("action_back"),
        )
    }
}

@StringRes
private fun currentLocationActionLabel(action: LocationAction): Int = when (action) {
    LocationAction.RequestPermission -> R.string.location_action_request_permission
    LocationAction.RetryCurrentLocation -> R.string.location_action_retry_current
    LocationAction.OpenSystemSettings -> R.string.location_action_open_settings
    else -> R.string.location_action_use_current
}

@Composable
private fun deviceStatusLabel(saved: Boolean, precise: Boolean): String = stringResource(
    when {
        saved && precise -> R.string.location_status_saved_precise
        saved -> R.string.location_status_saved_approximate
        precise -> R.string.location_status_current_precise
        else -> R.string.location_status_current_approximate
    },
)

@Composable
private fun LocationTextAction(
    presentation: LocationActionPresentation,
    callbacks: LocationCallbacks,
) {
    CelestialSecondaryTextAction(
        label = presentation.label,
        onClick = { callbacks.onAction(presentation.action) },
        modifier = Modifier.testTag(
            locationActionTestTag(presentation.action, presentation.enabled),
        ),
        enabled = presentation.enabled,
    )
}

@Composable
private fun LocationSectionHeading(text: String) {
    Text(
        text = text,
        modifier = Modifier.semantics { heading() },
        style = MaterialTheme.celestialArchiveType.title,
        color = MaterialTheme.celestialArchiveColors.contentPrimary,
    )
}
