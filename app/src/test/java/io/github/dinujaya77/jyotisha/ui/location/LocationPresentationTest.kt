package io.github.dinujaya77.jyotisha.ui.location

import io.github.dinujaya77.jyotisha.ui.preview.LocationPreviewState
import io.github.dinujaya77.jyotisha.ui.preview.locationStateFixtures
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationPresentationTest {
    @Test
    fun `V1-M3-03 runtime capabilities cannot acquire catalogue or persist`() {
        val capabilities = LocationRuntimeCapabilities()

        assertFalse(capabilities.canAcquireDeviceLocation)
        assertFalse(capabilities.hasApprovedTownCatalogue)
        assertFalse(capabilities.canPersistSelection)
    }

    @Test
    fun `V1-M3-03 preview matrix covers approved static location states only`() {
        assertEquals(LocationPreviewState.entries, locationStateFixtures.map { it.state })
        assertTrue(locationStateFixtures.all { it.synthetic })
        assertTrue(locationStateFixtures.all { it.label.startsWith("Synthetic") })
    }
}
