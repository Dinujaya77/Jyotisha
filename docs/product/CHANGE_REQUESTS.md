# Product Change Requests

| Field | Value |
|---|---|
| Status | Approved |
| Version | 0.1 |
| Last updated | 2026-08-02 |
| Owner role | Lead Coordinator |
| Approval state | Change Request CR-001 approved 2026-08-02; PA-001 remains the preserved historical baseline |

## Identifier namespace

`Change Request CR-001` is a product change-control identifier. It is distinct from the already approved calculation-rule identifier `CR-001 — Solar anchors` in `CALCULATION_RULES.md`. Existing approved calculation-rule IDs are not renamed. New calculation rules introduced by this change use `KH-*`, `PK-*`, and `RK-*` prefixes to avoid ambiguity.

## CR-001 — Sri Lankan Kala Hora, Panchama Kala and Rahu Kalaya

| Field | Value |
|---|---|
| Requested | 2026-08-01 by repository owner |
| State | Approved 2026-08-02 |
| Change type | Material amendment to the PA-001 requirements baseline |
| Approval phrase | `APPROVE REQUIREMENTS AMENDMENT CR-001` |
| Approval evidence | Exact phrase supplied by repository owner in the approved request attached 2026-08-02 |
| Implementation authority | None; documentation reconciliation only |
| Phase B effect | Architecture and design reconciliation may resume; neither approval is granted by this change |

### Requested outcome

Investigate and, when sufficiently validated, present Sri Lankan Kala Hora, Panchama Kala and daytime Rahu Kalaya without treating them as synonyms for the approved seasonal Planetary Hora. Preserve location-aware astronomical sunrise/sunset, current/manual/fallback location, methodology, provenance, neutral timing-only content, offline operation, and older-user accessibility.

### Baseline relationship

- PA-001 and its original approval on 2026-08-01 remain recorded and are not rescinded.
- The repository owner supplied the exact amendment approval phrase on 2026-08-02, so the Option C allocation, FR-012–FR-015, NFR-015, US-011–US-013, conditional deltas, and linked validation obligations/case definitions now amend the approved product baseline.
- PA-001's FR-001–FR-011, NFR-001–NFR-014, CP-001, and calculation rules CR-001–CR-010 remain approved and are not replaced.
- CP-002/CP-003 and KH-*/PK-*/RK-* retain separate domain gates and remain non-executable. Unresolved expected outputs remain blank/Blocked.

### User-supplied requirement

`USR-DOM-001 — User-supplied Panchama Kala rule` states that a main Kala is 60 civil minutes and contains five consecutive 12-minute subdivisions. For Ravi, the user supplied Ravi → Sandu/Chandra → Angaharu/Kuja → Budha → Brahaspathi/Guru. This is authoritative as the owner's desired product behavior, but not as a verified traditional calculation rule.

The owner also supplied the proposed daytime Rahu method: divide local astronomical sunrise-to-sunset into eight continuous parts and select Sunday 8, Monday 2, Tuesday 7, Wednesday 5, Thursday 6, Friday 4, or Saturday 3. Rahu must remain separate from the seven-ruler sequence.

### Investigation conclusion

- **Seasonal Planetary Hora:** the approved CP-001 system; 12 equal daylight intervals plus 12 equal night intervals, normally variable in duration.
- **Sri Lankan Kala Hora:** practitioner sources describe a separate fixed-clock system beginning at actual local sunrise, using weekday lord first and Ravi → Sikuru/Shukra → Budha → Sandu/Chandra → Senesuru/Shani → Brahaspathi/Guru → Angaharu/Kuja. The evidence is useful but not independently authoritative.
- **Panchama Kala:** practitioner evidence and USR-DOM-001 agree on five 12-minute subdivisions within a fixed 60-minute Kala. The first sub-ruler matches the parent; later sub-rulers follow the weekday-ruler order. The complete matrix is provisionally corroborated, not approved.
- **Rahu Kalaya:** multiple contemporary sources agree on daytime sunrise-to-sunset eighths and the supplied weekday allocation. A Sri Lankan commercial source also presents a night variant, so the selected tradition must explicitly choose daytime-only or approve a separate night rule.

### Blocking domain defect

Under a 3,600-elapsed-seconds interpretation—or on an offset-stable date such as modern Sri Lanka—twenty-four fixed 60-minute periods end exactly 24 elapsed hours after sunrise, while the following astronomical sunrise is generally earlier or later. Across a civil-time offset transition, the relationship between 60 wall-clock civil minutes and elapsed seconds is itself unresolved. Available sources do not resolve whether the tradition leaves a gap/overlap, truncates or adds a period, resets/splits at sunrise, or uses another correction. Panchama inherits this conflict. No production profile may guess the answer.

### Product-option decision record

| Option | User value/domain confidence | Older-user and visual effect | Testing, cost, and release effect | Assessment |
|---|---|---|---|---|
| A — both systems in V1.0 | Highest breadth but confidence is capped by the least-established fixed/Panchama rule. | Highest confusion risk from two Hora answers; requires an explicit system selector and separate timelines. | Largest 35-cell/boundary/traditional matrix, implementation cost, and release delay; V1 is blocked by CP-002. | Not recommended. |
| B — Sri Lankan system primary | Best match to the desired eventual family experience if CP-002 becomes authoritative; currently demotes an approved feature on incomplete evidence. | Cleanest eventual combined Kala/Panchama card with seasonal Hora secondary. | Reworks the approved primary experience and delays V1 until coverage, language, accessibility, and full traditional validation pass. | Preferred future experience only after strong traditional validation. |
| C — staged introduction | Adds useful, better-corroborated daytime Rahu while retaining the approved high-confidence seasonal profile. | Lowest V1 cognitive density: one primary Hora result and one subordinate Rahu status; a major hierarchy change is deferred. | Smallest bounded V1 calculation/UI/test addition; CP-002 receives focused V1.1 evidence without delaying V1. | Recommended now. |

**Approved allocation:** Option C. Daytime Rahu is allocated to V1.0 subject to an approved CP-003/RK rule package. Sri Lankan Kala Hora and Panchama Kala are allocated to V1.1 and remain blocked from implementation until CP-002 is complete and independently/traditionally validated. If Rahu's selected Sri Lankan convention is not adequately validated, it defers rather than weakening the gate.

### Impact summary

- Product: four new proposed functional requirements, one non-functional requirement, three stories, and explicit conditional deltas to FR-001/FR-002/FR-009–FR-011/NFR-001/NFR-003/NFR-014; no approved history is silently rewritten.
- Domain: separate CP-002 and CP-003 profiles, namespaced rule sets, source-quality record, variation record, matrix, and explicit unresolved questions.
- Architecture: separate pure calculators over shared immutable solar/location/time context; typed profile approval and provenance; no module or dependency.
- UI: keep Now/Day/Method and all three directions; conditional hierarchy for a future combined main-Kala/Panchama card; Rahu subordinate; distinct per-system unavailable states.
- Validation: proposed cases cover every main/sub-ruler, boundaries, sunrise mismatch, all Rahu weekdays, location/zone/leap/restart, accessibility, and provenance.
- Delivery: Phase B architecture/design reconciliation was authorized and later approved separately under PA-002 and DA-001; final UI specification, delivery planning, and application implementation remain stopped.

### Required unresolved approvals

1. A trusted Sri Lankan practitioner, printed Panchanga, or equivalent authority for the exact Kala/Panchama and Rahu traditions.
2. Fixed 60-minute coverage at sunset and following sunrise, including gap/overlap/reset/partial-period policy and civil-versus-elapsed minutes.
3. Whether day and night use the same fixed Kala/Panchama rules.
4. The complete Panchama matrix and whether each parent restarts at its own ruler.
5. Daytime-only versus a separately defined nighttime Rahu rule; boundary membership and post-sunset status.
6. Reviewed Sinhala spellings, transliterations, aliases, and concise display names.
7. CP-003's exact solar-anchor inheritance and source-backed expected vectors.

### Gate

The exact phrase `APPROVE REQUIREMENTS AMENDMENT CR-001` was received and recorded on 2026-08-02. It approves only the amended product baseline, Option C allocation, FR-012–FR-015, NFR-015, US-011–US-013, the stated deltas to existing requirements, and the linked validation obligations/case definitions.

It does **not** approve or make executable:

- CP-002 or KH/PK rules, which remain **Blocked** pending separate traditional/domain approval;
- CP-003 or RK rules, which are **Blocked from approval** pending selected-tradition confirmation, approved golden vectors, canonical terminology, and frozen source/profile versions;
- any unresolved expected validation output, which remains blank/Blocked;
- ADR-008/ADR-009, Phase B architecture, dependencies, a design direction, `UI_SPEC.md`, delivery planning, application implementation, release validation, a commit, merge, or push.

Those retained their own explicit gates after the requirements amendment. PA-002 architecture, DA-001 Direction A, PA-004 `SOLAR-001`, DA-004 final UI, and DP-001 delivery were later approved separately on 2026-08-02. CP-002/CP-003, explicit task start, implementation evidence, and release retain their own gates. The CR-001 approval itself authorized documentation reconciliation only, not application implementation.
