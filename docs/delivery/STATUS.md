# Delivery Status

| Field | Value |
|---|---|
| Status | In Progress |
| Version | 0.6 |
| Last updated | 2026-08-02 |
| Owner role | Lead Coordinator |
| Approval state | PA-001 and Change Request CR-001 approved; Phase B architecture and design reconciliation authorized but not approved |

## Current phase

**Requirements Amendment CR-001 and Option C are Approved.** On 2026-08-02 the repository owner supplied the exact phrase `APPROVE REQUIREMENTS AMENDMENT CR-001`, approving only the staged Version 1.0/1.1 allocation, FR-012–FR-015, NFR-015, US-011–US-013, conditional deltas, and linked validation obligations/case definitions. Phase B architecture/design reconciliation is now In Progress; their approvals, all new calculation profiles/rules, delivery planning, and application implementation remain unauthorized.

The approved product allocation is **Option C**: retain approved CP-001 Seasonal Planetary Hora as the Version 1.0 primary system; add daytime Rahu (CP-003) only after separate profile/RK approval; allocate fixed Sri Lankan Kala/Panchama (CP-002) to Version 1.1. CP-002 is implementation-blocked because, under elapsed-duration semantics or on offset-stable dates, 24 fixed 60-minute periods do not generally end at the next astronomical sunrise; wall-clock civil-minute behavior across offset transitions is also unresolved. Available sources do not define the required gap/overlap/reset/partial-period rule. Nighttime Rahu and final Sinhala terminology remain unresolved.

Phase A is Approved. On 2026-08-01 the repository owner supplied `APPROVE VERSION 1.0 REQUIREMENTS`, approving the Version 1.0 product requirements, scope, CP-001, CR-001–CR-010, and validation strategy subject to the documented architecture decisions and release-validation blockers.

The original Phase B analysis is preserved but its approval gate is held. The Android Architect had completed read-only solar, location, town-data, persistence, lifecycle, dependency, testing, and proportionality analysis. The UI/UX Designer had completed read-only information architecture plus Celestial Archive, Astral Observatory, and Moonlit Sanctuary directions. CR-001 amendments retain the one-module proposal and all three directions while adding explicit timing-system boundaries and conditional hierarchies. No Kotlin, Compose production UI, Manifest, Gradle, resource, branch, worktree, merge, or push change occurred.

The independent QA Reviewer found zero Critical, three High, six Medium, and one Low Phase B documentation issues. Corrections made persistence payloads mutually exclusive, required a normative approved `SOLAR-001` before code, prevented premature production authorization, broadened backup exclusion, clarified direction evidence and Back behavior, restored VC-019 traceability, expanded dependency evidence, separated lifecycle cancellation outcomes, and fixed performance wording. QA re-verified all ten findings resolved with no new Critical/High issues. `UI_SPEC.md` remains unfinalized.

## Phase B proposal outcome

- Solar: local pure-Kotlin NOAA/Meeus-style method family; `SOLAR-001` must freeze exact equations/constants/calendar/sign/root/rounding/failure/golden-vector behavior and receive independent approval before implementation. NREL SPA is the validation reference; Solarpositioning and Astronomy Engine were evaluated but not selected for V1.
- Location provider: existing AndroidX Core `LocationManagerCompat.getCurrentLocation()` behind `DeviceLocationProvider`; no Google Play Services dependency.
- Location policy: 20-second request; fresh through 2 minutes; stale at 24 hours; movement at 10 km; replacement improvement requires both 50 m and 25%; warning over 10 km uncertainty; maximum usable uncertainty 20 km; approximate remains usable within the ceiling.
- Town catalogue: frozen/reviewed GeoNames `LK.zip` CC BY 4.0 snapshot; nine proposed province-representative towns; no coordinate is guessed or approved yet.
- Persistence: Preferences DataStore 1.2.1 with one mutually exclusive device/manual/default payload, full deletion on mode/permission downgrade rules, dedicated backup-excluded directory, migrations/corruption/reset tests, and no history.
- Structure: one app module/activity, pure domain engines, immutable `StateFlow`, atomic snapshots, manual composition, and only four proposed AndroidX additions (DEP-011–DEP-014).
- Design: three-destination Now/Day/Method IA plus focused location flow; A/B/C all cover required location/error/theme/accessibility/motion states. The design recommendation is A, while the owner must select exactly one.
- CR-001 amendment: separate `PlanetaryHora`, `SriLankanKalaHora`, `PanchamaKala`, and `RahuKala` calculators over one immutable shared context; no new dependency/module. Recommended Option C; daytime Rahu is proposed for V1.0, fixed Kala/Panchama for V1.1. No direction is approved and `UI_SPEC.md` remains stopped.

## Foundation milestone

M-000 establishes the controlled Codex virtual team and documentation/verification foundation. No Jyotisha feature or intentional application behavior change is part of this milestone.

## Existing repository state before work

- Branch: `dev`
- HEAD: `dfbdb20` (`initial application setup`); `origin/main`, `origin/dev`, and local `main` pointed to the same commit.
- Worktree was not clean: untracked Android Studio files existed under `.idea/`.
- Consequence: the requested documentation checkpoint commit cannot be created under the repository rule requiring a clean starting tree, even though `.idea/` is now covered by an ignore rule. The files were not deleted or edited.

## Baseline verification (2026-08-01)

Environment command for successful sandbox runs:

```powershell
$env:GRADLE_USER_HOME=(Join-Path (Get-Location) '.gradle-codex')
$env:ANDROID_USER_HOME=(Join-Path (Get-Location) '.android-codex')
```

| Check | Exact command | Result |
|---|---|---|
| Initial configuration attempt | `.\gradlew.bat projects --console=plain` | Failed before configuration: Gradle tried to create `C:\.gradle\...zip.lck`, which the sandbox could not write. |
| Wrapper/cache retry | `.\gradlew.bat projects --console=plain` with repository-local `GRADLE_USER_HOME` | Timed out after 181 seconds after successfully downloading Gradle 8.13 and starting the daemon. |
| Configuration | `.\gradlew.bat projects --console=plain --no-daemon` with both local environment variables | Passed in 44 seconds; root `Jyothisha` and `:app` discovered. |
| Unit tests | `.\gradlew.bat testDebugUnitTest --console=plain --no-daemon` with both local environment variables | Passed in 2m 4s; 22 tasks executed. Kotlin daemon writes to the sandboxed user profile were denied, so compilation fell back successfully without the Kotlin daemon. |
| Lint | `.\gradlew.bat lintDebug --console=plain --no-daemon` with both local environment variables | Passed in 2m 10s; report generated under ignored `app/build/reports/`. |
| Debug build | `.\gradlew.bat assembleDebug --console=plain --no-daemon` with both local environment variables | Passed in 1m 16s; native `libandroidx.graphics.path.so` could not be stripped and was packaged unchanged. |

All successful Gradle runs emitted a non-fatal metrics warning because the Android tooling also attempted `C:\.android\analytics.settings`. This did not fail configuration, tests, lint, or assembly. Generated build output and repository-local caches are ignored.

Static validation parsed `.codex/config.toml` and all seven agent TOML files successfully, confirming required fields and the requested sandbox modes. All 29 required documentation files and metadata fields were also found. A supplementary `codex --version` / `codex features list` check could not run because this environment denied execution of `codex.exe`, including on an escalated retry; this is an environment limitation, not a TOML parse failure.

## Phase B verification (2026-08-01)

| Check | Exact command/approach | Result |
|---|---|---|
| Branch/scope | `git -c safe.directory='C:/Abishek Data/Applications/Jyotisha' status --short --branch` plus `diff --name-status` for app/Gradle paths | Passed: branch `dev`; no Android source or Gradle diff |
| Patch whitespace | `git -c safe.directory='C:/Abishek Data/Applications/Jyotisha' diff --check` plus `rg -n '[ \t]+$' README.md AGENTS.md .codex docs` | Passed; no whitespace errors |
| Secrets/private/signing | Targeted `rg` credential/key scan; untracked-file and ignored-signing audit | Passed; no proposed file contains a secret, private family coordinate, keystore, APK/AAB, or generated build output. Ignored sandbox debug keystore is outside Git. |
| Documentation schema | PowerShell metadata parse plus `rg` ID counts | Passed: 29 documents; 11 FR, 14 NFR, 10 CR, 19 VC, 7 ADR, and 7 IA records |
| QA | Independent Phase B review and read-only re-verification | Passed: all ten findings resolved; no Critical/new High |
| Android verification | Not rerun | Documentation-only Phase B; prior baseline unit/lint/debug checks remain unchanged |

## Change Request CR-001 verification (2026-08-01)

| Check | Exact command/approach | Result |
|---|---|---|
| Agent review | Read-only Business Analyst, Android Architect, UI/UX Designer, and independent QA Reviewer | Completed; no agent edited files |
| Documentation schema/counts | PowerShell metadata inventory plus targeted `rg` counts | Passed: 30 Markdown documents; 15 FR, 15 NFR, 13 US, 3 CP, 10 preserved CR plus 5 KH/4 PK/5 RK, 39 VC, 18 sources, 15 tradition variations, 29 terms, 9 ADR, and 8 IA records |
| Patch/whitespace/secrets | `git diff --check`; targeted trailing-whitespace and credential/key scans across `README.md`, `AGENTS.md`, `.codex`, and `docs` | Passed; no whitespace error or credential/private-key pattern |
| Android/Gradle scope | Scoped `git status --short` and `git diff --name-status` for `app`, Kotlin/KTS/XML, Manifest, Gradle wrapper/configuration | Passed: no Android source, Compose, resource, Manifest, Gradle, or dependency change |
| Git scope | `branch --show-current`, `rev-parse --short HEAD`, `worktree list --porcelain`, full status | Passed: `dev`, `dfbdb20`, one worktree; no commit/branch/merge/push; pre-existing dirty/untracked foundation state retained |
| Independent CR-001 QA | Timing systems, sources, uncertainties, sequences, boundaries, scope, accessibility, traceability, release/code gates | Initial: 0 Critical, 1 High, 2 Medium, 1 Low; all four corrected. Read-only recheck: 0 Critical/High/Medium/Low open; Ready for Lead acceptance |

The corrections precisely bounded amendment approval, qualified elapsed-versus-civil fixed-duration wording, made golden records profile-neutral, and added this current verification record. Genuine traditional uncertainty remains unresolved rather than being closed as a documentation defect.

## Blockers and unresolved decisions

- No blocker prevents completing M-000; it remains Done.
- The dirty starting worktree blocks the optional commit under the task's Git rule.
- Requirements, CP-001, CR-001–CR-010, and the linked validation strategy are approved under PA-001.
- ADR-001–ADR-007 and DEP-011–DEP-014 await architecture approval. Even after it, solar implementation remains blocked until normative `SOLAR-001` is independently reviewed and approved.
- Exact GeoNames snapshot/coordinates, Colombo row, family-town coverage, DataStore installed path/backup proof, dependency resolution/transitives/size, package identity, and Android provider/OEM behavior require Phase C evidence.
- Exactly one visual direction must be selected; final UI/theme tokens, fonts/assets/copy/components/previews/accessibility evidence remain unapproved.
- `minSdk 26` is provisional pending actual family-device inventory and verification; current `compileSdk`, `targetSdk`, and `minSdk` are independently 36.
- Trusted Sri Lankan traditional authority, independent astronomical tolerance evidence, physical family-device testing, and signed-APK/privacy checks block release approval only.
- Change Request CR-001 is approved; Phase B proposals must now be reconciled and independently reviewed before their own approval gates.
- CP-002 fixed 60-minute coverage at following sunrise, day/night behavior, the full Panchama matrix, cross-boundary behavior, and civil-versus-elapsed duration require traditional approval and block implementation.
- CP-003 daytime Rahu requires selected-tradition confirmation and source-backed expected cases; a nighttime variant is recognized but deferred/unselected.
- Reviewed Sinhala spellings, transliterations, aliases, and user-facing Panchama grammar remain pending.

## Next gate

After the authorized Phase B reconciliation and independent QA, request `APPROVE VERSION 1.0 ARCHITECTURE` and exactly one design direction approval. Separately state whether CP-003 is ready for `APPROVE DAYTIME RAHU CALCULATION PROFILE CP-003`. Do not proceed to delivery planning or application implementation at this gate.
