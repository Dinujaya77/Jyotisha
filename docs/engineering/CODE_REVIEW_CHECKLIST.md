# Code Review Checklist

| Field | Value |
|---|---|
| Status | Ready |
| Version | 0.1 |
| Last updated | 2026-08-01 |
| Owner role | QA Reviewer |
| Approval state | Operational foundation checklist |

## Scope and traceability

- Change matches one approved task and allowed files; unrelated edits are absent.
- Requirement, domain rule, UI spec, ADR, and test IDs are linked where applicable.
- Deviations and unresolved decisions are visible.

## Correctness and quality

- State/data flow, errors, lifecycle, concurrency, locale, time-zone, location, rounding, and boundaries are correct.
- Calculation logic is pure/deterministic and backed by approved validation cases.
- No new dependency, permission, data flow, or backup behavior lacks approval.
- Accessibility, adaptive layouts, semantic tokens, performance, security, privacy, and older-user usability are checked.
- Tests cover behavior and regressions rather than only implementation details.

## Verification

- Focused tests, `testDebugUnitTest`, `lintDebug`, and `assembleDebug` pass or exact environmental blockers are recorded.
- QA findings are severity-ranked and re-verified after fixes.
- Git diff/status contain no secrets, generated files, private data, or unintended changes.
