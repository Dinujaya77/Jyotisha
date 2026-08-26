package io.github.dinujaya77.jyotisha.ui.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.tooling.preview.Preview
import io.github.dinujaya77.jyotisha.ui.firstuse.FirstUseStaticScreen
import io.github.dinujaya77.jyotisha.ui.location.LocationScreen
import io.github.dinujaya77.jyotisha.ui.theme.JyotishaTheme

internal enum class LocationPreviewState {
    Precise,
    Approximate,
    Saved,
    Manual,
    Default,
    PermissionDenied,
    ServicesDisabled,
    Timeout,
    Invalid,
    Stale,
}

@Immutable
internal data class LocationStateFixture(
    val state: LocationPreviewState,
    val label: String,
    val synthetic: Boolean = true,
)

internal val locationStateFixtures = LocationPreviewState.entries.map { state ->
    LocationStateFixture(state, "Synthetic ${state.name} presentation fixture")
}

@Preview(name = "M3 Location runtime unavailable", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun LocationUnavailablePreview() {
    JyotishaTheme {
        LocationScreen(onBack = {}, innerPadding = PaddingValues())
    }
}

@Preview(name = "M3 First-use isolated fixture", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun FirstUseFixturePreview() {
    JyotishaTheme {
        FirstUseStaticScreen(openMethod = {}, openAbout = {}, innerPadding = PaddingValues())
    }
}
