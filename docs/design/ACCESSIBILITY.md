# Version 1.0 Accessibility Specification

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.0 |
| Last updated | 2026-08-02 |
| Owner role | Lead Coordinator; UI/UX Designer read-only reviewer |
| Approval state | Included in DA-004; Approved on 2026-08-02 through `APPROVE VERSION 1.0 UI` |

## Global acceptance contract

- Minimum interactive target is 48dp by 48dp; adjacent compact actions should have at least 8dp separation where practical.
- Normal text contrast is at least 4.5:1. Large text and meaningful icons, outlines, focus indicators, and component boundaries are at least 3:1.
- State always uses explicit text plus icon, outline, weight, or selected semantics; color is never the only signal.
- Font scale 2.0 must not clip, overlap, hide actions, ellipsize essential ruler/state/time text, or require horizontal scrolling for primary content.
- Initial focus is the screen heading. D-pad/keyboard order matches visual and semantic order and exposes every action without a gesture.
- Decorative stars, arcs, gradients, horizon rules, and linework have no accessibility semantics.
- Visual time abbreviations expose full localized spoken phrases. English is externalized and tested with pseudolocale/long-copy fixtures; no unreviewed Sinhala copy is invented.
- Reduced motion makes ornamental/content transitions immediate while retaining focus, text, outline, progress, and state meaning.

## Screen reading and focus order

| UI ID | Required reading/focus order |
|---|---|
| UI-001 First use | Heading; rationale; privacy statement; current-location action; town action; Colombo-default action; Method/About links. Permission is requested only after the current-location action. |
| UI-002 Dashboard | Heading/date; location/provenance and refresh; warning; merged Seasonal Hora summary; separate timeline action; solar anchors; CP-003-gated Rahu note; upcoming Horas; calculation time; Method. |
| UI-003 Timeline | Heading/context; Go to current; Day heading and rows 1–12; Night heading and rows 13–24; Method. Each row speaks group, ordinal, ruler, start, end, and Current when applicable. |
| UI-004 Location | Heading/privacy; device-location status/actions; active selection; town-list heading/filter; selectable town rows; Colombo default. Selection exposes selectable-group and selected semantics. |
| UI-005 Settings | Heading; active location/change/reset; device appearance statement; device reduced-motion statement; Method; About/privacy. Confirmation focus begins on its heading and returns to the invoking action. |
| UI-006 Method | Heading; structured section headings; calculation context; assumptions/limitations; profile/engine/source details; privacy; CP-003-gated Rahu status. Disclosures announce expanded/collapsed state. |
| UI-007 About/privacy | Heading; purpose/version; offline/privacy; stored-location/reset path; backup exclusion; attribution; Method link. Back restores focus to the opening action. |

Location/About child routes return focus to the action that opened them where practical. Platform permission and Settings screens are opened only after explicit action. `Go to current` may scroll visually but must not unexpectedly move TalkBack focus.

## Dynamic values and announcements

The Dashboard Hora folio is one coherent summary; its actions are separate nodes. The countdown is not a live region and never announces ticks. On an exact foreground boundary, at most one polite announcement is permitted: `Hora changed to [ruler]. Ends at [time].` Do not announce a boundary on initial load or resume. If a future approved Rahu transition coincides, combine it with the Hora announcement rather than issue two announcements.

Loading uses plain progress text and a progress semantic without shimmer. Refresh retains the prior coherent snapshot and labels progress. Solar-unavailable UI has no placeholder ruler/time/countdown that could be mistaken for a calculation. Status text and each recovery action remain separate nodes.

Before PA-003, the Rahu slot speaks only that its calculation method is not approved; it exposes no time, countdown, urgency, toggle, or functional-looking disabled action. After PA-003/M8, valid states speak start/end and next transition; pending/unavailable states speak no guessed time.

## Adaptive and large-text behavior

- At font scale 2.0, time pairs and label/value groups stack, cards/rows grow, actions may become full width, and ornament disappears before content.
- Timeline rows change from aligned columns to vertical blocks when needed.
- Compact landscape remains vertically scrollable; navigation and focused content are not obscured by system bars, cut-outs, or keyboard.
- Medium/expanded visual columns preserve compact semantic order.
- Method/About prose uses a readable maximum line width.

## Automated evidence matrix

| Evidence ID | Required assertion |
|---|---|
| A11Y-AUTO-001 | Every route has one discoverable heading; navigation exposes role, label, and selected state. |
| A11Y-AUTO-002 | Hora folio summary is merged deliberately; refresh/timeline/Method actions remain separately operable. |
| A11Y-AUTO-003 | Timeline has 24 ordered rows, Day then Night, and exactly one Current row when a valid current interval exists. |
| A11Y-AUTO-004 | Location is a selectable group with selected semantics and non-color cues; recovery actions match state. |
| A11Y-AUTO-005 | Countdown is not a live region; decorative nodes are absent from the tree. |
| A11Y-AUTO-006 | Required controls meet 48dp targets and D-pad/keyboard traversal has no trap. |
| A11Y-AUTO-007 | Loading/unavailable/CP-003-blocked states expose no fabricated time semantics. |
| A11Y-AUTO-008 | Light/dark/reduced-motion/alternate-token states retain identical labels and actions. |

## Preview and manual evidence matrix

| Evidence ID | Required evidence before release |
|---|---|
| A11Y-MAN-001 | TalkBack traversal for every route and all denied/disabled/timeout/invalid/solar-unavailable states on API 26 and the newest target API. |
| A11Y-MAN-002 | Font scale 2.0 on 320dp portrait and 640dp landscape; no clipping, essential truncation, overlap, hidden focus, or lost action. |
| A11Y-MAN-003 | Light/dark contrast report for text, icon, outline, focus, selected, pressed, and disabled component states. |
| A11Y-MAN-004 | Reduced-motion review with animator scale zero; state remains clear and progress remains perceivable. |
| A11Y-MAN-005 | Keyboard/D-pad order and child-route focus restoration. |
| A11Y-MAN-006 | Physical oldest/newest family-device review, including screen magnification where applicable. |
| A11Y-MAN-007 | Grayscale/non-color recognition and long English/pseudolocale expansion. |

Compose previews and screenshot fixtures use fixed clocks and only public approved or clearly synthetic locations. They never contain real family/device coordinates. Evidence is linked back to UI-001–UI-007 and the corresponding delivery task; screenshots alone do not prove accessibility.

## M2 implementation evidence (2026-08-06)

V1-M2-01 through V1-M2-04 implement the semantic-colour and exact-token foundation, 48dp action contract, explicit text/non-colour component states, deliberately merged timing-folio summary with separate action, domain-neutral state models and eight deterministic synthetic adaptive/theme/state/reduced-motion preview fixtures. Focused M2 JVM tests passed 26/26, the complete JVM suite passed 35/35, and the complete connected suite passed 13/13 on an API-36 emulator, including component and representative preview semantics. Lint and both APK builds passed, and final independent QA found zero open findings.

This automated evidence does not claim manual TalkBack traversal, keyboard/D-pad focus and restoration, actual 200% font-scale or compact-landscape visual inspection, grayscale/high-contrast/OLED appearance, pressed/focus/disabled visual contrast, screen magnification, API-26 runtime or physical-family-device results. A11Y-MAN-001 through A11Y-MAN-005 and A11Y-MAN-007 close the accessibility/adaptive/localization/manual-visual obligations under V1-M9-01; A11Y-MAN-001's API matrix also depends on V1-M9-02 API runtime/regression evidence; and A11Y-MAN-006's physical-family/OEM execution belongs to V1-M9-03 while its accessibility and magnification findings feed V1-M9-01. All seven manual evidence rows remain required inputs to V1-M9-05 final release acceptance.
