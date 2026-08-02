# Accessibility

| Field | Value |
|---|---|
| Status | Ready |
| Version | 0.4 |
| Last updated | 2026-08-02 |
| Owner role | UI/UX Designer |
| Approval state | DA-001 Direction A selected with the cross-direction accessibility contract; final UI measurements and implementation evidence remain separately gated |

## Required review areas

- Readable type and layouts at large font/display scales without clipping or lost actions.
- Sufficient text, icon, focus, and component contrast; no colour-only meaning.
- Descriptive semantics, logical TalkBack traversal, headings, state announcements, and merged semantics used deliberately.
- Adequate touch targets, spacing, predictable navigation, and alternatives to gestures.
- Reduced-motion support and no distracting background animation.
- Clear plain-language inputs, errors, recovery, and domain explanations suitable for older family members.
- RTL, locale expansion, date/time pronunciation, and numeric formatting where supported.
- Initial focus on screen heading; coherent current-Hora summary with actions outside merged semantics.
- Countdown excluded from continuous live-region announcements; at most one concise boundary announcement.
- Day traversal ordered Day heading/rows 1–12 then Night heading/rows 13–24; every row speaks ruler, boundaries, and current status.
- At 200% font, time pairs/columns stack, rows grow, ornament disappears before content, and no action/value clips.
- Precise, approximate, saved, stale, manual, default, retrieval, failure, and unavailable states use explicit text plus a non-colour cue.
- Solar-unavailable UI never presents placeholders or ornament that could be mistaken for a calculated result.
- Reduced motion removes all ornamental movement without obscuring the state change.
- Rahu uses one coherent labelled status: `Active now`, `Later today`, `Completed today`, `Calculating Rahu Kalaya…`, `Unavailable — location unavailable`, `Unavailable — calculated sunrise or sunset unavailable`, or `Calculation method not approved`. Each uses text plus icon/outline/weight, never colour alone.
- Active/later/completed states speak the interval start/end and the next relevant transition; pending/unavailable/unapproved states speak no guessed time. A prior value remains only when explicitly labelled previous with its provenance.
- Rahu countdown ticks are not live regions. At an exact foreground boundary, announce at most one concise combined status transition.
- Status and recovery actions are separate semantic nodes; merged status semantics do not hide `Choose a town`, `Use current location`, retry, Method, or Settings actions.
- At 200% font, result and status containers have no fixed height, essential values stack, and actions may become full width. Compact landscape, keyboard/D-pad order, and minimum 48dp targets retain every action.
- Reduced motion makes status/boundary changes immediate while preserving text, outline, focus, and the optional single announcement.
- Validate localized date/time pronunciation, pseudolocale expansion, and longest approved English/Sinhala names without inventing unreviewed Sinhala copy.

## Evidence template

Link each UI ID to preview/screenshot cases, automated accessibility checks, manual TalkBack results, font-scale/device matrix, defects, and approval record.
