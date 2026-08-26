# User Stories

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.1 |
| Last updated | 2026-08-02 |
| Owner role | Business Analyst |
| Approval state | US-001–US-010 approved under PA-001; US-011–US-013 approved under CR-001 with separate domain/design implementation gates |

| ID | Version | User story | Acceptance summary | Requirement references | State |
|---|---|---|---|---|---|
| US-001 | 1.0 | As an older family user, I want the current and next Hora at a glance so I do not need to interpret a complex chart. | Current ruler, start, end, countdown, and next ruler form one coherent accessible result. | FR-001, NFR-005 | Approved |
| US-002 | 1.0 | As a user planning my day, I want all 24 Hora intervals with sunrise and sunset anchors. | Exactly 12 day and 12 night intervals cover `[sunrise, nextSunrise)` without gaps/overlaps. | FR-002, FR-003 | Approved |
| US-003 | 1.0 | As a user at my current place, I want a one-time foreground location fix so calculations reflect where I am. | A precise or approximate fix is obtained only in context, can be cancelled/timed out, and never starts tracking. | FR-004, FR-005 | Approved |
| US-004 | 1.0 | As a privacy-conscious user, I want the app to work after denying location and never track or upload me. | Denial uses saved/manual/default fallback; no background permission, history, analytics, network upload, or backup of coordinates. | FR-005–FR-007, NFR-004, NFR-007, NFR-008 | Approved |
| US-005 | 1.0 | As a user without a device fix, I want to select a Sri Lankan town or use a clearly labeled fallback. | Explicit manual selection remains active until changed; all fallback provenance is visible. | FR-006, FR-009 | Approved |
| US-006 | 1.0 | As a returning user, I want immediate coherent saved-location results while an optional refresh occurs. | Launch restores one complete result; refresh swaps atomically or retains the prior result with a warning. | FR-007, FR-008 | Approved |
| US-007 | 1.0 | As a user checking trustworthiness, I want the method, active location, time zone, sources, and limitations explained. | Methodology discloses profile/engine versions, assumptions, model limitations, and active provenance. | FR-009, FR-010 | Approved |
| US-008 | 1.0 | As a low-vision or TalkBack user, I want equivalent access to the dashboard, timeline, location flow, and methodology. | 200% font, logical semantics, minimum targets, adequate contrast, and non-colour cues pass verification. | FR-001–FR-011, NFR-005 | Approved |
| US-009 | 1.0 | As a family user, I want neutral timing information without unsupported predictions or advice. | Prohibited advice and auspiciousness content is absent. | FR-010, FR-011 | Approved |
| US-010 | 1.0 | As a maintainer, I want deterministic, versioned, independently validated results. | Inputs/profile/engine reproduce exact internal results and trace to validation evidence. | NFR-001–NFR-003, NFR-012–NFR-014 | Approved |
| US-011 | 1.0 | As a family user, I want today's daytime Rahu Kalaya start, end, and current/upcoming status without losing the primary seasonal-Hora answer. | Rahu uses a separately identified approved profile, local solar anchors and textual status; it is not presented as an eighth ruler or as advice. | FR-012, NFR-015 | Approved under CR-001; implementation blocked on CP-003/RK |
| US-012 | 1.1 | As a family user, I want the current Sri Lankan main Kala and Panchama using familiar reviewed names. | One coherent result shows parent Kala, current Panchama, boundaries, countdown, next subdivision, and validation provenance. | FR-013–FR-015, NFR-015 | Approved under CR-001; implementation blocked on CP-002 |
| US-013 | 1.1 | As an older or TalkBack user, I want an expandable Kala/Panchama timeline that is clearly distinct from seasonal Hora and Rahu. | Grouped parent/subdivision chronology works at 200% font, uses no colour-only meaning, and never interleaves systems. | FR-015, NFR-005, NFR-015 | Approved under CR-001; implementation blocked on CP-002 and design approval |

## Later-story boundary

Nighttime Rahu, other Panchanga expansion, final Sinhala translation, advice, theme packs, widgets, notifications, date browsing, personal profiles, birth charts, compatibility, and predictions require later requirements and approval; none is implied by these stories. Approval of US-011–US-013 does not approve their calculation profiles, rules, design, or implementation.
