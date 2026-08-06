package io.github.dinujaya77.jyotisha.ui.preview

import androidx.compose.runtime.Immutable
import io.github.dinujaya77.jyotisha.ui.theme.BackgroundWash
import io.github.dinujaya77.jyotisha.ui.theme.CelestialArchiveSpacing
import io.github.dinujaya77.jyotisha.ui.theme.VisualConstraintContext
import io.github.dinujaya77.jyotisha.ui.theme.backgroundWash
import androidx.compose.ui.unit.Dp

enum class PreviewTheme { Light, Dark }

enum class PreviewViewport(
    val widthDp: Int,
    val heightDp: Int,
) {
    CompactPortrait(widthDp = 320, heightDp = 720),
    CompactLandscape(widthDp = 640, heightDp = 320),
    Medium(widthDp = 720, heightDp = 900),
    Expanded(widthDp = 1000, heightDp = 900),
}

enum class ComponentPreviewScenario {
    ComponentCombination,
    LongEnglish,
    StateMatrix,
    ReducedMotionEndState,
}

/** Machine-verifiable preview evidence dimensions for V1-M2-04. */
@Immutable
data class ComponentPreviewFixture(
    val id: String,
    val theme: PreviewTheme,
    val viewport: PreviewViewport,
    val fontScale: Float = 1f,
    val scenario: ComponentPreviewScenario,
    val longEnglish: Boolean = false,
    val selectedTimeline: Boolean = false,
    val unselectedTimeline: Boolean = false,
    val enabledAction: Boolean = false,
    val disabledAction: Boolean = false,
    val loadingState: Boolean = false,
    val errorState: Boolean = false,
    val unavailableState: Boolean = false,
    val reducedMotion: Boolean = false,
    val immediateEndState: Boolean = false,
    val washSuppressed: Boolean = false,
) {
    init {
        require(id.isNotBlank())
        require(fontScale >= 1f)
        require(!longEnglish || fontScale == 2f)
        require(!reducedMotion || immediateEndState)
        require(!reducedMotion || washSuppressed)
    }

    val darkTheme: Boolean get() = theme == PreviewTheme.Dark
}

internal val ComponentPreviewFixtures = listOf(
    ComponentPreviewFixture(
        id = "compact-light-components",
        theme = PreviewTheme.Light,
        viewport = PreviewViewport.CompactPortrait,
        scenario = ComponentPreviewScenario.ComponentCombination,
        selectedTimeline = true,
        unselectedTimeline = true,
        enabledAction = true,
    ),
    ComponentPreviewFixture(
        id = "compact-dark-components",
        theme = PreviewTheme.Dark,
        viewport = PreviewViewport.CompactPortrait,
        scenario = ComponentPreviewScenario.ComponentCombination,
        selectedTimeline = true,
        unselectedTimeline = true,
        enabledAction = true,
    ),
    ComponentPreviewFixture(
        id = "compact-landscape-components",
        theme = PreviewTheme.Light,
        viewport = PreviewViewport.CompactLandscape,
        scenario = ComponentPreviewScenario.ComponentCombination,
        selectedTimeline = true,
        unselectedTimeline = true,
        enabledAction = true,
    ),
    ComponentPreviewFixture(
        id = "medium-components",
        theme = PreviewTheme.Dark,
        viewport = PreviewViewport.Medium,
        scenario = ComponentPreviewScenario.ComponentCombination,
        selectedTimeline = true,
        unselectedTimeline = true,
        enabledAction = true,
    ),
    ComponentPreviewFixture(
        id = "expanded-components",
        theme = PreviewTheme.Light,
        viewport = PreviewViewport.Expanded,
        scenario = ComponentPreviewScenario.ComponentCombination,
        selectedTimeline = true,
        unselectedTimeline = true,
        enabledAction = true,
    ),
    ComponentPreviewFixture(
        id = "compact-long-english-200-percent",
        theme = PreviewTheme.Dark,
        viewport = PreviewViewport.CompactPortrait,
        fontScale = 2f,
        scenario = ComponentPreviewScenario.LongEnglish,
        longEnglish = true,
        selectedTimeline = true,
        unselectedTimeline = true,
        enabledAction = true,
    ),
    ComponentPreviewFixture(
        id = "compact-state-matrix",
        theme = PreviewTheme.Light,
        viewport = PreviewViewport.CompactPortrait,
        scenario = ComponentPreviewScenario.StateMatrix,
        selectedTimeline = true,
        unselectedTimeline = true,
        enabledAction = true,
        disabledAction = true,
        loadingState = true,
        errorState = true,
        unavailableState = true,
    ),
    ComponentPreviewFixture(
        id = "compact-reduced-motion-end-state",
        theme = PreviewTheme.Dark,
        viewport = PreviewViewport.CompactPortrait,
        scenario = ComponentPreviewScenario.ReducedMotionEndState,
        selectedTimeline = true,
        unselectedTimeline = true,
        enabledAction = true,
        reducedMotion = true,
        immediateEndState = true,
        washSuppressed = true,
    ),
)

internal fun componentPreviewFixture(id: String): ComponentPreviewFixture =
    requireNotNull(ComponentPreviewFixtures.singleOrNull { it.id == id })

internal fun previewBackgroundWash(fixture: ComponentPreviewFixture): BackgroundWash =
    backgroundWash(
        darkTheme = fixture.darkTheme,
        context = VisualConstraintContext(
            reducedMotion = fixture.reducedMotion,
            screenshotFixture = fixture.washSuppressed,
        ),
    )

internal fun previewGutter(viewport: PreviewViewport): Dp = when (viewport) {
    PreviewViewport.CompactPortrait,
    PreviewViewport.CompactLandscape,
    -> CelestialArchiveSpacing.gutterCompact
    PreviewViewport.Medium -> CelestialArchiveSpacing.gutterMedium
    PreviewViewport.Expanded -> CelestialArchiveSpacing.gutterExpanded
}
