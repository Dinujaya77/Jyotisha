# Traceability Matrix

| Field | Value |
|---|---|
| Status | Approved |
| Version | 2.0 |
| Last updated | 2026-08-02 |
| Owner role | Lead Coordinator; Project Manager and QA Reviewer read-only reviewers |
| Approval state | Requirements/rules/architecture/SOLAR/UI/delivery trace Approved as recorded; DA-004 and DP-001 Approved 2026-08-02; task mapping does not start implementation |

## Version 1.0 functional traceability

| Requirement | Approved rules / gates | Validation and planned tests | Primary candidate tasks |
|---|---|---|---|
| FR-001 | CP-001; CR-001–010; SOL-R-001–014 | VC-001/003/005/007/015; T-FR-001, T-NFR-CALC, T-SOL-007/009 | V1-M3-01, V1-M6-01/03/04, V1-M7-01/03/04 |
| FR-002 | CP-001; CR-002–007; SOL-R-009/013 | VC-001/002/004/005/007/015; T-FR-002, T-NFR-CALC/A11Y, T-SOL-009 | V1-M3-02, V1-M6-01/02, V1-M7-01/04 |
| FR-003 | CP-001; CR-001/002/008; SOL-R-001–014 | VC-006/008/009/014/034; SOL-G/B; T-FR-003, T-SOL-001–008 | V1-M3-01/04, V1-M5-01, V1-M6-04, V1-M7-04 |
| FR-004 | CR-008–010 | VC-010–012/019; T-FR-004, T-NFR-COMPAT | V1-M3-03, V1-M4-02/05, V1-M7-02 |
| FR-005 | CR-009/010 | VC-010–012/016/037; T-FR-005, T-NFR-COMPAT | V1-M3-03, V1-M4-01/02/05, V1-M7-02 |
| FR-006 | CR-008–010; exact town data separately gated | VC-006/011–013/034/037; T-FR-006, T-NFR-OFFLINE | V1-M3-03, V1-M4-04/05, V1-M7-02 |
| FR-007 | CR-009/010 | VC-012/016/017/019/037; T-FR-007, T-NFR-PRIVACY | V1-M3-03, V1-M4-01/03/05, V1-M7-02 |
| FR-008 | CR-002/006/008/010; SOL-R-004/005 | VC-003/005/008/014/017/035/037; T-FR-008, T-NFR-CALC, T-SOL-002 | V1-M5-01, V1-M6-03, V1-M7-03 |
| FR-009 | CR-009/010 | VC-010/011/013/016/037; T-FR-009, T-NFR-A11Y | V1-M3-01/03, V1-M4-01/04/05, V1-M7-01/04 |
| FR-010 | CP-001; CR-001–010; SOL-R-010/014 | VC-018/038; SOL-G; T-FR-010, T-NFR-PRIVACY, T-SOL-006/007 | V1-M3-04, V1-M5-01, V1-M6-04, V1-M7-02 |
| FR-011 | Approved terminology; DA-004 Approved | Content/A11Y cases; T-FR-011, T-NFR-A11Y | V1-M2-01/02, V1-M3-01/04, V1-M7-04 |
| FR-012 | Requirement approved; CP-003/RK and PA-003 Blocked | VC-020/030–039; T-FR-012, T-NFR-CALC/A11Y/SYSTEMS | V1-M8-01/02/03, all Blocked |
| FR-013–015 | Option C allocation approved; CP-002/KH/PK Blocked; Version 1.1 only | VC-020–029/034–039 as linked; T-FR-013–015 | No Version 1.0 implementation task |

`Change Request CR-001` is the amendment identifier and remains distinct from calculation rule `CR-001 — Solar anchors`.

## Non-functional traceability

| Requirements | Primary milestones/tasks | Test suites / release evidence |
|---|---|---|
| NFR-001–003, NFR-014 | M5, M6, M9-02/05 | T-NFR-CALC; T-SOL-001–012; independent datasets |
| NFR-004, NFR-007, NFR-008 | M4, M7, M9-03 | T-NFR-OFFLINE, T-NFR-PRIVACY; backup/transfer evidence |
| NFR-005, NFR-011 | M2, M3, M9-01 | T-NFR-A11Y; DA-004 and produced UI evidence |
| NFR-006 | V1-M5-05, V1-M9-03 | T-NFR-PERF, T-SOL-012; family-device measurements |
| NFR-009, NFR-010 | M1, V1-M4-02, V1-M9-02/03 | T-NFR-COMPAT; API/OEM/device matrix |
| NFR-012 | M1, V1-M5-05, M7, V1-M9-02 | T-NFR-ARCH; pure fakes/boundaries |
| NFR-013 | V1-M1-02, V1-M9-04/05 | T-NFR-PRIVACY; signed APK/install/update audit |
| NFR-015 | V1-M5-05, V1-M6-04, M7, M8, V1-M9-05 | T-NFR-SYSTEMS; CP-003 remains separate |

## UI and evidence traceability

| UI unit | Requirements | Implementation/evidence tasks | Approval gate |
|---|---|---|---|
| UI-001 First use | FR-004–006, FR-010 | V1-M3-03, V1-M4-02/05, V1-M9-01 | DA-004 |
| UI-002 Dashboard | FR-001/003/007–012 | V1-M3-01, V1-M7-04, V1-M9-01 | DA-004; Rahu times additionally PA-003/M8 |
| UI-003 Timeline | FR-001–003/008/011 | V1-M3-02, V1-M7-04, V1-M9-01 | DA-004 |
| UI-004 Location | FR-004–007/009 | V1-M3-03, V1-M4-02/04/05, V1-M9-01 | DA-004; town data separate |
| UI-005 Settings | FR-004–011 | V1-M3-03, V1-M4-03/05, V1-M9-01 | DA-004 |
| UI-006 Method | FR-003/008–012 | V1-M3-04, V1-M7-04, V1-M9-01 | DA-004; Rahu method additionally PA-003 |
| UI-007 About/privacy | NFR-001/004/007/008/010–012 | V1-M3-04, V1-M9-01/04 | DA-004 |
| THEME/A11Y | FR-011; NFR-005/011 | V1-M2-01–04, V1-M9-01 | DA-004; actual evidence at milestone/release gates |

## SOLAR-001 traceability

| Rules | Consumers | Tests | State |
|---|---|---|---|
| SOL-R-001–005 | FR-003/008/010; CP-001 | T-SOL-001/002 | PA-004 Approved; V1-M5-01 Proposed |
| SOL-R-006/007 | Solar engine | T-SOL-003; 105 frozen intermediates | PA-004 Approved; V1-M5-02 Proposed |
| SOL-R-008–012 | Solar engine/outcomes | T-SOL-004–007; SOL-G-001–010, SOL-B cases | PA-004 Approved; V1-M5-03/04 Proposed |
| SOL-R-013/014 | Shared context/cache/display | T-SOL-008–012 | PA-004 Approved; V1-M5-05 and later consumers Proposed; CP-003 separately Blocked |

## Delivery controls

| Record | State / evidence |
|---|---|
| SETUP-001 / M-000 | Done; team and documentation foundation |
| PA-001 | Approved 2026-08-01; V1.0 requirements, CP-001 and CR-001–010 |
| Change Request CR-001 | Approved 2026-08-02; Option C and FR-012–015/NFR-015 allocation |
| PA-002 | Approved 2026-08-02; architecture/ADR-001–009 and DEP-011–014 in principle |
| DA-001 | Approved 2026-08-02; Direction A Celestial Archive narrative |
| PA-003 | Blocked; CP-003/RK authority, terminology, sources and goldens missing |
| PA-004 | Approved 2026-08-02; SOLAR-001 package; checkpoint `44d248d` |
| DA-004 | Approved 2026-08-02; exact phrase `APPROVE VERSION 1.0 UI` explicitly received |
| DP-001 | Approved 2026-08-02; M1–M9 and V1-M1-01–V1-M9-05; exact phrase `APPROVE DELIVERY PLAN` explicitly received |

Approved planning task IDs are traceability assignments, not automatic task-start authorization. Coding remains not started until the Lead explicitly starts V1-M1-01 or another dependency-eligible task.
