# Requirements

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.1 |
| Last updated | 2026-08-06 |
| Owner role | Business Analyst |
| Approval state | FR-001–FR-011 and NFR-001–NFR-014 approved under PA-001; FR-012–FR-015 and NFR-015 approved under Change Request CR-001 |

PA-001 approves FR-001–FR-011 and NFR-001–NFR-014 below. Change Request CR-001, approved 2026-08-02, adds FR-012–FR-015 and NFR-015 plus the stated conditional deltas. “Must” indicates version priority, not implementation status; calculation profiles/rules, architecture, design, implementation, validation evidence, and release retain separate gates. In approved FR-001/FR-002, `Hora` means **Seasonal Planetary Hora under CP-001**.

## Functional requirements

### FR-001 — Current Hora summary

- Version: 1.0
- Priority: Must
- User value: An older family user can understand the current planetary Hora immediately.
- Description: Show current Hora ruler, start, end, remaining duration, and next ruler as one coherent result.
- Inputs: Current instant, selected coordinates, active time zone, CP-001 solar anchors.
- Behaviour: Apply CR-001–CR-010; update the countdown while visible; switch the complete display atomically at exact boundaries.
- Output: Ruler name, local start/end times, remaining duration, and next ruler.
- Acceptance criteria: All fields come from one versioned 24-interval result; exact end belongs to the next interval; no mixed old/new values appear during refresh.
- Edge cases: Exact sunrise/sunset/internal boundary, pre-sunrise civil time, midnight, resume, clock/date/zone change, unavailable location.
- Offline behaviour: Fully functional from usable saved, manual, or default location without network access.
- Accessibility implications: Coherent semantic group; countdown must not cause continuous TalkBack announcements; no colour-only state.
- Localization implications: All text externalized; locale-aware time/numeral formatting; English V1.0 and Sinhala-ready resources.
- Calculation-rule references: CP-001; CR-001–CR-010.
- Validation method: T-FR-001; VC-001, VC-003, VC-005, VC-007, and VC-015; unit plus UI/state tests.
- Approval state: Approved under PA-001.

### FR-002 — Continuous 24-Hora timeline

- Version: 1.0
- Priority: Must
- User value: Users can inspect the full sunrise-to-following-sunrise schedule.
- Description: Present exactly 24 ordered intervals: 12 day and 12 night.
- Inputs: Sunrise, sunset, following sunrise, and weekday ruler.
- Behaviour: Generate CR-004 intervals, identify current/next entries, and preserve exact shared endpoints.
- Output: Accessible chronological list/timeline with ruler, start/end, day/night grouping, and current marker.
- Acceptance criteria: First start equals sunrise; day interval 12 ends at sunset; night interval 1 starts at sunset; final end equals next sunrise; adjacent endpoints are identical with no gap/overlap.
- Edge cases: Unequal day/night duration, fractional division, final sunrise, landscape, tablet, and 200% font.
- Offline behaviour: Fully offline.
- Accessibility implications: Chronological TalkBack order, day/night headings, textual current marker, large-text support.
- Localization implications: Externalized ruler/group labels and locale-aware times.
- Calculation-rule references: CP-001; CR-002–CR-007.
- Validation method: T-FR-002; VC-001, VC-002, VC-004, VC-005, VC-007, and VC-015; invariant/property and UI tests.
- Approval state: Approved under PA-001.

### FR-003 — Calculated astronomical sunrise and sunset

- Version: 1.0
- Priority: Must
- User value: Users can see and understand the anchors used for Hora calculation.
- Description: Calculate and display sunrise, sunset, and following sunrise consistently with one approved, versioned, offline solar algorithm.
- Inputs: Latitude, longitude, relevant civil dates, active time zone, and CP-001 assumptions.
- Behaviour: Use apparent upper solar limb, standard refraction, solar-centre zenith 90.8333°, sea-level and unobstructed-horizon assumptions; never mix providers/algorithms within a result.
- Output: Local calculated astronomical sunrise/sunset times or a clear unavailable/error state.
- Acceptance criteria: One engine version produces all anchors; profile and inputs are retained for diagnostics; UI calls them “calculated astronomical” times and discloses model limitations.
- Edge cases: Invalid/non-finite coordinates, engine date range, missing event, zone/date change, observed conditions differing from the model.
- Offline behaviour: Algorithm and required data are packaged locally.
- Accessibility implications: Descriptive labels and logical reading order.
- Localization implications: Locale-aware time display and externalized methodology wording.
- Calculation-rule references: CP-001; CR-001, CR-002, CR-008.
- Validation method: T-FR-003; VC-006, VC-008, VC-009, and VC-014; independent same-convention astronomical comparison. Proposed CR-001 supplementary coverage: VC-034.
- Approval state: Approved under PA-001; PA-002 selects the method family and PA-004 approves normative `SOLAR-001`, which remains the unchanged implementation prerequisite for an approved task.

### FR-004 — Foreground current-location acquisition

- Version: 1.0
- Priority: Must
- User value: Calculations can reflect the user's current place without manual coordinate entry.
- Description: Obtain one foreground current location behind `DeviceLocationProvider` after a clear user-facing rationale.
- Inputs: Explicit onboarding/use/refresh action, coarse/fine permission result, location-services/provider state, cancellation, and timeout.
- Behaviour: Declare/request only `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`; request both together when precise access is sought; perform one cancellable current-location request; handle null/error/timeout; never start indefinite updates, background permission, location foreground service, or main-thread blocking.
- Output: Precise or approximate coordinates with metre accuracy where supplied, source, and acquisition timestamp.
- Acceptance criteria: Trigger only in approved contexts; cancellation/timeout ends cleanly; late results are ignored; a valid fix causes one atomic recalculation; no continuous tracking occurs.
- Edge cases: Approximate-only, one-time permission, denial, in-app request unavailable, services disabled, provider absent, timeout, null, exception, process restart, lifecycle cancellation.
- Offline behaviour: Device retrieval may be unavailable, but saved/manual/default calculation remains fully usable offline.
- Accessibility implications: Plain-language rationale before the platform dialog; understandable state and recovery actions.
- Localization implications: Rationale and errors externalized.
- Calculation-rule references: CR-008–CR-010.
- Validation method: T-FR-004; VC-010–VC-012 and VC-019; fake-provider, permission, timeout, and instrumented tests.
- Approval state: Approved under PA-001; PA-002 selects AndroidX `LocationManagerCompat` without Play Services.

### FR-005 — Permission and location-state recovery

- Version: 1.0
- Priority: Must
- User value: The app remains useful and respectful when location is approximate or unavailable.
- Description: Handle precise, approximate, denied, in-app request unavailable, services disabled, timeout, unavailable, stale, and manual states.
- Inputs: Permission grants, request history/rationale signals, provider result, saved/manual/default records.
- Behaviour: Continue with approximate location and label it; offer optional precision improvement without repeated pressure; provide Settings/manual/fallback recovery; never treat metre accuracy alone as proof of fine permission.
- Output: Unambiguous location state and available recovery action.
- Acceptance criteria: Every state has understandable recovery; denial never blocks fallback calculations; Settings opens only by user action; a stored precise fix is not presented as current precise after permission downgrades.
- Edge cases: Permission changed externally, approximate downgrade causing process restart, reset/policy states, stale result after restart, provider disabled.
- Offline behaviour: All fallbacks and recovery information are local.
- Accessibility implications: State and action announced separately; no colour-only warning or coercive repeated dialog.
- Localization implications: State/recovery text externalized.
- Calculation-rule references: CR-009, CR-010.
- Validation method: T-FR-005; VC-010–VC-012 and VC-016; state-machine/instrumented tests. Proposed CR-001 supplementary coverage: VC-037.
- Approval state: Approved under PA-001.

### FR-006 — Location selection and fallback

- Version: 1.0
- Priority: Must
- User value: A reliable calculation remains available without a fresh device fix.
- Description: Support current device, saved device, explicit manual Sri Lankan town, and provisional Colombo default locations.
- Inputs: Fresh precise/approximate fix, saved device record, user-selected town, bundled default.
- Behaviour: An explicit manual selection remains active until the user chooses current location or another town. During a user-initiated current-location attempt, fallback order is fresh precise → fresh approximate → saved device → active/manual selection → Colombo default.
- Output: Coordinates, display name if available, active zone, provenance, precision/approximation, and fallback state.
- Acceptance criteria: Exactly one source is active; manual Sri Lankan/default locations use `Asia/Colombo`; no zone is guessed from longitude; default and fallback are visibly labeled.
- Edge cases: No prior data, invalid bundled data, missing display name, current request failure while manual selection is active.
- Offline behaviour: Town records and Colombo default are bundled locally.
- Accessibility implications: Town picker has logical TalkBack/keyboard operation and explicit selection state.
- Localization implications: Display names can be localized without changing canonical coordinates.
- Calculation-rule references: CR-008–CR-010.
- Validation method: T-FR-006; VC-006 and VC-011–VC-013; repository and UI tests. Proposed CR-001 supplementary coverage: VC-034 and VC-037.
- Approval state: Approved under PA-001; exact Colombo coordinates and town dataset/provenance are unresolved before implementation.

### FR-007 — Minimal persistence, staleness, and refresh

- Version: 1.0
- Priority: Must
- User value: Launch is immediate and does not repeatedly request location.
- Description: Save only the active location and explicitly saved manual locations, then refresh on approved events/material change.
- Inputs: Coordinates, optional name, metre accuracy, permission precision, source, acquisition timestamp, and active zone.
- Behaviour: Keep no raw device-location history; immediately restore a complete saved result; refresh on explicit use/refresh, onboarding grant, or absence of usable saved data; retain prior result on failure; adopt Architect thresholds unless later amended: 20-second timeout, returned-fix freshness ≤2 minutes, saved-device stale at 24 hours, movement ≥10 km, accuracy improvement at least 50 m and 25%.
- Output: Restored/fresh/stale state and one atomically replaced calculation snapshot.
- Acceptance criteria: At exactly 24 hours a saved fix is stale but usable; invalid/future timestamps are stale; 9,999 m does not satisfy movement while 10,000 m does; refresh failure retains prior data with warning; insignificant movement does not recalculate.
- Edge cases: Corrupt/old schema, less-accurate fix, late callback, process restart, permission downgrade, boundary thresholds.
- Offline behaviour: Persistence/restoration local; stale/manual/default fallbacks remain available.
- Accessibility implications: Stale/fallback warning announced once with descriptive refresh action.
- Localization implications: Relative age formatted for locale.
- Calculation-rule references: CR-009, CR-010.
- Validation method: T-FR-007; VC-012, VC-016, VC-017, VC-019; threshold boundary and persistence tests. Proposed CR-001 supplementary coverage: VC-037.
- Approval state: Approved under PA-001; maximum reported uncertainty and precision-downgrade retention are approved under PA-002.

### FR-008 — Time-zone, date, clock, and lifecycle handling

- Version: 1.0
- Priority: Must
- User value: Results remain correct when civil-time context changes.
- Description: Use device system zone for current-device calculations and `Asia/Colombo` for manual Sri Lankan/default calculations; recalculate on relevant changes.
- Inputs: Instant, system zone/date/time signals, location mode, and foreground resume.
- Behaviour: Determine the most recent sunrise; respond to date/time/time-zone changes and foreground resume; recalculate midnight/sunrise boundaries without requiring a location refresh; atomically publish complete results.
- Output: Hora result with active zone shown in methodology/location details.
- Acceptance criteria: Pre-sunrise time uses the cycle begun on the prior civil date; system changes cannot leave mixed results; manual Sri Lankan result remains `Asia/Colombo` after device-zone changes.
- Edge cases: Midnight, exact sunrise, manual clock change, zone change, restart, clock materially incorrect.
- Offline behaviour: Fully offline.
- Accessibility implications: Stale/clock/fallback conditions conveyed textually.
- Localization implications: Locale-aware display; stored instants and zone identifiers remain locale-neutral.
- Calculation-rule references: CR-002, CR-006, CR-008, CR-010.
- Validation method: T-FR-008; VC-003, VC-005, VC-008, VC-014, VC-017. Proposed CR-001 supplementary coverage: VC-035 and VC-037.
- Approval state: Approved under PA-001.

### FR-009 — Dashboard location and calculation provenance

- Version: 1.0
- Priority: Must
- User value: Users can judge which location and freshness produced the displayed Hora.
- Description: Show location name, current/approximate/saved/manual/default state, refresh action, latest calculation time, and fallback warning.
- Inputs: Active location metadata and result generation time.
- Behaviour: Keep coordinates/details in methodology/diagnostics; primary dashboard uses concise labels and never implies unsupported precision.
- Output: Labels such as `Current location · Approximate`, `Kandy · Manually selected`, or `Colombo · Default location`.
- Acceptance criteria: Provenance matches coordinates used; stale/default never appears current/precise; every state exposes a location action—`Refresh location` for current/saved states and `Use current location` for manual/default states; detailed coordinates are not exposed on the primary dashboard.
- Edge cases: Missing name, approximate fix, stale saved fix, failed refresh.
- Offline behaviour: Fully local.
- Accessibility implications: Location and provenance read together; refresh has a descriptive label.
- Localization implications: Labels and relative age externalized.
- Calculation-rule references: CR-009, CR-010.
- Validation method: T-FR-009; VC-010, VC-011, VC-013, and VC-016; UI semantics/state tests. Proposed CR-001 supplementary coverage: VC-037.
- Approval state: Approved under PA-001.

### FR-010 — Methodology, sources, privacy, and limitations

- Version: 1.0
- Priority: Must
- User value: Users can understand how results were produced and what they do not mean.
- Description: Explain seasonal division, ruler cycle, boundaries, solar convention, active location/zone, profile/engine versions, privacy, sources, and limitations.
- Inputs: CP-001 metadata and active calculation/location provenance.
- Behaviour: Disclose that visible sunrise/sunset may differ due to terrain, mountains, buildings, elevation, weather, horizon obstruction, and unusual refraction; state that location stays on-device and distinguish calculated astronomy from observation.
- Output: Offline accessible methodology/privacy screen.
- Acceptance criteria: No claim exceeds approved sources; profile/engine version and zone are visible; timing-only scope and excluded advice are explicit; exact coordinate details are not needlessly prominent.
- Edge cases: Default, approximate, stale, or unavailable states.
- Offline behaviour: Entire screen is packaged locally.
- Accessibility implications: Structured headings, plain language, logical TalkBack order, readable/selectable text.
- Localization implications: All prose externalized and translation-reviewable.
- Calculation-rule references: CP-001; CR-001–CR-010.
- Validation method: T-FR-010; source-to-content, privacy, and accessibility review; VC-018 release evidence. Proposed CR-001 supplementary coverage: VC-038.
- Approval state: Approved under PA-001.

### FR-011 — Light/dark presentation and neutral content

- Version: 1.0
- Priority: Must
- User value: Comfortable, professional viewing without unsupported advice.
- Description: Follow system light/dark appearance using semantic tokens and only approved neutral content.
- Inputs: System appearance and approved text resources.
- Behaviour: Preserve meaning across themes; omit favorable/unfavorable labels, predictions, remedies, medical/financial/legal guidance, and deterministic advice.
- Output: Equivalent readable dashboard, timeline, location flow, and methodology in light/dark modes.
- Acceptance criteria: Prohibited content is absent; feature screens contain no hard-coded brand values; meaning never depends on colour.
- Edge cases: Live theme change, high contrast, 200% font, reduced motion.
- Offline behaviour: Fully offline.
- Accessibility implications: Must meet NFR-005.
- Localization implications: English complete; resource structure ready for reviewed Sinhala translation.
- Calculation-rule references: Approved terminology only.
- Validation method: T-FR-011; UI/theme/content/accessibility audit.
- Approval state: Approved under PA-001.

## Approved Change Request CR-001 functional requirements

### FR-012 — Daytime Rahu Kalaya

- Version: 1.0
- Priority: Must after CP-003/RK approval
- User value: A family user can see today's Rahu Kalaya interval and whether it is active or upcoming without interpreting a table.
- Description: Calculate a separate daytime Rahu interval from the approved local astronomical sunrise and sunset; display its start, end, and explicit textual status.
- Inputs: Current instant; selected coordinates; active IANA zone; applicable local civil date/weekday; CP-003 solar anchors and approved RK rules.
- Behaviour: Partition `[sunrise,sunset)` into eight exact adjacent segments from a common anchor; select Sunday 8, Monday 2, Tuesday 7, Wednesday 5, Thursday 6, Friday 4, or Saturday 3; use `[start,end)` membership; return active, upcoming today, completed today, or unavailable without interpreting auspiciousness. Rahu is not inserted into the seven-ruler cycle.
- Output: Rahu Kalaya start/end, active/upcoming/completed/unavailable status, next relevant transition, and calculation-system/profile/provenance identifiers.
- Acceptance criteria: Exact endpoints are gap-free; exact start is active and exact end is not; the same immutable solar/location snapshot supplies the visible seasonal Hora and Rahu results; a failed/unapproved Rahu profile cannot corrupt or relabel another system.
- Edge cases: Before sunrise, each weekday, exact start/end, after sunset, non-6:00 sunrise, leap day, location/zone/date change, approximate/manual/default location, restart/resume, missing solar event.
- Offline behaviour: Fully offline after the approved solar engine/data are packaged.
- Accessibility implications: Rahu is a subordinate textual status with start/end; status never depends on colour and does not imply favorable/unfavorable advice.
- Localization implications: Reviewed `Rahu Kalaya`/Sinhala terminology and locale-aware time formatting; rule IDs remain locale neutral.
- Calculation-rule references: CP-003; RK-001–RK-005; CP-001 solar convention only where CP-003 explicitly inherits it.
- Validation method: T-FR-012; VC-020, VC-030–VC-039.
- Approval state: Approved under Change Request CR-001; implementation remains blocked on separate CP-003/RK and golden-evidence approval.

### FR-013 — Fixed Sri Lankan Kala Hora

- Version: 1.1
- Priority: Must after CP-002/KH approval
- User value: A family user can see the main Kala in the Sri Lankan fixed-clock tradition using reviewed familiar names.
- Description: Provide an actual-sunrise-anchored fixed main-Kala schedule only after CP-002 defines complete coverage and day/night behavior.
- Inputs: Current instant, location, zone, civil date, approved solar anchors, CP-002, and KH rules.
- Behaviour: Proposed behavior uses the sunrise weekday ruler first and the Ravi → Sikuru/Shukra → Budha → Sandu/Chandra → Senesuru/Shani → Brahaspathi/Guru → Angaharu/Kuja cycle. Sixty-minute duration, restart, final-period, and following-sunrise rules remain non-executable until approved.
- Output: Main ruler, exact start/end, next ruler, validation/provenance state, or typed unavailable result.
- Acceptance criteria: No schedule is produced from a provisional profile; every enabled schedule proves its approved count/coverage/gap/overlap invariants and is clearly distinct from CP-001.
- Edge cases: Sunrise not 6:00, sunset, following sunrise earlier/equal/later than sunrise + 24 hours, civil versus elapsed minutes, offset change, zone/location/date change.
- Offline/accessibility/localization: Fully offline; names wrap at 200% font; canonical Sinhala/English forms require language approval.
- Calculation-rule references: CP-002; KH-001–KH-005.
- Validation method: T-FR-013; VC-020, VC-021, VC-024–VC-028, VC-034–VC-039.
- Approval state: Approved under Change Request CR-001; implementation Blocked on CP-002 fixed-day coverage and traditional validation.

### FR-014 — Panchama Kala

- Version: 1.1
- Priority: Must after CP-002/PK approval
- User value: A family user can see the current and next five-part subdivision and its transition.
- Description: For each approved fixed main Kala, show five approved parent-linked subdivisions, current start/end, countdown, and next subdivision.
- Inputs: One validated CP-002 main-Kala schedule and approved PK rules/matrix.
- Behaviour: USR-DOM-001 proposes five consecutive 12-minute subdivisions; first sub-ruler equals the parent and later sub-rulers follow the weekday-ruler order. This behavior remains unavailable until the entire matrix and parent coverage are approved.
- Output: Parent ruler, sub-ruler, ordinal, exact start/end, current/next/countdown, and provenance.
- Acceptance criteria: Parent and subdivisions publish atomically; five approved subdivisions exactly cover an approved parent; exact boundary selects the next subdivision; no matrix cell is inferred at runtime.
- Edge cases: All 35 parent/sub-ruler cells, first/final instants, main boundary, sunset/next sunrise, restart during transition, long localized names.
- Offline/accessibility/localization: Fully offline; structured parent/child semantics; no truncated primary names at 200% font; Sinhala grammar and aliases require review.
- Calculation-rule references: CP-002; PK-001–PK-004; USR-DOM-001 as evidence, not an executable rule.
- Validation method: T-FR-014; VC-020, VC-022–VC-027, VC-029, VC-034–VC-039.
- Approval state: Approved under Change Request CR-001; implementation Blocked with FR-013.

### FR-015 — Combined Kala/Panchama result and timeline

- Version: 1.1
- Priority: Must after CP-002/KH/PK and design approval
- User value: Older users can understand parent Kala and current Panchama without four competing dashboard cards.
- Description: Present current main Kala and Panchama in one dominant structured card, next Panchama transition, and an expandable grouped timeline; keep Rahu and seasonal Hora visually/semantically separate.
- Inputs: One atomic multi-system bundle containing only approved per-system results.
- Behaviour: Group five Panchamas under each parent; initially expand the current parent; expose `Go to current`; preserve per-system unavailable/validation states; never interleave seasonal and fixed timelines.
- Output: Accessible current summary and chronological grouped timeline.
- Acceptance criteria: Logical TalkBack order, one boundary announcement, 200% reflow, no fixed-height truncation, no colour-only current/Rahu state, and no mixed profile/location/version data.
- Calculation-rule references: CP-002; KH-*; PK-*; NFR-015.
- Validation method: T-FR-015; VC-020, VC-024, VC-026, VC-029, VC-034, VC-035, and VC-037–VC-039 plus design accessibility evidence.
- Approval state: Approved under Change Request CR-001; implementation Blocked on CP-002 and later design approval.

## Approved amendments to PA-001 requirements

These deltas took effect when Change Request CR-001 was approved on 2026-08-02; the PA-001 wording above remains the historical approved baseline.

| Requirement | Proposed delta | Reason/state |
|---|---|---|
| FR-001 | User-facing ambiguous `Hora` becomes `Seasonal Planetary Hora`; FR-012 Rahu remains a subordinate separate result. | Prevents system confusion; Approved under CR-001. |
| FR-002 | Timeline is explicitly the CP-001 Seasonal Planetary Hora timeline and is never interleaved with CP-002. | Preserves approved invariants; Approved under CR-001. |
| FR-009 | Provenance additionally names each visible calculation system/profile/rule/evidence state; all results in one bundle share the active location fingerprint. | Prevents mixed-system claims; Approved under CR-001. |
| FR-010 | Method contains separate sections for Seasonal Planetary Hora, daytime Rahu, and any later approved Kala/Panchama system, including status, sources, assumptions, conflicts and limitations. | Required transparency; Approved under CR-001. |
| FR-011 | Neutral-content prohibition explicitly applies to Rahu: show timing/status without favorable/unfavorable meaning or activity advice. | Maintains PD-004; Approved under CR-001. |
| NFR-001, NFR-003, NFR-014 | Determinism/invariants/traceability are evaluated per system/profile; CP-001's exactly-24 invariant is not copied to CP-002 or CP-003. | Prevents hidden profile inheritance; Approved under CR-001. |

## Non-functional requirements

| ID | Version | Priority | Measurable completion criterion | State |
|---|---|---|---|---|
| NFR-001 | 1.0 | Must | Identical instant, coordinates, zone, CP-001 version, and engine version produce identical anchors and exact internal intervals in repeatable tests. | Approved |
| NFR-002 | 1.0 | Must | Solar anchors match an independent same-convention astronomical implementation within ±60 seconds across every approved Sri Lankan golden case; every discrepancy is retained/explained. Traditional-source comparison remains an additional release gate and may document source-specific differences without weakening this criterion. | Approved |
| NFR-003 | 1.0 | Must | Property/invariant tests prove exactly 24 intervals, 12 day + 12 night, exact adjacency, no gaps/overlaps, and complete `[sunrise,nextSunrise)` coverage. | Approved |
| NFR-004 | 1.0 | Must | In airplane mode/no network, saved/manual/default calculations, timeline, settings, and methodology remain functional; automated/manual tests observe no required network path. | Approved |
| NFR-005 | 1.0 | Must | All V1.0 flows work at 200% font with no lost content/action; touch targets are at least 48dp; normal text contrast ≥4.5:1 and large text/non-text ≥3:1; TalkBack order is logical; meaning never uses colour alone. | Approved |
| NFR-006 | 1.0 | Must | With usable coordinates, a complete result renders within 1 second on the slowest approved family device; refresh/calc never blocks the main thread; benchmark threshold is revalidated during implementation/device verification. | Approved |
| NFR-007 | 1.0 | Must | Manifest/runtime audit finds only coarse/fine foreground location; no background location/service, backend, analytics, ads, history, coordinate logs/uploads, or `INTERNET` permission without separate approval. | Approved |
| NFR-008 | 1.0 | Must | Persistence contains only approved selected-location fields; real/private family coordinates are absent from logs and committed fixtures, while approved public town/default and clearly synthetic coordinates are permitted for bundled data/tests; persisted user coordinates are excluded from Auto Backup and device-to-device transfer. | Approved |
| NFR-009 | 1.0 | Must | Provisional `minSdk 26` is verified by build/lint/unit tests and an API 26 device/emulator; modern `compileSdk 36` and `targetSdk 36` remain unless an approved ADR changes them; actual oldest/newest family devices are manually tested. | Approved; support provisional pending evidence |
| NFR-010 | 1.0 | Must | Automated state tests cover every approved permission/provider/fallback/lifecycle/zone/restart outcome with no crash or blank dashboard when a fallback exists. | Approved |
| NFR-011 | 1.0 | Must | No user-visible hard-coded text; English resources complete; pseudolocale/expansion checks pass; adding reviewed Sinhala resources requires no calculation change. | Approved |
| NFR-012 | 1.0 | Must | Solar engine, Hora engine, clock, location provider, zone source, and persistence are replaceable/testable boundaries; pure calculation tests require no Android runtime. | Approved |
| NFR-013 | 1.0 | Must | Signed family APK process, signing ownership, privacy review, physical-device checks, and external validation complete; Git scan confirms no key/credential. | Approved; release blocker |
| NFR-014 | 1.0 | Must | Every displayed result exposes/retains CP/CR/source and engine/profile version traceability; methodology review matches repository documents. | Approved |
| NFR-015 | 1.0/1.1 | Must | Each timing system has a distinct system ID, approved profile/rule version, typed outcome, source/validation state, and exact input fingerprint; all enabled systems share one immutable solar/location/time context, while calculation selection, interval membership and rounding remain outside UI code. | Approved under Change Request CR-001; implementation separately gated |

## Approval and dependency gates

PA-001 approves the original baseline identified above. Change Request CR-001 approves the amended product requirements and Option C allocation. PA-002 approves ADR-001–ADR-009 and DEP-011–DEP-014 in principle; DA-001 selects Direction A; PA-004 approves normative `SOLAR-001`; DA-004 approves the final Version 1.0 UI; DP-001 approves M1–M9 and 39 tasks. V1-M1-01 through V1-M1-05 were explicitly started, are Done and Lead/owner accepted, and are incorporated into `dev` through `79d5116`. DEP-011–DEP-013 and the permanent identity are implemented, while DEP-014, CP-003/RK, exact town data/default coordinates, direct coroutine decisions, later implementation, and all release verification retain their task-specific gates. M2 and later tasks have not started.

Before release: complete independent astronomical comparison, nominate and complete traditional Sri Lankan validation for every enabled timing system, test actual family devices, complete privacy/security/accessibility checks, and document signed-APK delivery. Expected results must never be altered merely to make tests pass. Fixed Kala/Panchama is implementation-blocked until the following-sunrise mismatch has an approved rule; nighttime Rahu is outside this amendment's recommended V1.0 scope.
