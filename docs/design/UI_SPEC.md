# Version 1.0 UI Specification

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.0 |
| Last updated | 2026-08-06 |
| Owner role | Lead Coordinator; UI/UX Designer reviewed read-only |
| Approval state | DA-004 Approved through exact phrase `APPROVE VERSION 1.0 UI` received 2026-08-02 |

## Scope and implementation authority

This specification turns the approved Celestial Archive direction into the complete Version 1.0 UI contract. It covers Seasonal Planetary Hora, PA-004-approved calculated astronomical sunrise/sunset, location/settings/privacy flows, and typed unavailable states. Daytime Rahu is represented only by a conditional component contract and the explicit `Calculation method not approved` state until PA-003 approves CP-003/RK. Fixed Sri Lankan Kala Hora and Panchama Kala are Version 1.1 only and have no Version 1.0 route, card, timeline, setting, selector, preview, disabled control, live value, or teaser.

Approval of this document permits implementation only through an approved delivery milestone/task. It does not approve CP-003, CP-002, dependencies, implementation evidence, traditional validation, signing, or release.

## Navigation contract

Version 1.0 uses one activity and these routes:

| Route/UI ID | Role | Entry and Back behavior |
|---|---|---|
| `dashboard` / UI-002 | Primary top-level destination | App/default destination. Back exits the task. |
| `horaTimeline` / UI-003 | Top-level full 24-Hora timeline | Top-level navigation or dashboard action. Back returns Dashboard without building duplicate history. |
| `methodology` / UI-006 | Top-level methodology and diagnostics | Top-level navigation or result/source action. Back returns Dashboard when entered top-level, otherwise the origin. |
| `settings` / UI-005 | Focused preferences and recovery route | Entered from the Dashboard app-bar action or Method. Back returns the origin and restores focus. |
| `locationSelection` / UI-004 | Focused child route | Entered from Dashboard/Settings/first use. Back/cancel restores the origin and the last coherent selection. |
| `aboutPrivacy` / UI-007 | Settings child route | Back returns Settings and restores focus to `About and privacy`. |

Compact devices preserve the PA-002 three-destination architecture with labelled bottom destinations: `Dashboard`, `Timeline`, and `Method`. Medium/expanded devices use the same three labels in a navigation rail. Settings, Location, and About/privacy are required supporting routes reached from explicit actions and never occupy bottom/rail slots. This reconciles the six-route product inventory without changing the approved top-level architecture or adding a navigation dependency. Selecting an already active top-level route does not duplicate it. No public deep links are required. Opening Android permission or system Settings requires an explicit user action.

## Global shell and semantics

- Root content respects system bars, display cut-outs, IME, and edge-to-edge insets.
- Initial accessibility focus lands on the screen heading. Navigation follows content in traversal order.
- Every screen is vertically scrollable when height or font scale requires it; no required content or action depends on a fixed-height container.
- Top-level labels remain visible. Icons supplement text and never carry route/state meaning alone.
- A single immutable UI state renders each frame. Composables never calculate astronomy, Hora membership, fallbacks, or time-zone selection.
- A prior coherent result may remain during refresh only with its existing provenance. Mixed old/new location, solar, or Hora fields are forbidden.
- All strings are resources. V1.0 ships complete English; layouts remain pseudolocale-, expansion-, and reviewed-Sinhala-ready without inventing Sinhala copy.
- Decorative archive rules, stars, arcs, and gradients are hidden from accessibility services and never intercept input.

## UI-001 — First-use location introduction

- **Purpose:** explain why location changes calculated times and provide a usable non-coercive path.
- **Hierarchy:** heading; two-sentence rationale; privacy statement; `Use current location`; `Choose a town`; `Continue with Colombo default`; Method/About links.
- **Behavior:** permission is requested only after `Use current location`. Denial or cancellation keeps town/default choices. No result is shown before a complete context exists.
- **Accessibility:** one heading; each choice a separate 48dp target; privacy text precedes actions in TalkBack; no permission request on launch.
- **Previews:** compact/light; compact/dark/200%; landscape; permission denied return; reduced motion.

## UI-002 — Dashboard

### Content hierarchy

1. `Dashboard` heading and applicable Hora-day/date label.
2. Location/provenance row: location label, source (`Precise`, `Approximate`, `Saved device location`, `Manually selected`, or `Default location`), and age/staleness where applicable. Current/saved device sources offer `Refresh location`; manual/default sources offer `Use current location`; unavailable sources expose only their state-appropriate recovery action.
3. One concise warning/status banner when the location, clock, or retained result needs attention.
4. Dominant Seasonal Planetary Hora folio:
   - explicit `Seasonal Planetary Hora` label;
   - current ruler name;
   - start and end local times;
   - positive remaining duration;
   - next ruler and transition time;
   - `View all 24 Horas` action.
5. Calculated astronomical anchors: labelled sunrise and sunset, active local date/zone, and limitation/Method affordance.
6. Conditional Rahu slot. Before PA-003 it may show only `Rahu Kalaya isn’t available yet. Its calculation method has not been approved.` with Method access and no time/action that implies functionality.
7. Upcoming Hora preview.
8. `Last calculated …`, active zone, calculation/profile status, and Method link.

Coordinates, raw fingerprints, and iteration diagnostics do not appear on Dashboard. The display rounds shared solar/Hora boundaries once through `SOLAR-DISPLAY-001-v1.0`; exact instants control current/next membership and countdown.

### State behavior

| State | Dashboard behavior |
|---|---|
| Loading with no prior result | Heading/location shell plus progress text `Preparing calculation…`; no fake times, shimmer, or ruler placeholder. |
| Refresh/loading with prior result | Keep the coherent result; provenance row says `Finding current location…` or `Recalculating…`; offer Cancel when supported. |
| Success | Render the complete atomic location/solar/Hora bundle and calculation time. |
| Precise/approximate | Explicit text state. Approximate may include reported accuracy in detail, never as alarm or colour only. |
| Saved/manual/default | Name the active source and age/town/default; offer `Use current location` without repeated permission pressure. |
| Permission denied | Explain that current location is unavailable; retain/offer town/default and a user-triggered permission retry where valid. |
| Location services disabled | Text plus location-off icon; `Open settings` and `Choose a town`; no settings launch without tap. |
| Timeout/provider failure | End progress at 20 seconds; retain fallback/prior bundle with warning and Retry; ignore late callback. |
| Invalid location | Reject the value; show safe reason without echoing private coordinates; offer another source. |
| Solar unavailable / no sunrise / no sunset | No ruler, anchors, or countdown. Show typed plain-language reason, active location/provenance, `Choose location`, Retry where meaningful, and Method. |
| Unsupported date | State the supported `1900-01-01..2100-12-31` range and following-day limitation; no clamped result. |
| Stale calculation | Keep the last coherent bundle only when allowed; show `Previous calculation` plus calculation time and source; never label it current after its validity interval. |
| Offline | No special failure when saved/manual/default data is usable; optional `Offline` informational text only where it explains unavailable current-location refresh. |

### Semantics and layout

The Hora folio is one merged summary read as: `Seasonal Planetary Hora, [ruler], from [start] to [end], [remaining] remaining, next [ruler] at [time].` Actions are separate nodes. Countdown ticks are not a live region; a foreground Hora boundary may announce one concise transition. At 200% font, ruler, time pair, countdown, and next-Hora content stack in that order. No ellipsis may remove ruler/state/time meaning.

## UI-003 — Full Hora timeline

- Show the applicable Hora-day date range, location/source, calculated sunrise/sunset/following sunrise, and profile/engine link.
- Render exactly 24 chronological rows grouped under `Day — 12 Horas` and `Night — 12 Horas`.
- Each row shows ordinal, ruler, start, end, and textual `Current` when applicable. Current state also uses outline/weight/icon, never colour alone.
- `Go to current Hora` scrolls the current row into view without unexpectedly moving TalkBack focus. If no current row exists, explain why.
- TalkBack order is screen heading, context, Day heading/rows 1–12, Night heading/rows 13–24. Row phrase: `[Day/Night] Hora [ordinal], [ruler], [start] to [end], Current` when applicable.
- Compact uses full-width growing rows. Medium may align labels/times. Expanded may use a readable two-column Day/Night ledger only if traversal remains chronological Day then Night.
- Required boundary screenshot states: first Day row current, exact sunset/first Night row, final Night row, unavailable, 200% font, and landscape.

## UI-004 — Location selection and recovery

- Sections: `Use current location`; current acquisition/permission/services status; selected location; searchable/selectable approved Sri Lankan town list; `Colombo default` explanation.
- Town selection requires an explicit tap and publishes one atomic recalculation before returning. Search/filter never sends network traffic.
- Selected state uses text and platform selected semantics plus icon/outline. Town rows show town and province/region only; coordinates are not primary UI.
- Recovery actions are conditional: Retry after retryable failure, request permission after rationale, system Settings only for applicable denial/disabled states, town/default always available.
- Saved device location is not presented as a town. Stale/device accuracy is labelled plainly.
- Empty town-filter results state `No matching towns` and preserve the query/action; an unavailable catalogue keeps current/default paths.

## UI-005 — Settings

- **Location:** active mode/source summary; `Change location`; refresh behavior explanation.
- **Appearance:** read-only statement that Celestial Archive light/dark follows the device. Dynamic colour and user-selectable theme packs are outside V1.0.
- **Accessibility:** read-only statement that reduced motion follows the device animation setting; no app-specific motion or font-size preference is introduced.
- **Calculation:** active zone policy, last calculation/version summary, and links to Method/diagnostics. Solar constants and profiles are not user-editable.
- **Data/privacy:** what is stored; `Reset location data` with confirmation and the labelled Colombo-default consequence; link to About/privacy.
- A location-mode change commits only after a complete valid selection; reset follows ADR-005. No notification, advice, background-location, language, date-browsing, or sharing control is in V1.0.

## UI-006 — Methodology and diagnostics

Use selectable, offline, readable sections with a maximum text line width:

1. Seasonal Planetary Hora method and seven-ruler sequence.
2. Solar event convention, sea-level fixed policy, supported range, following-day requirement, atmosphere/terrain/height limitations, and 60-second validation tolerance.
3. Current context: display location/source, zone, civil date, calculation time, CP/CR/SOL-R/profile/engine/dataset/display-policy versions.
4. Diagnostics: anchor UTC/local values, iteration counts and safe numeric diagnostics where approved; private coordinates and fingerprints are not logged or shareable.
5. Validation and source roles: production versus independent NREL-SPA/pvlib evidence.
6. Daytime Rahu status, explicitly unapproved until PA-003. Fixed Kala/Panchama has no Version 1.0 runtime content.
7. Privacy and About links.

Methodology must say `calculated astronomical sunrise/sunset`, not observed/exact/certified or physically millisecond-accurate. Technical values remain copyable only if the copy action and private-data handling are separately approved; V1.0 has no bulk export/share action.

## UI-007 — About and privacy

- App name/version/build identity and family-only purpose.
- Neutral timing-only scope; no predictions, advice, auspiciousness claims, ads, analytics, account, backend, or required network.
- Foreground location rationale; precise/approximate behavior; saved fields; backup/device-transfer exclusion; clear-data route.
- Source/licence/attribution summaries and full notices route where required.
- Model limitations and emergency/non-authoritative disclaimer in plain language.
- Signing/contact/update instructions remain release-owned content and may not expose credentials.

## Conditional daytime Rahu component

The component is structurally specified for future activation but remains non-functional while PA-003 is Blocked. Approved future states are `Active now`, `Later today`, `Completed today`, `Calculating`, `Location unavailable`, `Solar unavailable`, and `Calculation method not approved`. Valid states show start/end and next relevant transition. Pending/unavailable states show no guessed or previous time unless explicitly labelled `Previous calculation` with provenance. Rahu never joins the seven-ruler sequence or invalidates a coherent Seasonal Planetary Hora.

## Adaptive layout contract

| Width/state | Required behavior |
|---|---|
| Compact `<600dp` | One content column; three-item labelled bottom navigation; 16dp horizontal gutter; cards/rows grow vertically; all screens scroll. |
| Medium `600–839dp` | Labelled rail; centered readable column up to 720dp or aligned timeline columns; no gratuitous empty side pane. |
| Expanded `>=840dp` | Labelled rail; Dashboard may use summary plus upcoming/anchors panes, but location/warnings precede both semantically; Method max line width 720dp. |
| Compact landscape | Scrollable single column; bottom navigation remains reachable; no content hidden by system bars/IME. |
| Font scale 2.0 | Stack label/value and time pairs, full-width actions when needed, remove ornament before reducing spacing, no horizontal scroll for primary content. |

## Motion and reduced motion

Only short state-confirming transitions are allowed: screen/content fade, pressed state, disclosure expansion, and a single boundary emphasis. No moving star field, shimmer, pulsing/glow loop, number roll, parallax, orbit, or animated background. System `TRANSITION_ANIMATION_SCALE=0` and the effective reduced-motion preference make nonessential transitions immediate; focus, text, outline, and optional single boundary announcement preserve meaning.

## Required implementation evidence

### Compose previews and screenshots

- UI-001–UI-007 in light and dark.
- Compact 320dp portrait, compact landscape, medium, and expanded.
- Font scale 2.0 with pseudolocale/long English strings.
- Dashboard loading, success, precise, approximate, saved, manual, default, denied, services-disabled, timeout, invalid, solar-unavailable, unsupported-date, stale, and offline states.
- Timeline first/day-night/final boundary and unavailable states.
- Location acquisition/recovery/selected/empty-filter states.
- Reduced-motion end states and one future theme-token substitution proving no feature hard-coding.
- Rahu profile-not-approved only; functional Rahu screenshots wait for PA-003.

Screenshot tests use deterministic clocks, public/synthetic coordinates, fixed zone/profile versions, and no private family data. Golden images are review evidence, not calculation or accessibility proof.

### Accessibility and manual evidence

- Automated semantics assertions for headings, selected/current states, labels, action separation, traversal order, and hidden decoration.
- Manual TalkBack reading-order record for every route and all recovery/unavailable states.
- Minimum interactive target `48dp × 48dp`, including icon actions.
- WCAG/Material contrast: normal text at least 4.5:1; large text, icons, focus indicators, outlines, and other meaningful non-text at least 3:1.
- Keyboard/D-pad traversal follows visual order and exposes every action without gesture-only interaction.
- No clipping, lost action, obscured focus, or essential truncation at 200% font or compact landscape.
- Light/dark semantic equivalence and reduced-motion verification.

## UI approval checklist

- [x] Six required routes and Back/focus behavior specified.
- [x] Dashboard content, precision, provenance, loading, and unavailable behavior specified.
- [x] Celestial Archive semantic theme contract linked.
- [x] Compact/landscape/large-font/light/dark/reduced-motion evidence defined.
- [x] TalkBack order, touch targets, contrast, and non-colour meaning defined.
- [x] Rahu remains conditional on PA-003; Kala/Panchama remains Version 1.1.
- [x] Independent final QA/recheck recorded in delivery status with zero open findings.
- [x] Repository owner supplied `APPROVE VERSION 1.0 UI` on 2026-08-02.

DA-004 and DP-001 were approved before coding began. The owner subsequently started and accepted the M1 tasks: a bounded single-activity shell now provides Dashboard, Timeline, and Method top-level destinations plus Location, Settings, and About children using rendering-only placeholders. M1 is Done and incorporated into `dev` through `79d5116`; final Celestial Archive tokens/layouts, adaptive/accessibility/localization/visual evidence, feature content, and every M2 or later task remain unstarted and separately gated.
