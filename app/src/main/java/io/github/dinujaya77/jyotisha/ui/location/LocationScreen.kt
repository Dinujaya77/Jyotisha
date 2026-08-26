package io.github.dinujaya77.jyotisha.ui.location

import androidx.compose.foundation.layout.PaddingValues
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
import io.github.dinujaya77.jyotisha.ui.components.CelestialSecondaryTextAction
import io.github.dinujaya77.jyotisha.ui.components.CelestialUnavailablePanel
import io.github.dinujaya77.jyotisha.ui.components.UnavailablePanelPresentation
import io.github.dinujaya77.jyotisha.ui.screens.CelestialScreenLayout
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveColors
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveType

internal const val LOCATION_SCREEN_TAG = "screen_location"

@Immutable
internal data class LocationRuntimeCapabilities(
    val canAcquireDeviceLocation: Boolean = false,
    val hasApprovedTownCatalogue: Boolean = false,
    val canPersistSelection: Boolean = false,
) {
    init {
        require(!canAcquireDeviceLocation)
        require(!hasApprovedTownCatalogue)
        require(!canPersistSelection)
    }
}

@Composable
internal fun LocationScreen(
    onBack: () -> Unit,
    innerPadding: PaddingValues,
    capabilities: LocationRuntimeCapabilities = LocationRuntimeCapabilities(),
) {
    CelestialScreenLayout(
        title = stringResource(R.string.screen_location_title),
        testTag = LOCATION_SCREEN_TAG,
        innerPadding = innerPadding,
    ) {
        LocationSectionHeading(stringResource(R.string.location_use_current_heading))
        CelestialUnavailablePanel(
            presentation = UnavailablePanelPresentation(
                heading = stringResource(R.string.location_device_unavailable_heading),
                reason = stringResource(R.string.location_device_unavailable_reason),
                supportingText = stringResource(R.string.location_device_unavailable_supporting),
            ),
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.location_action_use_current),
            onClick = {},
            enabled = capabilities.canAcquireDeviceLocation,
        )

        LocationSectionHeading(stringResource(R.string.location_selected_heading))
        Text(
            text = stringResource(R.string.location_selected_none),
            style = MaterialTheme.celestialArchiveType.body,
            color = MaterialTheme.celestialArchiveColors.contentSecondary,
        )

        LocationSectionHeading(stringResource(R.string.location_town_heading))
        CelestialUnavailablePanel(
            presentation = UnavailablePanelPresentation(
                heading = stringResource(R.string.location_town_unavailable_heading),
                reason = stringResource(R.string.location_town_unavailable_reason),
                supportingText = stringResource(R.string.location_town_unavailable_supporting),
            ),
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.location_action_choose_town),
            onClick = {},
            enabled = capabilities.hasApprovedTownCatalogue && capabilities.canPersistSelection,
        )

        LocationSectionHeading(stringResource(R.string.location_default_heading))
        Text(
            text = stringResource(R.string.location_default_explanation),
            style = MaterialTheme.celestialArchiveType.body,
            color = MaterialTheme.celestialArchiveColors.contentSecondary,
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.location_action_use_default),
            onClick = {},
            enabled = capabilities.canPersistSelection,
        )
        CelestialPrimaryTextAction(
            label = stringResource(R.string.action_back),
            onClick = onBack,
            modifier = Modifier.testTag("action_back"),
        )
    }
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
