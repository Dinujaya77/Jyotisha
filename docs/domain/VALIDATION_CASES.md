# Validation Cases

| Field | Value |
|---|---|
| Status | Partially Complete |
| Version | 1.2 |
| Last updated | 2026-08-02 |
| Owner role | QA Reviewer |
| Approval state | VC-001–VC-019 approved under PA-001; VC-020–VC-039 obligations approved under Change Request CR-001; SOLAR-GOLDEN-001-v1.0, SOLAR-INTERMEDIATE-001-v1.0, and SOL-B cases approved under PA-004 |

All cases use CP-001 unless stated otherwise. Automated fixtures must record profile and engine versions. External expected results must never be changed merely to make tests pass. For VC-020–VC-039, labels such as `Proposed`, `pending`, or `Blocked` describe rule/evidence/execution readiness, not the now-approved obligation to cover the case.

| ID | Purpose and inputs | Expected result/invariant | Requirement/rule references | Evidence state |
|---|---|---|---|---|
| VC-001 | Synthetic Sunday: sunrise 06:00, sunset 18:00, following sunrise 06:00. | 24 one-hour intervals; first rulers Sun, Venus, Mercury, Moon…; following sunrise begins Monday/Moon. | FR-001, FR-002; CR-003–CR-006 | Deterministic fixture ready to define |
| VC-002 | Synthetic unequal day/night durations with remainders 1 and 11 modulo 12 nanoseconds. | Boundaries equal the common-anchor floor formula; endpoints are exact; intervals differ by at most 1 ns; repeated-addition output is rejected. | FR-002; CR-004, CR-005 | Exact/property tests planned |
| VC-003 | Instants immediately before, exactly at, and immediately after an internal boundary. | Unique membership follows `[start,end)`; exact boundary selects new interval. | FR-001, FR-008; CR-006, CR-007 | Boundary test planned |
| VC-004 | Exact calculated sunset. | Final day interval ends and first night interval begins with no gap/overlap. | FR-002; CR-004–CR-006 | Boundary test planned |
| VC-005 | Exact following sunrise. | Prior final interval ends; next Hora day starts with next weekday ruler. | FR-001, FR-002, FR-008; CR-002, CR-006 | Boundary test planned |
| VC-006 | Colombo, Kandy, Jaffna, Galle, and any approved family town across representative dates. | Valid anchors and complete timelines use recorded coordinates/zone; comparison data retained. | FR-003, FR-006; CR-001, CR-008 | Coordinates/source pending |
| VC-007 | Dates covering all weekdays and different months/seasons. | First ruler follows weekday mapping; seasonal duration varies from anchors without breaking invariants. | FR-001, FR-002; CR-002–CR-005 | Golden dataset pending |
| VC-008 | Leap day 2028-02-29 within engine range. | Valid anchors/timeline with correct civil-date/weekday handling. | FR-003, FR-008 | Golden dataset pending |
| VC-009 | Approved Sri Lankan cases compared with an independent astronomical implementation using the same convention. | Absolute anchor difference is at most 60 seconds; every discrepancy is recorded/explained. | FR-003; NFR-002 | Reference/algorithm pending |
| VC-010 | Precise foreground permission and valid current fix. | Provenance says current/precise; metre accuracy retained separately; one atomic recalculation. | FR-004, FR-005, FR-009 | Fake/instrumented test planned |
| VC-011 | Approximate-only permission and valid obfuscated fix. | Calculation continues, approximation is labeled, and no repeated pressure for precision occurs. | FR-004–FR-006, FR-009 | Fake/instrumented test planned |
| VC-012 | Denied, in-app request unavailable, services disabled, provider absent, timeout, null, and exception. | Each follows saved → manual → default fallback as applicable, without crash/blank UI; recovery is clear. | FR-004–FR-007 | State matrix planned |
| VC-013 | Explicit Kandy manual selection and Colombo default. | Manual selection persists until changed; both use `Asia/Colombo`; source label matches coordinates. | FR-006, FR-009; CR-008, CR-009 | Town/default data pending |
| VC-014 | Device time-zone change while current-device mode is active and while manual mode is active. | Current-device result recomputes in new system zone; manual Sri Lankan result remains `Asia/Colombo`; no mixed state. | FR-003, FR-008; CR-008, CR-010 | Clock/zone test planned |
| VC-015 | Fractional internal division plus display rounding. | Exact endpoints remain anchors/adjacent; displayed rounding never changes membership or creates apparent negative countdown. | FR-001, FR-002; CR-004–CR-007 | Property test planned |
| VC-016 | Saved fix immediately before/at 24 hours; failed refresh. | At 24 hours it becomes stale but remains a labeled fallback; prior coherent calculation remains visible. | FR-005, FR-007, FR-009 | Threshold test planned |
| VC-017 | Restart, lifecycle resume, midnight, manual clock change, and movement boundaries. | Selected location restores; date/clock recompute is atomic; 9,999 m does not replace for movement and 10,000 m does; a stale prior fix may be replaced regardless of movement when the new fix is usable. | FR-007, FR-008; CR-010 | State/threshold tests planned |
| VC-018 | Release comparison with SRC-009 across approved locations, boundaries, months, leap day, and fallback states. | Inputs/expected/actual/tolerance/evidence are retained; unexplained differences block release; expectations are not rewritten. | FR-010; NFR-013, NFR-014 | Release blocker; authority pending |
| VC-019 | Location timing/accuracy threshold matrix. | Returned-fix age immediately below/at/above 2 minutes; request completion immediately before/at/after 20 seconds with late callback ignored; accuracy improves only when both ≥50 m and ≥25% conditions hold; invalid/negative/future timestamps are stale. | FR-004, FR-007; CR-009, CR-010 | Exact threshold tests planned |
| VC-020 | Same location/date/instant evaluated by every enabled timing system. | Each outcome retains a distinct system/profile/rule ID; no result, sequence, count, rounding rule, validation state, or label aliases another system. | FR-012–FR-015; NFR-015 | Proposed under Change Request CR-001 |
| VC-021 | CP-002 cases whose sunrise weekdays cover all seven possible first main rulers and at least one full main-cycle wrap. | After KH-002 approval, first ruler and every successor match the authority-backed main sequence; before approval, calculator is unavailable. | FR-013; KH-002 | Proposed; exact authority vectors pending |
| VC-022 | Every one of the 35 cells in the PK-002 seven-by-five matrix. | Each expected parent/sub-ruler pair comes from an approved external record; no runtime inference substitutes for missing evidence. | FR-014; PK-001–PK-003 | Proposed; complete matrix authority pending |
| VC-023 | For every approved Panchama: exact first instant, final representable instant before end, exact end, and first instant after end. | `[start,end)` gives unique selection; exact end selects the next subdivision; countdown never becomes negative; displayed rounding cannot alter selection. | FR-014; PK-004 | Proposed boundary suite |
| VC-024 | Each of the seven parent main-Kala boundaries, including fifth Panchama end and next parent/first Panchama start. | Parent/subdivision endpoints are identical and atomic; approved cross-boundary cycle continues without gap/overlap or mixed parent/sub-ruler data. | FR-013–FR-015; KH-002, PK-003, PK-004 | Proposed; rule approval pending |
| VC-025 | Synthetic actual sunrise 06:07 with the proposed fixed rule. | If KH-001/KH-003 are approved unchanged, first main period is 06:07–07:07 and subdivisions begin 06:07, 06:19, 06:31, 06:43, and 06:55 with final end 07:07; otherwise update only through an approved profile revision. | FR-013, FR-014; KH-001, KH-003, PK-001 | Provisional expectation from USR-DOM-001/practitioner evidence; not approved |
| VC-026 | Exact sunrise and sunset under proposed CP-002, with representative day/night transition. | Sunrise reset and sunset behavior follow an authority-approved day/night rule; no expected count or split is asserted while KH-004/KH-005 are unresolved. | FR-013–FR-015; KH-004, KH-005 | Proposed; Blocked |
| VC-027 | Synthetic following-sunrise spans of 23:59, 24:00, and 24:01 after the prior sunrise. | The approved rule must explicitly prove period count, final endpoint, reset precedence, and gap/overlap/partial-period policy. Until KH-005 approval, return `RuleUnresolved` rather than a schedule. | FR-013, FR-014; KH-005 | Proposed; Blocked critical gate |
| VC-028 | Offset-transition dates in a non-Sri-Lankan current-device zone plus modern `Asia/Colombo` controls. | The approved CP-002 definition distinguishes 60 wall-clock civil minutes from 3,600 elapsed seconds; no hidden platform default decides behavior. | FR-013; KH-003, CR-008 | Proposed; Blocked on CP-002 duration semantics/terminology |
| VC-029 | App restart, foreground resume, clock tick and recalculation immediately before/at/after a Panchama and coincident main-Kala transition. | Selector re-evaluates exact time from one coherent schedule; one atomic result and one concise accessibility announcement occur; no stale parent/current/next combination. | FR-014, FR-015; PK-004; NFR-005, NFR-015 | Proposed lifecycle/accessibility suite |
| VC-030 | Synthetic 12-hour daylight divided into eight plus remainder-bearing integer-nanosecond daylight cases. | RK-001 yields eight exact adjacent parts from the common anchor; endpoint is sunset; integer quantization is bounded and iterative addition is rejected. | FR-012; RK-001 | Proposed deterministic fixture |
| VC-031 | Sunday through Saturday daytime Rahu allocation. | Selected indices are respectively 8, 2, 7, 5, 6, 4, and 3 after RK-002 approval; each case retains local civil weekday and source evidence. | FR-012; RK-002 | Proposed; source-backed golden records pending |
| VC-032 | Instants immediately before, exactly at, and immediately after Rahu start and end. | Exact start is `ActiveNow`; exact end is `CompletedToday`; `LaterToday` points to start, `ActiveNow` to end, and completed has no current-day next transition; displayed rounding and colour cannot alter meaning. | FR-012; RK-003 | Approved obligation; proposed boundary/UI suite pending RK approval |
| VC-033 | Before sunrise, daytime before/inside/after Rahu, exact sunset, after sunset, midnight, and following civil date. | Active-zone local date selects anchors/weekday; before sunrise is `LaterToday`; after exact end/sunset is `CompletedToday` with null next transition; midnight recomputes the new date; no nighttime/tomorrow interval is fabricated. | FR-012; RK-002, RK-003, RK-005 | Approved obligation; daytime-only scope pending RK approval |
| VC-034 | Material location change between approved Sri Lankan test points while multiple systems are enabled. | One new solar snapshot feeds all enabled calculators; every result changes atomically and retains identical location fingerprint/provenance where relevant. | FR-003, FR-006, FR-012–FR-015; NFR-015 | Proposed cross-system integration case |
| VC-035 | Current-device zone change and manual `Asia/Colombo` mode around date/weekday boundaries. | Current-device results recompute under the new system zone/date; manual results remain `Asia/Colombo`; Rahu weekday allocation and every enabled result stay coherent. | FR-008, FR-012–FR-015; RK-002; NFR-015 | Proposed zone/date case |
| VC-036 | Leap day 2028-02-29 under approved CP-003 and, later, CP-002. | Solar/civil weekday handling is valid; Rahu allocation follows the recorded weekday; blocked CP-002 remains unavailable until approved. | FR-012–FR-014; RK-002 | Proposed golden case |
| VC-037 | Approximate device fix, saved/stale fix, explicit manual Kandy selection, current request failure, and Colombo default. | Every enabled system uses the same selected fallback and labels precision/source; manual/default remain `Asia/Colombo`; no system silently retains prior coordinates after atomic replacement. | FR-005–FR-009, FR-012–FR-015; NFR-015 | Proposed location-state matrix |
| VC-038 | Provenance and calculation-call audit for enabled seasonal Hora and daytime Rahu, later CP-002. | One solar calculation per required context date; system/profile/rule/source/engine/display-policy IDs and exact inputs are present; internal representation, model accuracy, validation tolerance, and displayed precision remain distinct. | FR-010, FR-012–FR-015; NFR-014, NFR-015 | Approved architecture obligation; execution evidence pending |
| VC-039 | Compact phone, light/dark, 200% font, TalkBack, reduced motion, longest reviewed Sinhala/English names, valid/partial/unapproved system states. | One dominant approved result, subordinate Rahu, readable grouped timeline when enabled, logical semantics, combined boundary announcements, no truncation or colour-only state, and no live result for an unapproved rule. | FR-012–FR-015; NFR-005, NFR-011, NFR-015 | Direction A selected; final UI evidence pending |

## SOLAR-GOLDEN-001-v1.0 — frozen independent astronomical vectors

These expected values were not produced by the proposed production equations. They were generated by pinned pvlib 0.15.1's independent NREL-SPA `spa.transit_sunrise_sunset` implementation with `delta_t=67.0`, `numthreads=1`, Python 3.12.2, NumPy 2.5.1, pandas 3.0.5, and tzdata 2026.3. The pvlib wheel SHA-256 is `EBD41A93DBC215DB8CA3B3EE7D69BDE213A1AA861B8313D5D3BE6F82FB74B6B8`; the official NREL report is SRC-021, Revised January 2008, SHA-256 `1B9E2B6131D89EBC17FE4C047E64A0168143C949BFE6FAFE06D856D651555CC2`.

For each UTC base date around the requested IANA civil day, the oracle calculated NREL Appendix A.2 events at solar-centre elevation `−0.8333°`. The generator retained the unique event whose zoned local date matched the input date and rounded the frozen output to the nearest millisecond, half-even. Coordinates are unchanged public WGS84 values from the GeoNames SRC-023 snapshot, SHA-256 `68301F0C9465E9A312D172F586D4B86BCB727DA57B42E419BB3488EB65B7CEA0`, retrieved 2026-08-02. All vectors use `SEA_LEVEL_FIXED`, `Asia/Colombo`, the CP-001 apparent-upper-limb convention, retrieval date 2026-08-02, and an inclusive `60.000 s` per-anchor tolerance.

The oracle's fixed `67.0 s` ΔT is recorded for exact reproducibility; it is not a production input or a historical ΔT claim. Shared theoretical ancestry and the identical event threshold reduce—but do not eliminate—the correlation between methods. Independence comes from different equation families and code. NOAA output is not used as the sole oracle.

### Inputs and provenance

| Test ID | Coverage | Location and GeoNames ID | Civil date | Latitude | Longitude | Elevation | Zone | Source/version | Convention/tolerance | Reviewer status |
|---|---|---|---|---:|---:|---|---|---|---|---|
| SOL-G-001 | March equinox | Colombo, `1248991` | 2026-03-20 | 6.93548 | 79.84868 | Sea level fixed | `Asia/Colombo` | SRC-021 via SRC-022 0.15.1; SRC-023 | `−0.8333°`; 60.000 s | Approved under PA-004; QA reviewed 2026-08-02 |
| SOL-G-002 | June solstice | Kandy, `1241622` | 2026-06-21 | 7.29060 | 80.63360 | Sea level fixed | `Asia/Colombo` | Same frozen sources | Same | Approved under PA-004; QA reviewed 2026-08-02 |
| SOL-G-003 | December solstice | Jaffna, `1242833` | 2026-12-21 | 9.66845 | 80.00742 | Sea level fixed | `Asia/Colombo` | Same frozen sources | Same | Approved under PA-004; QA reviewed 2026-08-02 |
| SOL-G-004 | Leap day | Galle, `1246294` | 2028-02-29 | 6.04610 | 80.21030 | Sea level fixed | `Asia/Colombo` | Same frozen sources | Same | Approved under PA-004; QA reviewed 2026-08-02 |
| SOL-G-005 | Ordinary August date | Trincomalee, `1226260` | 2026-08-02 | 8.57780 | 81.22890 | Sea level fixed | `Asia/Colombo` | Same frozen sources | Same | Approved under PA-004; QA reviewed 2026-08-02 |
| SOL-G-006 | Inland/elevated town; November | Nuwara Eliya, `1232783` | 2026-11-15 | 6.97078 | 80.78286 | Sea level fixed; catalogue elevation 1868 m deliberately not applied | `Asia/Colombo` | Same frozen sources | Same | Approved under PA-004; QA reviewed 2026-08-02 |
| SOL-G-007 | Exact lower range and historical zone offset | Colombo, `1248991` | 1900-01-01 | 6.93548 | 79.84868 | Sea level fixed | `Asia/Colombo` tzdata 2026.3 | Same frozen sources | Same | Approved under PA-004; QA reviewed 2026-08-02 |
| SOL-G-008 | Upper range with in-range following sunrise | Colombo, `1248991` | 2100-12-30 | 6.93548 | 79.84868 | Sea level fixed | `Asia/Colombo` | Same frozen sources | Same | Approved under PA-004; QA reviewed 2026-08-02 |
| SOL-G-009 | Ordinary January date | Jaffna, `1242833` | 2026-01-15 | 9.66845 | 80.00742 | Sea level fixed | `Asia/Colombo` | Same frozen sources | Same | Approved under PA-004; QA reviewed 2026-08-02 |
| SOL-G-010 | September equinox period | Galle, `1246294` | 2026-09-22 | 6.04610 | 80.21030 | Sea level fixed | `Asia/Colombo` | Same frozen sources | Same | Approved under PA-004; QA reviewed 2026-08-02 |

### Frozen expected anchors

| Test ID | Sunrise UTC / local | Sunset UTC / local | Following sunrise UTC / local |
|---|---|---|---|
| SOL-G-001 | `2026-03-20T00:44:56.035Z` / `2026-03-20T06:14:56.035+05:30` | `2026-03-20T12:51:22.417Z` / `2026-03-20T18:21:22.417+05:30` | `2026-03-21T00:44:26.837Z` / `2026-03-21T06:14:26.837+05:30` |
| SOL-G-002 | `2026-06-21T00:22:47.376Z` / `2026-06-21T05:52:47.376+05:30` | `2026-06-21T12:55:40.734Z` / `2026-06-21T18:25:40.734+05:30` | `2026-06-22T00:23:00.521Z` / `2026-06-22T05:53:00.521+05:30` |
| SOL-G-003 | `2026-12-21T00:51:03.091Z` / `2026-12-21T06:21:03.091+05:30` | `2026-12-21T12:24:47.687Z` / `2026-12-21T17:54:47.687+05:30` | `2026-12-22T00:51:32.940Z` / `2026-12-22T06:21:32.940+05:30` |
| SOL-G-004 | `2028-02-29T00:51:36.008Z` / `2028-02-29T06:21:36.008+05:30` | `2028-02-29T12:51:40.437Z` / `2028-02-29T18:21:40.437+05:30` | `2028-03-01T00:51:14.942Z` / `2028-03-01T06:21:14.942+05:30` |
| SOL-G-005 | `2026-08-02T00:26:45.331Z` / `2026-08-02T05:56:45.331+05:30` | `2026-08-02T12:55:55.787Z` / `2026-08-02T18:25:55.787+05:30` | `2026-08-03T00:26:51.454Z` / `2026-08-03T05:56:51.454+05:30` |
| SOL-G-006 | `2026-11-15T00:27:10.909Z` / `2026-11-15T05:57:10.909+05:30` | `2026-11-15T12:15:35.867Z` / `2026-11-15T17:45:35.867+05:30` | `2026-11-16T00:27:29.132Z` / `2026-11-16T05:57:29.132+05:30` |
| SOL-G-007 | `1900-01-01T00:52:16.656Z` / `1900-01-01T06:11:48.656+05:19:32` | `1900-01-01T12:36:05.446Z` / `1900-01-01T17:55:37.446+05:19:32` | `1900-01-02T00:52:42.488Z` / `1900-01-02T06:12:14.488+05:19:32` |
| SOL-G-008 | `2100-12-30T00:51:01.364Z` / `2100-12-30T06:21:01.364+05:30` | `2100-12-30T12:34:43.223Z` / `2100-12-30T18:04:43.223+05:30` | `2100-12-31T00:51:28.034Z` / `2100-12-31T06:21:28.034+05:30` |
| SOL-G-009 | `2026-01-15T01:00:41.297Z` / `2026-01-15T06:30:41.297+05:30` | `2026-01-15T12:37:57.184Z` / `2026-01-15T18:07:57.184+05:30` | `2026-01-16T01:00:53.859Z` / `2026-01-16T06:30:53.859+05:30` |
| SOL-G-010 | `2026-09-22T00:28:32.364Z` / `2026-09-22T05:58:32.364+05:30` | `2026-09-22T12:35:18.335Z` / `2026-09-22T18:05:18.335+05:30` | `2026-09-23T00:28:21.051Z` / `2026-09-23T05:58:21.051+05:30` |

Each implementation record must add actual production values, signed/absolute differences, iteration diagnostics, runtime/API/device, and reviewer. A difference above tolerance, changed source bytes, event/no-event disagreement, or local-date mismatch blocks acceptance. Golden expected values are immutable unless PA-004 is superseded through an explicit source/profile change; they are never edited to pass.

### SOLAR-INTERMEDIATE-001-v1.0 — frozen equation fixture

This fixture is a test oracle for conformance to the exact SOL-R-006–SOL-R-009 production specification, not an astronomical-accuracy oracle. The Lead Coordinator prepared it on 2026-08-02 with a throwaway Python 3.12 reference evaluator outside the repository, independently of any Android/production implementation, by direct transcription from frozen NOAA SRC-020 (`D013D001A7620645C3D1DD23B8D876F76D6FBAE577DF295330CB494A0C6CD3B5`) plus the project-owned `90.8333°`/iteration rules. The source spreadsheet's cached cells were separately checked for shared Julian-century, `L0`, `M`, `e`, `C`, apparent-longitude, obliquity, declination, `y`, and equation-of-time formula families. This fixture may be superseded only with the engine/profile approval unit; production code must not generate its own expected values.

Input is SOL-G-001's Colombo coordinates, UTC base date `2026-03-20`, and `JD0=2461119.5`. Every decimal literal below is the shortest round-trip representation of the exact reference binary64 value, not a display-rounded value. Implementation tests parse the literals as binary64, compare each intermediate within 1 ULP, and require identical final anchor bits across supported runtimes.

| Event/iteration | Candidate JD | T | Declination ° | Equation of time min | Hour-angle argument | H ° | Event minutes UTC | Change seconds |
|---|---:|---:|---:|---:|---:|---:|---:|---:|
| Sunrise 1 | 2461119.2781981113 | 0.26212945100920837 | −0.3286593209880223 | −7.646144624173019 | −0.013952991387700578 | 90.7994734605729 | 45.05353078188142 | initial |
| Sunrise 2 | 2461119.531287174 | 0.2621363802101083 | −0.22859147230573373 | −7.571530278056583 | −0.014165320818715469 | 90.81164024336111 | 44.930249304612076 | 7.396888636160668 |
| Sunrise 3 | 2461119.531201562 | 0.26213637786617144 | −0.22862532052225423 | −7.571555541176036 | −0.014165248990737305 | 90.81163612750815 | 44.93029103114337 | 0.00250359187759841 |
| Sunrise 4 | 2461119.531201591 | 0.2621363778669619 | −0.22862530910704823 | −7.571555532656142 | −0.014165249014961067 | 90.81163612889621 | 44.930291017071234 | 8.443281274139736e-7; converged |
| Sunset 1 | 2461119.2781981113 | 0.26212945100920837 | −0.3286593209880223 | −7.646144624173019 | −0.013952991387700578 | 90.7994734605729 | 771.4493184664645 | initial |
| Sunset 2 | 2461120.0357286935 | 0.26215019106621423 | −0.02917526298405965 | −7.422412250221574 | −0.014588578749618292 | 90.83589364330668 | 771.3712668234483 | 4.683098580974274 |
| Sunset 3 | 2461120.035674491 | 0.2621501895822304 | −0.029196687661928403 | −7.4224283005716325 | −0.014588533266790677 | 90.83589103705525 | 771.3712724487926 | 0.00033752065974113066; converged |

| Event/iteration | L0 ° | M ° | e | C ° | Apparent longitude ° | Corrected obliquity ° | y | Denominator |
|---|---:|---:|---:|---:|---:|---:|---:|---:|
| Sunrise 1 | 357.32851228020263 | 9793.940388809571 | 0.016697606158476645 | 1.84909233137494 | 359.17370167293683 | 23.438256693551793 | 0.04303062293532319 | 0.9926664262226056 |
| Sunrise 2 | 357.57796884801064 | 9794.18983346068 | 0.01669760586673356 | 1.851228805152615 | 359.42529575148126 | 23.43825637950685 | 0.043030621749404134 | 0.9926748572152364 |
| Sunrise 3 | 357.5778844644774 | 9794.189749081179 | 0.01669760586683225 | 1.8512280884753571 | 359.42521065092 | 23.438256379613104 | 0.04303062174980539 | 0.9926748548753648 |
| Sunrise 4 | 357.5778844929355 | 9794.189749109635 | 0.016697605866832214 | 1.8512280887170576 | 359.4252106796199 | 23.43825637961307 | 0.04303062174980525 | 0.9926748548761539 |
| Sunset 1 | 357.32851228020263 | 9793.940388809571 | 0.016697606158476645 | 1.84909233137494 | 359.17370167293683 | 23.438256693551793 | 0.04303062293532319 | 0.9926664262226056 |
| Sunset 2 | 358.0751703020287 | 9794.687011163072 | 0.016697605285249186 | 1.855380711240852 | 359.92665117809804 | 23.438255753184375 | 0.04303061938424032 | 0.9926826290213976 |
| Sunset 3 | 358.07511687746955 | 9794.686957741063 | 0.016697605285311667 | 1.855380272733848 | 359.92659731480984 | 23.438255753251703 | 0.043030619384494574 | 0.9926826288323141 |

The final unquantized instants are `2026-03-20T00:44:55.817461Z` and `2026-03-20T12:51:22.276347Z`; half-even epoch-millisecond outputs are `2026-03-20T00:44:55.817Z` and `2026-03-20T12:51:22.276Z`, with quantization deltas `−0.461 ms` and `−0.347 ms`. T-SOL-003 must store every table entry as a test literal, not calculate expected values through production helpers.

## SOLAR-001 behavior, boundary, and integration cases

| ID | Input/trigger | Expected result/invariant | References | Evidence state |
|---|---|---|---|---|
| SOL-B-001 | Missing location. | `Unavailable(MissingLocation)`; no zero/default coordinate inside the engine. | SOL-R-003, SOL-R-011 | Approved normative case; execution pending |
| SOL-B-002 | Latitude NaN, infinities, `−90.000001`, `90.000001`; valid exact `−90`, `90`. | Invalid values return `InvalidCoordinate(latitude,…)`; exact endpoints are valid input and yield a typed event/no-event outcome, never normalization. | SOL-R-003, SOL-R-011 | Approved normative case; execution pending |
| SOL-B-003 | Longitude NaN, infinities, `−180.000001`, `180.000001`; exact `−180`, `180`; negative zero. | Invalid values rejected; endpoints preserved; negative zero canonicalized only for key/provenance. | SOL-R-003 | Approved normative case; execution pending |
| SOL-B-004 | Dates `1899-12-31`, `1900-01-01`, `2100-12-31`, `2101-01-01`. | Outside dates unsupported. A day request at bounds is allowed; a complete triple at 2100-12-31 is `UnsupportedDate` for following sunrise; pre-sunrise context at lower bound fails if it needs D−1. | SOL-R-003, SOL-R-005, SOL-R-011 | Approved boundary case; execution pending |
| SOL-B-005 | Repeat one golden request 1,000 times and concurrently on supported JVM/API runtimes. | Identical quantized instants, statuses, and deterministic diagnostics; no mutable-state race. | SOL-R-006–SOL-R-009, SOL-R-013 | Planned property/concurrency evidence |
| SOL-B-006 | Device-mode zone changes while coordinates stay fixed. | Coordinator invalidates the context, derives the new IANA civil date/day bounds, recalculates atomically, and never derives zone from longitude. | SOL-R-004, CR-008, CR-010 | Planned clock/zone evidence |
| SOL-B-007 | Manual Colombo/Kandy selected; device zone changes. | Calculation zone remains `Asia/Colombo`; same absolute anchors format through that zone. SOL-G-007 proves historical rules are not fixed `+05:30`. | SOL-R-004; VC-013, VC-014 | Planned integration evidence |
| SOL-B-008 | IANA date with 23/25-hour offset change and a skipped-date fixture. | Use `[atStartOfDay(D),atStartOfDay(D+1))`; normal overlap/gap dates retain unique local association; non-positive interval returns `SkippedCivilDate`. | SOL-R-004 | Planned JVM zone-rules tests |
| SOL-B-009 | `90,0,UTC,2026-03-20`; `−90,0,UTC,2026-03-20`; Longyearbyen `78.2232,15.6469,Europe/Oslo` on 2026-06-21 and 2026-12-21. | Each exact pole returns `NoSunriseAndNoSunset(date)` from degenerate geometry. Each Longyearbyen solstice case returns `NoSunriseAndNoSunset(date)` from no crossing; never neighbour-day/twilight substitution. | SOL-R-008, SOL-R-011, SOL-R-012 | Exact high-latitude fixtures |
| SOL-B-010 | SOL-G-001 final-iteration seam uses the exact binary64 `positiveOuter = 1.0 + 1e-12` computed once in that order, then `nextDown(positiveOuter)`, `positiveOuter`, and `nextUp(positiveOuter)`; mirror around `negativeOuter = −1.0 − 1e-12`. Also test `positiveInner`, `1.0`, `negativeInner`, and `−1.0`. | Inner/endpoint values through the inclusive outer bound return `AmbiguousSolarEvents(Sunrise,2026-03-20,GrazingHorizon)` when the tentative instant belongs to the day. `nextUp(positiveOuter)` and `nextDown(negativeOuter)` are absent with `HorizonNotCrossed`. No endpoint is published as a crossing. | SOL-R-008 | Exact binary64 numerical/grazing fixture |
| SOL-B-011 | SOL-G-001 collector seam supplies sunrise candidates `2026-03-20T00:44:55.817Z` and `2026-03-20T00:45:55.817Z`; separate seams force sixth iteration, non-finite, and overflow. A successful D plus absent D+1 sunrise is also injected. | Multiple candidates return `AmbiguousSolarEvents(Sunrise,2026-03-20,MultipleCandidates)`. Ordered failures return respectively `ConvergenceFailure`, `NonFiniteIntermediate`, or `ArithmeticOverflow` with event/date/base-date fields. The adjacent-day seam returns `NoSunrise(FollowingSunrise,2026-03-21)`; no partial triple. | SOL-R-008, SOL-R-011 | Exact aggregation/failure fixtures |
| SOL-B-012 | Unquantized values immediately below/at/above half millisecond, including even/odd target milliseconds. | One nearest-millisecond half-even quantization; absolute error `<=0.500 ms`. | SOL-R-009, SOL-R-014 | Exact rounding fixture |
| SOL-B-013 | Persist and restore manual town/device record with microdegree coordinates/profile selection. | Input coordinates/mode round-trip exactly under ADR-005; solar anchors/schedules are not persisted and recalculate to the same deterministic result. | SOL-R-003, SOL-R-013; ADR-005 | Planned repository integration test |
| SOL-B-014 | Build Seasonal Planetary Hora from every golden triple. | Calculator receives exact SOLAR-001 anchor instants/fingerprint; CR-004 endpoints equal anchors; no second solar call; oracle-derived boundary difference `<=60 s`. | SOL-R-013, SOL-R-014; CR-004, VC-038 | Planned pure integration/property test |
| SOL-B-015 | Future approved daytime Rahu consumes a golden current-date pair; current CP-003 remains unapproved. | Shared exact sunrise/sunset and fingerprint are available to the context, but Rahu remains `Unavailable(ProfileNotApproved)` until PA-003; no recalculation. | SOL-R-013; RK-001, VC-034, VC-038 | Architecture obligation; CP-003 Blocked |
| SOL-B-016 | Format SOL-G-007 through `Asia/Colombo`; also test exact half-minute even/odd ties, local-date carry, and IANA overlap/gap wall labels. | Historical dashboard labels are sunrise `06:12`, sunset `17:56`, following sunrise `06:12`; diagnostic labels are `06:11:49`, `17:55:37`, `06:12:14`, retaining original `+05:19:32` in Method view. Wall-time half-even rules, direct label semantics, one shared formatting pass, and unchanged instant membership apply to every transition fixture. | SOL-R-014; VC-015 | Exact formatter fixture plus planned property test |

The implementation test suite must also compare the engine with the NREL Appendix A.5 published SPA worked example as an oracle-regression check. That example uses elevation, pressure, temperature, and a non-Sri Lankan site, so it validates the host-side SPA method only and is not a CP-001 end-to-end golden.

## Required case record

Every concrete golden/release record must include:

- Case ID, requirement/rule IDs, purpose, reviewer, and approval state.
- Input coordinates, display location, reported accuracy, permission precision, location source, and source-specific time/provenance: acquisition time for device fixes, selection time for manual locations, or dataset/version for defaults.
- Civil date, instant, active IANA zone, calculation time, engine version, and every applicable calculation-profile version; include CP-001 solar-convention provenance only when explicitly inherited/consumed.
- Calculated sunrise, sunset, following sunrise, relevant intermediate boundaries, and ruler sequence.
- Expected result, actual result, accepted tolerance, independent/traditional evidence, and explanation for every difference.
- Calculation-system ID, profile/rule/source/evidence-state IDs, exact internal-duration semantics, display-policy version, and shared-context fingerprint for CR-001 cases.
- Test environment/API/device where relevant, including precise, approximate, denied, disabled, stale, manual, and restart states.

## Physical/API matrix

After provisional `minSdk 26` is implemented, verify API 26, 30, 31/32, and 36 plus the actual oldest family/OEM device. Instrumented permission behavior supplements, but does not replace, pure deterministic engine tests. VC-020–VC-039 definitions/obligations are approved under CR-001; unresolved expected results in VC-022/VC-026–VC-028 must remain blank/blocked rather than be invented.
