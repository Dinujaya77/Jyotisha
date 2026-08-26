package io.github.dinujaya77.jyotisha.ui.preview

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TimelinePreviewFixtureTest {
    @Test
    fun `V1-M3-02 synthetic ledger has exactly 12 Day then 12 Night rows`() {
        val rows = syntheticTimelineFixture().rows

        assertEquals((1..24).toList(), rows.map { it.position })
        assertEquals(List(12) { "Day" }, rows.take(12).map { it.groupLabel })
        assertEquals(List(12) { "Night" }, rows.drop(12).map { it.groupLabel })
        assertEquals(1, rows.count { it.currentLabel == "Current" })
        assertTrue(rows.all { it.spokenSummary.contains(it.primaryValue) })
    }
}
