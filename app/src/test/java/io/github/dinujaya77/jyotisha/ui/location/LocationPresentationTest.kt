package io.github.dinujaya77.jyotisha.ui.location

import io.github.dinujaya77.jyotisha.data.location.LocationFallback
import io.github.dinujaya77.jyotisha.data.location.LocationSelectionState
import io.github.dinujaya77.jyotisha.data.location.TownCatalog
import io.github.dinujaya77.jyotisha.domain.location.LocationProvenance
import io.github.dinujaya77.jyotisha.domain.location.LocationSource
import io.github.dinujaya77.jyotisha.domain.location.SelectedLocation
import io.github.dinujaya77.jyotisha.domain.location.SelectedLocationMode
import io.github.dinujaya77.jyotisha.ui.preview.LocationPreviewState
import io.github.dinujaya77.jyotisha.ui.preview.locationStateFixtures
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationPresentationTest {
    @Test
    fun fallbackColomboIsNotPresentedAsAnExplicitManualTownSelection() {
        val town = TownCatalog.defaultTown
        val default = locationState(LocationFallback.DEFAULT, SelectedLocationMode.DEFAULT, LocationSource.DEFAULT)
        val manual = locationState(LocationFallback.MANUAL, SelectedLocationMode.MANUAL, LocationSource.MANUAL)

        assertFalse(isExplicitManualTownSelection(default, town.stableId))
        assertTrue(isExplicitManualTownSelection(manual, town.stableId))
    }
    @Test
    fun `V1-M3-03 preview matrix carries every approved static state and constraint`() {
        assertEquals(LocationPreviewState.entries, locationStateFixtures.map { it.state })
        assertTrue(locationStateFixtures.all { it.synthetic })
        assertTrue(locationStateFixtures.all { it.label.startsWith("Synthetic") })
        assertTrue(locationStateFixtures.all { fixture ->
            fixture.presentation.currentLocation.toString().contains("synthetic", ignoreCase = true)
        })
        assertTrue(locationStateFixtures.all { fixture ->
            fixture.presentation.selectedLocation.details.any {
                it.label == "Provenance" && it.value.contains("synthetic", ignoreCase = true)
            }
        })
        assertTrue(locationStateFixtures.all { fixture ->
            fixture.presentation.townAction.action == LocationAction.ChooseTown &&
                !fixture.presentation.townAction.enabled
        })
        assertTrue(locationStateFixtures.all { it.presentation.defaultExplanation.isNotBlank() })
        assertFalse(
            locationStateFixtures.single { it.state == LocationPreviewState.Default }
                .presentation.defaultAction.enabled,
        )
    }

    @Test
    fun `V1-M3-03 preview actions map to the approved state-specific recovery`() {
        val expectedActions = mapOf(
            LocationPreviewState.Precise to LocationAction.UseCurrentLocation,
            LocationPreviewState.Approximate to LocationAction.UseCurrentLocation,
            LocationPreviewState.Saved to LocationAction.UseCurrentLocation,
            LocationPreviewState.Manual to LocationAction.UseCurrentLocation,
            LocationPreviewState.Default to LocationAction.UseCurrentLocation,
            LocationPreviewState.PermissionDenied to LocationAction.RequestPermission,
            LocationPreviewState.ServicesDisabled to LocationAction.OpenSystemSettings,
            LocationPreviewState.Timeout to LocationAction.RetryCurrentLocation,
            LocationPreviewState.Invalid to LocationAction.UseCurrentLocation,
            LocationPreviewState.Stale to LocationAction.RetryCurrentLocation,
        )

        locationStateFixtures.forEach { fixture ->
            val currentAction = fixture.presentation.currentActions.single()
            assertEquals(expectedActions.getValue(fixture.state), currentAction.action)
            assertTrue(currentAction.enabled)
            assertTrue(currentAction.label.contains("synthetic", ignoreCase = true))
            assertNotEquals(fixture.presentation.townAction.action, currentAction.action)
        }
    }

    @Test
    fun `V1-M3-03 selected semantics are backed by explicit non-colour state text`() {
        val unselectedStates = setOf(
            LocationPreviewState.PermissionDenied,
            LocationPreviewState.ServicesDisabled,
            LocationPreviewState.Invalid,
        )

        locationStateFixtures.forEach { fixture ->
            assertEquals(fixture.state !in unselectedStates, fixture.presentation.selected)
            val selectionText = fixture.presentation.selectedLocation.details
                .single { it.label == "Selection" }
                .value
            assertEquals(fixture.presentation.selected, selectionText.startsWith("Selected"))
        }
    }

    private fun locationState(
        fallback: LocationFallback,
        mode: SelectedLocationMode,
        source: LocationSource,
    ): LocationSelectionState {
        val town = TownCatalog.defaultTown
        return LocationSelectionState(
            selectedLocation = SelectedLocation(
                coordinates = io.github.dinujaya77.jyotisha.domain.location.GeoCoordinates(
                    town.latitudeE6 / 1_000_000.0,
                    town.longitudeE6 / 1_000_000.0,
                ),
                provenance = LocationProvenance(source, "Asia/Colombo"),
            ),
            selectedTown = town,
            selectedMode = mode,
            fallback = fallback,
            isFirstUse = false,
        )
    }
}
