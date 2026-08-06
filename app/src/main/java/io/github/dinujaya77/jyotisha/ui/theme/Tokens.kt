package io.github.dinujaya77.jyotisha.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Exact layout dimensions from THEME-CELESTIAL-ARCHIVE-001. */
internal object CelestialArchiveSpacing {
    val spaceXs = 4.dp
    val spaceSm = 8.dp
    val spaceMd = 12.dp
    val spaceLg = 16.dp
    val spaceXl = 24.dp
    val space2xl = 32.dp
    val space3xl = 48.dp

    val gutterCompact = 16.dp
    val gutterMedium = 24.dp
    val gutterExpanded = 32.dp
}

internal object CelestialArchiveShapes {
    val shapeSmall = 8.dp
    val shapeMedium = 12.dp
    val shapeLarge = 16.dp
    val shapeExtraLarge = 24.dp

    val small = RoundedCornerShape(shapeSmall)
    val medium = RoundedCornerShape(shapeMedium)
    val large = RoundedCornerShape(shapeLarge)
    val extraLarge = RoundedCornerShape(shapeExtraLarge)
}

/** Material shape roles contain only approved semantic Celestial Archive shapes. */
internal val CelestialArchiveMaterialShapes = Shapes(
    extraSmall = CelestialArchiveShapes.small,
    small = CelestialArchiveShapes.small,
    medium = CelestialArchiveShapes.medium,
    large = CelestialArchiveShapes.large,
    extraLarge = CelestialArchiveShapes.extraLarge,
)

internal object CelestialArchiveElevation {
    val elevationBase = 0.dp
    val elevationGrouped = 1.dp
    val elevationTransient = 3.dp
}

/** Sizing plus the non-colour and accessible-name contracts for icon actions. */
internal object CelestialArchiveInteraction {
    val touchTargetMinimum = 48.dp
    val focusOutlineWidth = 2.dp
    val iconGlyphSize = 24.dp
    const val outlinedIconStylePreferred = true
    const val actionableIconRequiresLabelOrAccessibleName = true
    const val stateIconRequiresExplicitText = true
    const val focusRequiresShapeCue = true
}

internal data class VisualConstraintContext(
    val constrained: Boolean = false,
    val highContrast: Boolean = false,
    val reducedMotion: Boolean = false,
    val screenshotFixture: Boolean = false,
    val largeText: Boolean = false,
)

internal sealed interface BackgroundWash {
    data object None : BackgroundWash
    data class Static(val start: Color, val end: Color) : BackgroundWash
}

internal fun backgroundWash(
    darkTheme: Boolean,
    context: VisualConstraintContext = VisualConstraintContext(),
): BackgroundWash {
    if (
        context.constrained || context.highContrast || context.reducedMotion ||
        context.screenshotFixture
    ) {
        return BackgroundWash.None
    }
    val colors = if (darkTheme) DarkCelestialArchiveColors else LightCelestialArchiveColors
    return BackgroundWash.Static(
        start = colors.archiveBackground,
        end = colors.archiveSurface,
    )
}

internal object CelestialArchiveMotion {
    const val motionInstant = 0
    const val motionQuick = 120
    const val motionStandard = 180
    const val motionEmphasized = 240

    val easingStandard = CubicBezierEasing(0.2f, 0f, 0f, 1f)
    val easingExit = CubicBezierEasing(0.4f, 0f, 1f, 1f)

    fun duration(durationMillis: Int, reducedMotion: Boolean): Int =
        if (reducedMotion) motionInstant else durationMillis
}

internal enum class CelestialIllustrationMotif {
    CelestialArc,
    Dot,
    HorizonRule,
    ArchivalLinework,
}

/** Decorative illustration never enters the accessibility tree and yields before content. */
internal object CelestialArchiveIllustration {
    val allowedMotifs = CelestialIllustrationMotif.entries.toSet()
    const val isAccessibilityElement = false

    fun isVisible(context: VisualConstraintContext): Boolean =
        !context.constrained && !context.highContrast && !context.largeText
}
