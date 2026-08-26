package io.github.dinujaya77.jyotisha.ui.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import io.github.dinujaya77.jyotisha.R
import io.github.dinujaya77.jyotisha.ui.components.CelestialPrimaryTextAction
import io.github.dinujaya77.jyotisha.ui.components.CelestialSecondaryTextAction
import io.github.dinujaya77.jyotisha.ui.screens.CelestialScreenLayout
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveColors
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveType

internal const val SETTINGS_SCREEN_TAG = "screen_settings"

@Composable
internal fun SettingsScreen(
    openLocation: () -> Unit,
    openMethod: () -> Unit,
    openAbout: () -> Unit,
    onBack: () -> Unit,
    innerPadding: PaddingValues,
) {
    CelestialScreenLayout(
        title = stringResource(R.string.screen_settings_title),
        testTag = SETTINGS_SCREEN_TAG,
        innerPadding = innerPadding,
    ) {
        ReadOnlySettingSection(
            heading = stringResource(R.string.settings_location_heading),
            body = stringResource(R.string.settings_location_body),
        )
        CelestialPrimaryTextAction(
            label = stringResource(R.string.settings_action_change_location),
            onClick = openLocation,
            modifier = Modifier.testTag("action_location"),
        )
        ReadOnlySettingSection(
            heading = stringResource(R.string.settings_appearance_heading),
            body = stringResource(R.string.settings_appearance_body),
        )
        ReadOnlySettingSection(
            heading = stringResource(R.string.settings_accessibility_heading),
            body = stringResource(R.string.settings_accessibility_body),
        )
        ReadOnlySettingSection(
            heading = stringResource(R.string.settings_calculation_heading),
            body = stringResource(R.string.settings_calculation_body),
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.action_method),
            onClick = openMethod,
        )
        ReadOnlySettingSection(
            heading = stringResource(R.string.settings_privacy_heading),
            body = stringResource(R.string.settings_privacy_body),
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.settings_action_reset_location),
            onClick = {},
            modifier = Modifier.testTag("action_reset_location_disabled"),
            enabled = false,
        )
        CelestialPrimaryTextAction(
            label = stringResource(R.string.action_about),
            onClick = openAbout,
            modifier = Modifier.testTag("action_about"),
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.action_back),
            onClick = onBack,
            modifier = Modifier.testTag("action_back"),
        )
    }
}

@Composable
private fun ReadOnlySettingSection(heading: String, body: String) {
    Text(
        text = heading,
        modifier = Modifier.semantics { heading() },
        style = MaterialTheme.celestialArchiveType.title,
        color = MaterialTheme.celestialArchiveColors.contentPrimary,
    )
    Text(
        text = body,
        style = MaterialTheme.celestialArchiveType.body,
        color = MaterialTheme.celestialArchiveColors.contentSecondary,
    )
}
