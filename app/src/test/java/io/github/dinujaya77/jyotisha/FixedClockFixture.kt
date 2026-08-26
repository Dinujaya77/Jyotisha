package io.github.dinujaya77.jyotisha

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

internal object FixedClockFixture {
    val knownInstant: Instant = Instant.parse("2026-03-20T00:45:00Z")
    val knownZone: ZoneId = ZoneId.of("Asia/Colombo")

    fun create(
        instant: Instant = knownInstant,
        zone: ZoneId = knownZone,
    ): Clock = Clock.fixed(instant, zone)
}
