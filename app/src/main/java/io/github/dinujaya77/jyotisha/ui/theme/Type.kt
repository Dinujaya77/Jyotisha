package io.github.dinujaya77.jyotisha.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private fun archiveTextStyle(
    size: Int,
    lineHeight: Int,
    weight: Int,
) = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight(weight),
    fontSize = size.sp,
    lineHeight = lineHeight.sp,
)

/** Semantic type roles approved for the Celestial Archive theme by DA-004. */
@Immutable
class CelestialArchiveTypeScale internal constructor(
    val display: TextStyle,
    val headline: TextStyle,
    val title: TextStyle,
    val body: TextStyle,
    val bodySmall: TextStyle,
    val label: TextStyle,
    val timePrimary: TextStyle,
)

internal val CelestialArchiveType = CelestialArchiveTypeScale(
    display = archiveTextStyle(size = 40, lineHeight = 48, weight = 600),
    headline = archiveTextStyle(size = 28, lineHeight = 36, weight = 600),
    title = archiveTextStyle(size = 22, lineHeight = 28, weight = 600),
    body = archiveTextStyle(size = 16, lineHeight = 24, weight = 400),
    bodySmall = archiveTextStyle(size = 14, lineHeight = 20, weight = 400),
    label = archiveTextStyle(size = 14, lineHeight = 20, weight = 600),
    timePrimary = archiveTextStyle(size = 32, lineHeight = 40, weight = 600),
)

/** Material roles deliberately map to the approved semantic hierarchy. */
internal val Typography = Typography(
    displayLarge = CelestialArchiveType.display,
    displayMedium = CelestialArchiveType.display,
    displaySmall = CelestialArchiveType.display,
    headlineLarge = CelestialArchiveType.headline,
    headlineMedium = CelestialArchiveType.headline,
    headlineSmall = CelestialArchiveType.headline,
    titleLarge = CelestialArchiveType.title,
    titleMedium = CelestialArchiveType.title,
    titleSmall = CelestialArchiveType.title,
    bodyLarge = CelestialArchiveType.body,
    bodyMedium = CelestialArchiveType.body,
    bodySmall = CelestialArchiveType.bodySmall,
    labelLarge = CelestialArchiveType.label,
    labelMedium = CelestialArchiveType.label,
    labelSmall = CelestialArchiveType.label,
)
