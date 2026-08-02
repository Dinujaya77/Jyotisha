# Calculation Profile

| Field | Value |
|---|---|
| Status | Awaiting Approval |
| Version | 1.1 |
| Last updated | 2026-08-02 |
| Owner role | Business Analyst |
| Approval state | CP-001 approved under PA-001; CR-001 allocation approved, while CP-002/CP-003 retain separate domain approval gates |

## CP-001 — Sri Lanka Seasonal Planetary Hora V1.0

- State: Approved under PA-001 on 2026-08-01; algorithm and release-validation decisions remain separately gated.
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
- Precision: Anchor and boundary instants use integer nanoseconds on the UTC timeline. Fractional twelfths use CR-004 floor quantization from the common anchor, never iterative addition. Display formatting/rounding must never change interval membership.
- Output: 24 continuous versioned intervals, current/next interval, countdown, all solar anchors, and calculation/location provenance.
- Offline: One approved algorithm and required data are packaged locally; no provider mixing.
- Location provenance: Coordinates, optional display name, metre accuracy if provided, permission precision, source, source-specific timestamp/provenance, active zone, calculation time, profile version, and engine version. Device fixes use acquisition time; manual locations use selection time; defaults use dataset/version provenance and may have no acquisition time.
- Selected location modes: Fresh precise, fresh approximate, saved device, explicit manual Sri Lankan, and provisional Colombo default.
- Excluded variations: Equal sunrise-to-next-sunrise 24ths; centre-disc/no-refraction anchors; elevation/terrain/weather model; Rahu/Ketu rulers; activity advice; worldwide manual zone inference.
- Source references: SRC-001, SRC-002, SRC-004, SRC-006, SRC-007; SRC-003/SRC-008 document variations only.
- Validation references: VC-001–VC-019.

## Implementation prerequisites

- Select and approve the Version 1.0 solar algorithm and linked architecture/dependency decisions.
- Record one solar algorithm/version and its supported date/precision range.
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

- State: Awaiting separate domain approval; production execution requires approved RK rules and source-backed golden cases.
- Target allocation: Version 1.0 under approved Option C.
- Purpose: Neutral daytime Rahu Kalaya start/end and active/upcoming/completed status; no advice and no eighth ruler.
- Inputs: Current instant; selected coordinates; active IANA zone; applicable local civil date/weekday; calculated astronomical sunrise/sunset; solar/profile/engine versions.
- Proposed solar convention: Reuse the same approved astronomical solar snapshot as CP-001 for the location/date. If a trusted traditional authority requires a different sunrise convention, that conflict must be approved as a new profile rather than hidden.
- Division: Partition `[sunrise,sunset)` into eight exact continuous parts using common-anchor integer arithmetic.
- Weekday allocation: Sunday 8; Monday 2; Tuesday 7; Wednesday 5; Thursday 6; Friday 4; Saturday 3.
- Membership: `[start,end)`; exact start is active and exact end is not.
- Zones: Device system IANA zone for current-device mode; `Asia/Colombo` for manual/default Sri Lankan locations; weekday comes from the local civil date of the sunrise.
- Output: Interval, status, next relevant transition, solar/location/system/profile/rule/source provenance, or typed unavailable result.
- Exclusions: Nighttime Rahu; activity advice; insertion into the seven-ruler sequence.
- Source references: SRC-009, SRC-012, SRC-013, SRC-015–SRC-017.
- Validation references: VC-030–VC-039.

## Release-only blockers

Complete same-convention astronomical comparison, nominate and compare every enabled system against a trusted Sri Lankan practitioner/printed Panchanga or approved authority, explain discrepancies without rewriting expected results, and test actual family devices. CP-002's unresolved coverage questions block implementation, not merely release.
