# Product Approvals

| Field | Value |
|---|---|
| Status | Awaiting Approval |
| Version | 1.0 |
| Last updated | 2026-08-02 |
| Owner role | Lead Coordinator |
| Approval state | PA-001 and Change Request CR-001 approved; Version 1.0 architecture and design remain unapproved |

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

The amendment phrase approves the Option C product allocation, FR-012–FR-015, NFR-015, US-011–US-013, conditional deltas to existing requirements, and linked validation obligations/case definitions only. CP-002/KH/PK stay Blocked; CP-003/RK stay Awaiting Approval; unresolved expected outputs stay blank/Blocked; ADR-001–ADR-009, architecture, dependencies, design, final UI, delivery, implementation, and release retain separate gates.

## Pending Phase B approval

| ID | Item/version | Required phrase | State |
|---|---|---|---|
| PA-002 | Version 1.0 architecture proposal v0.4; ADR-001–ADR-009; DEP-011–DEP-014 | `APPROVE VERSION 1.0 ARCHITECTURE` | Proposed — reconciliation authorized; not approved |

PA-002 does not approve exact town coordinates/snapshot rows, production code, final UI/theme specifications, signing/distribution, independent validation results, or release accuracy.

CR-001 is decided, so the affected architecture and design proposals may be reconciled for their own approval gates. No architecture, dependency, or design direction is approved by CR-001.
