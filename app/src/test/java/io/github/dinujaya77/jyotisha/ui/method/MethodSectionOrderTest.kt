package io.github.dinujaya77.jyotisha.ui.method

import org.junit.Assert.assertEquals
import org.junit.Test

class MethodSectionOrderTest {
    @Test
    fun `V1-M3-04 follows the approved unified UI-006 traversal order`() {
        assertEquals(
            listOf(
                MethodSection.SeasonalHora,
                MethodSection.SolarConvention,
                MethodSection.CurrentContext,
                MethodSection.Diagnostics,
                MethodSection.ValidationSources,
                MethodSection.RahuStatus,
                MethodSection.PrivacyAbout,
            ),
            approvedMethodSectionOrder,
        )
    }
}
