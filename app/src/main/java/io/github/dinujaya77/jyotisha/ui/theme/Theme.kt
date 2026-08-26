package io.github.dinujaya77.jyotisha.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalCelestialArchiveColors = staticCompositionLocalOf {
    LightCelestialArchiveColors
}

private val LocalCelestialArchiveType = staticCompositionLocalOf {
    CelestialArchiveType
}

val MaterialTheme.celestialArchiveColors: CelestialArchiveColors
    @Composable
    @ReadOnlyComposable
    get() = LocalCelestialArchiveColors.current

val MaterialTheme.celestialArchiveType: CelestialArchiveTypeScale
    @Composable
    @ReadOnlyComposable
    get() = LocalCelestialArchiveType.current

internal val CelestialArchiveLightColorScheme = celestialArchiveLightColorScheme()
internal val CelestialArchiveDarkColorScheme = celestialArchiveDarkColorScheme()

internal fun celestialArchiveColorScheme(darkTheme: Boolean): ColorScheme =
    if (darkTheme) CelestialArchiveDarkColorScheme else CelestialArchiveLightColorScheme

@Composable
fun JyotishaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val semanticColors = if (darkTheme) {
        DarkCelestialArchiveColors
    } else {
        LightCelestialArchiveColors
    }

    CompositionLocalProvider(
        LocalCelestialArchiveColors provides semanticColors,
        LocalCelestialArchiveType provides CelestialArchiveType,
    ) {
        MaterialTheme(
            colorScheme = celestialArchiveColorScheme(darkTheme),
            typography = Typography,
            shapes = CelestialArchiveMaterialShapes,
            content = content,
        )
    }
}

private fun celestialArchiveLightColorScheme(): ColorScheme {
    val colors = LightCelestialArchiveColors
    val inverse = DarkCelestialArchiveColors
    return lightColorScheme(
        primary = colors.actionPrimary,
        onPrimary = colors.onActionPrimary,
        primaryContainer = colors.archiveSurfaceVariant,
        onPrimaryContainer = colors.contentPrimary,
        inversePrimary = inverse.actionPrimary,
        secondary = colors.actionSecondary,
        onSecondary = colors.onActionSecondary,
        secondaryContainer = colors.statusInfoContainer,
        onSecondaryContainer = colors.statusInfoContent,
        tertiary = colors.actionSecondary,
        onTertiary = colors.onActionSecondary,
        tertiaryContainer = colors.statusActiveContainer,
        onTertiaryContainer = colors.statusActiveContent,
        background = colors.archiveBackground,
        onBackground = colors.contentPrimary,
        surface = colors.archiveSurface,
        onSurface = colors.contentPrimary,
        surfaceVariant = colors.archiveSurfaceVariant,
        onSurfaceVariant = colors.contentSecondary,
        surfaceTint = colors.actionPrimary,
        inverseSurface = inverse.archiveSurface,
        inverseOnSurface = inverse.contentPrimary,
        error = colors.statusErrorContent,
        onError = colors.statusErrorContainer,
        errorContainer = colors.statusErrorContainer,
        onErrorContainer = colors.statusErrorContent,
        outline = colors.outline,
        outlineVariant = colors.outline,
        scrim = colors.contentPrimary,
        surfaceBright = colors.archiveSurface,
        surfaceDim = colors.archiveSurfaceVariant,
        surfaceContainer = colors.archiveSurface,
        surfaceContainerHigh = colors.archiveSurfaceVariant,
        surfaceContainerHighest = colors.archiveSurfaceVariant,
        surfaceContainerLow = colors.archiveBackground,
        surfaceContainerLowest = colors.archiveSurface,
    )
}

private fun celestialArchiveDarkColorScheme(): ColorScheme {
    val colors = DarkCelestialArchiveColors
    val inverse = LightCelestialArchiveColors
    return darkColorScheme(
        primary = colors.actionPrimary,
        onPrimary = colors.onActionPrimary,
        primaryContainer = colors.archiveSurfaceVariant,
        onPrimaryContainer = colors.contentPrimary,
        inversePrimary = inverse.actionPrimary,
        secondary = colors.actionSecondary,
        onSecondary = colors.onActionSecondary,
        secondaryContainer = colors.statusInfoContainer,
        onSecondaryContainer = colors.statusInfoContent,
        tertiary = colors.actionSecondary,
        onTertiary = colors.onActionSecondary,
        tertiaryContainer = colors.statusActiveContainer,
        onTertiaryContainer = colors.statusActiveContent,
        background = colors.archiveBackground,
        onBackground = colors.contentPrimary,
        surface = colors.archiveSurface,
        onSurface = colors.contentPrimary,
        surfaceVariant = colors.archiveSurfaceVariant,
        onSurfaceVariant = colors.contentSecondary,
        surfaceTint = colors.actionPrimary,
        inverseSurface = inverse.archiveSurface,
        inverseOnSurface = inverse.contentPrimary,
        error = colors.statusErrorContent,
        onError = colors.statusErrorContainer,
        errorContainer = colors.statusErrorContainer,
        onErrorContainer = colors.statusErrorContent,
        outline = colors.outline,
        outlineVariant = colors.outline,
        scrim = colors.contentPrimary,
        surfaceBright = colors.archiveSurfaceVariant,
        surfaceDim = colors.archiveBackground,
        surfaceContainer = colors.archiveSurface,
        surfaceContainerHigh = colors.archiveSurfaceVariant,
        surfaceContainerHighest = colors.archiveSurfaceVariant,
        surfaceContainerLow = colors.archiveBackground,
        surfaceContainerLowest = colors.archiveBackground,
    )
}
