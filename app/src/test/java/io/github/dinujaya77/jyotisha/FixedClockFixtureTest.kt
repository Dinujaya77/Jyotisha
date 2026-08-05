package io.github.dinujaya77.jyotisha

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

/** V1-M1-04 deterministic time foundation for NFR-009 through NFR-012. */
class FixedClockFixtureTest {
    @Test
    fun repeatedReadsReturnTheSameKnownInstant() {
        // Arrange
        val clock = FixedClockFixture.create()

        // Act
        val firstRead = clock.instant()
        val secondRead = clock.instant()

        // Assert
        assertEquals(FixedClockFixture.knownInstant, firstRead)
        assertEquals(firstRead, secondRead)
    }

    @Test
    fun explicitZoneControlsTheLocalRepresentation() {
        // Arrange
        val instant = Instant.parse("2026-03-20T00:45:00Z")
        val zone = ZoneId.of("Asia/Colombo")
        val clock = FixedClockFixture.create(instant, zone)

        // Act
        val localDateTime = ZonedDateTime.now(clock)

        // Assert
        assertEquals(zone, clock.zone)
        assertEquals("2026-03-20T06:15+05:30[Asia/Colombo]", localDateTime.toString())
    }
}
