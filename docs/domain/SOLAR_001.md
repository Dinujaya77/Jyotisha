# SOLAR-001 — Astronomical Solar Anchors

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.0 |
| Last updated | 2026-08-02 |
| Owner role | Lead Coordinator |
| Approval state | PA-004 Approved on 2026-08-02; implementation requires an approved delivery milestone and task |

## Approval unit and purpose

`SOLAR-001-v1.0` is the normative Version 1.0 profile for calculated astronomical sunrise, calculated astronomical sunset, and following calculated astronomical sunrise. It freezes the production method, event convention, numerical process, result contract, independent validation method, and display precision needed by approved CP-001 Seasonal Planetary Hora. A future approved CP-003 daytime Rahu profile must consume the same current-date sunrise and sunset; it must not recalculate them.

The approval unit is:

- profile ID `SOLAR-001-v1.0`;
- rule set `SOL-R-v1.0` (`SOL-R-001`–`SOL-R-014` below);
- production engine ID/version `jyotisha.noaa-meeus` / `NOAA-MEEUS-001-v1.0`;
- golden dataset `SOLAR-GOLDEN-001-v1.0` in `VALIDATION_CASES.md`;
- equation-conformance dataset `SOLAR-INTERMEDIATE-001-v1.0` in `VALIDATION_CASES.md`;
- canonical PA-004 source-role set SRC-004 and SRC-019–SRC-027, classified below;
- validation and display policies recorded in this document.

The repository owner supplied the exact phrase `APPROVE SOLAR-001` on 2026-08-02, moving PA-004 and this package to Approved. Approval authorizes the solar method only for a later approved delivery milestone and task; it does not authorize Kotlin/Gradle changes by itself, approve CP-003/RK, approve a town catalogue for product use, finalize UI, or approve release accuracy.

## Selected production approach

Use an independently written, stateless, pure-Kotlin NOAA/Meeus-style engine. The engine implements the equations and control flow below, uses no network or astronomy dependency, and does not copy NOAA calculator code, NREL software, or copyrighted Meeus text.

| Candidate | Correctness, range, and determinism | Cost, maintenance, and fit | Decision |
|---|---|---|---|
| A — local NOAA/Meeus-style | Official NOAA material documents the Meeus-derived terms. NOAA describes about one-minute theoretical rise/set accuracy within latitude ±72° and about ten minutes outside it. The project fixes the exact convention, equations, order, range, solver, and rounding below. Fully offline and deterministic. | Moderate project-owned numerical maintenance; small test surface; exact CP-001 control; no APK or supply-chain cost. Proportionate to a private family app and three daily anchors. | **Selected**. |
| B — local NREL SPA-derived | NREL SPA covers years −2000 through 6000 and reports solar-position uncertainty ±0.0003°. Appendix A.2 directly supports `−0.8333°` events. | Far larger coefficient, time-scale, interpolation, and verification surface than V1 needs. NREL's distributed C software has restrictive internal/noncommercial and no-redistribution terms; it is not copied or ported. | Rejected for production; the published SPA algorithm is the independent validation family. |
| C — Solarpositioning Java library | Maintained MIT library with SPA/Grena implementations, more than 1,000 upstream test points, stateless behavior, rise/set support, and no runtime dependencies. The inspected project requires Java 17 and documents a usual `0.833°` correction. | Exact `0.8333°` compatibility, Android/API 26 bytecode/desugaring, R8, artifact 2.0.13, APK, and supply-chain evidence are not established. Its broader solar-position surface adds no V1 product value. | Rejected for V1 production. Reconsider only through a dependency/ADR change. |

Candidate A is selected for correctness under the approved convention and proportionality, not merely ease. The engine can later be replaced behind `SolarEngine` by changing the engine version, regenerating independent vectors, and obtaining approval; Compose, ViewModels, location, Hora, and Rahu contracts do not change.

## Source roles and independence

The canonical PA-004 source-role set is exactly SRC-004 and SRC-019–SRC-027. SRC-005 is a superseded Phase A placeholder retained for history and cross-reference only; it is not part of the PA-004 approval unit. Within the canonical set:

- **Normative event source:** USNO SRC-004 defines apparent upper-limb contact under average refraction at a level surface, represented by solar-centre zenith `90.8333°`.
- **Normative production-equation artifact:** frozen NOAA spreadsheet SRC-020 supplies the SOL-R-007 term family and constants. The exact project control flow below, not a live NOAA calculator, is normative.
- **Validation sources:** NREL TP-560-34302 SRC-021 is the independent higher-order SPA authority; pinned pvlib 0.15.1 SRC-022 executes Appendix A.2 host-side; frozen GeoNames SRC-023 supplies public validation coordinates. None is an Android dependency.
- **Explanatory lineage/cross-checks:** NOAA limitations page SRC-019, Meeus bibliographic lineage SRC-024, and NOAA note SRC-025 explain accuracy, ancestry, signs, or approximate equations but are not executable alone.
- **Evaluated and rejected production alternatives/context:** Solarpositioning SRC-026 and restricted NREL software page SRC-027 record why those implementation paths were not selected.

Production and validation share general solar astronomy and the same selected event threshold, but not code or equation sets. NOAA-derived production results must never be validated only against NOAA output or another port of the production equations.

## Normative event convention

### SOL-R-001 — Event meaning

Calculated sunrise is the ascending modelled instant when the Sun's geometric centre reaches elevation `−0.8333°`, equivalently zenith distance `90.8333°`, relative to a level, unobstructed sea-level horizon. Calculated sunset is the descending event under the same definition.

The `0.8333°` total is the conventional `50` arcminutes: approximately `16` arcminutes for mean solar semidiameter plus `34` arcminutes for average atmospheric refraction. It approximates apparent upper-limb contact. It is not a claim that the Sun's centre is visibly at that geometric elevation under the user's actual conditions.

The production equations use geocentric solar coordinates and a zero-elevation spherical-observer horizon. They do not apply topocentric parallax, terrain, dip of horizon, local horizon profiles, buildings, mountains, or observer-height correction.

### SOL-R-002 — Elevation and atmosphere

Version 1.0 has the typed elevation policy `SEA_LEVEL_FIXED`. It does not accept a numeric observer elevation. A request for an elevation-adjusted profile returns `EngineProfileMismatch(UnsupportedElevationPolicy)`; a numeric elevation must not be silently ignored.

Pressure, temperature, humidity, cloud, and live weather are intentionally absent. The fixed `34` arcminute conventional refraction component represents average conditions only. Actual visible sunrise/sunset can differ by minutes because of atmosphere, terrain, observer height, and obstructions. Nuwara Eliya's catalogue elevation is retained as source metadata only and is not passed to this engine.

## Inputs and validation

### SOL-R-003 — Request

The language-neutral request contains:

| Input | Contract |
|---|---|
| `civilDate` | Proleptic-Gregorian local date in `1900-01-01..2100-12-31`, inclusive. |
| `zoneId` | Explicit valid IANA identifier and platform zone-rules version/fingerprint where available. |
| `latitudeDegrees` | Finite decimal degrees in `[-90,+90]`; north positive. |
| `longitudeDegrees` | Finite decimal degrees in `[-180,+180]`; east positive. |
| `elevationPolicy` | Exactly `SEA_LEVEL_FIXED`. |
| `profileId` | Exactly `SOLAR-001-v1.0` and an approved-state token. |
| `engineId/version` | Exactly `jyotisha.noaa-meeus` / `NOAA-MEEUS-001-v1.0`. |

Coordinates are not wrapped, reflected, or clamped. Reject out-of-range, NaN, and infinity. Canonicalize IEEE negative zero to positive zero before cache keys/provenance. Longitude `+180` and `−180` remain distinct valid representations of the same meridian; do not silently rewrite one to the other.

Inputs may carry all finite `Double` precision, while the proposed town catalogue stores integer microdegrees. Extra decimal digits improve reproducibility, not physical location accuracy. Missing coordinates return `MissingLocation`, not zero coordinates.

All valid latitudes are accepted as inputs. A valid high-latitude request can still produce a typed missing-event outcome. Dates outside the range return `UnsupportedDate`; no clamping. A complete three-anchor request for `2100-12-31` is unavailable because its following date is outside the engine range. A pre-sunrise CP-001 context on `1900-01-01` is likewise unavailable if it requires the previous date.

## Civil date, time zone, and absolute time

### SOL-R-004 — Civil-day interval

Resolve `start = civilDate.atStartOfDay(zoneId)` and `end = (civilDate + 1 day).atStartOfDay(zoneId)` using the supplied IANA rules. The interval is `[start,end)` on the UTC timeline and may be 23, 24, or 25 hours. `atStartOfDay` means the earliest valid zoned time: a midnight gap advances to the first valid time and an overlap uses the earlier valid offset. If `end <= start`, return `SkippedCivilDate`.

An event belongs to the requested civil date only if its absolute instant is inside `[start,end)` and converting it through the same zone produces `civilDate`. Search UTC base dates that intersect this interval plus one UTC date before and after; never add or subtract 24 hours merely to force a local date.

UTC is represented by Unix/Java `Instant` semantics with 86,400 SI seconds per represented day. Version 1.0 treats UTC as UT1 for the low-order production equations (`ΔUT1 = 0`) and introduces no predictive leap-second or `ΔT` table. That time-scale simplification is part of the engine version and validation tolerance.

Current-device mode uses the current system IANA zone. A system zone change invalidates the context and causes atomic recalculation for the newly applicable local date. Manual/default Sri Lankan locations always use `Asia/Colombo`, even if the device zone changes. A zone is never derived from longitude. Historical `Asia/Colombo` offsets come from IANA rules; it is not treated as a fixed `+05:30` offset for all dates.

### SOL-R-005 — Following sunrise

Following sunrise is the sunrise event for `civilDate + 1` using the exact same coordinates, zone, profile, engine, convention, and canonical input fingerprint. It is calculated, not produced by adding 24 hours. Midnight changes the requested civil date and rebuilds the atomic context. A missing following sunrise makes the three-anchor result unavailable; it must not be fabricated from the current sunrise.

## Exact production equations

### SOL-R-006 — Julian day and angle rules

For a candidate absolute instant with checked signed `epochSecond` and `nanoOfSecond`:

```text
JD = 2440587.5 + toDouble(epochSecond) / 86400.0
               + toDouble(nanoOfSecond) / 86400000000000.0
T  = (JD - 2451545.0) / 36525
```

Evaluate in the written order with IEEE-754 binary64 (`Double`) and Java/Kotlin `StrictMath` functions for reproducible cross-platform results under their specified accuracy semantics. No universal correctly-rounded guarantee is asserted for transcendental functions. Degrees-to-radians is `degrees × π / 180`; radians-to-degrees is `radians × 180 / π`, using `StrictMath.PI`. Do not use locale-sensitive parsing, extended-precision decimal intermediates, fast-math, fused algebraic rewrites, mutable lookup state, or platform clock/network input.

`normalize360(x)` is `x - 360 × floor(x / 360)`, yielding `[0,360)`; canonicalize a zero result to positive zero. Normalize only values explicitly marked below. No input coordinate is normalized.

### SOL-R-007 — Solar terms

For each event iteration compute, in this order:

```text
L0 = normalize360(280.46646 + T × (36000.76983 + T × 0.0003032))
M  = 357.52911 + T × (35999.05029 - 0.0001537 × T)
e  = 0.016708634 - T × (0.000042037 + 0.0000001267 × T)

C = sin(M)     × (1.914602 - T × (0.004817 + 0.000014 × T))
  + sin(2 × M) × (0.019993 - 0.000101 × T)
  + sin(3 × M) × 0.000289

trueLongitude     = L0 + C
omega             = 125.04 - 1934.136 × T
apparentLongitude = trueLongitude - 0.00569 - 0.00478 × sin(omega)

meanObliquity = 23 + (26 + (21.448 - T × (46.815 + T × (0.00059 - T × 0.001813))) / 60) / 60
obliquity     = meanObliquity + 0.00256 × cos(omega)
declination   = asin(sin(obliquity) × sin(apparentLongitude))
y             = tan(obliquity / 2)²

equationOfTimeMinutes = 4 × degrees(
    y × sin(2 × L0)
  - 2 × e × sin(M)
  + 4 × e × y × sin(M) × cos(2 × L0)
  - 0.5 × y² × sin(4 × L0)
  - 1.25 × e² × sin(2 × M)
)
```

Angles passed to `sin`, `cos`, `tan`, and `asin` are converted as stated in SOL-R-006. `declination` is converted back to degrees before the event formula. These constants and equation order are adapted into an independently authored specification from the official NOAA day spreadsheet SRC-020; the project replaces its `90.833°` cell with the approved `90.8333°` profile constant.

### SOL-R-008 — Event iteration

For every searched UTC base date `B`, let `JD0` be Julian day at `B 00:00:00Z`. Solve sunrise and sunset separately:

1. Initial candidate Julian day: `candidateJD = JD0 - longitudeDegrees / 360`.
2. Evaluate SOL-R-007 at `candidateJD`.
3. Let:

```text
denominator = cos(latitude) × cos(declination)
hourAngleArgument = cos(90.8333°) / denominator
                  - tan(latitude) × tan(declination)
```

4. If `abs(denominator) <= 1e-15`, this base-date event is `NoEvent(DegenerateLatitudeGeometry)`.
5. Compute four binary64 bounds once in the written order: `negativeOuter = −1.0 − 1e-12`, `negativeInner = −1.0 + 1e-12`, `positiveInner = 1.0 − 1e-12`, and `positiveOuter = 1.0 + 1e-12`. During iteration, clamp a raw argument in `[negativeOuter,−1.0)` to `−1.0` and one in `(1.0,positiveOuter]` to `1.0`. A value below `negativeOuter` or above `positiveOuter` is `NoEvent(HorizonNotCrossed)` for that base-date/event, not a fabricated angle. Comparisons use these computed `Double` bounds directly; do not compare a recomputed subtraction/distance.
6. `H = degrees(acos(hourAngleArgument))`. Use `+H` for sunrise and `−H` for sunset.
7. `eventMinutesUTC = 720 - 4 × (longitudeDegrees + signedH) - equationOfTimeMinutes`.
8. New candidate: `candidateJD = JD0 + eventMinutesUTC / 1440`.
9. Repeat steps 2–8 for at most five evaluations. Converge when consecutive `eventMinutesUTC` values differ by at most `0.0005` seconds (`1 / 120000` minute), inclusive.
10. If five evaluations do not converge, return `ConvergenceFailure` for that event.

An event candidate is `B 00:00:00Z + eventMinutesUTC × 60 seconds`; minutes outside `[0,1440)` are allowed before civil-date filtering. On the converged evaluation, a raw argument in `[negativeOuter,negativeInner]` or `[positiveInner,positiveOuter]`, inclusive, is a `GrazingHorizon` diagnostic, not proof of a unique ascending or descending crossing. Compute its tentative instant using the endpoint-clamped value only when outside `[-1,1]` and only for civil-date filtering; never publish it as an event. The named bounds from step 5 are reused without recomputation.

Search UTC base dates in ascending order. For each base date process sunrise before sunset. A non-finite value, checked-arithmetic overflow, or convergence failure stops that one-day calculation immediately and returns the exact reason, event kind, requested civil date, and UTC base date; the first failure in that fixed order wins. Otherwise filter each regular or grazing result through SOL-R-004, then aggregate separately by event kind:

1. no relevant regular candidate and no relevant grazing diagnostic means that event is absent;
2. any relevant grazing diagnostic means `AmbiguousSolarEvents(eventKind,civilDate,GrazingHorizon)`;
3. deduplicate regular candidates only when their quantized instants are identical;
4. more than one distinct regular candidate means `AmbiguousSolarEvents(eventKind,civilDate,MultipleCandidates)`;
5. exactly one regular candidate means that event is available.

At the one-day boundary, ambiguity for sunrise takes precedence over ambiguity for sunset; then both absent gives `NoSunriseAndNoSunset(civilDate)`, sunrise absent gives `NoSunrise(civilDate)`, sunset absent gives `NoSunset(civilDate)`, and two unique candidates give success. The coordinator evaluates the requested day before the following day. A requested-day failure wins; otherwise a following-day sunrise failure is returned with `eventKind=FollowingSunrise` and `civilDate=D+1`. A following-day sunset is calculated for the pure day outcome but is not part of the public anchor triple.

### SOL-R-009 — Quantization and chronology

Round each converged unquantized event once to the nearest integer UTC epoch millisecond; an exact half-millisecond tie rounds to the even millisecond. In Kotlin-equivalent terms, apply `StrictMath.rint` to checked epoch milliseconds and convert to `Instant`. The resulting nanoseconds are a multiple of `1,000,000`.

Do not round intermediates. Do not round to seconds/minutes inside the engine. After quantization require:

```text
sunrise < sunset < followingSunrise
```

and require each event to retain its intended civil-date association. Otherwise return `InconsistentSolarOrdering`. CR-004 then converts these exact anchors to checked integer UTC epoch nanoseconds and calculates every Hora boundary from a common anchor; it never repeatedly adds rounded interval durations.

## Outputs and typed outcomes

### SOL-R-010 — Success model

The pure engine unit returns one date's typed `SolarDayOutcome` (sunrise and sunset or unavailable). The context coordinator combines the current date and following date into the public `SolarAnchorSetOutcome`. A successful anchor-set result contains:

- quantized sunrise, sunset, and following-sunrise `Instant` values;
- requested civil date, following civil date, zone ID, resolved local date/time/offset for each anchor, and zone-rules version/fingerprint where available;
- canonical latitude/longitude and `SEA_LEVEL_FIXED` policy;
- `SOLAR-001-v1.0`, `SOL-R-v1.0`, engine ID/version, equation-source IDs/hashes, and golden-dataset version;
- calculation status `Success` and warnings;
- model class (`NOAA ±72° expectation` or `HighLatitudeLowConfidence`), validation class, tolerance, engine quantization, and display policy as separate fields;
- calculation instant supplied by the coordinator, input/location provenance, and a non-displayable context fingerprint;
- diagnostics: iteration count per anchor, final equation of time, declination, hour angle argument/value, unquantized event minutes, quantization delta, and UTC base date.

Diagnostics are local/offline and must not include private coordinates in logs. The context fingerprint is sensitive, is never displayed/logged/persisted, and is invalidated by coordinates, zone/day bounds, profile, or engine version.

### SOL-R-011 — Unavailable model

Expected unavailable conditions are values, not normal-flow exceptions:

| Reason | Meaning |
|---|---|
| `MissingLocation` | Coordinates were not supplied. |
| `InvalidCoordinate(axis,valueClass)` | Non-finite or out-of-range latitude/longitude. |
| `InvalidZone` | IANA ID/rules unavailable. |
| `UnsupportedDate` | Requested or required adjacent date is outside the supported range. |
| `SkippedCivilDate` | Zone rules yield a non-positive civil-day interval. |
| `EngineProfileMismatch` | Profile/engine/elevation policy is absent, unapproved, or incompatible. |
| `NoSunrise(eventKind,civilDate)` / `NoSunset(eventKind,civilDate)` | No event of that type belongs to the identified civil date; `eventKind` distinguishes current sunrise/sunset from following sunrise. |
| `NoSunriseAndNoSunset(civilDate)` | Neither crossing belongs to the civil date; includes polar day/night or persistent twilight without fabricating a physical label not proven by the solver. |
| `AmbiguousSolarEvents(eventKind,civilDate,cause)` | A relevant grazing diagnostic exists or more than one distinct candidate survives; cause is `GrazingHorizon` or `MultipleCandidates`. |
| `NonFiniteIntermediate` | A required numerical value is NaN/infinite. |
| `ConvergenceFailure` | Five evaluations did not meet the frozen tolerance. |
| `ArithmeticOverflow` | Epoch/JD/nanosecond conversion exceeds checked representation. |
| `InconsistentSolarOrdering` | Civil-date association or `sunrise < sunset < followingSunrise` fails. |
| `CalculationUnavailable` | Unexpected internal failure translated at the boundary with safe partial provenance. |

Only `Success` supplies anchors. An unavailable following sunrise invalidates a CP-001 schedule that needs it. An unavailable current-date sunrise/sunset invalidates future Rahu only; it must not corrupt an independently coherent prior CP-001 snapshot. Unexpected programmer defects may still throw internally, but the public boundary translates them to `CalculationUnavailable` and records no partial schedule.

### SOL-R-012 — High-latitude behavior

Never synthesize rise/set from neighbouring days and never substitute twilight. Regular results for `abs(latitude) > 72°` carry `HighLatitudeLowConfidence`. Missing crossings and endpoint/tangent geometry follow the exact precedence in SOL-R-008. Both exact poles are valid inputs but produce `NoSunriseAndNoSunset` when the denominator is degenerate. The app initially targets Sri Lanka; high-latitude support is deterministic unavailable behavior, not a release-quality worldwide accuracy claim.

## Shared-context and architecture contract

### SOL-R-013 — Consumption, caching, threading, and replacement

`SolarEngine.calculate(request)` is synchronous, CPU-only, Android-free, stateless, immutable, and thread-safe. It owns no clock, dispatcher, cache, cancellation, location provider, persistence, or UI formatting.

The coordinator runs it off-main and checks cancellation/generation between the required `D−1`, `D`, and `D+1` requests. A running pure calculation may finish, but a cancelled/superseded generation cannot publish. A complete result replaces the prior immutable `DailyTimingBundle` atomically; countdown ticks do not rerun astronomy.

Use a small bounded in-memory cache (target 32 date entries). Key by canonical coordinate bits, zone ID plus resolved civil-day start/end instants, civil date, profile/rule version, and engine version. Do not key numerical solar values by display name, reported accuracy, acquisition time, or `now`; these remain enclosing provenance. Compute outside a short synchronized cache get/put. Parallel duplicate work is acceptable. Do not persist solar events or schedules.

`DailyCalculationContext` holds one sampled `now`, selected-location provenance, explicit zone, date-keyed typed solar outcomes, profile/engine versions, and the canonical context fingerprint. Seasonal Hora consumes these exact anchors. Future approved daytime Rahu consumes the exact same `D` sunrise/sunset object. No feature calls a second solar engine or alters an anchor.

Replacement requires a new engine version, new cache namespace, regenerated independent vectors, architecture impact review, and explicit approval. No UI/ViewModel/Hora/Rahu/location-provider API change is required.

## Accuracy, validation, and presentation

### SOL-R-014 — Four separate precision concepts

1. **Internal computation/representation:** binary64 solar intermediates; one half-even millisecond anchor quantization; checked nanosecond timeline for downstream exact partitions. This is determinism, not physical nanosecond accuracy.
2. **Algorithmic expectation:** NOAA reports its Meeus-derived calculations as theoretically within about one minute at latitudes within ±72°. `NOAA-MEEUS-001-v1.0` adopts that as a model target for ordinary Sri Lankan geometry, subject to implementation evidence. Do not convert NREL's angular `±0.0003°` claim into a sunrise-time claim.
3. **Validation tolerance:** for every approved Sri Lankan golden, absolute production-oracle difference must be `<=60.000` seconds independently for sunrise, sunset, and following sunrise. Derived seasonal Hora boundaries must be `<=60.000` seconds from boundaries independently derived from the oracle anchors, while still meeting CR-004's exact internal adjacency/endpoint rules. Anchor quantization error must be `<=0.500` ms. Values above ±72° have a diagnostic comparison ceiling of 600 seconds only; they are not V1 release evidence. Event/no-event disagreement is a review failure, never solved by widening tolerance.
4. **User-facing precision:** `SOLAR-DISPLAY-001-v1.0` first converts the exact instant through the active zone to one resolved local date/time/offset. It then rounds that local wall value to the nearest civil minute for dashboard labels, or nearest civil second for Method/diagnostic labels, with half-even ties. Rounding operates on the local epoch-day plus nano-of-day and may carry into the adjacent local date. The resulting label is formatted directly; it is not resolved back to an instant, so an overlap keeps no hidden offset choice and a gap may produce a wall label that has no instant. Where an offset transition falls within the rounding window, the UI must include the original resolved offset in Method view and tests must freeze the label. Shared boundaries are formatted once and reused by adjacent rows; each interval must not independently round its copy. Exact internal instants—not labels—determine membership, current/next state, and transitions.

The `60`-second tolerance is selected before production output exists: it follows the approved NFR-002/VC-009 criterion and NOAA's stated model expectation for Sri Lanka. It is not loosened to accommodate an implementation. Any oracle disagreement records magnitude, source versions/configuration, likely time-scale/ephemeris/refraction/rounding cause, selected expected result, and reviewer rationale; expected values are not rewritten to pass.

User-facing limitation text must say “calculated astronomical sunrise/sunset,” identify the sea-level/average-refraction/unobstructed-horizon model, and state that observed visibility may differ by minutes. It must not say “observed,” “exact,” “nanosecond accurate,” “NOAA certified,” or “USNO uses Jyotisha's equations.”

## Frozen validation method

`SOLAR-GOLDEN-001-v1.0` is generated outside Android by pinned pvlib 0.15.1, wheel SHA-256 `EBD41A93DBC215DB8CA3B3EE7D69BDE213A1AA861B8313D5D3BE6F82FB74B6B8`, calling its independent NREL-SPA `spa.transit_sunrise_sunset` with `delta_t=67.0`, `numthreads=1`, and the exact `−0.8333°` threshold present in that function. The controlled environment is Python 3.12.2, NumPy 2.5.1, pandas 3.0.5, and tzdata 2026.3. Expected instants are frozen to half-even milliseconds after selecting the event whose IANA local date matches the test date.

The `67.0`-second ΔT is an explicit oracle input, not a production input or a claim of historical ΔT. Its effect on these sunrise/set comparisons is inside the preselected tolerance. The validation implementation and its BSD-3-Clause wheel are not packaged, added to Gradle, or called at runtime. NREL report SHA-256 is `1B9E2B6131D89EBC17FE4C047E64A0168143C949BFE6FAFE06D856D651555CC2` (official DOE OSTI copy, retrieved 2026-08-02).

Golden inputs/outputs and all behavior/property cases are frozen in `VALIDATION_CASES.md`. Primary-source services that publish only minute-level values may corroborate display output but cannot replace this second-level oracle. Traditional/Panchanga comparison remains a release gate separate from astronomical algorithm validation.

## Approval checklist

- [x] Exact event convention and limitations recorded.
- [x] Exact production equations, constants, order, solver, range, failure, and rounding frozen.
- [x] Production/validation implementations are independent.
- [x] Frozen six-location Sri Lankan golden set covers required date classes and range edges.
- [x] Time-zone, invalid-input, high-latitude, determinism, persistence, and reuse behavior specified.
- [x] Dependency impact is zero for production.
- [x] Independent QA findings resolved and final recheck recorded; zero open findings on 2026-08-02.
- [x] Repository owner supplied `APPROVE SOLAR-001` on 2026-08-02.

No application or Gradle work is permitted until a later approved delivery milestone and task explicitly authorize implementation.
