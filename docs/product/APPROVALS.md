# Product Approvals

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.3 |
| Last updated | 2026-08-06 |
| Owner role | Lead Coordinator |
| Approval state | PA-001, Change Request CR-001, PA-002, DA-001, PA-004 SOLAR-001, DA-004 final UI, and DP-001 delivery plan Approved; PA-003 Blocked |

## Confirmed stakeholder decisions

| ID | Date | Decision | State | Evidence |
|---|---|---|---|---|
| PD-001 | 2026-08-01 | V1.0 uses 12 seasonal day Horas plus 12 seasonal night Horas and the seven-ruler cyclic sequence. | Confirmed | Owner Phase A response |
| PD-002 | 2026-08-01 | Solar anchors use apparent upper limb, standard refraction, centre zenith 90.8333°, sea-level/unobstructed-horizon assumptions. | Confirmed | Owner Phase A response |
| PD-003 | 2026-08-01 | Foreground current-device location is preferred; manual Sri Lankan towns and saved/default fallbacks remain functional. | Confirmed | Owner Phase A response |
| PD-004 | 2026-08-01 | V1.0 provides neutral timing/education only and excludes advice, predictions, remedies, and deterministic life guidance. | Confirmed | Owner Phase A response |
| PD-005 | 2026-08-01 | `minSdk 26` is provisional; compile/target SDK remain modern and independent. | Confirmed provisional direction | Owner Phase A response |
| PD-006 | 2026-08-01 | Missing traditional authority blocks release approval only; expected results must never be changed merely to pass. | Confirmed | Owner Phase A response |
| PD-007 | 2026-08-01 | Investigate Sri Lankan Kala Hora, Panchama Kala and Rahu Kalaya as distinct concepts; USR-DOM-001 is user supplied but not independently verified. | Confirmed request, not an approved rule | Owner Change Request CR-001 |

## Package approval

| ID | Item/version | Required phrase | State |
|---|---|---|---|
| PA-001 | Version 1.0 product requirements, CP-001, CR-001–CR-010, and linked validation strategy v1.0 | `APPROVE VERSION 1.0 REQUIREMENTS` | Approved 2026-08-01 |

Approval of PA-001 does not approve an unselected solar algorithm, an external location dependency, unverified Colombo coordinates, an unverified Sri Lankan town dataset, unresolved persistence/backup behavior, a visual direction, final UI specification, delivery plan, or final release calculation accuracy. Those retain their later gates.

## Approved requirements amendment

| ID | Item/version | Required phrase | State |
|---|---|---|---|
| Change Request CR-001 | Sri Lankan Kala Hora, Panchama Kala and Rahu Kalaya amendment v0.1 | `APPROVE REQUIREMENTS AMENDMENT CR-001` | Approved 2026-08-02 |

This amendment preserves PA-001's history and approves the Option C Version 1.0/1.1 allocation, FR-012–FR-015, NFR-015, US-011–US-013, conditional requirement deltas, and VC-020–VC-039 case definitions/obligations. CP-002/CP-003 and KH-*/PK-*/RK-* remain separately gated and unapproved. The change-request namespace is distinct from calculation rule `CR-001 — Solar anchors`.

The amendment phrase approves the Option C product allocation, FR-012–FR-015, NFR-015, US-011–US-013, conditional deltas to existing requirements, and linked validation obligations/case definitions only. CP-002/KH/PK stay Blocked; CP-003/RK are Blocked from approval until PA-003 prerequisites are complete; unresolved expected outputs stay blank/Blocked; ADR-001–ADR-009, architecture, dependencies, design, final UI, delivery, implementation, and release retain separate gates.

## Pending daytime Rahu domain approval

| ID | Item/version | Required phrase | State |
|---|---|---|---|
| PA-003 | `CP-003-v0.2`, `RK-v0.2`, and linked source-backed golden dataset | `APPROVE DAYTIME RAHU CALCULATION PROFILE CP-003` | Blocked — selected authority, golden vectors, and terminology evidence incomplete |

PA-003 would approve the daytime profile/rules only. It would not approve architecture, design, implementation, release, CP-002, or nighttime Rahu. Do not request the phrase until `CALCULATION_PROFILE.md`'s readiness list is complete and independently reviewed.

## Approved SOLAR-001 profile

| ID | Item/version | Required phrase | State |
|---|---|---|---|
| PA-004 | `SOLAR-001-v1.0`; SOL-R-001–SOL-R-014 (`SOL-R-v1.0`); `NOAA-MEEUS-001-v1.0`; `SOLAR-GOLDEN-001-v1.0`; `SOLAR-INTERMEDIATE-001-v1.0`; canonical source-role set SRC-004 and SRC-019–SRC-027 (SRC-005 excluded as superseded) | `APPROVE SOLAR-001` | Approved — exact phrase supplied 2026-08-02 after independent QA/recheck found zero open findings |

PA-004 became valid when the repository owner explicitly supplied the exact phrase on 2026-08-02; no approval was inferred from proposal preparation, frozen vectors, PA-002, or test results.

PA-004 approves the astronomical convention and production/validation profile only. It does not authorize a dependency change, Kotlin/Compose/resource/Manifest/Gradle edit, implementation milestone/task, UI specification, town catalogue for product use, CP-003/RK, CP-002, traditional release validation, or release. The same owner instruction separately authorizes one documentation checkpoint and a documentation-only UI/delivery-planning pass; those permissions are not effects of PA-004 itself.

## Approved Phase B architecture

| ID | Item/version | Required phrase | State |
|---|---|---|---|
| PA-002 | Version 1.0 architecture v0.5; ADR-001–ADR-009 v0.5; dependency policy/register v0.4 | `APPROVE VERSION 1.0 ARCHITECTURE` | Approved 2026-08-02 |

PA-002 does not approve exact town coordinates/snapshot rows, production code, final UI/theme specifications, signing/distribution, independent validation results, or release accuracy.

The exact phrase was received on 2026-08-02 after reconciliation and an independent recheck with zero open findings. PA-002 selects the shared V1.0 architecture and DEP-011–DEP-014 in principle; it does not approve `SOLAR-001`, CP-003/RK, exact town data, package identity, direct coroutine artifacts, final UI, implementation, signing/distribution, or release accuracy. Direction A is approved separately under DA-001, not by PA-002.

## Approved Version 1.0 UI and delivery plan

| ID | Item/version | Required phrase | State |
|---|---|---|---|
| DA-004 | `UI-V1.0-001`, `THEME-CELESTIAL-ARCHIVE-001`, and `A11Y-V1.0-001` v1.0 | `APPROVE VERSION 1.0 UI` | Approved — exact phrase supplied 2026-08-02 after independent QA/recheck found zero open findings |
| DP-001 | M1–M9, 39 tasks V1-M1-01–V1-M9-05, trace/test/branch gates v1.0 | `APPROVE DELIVERY PLAN` | Approved — exact phrase supplied 2026-08-02 |

DA-004 and DP-001 became valid only when the repository owner explicitly supplied both phrases. They approved the Version 1.0 UI specification and delivery plan, not automatic implementation; at the time of approval, coding had not started. The owner subsequently started and accepted V1-M1-01 through V1-M1-05; M1 is Done and incorporated into `dev` through `79d5116`. V1-M2-01 through V1-M2-04 are now Done; M2 task execution is complete, while M2 remains In Review, not Lead/owner accepted, not merged into `dev`, and not pushed. M3 and later milestones remain unstarted. M5 depends on an accepted M1, M6 depends on accepted M5 and CP-001, and M8 remains Blocked by PA-003/CP-003. Fixed Kala Hora and Panchama remain Version 1.1.
