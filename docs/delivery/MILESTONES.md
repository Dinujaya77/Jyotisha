# Version 1.0 Milestones

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.0 |
| Last updated | 2026-08-26 |
| Owner role | Lead Coordinator; Project Manager read-only reviewer |
| Approval state | DP-001 Approved through exact phrase `APPROVE DELIVERY PLAN` received 2026-08-02; milestone/task internal gates remain |

## Completed foundation

`M-000 — Controlled delivery foundation` is Done. It established the team, repository instructions, documentation structure, baseline verification, and approved documentation checkpoints without changing application behavior.

## Version 1.0 plan

| Milestone | State | Outcome | Tasks | Model recommendation | Entry gate | Exit gate |
|---|---|---|---:|---|---|---|
| M1 — Android foundation and quality tooling | Done | Normalize the existing project, resolve identity, approved dependency evidence, deterministic test infrastructure, single-activity shell, bounded six-route/three-top-level navigation | 5 | Medium | Entry gate passed; V1-M1-01 through V1-M1-05 Done | Passed; corrective commits `580bab1`/`79d5116`, final independent QA, Lead/owner acceptance, and fast-forward into `dev` complete |
| M2 — Celestial Archive design system | Done | Semantic light/dark theme, type, spacing, shape, elevation, motion, reusable accessible components, deterministic previews | 4 | Medium | DA-004 Approved; accepted M1 foundation; V1-M2-01 through V1-M2-04 Done; explicit Lead/owner acceptance | Implementation, reconciliation through `7e0db33`, independent QA, explicit acceptance, and fast-forward into `dev` complete; not pushed |
| M3 — Static dashboard and navigation | Done | Fixed-state Dashboard, 24-Hora timeline, location/settings/method/about shells, complete loading/error matrix | 4 | Medium | Passed: accepted M1 and M2; V1-M3-01 through V1-M3-04 Done | Implementation, corrections through `bca3f7f`, Developer verification, independent QA, explicit Lead/owner acceptance, and fast-forward integration into `dev` complete |
| M4 — Location, settings and persistence foundation | In Progress | Corrective QA pass for pure location policy, one-shot foreground provider, manual/default flows, no-backup DataStore, lifecycle/time seams and fakes | 5 | Medium; High privacy/lifecycle review | DA-004 + DP-001 Approved; implementation-complete V1-M4-01–05; province/region data/schema clarification pending for UI-004 | Repeat independent QA and Lead acceptance; M4 is not accepted or merged |
| M5 — SOLAR-001 implementation | Proposed | Pure Kotlin approved solar engine, typed outcomes, exact intermediates, frozen goldens, provenance, coordinator/cache boundary | 5 | High | PA-004 + DP-001 Approved; accepted M1 quality baseline | 105 intermediate literals, 30 anchors within 60s, deterministic/boundary/performance evidence, QA, Lead acceptance |
| M6 — Seasonal Planetary Hora engine | Proposed | Exact 12 day + 12 night schedule, rulers, `[start,end)`, current/next/countdown and provenance | 4 | High | CP-001 Approved; accepted M5 | Invariants, transition/golden-derived tests, shared-anchor proof, QA, Lead acceptance |
| M7 — Live dashboard integration | Proposed | Immutable StateFlow UI, atomic location/solar/Hora publication, countdown/scheduling, lifecycle/date/zone recovery | 4 | Medium; High time-boundary review | Accepted M3–M6 | Full live state/navigation matrix, transition/lifecycle evidence, QA, Lead acceptance |
| M8 — Daytime Rahu | Blocked | Approve then implement the separate daytime Rahu profile and subordinate UI | 3 | High | PA-003/CP-003/RK approval; accepted shared context | Traditional/source goldens, engine/UI evidence, QA, Lead acceptance |
| M9 — Accessibility, optimization and family release | Proposed | Accessibility closure, regression, physical-device/performance/privacy, signed APK/update/guide, release acceptance | 5 | Medium; High signing and final-calculation review | All enabled V1 milestones; if Rahu remains unavailable, explicit owner deferral from V1.0 | Independent solar/traditional evidence as applicable, devices, accessibility, signing/install/update, QA, explicit release approval |

Total planned Version 1.0 tasks: **39**.

### Delivery approval record

| ID | Approval unit | Decision | Required phrase |
|---|---|---|---|
| DP-001 | `MILESTONES.md` M1–M9, `TASKS.md` V1-M1-01–V1-M9-05, linked trace/test/branch gates, v1.0 | Approved 2026-08-02; exact phrase explicitly received | `APPROVE DELIVERY PLAN` |

DA-004 and DP-001 are Approved. M1–M4 are eligible to begin through explicitly assigned non-blocked tasks and do not depend on CP-003; their ordering and internal blockers still apply. M5 depends on PA-004-approved SOLAR-001 and the accepted M1 quality baseline. M6 depends on completed/accepted M5 and approved CP-001. M8 remains Blocked by PA-003/CP-003. M9 must not silently omit the approved Option C Rahu allocation: M8 must be accepted or the repository owner must explicitly defer daytime Rahu from the release.

## Branch and integration policy

- `main` holds stable, Lead-accepted family releases; no direct feature work.
- `dev` holds approved integrated development; only explicitly accepted milestone merges.
- One `milestone/<id>-<name>` branch per implementation milestone, created from clean `dev`; no branch per small task.
- One write-enabled Android Developer owns overlapping production/Gradle paths. Task commits include task IDs.
- Run focused checks during tasks and full unit/lint/debug verification at milestone exit. Then stop for independent QA, Lead review, and explicit user authorization before merge into `dev` or any push.
- Do not create an M8 branch while PA-003 is Blocked.

M1, M2, and M3 are Done, explicitly Lead/owner accepted, and incorporated into `dev`. V1-M3-01 through V1-M3-04 passed Developer verification and final independent QA, including corrections `2bc95be`, `045ed65`, and `bca3f7f`; accepted M3 implementation and review history is incorporated through `3d9d9d2`. M4 is In Progress in corrective QA and is not accepted or merged; M5 and every later milestone remain unstarted (or Blocked where recorded). Deferred evidence remains mandatory under its approved ownership: V1-M9-01 accessibility/adaptive/localization/manual visual; V1-M9-02 API runtime and complete API/state regression; V1-M9-03 physical-family/OEM/performance/privacy; V1-M9-04 signing/reproducible APK/install-update-rollback/update lineage; V1-M9-05 final QA/family acceptance/release validation.
