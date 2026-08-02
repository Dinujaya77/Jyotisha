# Engineering Decisions

| Field | Value |
|---|---|
| Status | Approved |
| Version | 0.7 |
| Last updated | 2026-08-02 |
| Owner role | Android Architect |
| Approval state | ADR-001–ADR-009 remain Approved under PA-002; SOLAR-001 refinement Approved under PA-004; implementation still requires an approved milestone/task |

## ADR-001 — Version 1.0 solar engine

- **State:** Approved method family under PA-002 and complete `SOLAR-001-v1.0` package under PA-004 on 2026-08-02; implementation requires a separately approved milestone/task.
- **Linked requirements:** FR-001–FR-003, FR-008, FR-010; NFR-001–NFR-003, NFR-006, NFR-012, NFR-014; CP-001; CR-001–CR-008.
- **Context:** V1 needs deterministic offline sunrise/sunset under the exact `90.8333°` convention. Internal nanoseconds, model accuracy, validation tolerance, and display precision must not be conflated.
- **Options:** Local NOAA/Meeus-style pure Kotlin; NREL SPA/port; maintained permissive astronomy library (Astronomy Engine evaluated).
- **Decision:** Select a small independently written pure-Kotlin NOAA/Meeus-style method family. SOLAR-001 now proposes exact engine `NOAA-MEEUS-001-v1.0`, date range `1900-01-01..2100-12-31`, centre altitude `−0.8333°`, `SEA_LEVEL_FIXED`, binary64/`StrictMath`, five event evaluations to a 0.0005-second convergence threshold, half-even millisecond anchor quantization, and typed unavailable states. Exact anchors then use checked UTC nanoseconds; display never changes membership.
- **Why:** NOAA states about one-minute theoretical sunrise/set accuracy within ±72°, including Sri Lanka. This is proportional to the approved ±60-second criterion, exactly controllable for CP-001, offline, small, and easy to unit test.
- **Rejected production alternatives:** NREL SPA is a high-quality independent reference but materially more complex than V1 needs; downloadable NREL C code also has restrictive no-redistribution terms. Solarpositioning's reported 2.0.13 tag is a maintained MIT SPA/Grena library with no runtime dependencies and extensive claimed tests, but it documents `0.833°`, requires Java 17, and lacks exact-profile/Android/API 26 evidence. Astronomy Engine remains a broader future candidate with a different normal rise/set convention.
- **Normative prerequisite:** Architecture approval alone did not authorize implementation. PA-004 now approves `SOLAR_001.md`, which freezes official NOAA source hashes, independently authored equations/control flow, calendar/sign/unit/time-scale behavior, event convergence/domain rules, half-even rounding, diagnostics, versions, and independent NREL-SPA vectors. A later approved milestone/task remains required.
- **Consequences:** The project owns algorithm specification and maintenance. Pinned pvlib 0.15.1 executes the published NREL SPA family outside Android to freeze validation expectations; it is not a production/test dependency in Gradle. NOAA's calculator is no longer actively maintained, so do not copy web implementation code or claim ongoing NOAA support.
- **Failure/limits:** Typed invalid-input, unsupported-date, missing-event, and chronology failures; no fabricated result. Observed terrain/weather/refraction differences remain disclosed.
- **API/execution:** One synchronous stateless `SolarEngine` pure boundary. Coordinator-owned off-main execution, cancellation generation, atomic context publication, and bounded in-memory date cache; no persistent schedule. Future engine replacement preserves UI/ViewModel/Hora/Rahu/location contracts and changes engine/cache/golden versions.
- **Dependencies:** None; SRC-022 host-side oracle tooling is temporary validation infrastructure outside the repository/Android build.
- **Rollback:** `SolarEngine` permits a later approved engine/profile version without changing the Hora/UI contracts.
- **Approval record:** PA-002 approved by exact phrase `APPROVE VERSION 1.0 ARCHITECTURE` on 2026-08-02.

## ADR-002 — Foreground current-location provider

- **State:** Approved under PA-002 on 2026-08-02.
- **Linked requirements:** FR-004–FR-009; NFR-004, NFR-007, NFR-009–NFR-012.
- **Context:** V1 needs one cancellable foreground fix from API 26, accepts approximate access, and must remain useful without Play Services/network.
- **Options:** Existing AndroidX `LocationManagerCompat.getCurrentLocation()`; Google Play Services `FusedLocationProviderClient.getCurrentLocation()`.
- **Decision:** Use existing AndroidX Core 1.17.0 behind `DeviceLocationProvider`; no Play Services Location dependency.
- **Provider policy:** API 31+ prefers an enabled public fused provider when available, else GPS for fine or network for coarse. API 26–30 uses Criteria-based best-provider selection within the adapter. Never use passive provider or continuous/simultaneous/sequential indefinite updates.
- **Request policy:** Joint coarse/fine request when precise is sought; permission grant determines precision label. Precheck service state; handle races/no provider/security/invalid-provider/null/shutdown. A 20-second coroutine timeout cancels `CancellationSignal`; exactly one completion wins and late callbacks are ignored.
- **Lifecycle:** Explicit Cancel, superseding request, ViewModel clear, or genuine backgrounding cancels. The Activity forwards an explicit lifecycle signal that distinguishes `isChangingConfigurations` from genuine `ON_STOP`; configuration recreation retains the ViewModel/request without a process-lifecycle dependency. Lifecycle cancellation is silent, retains the prior snapshot, and never leaves a false failure warning or silently restarts acquisition. No Activity reference in the ViewModel; calculation/persistence run off-main.
- **Consequences:** No new location dependency/APK/Play-services coupling. Provider/OEM behavior and possible provider-level network assistance require API/OEM device evidence and disclosure.
- **Testing:** Fake the interface for JVM state tests; instrument framework behavior on API 26/30/31+/36 and physical family devices.
- **Dependencies:** None beyond existing DEP-003.
- **Approval record:** Approved under PA-002 on 2026-08-02; provider/OEM evidence remains required before implementation acceptance.

## ADR-003 — Location usability, freshness, replacement, and privacy

- **State:** Approved under PA-002 on 2026-08-02.
- **Linked requirements:** FR-004–FR-009; NFR-007, NFR-008, NFR-010; VC-010–VC-013, VC-016, VC-017, VC-019.
- **Decision:** Treat concepts independently:
  - usable device result: finite valid coordinates/source/zone/timestamps and accuracy `0..20,000 m` inclusive;
  - fresh result: age `0..2 minutes` inclusive, using monotonic age where possible;
  - stale saved result: age `>=24 hours`, still usable and labelled;
  - adopt new fix when none is saved, old is stale, distance is `>=10 km`, or accuracy improves by both `>=50 m` and `>=25%`;
  - approximate access remains usable inside the ceiling and is always labelled;
  - accuracy `>10 km..20 km` receives a low-accuracy warning and visible manual alternative;
  - recommend manual fallback for no usable fix, acquisition failure without usable saved data, stale+failed refresh, or uncertainty over 10 km.
- **Manual selection:** Remains active until another town or explicit `Use current location` action.
- **Non-material refresh:** Retains coordinates and acquisition time; no false freshness/history.
- **Mode/downgrade/revocation:** Switching to manual/default deletes saved device coordinates. Fine→coarse or foreground revocation also deletes a fine-derived device record. Request a new current/approximate fix only in an approved foreground context; failure retains manual/default.
- **Why 20 km:** A deliberately broad ceiling keeps normal Android approximate fixes usable while bounding obviously poor results. It is a policy guard, not a physical-accuracy claim; Android accuracy is a 68-percent confidence radius.
- **Consequences:** Privacy choice may reduce saved-device fallback availability after downgrade. Exact boundary tests are mandatory.
- **Approval record:** Approved under PA-002 on 2026-08-02; exact boundary tests remain mandatory.

## ADR-004 — Bundled Sri Lankan town catalogue

- **State:** Approved under PA-002 on 2026-08-02 for source/schema policy; exact snapshot and records remain unapproved.
- **Linked requirements:** FR-006, FR-007, FR-009, FR-010; NFR-004, NFR-008, NFR-011, NFR-014.
- **Context:** Manual selection/default must work offline with reviewed coordinates, stable IDs, attribution, and future Sinhala names. Coordinates must never be guessed.
- **Options:** GeoNames country extract; OpenStreetMap-derived data; manually assembled coordinates; other government/maintained sources if evidence becomes available.
- **Decision:** Use a frozen, reviewed subset of GeoNames `LK.zip`, CC BY 4.0, WGS84. Bundle records locally; do not call its web service or a geocoder at runtime.
- **Initial towns:** Colombo, Kandy, Galle, Jaffna, Trincomalee, Kurunegala, Anuradhapura, Badulla, Ratnapura—one per province. Add the family's actual town before catalogue approval if absent. No coordinates are approved by this list.
- **Identity/schema:** `stableId = geonames:<geonameId>`; source ID; English resource key; nullable future Sinhala resource key; integer microdegree latitude/longitude; fixed `Asia/Colombo`; dataset version.
- **Evidence:** Source URL/date/hash/extraction rule/reviewer; source-record equality; Sri Lanka containment; unique IDs; coordinate/zone/resource checks; attribution; independent human/map review of every selected point.
- **Consequences:** Method/About must attribute GeoNames and its CC BY 4.0 snapshot. Alternate names are not automatically suitable Sinhala translations.
- **Rollback:** Dataset is separately versioned; corrections preserve stable IDs where the source entity is unchanged.
- **Approval record:** Source/schema policy approved under PA-002 on 2026-08-02; exact snapshot/records remain a separately reviewed prerequisite.

## ADR-005 — Location persistence, migration, reset, and backup

- **State:** Approved under PA-002 on 2026-08-02; implementation and restore/transfer evidence remain gated.
- **Linked requirements:** FR-006–FR-010; NFR-004, NFR-007, NFR-008, NFR-010, NFR-012.
- **Options:** Platform SharedPreferences; Preferences DataStore; Proto DataStore; Room.
- **Decision:** Use one application-scoped Preferences DataStore 1.2.1 for one versioned discriminated active-mode record. Device mode contains only current coordinates, accuracy, permission precision, acquisition instant, and source; its zone always comes from current `ZoneSource`. Manual mode contains only town ID/selection time and derives `Asia/Colombo`; default contains dataset provenance and derives `Asia/Colombo`. Modes are mutually exclusive. Do not persist derived schedules or history.
- **Why:** SharedPreferences could store the fields, but DataStore provides asynchronous transactional edits, Flow integration, migrations, and corruption handling proportionate to UDF. Proto and Room are excessive.
- **Manual/default minimization:** Manual mode persists a stable town ID and resolves bundled coordinates. Default stores no coordinates. Selecting manual/default deletes device coordinates; a later current-location attempt requests a fresh fix and failure retains the active manual/default result.
- **Recovery/migration:** Pure versioned migrations. Unknown newer schema, corruption, invalid values, or impossible timestamps reset only location preferences to labelled Colombo default and emit one recovery warning. Migrations never silently widen retention.
- **Reset:** User action deletes the full record and returns to default.
- **Backup:** Store the DataStore under credential-protected `noBackupFilesDir/location-data/`. Retain explicit exclusions of the complete directory/domain, including temporary/replacement/companion artifacts, from legacy Auto Backup and Android 12+ cloud/device transfer as defence in depth. Verify installed paths and clean-device restore/transfer behavior; disable app backup if exclusion cannot be proven.
- **Dependencies:** DEP-014 DataStore Preferences approved in principle under PA-002; not added.
- **Approval record:** Approved under PA-002 on 2026-08-02.

## ADR-006 — SDK support baseline

- **State:** Approved under PA-002 on 2026-08-02 as the provisional install-floor policy; device evidence remains required.
- **Linked requirements:** NFR-009.
- **Decision:** Retain `compileSdk 36` and `targetSdk 36`; lower only `minSdk` from 36 to 26 during an approved implementation task.
- **Rationale:** API 26 sets the desired provisional install floor and provides `java.time`; no current source or proposed V1 feature requires a newer minimum. Compile/target meanings are independent of install eligibility.
- **Evidence:** Build, lint, JVM/instrumented tests at API 26 plus physical oldest-family/OEM device; actual family-device inventory remains required.
- **Consequences:** API branches and resource behavior need API 26 coverage. Do not lower compile/target or upgrade dependencies opportunistically.
- **Approval record:** Architecture policy approved under PA-002 on 2026-08-02; device verification remains required before the final release support claim.

## ADR-007 — Application structure, state, and dependencies

- **State:** Approved under PA-002 on 2026-08-02.
- **Linked requirements:** FR-001–FR-015; NFR-001–NFR-015.
- **Decision:** One app module/activity; domain/data/platform/UI packages; pure domain; immutable ViewModel `StateFlow`; atomic complete snapshots; manual composition root. Use a small sealed destination state for Now/Day/Method unless approved UI evidence proves Navigation Compose necessary.
- **Lifecycle/time:** Injectable clock/zone; resume reconciliation; visible-only date/time/time-zone/provider observation; calculation snapshot separate from acquisition state; countdown does not rerun astronomy.
- **Failures:** Typed domain/platform/storage failures; no raw exception reaches UI.
- **Dependencies approved in principle:** Lifecycle ViewModel KTX, Lifecycle ViewModel Compose, Lifecycle Runtime Compose at the existing 2.9.2 family, plus Preferences DataStore 1.2.1. They are not authorized to be added until an implementation task satisfies the register prerequisites.
- **Rejected additions:** Hilt/DI, Navigation Compose, Room/database, solar library, Play Services Location, network, analytics, crash uploader, or logging framework.
- **Performance:** Cold calculation/usable-location render under one second on the slowest approved family device; calculations and storage off-main.
- **Security/release:** No Internet/background-location permissions; signing keys/properties outside Git; family APK ownership/update/fingerprint process remains release planning.
- **Extension:** Version 1.0 creates only approved/needed source boundaries. CP-002 `SriLankanKalaHora` and `PanchamaKala` remain conceptual until Version 1.1 domain approval; do not add stub packages/calculators. New timing/Panchanga capabilities require explicit system-specific calculators and approved profiles/engines/domain models. A generic `Hora`/`Panchanga` engine must not hide fixed-versus-seasonal behavior or invented lunar/traditional rules.
- **Approval record:** Approved under PA-002 on 2026-08-02.

## ADR-008 — Timing-system separation and shared calculation context

- **State:** Approved under PA-002 on 2026-08-02.
- **Linked requirements:** FR-012–FR-015; NFR-015; CP-001–CP-003.
- **Decision:** Keep `PlanetaryHoraCalculator`, `RahuKalaCalculator`, future `SriLankanKalaHoraCalculator`, and future `PanchamaKalaCalculator` as separate pure boundaries. Feed enabled calculators one immutable, date-keyed `DailyCalculationContext` containing shared solar events, location, zone, civil date, sampled clock instant, and fingerprint. Publish one atomic `DailyTimingBundle` and select current/next intervals outside UI code. An unapproved CP-003 yields typed `Unavailable(ProfileNotApproved)` without invalidating CP-001.
- **Rationale:** The systems share inputs but have different duration, ruler, coverage, validation, and display meanings. Separation prevents duplicated solar work, profile aliasing, UI calculations, inconsistent rounding, and Rahu becoming an eighth ruler.
- **Consequences:** Cache solar events once per solar context and derived schedules per system/profile. Preserve distinct typed outcomes so one unavailable system does not invalidate or mislabel another. No module or dependency is added.
- **Approval record:** Change Request CR-001 and PA-002 are approved; this does not approve CP-003/RK or authorize production code.

## ADR-009 — Approved-profile execution and fixed-duration coverage gate

- **State:** Approved under PA-002 on 2026-08-02; CP-002 subdecision remains Blocked and CP-003 remains separately gated.
- **Linked requirements:** FR-012–FR-015; NFR-014, NFR-015; KH-001–KH-005; PK-001–PK-004; RK-001–RK-005.
- **Decision:** A timing calculator executes only with a typed approved profile carrying system/rule/source/validation status. `fixedDurationSeries` has no default coverage policy. CP-002 returns a typed unavailable result until an approved authority resolves following-sunrise count, gap/overlap, reset/split/partial-period, duration semantics, and day/night behavior.
- **Rationale:** Under 3,600-elapsed-seconds semantics—or on an offset-stable date—twenty-four fixed 60-minute periods do not normally end at the next astronomical sunrise; wall-clock behavior across offset changes is an additional unresolved choice. Any convenient implementation default would silently invent a traditional rule and would also break five fixed 12-minute Panchamas.
- **Consequences:** Constants in USR-DOM-001 and provisional tables remain documentary evidence, not executable configuration. CP-003 daytime Rahu may proceed separately after its rules/golden vectors are approved. Internal timeline precision, solar-model accuracy, validation tolerance, and display precision remain separate provenance fields.
- **Approval record:** ADR-009 is approved under PA-002; this does not approve any calculation profile/rule. CP-002 still requires explicit domain/traditional approval, while CP-003/RK requires selected-tradition/golden-vector approval.

## Remaining decisions outside this gate

CP-002 coverage, CP-003 rule evidence, final package/application name, direct coroutine dependency decision, family-device inventory, product town-catalogue rows/family-town coverage, traditional authority, final UI specification, signing owner/distribution mechanics, SOLAR-001 implementation evidence, and release calculation accuracy remain separately gated. CR-001/Option C and PA-004 are approved but do not approve any new subordinate profile or implementation task.
