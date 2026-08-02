# Traceability Matrix

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.1 |
| Last updated | 2026-08-02 |
| Owner role | Lead Coordinator |
| Approval state | PA-001 and Change Request CR-001 product/validation trace approved; no implementation task authorized |

## Version 1.0 functional traceability

| Requirement | Stories | Profile/rules | Validation cases | Planned tests | Implementation task |
|---|---|---|---|---|---|
| FR-001 | US-001, US-008 | CP-001; CR-001–CR-010 | VC-001, VC-003, VC-005, VC-007, VC-015 | T-FR-001, T-NFR-CALC | Phase C — not yet authorized |
| FR-002 | US-002, US-008 | CP-001; CR-002–CR-007 | VC-001, VC-002, VC-004, VC-005, VC-007, VC-015 | T-FR-002, T-NFR-CALC, T-NFR-A11Y | Phase C — not yet authorized |
| FR-003 | US-002, US-008 | CP-001; CR-001, CR-002, CR-008 | VC-006, VC-008, VC-009, VC-014; proposed CR-001: VC-034 | T-FR-003, T-NFR-CALC | Phase C — not yet authorized |
| FR-004 | US-003, US-008 | CR-008–CR-010 | VC-010–VC-012, VC-019 | T-FR-004, T-NFR-COMPAT | Phase C — not yet authorized |
| FR-005 | US-003, US-004, US-008 | CR-009, CR-010 | VC-010–VC-012, VC-016; proposed CR-001: VC-037 | T-FR-005, T-NFR-COMPAT | Phase C — not yet authorized |
| FR-006 | US-004, US-005, US-008 | CR-008–CR-010 | VC-006, VC-011–VC-013; proposed CR-001: VC-034, VC-037 | T-FR-006, T-NFR-OFFLINE | Phase C — not yet authorized |
| FR-007 | US-004, US-006, US-008 | CR-009, CR-010 | VC-012, VC-016, VC-017, VC-019; proposed CR-001: VC-037 | T-FR-007, T-NFR-PRIVACY | Phase C — not yet authorized |
| FR-008 | US-006, US-008 | CR-002, CR-006, CR-008, CR-010 | VC-003, VC-005, VC-008, VC-014, VC-017; proposed CR-001: VC-035, VC-037 | T-FR-008, T-NFR-CALC | Phase C — not yet authorized |
| FR-009 | US-005, US-007, US-008 | CR-009, CR-010 | VC-010, VC-011, VC-013, VC-016; proposed CR-001: VC-037 | T-FR-009, T-NFR-A11Y | Phase C — not yet authorized |
| FR-010 | US-007, US-008, US-009 | CP-001; CR-001–CR-010 | VC-018; proposed CR-001: VC-038 | T-FR-010, T-NFR-PRIVACY | Phase C — not yet authorized |
| FR-011 | US-008, US-009 | Approved terminology | Content/accessibility cases | T-FR-011, T-NFR-A11Y | Phase C — not yet authorized |

## Approved Change Request CR-001 traceability

| Requirement | Stories | Profile/rules | Validation cases | Planned tests | Implementation task/state |
|---|---|---|---|---|---|
| FR-012 | US-011 | CP-003; RK-001–RK-005 | VC-020, VC-030–VC-039 | T-FR-012, T-NFR-CALC, T-NFR-A11Y, T-NFR-SYSTEMS | Requirement approved; implementation blocked on CP-003/RK |
| FR-013 | US-012 | CP-002; KH-001–KH-005 | VC-020, VC-021, VC-024–VC-028, VC-034–VC-039 | T-FR-013, T-NFR-CALC, T-NFR-SYSTEMS | Requirement approved; implementation Blocked on CP-002 |
| FR-014 | US-012 | CP-002; PK-001–PK-004; USR-DOM-001 evidence | VC-020, VC-022–VC-027, VC-029, VC-034–VC-039 | T-FR-014, T-NFR-CALC, T-NFR-SYSTEMS | Requirement approved; implementation Blocked on CP-002 |
| FR-015 | US-012, US-013 | CP-002; KH-*; PK-* | VC-020, VC-024, VC-026, VC-029, VC-034, VC-035, VC-037–VC-039 | T-FR-015, T-NFR-A11Y, T-NFR-SYSTEMS | Requirement approved; implementation Blocked on domain/design |
| NFR-015 | US-011–US-013 | CP-001–CP-003; ADR-008, ADR-009 proposed | VC-020, VC-029, VC-034, VC-035, VC-037–VC-039 | T-NFR-SYSTEMS, T-NFR-CALC, T-NFR-ARCH | Approved; implementation architecture/profile gates pending |

CP-002 is not linked to CP-001 interval rules merely because both consume solar events. `Change Request CR-001` is a change-control identifier and remains distinct from approved calculation rule `CR-001 — Solar anchors`.

## Non-functional traceability

| Requirements | Stories | Tests/evidence | Release status |
|---|---|---|---|
| NFR-001–NFR-003, NFR-014 | US-010 | T-NFR-CALC; VC-001–VC-009, VC-015, VC-018 | Traditional/independent datasets pending |
| NFR-004, NFR-007, NFR-008 | US-004 | T-NFR-OFFLINE, T-NFR-PRIVACY | Backup implementation pending |
| NFR-005, NFR-011 | US-008 | T-NFR-A11Y | UI specification pending |
| NFR-006 | US-001, US-006 | T-NFR-PERF | Device threshold revalidation pending |
| NFR-009, NFR-010 | US-003–US-006 | T-NFR-COMPAT | Actual family-device inventory pending |
| NFR-012 | US-010 | T-NFR-ARCH | Architecture approval pending |
| NFR-013 | US-010 | T-NFR-PRIVACY plus release checklist | Release blocker |

## Delivery-control traceability

| Control/task | Outcome | Files | Evidence | State |
|---|---|---|---|---|
| SETUP-001 | Controlled team and documentation foundation | `.codex/**`, `AGENTS.md`, `docs/**` | T-SETUP-001–T-SETUP-003, T-BASE-001–T-BASE-004 | Done |
| PA-001 | V1.0 requirements/domain package | Product/domain/security/delivery docs v1.0 | Business, architecture, QA reviews; explicit phrase received 2026-08-01 | Approved |
| Change Request CR-001 | Requirements amendment and Option C V1.0/V1.1 allocation | Product/domain/engineering/design/delivery docs | BA/Architect/Designer analyses; independent QA/recheck; exact phrase supplied 2026-08-02 | Approved |

Task IDs may be filled only in a later authorized planning phase after requirements, applicable profiles/rules, architecture, and UI approval. “Not authorized” is an explicit gate, not claimed implementation coverage.
