package io.github.dinujaya77.jyotisha.ui.location

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
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
        CelestialUnavailablePanel(presentation.townCatalogue)
        LocationTextAction(presentation.townAction, callbacks)

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
