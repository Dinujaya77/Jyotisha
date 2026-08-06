package io.github.dinujaya77.jyotisha.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt

/** V1-M2-01: FR-011; NFR-005, NFR-011; T-FR-011; T-NFR-A11Y. */
class CelestialArchiveColorTest {

    @Test
    fun approvedSemanticRoleValuesAreExactInBothModes() {
        assertSemanticValues(
            LightCelestialArchiveColors,
            0xFFF7F1E5, 0xFFFFF9EF, 0xFFEDE4D4, 0xFF201C17, 0xFF514A40,
            0xFF6E5A1E, 0xFFFFFFFF, 0xFF334563, 0xFFFFFFFF,
            0xFFE7EFE5, 0xFF1D3A22, 0xFFFFF0CC, 0xFF4B3400,
            0xFFFCE8E6, 0xFF5B1A18, 0xFFE7EEF8, 0xFF17324E,
            0xFFECE8E0, 0xFF3E3932, 0xFF776F63, 0xFF334563,
        )
        assertSemanticValues(
            DarkCelestialArchiveColors,
            0xFF101827, 0xFF182235, 0xFF263248, 0xFFF4EBDD, 0xFFCCC3B5,
            0xFFD8BD69, 0xFF302700, 0xFFB7C7E6, 0xFF18263C,
            0xFF193521, 0xFFD7F0DA, 0xFF3D2E07, 0xFFFFE5A6,
            0xFF5B1917, 0xFFFFDAD6, 0xFF183047, 0xFFD6E4FA,
            0xFF302D29, 0xFFE2DDD4, 0xFF928B80, 0xFFB7C7E6,
        )
    }

    @Test
    fun everySemanticRoleHasEquivalentLightAndDarkDefinitions() {
        val light = LightCelestialArchiveColors.roleValues()
        val dark = DarkCelestialArchiveColors.roleValues()

        assertEquals(light.keys, dark.keys)
        assertEquals(21, light.size)
        assertTrue(light.values.none { it == Color.Unspecified })
        assertTrue(dark.values.none { it == Color.Unspecified })
        assertTrue(light.keys.all { light.getValue(it) != dark.getValue(it) })
    }

    @Test
    fun timeStateAliasesAreDeterministicAndIntroduceNoNewColors() {
        listOf(LightCelestialArchiveColors, DarkCelestialArchiveColors).forEach { colors ->
            assertEquals(colors.statusInfoContainer, colors.statusLaterContainer)
            assertEquals(colors.statusInfoContent, colors.statusLaterContent)
            assertEquals(colors.statusInfoContainer, colors.statusPendingContainer)
            assertEquals(colors.statusInfoContent, colors.statusPendingContent)
            assertEquals(colors.statusUnavailableContainer, colors.statusCompletedContainer)
            assertEquals(colors.statusUnavailableContent, colors.statusCompletedContent)
        }
    }

    @Test
    fun fixedMaterialSchemesUseOnlyApprovedSemanticColors() {
        assertSchemeUsesOnlyApprovedColors(
            CelestialArchiveLightColorScheme,
            LightCelestialArchiveColors,
            DarkCelestialArchiveColors,
        )
        assertSchemeUsesOnlyApprovedColors(
            CelestialArchiveDarkColorScheme,
            DarkCelestialArchiveColors,
            LightCelestialArchiveColors,
        )
    }

    @Test
    fun themeSelectionIsStableAndHasNoDynamicPaletteInput() {
        assertSame(CelestialArchiveLightColorScheme, celestialArchiveColorScheme(darkTheme = false))
        assertSame(CelestialArchiveDarkColorScheme, celestialArchiveColorScheme(darkTheme = true))
        assertSame(
            celestialArchiveColorScheme(darkTheme = false),
            celestialArchiveColorScheme(darkTheme = false),
        )
        assertSame(
            celestialArchiveColorScheme(darkTheme = true),
            celestialArchiveColorScheme(darkTheme = true),
        )
        assertFalse(CelestialArchiveLightColorScheme == CelestialArchiveDarkColorScheme)
    }

    @Test
    fun approvedTextPairsMeetNormalTextContrast() {
        listOf(LightCelestialArchiveColors, DarkCelestialArchiveColors).forEach { colors ->
            val pairs = listOf(
                colors.contentPrimary to colors.archiveBackground,
                colors.contentPrimary to colors.archiveSurface,
                colors.contentPrimary to colors.archiveSurfaceVariant,
                colors.contentSecondary to colors.archiveBackground,
                colors.onActionPrimary to colors.actionPrimary,
                colors.onActionSecondary to colors.actionSecondary,
                colors.statusActiveContent to colors.statusActiveContainer,
                colors.statusCautionContent to colors.statusCautionContainer,
                colors.statusErrorContent to colors.statusErrorContainer,
                colors.statusInfoContent to colors.statusInfoContainer,
                colors.statusUnavailableContent to colors.statusUnavailableContainer,
            )
            pairs.forEach { (foreground, background) ->
                assertTrue(
                    "Expected >= 4.5:1, was ${contrastRatio(foreground, background)}",
                    contrastRatio(foreground, background) >= 4.5,
                )
            }
        }
    }

    @Test
    fun approvedOutlineAndFocusMeetNonTextContrast() {
        listOf(LightCelestialArchiveColors, DarkCelestialArchiveColors).forEach { colors ->
            listOf(colors.archiveBackground, colors.archiveSurface).forEach { background ->
                assertTrue(contrastRatio(colors.outline, background) >= 3.0)
                assertTrue(contrastRatio(colors.focus, background) >= 3.0)
            }
        }
    }

    private fun assertSemanticValues(colors: CelestialArchiveColors, vararg expected: Long) {
        assertEquals(expected.toList(), colors.roleValues().values.map { it.encodedArgb() })
    }

    private fun assertSchemeUsesOnlyApprovedColors(
        scheme: ColorScheme,
        colors: CelestialArchiveColors,
        inverseColors: CelestialArchiveColors,
    ) {
        val approved = colors.roleValues().values + inverseColors.roleValues().values
        assertTrue(scheme.roleValues().all { it in approved })
        assertEquals(colors.archiveBackground, scheme.background)
        assertEquals(colors.archiveSurface, scheme.surface)
        assertEquals(colors.contentPrimary, scheme.onBackground)
        assertEquals(colors.contentPrimary, scheme.onSurface)
        assertEquals(colors.contentSecondary, scheme.onSurfaceVariant)
        assertEquals(colors.actionPrimary, scheme.primary)
        assertEquals(colors.onActionPrimary, scheme.onPrimary)
        assertEquals(colors.actionSecondary, scheme.secondary)
        assertEquals(colors.onActionSecondary, scheme.onSecondary)
        assertEquals(colors.statusInfoContainer, scheme.secondaryContainer)
        assertEquals(colors.statusInfoContent, scheme.onSecondaryContainer)
        assertEquals(colors.statusErrorContainer, scheme.errorContainer)
        assertEquals(colors.statusErrorContent, scheme.onErrorContainer)
        assertEquals(colors.outline, scheme.outline)
    }

    private fun CelestialArchiveColors.roleValues(): LinkedHashMap<String, Color> = linkedMapOf(
        "archiveBackground" to archiveBackground,
        "archiveSurface" to archiveSurface,
        "archiveSurfaceVariant" to archiveSurfaceVariant,
        "contentPrimary" to contentPrimary,
        "contentSecondary" to contentSecondary,
        "actionPrimary" to actionPrimary,
        "onActionPrimary" to onActionPrimary,
        "actionSecondary" to actionSecondary,
        "onActionSecondary" to onActionSecondary,
        "statusActiveContainer" to statusActiveContainer,
        "statusActiveContent" to statusActiveContent,
        "statusCautionContainer" to statusCautionContainer,
        "statusCautionContent" to statusCautionContent,
        "statusErrorContainer" to statusErrorContainer,
        "statusErrorContent" to statusErrorContent,
        "statusInfoContainer" to statusInfoContainer,
        "statusInfoContent" to statusInfoContent,
        "statusUnavailableContainer" to statusUnavailableContainer,
        "statusUnavailableContent" to statusUnavailableContent,
        "outline" to outline,
        "focus" to focus,
    )

    private fun ColorScheme.roleValues(): List<Color> = listOf(
        primary, onPrimary, primaryContainer, onPrimaryContainer, inversePrimary,
        secondary, onSecondary, secondaryContainer, onSecondaryContainer,
        tertiary, onTertiary, tertiaryContainer, onTertiaryContainer,
        background, onBackground, surface, onSurface, surfaceVariant, onSurfaceVariant,
        surfaceTint, inverseSurface, inverseOnSurface, error, onError, errorContainer,
        onErrorContainer, outline, outlineVariant, scrim, surfaceBright, surfaceDim,
        surfaceContainer, surfaceContainerHigh, surfaceContainerHighest,
        surfaceContainerLow, surfaceContainerLowest,
    )

    private fun Color.encodedArgb(): Long =
        ((alpha * 255).roundToInt().toLong() shl 24) or
            ((red * 255).roundToInt().toLong() shl 16) or
            ((green * 255).roundToInt().toLong() shl 8) or
            (blue * 255).roundToInt().toLong()

    private fun contrastRatio(first: Color, second: Color): Double {
        val firstLuminance = relativeLuminance(first)
        val secondLuminance = relativeLuminance(second)
        return (max(firstLuminance, secondLuminance) + 0.05) /
            (min(firstLuminance, secondLuminance) + 0.05)
    }

    private fun relativeLuminance(color: Color): Double =
        0.2126 * linearize(color.red.toDouble()) +
            0.7152 * linearize(color.green.toDouble()) +
            0.0722 * linearize(color.blue.toDouble())

    private fun linearize(channel: Double): Double =
        if (channel <= 0.04045) channel / 12.92 else ((channel + 0.055) / 1.055).pow(2.4)
}
