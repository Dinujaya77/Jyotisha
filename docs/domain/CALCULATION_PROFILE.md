# Calculation Profile

| Field | Value |
|---|---|
| Status | Partially Complete |
| Version | 1.2 |
| Last updated | 2026-08-02 |
| Owner role | Business Analyst |
| Approval state | CP-001 approved under PA-001; SOLAR-001 approved under PA-004; CP-002/CP-003 retain separate domain approval gates |

## CP-001 — Sri Lanka Seasonal Planetary Hora V1.0

- State: Approved under PA-001 on 2026-08-01; normative solar package `SOLAR-001-v1.0` was approved under PA-004 on 2026-08-02; implementation still requires an approved delivery milestone and task.
- Purpose: Current/daily planetary Hora timing for private Sri Lankan family use, without interpretive advice.
- Inputs: Current instant; finite latitude `[-90,90]`; finite longitude `[-180,180]`; explicit IANA time zone; required civil dates; profile and engine version.
- Hora day: Calculated local sunrise through following calculated local sunrise.
- Solar convention: Apparent upper solar limb; standard atmospheric refraction; solar-centre zenith distance 90.8333°; sea-level, level/unobstructed horizon.
- Solar limitations: Actual visible events may differ because of terrain, mountains, buildings, observer elevation, weather, local horizon obstruction, or unusual refraction.
- Division: Sunrise-to-sunset divided into 12 equal internal durations; sunset-to-following-sunrise divided independently into 12 equal internal durations.
- Ruler cycle: Saturn → Jupiter → Mars → Sun → Venus → Mercury → Moon, cyclically.
- First ruler: The civil weekday ruler at the sunrise beginning the Hora day.
- Weekday mapping: Sunday Sun; Monday Moon; Tuesday Mars; Wednesday Mercury; Thursday Jupiter; Friday Venus; Saturday Saturn.
- Interval membership: Inclusive start, exclusive end `[start,end)`.
- Current-device zone: Device's current system IANA zone; coordinate alone never determines zone.
- Manual/default Sri Lankan zone: `Asia/Colombo`.
- Precision: PA-004-approved SOLAR-001 uses binary64/`StrictMath` intermediates and one half-even millisecond quantization per anchor. Anchor and boundary instants then use checked integer nanoseconds on the UTC timeline. Fractional twelfths use CR-004 floor quantization from the common anchor, never iterative addition. Display formatting/rounding must never change interval membership.
- Output: 24 continuous versioned intervals, current/next interval, countdown, all solar anchors, and calculation/location provenance.
- Offline: One approved algorithm and required data are packaged locally; no provider mixing.
- Location provenance: Coordinates, optional display name, metre accuracy if provided, permission precision, source, source-specific timestamp/provenance, active zone, calculation time, profile version, and engine version. Device fixes use acquisition time; manual locations use selection time; defaults use dataset/version provenance and may have no acquisition time.
- Selected location modes: Fresh precise, fresh approximate, saved device, explicit manual Sri Lankan, and provisional Colombo default.
- Excluded variations: Equal sunrise-to-next-sunrise 24ths; centre-disc/no-refraction anchors; elevation/terrain/weather model; Rahu/Ketu rulers; activity advice; worldwide manual zone inference.
- Source references: SRC-001, SRC-002, SRC-004, SRC-006, SRC-007; SRC-003/SRC-008 document variations only.
- Validation references: VC-001–VC-019 and SOL-G-001–SOL-G-010 / SOL-B-001–SOL-B-016.

## SOLAR-001 — Version 1.0 astronomical anchor profile

- State: **Approved** under PA-004 on 2026-08-02; executable only through a separately approved delivery milestone and task.
- Profile/rules/engine: `SOLAR-001-v1.0`; `SOL-R-v1.0`; `jyotisha.noaa-meeus` / `NOAA-MEEUS-001-v1.0`.
- Inputs: Proleptic-Gregorian civil date; explicit IANA zone; finite latitude `[-90,+90]` north-positive; finite longitude `[-180,+180]` east-positive; `SEA_LEVEL_FIXED`; exact approved profile and engine versions.
- Supported solar-day range: `1900-01-01..2100-12-31` inclusive. A three-anchor result additionally requires the following date; no date is clamped.
- Convention: Geometric solar-centre elevation `−0.8333°` (`90.8333°` zenith), conventionally representing apparent upper-limb contact through 16 arcminutes mean semidiameter plus 34 arcminutes average refraction at a level, unobstructed sea-level horizon.
- Elevation/weather: Numeric elevation adjustments and live pressure, temperature, humidity, weather, terrain, and horizon models are excluded. A different elevation policy is rejected, not silently ignored.
- Production: Independently written pure-Kotlin NOAA/Meeus-style equations and iteration frozen in `SOLAR_001.md`; no runtime library/network.
- Validation: Independent NREL-SPA family executed by pinned host-side pvlib 0.15.1; expected anchors frozen in `SOLAR-GOLDEN-001-v1.0`; no Android dependency.
- Precision: Binary64 computation; converge to 0.0005 seconds within five evaluations; quantize once to half-even milliseconds; dashboard labels round resolved local wall time to the nearest half-even minute; diagnostics may show the nearest local wall second.
- Acceptance: Each Sri Lankan sunrise, sunset, and following sunrise differs from the oracle by at most 60 seconds; derived Hora boundaries use the same 60-second astronomical comparison ceiling while retaining exact CR-004 adjacency.
- Outputs/failures: Versioned anchors/provenance/diagnostics or typed missing/invalid/unsupported/no-event/ambiguous/non-finite/convergence/overflow/chronology outcomes. No fabricated event.
- Source references: canonical PA-004 set SRC-004 and SRC-019–SRC-027; SRC-005 is a superseded historical cross-reference outside the approval unit.
- Validation references: SOLAR-GOLDEN-001-v1.0 (SOL-G-001–SOL-G-010), SOLAR-INTERMEDIATE-001-v1.0, and SOL-B-001–SOL-B-016.

## Implementation prerequisites

- Use the PA-004-approved `SOLAR-001-v1.0` package unchanged; any profile/engine/source/tolerance change requires a superseding approval.
- Record Android location implementation and any dependency.
- Approve exact Colombo coordinates, town dataset/provenance, acceptable location uncertainty, permission-downgrade retention, and backup exclusion mechanism.

## CP-002 — Sri Lankan Fixed Kala Hora and Panchama Kala

- State: **Blocked and non-executable**; CR-001 approves placement, not this profile.
- Target allocation: Version 1.1 under approved Option C.
- Purpose: Fixed Sri Lankan main-Kala and five-part Panchama timing, clearly distinct from CP-001.
- Provisionally supported inputs: Current instant; approved local sunrise/next sunrise; finite selected coordinates; explicit IANA zone; applicable civil weekday; approved terminology, source IDs, profile and engine versions.
- Proposed main rule: Fixed 60-minute main periods beginning at actual local sunrise; weekday ruler first; cyclic order Ravi → Sikuru/Shukra → Budha → Sandu/Chandra → Senesuru/Shani → Brahaspathi/Guru → Angaharu/Kuja.
- User/provisional Panchama rule: Five fixed 12-minute subdivisions per main period; first sub-ruler matches the parent; subsequent sub-rulers follow Ravi → Chandra → Kuja → Budha → Guru → Shukra → Shani. See PK-001–PK-004.
- Unresolved coverage: Under a 3,600-elapsed-seconds interpretation—or on an offset-stable date—twenty-four fixed periods end at `sunrise + 24 elapsed hours`, which normally differs from the following calculated astronomical sunrise. Across an offset transition, 60 wall-clock civil minutes versus 3,600 elapsed seconds is itself unresolved. Count, gap/overlap, truncation/extension, split/reset, partial-period, day/night, and duration behavior are not approved.
- Output while blocked: Typed unavailable state with system/profile/rule/evidence provenance; never a guessed schedule.
- Excluded inheritance: CP-001 seasonal interval division, count, and continuity do not apply merely because solar inputs are shared.
- Source references: SRC-002, SRC-009, SRC-012–SRC-015.
- Validation references: VC-020–VC-029, VC-037–VC-039.

## CP-003 — Sri Lankan Daytime Rahu Kalaya

- State: **Blocked from approval and non-executable**; production execution requires an approved profile/RK package and source-backed golden cases.
- Candidate package: `CP-003-v0.2` with `RK-v0.2`; deterministic structure reconciled, evidence incomplete.
- Target allocation: Version 1.0 under approved Option C.
- Purpose: Neutral daytime Rahu Kalaya start/end and active/upcoming/completed status; no advice and no eighth ruler.
- Inputs: Current instant; selected coordinates; active IANA zone; applicable local civil date/weekday; calculated astronomical sunrise/sunset; solar/profile/engine versions.
- Proposed solar convention: Explicitly inherit CP-001's approved apparent-upper-limb `90.8333°`, sea-level/unobstructed-horizon convention and the same immutable location/zone/solar-engine fingerprint for the applicable civil date. If a trusted traditional authority requires a different sunrise convention, that conflict must be approved as a new profile rather than hidden.
- Division: Partition `[sunrise,sunset)` into eight exact continuous parts using common-anchor integer arithmetic.
- Weekday allocation: Sunday 8; Monday 2; Tuesday 7; Wednesday 5; Thursday 6; Friday 4; Saturday 3.
- Membership: `[start,end)`; exact start is active and exact end is not.
- Civil date/zones: Convert `now` in the active zone. Device mode uses the current system IANA zone; manual/default Sri Lankan modes use `Asia/Colombo`. The resulting local civil date selects that date's sunrise/sunset and weekday. Before sunrise the result is `LaterToday`; during `[start,end)` it is `ActiveNow`; from exact end through the remaining local date it is `CompletedToday`; midnight or a zone/date change triggers atomic recomputation for the new local date.
- Next transition: `LaterToday` points to today's Rahu start; `ActiveNow` points to its end; `CompletedToday` has no next transition in the current-day result. Tomorrow is not calculated merely to populate this field.
- Failures: Missing/unapproved profile, no usable location, missing solar anchor, `sunrise >= sunset`, non-finite/invalid input, chronology failure, or arithmetic overflow returns a typed unavailable result and no interval. A profile-not-approved Rahu outcome cannot invalidate CP-001.
- Fallback: Consume the same already-selected fresh/saved/manual/default location snapshot as CP-001. If no usable fallback exists, return `LocationUnavailable`; never choose a different location inside the Rahu calculator.
- Output: Interval, `LaterToday | ActiveNow | CompletedToday`, nullable next relevant transition, solar/location/system/profile/rule/source provenance, or typed unavailable result.
- Exclusions: Nighttime Rahu; activity advice; insertion into the seven-ruler sequence.
- Source references: SRC-009, SRC-012, SRC-013, SRC-015–SRC-017.
- Validation references: VC-030–VC-039.

### CP-003 approval readiness

Do **not** request `APPROVE DAYTIME RAHU CALCULATION PROFILE CP-003` yet. The deterministic candidate is structurally complete, but these approval prerequisites remain:

1. Nominate SRC-009 or an equivalent selected Sri Lankan authority confirming the daytime convention, weekday allocation, inherited sunrise convention, and deliberate exclusion—not denial—of nighttime Rahu.
2. Add independently prepared, source-backed golden records for all seven weekdays with exact public/synthetic coordinates, zone, date, sunrise/sunset, selected segment, expected start/end, source, reviewer and tolerance; include a non-06:00 sunrise and a remainder-bearing partition.
3. Complete independent boundary/status records before/at/after sunrise, Rahu start/end, sunset, midnight, leap day, and location/zone change without deriving expected values from production code.
4. Approve one canonical user-facing `Rahu Kalaya` term/evidence label and freeze the exact candidate profile/rule/golden-dataset versions or hashes in the approval record.

That later phrase approves daytime CP-003/RK only; it must not approve architecture, UI, implementation, release, CP-002, or nighttime Rahu.

## Release-only blockers

Complete same-convention astronomical comparison, nominate and compare every enabled system against a trusted Sri Lankan practitioner/printed Panchanga or approved authority, explain discrepancies without rewriting expected results, and test actual family devices. CP-002's unresolved coverage questions block implementation, not merely release.
