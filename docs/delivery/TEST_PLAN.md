# Test Plan

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.2 |
| Last updated | 2026-08-26 |
| Owner role | QA Reviewer |
| Approval state | PA-001/CR-001 strategy and PA-004 SOLAR-001 test obligations are approved; no implementation task is authorized by this document |

## Existing baseline gates

| Test ID | Purpose | Command/evidence | Expected |
|---|---|---|---|
| T-BASE-001 | Gradle configuration | `gradlew projects` | `:app` configures |
| T-BASE-002 | Local unit tests | `gradlew testDebugUnitTest` | All local tests pass |
| T-BASE-003 | Android lint | `gradlew lintDebug` | No failing lint issue |
| T-BASE-004 | Debug packaging | `gradlew assembleDebug` | Debug APK assembles |
| T-SETUP-001 | Team configuration | Parse project/agent TOML | Valid TOML and permissions |
| T-SETUP-002 | Documentation contract | File/metadata inventory | Required files/metadata exist |
| T-SETUP-003 | Behavior preservation | Git diff path review | No production Android change in setup |

## Planned V1.0 requirement tests

| Test ID | Requirement | Planned verification |
|---|---|---|
| T-FR-001 | FR-001 | Pure current/next/countdown boundary tests plus Compose state/semantics tests. |
| T-FR-002 | FR-002 | 24-interval property/invariant tests and adaptive/200%-font timeline tests. |
| T-FR-003 | FR-003 | Solar golden cases, invalid/unavailable cases, engine-version evidence, independent same-convention comparison. |
| T-FR-004 | FR-004 | Fake-provider and instrumented one-shot, approximate/precise, null/error, immediately-before/at/after 20-second timeout, cancellation, late-result tests. |
| T-FR-005 | FR-005 | Permission/recovery state-machine tests including denial, rationale, in-app unavailable, downgrade, and services disabled. |
| T-FR-006 | FR-006 | Fallback order, explicit manual-selection persistence, town/default provenance and `Asia/Colombo` tests. |
| T-FR-007 | FR-007 | Persistence/restart, 2-minute and 24-hour boundaries, 10 km threshold, each 50 m/25% accuracy condition separately and together, invalid/future timestamps, corrupt schema, no-history and backup-exclusion tests. |
| T-FR-008 | FR-008 | Pre-sunrise/midnight, clock/date/zone broadcast, resume/restart, and atomic replacement tests. |
| T-FR-009 | FR-009 | Location/provenance state rendering, stale/default warnings, refresh actions, and TalkBack semantics tests. |
| T-FR-010 | FR-010 | Methodology/source/profile/privacy content audit, offline access, limitations, and structured accessibility tests. |
| T-FR-011 | FR-011 | Light/dark equivalence, semantic-token audit, contrast, 200% font, reduced motion, and prohibited-content audit. |

## Approved Change Request CR-001 test obligations

| Test ID | Requirement | Planned verification | State |
|---|---|---|---|
| T-FR-012 | FR-012 | Pure eight-part Rahu partition/property tests; all weekday allocations; exact start/end; date/zone/location/fallback/leap/restart; status and accessibility rendering; shared solar/provenance audit. | Approved obligation; implementation blocked on CP-003/RK |
| T-FR-013 | FR-013 | Approved-profile gating; all seven main rulers; actual-sunrise/non-6:00 cases; sunset/next-sunrise; 23:59/24:00/24:01 spans; civil-versus-elapsed duration; no schedule while KH-005 is unresolved. | Approved obligation; CP-002 Blocked |
| T-FR-014 | FR-014 | All 35 matrix cells; first/final/exact-boundary instants; parent coverage; main-boundary continuity; restart/transition atomicity. | Approved obligation; CP-002 Blocked |
| T-FR-015 | FR-015 | Combined-card and grouped-timeline semantics, system separation, longest approved English/Sinhala labels, compact/200% font, TalkBack, reduced motion, per-system unavailable states. | Approved obligation; CP-002 and final UI remain blocked |
| T-NFR-SYSTEMS | NFR-015 | Distinct typed calculators/profiles/outcomes, one shared solar context, cache-call counts, fingerprints, rounding ownership, UI-calculation prohibition, and atomic multi-system bundle. | Approved obligation, architecture, UI and delivery mapping; CP-003/profile/task/implementation gates remain |

## Non-functional test suites

| Test ID | Covers | Completion evidence |
|---|---|---|
| T-NFR-CALC | NFR-001–NFR-003, NFR-014 | Determinism, exact interval invariants, independent astronomy/traditional datasets, profile/engine provenance. |
| T-NFR-OFFLINE | NFR-004, NFR-007 | Airplane-mode workflows, manifest/network observation, no upload/analytics/background location. |
| T-NFR-A11Y | NFR-005, NFR-011 | TalkBack, 200% font, 48dp targets, contrast, pseudolocale/expansion, non-colour meaning. |
| T-NFR-PERF | NFR-006 | Slowest approved family-device measurement; complete result within the approved 1-second threshold; no main-thread blocking. |
| T-NFR-PRIVACY | NFR-007, NFR-008, NFR-013 | Storage/log/manifest/backup/transfer/secret audit and signed-APK review. |
| T-NFR-COMPAT | NFR-009, NFR-010 | API 26, 30, 31/32, 36 plus oldest/newest physical family devices; complete outcome matrix. |
| T-NFR-ARCH | NFR-012 | V1-M1-04 implements a reusable test-only fixed clock/zone fixture. Location/repository/provider/UI-state fakes and pure calculation tests remain assigned to later tasks that introduce their approved contracts. |

## Implemented V1-M1-04 foundation

| Test ID | Scope | Evidence | Result |
|---|---|---|---|
| T-M1-04-UNIT | NFR-009–NFR-012 | Two JVM tests exercise repeated reads from `Clock.fixed` and explicit `Asia/Colombo` local representation from a known instant; the forced focused suite passed three times. | Passed 2/2 on every run |
| T-M1-04-LAUNCH | NFR-009–NFR-012 | Instrumented smoke verifies the target package, installed launcher resolution to `MainActivity`, resumed lifecycle, and displayed root without text, style, network, sleep, or wall-clock coupling. | Passed 1/1 on API 36 emulator |

The fixtures live only under `src/test` and `src/androidTest`. V1-M1-04 intentionally did not invent interfaces before their production contracts exist. Location-result and provider seams wait for V1-M4-01/V1-M4-02; persistence and repository seams wait for V1-M4-03/V1-M4-05; UI-state seams wait for V1-M7-01/V1-M7-02; injected clock/zone integration waits for V1-M6-03/V1-M7-03; and pure solar/Hora fixtures remain in M5/M6. API-26 runtime, physical-device, process-death, extended accessibility/localization, and later domain-boundary evidence remain open under their assigned tasks; this foundation does not complete NFR-009–NFR-012 by itself.

## Implemented V1-M1-05 shell evidence

| Test ID | Scope | Evidence | Result |
|---|---|---|---|
| T-M1-05-REDUCER | FR-008, FR-011; NFR-012; ADR-007 | Pure reducer tests cover Dashboard initial state, bounded top-level switching, no-op reselection at a top-level root, representative Location/Settings/About child-to-top-level root resets with canonical no-history state, every permitted child/origin, non-default Back, invalid-transition rejection, and determinism. | Passed 7/7 repeatedly; full JVM suite passed 9/9 |
| T-M1-05-SHELL | UI-002–UI-007; NFR-005, NFR-009, NFR-012 | Compose tests cover launcher Dashboard, Timeline/Method switching, Location/Settings/About and Back, top-level recreation, nested Method → Settings → Location recreation with exact origin restoration, and verified landscape/portrait Activity recreation. | Passed 5/5 independently; complete connected suite passed 6/6 on API 36 |

V1-M1-05 establishes only rendering placeholders and bounded shell structure. Dashboard-root system Back delegates to normal Activity behavior by disabling the app Back handler there; destructive exit is code-reviewed rather than asserted. Activity/configuration recreation is proven through primitive saveable state, but full operating-system process-death restoration is not claimed. Final adaptive, accessibility, localization, visual, physical-device, and feature-state evidence remains assigned to later milestones.

## Implemented M2 design-system evidence

| Test ID | Scope | Evidence | Result |
|---|---|---|---|
| T-M2-01-COLOR | FR-011; NFR-005, NFR-011; UI/THEME | Seven focused JVM tests verify every exact approved light/dark semantic value, the complete 21-role equivalence set, deterministic later/pending/completed aliases, fixed Material mappings using approved colors only, stable light/dark selection with no dynamic-palette input, all approved text pairs, and outline/focus contrast on background and surface. | Passed 7/7 in two forced runs; full JVM suite passed 16/16 |
| T-M2-02-TOKENS | FR-011; NFR-005, NFR-011; UI/THEME | Eight focused JVM tests verify exact typography, spacing, gutters, shapes, elevations, wash constraints, durations/easings, reduced-motion substitution, icon/illustration contracts, focus thickness and 48dp touch targets. | Passed 8/8; exact approved token values retained |
| T-M2-03-MODELS | FR-001–003, FR-009–012; NFR-005 | Pure presentation-model tests verify strict chronology, at most one current row, required text, unavailable/error-kind restrictions and rejection of blank action labels without domain calculation coupling. | Passed 4/4 |
| T-M2-03-COMPONENTS | FR-001–003, FR-009–012; NFR-005; A11Y | Compose tests verify the merged timing-folio summary with separate action, timeline selected state, provenance loading/progress state, unavailable behavior, 48dp action targets and disabled action state. Static review verifies the anchor row, domain-neutral inputs, flexible-height composition and semantic-token use. | Passed as part of complete connected suite 13/13 on API 36; static scope review passed |
| T-M2-04-PREVIEWS | NFR-005, NFR-011; A11Y | Seven JVM tests verify eight deterministic synthetic fixtures across 320dp portrait, 640×320 landscape, 720dp medium, 1000dp expanded, light/dark, long English, selected/unselected, enabled/disabled, loading/error/unavailable and reduced-motion states; Compose smoke verifies representative preview semantics. | JVM 7/7 and connected preview checks passed as part of 13/13 |
| T-M2-FULL | M2; DA-004 | Project discovery, debug runtime dependency audit, forced focused and full JVM suites, lint, debug and Android-test APK builds, install, connected tests, package/launcher/Manifest/SDK inspection, cold launch/crash inspection and repository-integrity audits. | Focused 26/26; full JVM 35/35; connected 13/13 on API 36; lint/build/install/cold launch passed; no dependency/build/SDK/identity delta |

WCAG calculations use the approved exact sRGB values. The weakest tested normal-text pair is 6.68:1 and the weakest outline/focus pair is 4.40:1, exceeding the 4.5:1 and 3.0:1 gates respectively. M2 establishes the semantic palette, exact design tokens, reusable domain-neutral components and deterministic preview matrix. Automated semantics, state, reduced-motion and adaptive-fixture evidence is complete for this milestone. Deferred evidence retains its approved task-specific release gates: V1-M9-01 closes manual OLED/grayscale/high-contrast and pressed/focus/disabled visual review, TalkBack, keyboard/D-pad/focus restoration, actual 200% font-scale, compact-landscape/adaptive inspection, screen magnification, non-colour recognition, localization/expansion, and final screenshot/manual visual evidence; V1-M9-02 supplies API-26 runtime and the complete API/state regression matrix; V1-M9-03 supplies oldest/newest physical-family-device and OEM performance/privacy evidence; V1-M9-04 supplies signing and reproducible APK install/update/rollback/update-lineage evidence; and V1-M9-05 performs final independent QA, owner family acceptance, and release validation.

## Implemented M3 static-product-UI evidence

| Test ID | Scope | Evidence | Result |
|---|---|---|---|
| T-M3-01-DASHBOARD | V1-M3-01; FR-001/003/009/011; UI-002 | JVM preview mapping and Compose tests cover the honest runtime-unavailable state and preview/test-only synthetic Hora ruler, anchors, provenance, calculation time and four actions. Portrait light/dark and corrected landscape-rail presentations were inspected. | Passed; independent QA found the corrected synthetic success complete and production runtime free of fabricated values |
| T-M3-02-TIMELINE | V1-M3-02; FR-002/003/008/011; UI-003 | JVM fixtures require exactly 12 Day plus 12 Night rows, strict order and at most one Current row. Compose checks cover navigation, scrolling and adaptive presentation without UI interval calculation. | Passed; independent QA accepted the fixed synthetic schedule boundary |
| T-M3-03-LOCATION | V1-M3-03; FR-004–007/009; UI-001/004/005 | JVM and Compose checks cover ten preview-only precise/approximate/saved/manual/default/denied/disabled/timeout/invalid/stale presentations with explicit selection, rationale and state-specific recovery actions; production remains unavailable/read-only. | Passed after `bca3f7f`; no provider, permission or persistence behavior added |
| T-M3-04-CONTENT | V1-M3-04; FR-003/010/011; UI-006/007 | JVM and Compose checks cover the approved seven-section Method order, headings, offline About/privacy copy, installed versionName/versionCode and honest unavailable semantics. | Passed after the approved documentation-order correction `25756f9` and implementation review |
| T-M3-SHELL-ADAPTIVE | M3; NFR-005/012; UI-002–007 | Shell tests and visual inspection cover preserved bounded navigation/Back behavior, route-local scroll state, compact bottom navigation, medium/expanded content-before-rail traversal and the 720dp readable-width cap. | Passed after `2bc95be` and `045ed65`; no navigation dependency or M4+ behavior added |
| T-M3-FULL | M3; DA-004; DP-001 | Forced full JVM, lint, debug APK, Android-test APK, compile, install/cold-launch, complete diff/scope/integrity audits, emulator visual review and independent QA. | JVM 41/41; lint 0 errors; debug and Android-test APK builds passed; application/navigation/Compose connected tests 17/17 passed. The separate launcher smoke test was obstructed by an emulator SystemUI ANR focus surface after the last correction; pre-correction complete connected suite passed 16/16 and no app crash/ANR or launcher-code delta was found. Independent QA: PASS, zero open findings. |

The connected launcher-smoke deviation is recorded as an emulator-environment residual, not as evidence for API-family or physical-device release acceptance. V1-M9-01 through V1-M9-05 retain the mandatory task-specific obligations listed above.

## SOLAR-001 planned verification

| Test ID | Scope | Required evidence | Gate |
|---|---|---|---|
| T-SOL-001 | SOL-R-001–SOL-R-003 | Exact convention/profile IDs; coordinate endpoints, NaN/infinity/out-of-range/missing; negative-zero canonicalization; unsupported elevation policy. | PA-004 + later implementation task |
| T-SOL-002 | SOL-R-004, SOL-R-005 | IANA civil-day bounds, 23/25-hour dates, skipped date, historical Colombo offset, manual-vs-device zone change, UTC/local association, following date and range adjacency. | PA-004 + later implementation task |
| T-SOL-003 | SOL-R-006, SOL-R-007 | Equation-by-equation unit tests against `SOLAR-INTERMEDIATE-001-v1.0` literals prepared independently of production code; every frozen term/iteration, constants/order/normalization/`StrictMath`, 1-ULP intermediate checks, identical cross-runtime final anchors, and source-hash audit. | PA-004 + later implementation task |
| T-SOL-004 | SOL-R-008 | Sunrise/sunset sign, UTC base-date search, convergence threshold and five-evaluation cap, inverse-domain margin, no/multiple/grazing event behavior. | PA-004 + later implementation task |
| T-SOL-005 | SOL-R-009 | Half-even millisecond ties, checked epoch conversion, chronology and civil-date invariants, exact following-sunrise ordering. | PA-004 + later implementation task |
| T-SOL-006 | SOL-R-010–SOL-R-012 | Complete success provenance/diagnostics plus every typed unavailable outcome; high-latitude warning and no fabricated event/twilight. | PA-004 + later implementation task |
| T-SOL-007 | SOLAR-GOLDEN-001-v1.0 | SOL-G-001–SOL-G-010: all 30 anchors within inclusive 60.000 s; source/oracle/runtime hashes retained; expected values immutable. | PA-004 + later implementation task |
| T-SOL-008 | SOL-R-013 | Stateless/thread-safe repeated calls, coordinator cancellation generation, 32-entry bounded cache semantics, no persistence, atomic context publication, call counts. | PA-004 + later implementation task |
| T-SOL-009 | CP-001 / CR-004 | Exact shared-anchor reuse by Seasonal Hora; independently derived boundaries within 60 s; exact common-anchor nanosecond endpoints/adjacency and no cumulative drift. | PA-004 + CP-001 implementation task |
| T-SOL-010 | Future CP-003 | Same exact current-date sunrise/sunset/fingerprint is consumable without a second solar call; Rahu remains profile-unavailable until PA-003. | Architecture test now; functional test after PA-003 |
| T-SOL-011 | SOL-R-014 | Resolved-local-wall dashboard minute and diagnostic second half-even rounding, historical sub-minute offset, date carry, overlap/gap label semantics, shared boundary formatted once; display never changes membership/countdown. | PA-004 + final UI/display task |
| T-SOL-012 | Performance/replacement | Three-date solar computation target 50 ms p95 and full result under approved one second on slowest family device; replacement-engine contract/cache invalidation. | Device evidence; 50 ms is engineering target, not product claim |

The astronomical tolerance is selected before production output. A failing vector blocks acceptance; tests may not widen tolerance or rewrite expected values. Event/no-event disagreement requires source/algorithm review. Unit fixtures for arithmetic/failure behavior are derived from SOL-R rules; astronomical expected times come only from the frozen independent oracle.

## Domain validation

VC-001–VC-019 remain the PA-001 deterministic, astronomical, location, lifecycle, and release cases. Approved VC-020–VC-039 obligations cover CR-001 system separation, fixed Kala/Panchama boundaries and unresolved coverage, Rahu, cross-system inputs/provenance, and accessibility. Every concrete record must retain inputs, expected/actual values, tolerance, evidence, versions, and reviewers. CP-002 traditional uncertainty is implementation-blocking; missing expectations must not be invented.

## Delivery-plan test gate

PA-004 approves the SOLAR-001 test obligations and DP-001 approves their assignment to focused tasks in `TASKS.md` and `TRACEABILITY_MATRIX.md`; neither automatically starts implementation. The bounded M1 foundation, M2 design-system evidence, and V1-M3-01 through V1-M3-04 static-product-UI evidence above are implemented. M1, M2, and M3 are Done, explicitly Lead/owner accepted, and incorporated into `dev`; accepted M3 evidence is recorded through `3d9d9d2`. M4 and later test execution begins only through an explicitly started task. Task results do not bypass independent QA, Lead acceptance, milestone integration, or later task-specific release gates.
