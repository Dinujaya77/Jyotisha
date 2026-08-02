# Security and Privacy

| Field | Value |
|---|---|
| Status | Awaiting Approval |
| Version | 0.3 |
| Last updated | 2026-08-01 |
| Owner role | Android Architect |
| Approval state | V1.0 privacy requirements approved; ADR-002/ADR-003/ADR-005 controls await architecture approval |

## Confirmed V1.0 posture

- Private family-only signed APK; no backend, login, advertising SDK, analytics, remote configuration, or cloud sync.
- The application calculates and stores selected-location results on-device and makes no application/backend upload. OS or third-party location-provider acquisition may use platform/network-assisted behavior; the selected provider's data flow and offline characteristics require explicit ADR/privacy review and methodology disclosure.
- Declare/request only foreground `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`.
- No `ACCESS_BACKGROUND_LOCATION`, location foreground service, continuous tracking, raw device-location history, geofencing, upload, or location analytics.
- The app remains functional through saved/manual/default fallback when permission or services are unavailable.
- Do not declare `INTERNET` unless a later requirement and security/dependency approval justifies it.

## Data inventory and minimization

The only approved selected-location fields are latitude, longitude, optional display name, metre accuracy where provided, permission precision, source, acquisition timestamp, selected IANA time zone, and whether the fix is precise/approximate. Keep only the active selected location and explicitly saved manual locations required by approved requirements. Do not log exact coordinates or use real private coordinates in committed tests.

Phase B proposes one mutually exclusive Preferences DataStore record: schema version plus either device coordinates/accuracy/precision/acquisition/zone/source, a manual stable town ID/selection time, or default dataset provenance. Switching to manual/default deletes device coordinates; switching to device deletes the other mode payload. Derived solar/Hora schedules, refresh-attempt history, prior fixes, routes, and location trails are not stored.

If fine permission is downgraded to coarse-only, or foreground permission is revoked externally, ADR-003 proposes deleting the saved fine-derived device record. The app must not continue presenting a more precise stored fix as compatible with the user's current permission choice. This privacy/availability tradeoff remains unapproved until the architecture gate.

## Backup correction

The existing manifest enables Android backup. Auto Backup or device transfer could copy app-private coordinates, conflicting with an unqualified “no location upload” promise. V1.0 therefore requires location persistence to live in a dedicated location-data directory whose complete backup domain/path—including temporary, replacement, or companion artifacts—is excluded from both legacy backup rules and Android 12+ cloud/device-transfer extraction rules. Implementation must verify all actual installed paths and demonstrate that backup/restore and device transfer do not reproduce the record. If reliable exclusion cannot be proven, disable application backup before release. No production backup or extraction file is changed during Phase B.

Backup tests must use public/synthetic coordinates, inspect the installed storage domain/path, exercise configured backup tooling where available, restore/transfer to a clean test install, and assert that the location record is absent while permitted non-sensitive preferences behave as intended. A static XML review alone is insufficient release evidence.

## Permission experience

Ask in context after explaining local calculation use. Accept approximate-only access and do not repeatedly pressure for fine access. Because platform rationale signals do not universally prove permanent denial, use a neutral “permission unavailable through the in-app request flow” state and offer user-initiated Settings, manual town, and default fallback.

## Threats and controls

| Risk | Required control/evidence |
|---|---|
| Coordinate leakage through logs, crash data, fixtures, or screenshots | No analytics/crash uploader; logging/test-data audit; coordinates omitted or synthetic. |
| Coordinate backup/transfer | Explicit exclusion rules or disabled backup; restore/transfer verification. |
| Permission downgrade retains precise coordinates | Observe permission state on resume/restart; delete fine-derived device record when ADR-003 condition applies; test fine→coarse and revocation. |
| Manual/default mode silently retains device coordinates | Use mutually exclusive stored payloads; delete device fix on mode change; test on-disk state and failed later current-location attempt. |
| Corrupt/newer persisted schema | Reset only location data to labelled default; one recovery message; no coordinate logging. |
| Silent tracking | One-shot foreground abstraction; no update subscription/background permission/service; manifest/code review. |
| Coercive permission UX | Contextual rationale, manual/default equivalence, no repeated pressure, accessible recovery. |
| Stale or misleading provenance | Store source/time/precision separately; label fallback/stale state; never infer fine permission from metre accuracy. |
| Dependency data behavior | Record dependency, privacy/data-flow/license/security review before addition. |
| Signing-key compromise | Keep key/properties outside Git; never request credentials in prompts; document owner, backup, rotation/recovery privately. |

## Release gates

Manifest/permission audit, offline/network observation, storage/backup/transfer inspection, DataStore migration/corruption tests, permission-downgrade device tests, privacy/methodology content review, secret scan, and signed-APK verification must pass. Family-only distribution does not waive data-minimization or disclosure obligations.
