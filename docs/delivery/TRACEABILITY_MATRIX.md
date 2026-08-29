# Traceability Matrix

| Field | Value |
|---|---|
| Status | Approved |
| Version | 2.0 |
| Last updated | 2026-08-26 |
| Owner role | Lead Coordinator; Project Manager and QA Reviewer read-only reviewers |
| Approval state | Requirements/rules/architecture/SOLAR/UI/delivery trace Approved as recorded; DA-004 and DP-001 Approved 2026-08-02; M1, M2, and M3 Done, explicitly Lead/owner accepted, and incorporated into `dev`; V1-M3-01 through V1-M3-04 Done; remaining mappings do not start implementation |

## Version 1.0 functional traceability

| Requirement | Approved rules / gates | Validation and planned tests | Primary candidate tasks |
|---|---|---|---|
| FR-001 | CP-001; CR-001–010; SOL-R-001–014 | VC-001/003/005/007/015; T-FR-001, T-NFR-CALC, T-SOL-007/009 | V1-M3-01, V1-M6-01/03/04, V1-M7-01/03/04 |
| FR-002 | CP-001; CR-002–007; SOL-R-009/013 | VC-001/002/004/005/007/015; T-FR-002, T-NFR-CALC/A11Y, T-SOL-009 | V1-M3-02, V1-M6-01/02, V1-M7-01/04 |
| FR-003 | CP-001; CR-001/002/008; SOL-R-001–014 | VC-006/008/009/014/034; SOL-G/B; T-FR-003, T-SOL-001–008 | V1-M3-01/04, V1-M5-01, V1-M6-04, V1-M7-04 |
| FR-004 | CR-008–010 | VC-010–012/019; T-FR-004, T-NFR-COMPAT | V1-M3-03, V1-M4-02/05, V1-M7-02 |
| FR-005 | CR-009/010 | VC-010–012/016/037; T-FR-005, T-NFR-COMPAT | V1-M3-03, V1-M4-01/02/05, V1-M7-02 |
| FR-006 | CR-008–010; exact town data recorded in [V1-M4-04 evidence](V1-M4-04_TOWN_CATALOG_EVIDENCE.md) | VC-006/011–013/034/037; T-FR-006, T-NFR-OFFLINE | V1-M3-03, V1-M4-04/05, V1-M7-02 |
| FR-007 | CR-009/010 | VC-012/016/017/019/037; T-FR-007, T-NFR-PRIVACY | V1-M3-03, V1-M4-01/03/05, V1-M7-02 |
| FR-008 | CR-002/006/008/010; SOL-R-004/005 | VC-003/005/008/014/017/035/037; T-FR-008, T-NFR-CALC, T-SOL-002 | V1-M3-02 (presentation only), V1-M5-01, V1-M6-03, V1-M7-03 |
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
| THEME/A11Y | FR-011; NFR-005/011 | V1-M2-01–04, V1-M9-01 | DA-004; all M2 tasks Done with exact semantic-colour, token, reusable-component, deterministic-preview, automated semantics and API-36 connected evidence; manual/device evidence remains at M9/release gates |

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
| V1-M1-01 | Done 2026-08-02 on `milestone/M1-android-foundation`; NFR-009/NFR-012/NFR-013 and ADR-006/007 baseline recorded; Developer and independent QA unit/lint/debug/test-APK compilation and repository audits passed; zero Android/Gradle normalization delta; Lead accepted |
| V1-M1-02 | Done 2026-08-02 on `milestone/M1-android-foundation`; NFR-009/NFR-013 and ADR-006/007; permanent visible name `Jyotisha`, namespace/application ID `io.github.dinujaya77.jyotisha`; Developer and independent QA verified package paths, merged Manifest, APK identity, and required builds; Lead accepted |
| V1-M1-03 | Done 2026-08-05 on `milestone/M1-android-foundation`; NFR-009/NFR-012, ADR-006, DEP-011–013; min SDK 26 with compile/target 36; exact Lifecycle 2.9.2 direct dependencies and transitive/licence/advisory/size evidence recorded; API-26 compile and API-36 install/launch/connected checks passed; Developer and independent QA verified; Lead accepted |
| V1-M1-04 | Done 2026-08-05 on `milestone/M1-android-foundation`; NFR-009–NFR-012; generated template tests replaced by a test-only fixed clock/zone fixture and package/launcher/MainActivity/root smoke test; JVM 2/2 repeatedly and API-36 connected 1/1 passed; no production/dependency/private-location change; later contract fakes and broader compatibility suites remain assigned; Developer and independent QA verified; Lead accepted |
| V1-M1-05 | Done 2026-08-05 on `milestone/M1-android-foundation`; FR-008/FR-011, NFR-005/NFR-012, ADR-007, UI-002–UI-007; one-activity manual root, immutable typed bounded shell reducer, exact origin-aware Back/top-level reset, primitive saveable restoration and externalized rendering-only placeholders; JVM 9/9 and committed connected 6/6 on API 36 passed, including child-to-root no-history, nested and landscape Activity recreation; no dependency/future-feature expansion; Developer and independent QA verified; Lead accepted |
| V1-M2-01 | Done 2026-08-06 on `milestone/M2-celestial-archive-design-system`; FR-011, NFR-005/NFR-011, UI/THEME; exact 21-role light/dark semantic palette, approved aliases, fixed approved-color-only Material mappings and dynamic-color-off theme boundary; focused JVM 7/7 twice, full JVM 16/16, lint and both debug APK builds passed; minimum text contrast 6.68:1 and outline/focus 4.40:1; no dependency/later-feature change; Developer, Lead verification and independent QA passed |
| V1-M2-02 | Done 2026-08-06 at `f6fbe1c`; FR-011, NFR-005/NFR-011, UI/THEME; exact approved typography, spacing, gutter, shape, elevation, wash, motion, icon, illustration, focus and touch-target tokens; focused 8/8; no dependency or feature change |
| V1-M2-03 | Done 2026-08-06 at `a353c4f`; FR-001–003, FR-009–012, NFR-005; domain-neutral timing folio, anchors, provenance/status, timeline, actions and unavailable components with explicit semantics/non-colour cues; JVM 4/4 and connected component evidence passed |
| V1-M2-04 | Done 2026-08-06 at `55c5d77`; NFR-005/NFR-011, A11Y; eight deterministic synthetic adaptive/theme/state/reduced-motion preview fixtures; JVM 7/7 and connected preview evidence passed; complete M2 focused 26/26, JVM 35/35 and connected 13/13 on API 36; independent QA zero open findings |
| V1-M3-01 | Done 2026-08-26 at `3de1c15`, corrected through `045ed65`/`bca3f7f`; FR-001/003/009/011, UI-002; static production-unavailable Dashboard plus preview/test-only complete synthetic success, adaptive navigation and readable-width evidence; Developer and independent QA passed |
| V1-M3-02 | Done 2026-08-26 at `4cdb1b2`, corrected through `2bc95be`/`045ed65`; FR-002/003/008/011, UI-003; production-unavailable Timeline plus preview/test-only exactly 24 ordered rows, at most one Current, isolated scroll state and adaptive evidence; Developer and independent QA passed |
| V1-M3-03 | Done 2026-08-26 at `7686976`, corrected through `bca3f7f`; FR-004–007/009, UI-001/004/005; production-unavailable/read-only flows plus ten preview/test-only Location state presentations; no provider, permission or persistence; Developer and independent QA passed |
| V1-M3-04 | Done 2026-08-26 at `ec1d1df`, with approved Method-order correction `25756f9` and QA correction `bca3f7f`; FR-003/010/011, UI-006/007; seven-section Method, About/privacy, installed version and honest unavailable content; Developer and independent QA passed |
| V1-M4-04 | Implementation-complete, **In Progress — corrective QA**; FR-006/009, ADR-004, UI-004 exception; exact owner-approved GeoNames LK product subset, stable IDs, Colombo default, English-only resources and provenance in [evidence](V1-M4-04_TOWN_CATALOG_EVIDENCE.md); `TownCatalogTest` passes. Owner-approved town-only rows retain selection/provenance/status semantics for this frozen catalogue; no province/region data is invented. |
| V1-M4-05 | Implementation-complete, **In Progress — corrective QA**; FR-004–009, CR-009/010; application-root repository/provider composition, foreground-only precise/approximate permission path, nine-town offline search, Colombo fallback, lifecycle/permission reconciliation, recovery/reset and truthful privacy copy; repeat independent QA and Lead acceptance pending; connected execution remains subject to environment availability. |

Approved planning task IDs are traceability assignments, not automatic task-start authorization. V1-M1-01 through V1-M3-04 have been explicitly started and are Done. M1, M2, and M3 are explicitly Lead/owner accepted and incorporated into `dev`; accepted M3 history and evidence is recorded through `3d9d9d2`. V1-M4-01 through V1-M4-05 are implementation-complete and In Progress in corrective QA, not accepted or merged. Every later task remains unstarted (or Blocked where recorded) and requires separate explicit authorization. Deferred M9/release evidence remains mandatory under its approved task-specific ownership: V1-M9-01 accessibility/adaptive/localization/manual visual; V1-M9-02 API runtime and complete API/state regression; V1-M9-03 physical-family/OEM/performance/privacy; V1-M9-04 signing/reproducible APK/install-update-rollback/update lineage; V1-M9-05 final QA/family acceptance/release validation.
