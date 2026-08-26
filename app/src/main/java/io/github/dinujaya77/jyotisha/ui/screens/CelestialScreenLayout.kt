package io.github.dinujaya77.jyotisha.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import io.github.dinujaya77.jyotisha.ui.theme.CelestialArchiveSpacing
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveColors
import io.github.dinujaya77.jyotisha.ui.theme.celestialArchiveType

@Composable
internal fun CelestialScreenLayout(
    title: String,
    testTag: String,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val scrollState = key(testTag) { rememberScrollState() }
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        val horizontalGutter = when {
            maxWidth >= 840.dp -> CelestialArchiveSpacing.gutterExpanded
            maxWidth >= 600.dp -> CelestialArchiveSpacing.gutterMedium
            else -> CelestialArchiveSpacing.gutterCompact
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = horizontalGutter, vertical = CelestialArchiveSpacing.spaceXl)
                .testTag(testTag),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 720.dp),
                verticalArrangement = Arrangement.spacedBy(CelestialArchiveSpacing.spaceLg),
            ) {
                Text(
                    text = title,
                    modifier = Modifier.semantics { heading() },
                    style = MaterialTheme.celestialArchiveType.headline,
                    color = MaterialTheme.celestialArchiveColors.contentPrimary,
                )
                content()
            }
        }
    }
}
