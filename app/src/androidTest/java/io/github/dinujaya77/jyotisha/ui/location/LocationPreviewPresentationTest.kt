package io.github.dinujaya77.jyotisha.ui.location

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performScrollTo
import io.github.dinujaya77.jyotisha.ui.preview.locationStateFixtures
import io.github.dinujaya77.jyotisha.ui.theme.JyotishaTheme
import io.github.dinujaya77.jyotisha.data.location.LocationFallback
import io.github.dinujaya77.jyotisha.data.location.LocationSelectionState
import io.github.dinujaya77.jyotisha.data.location.LocationSelectionWarning
import io.github.dinujaya77.jyotisha.data.location.TownCatalog
import io.github.dinujaya77.jyotisha.domain.location.GeoCoordinates
import io.github.dinujaya77.jyotisha.domain.location.LocationProvenance
import io.github.dinujaya77.jyotisha.domain.location.LocationSource
import io.github.dinujaya77.jyotisha.domain.location.SelectedLocation
import io.github.dinujaya77.jyotisha.domain.location.SelectedLocationMode
import io.github.dinujaya77.jyotisha.platform.location.DeviceLocationUnavailableReason
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LocationPreviewPresentationTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun v1M303_allSyntheticStatesRenderSemanticsAndDispatchMappedActions() {
        val activeFixture = mutableStateOf(locationStateFixtures.first())
        var observedAction: LocationAction? = null
        var backCount = 0
        composeRule.setContent {
            JyotishaTheme {
                LocationScreen(
                    presentation = activeFixture.value.presentation,
                    callbacks = LocationCallbacks(
                        onAction = { observedAction = it },
                        onBack = { backCount += 1 },
                    ),
                    innerPadding = PaddingValues(),
                )
            }
        }

        locationStateFixtures.forEach { fixture ->
            composeRule.runOnIdle {
                observedAction = null
                activeFixture.value = fixture
            }
            composeRule.waitForIdle()

            val currentStateText = when (val current = fixture.presentation.currentLocation) {
                is CurrentLocationPresentation.Available -> current.status.details.first().value
                is CurrentLocationPresentation.Unavailable -> current.status.heading
            }
            composeRule.onNodeWithText(currentStateText).performScrollTo().assertIsDisplayed()
            composeRule.onNodeWithTag("location_selected")
                .performScrollTo()
                .assert(
                    SemanticsMatcher.expectValue(
                        SemanticsProperties.Selected,
                        fixture.presentation.selected,
                    ),
                )

            val currentAction = fixture.presentation.currentActions.single()
            composeRule.onNodeWithTag(
                locationActionTestTag(currentAction.action, currentAction.enabled),
            )
                .performScrollTo()
                .assertIsEnabled()
                .performClick()
            composeRule.runOnIdle {
                assertEquals(currentAction.action, observedAction)
            }

            composeRule.onNodeWithTag("action_choose_town_disabled")
                .performScrollTo()
                .assertIsNotEnabled()
            val defaultAction = fixture.presentation.defaultAction
            val defaultNode = composeRule.onNodeWithTag(
                locationActionTestTag(defaultAction.action, defaultAction.enabled),
            ).performScrollTo()
            if (defaultAction.enabled) {
                defaultNode
                    .assertIsEnabled()
                    .performClick()
                composeRule.runOnIdle {
                    assertEquals(LocationAction.UseDefault, observedAction)
                }
            } else {
                defaultNode.assertIsNotEnabled()
            }
        }

        composeRule.onNodeWithTag("action_back")
            .performScrollTo()
            .performClick()
        composeRule.runOnIdle {
            assertEquals(1, backCount)
        }
    }

    @Test
    fun v1M405_runtimeLocationActionsExposeTheReviewedTownsDefaultAndPermissionPath() {
        val runtime = mutableStateOf(LocationRuntimeState())
        var selectedTown: String? = null
        var defaultCount = 0
        var useCurrentCount = 0
        var permissionCount = 0
        composeRule.setContent {
            JyotishaTheme {
                RuntimeLocationScreen(
                    state = runtime.value,
                    onUseCurrent = { useCurrentCount += 1 },
                    onRequestPermission = { permissionCount += 1 },
                    onTownSelected = { selectedTown = it },
                    onUseDefault = { defaultCount += 1 },
                    onOpenMethod = {},
                    onOpenAbout = {},
                    onOpenSystemSettings = {},
                    onBack = {},
                    innerPadding = PaddingValues(),
                )
            }
        }

        composeRule.onNodeWithTag("town_1241622").performScrollTo().performClick()
        composeRule.onNodeWithTag("action_use_default").performScrollTo().performClick()
        composeRule.onNodeWithTag("action_use_current").performScrollTo().performClick()
        composeRule.runOnIdle {
            assertEquals("geonames:1241622", selectedTown)
            assertEquals(1, defaultCount)
            assertEquals(1, useCurrentCount)
        }

        composeRule.onNodeWithTag("location_town_search").performScrollTo().performTextInput("zz")
        composeRule.onNodeWithTag("location_town_no_matches").performScrollTo().assertIsDisplayed()

        composeRule.runOnIdle { runtime.value = LocationRuntimeState(acquisition = LocationAcquisition.REQUEST_PERMISSION) }
        composeRule.onNodeWithTag("action_request_location_permission").performScrollTo().performClick()
        composeRule.runOnIdle { assertEquals(1, permissionCount) }
    }

    @Test
    fun v1M405_firstUseAndRecoveryActionsAreLiveAndExplicit() {
        val runtime = mutableStateOf(LocationRuntimeState(selection = defaultSelection(isFirstUse = true)))
        var chooseTownCount = 0
        var defaultCount = 0
        var methodCount = 0
        var aboutCount = 0
        var backCount = 0
        var settingsReason: DeviceLocationUnavailableReason? = null
        composeRule.setContent {
            JyotishaTheme {
                RuntimeLocationScreen(
                    state = runtime.value,
                    onUseCurrent = {},
                    onRequestPermission = {},
                    onTownSelected = {},
                    onUseDefault = { defaultCount += 1 },
                    onOpenMethod = { methodCount += 1 },
                    onOpenAbout = { aboutCount += 1 },
                    onOpenSystemSettings = { settingsReason = it },
                    onBack = { backCount += 1 },
                    innerPadding = PaddingValues(),
                )
            }
        }
        composeRule.onNodeWithTag("screen_first_use").assertIsDisplayed()
        composeRule.onNodeWithTag("action_first_use_default").performClick()
        composeRule.onNodeWithTag("action_first_use_method").performClick()
        composeRule.onNodeWithTag("action_first_use_about").performClick()
        composeRule.onNodeWithTag("action_back").performClick()
        composeRule.onNodeWithTag("action_first_use_choose_town").performClick()
        composeRule.runOnIdle { chooseTownCount += 1 }
        composeRule.onNodeWithTag("town_1248991").assertIsDisplayed()

        composeRule.runOnIdle {
            runtime.value = LocationRuntimeState(
                selection = defaultSelection(
                    warning = LocationSelectionWarning.CURRENT_LOCATION_UNAVAILABLE(
                        DeviceLocationUnavailableReason.SERVICES_DISABLED,
                    ),
                ),
            )
        }
        composeRule.onNodeWithTag("action_open_location_settings").performScrollTo().performClick()
        composeRule.runOnIdle {
            assertEquals(DeviceLocationUnavailableReason.SERVICES_DISABLED, settingsReason)
            assertEquals(1, defaultCount)
            assertEquals(1, methodCount)
            assertEquals(1, aboutCount)
            assertEquals(1, backCount)
            assertEquals(1, chooseTownCount)
        }
    }

    private fun defaultSelection(
        isFirstUse: Boolean = false,
        warning: LocationSelectionWarning? = null,
    ): LocationSelectionState {
        val town = TownCatalog.defaultTown
        return LocationSelectionState(
            selectedLocation = SelectedLocation(
                coordinates = GeoCoordinates(town.latitudeE6 / 1_000_000.0, town.longitudeE6 / 1_000_000.0),
                provenance = LocationProvenance(
                    source = LocationSource.DEFAULT,
                    zoneId = "Asia/Colombo",
                    datasetVersion = TownCatalog.provenance.datasetVersion,
                ),
            ),
            selectedTown = town,
            selectedMode = SelectedLocationMode.DEFAULT,
            fallback = LocationFallback.DEFAULT,
            isFirstUse = isFirstUse,
            warning = warning,
        )
    }
}
