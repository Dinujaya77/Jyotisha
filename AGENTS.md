# Jyotisha repository instructions

## Purpose and sources of truth

Jyotisha is an early family-only Android app. Preserve the existing project and implement only approved milestone tasks. Approved product documents in `docs/product/`, calculation documents in `docs/domain/`, design specifications in `docs/design/`, engineering decisions in `docs/engineering/`, and delivery records in `docs/delivery/` are the sources of truth. When documents conflict, stop and route the conflict to the Lead Coordinator; do not silently choose.

## Delivery workflow and gates

Use the Lead Coordinator to scope work. Business Analyst, Project Manager, Android Architect, UI/UX Designer, and QA Reviewer are read-only. Only the Android Developer may normally edit production Android files. Prefer parallel agents only for independent read-heavy analysis; never run write-enabled agents concurrently on the same files.

Required gates: approved requirements and acceptance criteria; approved calculation profile/rules and validation cases for calculation work; approved UI/theme decisions for screens; approved architecture/dependencies for structural changes; Developer verification; independent QA; Lead acceptance. Use only these states: Proposed, Awaiting Approval, Approved, Ready, In Progress, In Review, Blocked, Partially Complete, Done.

## Integrity, design, and engineering rules

- Never invent or silently alter Jyotisha rules. Record sources, tradition variations, time-zone/location assumptions, precision, and expected validation results.
- Use semantic design tokens. Keep UI accessible, readable, responsive, culturally respectful, and compatible with Compose previews; never depend on colour alone.
- Prefer Kotlin, Compose, Material 3, a single activity, unidirectional data flow, immutable UI state, ViewModels/StateFlow, pure deterministic calculation logic, and offline-first behavior. Avoid premature modules and abstractions.
- Add no dependency without approval and an entry in `docs/engineering/DEPENDENCY_REGISTER.md`.
- Tests must trace to requirement IDs and cover deterministic boundaries, errors, accessibility, lifecycle, locale, time-zone, and location behavior as applicable. Run focused unit tests, lint, and a debug build before handoff.
- Preserve unrelated changes. Do not commit secrets, signing material, private test data, generated output, or IDE-local state. Do not push. Commit only when the task authorizes it and the starting tree was clean.

## Final report

Report: state; scope completed; files changed; requirement/test references; exact commands and results; deviations; blockers/unresolved approvals; Git status; and recommended next prompt.
