# Scope

| Field | Value |
|---|---|
| Status | Approved |
| Version | 1.1 |
| Last updated | 2026-08-02 |
| Owner role | Business Analyst |
| Approval state | PA-001 scope and CR-001 Option C allocation approved |

## Version 1.0 — Family Hora release

In scope:

- Seasonal planetary Hora: 12 equal sunrise-to-sunset day intervals plus 12 equal sunset-to-following-sunrise night intervals.
- Current Hora/ruler, start, end, remaining duration, next Hora, and continuous 24-Hora timeline.
- Calculated astronomical sunrise and sunset using the approved profile.
- Preferred one-shot foreground current-device location with precise/approximate handling.
- Saved location, explicit manual Sri Lankan town selection, and provisional Colombo fallback.
- Device time zone for current-device mode; `Asia/Colombo` for manual Sri Lankan locations.
- Location provenance, accuracy state, freshness, calculation time, refresh, warnings, methodology, sources, and limitations.
- On-device/offline calculation and minimal local persistence; no coordinate backup.
- English interface, Sinhala-ready resources, system light/dark appearance, semantic tokens, and older-user accessibility.
- Signed APK delivery to family with no backend, account, advertisements, or analytics.

Approved CR-001 allocation, separately gated for implementation:

- Daytime Rahu Kalaya from eight exact sunrise-to-sunset segments, with start/end and active/upcoming status, only under an approved CP-003/RK rule set.
- Explicit `Seasonal Planetary Hora` naming and per-system methodology/provenance so Rahu is never confused with an eighth Hora ruler.

## Explicit Version 1.0 exclusions

- Equal-duration sunrise-to-next-sunrise 24ths.
- Background/continuous location, background permission, location foreground service, raw location history, geofencing, or location upload.
- Worldwide manual locations or guessing a time zone from longitude.
- Favorable/unfavorable activity recommendations, auspiciousness labels, predictions, remedies, medical/financial/legal/life advice.
- Panchanga beyond the approved Hora-related information.
- Sinhala translation content, user-selectable theme packs, widgets, notifications, history/date browsing, calendar export, and sharing.
- Birth charts, personal profiles, compatibility, consultations, payments, cloud sync, login, or backend.
- Copying competitor content, calculations, design, data, or implementation.
- Under approved Option C, fixed Sri Lankan Kala Hora, Panchama Kala, and a Kala/Panchama timeline. They are allocated to Version 1.1 and remain blocked pending CP-002/KH/PK approval. Nighttime Rahu remains deferred/unallocated.

## Proposed later versions

| Version | Proposed scope |
|---|---|
| 1.1 | Approved allocation for fixed Sri Lankan Kala Hora and five-part Panchama Kala experience after CP-002/traditional approval; reviewed Sinhala translation and optional location refinements remain proposed. |
| 1.2 | Knowledge library, separately validated advice if desired, theme packs, optional widget, notifications/date browsing. |
| 2.0 | Local personal profiles, birth charts, compatibility/predictions, and other personal calculations under new privacy and calculation profiles. |

## Gates

PA-001 remains the preserved historical baseline and CR-001's Option C allocation is approved. Architecture/design reconciliation may proceed, but their approval gates, applicable domain profiles/rules, final UI specification, delivery planning, implementation, external traditional validation, physical family-device testing, release privacy review, and signed-APK checks remain separate.
