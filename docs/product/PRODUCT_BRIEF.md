# Product Brief

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.1 |
| Last updated | 2026-08-02 |
| Owner role | Business Analyst |
| Approval state | PA-001 product brief and CR-001 Option C amendment approved |

## Confirmed product context

Jyotisha is a private, family-oriented Android application initially intended for the owner's parents and relatives. It will be maintained in Git and manually installed as a signed APK. Version 1.0 has no backend, login, advertising, analytics, cloud synchronization, or background location. Its primary value is a clear, trustworthy, primarily offline answer to: “Which planetary Hora is active now, when does it end, and what comes next?”

The existing single-module Compose application is the implementation starting point. It currently has no Hora, solar-event, location, persistence, or production UI behavior.

## Version 1.0 outcome

Version 1.0 provides the current and next Hora, exact boundaries and countdown, a continuous 24-Hora sunrise-to-following-sunrise timeline, calculated astronomical sunrise and sunset, current-device or manual Sri Lankan location, transparent provenance and methodology, English resources prepared for future Sinhala translation, system light/dark appearance, and accessibility for older family members.

Under approved Change Request CR-001, the existing result is named **Seasonal Planetary Hora** wherever ambiguity is possible. Approved Option C adds a separate daytime Rahu Kalaya status to Version 1.0 only after CP-003 and its validation evidence are separately approved. Fixed Sri Lankan Kala Hora and Panchama Kala are allocated to Version 1.1 because their next-sunrise coverage rule is unresolved. The allocation is approved; the profiles/rules and implementation remain separately gated.

All calculations remain available from a saved, manual, or default location without a network connection. Current device location is a foreground, user-understood, one-shot operation. The app performs calculation, persistence, and fallback processing on-device and makes no backend upload; the selected OS/location-provider acquisition path and any provider-level network/data behavior must be disclosed by the architecture privacy review.

## Trust and content boundary

Version 1.0 presents timing, ruler names, neutral educational explanations, methodology, and limitations. It does not present auspiciousness labels, favorable/unfavorable activity advice, predictions, remedies, or medical, financial, legal, or deterministic life advice.

The reference website `jothishya.lk` is competitor evidence only. Its wording, calculations, visual design, data, and implementation must not be copied or treated as authoritative.

## Success measures

- A family user can identify the current and next Hora without interpreting a complex chart.
- One coherent result explains its location, time zone, freshness, calculation time, profile, and limitations.
- The 24 intervals are deterministic, continuous, gap-free, and overlap-free.
- Location denial or failure never makes the app unusable while a manual/default fallback exists.
- The release passes documented calculation validation, accessibility checks, and physical-device testing.

## Provisional and unresolved items

- `minSdk 26` is provisional pending actual family-device inventory and build/device verification; `compileSdk 36` and `targetSdk 36` remain modern and independent.
- The exact Colombo coordinates and bundled Sri Lankan town dataset/provenance require approval before implementation.
- PA-002 selects the pure-Kotlin NOAA/Meeus method family and AndroidX `LocationManagerCompat` location architecture; PA-004 approves normative `SOLAR-001`. Its Kotlin implementation, independent production evidence, and milestone/task acceptance remain separately gated.
- Location timeout/staleness/movement/uncertainty thresholds are approved product and PA-002 architecture constraints, subject only to an explicitly approved revision.
- A trusted Sri Lankan practitioner or printed Panchanga is not yet nominated; this blocks final V1.0 release approval, not drafting, architecture, UI, implementation, or automated tests.
- Permanent Android identity is approved and implemented under V1-M1-02: visible name `Jyotisha`, namespace and application ID `io.github.dinujaya77.jyotisha`.
- Change Request CR-001, Option C, PA-002 architecture, DA-001 Direction A, PA-004 SOLAR-001, DA-004 final UI, and DP-001 delivery were approved 2026-08-02. M1 development has started through explicitly assigned tasks; CP-003/RK, exact town data, dependency evidence, later implementation evidence, and release gates remain separate.
- The Sri Lankan fixed Kala/Panchama profile remains blocked on fixed-period coverage at following sunrise, day/night behavior, the approved five-subdivision matrix, and reviewed Sinhala terminology.
- Daytime Rahu is proposed for V1.0, but its exact selected Sri Lankan convention and golden cases remain pending; nighttime Rahu is not proposed for V1.0.
