# Domain Glossary

| Field | Value |
|---|---|
| Status | Awaiting Approval |
| Version | 1.1-draft |
| Last updated | 2026-08-02 |
| Owner role | Business Analyst |
| Approval state | TERM-001–TERM-019 approved under PA-001; TERM-020–TERM-029 definitions recorded under approved CR-001, while profile/rule and language validation remain separate |

| ID | Term | Working definition for V1.0 | Notes/state |
|---|---|---|---|
| TERM-001 | Hora | A planetary time interval governed by one of seven classical rulers; not D2 Hora or a fixed clock hour. | Approved terminology |
| TERM-002 | Seasonal/temporal Hora | One twelfth of the calculated daylight interval or one twelfth of the calculated night interval; duration varies with anchors/location/date. | Selected by PD-001 |
| TERM-003 | Hora day | The interval from one calculated local sunrise to the following calculated local sunrise. | Selected by PD-001 |
| TERM-004 | Planetary ruler | Sun, Moon, Mars, Mercury, Jupiter, Venus, or Saturn assigned to an interval by CR-003. | Approved terminology |
| TERM-005 | Daytime Hora | One of 12 equal internal intervals from calculated sunrise to calculated sunset. | Selected |
| TERM-006 | Nighttime Hora | One of 12 equal internal intervals from calculated sunset to following calculated sunrise. | Selected |
| TERM-007 | Calculated astronomical sunrise | Modelled time when the apparent upper solar limb reaches a level horizon under CP-001 assumptions. | Not guaranteed observed visibility |
| TERM-008 | Calculated astronomical sunset | Modelled time when the apparent upper solar limb leaves a level horizon under CP-001 assumptions. | Not guaranteed observed visibility |
| TERM-009 | Apparent upper limb | Upper edge of the apparent solar disk, with standard refraction included by the selected zenith convention. | SRC-004 |
| TERM-010 | Solar zenith distance | Angle from the observer's zenith to the Sun's centre; CP-001 uses 90.8333° for rise/set. | SRC-004 |
| TERM-011 | Active time zone | IANA zone used to interpret civil dates/times: device system zone for current-device mode or `Asia/Colombo` for manual/default Sri Lankan mode. | Selected |
| TERM-012 | Precise/approximate location | Android permission precision state derived from granted fine/coarse permission, distinct from reported metre accuracy. | Approved |
| TERM-013 | Saved location | Last successfully persisted selected-device location record used as a local fallback. | No raw history |
| TERM-014 | Manual location | Sri Lankan town/location explicitly selected by the user and retained until the user changes location mode. | Selected |
| TERM-015 | Default location | Provisional Colombo fallback used only when no higher-priority usable selection exists. | Coordinates unresolved |
| TERM-016 | Stale location | Saved device location at least 24 hours old or carrying invalid/future time metadata; usable only as a labeled fallback. | Approved product threshold; architecture policy pending |
| TERM-017 | Calculation profile | Versioned set of domain conventions, inputs, exclusions, sources, and validation requirements. | CP-001 |
| TERM-018 | Engine version | Identifier of the exact solar/Hora implementation used to produce a result. | Required provenance |
| TERM-019 | Inclusive-start/exclusive-end | `[start,end)`: start belongs to an interval; its end belongs to the next interval. | Selected |
| TERM-020 | Sri Lankan Kala Hora | Proposed fixed-clock timing system, provisionally actual-sunrise anchored and distinct from CP-001 seasonal Hora. | Awaiting Approval; CP-002 blocked |
| TERM-021 | Tatkal/Thathkala Hora | Practitioner label used for an actual-time/fixed Kala presentation; not automatically a synonym for every Sri Lankan or seasonal Hora method. | Spelling and applicability require review |
| TERM-022 | Main Kala | Parent period in CP-002, provisionally 60 civil minutes and governed by one of seven rulers. | User/practitioner supplied; coverage unresolved |
| TERM-023 | Panchama Kala | One of five proposed subdivisions of a CP-002 main Kala, governed by a parent-linked sub-ruler. | Not one-fifth of CP-001 unless separately approved |
| TERM-024 | Sub-ruler | Ruler assigned to a Panchama subdivision; proposed cycle follows weekday order and is not the main-Kala cycle. | Provisional |
| TERM-025 | Rahu Kalaya | A separately calculated interval selected from daylight eighths by weekday; not an eighth ruler and not a content label for advice. | CP-003 proposed |
| TERM-026 | Daytime Rahu | Proposed `[sunrise,sunset)` Rahu interval under CP-003. | V1.0 proposed |
| TERM-027 | Nighttime Rahu | Claimed variant using a night interval; no rule selected in this amendment. | Deferred/unresolved |
| TERM-028 | Calculation-system ID | Stable identifier distinguishing Seasonal Planetary Hora, Sri Lankan Kala Hora, Panchama Kala, and Rahu Kalaya results. | NFR-015 proposed |
| TERM-029 | Evidence state | Explicit classification such as user supplied, provisional, rule approved, independently validated, or traditionally validated. | Production display requires approved rules |

## Provisional ruler terminology

| Locale-neutral ruler | User-supplied/transliterated aliases | Provisional Sinhala forms | State |
|---|---|---|---|
| Sun | Ravi | රවි; හිරු/ඉරු as possible aliases | Language/practitioner review required |
| Moon | Sandu; Chandra | සඳු; චන්ද්‍ර | Language/practitioner review required |
| Mars | Angaharu; Kuja | අඟහරු; කුජ | Language/practitioner review required |
| Mercury | Budha | බුධ | Language/practitioner review required |
| Jupiter | Brahaspathi; Guru | බ්‍රහස්පති; ගුරු | Language/practitioner review required |
| Venus | Sikuru; Shukra | සිකුරු; ශුක්‍ර | Language/practitioner review required |
| Saturn | Senesuru; Shani | සෙනසුරු; ශනි | Language/practitioner review required |

Do not finalize alias priority, Latin spelling, Sinhala spelling, or compound Panchama grammar from this table. The UI should use one reviewed short primary name per language and place secondary aliases in Method/glossary rather than repeating every alias in each timeline row.

The common derivation of “Hora” from *ahorātra* is recorded only as a traditional explanation where sourced, not as a settled linguistic fact.
