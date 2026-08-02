# Accessibility

| Field | Value |
|---|---|
| Status | Awaiting Approval |
| Version | 0.2 |
| Last updated | 2026-08-01 |
| Owner role | UI/UX Designer |
| Approval state | Phase B cross-direction contract proposed; implementation evidence deferred |

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

## Evidence template

Link each UI ID to preview/screenshot cases, automated accessibility checks, manual TalkBack results, font-scale/device matrix, defects, and approval record.
