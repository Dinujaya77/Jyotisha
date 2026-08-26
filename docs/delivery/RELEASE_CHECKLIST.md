# Release Checklist

| Field | Value |
|---|---|
| Status | Proposed |
| Version | 0.1 |
| Last updated | 2026-08-01 |
| Owner role | Lead Coordinator |
| Approval state | No release authorized |

## Approval and scope

- Version/scope, requirements, calculation profile/rules, validation cases, UI/theme, architecture, dependencies, and privacy decisions are approved and traceable.
- All milestone tasks and severity findings meet exit criteria; deviations have owners.

## Verification

- Unit, UI/instrumented, accessibility, locale/time-zone/location, validation, lint, performance, privacy/security, backup/restore, install/upgrade, and release-build checks pass as applicable.
- Supported family devices and Android versions are exercised; offline and failure behavior is checked.

## Distribution and safety

- Application ID/name/versioning, signing ownership, secure credential handling, family-only delivery/update/rollback steps, APK integrity verification, licenses/notices, data disclosure, and recovery contacts are approved.
- No secrets, private test data, debug-only configuration, or unintended logs are packaged or committed.

Do not request real signing credentials in a task prompt or store them in Git.
