# Jyotisha

Jyotisha is an early Android application intended for private, family-only APK distribution. The repository now contains the completed M1 Android foundation and bounded application shell; no solar, Hora, Rahu, location-acquisition, or persistence feature is implemented yet.

## Current status

The delivery foundation and controlled Codex virtual team are established. Requirements, Option C, Version 1.0 architecture, Celestial Archive UI, `SOLAR-001`, and the delivery plan are approved. V1-M1-01 through V1-M1-05 and corrective commits `580bab1`/`79d5116` are incorporated into `dev`; M1 is **Done** and Lead/owner accepted. M2 is **In Progress** on its retained milestone branch: V1-M2-01 implements the approved semantic light/dark color foundation, while V1-M2-02 through V1-M2-04 and every later milestone remain unstarted. No remote push occurred. CP-001 Seasonal Planetary Hora remains the Version 1.0 primary system; daytime Rahu requires separate CP-003/RK approval, while fixed Kala/Panchama is allocated to Version 1.1 and remains blocked on CP-002/KH/PK approval. DEP-011–DEP-013 Lifecycle 2.9.2 are added and verified; DEP-014 DataStore remains deferred.

## Project baseline

- One Android application module: `:app`
- Application ID and namespace: `io.github.dinujaya77.jyotisha`
- `compileSdk`: 36; `targetSdk`: 36; provisional `minSdk`: 26. API 26 runtime and physical-family-device verification remain later gates.
- Kotlin 2.0.21; Java source/target and Kotlin JVM target 11
- Jetpack Compose enabled with Material 3
- Entry point: `app/src/main/java/io/github/dinujaya77/jyotisha/MainActivity.kt`

## Setup

1. Install Android Studio with Android SDK 36 and compatible build tools.
2. Use a JDK compatible with Gradle 8.13 and Android Gradle Plugin 8.11.2 (Android Studio's bundled JDK is recommended).
3. Open the repository and allow Gradle sync to create or update the ignored `local.properties` SDK path.
4. Do not add signing credentials to the repository. Debug signing is sufficient for development checks.

Windows commands:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest
.\gradlew.bat lintDebug
```

macOS/Linux equivalents use `./gradlew`.

## Documentation map

- `docs/product/`: product brief, scope, requirements, stories, and approvals
- `docs/domain/`: terminology, calculation profile and rules, sources, variations, and validation cases
- `docs/design/`: information architecture, visual direction, UI/theme specifications, accessibility, and design approvals
- `docs/engineering/`: baseline architecture, decisions, dependencies, security/privacy, and review checklist
- `docs/delivery/`: versions, milestones, tasks, tests, traceability, release checks, and current status

## Codex workflow

Root `AGENTS.md` defines repository-wide gates. Project-scoped agents under `.codex/agents/` support analysis, planning, architecture, design, implementation, QA, and final coordination. Use read-only agents in parallel only for independent analysis; use one Android Developer for an approved implementation task, followed by independent QA and Lead Coordinator acceptance.

The source of truth is the approved repository documentation, not an agent's unstated assumption. Calculation rules, tradition choices, time/location behavior, dependencies, and UI decisions require documented approval before production changes.

## Distribution

The current intention is private family-only APK distribution. Release signing, update delivery, supported devices, privacy disclosures, and distribution mechanics are not yet approved and must be resolved before a release milestone.
