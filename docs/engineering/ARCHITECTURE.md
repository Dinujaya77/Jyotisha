# Architecture

| Field | Value |
|---|---|
| Status | Approved |
| Version | 0.7 |
| Last updated | 2026-08-26 |
| Owner role | Android Architect |
| Approval state | PA-002 architecture, PA-004 SOLAR-001, DA-004 UI, and DP-001 delivery are Approved; explicit task/data/profile/evidence/release gates remain |

## Recommendation summary

Version 1.0 retains one `:app` module, one activity, Compose/Material 3, unidirectional data flow, immutable state, and pure deterministic calculation code. The approved architecture choices are:

- independently written pure-Kotlin NOAA/Meeus-style solar engine;
- pure-Kotlin `PlanetaryHoraCalculator` implementing approved CP-001/calculation rules CR-001–CR-010, with separate proposed calculators for other timing systems;
- existing AndroidX `LocationManagerCompat.getCurrentLocation()` behind `DeviceLocationProvider`, with no Google Play Services Location dependency;
- Preferences DataStore for one versioned active-location record;
- a reviewed, bundled GeoNames Sri Lanka snapshot for the manual-town catalogue;
- `minSdk 26`, `compileSdk 36`, and `targetSdk 36`;
- manual construction at the app composition root; no DI framework, database, networking, background service, navigation dependency, or new Gradle module.

PA-002 approves the architecture boundaries, PA-004 approves normative `SOLAR-001`, DA-004 approves the final UI, and DP-001 approves the delivery plan. None automatically starts implementation or dependency/application/Gradle edits; CP-003/RK, exact data, explicit task start, produced evidence, and release gates remain in force.

## Approved architecture boundary classification

| Decision | Classification | Reconciled recommendation |
|---|---|---|
| `SOLAR-001` | Method family and engine package Approved | PA-002 selects pure-Kotlin NOAA/Meeus. PA-004 approves `SOLAR-001-v1.0`, SOL-R-001–SOL-R-014, SOLAR-GOLDEN-001-v1.0, and SOLAR-INTERMEDIATE-001-v1.0; implementation requires a later approved milestone/task. |
| Device location | Approved | Existing AndroidX Core `LocationManagerCompat.getCurrentLocation()` behind `DeviceLocationProvider`; no Play Services; permission-derived precision, explicit provider filtering, one-winner cancellation and API/OEM evidence required. |
| Freshness/uncertainty | Approved | Timeout 20 s; returned monotonic age `<=2 min`; persisted wall-clock stale age `>=24 h`; movement `>=10 km`; accuracy improvement requires both `>=50 m` and `>=25%`; finite reported accuracy `0..20,000 m`; warn above 10 km. |
| Sri Lankan towns | Source/schema approved; exact data pending | Frozen GeoNames CC BY 4.0/WGS84 source, schema, stable IDs, offline use and attribution are selected; separately review the snapshot/hash, extraction, exact rows, Colombo default and family-town coverage. Town coordinates are source/town-centre points, not physical-accuracy claims. |
| Persistence | Approved | One application-scoped Preferences DataStore 1.2.1 with mutually exclusive device/manual/default records. Device zone is derived from the current `ZoneSource`, never persisted as authoritative; manual/default derive `Asia/Colombo`. |
| Backup/transfer | Policy approved; release evidence blocker | Store location data under credential-protected `noBackupFilesDir/location-data/`; retain explicit legacy and Android 12+ cloud/device-transfer exclusions as defence in depth; prove clean-device restore/transfer. |
| Package/namespace | Approved and implemented under V1-M1-02 | Visible name `Jyotisha`; namespace and application ID `io.github.dinujaya77.jyotisha`. Keep both identifiers identical and preserve this update lineage. |
| Dependencies | DEP-011–DEP-013 implemented; DEP-014 deferred | V1-M1-03 added Lifecycle ViewModel KTX, ViewModel Compose, and Runtime Compose 2.9.2 with verified resolution/licence/API evidence. DataStore remains deferred to V1-M4-03. No Navigation, Hilt, Room, solar, Play Services, networking, analytics or encryption dependency. Direct coroutine APIs may not rely silently on transitives. |
| Internal precision | Approved shared contract; solar anchor rule separate | `Double`/`StrictMath` only for solar intermediates; quantize each anchor once under `SOLAR-001`; thereafter integer UTC epoch nanoseconds with overflow-safe quotient/remainder partitions. |
| Display precision | Approved under PA-004 as `SOLAR-DISPLAY-001-v1.0` | Formatting is versioned and outside calculators. Dashboard/shared boundaries use local-wall nearest-minute half-even; Method uses nearest-second half-even. Exact instants drive membership/countdown; shared boundaries format once. DA-004 governs component placement only. |

## Current M1/M2 implementation baseline

The Gradle root `Jyotisha` has one Android application module with namespace/application ID `io.github.dinujaya77.jyotisha`. One `MainActivity` hosts a bounded manual Compose shell for Dashboard, Timeline, and Method plus Location, Settings, and About rendering-only children. Current SDK values are `compileSdk 36`, `targetSdk 36`, and provisional `minSdk 26`; only `minSdk` sets the install floor. Existing AndroidX Core KTX 1.17.0 provides `LocationManagerCompat`, and V1-M1-03 added the approved Lifecycle ViewModel KTX, ViewModel Compose, and Runtime Compose 2.9.2 artifacts. The manifest has no location permissions and retains the baseline backup/data-extraction rules. Deterministic clock/zone and shell reducer/Compose tests exist. M2 implements the approved 21-role Celestial Archive light/dark semantic palette, fixed Material 3 mapping with dynamic colour disabled, typography, spacing/gutters, shapes, elevation, wash, motion/reduced-motion, icon/illustration, focus and 48dp interaction-target tokens, accessible reusable presentation components, and eight deterministic synthetic preview fixtures. Solar, Hora, Rahu, location acquisition, repository, persistence, and integrated feature-state behavior remain unimplemented.

V1-M1-02 replaced the template identity once with the owner-approved permanent identity. Future changes to the application ID require an explicit update-lineage decision; do not introduce suffixes or flavours opportunistically.

M1 and M2 are Done, Lead/owner accepted, and incorporated into `dev`; neither was pushed. The shell uses primitive saveable state for activity/configuration recreation; full operating-system process-death restoration is not claimed. V1-M3-01 through V1-M3-04 are Done with the bounded shell preserved; M3 is In Review on its milestone branch, not Lead/owner accepted, incorporated into `dev`, or pushed. Production calculation/location states remain unavailable and synthetic data remains preview/test-only; no M4+ architecture behavior was added. Manual TalkBack, D-pad/focus-restoration, actual 200% font-scale and compact-landscape inspection, grayscale/high-contrast/OLED review, magnification, API-26 runtime, physical-family-device, screenshot, signing, update-lineage and release evidence remain mandatory under their approved task-specific gates. M4 and later milestones remain unstarted (or Blocked where recorded).

## System shape

Suggested packages inside the single app module:

```text
app/                    composition root, activity, top-level destination state
domain/model/           immutable domain values and typed failures
domain/solar/           SolarEngine and local algorithm
domain/timing/common/   shared context, intervals, provenance, typed outcomes
domain/timing/planetaryhora/  approved CP-001 calculator and invariants
domain/timing/rahu/     proposed CP-003 daytime Rahu calculator
domain/location/        acceptance/selection policy and models
data/location/          LocationSelectionRepository, DataStore, TownCatalog
platform/location/      Android provider and permission adapters
platform/time/          Clock, ZoneSource, system-change observer
ui/dashboard/           Now and Day state/presentation
ui/location/            introduction, selection, acquisition, recovery
ui/methodology/         method, sources, privacy, limitations
ui/theme/               semantic theme tokens and approved direction
```

`domain/**` has no Android imports. CP-002 `SriLankanKalaHora`/`PanchamaKala` boundaries remain conceptual; Version 1.0 creates no source package, stub calculator, or dependency for them. Interfaces are introduced at real substitution boundaries—solar, location, clock/zone, persistence—not as one-interface-per-class ceremony. No Hilt/service locator is proposed; a small application composition root constructs production objects and test factories construct fakes.

## State and data flow

UI events flow to one activity-scoped ViewModel owning selected location, multi-date calculation context, atomic timing bundle, small destination state and acquisition state. It calls repositories/engines and exposes one immutable `StateFlow<DashboardUiState>`. Acquisition progress/failure is distinct from the last complete `CalculationSnapshot`; composables emit intents and render only.

```text
UI event
  → ViewModel intent
  → location/time repository or pure engines
  → validate complete candidate snapshot
  → persist selected-location record when applicable
  → atomically publish one immutable UI state
```

Timeout, cancellation, null, provider failure, storage recovery, or calculation failure retains the last coherent snapshot and adds a non-destructive state message. A late callback after cancellation/timeout cannot publish. Derived solar/timing schedules are cached only in memory by coordinates, zone, civil date, system/profile version, and engine version; they are recomputed rather than persisted.

The countdown never reruns astronomy. The schedule is precomputed, remaining duration derives from an injected clock, and the app reconciles at exact boundaries plus lifecycle/time/date/zone events.

## Solar and timing calculation

### Selected solar method family and Approved normative specification

Through a later approved implementation milestone/task, implement the small pure-Kotlin `NOAA-MEEUS-001-v1.0` engine specified by PA-004-approved SOL-R-001–SOL-R-014. It solves Sun-centre events at geometric altitude `−0.8333°` (zenith `90.8333°`) with `SEA_LEVEL_FIXED`, uses civil dates `1900-01-01` through `2100-12-31`, explicit IANA day bounds and UTC instants, binary64/`StrictMath`, a five-evaluation solver, and one half-even millisecond anchor quantization. Outside/invalid/missing/no-event/ambiguous/non-convergent/chronology cases are typed unavailable.

NOAA describes its Meeus-based sunrise/sunset result as theoretically accurate within one minute for latitudes inside ±72°, which includes Sri Lanka, while warning that physical observations vary with atmospheric and horizon conditions. NOAA also states its calculator is no longer actively maintained; the project therefore owns the implementation and tests rather than copying unsupported web code. NREL SPA supplies independent validation, not production code.

`SOLAR_001.md` freezes the exact official NOAA equation-artifact hash and independently authored equation order; project `90.8333°` override; epoch-derived Julian day; sign/units; IANA civil-date search; five-evaluation event iteration; inverse-domain margin; typed failures; half-even millisecond quantization; diagnostics; versions; and pinned NREL-SPA/pvlib goldens. `APPROVE VERSION 1.0 ARCHITECTURE` selected the family; PA-004 approved the complete normative package; DP-001 places implementation in M5. Coding still requires the accepted M1 quality baseline and explicit task start.

### SOLAR-001 API, execution, and replacement boundary

Use one synchronous pure boundary equivalent to `SolarEngine.calculate(SolarDayRequest): SolarDayOutcome`. The request contains date, canonical coordinates, explicit zone, and approved profile; the outcome is immutable `RegularDay`/typed `Unavailable` with provenance. The engine is Android-free, stateless, thread-safe, CPU-only, and owns no clock, dispatcher, cache, cancellation, persistence, or location metadata.

```kotlin
fun interface SolarEngine {
    fun calculate(request: SolarDayRequest): SolarDayOutcome
}

data class SolarDayRequest(
    val civilDate: LocalDate,
    val coordinates: GeoCoordinates,
    val zone: ZoneId,
    val profile: ApprovedSolarProfile,
)

sealed interface SolarDayOutcome {
    data class RegularDay(
        val events: SolarEvents,
        val provenance: SolarProvenance,
        val warnings: Set<SolarWarning>,
    ) : SolarDayOutcome

    data class Unavailable(
        val reason: SolarUnavailableReason,
        val partialProvenance: SolarProvenance,
    ) : SolarDayOutcome
}
```

This is an architecture signature, not production code or authorization to add a stub.

The context coordinator requests date-keyed outcomes off-main, checks generation/cancellation between dates, and prevents superseded publication. A calculation already running need not be interruptible. Target at most three date calculations for a normal CP-001 context and retain the approved complete-result limit under one second; a proposed solar-only engineering budget is 50 ms p95 for three dates, pending physical-device evidence.

Cache at the coordinator/repository boundary, not inside the engine: bounded in-memory target 32 entries, keyed by canonical coordinate bits, zone plus resolved civil-day start/end instants, date, solar profile/rule and engine versions. Compute outside short synchronized get/put operations; duplicate parallel computation is safe. Do not persist solar events/schedules or expose the sensitive fingerprint.

`DailyCalculationContext` retains typed outcomes for the necessary dates so Seasonal Hora and future approved Rahu consume the same exact objects/fingerprint. Replacement implements the same boundary, changes engine/profile version, invalidates the cache namespace, regenerates independent vectors, and obtains approval; UI, ViewModels, Hora/Rahu rules, location provider, and persistence do not change.

### Precision contract

Keep six distinct concepts:

1. **Representation:** anchors and Hora boundaries are integer nanoseconds on the UTC timeline.
2. **Model accuracy:** astronomical output is not physically accurate to a nanosecond; the approved same-convention acceptance tolerance is ±60 seconds.
3. **Engine quantization:** PA-004-approved solar anchors are rounded once, half-even, to the nearest millisecond before conversion to integer nanoseconds.
4. **Hora division:** CR-004 computes all boundaries from common anchors with exact endpoint/adjacency guarantees and less than one-nanosecond rational quantization error.
5. **Validation:** compare the production engine with an independent same-convention implementation and retain every discrepancy.
6. **Display:** PA-004-approved `SOLAR-DISPLAY-001-v1.0`, outside calculators, uses resolved local-wall half-even nearest minute on Dashboard/shared Hora boundaries and nearest second in Method, including date carry and historical sub-minute offsets. A shared boundary is formatted once. Membership, transitions, and countdown always use unrounded internal values.

Before sunrise, the active Hora cycle begins at the previous local sunrise. Calculation may therefore require previous/current/next civil-date anchors. A typed missing-event, unsupported-date, invalid-input, or chronology failure must never produce a fabricated Hora.

### CR-001 multi-system boundary amendment

Model `PlanetaryHora`, `SriLankanKalaHora`, `PanchamaKala`, and `RahuKala` as distinct domain concepts unless an approved profile later proves equivalence.

```text
SolarEventsProvider
  → DailyCalculationContext
  → PlanetaryHoraCalculator
  → SriLankanKalaHoraCalculator
  → PanchamaKalaCalculator
  → RahuKalaCalculator
  → DailyTimingBundle
  → TimingBundleSelector(now)
  → CurrentTimingProjection
  → immutable ViewModel StateFlow
  → rendering-only UI
```

`DailyCalculationContext` contains immutable date-keyed `SolarEvents`, exact selected location/provenance, explicit zone, civil date, sampled clock instant, solar profile/engine version, and a canonical context fingerprint. “Same solar snapshot” means the same immutable location/zone/profile fingerprint, not necessarily the same civil-date anchor set: before sunrise CP-001 may consume the prior day's sunrise while daytime Rahu consumes the current civil date's sunrise/sunset. Each calculator accepts only a typed approved profile. A provisional constant in documentation cannot create an executable production profile.

`CalculationOutcome<T>` is either `Success(result, provenance, warnings)` or `Unavailable(reason, partialProvenance)`. Typed unavailable reasons include unapproved profile, unresolved rule questions, invalid input, missing solar event, parent/profile mismatch, coverage mismatch, chronology failure, arithmetic overflow, and unavailable storage/context. Evidence states remain distinct: user-supplied, provisional, rule-approved, independently validated, and traditionally validated.

Reuse only neutral mechanics: shared solar events, location/zone/date/clock inputs, integer-timeline intervals, `[start,end)` membership, exact common-anchor partitioning, continuity checks, profile/provenance structures, and a centralized display policy. Keep separate builders:

- `partitionExact(start,end,parts)` for seasonal partitions and Rahu eighths;
- `fixedDurationSeries(start,duration,coveragePolicy)` for a later approved fixed system.

The fixed builder has no default coverage policy. The UI never divides durations, selects rulers/weekday segments, determines interval membership, or rounds source boundaries.

Cache `SolarEvents` by solar profile/engine, coordinates, zone, and civil date. Cache each derived schedule by that solar-context fingerprint plus system/profile/rule version. Assemble prior/current/following date events once and publish one immutable `DailyTimingBundle` atomically; do not persist derived schedules. A profile change invalidates only the affected derived schedule unless the solar profile changes.

Every result retains calculation-system/profile/rule/engine versions; civil date and calculation instant; IANA zone; exact coordinates and location provenance; consumed sunrise/sunset/next-sunrise; solar engine/profile; source and validation state; internal timeline quantum; stated model accuracy; validation tolerance; display policy; context fingerprint; and typed warnings. This preserves four separate meanings: internal representation, astronomical model accuracy, validation tolerance, and displayed precision.

### Fixed 60-minute implementation gate

For the candidate interpretation of each fixed period as 3,600 elapsed seconds—or on an offset-stable date—let `S0` be sunrise, `S1` following sunrise, and `F = S0 + 24 elapsed hours`. Generally `F != S1`. The alternative meaning of wall-clock civil minutes across an offset transition remains separately unresolved.

- `S1 > F`: exactly 24 fixed periods leave a gap.
- `S1 < F`: period 24 overlaps the new sunrise.
- Extending/truncating creates a partial period.
- Dividing `S0..S1` into 24 removes the fixed-60-minute rule.
- Continuing across `S1` can prevent the new weekday ruler from beginning at sunrise.
- Resetting at `S1` can split a fixed interval.
- Distributing correction makes periods non-fixed.
- `60 civil minutes` may differ from 3,600 elapsed seconds on offset-change dates.

Five fixed 12-minute Panchamas inherit the conflict if their parent is not exactly 60 approved minutes. CP-002 must approve coverage start/end, period count, reset precedence, partial-period/gap/overlap policy, duration semantics, main/sub cycles, parent relationship, and day/night behavior. Until then, Kala/Panchama returns `Unavailable(ProfileNotApproved | RuleUnresolved)`.

Under approved Option C, CP-001 remains the V1.0 primary calculator and CP-003 daytime Rahu is added only after its own rule approval. CP-002 calculators target V1.1. This amendment adds no module or dependency.

### Options considered

| Option | Accuracy/range | Offline/determinism/testability | License/maintenance/APK/complexity | Extensibility and disposition |
|---|---|---|---|---|
| Local pure-Kotlin NOAA/Meeus style | NOAA states about one minute within ±72°; propose 1900–2100 | Fully local, deterministic, exact CP-001 convention, excellent JVM testing | No runtime dependency/APK cost; project owns moderate maintenance; calculator itself is no longer maintained | Solar-only but best proportional V1 choice; **recommended** |
| NREL SPA or an independent implementation | NREL states ±0.0003° from years −2000 to 6000 | Offline/deterministic but much larger verification surface | Supplied ANSI C and custom notice terms; a Kotlin port/reimplementation is materially complex | Solar-only and accuracy exceeds V1 need; use as independent reference |
| Solarpositioning Java library 2.0.13 candidate | SPA/Grena implementation with 1,000+ claimed upstream test points; SPA has broad date range | Offline, thread-safe, and fast, but its documented sunrise correction is `0.833°`, not the exact CP-001 `0.8333°` | MIT, no runtime deps; latest reported tag 2.0.13 requires artifact resolution plus Java 17 and Android/API 26 proof | Solar-only, no Panchanga advantage; closest maintained library candidate but reject for exact-convention/build simplicity |
| Astronomy Engine Kotlin/JVM | Project claims ±1 arcminute and broad dates; tested against NOVAS/JPL | Offline and well tested, but its normal rise/set convention is not the fixed CP-001 centre altitude | MIT, no runtime deps; adds repository/supply-chain and broad code surface | Strong future Moon/planet path, but exact CP-001 needs a custom search/adapter; reconsider for later Panchanga, reject for V1 |

## Foreground device location

Use the existing AndroidX Core `LocationManagerCompat.getCurrentLocation()` behind:

```text
DeviceLocationProvider.requestCurrentLocation(request): DeviceLocationResult
DeviceLocationProvider.cancelActiveRequest()
```

The adapter owns Android provider selection and exceptions; domain/UI never imports `LocationManager`, `Location`, or `CancellationSignal`.

- Jointly request coarse/fine only in an explicit foreground context when precise access is sought. Approximate-only remains functional.
- Classify permission precision from granted permissions, never from metre accuracy.
- Precheck `LocationManagerCompat.isLocationEnabled()`, while still handling races, missing providers, `SecurityException`, `IllegalArgumentException`, null, and provider shutdown.
- API 31+: prefer an enabled public fused provider when available; otherwise choose enabled GPS for fine or network for coarse.
- API 26–30: use Criteria-based best-provider lookup within the adapter. Never use the passive provider.
- Wrap the single callback with a 20-second coroutine timeout and `CancellationSignal`. Exactly one completion wins; late callbacks are ignored.
- Explicit Cancel, a superseding request, ViewModel clear, or genuine app backgrounding cancels the request. A configuration recreation with the same retained ViewModel does not cancel; the new UI resumes observing progress without an Activity reference in the provider.
- Lifecycle cancellation is silent and retains the prior snapshot; it must not create a persistent retrieval-failure warning. On foreground return the user may retry, or the first-use/no-usable-location flow may present its approved retry action. It does not silently restart permission/location acquisition.
- Callback bridging is non-blocking; validation, persistence, and calculation run off-main.
- Do not start continuous, simultaneous, or sequential indefinite update flows. Provider failure moves immediately to saved/manual/default recovery.

Google Play Services offers richer request controls but is disproportionate for the fallback-first family scope, adds Play-services availability and privacy review, and is not recommended for V1.

## Location acceptance policy

Usability, freshness, replacement, warning, and fallback are separate decisions:

| Concept | Approved architecture rule |
|---|---|
| Usable device result | Finite in-range coordinates; valid source/zone/timestamps; horizontal accuracy finite and `0..20,000 m` inclusive. Approximate permission remains usable within this ceiling. |
| Fresh returned fix | Age `0..2 minutes` inclusive, measured with monotonic elapsed realtime where available. Freshness is not precision. |
| Saved device staleness | Stale at age `>=24 hours`; stale remains usable and clearly labelled. Invalid/negative/future timestamps are stale/invalid as defined by source. |
| Adopt new fix | No valid saved device fix, old fix is stale, centre distance `>=10 km`, or accuracy improves by both `>=50 m` and `>=25%`. |
| Warning | Approximate is always labelled. Accuracy over `10 km` through `20 km` also receives a low-accuracy warning and visible manual alternative. Stale/default/failed refresh names the active fallback. |
| Recommend manual fallback | No usable device fix; timeout/null/provider failure with no usable saved fix; saved fix is stale and refresh fails; or uncertainty exceeds `10 km`. Never coerce when a usable approximate result exists. |

The 20 km ceiling is a policy guard, not a confidence guarantee; Android horizontal accuracy is a 68-percent confidence radius. Above the ceiling, treat the device fix as unavailable and use fallback. An explicit manual-town selection remains active until the user chooses another town or `Use current location`. A non-material refresh retains the existing selected coordinates and acquisition time; it may report a transient `Location unchanged` outcome but must not create history.

Approved privacy behavior on mode/permission change: switching to manual or default deletes any saved device fix; a later `Use current location` action requests a fresh fix and failure retains the active manual/default result. Likewise, if fine becomes coarse-only, or foreground permission is externally revoked, delete the saved fine-derived device record rather than continue presenting coordinates more precise than the current choice. Acquire a fresh approximate fix only in an approved foreground context; meanwhile use manual/default fallback.

## Sri Lankan town catalogue

Propose a bundled, reviewed subset of the daily GeoNames `LK.zip` country extract (CC BY 4.0, WGS84). Runtime uses no web service, geocoder, or network. Freeze the source URL, download date, SHA-256, extraction rule, reviewer, and dataset version; show attribution in Method/About.

Proposed V1 towns—one representative per province—are Colombo, Kandy, Galle, Jaffna, Trincomalee, Kurunegala, Anuradhapura, Badulla, and Ratnapura. Add the family's actual town before catalogue approval if absent. This list does not approve or guess coordinates; values remain provisional until imported and reviewed from the frozen source.

```text
TownRecord(
  stableId = "geonames:<geonameId>",
  geonamesId,
  englishNameResourceKey,
  futureSinhalaNameResourceKey?,
  latitudeE6,
  longitudeE6,
  zoneId = "Asia/Colombo",
  sourceDatasetVersion
)
```

Validation checks unique IDs, coordinate ranges, Sri Lanka containment, exact zone, source-record equality, attribution, snapshot hash, resource existence, and independent human/map review of every selected point. Do not publish unreviewed alternate names as Sinhala UI text.

## Persistence, backup, and reset

Prefer Preferences DataStore 1.2.1. SharedPreferences is technically adequate, but DataStore is asynchronous, transactional, Flow-native, supports migration/corruption handling, and fits UDF without the weight of Proto DataStore or Room.

Persist one discriminated active-selection record only:

```text
schemaVersion
activeMode = DEVICE | MANUAL_TOWN | DEFAULT
DEVICE { latitude, longitude, accuracyMetres,
         permissionPrecision, acquiredAtEpochMillis,
         source }
MANUAL_TOWN { activeTownId, selectedAtEpochMillis }
DEFAULT { datasetVersion }
```

Modes are mutually exclusive. Switching to manual/default deletes device coordinates; switching to device deletes the manual/default payload. Device mode derives the current system zone on every restore/resume rather than persisting it as authoritative. Bundled town coordinates resolve from the stable town ID and use `Asia/Colombo`; default mode stores provenance only, not coordinates. Do not persist derived schedules.

Updates are atomic. Unknown newer schema, corruption, invalid coordinates/zone/source, or impossible timestamp resets only location preferences to the Colombo default and emits one recovery warning. Migrations are pure, versioned, tested, and never widen retained data silently. `Reset location data` deletes the record and returns to the labelled Colombo default.

Create one application-scoped DataStore under credential-protected `noBackupFilesDir/location-data/`. Retain explicit exclusions for the complete directory/domain—not only the nominal `.preferences_pb` file—in legacy and Android 12+ cloud/device-transfer rules as defence in depth. Verify real installed paths and clean-device restore/transfer behavior; disable application backup if reliable exclusion cannot be proven. Never log location values or use real family coordinates in committed fixtures.

## Time, zone, lifecycle, and failures

Current-device mode uses the current system `ZoneId`; manual/default Sri Lankan modes use `Asia/Colombo`. Reconcile selected mode, location validity, permission precision, clock, civil date, and zone on foreground resume. While visible, observe relevant date/time/time-zone changes and reschedule the next boundary. Register observers only while needed and always unregister.

Typed failures include invalid input, unsupported date, missing solar event, chronology/invariant failure, permission unavailable, services disabled, provider unavailable, timeout, cancellation, and storage corruption. Platform/library exceptions are translated and never shown raw. A typed unavailable state contains safe user meaning, recovery options, and diagnostic profile/engine IDs without coordinates.

## Dependencies and proportionality

AndroidX artifacts approved in principle under PA-002, subject to an authorized implementation task and completed register evidence:

- `androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.2`
- `androidx.lifecycle:lifecycle-viewmodel-compose:2.9.2`
- `androidx.lifecycle:lifecycle-runtime-compose:2.9.2`
- `androidx.datastore:datastore-preferences:1.2.1`

No solar library, Google Play Services Location, Navigation Compose, DI, database, network, analytics, crash upload, or logging dependency is required. Reuse the existing Lifecycle 2.9.2 family; do not opportunistically upgrade it during implementation.

Direct coroutine APIs must not rely silently on transitive artifacts. Before implementation, either register and approve exact production/test coroutine dependencies with resolved versions and evidence, or constrain code to APIs supplied by the approved direct AndroidX dependencies. This is an implementation prerequisite, not permission to add a dependency now.

Change Request CR-001 adds no Gradle artifact, module, database, DI framework, navigation library, astronomy library, or networking path. Only enabled Version 1.0 schedules—CP-001 and, after PA-003 approval, CP-003—execute sequentially after shared solar calculation; their cost does not justify new concurrency infrastructure. CP-002 Kala/Panchama is not constructed or invoked before its Version 1.1 domain and delivery gates.

## Testing, performance, signing, and extension points

- Pure JVM: solar golden cases, exact Hora invariants, all threshold boundaries, selection/fallback, town-catalogue validation, migrations/corruption, clock/zone, and atomic state transitions.
- Independent astronomy expectations come from NREL SPA or another same-convention implementation, never the production engine itself.
- Instrument/API/device: API 26, 30, 31/32, and 36 permission/provider/lifecycle; at least the oldest actual family/OEM device.
- Test exactly at/beside 2 minutes, 24 hours, 10 km, 50 m, 25%, 10 km warning, and 20 km usability thresholds; test manual/default switching, downgrade deletion, and late callbacks.
- Distinguish explicit Cancel, background `ON_STOP`, configuration recreation, ViewModel clearing, and resume; configuration change preserves progress while lifecycle cancellation stays silent and never leaves a false failure warning.
- Test nearest-minute labels immediately before/at/after exact unrounded Hora boundaries.
- Test system separation, shared-solar call counts, distinct provenance/fingerprints, approved-profile gating, all Rahu weekday/boundary cases, and—only after CP-002 approval—the complete main/Panchama matrix and fixed-day coverage policy.
- Measure cold calculation and refresh on the slowest supported family device against NFR-006's one-second target.
- Verify backup exclusion with installed-file inspection plus backup/transfer evidence; secret/log/network/manifest audits remain release gates.

Signing keys/properties remain outside Git. Signing ownership, recovery, rotation, versioning, APK fingerprint verification, family installation/update process, final application ID, and current Android developer-verification implications remain release planning items.

Future Panchanga capability extends through new approved engines/profile IDs and domain models. Astronomy Engine may be reconsidered then, but V1 abstractions must not claim that an unselected future engine can satisfy lunar/traditional rules.

## Sources

- [NOAA solar calculation details](https://gml.noaa.gov/grad/solcalc/calcdetails.html)
- [NREL Solar Position Algorithm](https://midcdmz.nrel.gov/spa/)
- [NREL SPA report](https://docs.nrel.gov/docs/fy08osti/34302.pdf)
- [Astronomy Engine](https://github.com/cosinekitty/astronomy)
- [Solarpositioning](https://github.com/KlausBrunner/solarpositioning)
- [AndroidX LocationManagerCompat](https://developer.android.com/reference/androidx/core/location/LocationManagerCompat)
- [Android location permissions](https://developer.android.com/develop/sensors-and-location/location/permissions)
- [Android Location reference](https://developer.android.com/reference/android/location/Location)
- [GeoNames export terms](https://www.geonames.org/export/)
- [GeoNames download/readme](https://download.geonames.org/export/dump/)
- [GeoNames provenance and WGS84](https://www.geonames.org/about.html)
- [AndroidX DataStore releases](https://developer.android.com/jetpack/androidx/releases/datastore)
- [Android Auto Backup](https://developer.android.com/identity/data/autobackup)

## Remaining gated items

- Change Request CR-001, Option C, PA-002 architecture, and DA-001 Direction A are approved.
- CP-002 fixed-day coverage, day/night behavior, Panchama matrix, duration semantics, and reviewed terminology are implementation-blocking.
- CP-003 daytime Rahu remains a separate domain gate; Version 1.0 returns typed `Unavailable(ProfileNotApproved)` until it passes. Nighttime Rahu remains deferred.

Later implementation evidence for the PA-004-approved `SOLAR-001`; exact product GeoNames town rows/family town/attribution; authorized dependency addition with resolved evidence; actual family-device inventory; integrated feature-screen and manual DA-004 accessibility/adaptive/visual evidence; signing/distribution ownership and update-lineage proof; and external traditional authority remain unresolved until their stated gates. Permanent application identity is complete under V1-M1-02. M2 completed the Celestial Archive design-system production foundation and its automated Compose/API-36 evidence, but neither that evidence nor the approved UI specification and frozen SOLAR-GOLDEN-001 dataset constitutes completed feature or release evidence.
