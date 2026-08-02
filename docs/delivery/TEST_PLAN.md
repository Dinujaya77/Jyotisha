# Test Plan

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.1 |
| Last updated | 2026-08-02 |
| Owner role | QA Reviewer |
| Approval state | PA-001 test strategy and CR-001 test obligations approved; implementation/evidence remain separately gated |

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
| T-NFR-SYSTEMS | NFR-015 | Distinct typed calculators/profiles/outcomes, one shared solar context, cache-call counts, fingerprints, rounding ownership, UI-calculation prohibition, and atomic multi-system bundle. | Approved obligation and architecture; profile/UI/delivery/implementation gates remain |

## Non-functional test suites

| Test ID | Covers | Completion evidence |
|---|---|---|
| T-NFR-CALC | NFR-001–NFR-003, NFR-014 | Determinism, exact interval invariants, independent astronomy/traditional datasets, profile/engine provenance. |
| T-NFR-OFFLINE | NFR-004, NFR-007 | Airplane-mode workflows, manifest/network observation, no upload/analytics/background location. |
| T-NFR-A11Y | NFR-005, NFR-011 | TalkBack, 200% font, 48dp targets, contrast, pseudolocale/expansion, non-colour meaning. |
| T-NFR-PERF | NFR-006 | Slowest approved family-device measurement; complete result within the approved 1-second threshold; no main-thread blocking. |
| T-NFR-PRIVACY | NFR-007, NFR-008, NFR-013 | Storage/log/manifest/backup/transfer/secret audit and signed-APK review. |
| T-NFR-COMPAT | NFR-009, NFR-010 | API 26, 30, 31/32, 36 plus oldest/newest physical family devices; complete outcome matrix. |
| T-NFR-ARCH | NFR-012 | Fake clock/zone/location/repository and pure calculation tests without Android runtime. |

## Domain validation

VC-001–VC-019 remain the PA-001 deterministic, astronomical, location, lifecycle, and release cases. Approved VC-020–VC-039 obligations cover CR-001 system separation, fixed Kala/Panchama boundaries and unresolved coverage, Rahu, cross-system inputs/provenance, and accessibility. Every concrete record must retain inputs, expected/actual values, tolerance, evidence, versions, and reviewers. CP-002 traditional uncertainty is implementation-blocking; missing expectations must not be invented.

## Later planning gate

No Phase C/delivery-planning work is authorized by CR-001 approval. After all prerequisite approvals, later planning may assign planned test IDs to focused implementation tasks and physical-device milestones. No test is claimed as implemented by this document.
