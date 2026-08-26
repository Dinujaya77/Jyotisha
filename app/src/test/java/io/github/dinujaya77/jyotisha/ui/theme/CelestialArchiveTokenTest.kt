package io.github.dinujaya77.jyotisha.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

/** V1-M2-02: FR-011; NFR-005, NFR-011; T-FR-011; T-NFR-A11Y. */
class CelestialArchiveTokenTest {

    @Test
    fun approvedTypeRolesUseExactSystemSansMetrics() {
        assertType(CelestialArchiveType.display, 40, 48, 600)
        assertType(CelestialArchiveType.headline, 28, 36, 600)
        assertType(CelestialArchiveType.title, 22, 28, 600)
        assertType(CelestialArchiveType.body, 16, 24, 400)
        assertType(CelestialArchiveType.bodySmall, 14, 20, 400)
        assertType(CelestialArchiveType.label, 14, 20, 600)
        assertType(CelestialArchiveType.timePrimary, 32, 40, 600)
        assertTrue(
            listOf(CelestialArchiveType.bodySmall, CelestialArchiveType.label)
                .all { it.fontSize >= 14.sp },
        )
    }

    @Test
    fun materialTypographyMapsApprovedSemanticRoles() {
        assertEquals(CelestialArchiveType.display, Typography.displayLarge)
        assertEquals(CelestialArchiveType.headline, Typography.headlineLarge)
        assertEquals(CelestialArchiveType.title, Typography.titleLarge)
        assertEquals(CelestialArchiveType.body, Typography.bodyLarge)
        assertEquals(CelestialArchiveType.bodySmall, Typography.bodySmall)
        assertEquals(CelestialArchiveType.label, Typography.labelLarge)
        assertTrue(
            listOf(Typography.labelLarge, Typography.labelMedium, Typography.labelSmall)
                .all { it.fontSize >= 14.sp },
        )
    }

    @Test
    fun spacingGuttersShapesAndElevationAreExact() {
        assertDpValues(
            listOf(
                CelestialArchiveSpacing.spaceXs, CelestialArchiveSpacing.spaceSm,
                CelestialArchiveSpacing.spaceMd, CelestialArchiveSpacing.spaceLg,
                CelestialArchiveSpacing.spaceXl, CelestialArchiveSpacing.space2xl,
                CelestialArchiveSpacing.space3xl,
            ),
            4, 8, 12, 16, 24, 32, 48,
        )
        assertDpValues(
            listOf(
                CelestialArchiveSpacing.gutterCompact, CelestialArchiveSpacing.gutterMedium,
                CelestialArchiveSpacing.gutterExpanded,
            ),
            16, 24, 32,
        )
        assertDpValues(
            listOf(
                CelestialArchiveShapes.shapeSmall, CelestialArchiveShapes.shapeMedium,
                CelestialArchiveShapes.shapeLarge, CelestialArchiveShapes.shapeExtraLarge,
            ),
            8, 12, 16, 24,
        )
        assertDpValues(
            listOf(
                CelestialArchiveElevation.elevationBase,
                CelestialArchiveElevation.elevationGrouped,
                CelestialArchiveElevation.elevationTransient,
            ),
            0, 1, 3,
        )
    }

    @Test
    fun materialShapesMapOnlyExactApprovedRoundedCorners() {
        assertEquals(RoundedCornerShape(8.dp), CelestialArchiveShapes.small)
        assertEquals(RoundedCornerShape(12.dp), CelestialArchiveShapes.medium)
        assertEquals(RoundedCornerShape(16.dp), CelestialArchiveShapes.large)
        assertEquals(RoundedCornerShape(24.dp), CelestialArchiveShapes.extraLarge)

        assertSame(CelestialArchiveShapes.small, CelestialArchiveMaterialShapes.extraSmall)
        assertSame(CelestialArchiveShapes.small, CelestialArchiveMaterialShapes.small)
        assertSame(CelestialArchiveShapes.medium, CelestialArchiveMaterialShapes.medium)
        assertSame(CelestialArchiveShapes.large, CelestialArchiveMaterialShapes.large)
        assertSame(CelestialArchiveShapes.extraLarge, CelestialArchiveMaterialShapes.extraLarge)
    }

    @Test
    fun interactionAndIconContractsPreserveApprovedInvariants() {
        assertEquals(48.dp, CelestialArchiveInteraction.touchTargetMinimum)
        assertEquals(2.dp, CelestialArchiveInteraction.focusOutlineWidth)
        assertEquals(24.dp, CelestialArchiveInteraction.iconGlyphSize)
        assertEquals(
            CelestialArchiveInteraction.touchTargetMinimum / 2,
            CelestialArchiveInteraction.iconGlyphSize,
        )
        assertTrue(CelestialArchiveInteraction.outlinedIconStylePreferred)
        assertTrue(CelestialArchiveInteraction.actionableIconRequiresLabelOrAccessibleName)
        assertTrue(CelestialArchiveInteraction.stateIconRequiresExplicitText)
        assertTrue(CelestialArchiveInteraction.focusRequiresShapeCue)
    }

    @Test
    fun backgroundWashUsesExactModePairAndEveryConstraintSuppressesIt() {
        assertEquals(
            BackgroundWash.Static(Color(0xFFF7F1E5), Color(0xFFFFF9EF)),
            backgroundWash(darkTheme = false),
        )
        assertEquals(
            BackgroundWash.Static(Color(0xFF101827), Color(0xFF182235)),
            backgroundWash(darkTheme = true),
        )
        listOf(
            VisualConstraintContext(constrained = true),
            VisualConstraintContext(highContrast = true),
            VisualConstraintContext(reducedMotion = true),
            VisualConstraintContext(screenshotFixture = true),
        ).forEach { context ->
            assertSame(BackgroundWash.None, backgroundWash(darkTheme = false, context = context))
            assertSame(BackgroundWash.None, backgroundWash(darkTheme = true, context = context))
        }
    }

    @Test
    fun motionValuesAndReducedMotionMappingAreExact() {
        assertEquals(
            listOf(0, 120, 180, 240),
            listOf(
                CelestialArchiveMotion.motionInstant,
                CelestialArchiveMotion.motionQuick,
                CelestialArchiveMotion.motionStandard,
                CelestialArchiveMotion.motionEmphasized,
            ),
        )
        listOf(0, 120, 180, 240).forEach { duration ->
            assertEquals(0, CelestialArchiveMotion.duration(duration, reducedMotion = true))
            assertEquals(duration, CelestialArchiveMotion.duration(duration, reducedMotion = false))
        }
        assertEasingEquals(
            CubicBezierEasing(0.2f, 0f, 0f, 1f),
            CelestialArchiveMotion.easingStandard,
        )
        assertEasingEquals(
            CubicBezierEasing(0.4f, 0f, 1f, 1f),
            CelestialArchiveMotion.easingExit,
        )
    }

    @Test
    fun illustrationContractIsAbstractDecorativeAndContentFirst() {
        assertEquals(CelestialIllustrationMotif.entries.toSet(), CelestialArchiveIllustration.allowedMotifs)
        assertFalse(CelestialArchiveIllustration.isAccessibilityElement)
        assertTrue(CelestialArchiveIllustration.isVisible(VisualConstraintContext()))
        assertFalse(
            CelestialArchiveIllustration.isVisible(VisualConstraintContext(constrained = true)),
        )
        assertFalse(
            CelestialArchiveIllustration.isVisible(VisualConstraintContext(highContrast = true)),
        )
        assertFalse(
            CelestialArchiveIllustration.isVisible(VisualConstraintContext(largeText = true)),
        )
    }

    private fun assertType(style: TextStyle, size: Int, lineHeight: Int, weight: Int) {
        assertEquals(FontFamily.SansSerif, style.fontFamily)
        assertEquals(FontWeight(weight), style.fontWeight)
        assertEquals(size.sp, style.fontSize)
        assertEquals(lineHeight.sp, style.lineHeight)
    }

    private fun assertDpValues(actual: List<Dp>, vararg expected: Int) {
        assertEquals(expected.map { it.dp }, actual)
    }

    private fun assertEasingEquals(expected: CubicBezierEasing, actual: CubicBezierEasing) {
        listOf(0f, 0.1f, 0.25f, 0.5f, 0.75f, 0.9f, 1f).forEach { fraction ->
            assertEquals(expected.transform(fraction), actual.transform(fraction), 0f)
        }
    }
}
