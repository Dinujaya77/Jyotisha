package io.github.dinujaya77.jyotisha.ui.preview

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DashboardPreviewFixtureTest {
    @Test
    fun `V1-M3-01 synthetic success is explicitly preview-only`() {
        val fixture = syntheticDashboardSuccessFixture()

        assertTrue(fixture.location.details.any { it.value.contains("Synthetic") })
        assertTrue(fixture.seasonalHora.heading.contains("synthetic", ignoreCase = true))
        assertEquals("Calculation method not approved", fixture.rahuStatus.message)
    }
}
