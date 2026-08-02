# Calculation Rules

| Field | Value |
|---|---|
| Status | Partially Complete |
| Version | 1.2 |
| Last updated | 2026-08-02 |
| Owner role | Business Analyst |
| Approval state | CR-001–CR-010 approved under PA-001; SOL-R-001–SOL-R-014 approved under PA-004; KH/PK/RK retain separate gates |

All rules use CP-001. Inputs and intermediate instants must remain deterministic and versioned.

## CR-001 — Solar anchors

Calculate sunrise, sunset, and following sunrise with the PA-004-approved offline algorithm using latitude, longitude, civil date, active zone, apparent upper limb, standard refraction, solar-centre zenith 90.8333°, sea-level, and level/unobstructed horizon. `SOLAR-001-v1.0` / SOL-R-001–SOL-R-014 is the complete approved implementation profile. Implementation still requires an approved delivery milestone and task; never combine or fabricate anchors from different providers/algorithms.

## CR-002 — Applicable Hora day

Find the most recent calculated sunrise at or before `now`. Its local civil weekday supplies the first ruler. An instant after civil midnight but before that day's sunrise remains within the Hora day that began on the preceding civil date.

## CR-003 — Ruler sequence

Use the cyclic order Saturn → Jupiter → Mars → Sun → Venus → Mercury → Moon. The first interval uses the weekday ruler: Sunday Sun, Monday Moon, Tuesday Mars, Wednesday Mercury, Thursday Jupiter, Friday Venus, Saturday Saturn. Advance one cycle position per interval; exclude Rahu/Ketu.

## CR-004 — Seasonal interval division

Represent anchor instants on the UTC timeline with integer nanoseconds. Let `D` be the non-negative integer nanoseconds from sunrise to sunset. Day boundary `i` is `sunrise + floor(D × i / 12)` nanoseconds for `i = 0..11`, and boundary 12 is exactly sunset. Let `N` be the integer nanoseconds from sunset to following sunrise; define night boundaries identically from the sunset anchor, with boundary 12 exactly following sunrise. Compute each boundary independently from its common anchor—never by repeated addition. Use overflow-safe quotient/remainder arithmetic. Quantization error from the exact rational boundary is less than one nanosecond, and interval lengths may differ by at most one nanosecond.

## CR-005 — Continuity and count

Return exactly 24 ordered intervals. Day boundary 12 equals sunset and night boundary 0. Night boundary 12 equals following sunrise. Every interval end is exactly the next interval start; no gap or overlap is permitted.

## CR-006 — Boundary membership

An interval contains `start <= instant < end`. Exact end belongs to the next interval. At following sunrise, the prior final interval ends and the next Hora day begins with its new weekday ruler.

## CR-007 — Current, next, and countdown

Current is the unique interval containing `now`. Next is the following interval, or the next Hora day's first interval after index 23. Remaining duration is `current.end - now`, never negative. Display rounding must not affect selection or transition.

## CR-008 — Civil time and zones

Convert instants using an explicit IANA zone. Use the device's current system zone for current-device calculations and `Asia/Colombo` for manual/default Sri Lankan calculations. Never derive a zone from longitude. Recompute when the relevant system date/time/zone context changes.

## CR-009 — Location and calculation provenance

Every result retains the exact coordinates used, optional name, metre accuracy if supplied, permission precision, source, source-specific timestamp/provenance, active zone, result calculation time, CP-001 version, and engine version. Device fixes use acquisition time; manual locations use selection time; bundled defaults use dataset/version provenance and a nullable acquisition timestamp. Dashboard display may reduce coordinate precision but must not misstate provenance.

## CR-010 — Atomic recalculation

On a material location, clock, date, zone, profile, or engine change, calculate one complete immutable replacement result off the main thread. Publish it atomically only after validation. Timeout, cancellation, null, or calculation failure retains the prior coherent result with a warning when available.

## Location-policy parameters linked to CR-009/CR-010

- Current-location timeout: 20 seconds.
- A returned fix is fresh when age is no more than 2 minutes using monotonic elapsed time where available.
- A saved device fix becomes stale at exactly 24 hours; stale remains usable and labeled.
- Material movement: at least 10 km centre-to-centre.
- Meaningful accuracy improvement: at least 50 m and at least 25% better than saved accuracy.
- Invalid, negative, or future timestamps are stale.
- Maximum acceptable reported uncertainty and stored-precise-fix handling after permission downgrade are approved PA-002 architecture decisions; implementation evidence remains required.

## Integrity rule

Expected results must follow approved rules and evidence. Do not change an expectation merely to make an implementation or test pass.

## Proposed SOLAR-001 engine rules

The normative wording is in `SOLAR_001.md`; this index does not redefine it.

| Rule | Subject | State |
|---|---|---|
| SOL-R-001 | Apparent-upper-limb `90.8333°` event meaning | Approved under PA-004 |
| SOL-R-002 | Fixed sea-level elevation and average-refraction assumptions | Approved under PA-004 |
| SOL-R-003 | Typed inputs, ranges, rejection, and canonical coordinates | Approved under PA-004 |
| SOL-R-004 | IANA civil-day association and UTC time-scale model | Approved under PA-004 |
| SOL-R-005 | Following sunrise from the following civil date | Approved under PA-004 |
| SOL-R-006 | Julian day, binary64, `StrictMath`, and normalization | Approved under PA-004 |
| SOL-R-007 | Frozen NOAA/Meeus-derived solar terms/constants/order | Approved under PA-004 |
| SOL-R-008 | Five-evaluation event iteration and inverse-domain behavior | Approved under PA-004 |
| SOL-R-009 | Half-even millisecond quantization and ordering | Approved under PA-004 |
| SOL-R-010 | Success output/provenance/diagnostics | Approved under PA-004 |
| SOL-R-011 | Typed unavailable outcomes | Approved under PA-004 |
| SOL-R-012 | High-latitude/no-event behavior | Approved under PA-004 |
| SOL-R-013 | Shared context, cache, thread, cancellation, and replacement contract | Approved under PA-004 |
| SOL-R-014 | Model expectation, validation tolerance, internal and display precision separation | Approved under PA-004 |

## Proposed CP-002 main-Kala rules

### KH-001 — Fixed system identity and anchor

Model Sri Lankan fixed Kala Hora separately from CP-001 Seasonal Planetary Hora. Practitioner evidence provisionally anchors the first fixed period at actual local sunrise; a 06:07 sunrise would provisionally produce 06:07–07:07. State: **Provisional; non-executable**.

### KH-002 — Main-ruler cycle

Proposed cycle: Ravi/Sun → Sikuru or Shukra/Venus → Budha/Mercury → Sandu or Chandra/Moon → Senesuru or Shani/Saturn → Brahaspathi or Guru/Jupiter → Angaharu or Kuja/Mars, cyclically. The sunrise weekday ruler is proposed as the first main ruler. State: **Provisionally corroborated; terminology review pending**.

### KH-003 — Fixed duration

USR-DOM-001 and practitioner evidence propose each main Kala as 60 civil minutes. “Civil minutes” versus 3,600 elapsed seconds on offset-transition dates is unresolved. State: **User supplied and provisionally corroborated; non-executable**.

### KH-004 — Day/night behavior

Practitioner material describes 12 fixed day periods plus 12 fixed night periods, but does not adequately define sunset behavior or the relationship to the next actual sunrise. State: **Unresolved**.

### KH-005 — Following-sunrise coverage

No rule is selected. If following sunrise is later than `start + 24h`, 24 fixed periods leave a gap; if earlier, the final period overlaps the new sunrise. Adding/truncating/splitting a period, distributing a correction, continuing without reset, or resetting at sunrise each changes another stated property. A calculator must return `RuleUnresolved` until an authority approves count, coverage, reset precedence, partial-period policy, and duration semantics. State: **Blocked**.

## Proposed CP-002 Panchama rules

### PK-001 — Parent and subdivision duration

USR-DOM-001 proposes exactly five consecutive 12-minute subdivisions covering one fixed 60-minute parent Kala. A Panchama calculator must consume an approved CP-002 parent schedule and verify its profile ID; it must not divide a CP-001 seasonal Hora merely because both are called Hora. State: **User supplied and provisionally corroborated; non-executable**.

### PK-002 — Provisional seven-by-five matrix

Legend: `U` is user supplied and practitioner-corroborated; `P` is provisionally supported by practitioner evidence; no row is verified by SRC-009.

| Main ruler | 1 | 2 | 3 | 4 | 5 | Evidence |
|---|---|---|---|---|---|---|
| Ravi | Ravi | Chandra | Kuja | Budha | Guru | U |
| Sandu/Chandra | Chandra | Kuja | Budha | Guru | Shukra | P |
| Angaharu/Kuja | Kuja | Budha | Guru | Shukra | Shani | P |
| Budha | Budha | Guru | Shukra | Shani | Ravi | P |
| Brahaspathi/Guru | Guru | Shukra | Shani | Ravi | Chandra | P |
| Sikuru/Shukra | Shukra | Shani | Ravi | Chandra | Kuja | P |
| Senesuru/Shani | Shani | Ravi | Chandra | Kuja | Budha | P |

### PK-003 — Sub-ruler order and main boundary

The proposed sub-ruler cycle is weekday order Ravi → Chandra → Kuja → Budha → Guru → Shukra → Shani, not the KH-002 main cycle. The first sub-ruler equals its parent. Practitioner evidence indicates continuity across a main boundary: after the fifth sub-ruler, the next weekday-order ruler is also the next KH-002 parent. State: **Provisionally corroborated; awaiting SRC-009**.

### PK-004 — Boundary membership

Proposed membership is `[start,end)`, matching the repository-wide deterministic boundary convention. Exact end belongs to the next subdivision. The fifth subdivision end must equal its approved parent end. State: **Awaiting Approval; dependent on PK-001 and KH-005**.

## Proposed CP-003 daytime Rahu rules

Candidate bundle: `RK-v0.2`, paired only with `CP-003-v0.2`. The rules are Proposed, and the package is Blocked from approval until PA-003 evidence prerequisites are complete.

### RK-001 — Daylight partition

Using one approved immutable CP-001-convention solar snapshot for the selected location/zone/civil date, require `sunrise < sunset` and partition `[sunrise,sunset)` into eight ordered segments. For integer nanosecond daylight duration `D`, boundary `i` is `sunrise + floor(D × i / 8)` for `i = 0..7`, and boundary 8 is exactly sunset. Compute from the common anchor with overflow-safe quotient/remainder arithmetic. Invalid chronology or overflow returns typed unavailable with no partial intervals. State: **Proposed; PA-003 Blocked**.

### RK-002 — Weekday allocation

Select Sunday segment 8, Monday 2, Tuesday 7, Wednesday 5, Thursday 6, Friday 4, or Saturday 3. Convert `now` in the active IANA zone and use that local civil date's weekday and sunrise/sunset. Before sunrise still selects today's later interval; after sunset remains today's completed interval; midnight or a zone/date change selects the new local date atomically. State: **Proposed; strongly corroborated but PA-003 Blocked on selected-tradition evidence**.

### RK-003 — Membership and status

Use `[start,end)`. At exact start status is `ActiveNow`; at exact end it becomes `CompletedToday`. Before start status is `LaterToday`; after end through the local date it is `CompletedToday`. `LaterToday.nextRelevantTransition = start`, `ActiveNow.nextRelevantTransition = end`, and `CompletedToday.nextRelevantTransition = null`; do not calculate tomorrow merely for this field. Missing/unapproved inputs produce a typed unavailable outcome. State: **Proposed; PA-003 Blocked**.

### RK-004 — Separation and provenance

Rahu is not an eighth ruler and does not alter CP-001, KH-002, or PK-003 sequences. Consume the same selected location/context fingerprint as CP-001; do not perform fallback selection inside the calculator. Retain system ID, CP/RK versions, civil date, zone, coordinates, solar anchors/engine, source/validation state, calculation instant, internal precision, validation tolerance, and display policy. Typed unavailable reasons include `ProfileNotApproved`, `LocationUnavailable`, `SolarAnchorUnavailable`, `InvalidSolarChronology`, `InvalidInput`, and `ArithmeticOverflow`; none invalidates an independently valid Seasonal Planetary Hora. State: **Proposed; PA-003 Blocked**.

### RK-005 — Night exclusion

Some Sri Lankan sources/calculators present a sunset-to-following-sunrise Rahu period, while broader sources often emphasize daytime. `CP-003-v0.2` deliberately excludes nighttime Rahu; this is a scope selection, not a claim that the variant is invalid. No nighttime rule is selected for Version 1.0. State: **Deferred/unresolved**.

## Change-control integrity

The approved `Change Request CR-001` is a product change-control ID, distinct from calculation rule `CR-001 — Solar anchors`. KH/PK/RK rules remain non-executable until their respective profile/rule gates are separately approved. Constants documented above do not authorize production code.
