# Information Architecture

| Field | Value |
|---|---|
| Status | Ready |
| Version | 0.5 |
| Last updated | 2026-08-02 |
| Owner role | UI/UX Designer |
| Approval state | PA-002 architecture and DA-001 Direction A approved; detailed IA/component decisions remain part of the later UI specification gate |

## Navigation model

Version 1.0 uses one activity and three stable destinations:

1. **Now** — default dashboard and primary answer.
2. **Day** — complete chronological timeline for the approved primary timing system.
3. **Method** — methodology, sources, privacy, limitations, validation state, and detailed per-system provenance.

Location selection is a focused child flow entered from the always-visible location action on Now. It is not a fourth top-level destination because it supports the calculation rather than representing recurring content. Compact devices use labeled bottom navigation; medium/expanded devices may use a labeled navigation rail. Expanded layouts may place the Now summary beside upcoming intervals, while preserving compact semantic order.

No public deep links are proposed. System Back from a child flow returns to its origin without discarding the last valid result. Back from Day or Method returns to Now, preserving destination state and restoring focus to the action that opened the destination where practical; selecting top-level destinations does not build an unbounded history. Back from Now exits the task normally. Cancelling location acquisition retains the prior coherent result or a clearly labelled fallback. Platform permission and Settings screens open only after an explicit user action.

## Screen inventory

| IA ID | Destination/state | User goal | Main content/actions | Requirement/story references |
|---|---|---|---|---|
| IA-001 | First-use location introduction | Understand why location helps and continue without coercion | Plain-language rationale; `Use current location`; `Choose a town`; `Continue with Colombo default`; privacy summary | FR-004–FR-006, FR-010; US-003–US-005 |
| IA-002 | Now dashboard | Identify the current Seasonal Planetary Hora, its end, and what follows | Location/provenance; dominant Seasonal Planetary Hora; remaining time; start/end; next Hora; calculated sunrise/sunset; subordinate Rahu state when approved; warning and refresh/use-current action; Day link | FR-001, FR-003, FR-007–FR-009, FR-011, FR-012; US-001, US-006–US-011 |
| IA-003 | Day timeline | Inspect the complete Hora day | Hora-day date range; calculated anchors; 12 Day and 12 Night rows; textual current marker; return-to-current action | FR-002, FR-003, FR-008, FR-011; US-002, US-008 |
| IA-004 | Location selection | Choose current device or bundled Sri Lankan town | Current-location state/action; active selection; town list; Colombo default; concise precision/freshness labels | FR-004–FR-007, FR-009; US-003–US-006 |
| IA-005 | Location rationale/recovery | Recover from denied, unavailable, disabled, timeout, null, or failed retrieval | Explanation; retry where appropriate; town/default/retained fallback; optional Settings action | FR-004, FR-005, FR-007, FR-009; US-003, US-004 |
| IA-006 | Method | Judge how the result was produced | Seasonal-Hora method; ruler sequence; boundaries; solar convention/limits; location/zone/profile/engine; privacy; sources; neutral timing-only scope | FR-003, FR-008–FR-011; US-007–US-010 |
| IA-007 | Solar result unavailable | Understand why no trustworthy result can be shown | No fabricated Hora; safe reason; active location/method details; retry/reselect; retain earlier result only where CR-010 permits | FR-001, FR-003, FR-007–FR-010; US-006, US-007 |
| IA-008 | V1.0 Rahu status/detail | Understand the separately calculated daytime Rahu state without weakening the primary answer | Explicit Rahu state, approved profile/provenance, start/end and next transition when valid, typed unavailable reason, recovery/Method action | FR-012, NFR-015; US-011 | Approved requirement; implementation blocked on CP-003/RK and UI approval |
| IA-009 | Future V1.1 Kala/Panchama concept | Explain the approved future allocation without presenting a working feature | Clearly labelled non-functional concept and Method link only; no live ruler, time, timeline, selector, or enabled action | FR-013–FR-015, NFR-015; US-012, US-013 | Future concept; implementation Blocked on CP-002/KH/PK and later UI approval |

## Now hierarchy

The visual and semantic order is:

1. Screen heading and applicable Hora-day date.
2. Location/provenance row with its required action.
3. Concise stale, fallback, clock, or refresh-failure notice when applicable.
4. Dominant **Seasonal Planetary Hora** group: textual label, ruler name, remaining duration, local start/end, and explicit next ruler.
5. Calculated astronomical sunrise and sunset anchors.
6. Subordinate Rahu status after CP-003/RK approval; otherwise the explicit profile-not-approved state. Rahu never displaces or invalidates a valid Seasonal Planetary Hora.
7. Upcoming-Hora preview and `View all 24 Horas`.
8. Method/profile link.

Coordinates stay out of the primary dashboard. Retrieval progress or a recoverable warning must not blank or partially replace a still-valid calculation.

## Approved Option C hierarchy

Approved Option C preserves Seasonal Planetary Hora as the Version 1.0 primary hierarchy and adds Rahu after the solar anchors only after CP-003/RK approval. Its explicit states are `Active now`, `Later today`, `Completed today`, `Calculating Rahu Kalaya…`, `Unavailable — location unavailable`, `Unavailable — calculated sunrise or sunset unavailable`, and `Calculation method not approved`. Valid states show start/end and the next relevant transition with text plus icon/outline/weight; invalid/pending states show no guessed or mixed-context time. Rahu never appears in the seven-ruler list. The Seasonal Planetary Hora timeline remains Day; fixed Kala/Panchama is a non-functional Version 1.1 concept.

Only after CP-002/KH/PK and a later Version 1.1 UI approval may a functional Kala/Panchama experience use this future order:

1. Heading and applicable civil date.
2. Location/provenance and active zone.
3. Stale/fallback/clock/validation warning.
4. One dominant **Current Kala** card containing main ruler/start/end and a nested **Current Panchama** with sub-ruler/start/end/countdown.
5. Explicit next Panchama and transition time within the same card.
6. Subordinate Rahu status with start/end and next transition.
7. Calculated astronomical sunrise and sunset.
8. `View full Kala and Panchama timeline`.
9. Clearly labelled secondary Seasonal Planetary Hora/Method action.

This future order is not a Version 1.0 screen or navigation destination. Do not create four equally dominant cards. Main Kala and Panchama are a parent/child result and publish atomically; Rahu is a separate concurrent interval; Seasonal Planetary Hora is a distinct system.

The future Day view groups each approved main Kala as a disclosure heading containing its approved five Panchamas. Initially expand the current parent. `Go to current` scrolls without unexpectedly moving TalkBack focus. Do not hard-code 24 parents/120 subdivisions while KH-005 is unresolved. Day/Night headings appear only if CP-002 approves that distinction. Option A would require a clearly labelled system selector and two non-interleaved timelines; it is not recommended.

The future combined card and timeline must support reviewed English/Sinhala primary names, wrapping rather than truncation, compact-phone single-column flow, 200% font, reduced motion, and no fixed height. Secondary aliases belong in Method rather than every row. At a Panchama boundary announce once; at a coincident main/Panchama boundary combine the announcement. Countdown ticks are not live regions.

Per-system validity remains independent: an unapproved rule shows no live authoritative time; missing inputs identify the affected system; a prior coherent snapshot may remain only when explicitly labelled previous with its provenance; one valid system is not relabelled unavailable because another failed. Rahu pending/location/solar/profile states retain the independently valid Seasonal Planetary Hora and keep recovery plus Method accessible.

## Key flows

### First use

`Introduction → Use current location → rationale → platform permission → retrieval → atomic result`

`Choose a town` and `Continue with Colombo default` remain available before a device result. Denial never strands the user.

### Refresh current location

`Now → Refresh location → in-place progress → precise/approximate result OR retained result + warning`

The current snapshot remains visible. Cancellation or timeout ends progress; a late callback has no visible effect.

### Select a town

`Now → Location selection → town → explicit selection → atomic recalculation → Now`

The selected town remains active until another town or `Use current location` is chosen.

### Recover location

`Status → Retry, Choose a town, Open Settings when meaningful, or Continue with fallback`

Manual/default choices are never visually subordinated to repeated permission pressure.

### Inspect Day and Method

Now → Day initially positions the current interval visibly without unexpectedly moving accessibility focus. Now/Day → Method presents structured, selectable offline content and exact provenance without advice or promotional content.

## Focus and semantics

- Initial focus lands on the screen heading, not navigation or the changing countdown.
- The current-Hora card is one coherent summary; actions remain separately operable.
- The visual countdown is not a live region and does not announce every tick. Refocus reads the current value; a boundary may produce one concise announcement.
- Day traversal is heading → Day group → rows 1–12 → Night group → rows 13–24. Each row announces ordinal/group, ruler, start, end, and `Current` where applicable.
- A future approved Kala/Panchama timeline traverses parent heading then its subdivisions; each parent announces expansion state/count, and each subdivision announces ordinal, parent, sub-ruler, start/end, and current state.
- Status text and recovery action are separate semantic nodes.
- Location selection uses text and selected semantics, not checkmark or colour alone.
- Decorative stars, arcs, rules, charts, and ornaments are excluded from the accessibility tree.
- Touch targets are at least 48dp. Keyboard/D-pad order matches visual reading order and does not require gestures.

## Adaptive, localization, and large-text behavior

- At 200% font, content reflows vertically without fixed-height clipping; time pairs may stack.
- Navigation labels remain visible; icons never carry top-level meaning alone.
- Timeline rows expand rather than truncating ruler, time, or status.
- Compact landscape remains scrollable and does not hide actions behind system UI or a keyboard.
- Medium width may use a wider single column. Expanded width may use two panes with location/warnings preceding or spanning both.
- Method prose has a readable maximum line width.
- English strings are externalized; layout is Sinhala-ready and must later pass pseudolocale expansion and RTL resilience checks even though Sinhala content is not in V1.0.

## Deferred to approved UI specification

Exact components, copy, breakpoints, measurements, typography, colors, shapes, motion, town-picker behavior, Settings conditions, previews, and accessibility evidence remain unapproved. A detailed `UI_SPEC.md` may be prepared only after explicit authorization; PA-002 and DA-001 prerequisites are satisfied, while approved terminology and CP-003/RK evidence remain necessary before Rahu can function.
