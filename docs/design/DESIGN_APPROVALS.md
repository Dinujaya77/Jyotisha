# Design Approvals

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.0 |
| Last updated | 2026-08-02 |
| Owner role | Lead Coordinator |
| Approval state | DA-001 Direction A, DA-004 final Version 1.0 UI, and DA-004-A frozen nine-town UI-004 exception Approved; DA-002 and DA-003 are superseded alternatives |

| ID | Date | Artifact/version | Decision | Approver | Conditions/evidence | Supersedes |
|---|---|---|---|---|---|---|
| DA-001 | 2026-08-02 | Direction A — Celestial Archive v0.4 | Approved; exact phrase `APPROVE DESIGN DIRECTION A` received 2026-08-02 | Repository owner | Option C narrative selected; final UI/tokens/evidence deferred | — |
| DA-002 | 2026-08-02 | Direction B — Astral Observatory v0.4 | Superseded for Version 1.0 direction selection by DA-001 | Repository owner | Preserved as an unselected alternative; not approved | DA-001 selection |
| DA-003 | 2026-08-02 | Direction C — Moonlit Sanctuary v0.4 | Superseded for Version 1.0 direction selection by DA-001 | Repository owner | Preserved as an unselected alternative; not approved | DA-001 selection |
| DA-004 | 2026-08-02 | `UI-V1.0-001`, `THEME-CELESTIAL-ARCHIVE-001`, and `A11Y-V1.0-001` v1.0 | Approved; exact phrase `APPROVE VERSION 1.0 UI` received 2026-08-02 | Repository owner | Read-only UI/UX and architecture reviews complete; independent final QA/recheck recorded zero open findings; DP-001 approved separately | — |
| DA-004-A | 2026-08-29 | UI-004 V1 frozen nine-town catalogue exception | Approved owner amendment | Repository owner | Town-only rows are permitted only for the approved nine-town catalogue: its schema has no approved province/region field. Preserve text, selected/radio semantics, icon/outline, and provenance/status; do not invent region data. A separately approved schema expansion may restore region text. Historical DA-004 wording remains historical. | DA-004 UI-004 row-detail condition only |

DA-001 selects the visual narrative. DA-004 approves the final Version 1.0 UI unit in `UI_SPEC.md`, `THEME_SPEC.md`, `ACCESSIBILITY.md`, and the reconciled IA. The exact approval phrase was explicitly received on 2026-08-02; no approval was inferred from the earlier candidate or QA result.

Neither DA-001 nor DA-004 approves CP-003/RK, CP-002/KH/PK, dependency addition, Android implementation, evidence not yet produced, signing, or release. Architecture remains separately approved under PA-002 and delivery under DP-001. Coding still requires an explicitly started task.
