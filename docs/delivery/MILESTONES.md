# Version 1.0 Milestones

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.0 |
| Last updated | 2026-08-06 |
| Owner role | Lead Coordinator; Project Manager read-only reviewer |
| Approval state | DP-001 Approved through exact phrase `APPROVE DELIVERY PLAN` received 2026-08-02; milestone/task internal gates remain |

## Completed foundation

`M-000 — Controlled delivery foundation` is Done. It established the team, repository instructions, documentation structure, baseline verification, and approved documentation checkpoints without changing application behavior.

## Version 1.0 plan

| Milestone | State | Outcome | Tasks | Model recommendation | Entry gate | Exit gate |
|---|---|---|---:|---|---|---|
| M1 — Android foundation and quality tooling | Done | Normalize the existing project, resolve identity, approved dependency evidence, deterministic test infrastructure, single-activity shell, bounded six-route/three-top-level navigation | 5 | Medium | Entry gate passed; V1-M1-01 through V1-M1-05 Done | Passed; corrective commits `580bab1`/`79d5116`, final independent QA, Lead/owner acceptance, and fast-forward into `dev` complete |
| M2 — Celestial Archive design system | Proposed | Semantic light/dark theme, type, spacing, shape, elevation, motion, reusable accessible components, deterministic previews | 4 | Medium | DA-004 Approved; accepted M1 foundation where used | Token/component/previews and accessibility review; no hard-coded feature values |
| M3 — Static dashboard and navigation | Proposed | Fixed-state Dashboard, 24-Hora timeline, location/settings/method/about shells, complete loading/error matrix | 4 | Medium | Accepted M1 and M2 | State matrix, semantics, adaptive previews, Developer verification, QA, Lead acceptance |
| M4 — Location, settings and persistence foundation | Proposed | Pure location policy, one-shot foreground provider, manual/default flows, no-backup DataStore, lifecycle/time seams and fakes | 5 | Medium; High privacy/lifecycle review | DA-004 + DP-001 Approved; M1 integration baseline; exact town-data task remains Blocked | Threshold/provider/persistence/backup/lifecycle evidence and QA |
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

M1 task execution, both bounded merge-readiness corrections, final independent QA, and Lead/owner acceptance are complete. `dev` was fast-forwarded from the retained `milestone/M1-android-foundation` branch through accepted commit `79d5116`, including corrective commits `580bab1` and `79d5116`; no remote push occurred. M1 is Done. M2 and all later milestone tasks remain Proposed or Blocked and have not started. API-26 runtime, physical-family-device, full operating-system process-death, final accessibility/adaptive/localization/visual, signing, and release evidence remain deferred to their assigned gates.
