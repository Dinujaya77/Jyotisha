# Product Approvals

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.1 |
| Last updated | 2026-08-02 |
| Owner role | Lead Coordinator |
| Approval state | PA-001, Change Request CR-001, PA-002 architecture, and DA-001 Direction A approved; PA-003 remains Blocked |

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

## Approved Phase B architecture

| ID | Item/version | Required phrase | State |
|---|---|---|---|
| PA-002 | Version 1.0 architecture v0.5; ADR-001–ADR-009 v0.5; dependency policy/register v0.4 | `APPROVE VERSION 1.0 ARCHITECTURE` | Approved 2026-08-02 |

PA-002 does not approve exact town coordinates/snapshot rows, production code, final UI/theme specifications, signing/distribution, independent validation results, or release accuracy.

The exact phrase was received on 2026-08-02 after reconciliation and an independent recheck with zero open findings. PA-002 selects the shared V1.0 architecture and DEP-011–DEP-014 in principle; it does not approve `SOLAR-001`, CP-003/RK, exact town data, package identity, direct coroutine artifacts, final UI, implementation, signing/distribution, or release accuracy. Direction A is approved separately under DA-001, not by PA-002.
