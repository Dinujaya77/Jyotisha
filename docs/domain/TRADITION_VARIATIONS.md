# Tradition Variations

| Field | Value |
|---|---|
| Status | Partially Complete |
| Version | 1.2 |
| Last updated | 2026-08-02 |
| Owner role | Business Analyst |
| Approval state | TV-001–TV-007 approved under PA-001; TV-008–TV-015 recorded under CR-001; TV-016–TV-019 approved under PA-004 with SOLAR-001 |

| ID | Decision area | Selected V1.0 behavior | Documented alternative | Effect/state |
|---|---|---|---|---|
| TV-001 | Hora duration | 12 equal day + 12 equal night seasonal intervals | 24 equal sunrise-to-next-sunrise divisions | Explicitly excluded; materially different boundaries |
| TV-002 | Solar rise/set | Apparent upper limb, standard refraction, centre zenith 90.8333° | Centre-disc/no-refraction or other traditional definitions | Explicitly excluded from CP-001 |
| TV-003 | Time basis | Explicit civil IANA zone plus astronomical longitude | Local mean time interpretations in SRC-003 | Retained as variation; not selected |
| TV-004 | Rulers | Seven classical rulers in CR-003 | Rahu/Ketu or other additions | Excluded |
| TV-005 | Interpretation | Neutral timing and educational content | Auspiciousness/activity advice | Deferred until separately sourced/approved |
| TV-006 | Location/zone | Device zone for current-device mode; `Asia/Colombo` for manual/default Sri Lanka | Worldwide coordinate-to-zone resolution or fixed-offset guesses | Worldwide resolution deferred; guessing forbidden |
| TV-007 | Horizon | Sea-level, level and unobstructed model | Terrain/elevation/weather/local horizon model | Deferred; limitations disclosed |
| TV-008 | Meaning of Kala/Hora | Keep CP-001 Seasonal Planetary Hora and proposed CP-002 fixed Sri Lankan Kala as separate systems | Sources sometimes use overlapping `Hora`/`Kala Hora` labels for seasonal, fixed actual-sunrise, or fixed-table methods | Separation proposed; never infer synonymy from a label |
| TV-009 | Fixed Kala anchor | Proposed actual local sunrise | Fixed 06:00 tables, local-noon references, or seasonal anchors | CP-002 provisional; SRC-009 required |
| TV-010 | Fixed Kala duration/coverage | Proposed 60 civil minutes; no following-sunrise policy selected | Seasonal durations, equal sunrise-to-next-sunrise 24ths, continuation, reset/split, partial period, gap/overlap | Blocked; do not implement |
| TV-011 | Panchama duration | Proposed five fixed 12-minute parts of a fixed 60-minute parent | One-fifth of a seasonal Hora or another parent rule | CP-002 provisional; seasonal inheritance forbidden without approval |
| TV-012 | Panchama ruler cycle | Proposed weekday order Ravi → Chandra → Kuja → Budha → Guru → Shukra → Shani | Main-Kala/hourly cycle or regional table variation | Matrix provisionally corroborated; authority pending |
| TV-013 | Rahu period | Proposed daytime sunrise-to-sunset eighth | Some Sri Lankan sources/calculators show sunset-to-following-sunrise night Rahu | Daytime-only proposed for V1.0; night deferred |
| TV-014 | Solar convention by system | Proposed CP-003 reuses CP-001's approved astronomical anchors | Practitioner/almanac sunrise may use a different disc/refraction/table convention | Any difference requires a new explicit profile, never a hidden offset |
| TV-015 | Ruler names | One reviewed canonical display name per language plus secondary aliases | Ravi/Hiru/Iru; Sandu/Chandra; Angaharu/Kuja; Brahaspathi/Guru; Sikuru/Shukra; Senesuru/Shani variants | Language and practitioner review pending |
| TV-016 | Observer elevation | `SEA_LEVEL_FIXED`; reject elevation-adjusted profiles | Dip-of-horizon/topocentric observer height, terrain/elevation correction, or silently ignoring a supplied numeric elevation | Approved under PA-004; elevation-aware engine deferred |
| TV-017 | Refraction/weather | Fixed conventional 34 arcminutes plus 16 arcminutes solar semidiameter, total `0.8333°` | Live pressure/temperature/humidity model, NOAA variable-refraction position correction, or provider weather | Approved under PA-004; live weather intentionally excluded |
| TV-018 | Solar algorithm | Local NOAA/Meeus-derived equations frozen in SOL-R-007/SOL-R-008 | Production NREL SPA port, NOAA web output, third-party solar library, or mixed-provider anchors | Approved under PA-004; NREL SPA is validation only |
| TV-019 | Time-scale/display | Production UTC-as-UT1 (`ΔUT1=0`) model; half-even millisecond anchors; central nearest-minute display | ΔT/UT1 service, leap-second prediction, per-row independent rounding, or claimed sub-second physical accuracy | Approved under PA-004; future engine/profile change requires new approval |

Selection means a deliberate product profile, not a claim that other traditions are incorrect. SRC-009 must assess every selected profile. CP-002 uncertainty blocks implementation; CP-003 traditional comparison remains a separate approval/release gate.

SOLAR-001 is an astronomical model choice, not a declaration that a printed Panchanga or every Jyotisha tradition uses the same sunrise convention. Any traditional authority requiring a different disc, refraction, elevation, or civil-day rule must be represented as a separately sourced profile and change request; no hidden offset may alter CP-001 anchors.
