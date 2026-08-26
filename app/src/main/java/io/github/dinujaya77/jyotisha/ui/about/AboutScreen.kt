package io.github.dinujaya77.jyotisha.ui.about

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.selection.SelectionContainer
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

internal const val ABOUT_SCREEN_TAG = "screen_about"

@Composable
internal fun AboutScreen(
    openMethod: () -> Unit,
    onBackToSettings: () -> Unit,
    innerPadding: PaddingValues,
) {
    CelestialScreenLayout(
        title = stringResource(R.string.screen_about_title),
        testTag = ABOUT_SCREEN_TAG,
        innerPadding = innerPadding,
    ) {
        AboutSection(
            heading = stringResource(R.string.about_identity_heading),
            body = stringResource(R.string.about_identity_body, stringResource(R.string.app_name)),
        )
        AboutSection(
            heading = stringResource(R.string.about_purpose_heading),
            body = stringResource(R.string.about_purpose_body),
        )
        AboutSection(
            heading = stringResource(R.string.about_location_heading),
            body = stringResource(R.string.about_location_body),
        )
        AboutSection(
            heading = stringResource(R.string.about_sources_heading),
            body = stringResource(R.string.about_sources_body),
        )
        AboutSection(
            heading = stringResource(R.string.about_limitations_heading),
            body = stringResource(R.string.about_limitations_body),
        )
        AboutSection(
            heading = stringResource(R.string.about_release_heading),
            body = stringResource(R.string.about_release_body),
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.action_method),
            onClick = openMethod,
            modifier = Modifier.testTag("action_method"),
        )
        CelestialPrimaryTextAction(
            label = stringResource(R.string.action_back_to_settings),
            onClick = onBackToSettings,
            modifier = Modifier.testTag("action_back_to_settings"),
        )
    }
}

@Composable
private fun AboutSection(heading: String, body: String) {
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
