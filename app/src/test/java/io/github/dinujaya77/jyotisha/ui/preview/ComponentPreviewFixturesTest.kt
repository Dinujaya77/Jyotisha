package io.github.dinujaya77.jyotisha.ui.preview

import io.github.dinujaya77.jyotisha.ui.theme.BackgroundWash
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import androidx.compose.ui.unit.dp

/** V1-M2-04: NFR-005, NFR-011; A11Y-AUTO-006/008; T-NFR-A11Y. */
class ComponentPreviewFixturesTest {

    @Test
    fun fixtureIdsAreUniqueAndEveryApprovedViewportIsCovered() {
        assertEquals(ComponentPreviewFixtures.size, ComponentPreviewFixtures.map { it.id }.toSet().size)
        assertEquals(
            PreviewViewport.entries.toSet(),
            ComponentPreviewFixtures.map { it.viewport }.toSet(),
        )
        assertEquals(320, PreviewViewport.CompactPortrait.widthDp)
        assertEquals(640, PreviewViewport.CompactLandscape.widthDp)
        assertEquals(720, PreviewViewport.Medium.widthDp)
        assertEquals(1000, PreviewViewport.Expanded.widthDp)
    }

    @Test
    fun viewportGuttersMapToExactApprovedAdaptiveTokens() {
        assertEquals(16.dp, previewGutter(PreviewViewport.CompactPortrait))
        assertEquals(16.dp, previewGutter(PreviewViewport.CompactLandscape))
        assertEquals(24.dp, previewGutter(PreviewViewport.Medium))
        assertEquals(32.dp, previewGutter(PreviewViewport.Expanded))
    }

    @Test
    fun bothThemesAndComponentCombinationAreRepresented() {
        assertEquals(
            setOf(PreviewTheme.Light, PreviewTheme.Dark),
            ComponentPreviewFixtures.map { it.theme }.toSet(),
        )
        assertTrue(
            ComponentPreviewFixtures
                .filter { it.scenario == ComponentPreviewScenario.ComponentCombination }
                .all { it.selectedTimeline && it.unselectedTimeline && it.enabledAction },
        )
    }

    @Test
    fun longEnglishFixtureUsesExactTwoHundredPercentFontScale() {
        val fixture = ComponentPreviewFixtures.single { it.longEnglish }
        assertEquals(2f, fixture.fontScale)
        assertEquals(PreviewViewport.CompactPortrait, fixture.viewport)
        assertEquals(ComponentPreviewScenario.LongEnglish, fixture.scenario)
    }

    @Test
    fun stateMatrixCoversSelectedActionsLoadingErrorAndUnavailable() {
        val fixture = ComponentPreviewFixtures.single {
            it.scenario == ComponentPreviewScenario.StateMatrix
        }
        assertTrue(fixture.selectedTimeline)
        assertTrue(fixture.unselectedTimeline)
        assertTrue(fixture.enabledAction)
        assertTrue(fixture.disabledAction)
        assertTrue(fixture.loadingState)
        assertTrue(fixture.errorState)
        assertTrue(fixture.unavailableState)
    }

    @Test
    fun reducedMotionFixtureIsImmediateAndSuppressesDecorativeWash() {
        val fixture = ComponentPreviewFixtures.single { it.reducedMotion }
        assertTrue(fixture.immediateEndState)
        assertTrue(fixture.washSuppressed)
        assertEquals(ComponentPreviewScenario.ReducedMotionEndState, fixture.scenario)
        assertSame(BackgroundWash.None, previewBackgroundWash(fixture))
    }

    @Test
    fun everyFixtureUsesBoundedDeterministicDisplayConfiguration() {
        ComponentPreviewFixtures.forEach { fixture ->
            assertTrue(fixture.viewport.widthDp in 320..1000)
            assertTrue(fixture.viewport.heightDp in 320..900)
            assertTrue(fixture.fontScale == 1f || fixture.fontScale == 2f)
            assertSame(fixture, componentPreviewFixture(fixture.id))
        }
    }
}
