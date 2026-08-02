# Design Directions

| Field | Value |
|---|---|
| Status | Awaiting Approval |
| Version | 0.3 |
| Last updated | 2026-08-02 |
| Owner role | UI/UX Designer |
| Approval state | CR-001/Option C approved; three Phase B directions require reconciliation and no direction is approved |

## Shared contract

Change Request CR-001 does not replace the three directions. Under approved Option C, V1.0 keeps Seasonal Planetary Hora dominant and adds Rahu as a subordinate concurrent status only after CP-003/RK approval. Fixed Kala/Panchama remains a future Version 1.1 concept until CP-002/KH/PK approval. No direction may solve added density with four equal cards.

All directions must support long reviewed English/Sinhala names, grouped timeline disclosure, per-system unapproved/unavailable states, 200% font, compact phones, TalkBack boundary announcements, reduced motion, and colour-independent Rahu status.

Every direction uses Material 3 structure and semantic tokens, preserves meaning in light/dark mode, meets NFR-005, and keeps the current Hora dominant. The 24-Hora view remains chronological rather than becoming a decorative wheel. Status uses text plus icon/shape, never colour alone. Motion confirms state or continuity and disappears under reduced motion.

All directions avoid horoscope-ad styling, excessive glow, continuously animated backgrounds, tiny ornamental labels, dense radial charts, and unreviewed sacred/culturally specific imagery. They preserve professional, premium, calm, subtly fantastical, mysterious, culturally respectful, older-user-readable character.

## Direction A — Celestial Archive

### Thesis

A carefully preserved astronomical manuscript translated into a contemporary interface: editorial hierarchy, quiet midnight depth, warm paper-like light surfaces, fine celestial geometry, and restrained antique-metal accents. It should feel collected and trustworthy, not distressed, occult, or imitative of a sacred manuscript.

### Visual system

- Light mode: warm parchment-neutral background, clean ivory surfaces, deep ink content, muted slate secondary text, and sparing antique-gold accent.
- Dark mode: deep midnight blue, lifted blue-charcoal surfaces, warm off-white content, and softened antique gold.
- An editorial serif may be evaluated only for large ruler/headline text; system/humanist sans and tabular numerals serve times, actions, and body. No font is approved by this direction.
- Moderately rounded cards, fine archival rules, shallow elevation, and nonsemantic orbit/compass-line ornament.
- A short boundary crossfade or restrained line reveal is allowed; no shimmer or ambient animation.

### Screen character

The current Hora becomes a featured folio: large ruler, calm remaining-time emphasis, labelled boundaries, and a smaller attached next-Hora note. Sunrise/sunset form a labelled astronomical-anchors row. Day is a ledger with Day/Night chapters and a textual `Current` badge plus outline/weight. Provenance is a catalog line above the featured card; Method uses readable editorial sections and catalog-like version metadata without tiny type.

### State behavior

- Precise: `Current location · Precise` plus refresh.
- Approximate: `Approximate` plus outlined radius icon; optional improve action without pressure.
- Saved/stale: `Saved device location` plus age and explicit stale wording.
- Manual/default: town or `Colombo · Default location`, source text, and `Use current location`.
- Denied/services disabled: plain notice with location-off/shield icon; town/default remain primary; Settings only when meaningful.
- Retrieval: prior result stays visible; `Finding current location…` and Cancel appear in the provenance row.
- Failure/timeout: one caution notice names the retained fallback and offers user-initiated retry.
- Solar unavailable: featured card says `Hora unavailable`; it never invents ruler/times and keeps recovery/Method reachable.
- 200% font: folio becomes a vertical stack and ornament yields before text. TalkBack excludes geometry and never hears countdown ticks. Reduced motion removes reveals/transitions.

### Strengths and risks

Strongest heritage/trust/editorial balance and an excellent fit for methodology. Risks are serif legibility/Sinhala readiness, gold contrast, and drift toward faux-antique cliché.

## Direction B — Astral Observatory

### Thesis

A precise modern observing instrument: clean astronomical data, measured spacing, crisp coordinate-like rules, and cool technical surfaces softened for family use. It communicates calculation integrity most directly.

### Visual system

- Light mode: cool off-white, white or pale blue-grey panels, navy/graphite content, restrained teal/cyan information accent, and warm amber celestial accent.
- Dark mode: near navy, blue-grey panels, high-contrast neutral text, desaturated cyan and amber.
- Highly legible system/humanist sans throughout, strong display weight for the ruler, and tabular numerals for aligned times.
- Softly rounded instrument panels with low-emphasis grids, horizon rules, calibrated marks, and simple sun/moon discs.
- A short new-current marker sweep is allowed only at a Hora boundary; no looping radar/orbit animation.

### Screen character

The current Hora is a central observation readout: ruler, remaining duration, and aligned boundaries. Solar anchors sit on a labelled horizon scale. Day uses aligned chronological columns and strong Day/Night groups, switching to stacked rows at large text. Provenance is an observation-context panel. Method reads as an observation log: Method, Inputs, Assumptions, Privacy, Sources, Versions, Limits.

### State behavior

- Precise/approximate: context text explicitly names permission precision; dashed-radius decoration never substitutes for `Approximate`.
- Saved/stale: source and age remain visible with explicit stale status.
- Manual/default: source and `Use current location` remain visible; manual details show `Asia/Colombo`.
- Denied/disabled: plain-language panel with town/default operation; Settings action only when applicable.
- Retrieval: bounded acquisition status replaces only the context panel, not the calculation; Cancel remains available.
- Failure/timeout: caution strip above the retained readout identifies the active fallback.
- Solar unavailable: meaningful unavailable labels replace result fields; details and recovery remain.
- 200% font: columns become stacked label/value blocks and grids disappear before content compresses. TalkBack receives complete row phrases. Reduced motion removes sweeps and number transitions.

### Strengths and risks

Best calculation-integrity signal, timeline scanning, and typography/dependency simplicity. Risks are a clinical/generic feel, excessive dashboard metadata, and science-fiction styling if cyan or glow is overused.

## Direction C — Moonlit Sanctuary

### Thesis

A quiet, protective evening space for a family ritual of checking time: soft lunar arcs, generous breathing room, rounded surfaces, and a warmer human tone. Mystery comes from light, depth, and rhythm rather than religious imagery or fantasy illustration.

### Visual system

- Light mode: misty pearl or pale lavender-grey, warm-white surfaces, deep plum-indigo content, and muted clay or moon-gold accent.
- Dark mode: deep indigo/plum, softly lifted violet-charcoal surfaces, moon-white content, desaturated silver-blue and warm clay.
- Friendly humanist/system sans with open counters, generous line height, and clear numerals. Any softer display face is separately gated.
- Rounded cards and state labels with conventional Material actions; abstract crescent/horizon arcs are nonsemantic ornament.
- A gentle boundary crossfade/settle may be evaluated; no breathing, pulsing, floating, or continuous motion.

### Screen character

The current Hora occupies a spacious quiet-moment card with ruler, remaining time, boundaries, and next ruler. Sunrise/sunset are paired labelled horizon cards. Day is a calm sequence of rounded rows with prominent Day/Night headings and textual/icon/outline current state. Provenance uses a reassuring context card. Method favors short plain-language sections and progressive disclosure for technical detail.

### State behavior

- Precise/approximate: reassuring source text and refresh; approximation remains explicit and non-alarmist.
- Saved/stale: age and stale wording remain visible without relying on warning color.
- Manual/default: persistent selected town or explicit `Using Colombo default`, plus `Use current location`.
- Denied/disabled: the app-still-works message gives town/default equal or greater prominence than Settings.
- Retrieval: prior Hora remains; context says `Finding current location…` with Cancel and no pulsing moon.
- Failure/timeout: concise caution card retains result and offers retry/town actions.
- Solar unavailable: spacious plain-language state with no illustration that could be mistaken for a result.
- 200% font: cards stack and ornament disappears; actions may become full width. TalkBack uses headings/coherent summaries. Reduced motion removes settling/crossfade.

### Strengths and risks

Warmest and least intimidating for older family users. Risks are pastel contrast, a generic wellness feel, and accidental implication of spiritual authority.

## Cross-direction state contract

| State | Required content/action in A, B, and C |
|---|---|
| Current precise | `Current location · Precise`, acquisition/freshness in detail, `Refresh location` |
| Current approximate | Explicit `Approximate`, reported accuracy when useful, optional non-coercive precision action, refresh |
| Saved | `Saved device location`, source age, stale label at threshold, refresh |
| Manual | Town name + `Manually selected`, persistent selection, `Use current location` |
| Colombo fallback | `Colombo · Default location`, default disclosure, `Use current location` |
| Permission unavailable | Respectful explanation, manual/default choices, Settings only through user action |
| Services disabled | Text + icon, Settings action and town alternative |
| Retrieval active | Prior snapshot remains, progress text, Cancel |
| Retrieval failure/timeout | Retained fallback and its provenance, warning text, Retry/town action |
| Solar unavailable | No fabricated Hora/anchors/countdown; reason where safe, retry/reselect and Method |
| Light/dark | Equivalent hierarchy, semantics, contrast, and states |
| 200% font | Vertical reflow, no clipping/truncation of key values/actions |
| TalkBack | Coherent summaries, chronological rows, decorative content hidden, no tick announcements |
| Reduced motion | State remains understandable with all ornamental transitions removed |
| Rahu active/upcoming/completed | Explicit text, start/end, next transition and icon/shape/weight cue; never red/green or colour alone; no auspiciousness claim |
| Per-system unavailable/unapproved | Name the affected system and evidence/input state; preserve independently valid results; never show guessed times |
| Future Kala/Panchama | One atomic parent/child card and grouped disclosure timeline; long names wrap; one combined boundary announcement |

## CR-001 direction adaptations

### Direction A — Celestial Archive

- Under Option C, Rahu is a supporting marginal note/banded row below the seasonal-Hora folio, not another featured card.
- Under a later Option B, the featured folio becomes a two-level entry: main Kala heading and current Panchama active passage, separated by restrained editorial dividers. The Day view becomes a grouped ledger with parent chapter rows and subdivision entries.
- Keep long Sinhala/Panchama names and all times in the highly legible sans; any serif use is limited to a comfortable main-ruler display.
- Risk: bilingual labels plus archival dividers can become busy. Ornament disappears before content compresses or truncates.
- Recommendation impact: A remains the preferred visual direction for a validated Option B; B's timeline alignment remains an implementation principle, not a merged direction.

### Direction B — Astral Observatory

- Under Option C, Rahu is a secondary status strip with explicit status/start/end.
- Under a later Option B, one instrument panel contains separate main-Kala and current-Panchama readout bands; the next transition is the most prominent numeric value after ruler names. Its aligned grouped timeline is strongest for five subdivisions.
- Risk: system IDs, versions, transitions, and validation states can turn the dashboard into telemetry. Use progressive disclosure and strict content limits.
- B has the lowest layout risk if Option A were chosen, but visual precision cannot remove the conceptual burden of two competing Hora systems.

### Direction C — Moonlit Sanctuary

- Under Option C, Rahu is a calm textual context row below the quiet seasonal-Hora card with explicit state and outline marker.
- Under a later Option B, one quiet-moment surface contains main Kala plus a nested Panchama section using spacing/type rather than nested rounded cards. Day uses generous expandable parent rows.
- Risk: repeated rounded containers can recreate equal cards and excessive scrolling. Compact layouts use one surface with internal dividers/spacing; soft tones must retain status contrast.

### Option complexity

| Product option | Visual/cognitive consequence | Direction guidance |
|---|---|---|
| A — both systems V1.0 | Highest density and older-user risk; Now must still name one primary system and Day needs a non-interleaving selector. | Not preferred; B is lowest layout risk if mandated. |
| B — Sri Lankan primary | Cleanest eventual parent/child hierarchy and best family intention, but only when CP-002/language evidence is approved. | A remains recommended, conditional on domain approval. |
| C — staged | Smallest honest V1.0 change: seasonal result plus subordinate daytime Rahu; major Kala hierarchy waits for V1.1. | Recommended now; all three remain viable and unapproved. |

Suggested future semantic roles describe structure rather than rulers: `timingPrimaryContainer/content`, `timingNestedContainer/content`, `concurrentStatusContainer/content`, `activeOutline`, `validationCaution`, and `timelineGroupDivider`. Do not require seven ruler colours. Reduced motion removes number rolls, sweeps, reveals, and settling without changing text, outline, or status meaning.

## Selection guidance

- Choose **A** for heritage, trust, editorial craft, and subtle celestial mystery.
- Choose **B** for precision, timeline scanability, and lowest visual implementation risk.
- Choose **C** for emotional comfort and older-user approachability.

The UI/UX recommendation remains **A — Celestial Archive** for a future validated Option B, while retaining B's timeline discipline and C's generous spacing as later implementation principles. Under currently recommended product Option C, no direction advantage is large enough to reopen the held approval gate. This is not permission to merge or approve directions.

## Deferred after direction approval

Direction approval does not approve final tokens, palettes, fonts/licenses, assets, icons, copy, component measurements, breakpoints, motion values, town-picker behavior, or UI implementation. Final UI work remains blocked on CP-002/CP-003 rules relevant to the selected version, approved terminology, architecture, and one selected direction. Those later artifacts require light/dark previews, 200% font, TalkBack, keyboard, reduced-motion, contrast, pseudolocale, and cultural-review evidence.
