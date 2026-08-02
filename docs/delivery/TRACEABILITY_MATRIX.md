# Traceability Matrix

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.3 |
| Last updated | 2026-08-02 |
| Owner role | Lead Coordinator |
| Approval state | PA-001/CR-001 and PA-004 SOLAR-001 trace approved; no implementation task authorized by approval alone |

## Version 1.0 functional traceability

| Requirement | Stories | Profile/rules | Validation cases | Planned tests | Implementation task |
|---|---|---|---|---|---|
| FR-001 | US-001, US-008 | CP-001; CR-001–CR-010; SOL-R-001–SOL-R-014 approved | VC-001, VC-003, VC-005, VC-007, VC-015; SOL-G-001–010 | T-FR-001, T-NFR-CALC, T-SOL-007, T-SOL-009 | Awaiting approved delivery task |
| FR-002 | US-002, US-008 | CP-001; CR-002–CR-007; SOL-R-009/SOL-R-013 approved | VC-001, VC-002, VC-004, VC-005, VC-007, VC-015; SOL-B-014 | T-FR-002, T-NFR-CALC, T-NFR-A11Y, T-SOL-009 | Awaiting approved delivery task |
| FR-003 | US-002, US-008 | CP-001; CR-001, CR-002, CR-008; SOL-R-001–SOL-R-014 approved | VC-006, VC-008, VC-009, VC-014, VC-034; SOL-G-001–010, SOL-B-001–012 | T-FR-003, T-NFR-CALC, T-SOL-001–008 | Awaiting approved delivery task |
| FR-004 | US-003, US-008 | CR-008–CR-010 | VC-010–VC-012, VC-019 | T-FR-004, T-NFR-COMPAT | Phase C — not yet authorized |
| FR-005 | US-003, US-004, US-008 | CR-009, CR-010 | VC-010–VC-012, VC-016; proposed CR-001: VC-037 | T-FR-005, T-NFR-COMPAT | Phase C — not yet authorized |
| FR-006 | US-004, US-005, US-008 | CR-008–CR-010 | VC-006, VC-011–VC-013; proposed CR-001: VC-034, VC-037 | T-FR-006, T-NFR-OFFLINE | Phase C — not yet authorized |
| FR-007 | US-004, US-006, US-008 | CR-009, CR-010 | VC-012, VC-016, VC-017, VC-019; proposed CR-001: VC-037 | T-FR-007, T-NFR-PRIVACY | Phase C — not yet authorized |
| FR-008 | US-006, US-008 | CR-002, CR-006, CR-008, CR-010; SOL-R-004, SOL-R-005 approved | VC-003, VC-005, VC-008, VC-014, VC-017, VC-035, VC-037; SOL-B-004, SOL-B-006–008 | T-FR-008, T-NFR-CALC, T-SOL-002 | Awaiting approved delivery task |
| FR-009 | US-005, US-007, US-008 | CR-009, CR-010 | VC-010, VC-011, VC-013, VC-016; proposed CR-001: VC-037 | T-FR-009, T-NFR-A11Y | Phase C — not yet authorized |
| FR-010 | US-007, US-008, US-009 | CP-001; CR-001–CR-010; SOL-R-010, SOL-R-014 approved | VC-018, VC-038; all SOL-G records | T-FR-010, T-NFR-PRIVACY, T-SOL-006, T-SOL-007 | Awaiting approved delivery task |
| FR-011 | US-008, US-009 | Approved terminology | Content/accessibility cases | T-FR-011, T-NFR-A11Y | Phase C — not yet authorized |

## Approved Change Request CR-001 traceability

| Requirement | Stories | Profile/rules | Validation cases | Planned tests | Implementation task/state |
|---|---|---|---|---|---|
| FR-012 | US-011 | CP-003; RK-001–RK-005 | VC-020, VC-030–VC-039 | T-FR-012, T-NFR-CALC, T-NFR-A11Y, T-NFR-SYSTEMS | Requirement approved; implementation blocked on CP-003/RK |
| FR-013 | US-012 | CP-002; KH-001–KH-005 | VC-020, VC-021, VC-024–VC-028, VC-034–VC-039 | T-FR-013, T-NFR-CALC, T-NFR-SYSTEMS | Requirement approved; implementation Blocked on CP-002 |
| FR-014 | US-012 | CP-002; PK-001–PK-004; USR-DOM-001 evidence | VC-020, VC-022–VC-027, VC-029, VC-034–VC-039 | T-FR-014, T-NFR-CALC, T-NFR-SYSTEMS | Requirement approved; implementation Blocked on CP-002 |
| FR-015 | US-012, US-013 | CP-002; KH-*; PK-* | VC-020, VC-024, VC-026, VC-029, VC-034, VC-035, VC-037–VC-039 | T-FR-015, T-NFR-A11Y, T-NFR-SYSTEMS | Requirement approved; implementation Blocked on CP-002 and final UI |
| NFR-015 | US-011–US-013 | CP-001–CP-003; ADR-008, ADR-009 approved | VC-020, VC-029, VC-034, VC-035, VC-037–VC-039 | T-NFR-SYSTEMS, T-NFR-CALC, T-NFR-ARCH | Architecture approved; profile/UI/delivery gates pending |

CP-002 is not linked to CP-001 interval rules merely because both consume solar events. `Change Request CR-001` is a change-control identifier and remains distinct from approved calculation rule `CR-001 — Solar anchors`.

## SOLAR-001 traceability

| Solar rule group | Product/domain consumers | Validation cases | Planned tests | Approval/implementation state |
|---|---|---|---|---|
| SOL-R-001–SOL-R-003 | FR-003, FR-010; CP-001; future CP-003 | SOL-G-001–010; SOL-B-001–003 | T-SOL-001 | PA-004 Approved; task pending |
| SOL-R-004–SOL-R-005 | FR-003, FR-008; CR-002, CR-008, CR-010 | SOL-G-001–010; SOL-B-004, SOL-B-006–008 | T-SOL-002 | PA-004 Approved; task pending |
| SOL-R-006–SOL-R-009 | FR-001–FR-003; CR-001, CR-004 | SOLAR-INTERMEDIATE-001-v1.0; SOL-G-001–010; SOL-B-005, SOL-B-009–012 | T-SOL-003–T-SOL-005, T-SOL-007 | PA-004 Approved; task pending |
| SOL-R-010–SOL-R-012 | FR-003, FR-010; NFR-014 | SOL-B-001–SOL-B-011 | T-SOL-006 | PA-004 Approved; task pending |
| SOL-R-013 | NFR-012, NFR-015; CP-001/future CP-003 | VC-034, VC-038; SOL-B-005, SOL-B-013–015 | T-SOL-008–T-SOL-010 | PA-004 Approved; task pending; CP-003 separately Blocked |
| SOL-R-014 | NFR-001–NFR-003, NFR-014 | VC-009, VC-015, VC-018; SOL-G-001–010, SOL-B-012, SOL-B-014, SOL-B-016 | T-SOL-005, T-SOL-007, T-SOL-009, T-SOL-011 | PA-004 Approved; task pending; final UI separately gated |

## Non-functional traceability

| Requirements | Stories | Tests/evidence | Release status |
|---|---|---|---|
| NFR-001–NFR-003, NFR-014 | US-010 | T-NFR-CALC; VC-001–VC-009, VC-015, VC-018 | Traditional/independent datasets pending |
| NFR-004, NFR-007, NFR-008 | US-004 | T-NFR-OFFLINE, T-NFR-PRIVACY | Backup implementation pending |
| NFR-005, NFR-011 | US-008 | T-NFR-A11Y | UI specification pending |
| NFR-006 | US-001, US-006 | T-NFR-PERF | Device threshold revalidation pending |
| NFR-009, NFR-010 | US-003–US-006 | T-NFR-COMPAT | Actual family-device inventory pending |
| NFR-012 | US-010 | T-NFR-ARCH | Architecture approved; implementation evidence pending |
| NFR-013 | US-010 | T-NFR-PRIVACY plus release checklist | Release blocker |

## Delivery-control traceability

| Control/task | Outcome | Files | Evidence | State |
|---|---|---|---|---|
| SETUP-001 | Controlled team and documentation foundation | `.codex/**`, `AGENTS.md`, `docs/**` | T-SETUP-001–T-SETUP-003, T-BASE-001–T-BASE-004 | Done |
| PA-001 | V1.0 requirements/domain package | Product/domain/security/delivery docs v1.0 | Business, architecture, QA reviews; explicit phrase received 2026-08-01 | Approved |
| Change Request CR-001 | Requirements amendment and Option C V1.0/V1.1 allocation | Product/domain/engineering/design/delivery docs | BA/Architect/Designer analyses; independent QA/recheck; exact phrase supplied 2026-08-02 | Approved |
| Baseline checkpoint | Controlled team and approved requirements baseline | `.gitignore`, `README.md`, `.codex/**`, `AGENTS.md`, `docs/**` | Owner's explicit commit authorization; commit `be1b132` on `dev`; no app/Gradle change | Done |
| Architecture/design checkpoint | PA-002 architecture and DA-001 Celestial Archive approval baseline | 26 documentation files | Owner's explicit commit authorization; commit `7848f1b` on `dev`; no app/Gradle change | Done |
| PA-002 | Reconciled Version 1.0 architecture package v0.5 | Engineering docs; CP-003 unavailable contract | Architect analysis; independent QA/recheck zero open findings; exact phrase supplied 2026-08-02 | Approved |
| DA-001 | Direction A — Celestial Archive v0.4 | Design docs; seven daytime-Rahu states; V1/V1.1 hierarchy | Designer analysis; independent QA/recheck; exact phrase supplied 2026-08-02 | Approved |
| DA-002–DA-003 | Directions B/C v0.4 | Preserved alternative design narratives | Superseded by DA-001 selection for Version 1.0; not approved | Superseded |
| PA-003 | Candidate daytime Rahu profile CP-003-v0.2 / RK-v0.2 | Domain profile/rules/validation docs | Authority, source-backed vectors, terminology, and source versions/hashes missing | Blocked |
| PA-004 | `SOLAR-001-v1.0`, SOL-R-v1.0, `NOAA-MEEUS-001-v1.0`, SOLAR-GOLDEN-001-v1.0, SOLAR-INTERMEDIATE-001-v1.0 | Domain/engineering/test/source/approval/status docs | Exact approval phrase supplied 2026-08-02; official source hashes; Architect/source review; pinned independent NREL-SPA vectors; QA/recheck zero open findings | Approved |

Task IDs may be filled only in a later authorized planning phase after requirements, applicable profiles/rules, architecture, and UI approval. “Not authorized” is an explicit gate, not claimed implementation coverage.
