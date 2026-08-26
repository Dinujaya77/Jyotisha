package io.github.dinujaya77.jyotisha.ui.firstuse

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.github.dinujaya77.jyotisha.R
import io.github.dinujaya77.jyotisha.ui.components.CelestialPrimaryTextAction
import io.github.dinujaya77.jyotisha.ui.components.CelestialSecondaryTextAction
import io.github.dinujaya77.jyotisha.ui.screens.CelestialScreenLayout
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveColors
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveType

/** UI-001 presentation fixture only. It is intentionally not part of the M3 runtime route graph. */
@Composable
internal fun FirstUseStaticScreen(
    openMethod: () -> Unit,
    openAbout: () -> Unit,
    innerPadding: PaddingValues,
) {
    CelestialScreenLayout(
        title = stringResource(R.string.first_use_heading),
        testTag = "screen_first_use_fixture",
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
            label = stringResource(R.string.location_action_use_current),
            onClick = {},
            enabled = false,
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.location_action_choose_town),
            onClick = {},
            enabled = false,
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.first_use_continue_default),
            onClick = {},
            enabled = false,
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.action_method),
            onClick = openMethod,
        )
        CelestialSecondaryTextAction(
            label = stringResource(R.string.action_about),
            onClick = openAbout,
        )
    }
}
