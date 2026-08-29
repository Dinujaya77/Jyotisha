# Delivery Status

| Field | Value |
|---|---|
| Status | In Progress |
| Version | 1.2 |
| Last updated | 2026-08-26 |
| Owner role | Lead Coordinator |
| Approval state | PA-001, CR-001, PA-002, DA-001, PA-004, DA-004, and DP-001 Approved; M1, M2, and M3 Done, explicitly Lead/owner accepted, and incorporated into `dev`; V1-M3-01 through V1-M3-04 Done; PA-003/CP-003 Blocked |

## Current phase

**Version 1.0 M1, M2, and M3 are Done, explicitly Lead/owner accepted, and incorporated into `dev`.** V1-M3-01 through V1-M3-04 passed Developer verification and independent QA. The implementation supplies static Dashboard, Timeline, first-use/Location, Settings, Method and About presentations; production calculation/location states remain honestly unavailable, while synthetic success/state data is confined to previews and tests. Corrections `2bc95be`, `045ed65`, and `bca3f7f` isolate route scroll state, correct compact/rail traversal and readable width, and resolve the final QA findings. Full JVM passed 41/41, lint reported zero errors, both debug APK builds passed, and 17/17 application/navigation/Compose connected checks passed. A separate launcher smoke was obstructed by an emulator SystemUI ANR focus surface after the last correction; no app crash/ANR or launcher-code delta was found. Independent QA passed with zero open findings, and the accepted M3 history through `3d9d9d2` was fast-forwarded into `dev`. **M4 is In Progress — corrective QA:** V1-M4-01 through V1-M4-05 are implementation-complete but not accepted or merged; M5 remains unstarted.

**Requirements Amendment CR-001 and Option C are Approved.** On 2026-08-02 the repository owner supplied the exact phrase `APPROVE REQUIREMENTS AMENDMENT CR-001`, approving only the staged Version 1.0/1.1 allocation, FR-012–FR-015, NFR-015, US-011–US-013, conditional deltas, and linked validation obligations/case definitions. The repository owner later explicitly authorized the staged baseline commit, created on `dev` as `be1b132` with message `chore: establish Codex team and approved requirements baseline`.

**Phase B architecture and Celestial Archive are Approved.** On 2026-08-02 the repository owner supplied `APPROVE VERSION 1.0 ARCHITECTURE`, `APPROVE DESIGN DIRECTION A`, and later `APPROVE VERSION 1.0 UI`. PA-002 approves architecture v0.5, ADR-001–ADR-009 v0.5, and DEP-011–DEP-014 in principle. DA-001 selects Celestial Archive and DA-004 approves its final Version 1.0 UI/token/accessibility specification. Directions B/C remain superseded alternatives. CP-003/RK v0.2 remains Blocked until its authority, source-backed weekday goldens, canonical terminology, and exact source versions/hashes are approved. Implementation remains task-gated; V1-M1-01 through V1-M3-04 are complete, individual M4 states are recorded, and V1-M4-05 and later tasks remain unstarted or Blocked unless separately authorized.

**The normative SOLAR-001 package is Approved.** On 2026-08-02 the repository owner supplied the exact phrase `APPROVE SOLAR-001`, approving `SOLAR-001-v1.0`, SOL-R-001–SOL-R-014, `NOAA-MEEUS-001-v1.0`, SOLAR-GOLDEN-001-v1.0, SOLAR-INTERMEDIATE-001-v1.0, the sea-level fixed policy, supported range/following-day condition, 60-second Sri Lankan validation tolerance, half-even millisecond anchors, local-wall minute/second presentation policies, pinned host-side NREL-SPA/pvlib validation method, source roles, and zero production dependency decision. M5 is the approved delivery placement and still requires the accepted M1 quality baseline and explicit task start; M6 depends on accepted M5 and CP-001. PA-004 does not approve CP-003, CP-002, implementation evidence, or release.

The approved product allocation is **Option C**: retain approved CP-001 Seasonal Planetary Hora as the Version 1.0 primary system; add daytime Rahu (CP-003) only after separate profile/RK approval; allocate fixed Sri Lankan Kala/Panchama (CP-002) to Version 1.1. CP-002 is implementation-blocked because, under elapsed-duration semantics or on offset-stable dates, 24 fixed 60-minute periods do not generally end at the next astronomical sunrise; wall-clock civil-minute behavior across offset transitions is also unresolved. Available sources do not define the required gap/overlap/reset/partial-period rule. Nighttime Rahu and final Sinhala terminology remain unresolved.

Phase A is Approved. On 2026-08-01 the repository owner supplied `APPROVE VERSION 1.0 REQUIREMENTS`, approving the Version 1.0 product requirements, scope, CP-001, CR-001–CR-010, and validation strategy subject to the documented architecture decisions and release-validation blockers.

The approved Phase B package preserves the Android Architect's read-only solar, location, town-data, persistence, lifecycle, dependency, testing, and proportionality analysis. It also preserves the UI/UX Designer's three reconciled narratives while selecting Celestial Archive. CR-001 amendments retain the one-module architecture and explicit timing-system boundaries. Historical approval-time state: no Kotlin, Compose production UI, Manifest, Gradle, resource, branch, worktree, merge, or push change occurred during that Phase B documentation checkpoint.

The independent QA Reviewer found zero Critical, three High, six Medium, and one Low Phase B documentation issues. Corrections made persistence payloads mutually exclusive, required a normative approved `SOLAR-001` before code, prevented premature production authorization, broadened backup exclusion, clarified direction evidence and Back behavior, restored VC-019 traceability, expanded dependency evidence, separated lifecycle cancellation outcomes, and fixed performance wording. QA re-verified all ten findings resolved. DA-004 later approved the final UI after its own zero-open recheck.

## Phase B proposal outcome

- Solar: PA-004-approved local pure-Kotlin `NOAA-MEEUS-001-v1.0`; SOLAR-001 freezes exact source hashes, equations/constants/order, civil-day/time-scale/sign rules, five-evaluation solver, typed failures, half-even millisecond anchors, independent NREL-SPA goldens, 60-second acceptance, and central display rounding. Implementation still requires an approved milestone/task. Solarpositioning and Astronomy Engine were evaluated but not selected.
- Location provider: existing AndroidX Core `LocationManagerCompat.getCurrentLocation()` behind `DeviceLocationProvider`; no Google Play Services dependency.
- Location policy: 20-second request; fresh through 2 minutes; stale at 24 hours; movement at 10 km; replacement improvement requires both 50 m and 25%; warning over 10 km uncertainty; maximum usable uncertainty 20 km; approximate remains usable within the ceiling.
- Town catalogue and location composition: the exact nine owner-approved GeoNames `LK.zip` product rows, Colombo default, hash and review are recorded in [V1-M4-04 Town Catalogue Evidence](V1-M4-04_TOWN_CATALOG_EVIDENCE.md). The catalogue remains offline; its user-facing attribution and truthful platform-provider privacy disclosure are implemented. The owner-authorized UI-004 exception permits town-only display plus selection/provenance/status for this frozen catalogue because no province/region schema or sourced data exists; no data was invented. No networking or background location was added.
- Persistence: Preferences DataStore 1.2.1 with one mutually exclusive device/manual/default payload, full deletion on mode/permission downgrade rules, dedicated backup-excluded directory, versioned corruption recovery, future-candidate rejection, migrations/corruption/reset tests, and no history.
- Structure: one app module/activity, pure domain engines, immutable state publication, and manual composition; DEP-011–DEP-013 Lifecycle 2.9.2 and DEP-014 DataStore 1.2.1 are declared. DEP-014 corrective verification/acceptance remains pending.
- Design: DA-001 selects Direction A — Celestial Archive; DA-004 approves the six-route/three-top-level Version 1.0 UI, semantic tokens, accessibility contract, and required implementation evidence. At Phase B approval time, actual Compose/device evidence remained future milestone/release work. M2 has since completed source and preview compilation, automated theme/token/component/semantics/accessibility assertions, API-36 installation, cold launch, and connected tests. Manual TalkBack, D-pad/focus restoration, actual 200% font-scale and compact-landscape visual inspection, grayscale/high-contrast/OLED and pressed/focus/disabled visual review, magnification, API-26 runtime, physical-family-device testing, final screenshot evidence, signing, update lineage, and release validation remain deferred.
- CR-001 amendment: separate `PlanetaryHora`, `SriLankanKalaHora`, `PanchamaKala`, and `RahuKala` calculators over one immutable shared context; no new dependency/module. Approved Option C allocates daytime Rahu conditionally to V1.0 and fixed Kala/Panchama to V1.1. DA-004 UI is Approved; Rahu calculation remains Blocked by PA-003.

## Foundation milestone

M-000 establishes the controlled Codex virtual team and documentation/verification foundation. No Jyotisha feature or intentional application behavior change is part of this milestone.

## Repository baseline history

- The foundation work began on `dev` at `dfbdb20` (`initial application setup`) with pre-existing untracked Android Studio files under `.idea/`; those files were preserved and subsequently ignored.
- The initial task did not authorize a commit from that dirty starting state. On 2026-08-02 the owner separately and explicitly authorized the exact staged baseline commit.
- Baseline commit: `be1b132` (`chore: establish Codex team and approved requirements baseline`) on `dev`; no branch, merge, or push was performed.
- Architecture/design checkpoint: `7848f1b` (`docs: approve version 1.0 architecture and design direction`) on `dev`; exactly 26 documentation files, with no Android/Gradle/dependency change and no push.
- SOLAR-001 checkpoint: `44d248d` (`docs: approve normative solar calculation profile`) on `dev`; exactly the intended solar documentation was committed after the owner-authorized safety checks. No Android/Gradle/dependency change and no push occurred.

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

## V1-M1-01 baseline audit (2026-08-02)

- Branch: `milestone/M1-android-foundation`, created from clean approved planning checkpoint `fe9fd3a` on `dev`; no worktree was created.
- Project/build: root `Jyothisha`, one `:app` module, Gradle 8.13, AGP 8.11.2, Kotlin/Compose plugins 2.0.21, Java/Kotlin 11, and compile/min/target SDK 36.
- Identity/source sets: `com.example.jyothisha` namespace, application ID, packages, and paths; only `main`, `test`, and `androidTest`, containing the generated Compose activity/theme and one template test in each test source set.
- Manifest/backup: one exported launcher `ComponentActivity`, no source-declared permissions/services/providers/receivers, `allowBackup=true`, and sample backup/data-extraction rule files with no active include/exclude rule.
- Direct catalogue baseline: Core KTX 1.17.0, Lifecycle Runtime KTX 2.9.2, Activity Compose 1.10.1, Compose BOM 2024.09.00, JUnit 4.13.2, AndroidX Test JUnit 1.3.0, Espresso 3.7.0, and existing BOM-managed Compose artifacts. No dependency changed.
- Verification passed: project discovery, debug runtime dependency resolution, forced unit execution (one test; zero failures/errors), lint (zero errors; 16 recorded template/baseline warnings), debug APK compilation, Android-test APK compilation, whitespace/diff/status/generated-output/privacy/secret audits. Connected instrumented execution was unavailable because no device or emulator was attached.
- Independent QA: one Medium documentation-traceability finding required the QA result and Lead acceptance to be explicit. The three delivery records were corrected; QA's independent unit/lint/debug/test-APK rerun and scope/privacy audit passed. The Lead accepted V1-M1-01 after that correction.
- Normalization delta: zero. Identity/spelling belongs to blocked V1-M1-02; minSdk and dependency evidence to V1-M1-03; test replacement to V1-M1-04; app shell to V1-M1-05; backup/persistence changes to V1-M4-03. Advancing any of those in V1-M1-01 would exceed the approved task.
- Environment note: an initial forced compile timed out after the Kotlin daemon could not write to sandboxed user AppData; the in-process compiler rerun passed. The non-fatal Android metrics warning remains an environment limitation, not a product blocker.

## V1-M1-02 permanent identity (2026-08-02)

- Owner approval: visible name `Jyotisha`; namespace and application ID `io.github.dinujaya77.jyotisha`, identical and lowercase with no suffix or product flavour.
- Implementation: renamed the Gradle root, namespace/application ID, production/unit/instrumented package declarations and paths, instrumented assertion, Manifest theme references, application label, and template theme identifiers. Six Kotlin files moved; no duplicate old package tree remains.
- Verification passed: Gradle project configuration and debug dependency resolution; forced unit execution (one test; zero failures/errors/skips); lint (zero errors; 16 retained baseline warnings); debug APK and Android-test APK builds; package/path consistency; merged-Manifest, output-metadata, and APK badging inspection; old active-identity search; diff, secret, signing-material, and generated-output audits.
- Artifact identity: merged Manifest, debug APK, generated authorities/permissions, and output metadata use `io.github.dinujaya77.jyotisha`; the application and launcher label is `Jyotisha`; the launchable activity is the renamed `MainActivity` package.
- Scope preserved: compile/min/target SDK, version code/name, signing, dependencies, architecture, modules, and application behavior did not change. Connected install/launch and instrumented execution were unavailable because no device or emulator was attached.
- Independent QA: one Medium completion-gate finding required the QA result and Lead acceptance to be explicit. The three delivery records were corrected. QA independently repeated the forced unit and full unit/lint/debug/test-APK checks, inspected the moved files, merged Manifest and APK, and found no implementation defect. The Lead accepted V1-M1-02 after that correction.

## V1-M1-03 SDK and Lifecycle foundation (2026-08-05)

- SDK: `minSdk 26`; compile/target SDK 36; version, identity, signing, and one-module structure unchanged. API-26 compile/Manifest/dex compatibility passed; API-26 runtime and physical-family-device proof remain NFR-009 evidence.
- Direct dependencies: `lifecycle-viewmodel-ktx`, `lifecycle-viewmodel-compose`, and `lifecycle-runtime-compose`, declared and resolved at 2.9.2 through the existing version catalogue. DEP-014 and unrelated dependencies were not added.
- Current relevant transitives: coroutines android/core 1.8.1, serialization core 1.7.3, and Compose runtime/runtime-saveable 1.7.8 selected under the existing BOM. Application production source directly imports none of those APIs.
- Verification: project/dependency reports and targeted insights, forced unit test, lint, debug APK, Android-test APK, merged Manifest/APK metadata, package/scope/privacy audits all passed. On `emulator-5556` API 36, install passed, cold `MainActivity` launch returned `Status: ok`, no `FATAL EXCEPTION` appeared, and connected test passed 1/1.
- APK measurements: debug 24,647,690 to 9,718,886 bytes; Android-test 2,112,168 to 965,432 bytes. Conditions were not an identical controlled clean comparison, so the negative deltas are non-isolated and not attributed to Lifecycle.
- Independent QA found zero implementation issues and independently repeated unit/lint/build/connected verification. Dependency register and delivery evidence record Apache-2.0, maintained AndroidX provenance, public advisory-search result, and remaining evidence limits. Lead accepted V1-M1-03.

## V1-M1-04 deterministic test foundation (2026-08-05)

- Replaced the generated unit and instrumentation examples with a test-only `Clock.fixed` fixture, two deterministic JVM tests for repeated known-instant reads and explicit `Asia/Colombo` representation, and a launcher smoke test that verifies the package, installed launcher resolution to `MainActivity`, resumed lifecycle, and displayed root.
- The forced JVM suite passed 2/2 on three runs. Unit, lint, debug APK, Android-test APK, installation, and connected checks passed; the launcher smoke passed 1/1 on `emulator-5556` API 36. Reinstalled cold launch returned `Status: ok`, `MainActivity` was top-resumed, and the crash buffer contained no fatal exception.
- Scope/privacy audits found no production, Gradle, dependency, SDK, identity, version, Manifest, network, sleep, system-wall-clock, private-coordinate, family-data, secret, or generated-output change. Repository/provider/UI-state/location fakes were not invented before their approved production contracts exist.
- Independent QA found zero open Critical, High, Medium, or Low issues and repeated the JVM and connected tests successfully. Lead accepted V1-M1-04. API-26 runtime, physical-device, process-death, extended accessibility/localization, and later domain/state suites remain assigned to later tasks; NFR-009–NFR-012 are not complete solely from this task.

## V1-M1-05 bounded application shell (2026-08-05)

- Implementation: one `MainActivity` hosts a manual `JyotishaApp` composition root. Immutable `ShellState`, typed bounded top-level/child/origin values, a pure reducer, and primitive `rememberSaveable` state provide Dashboard/Timeline/Method plus Location/Settings/About without Navigation Compose or another dependency.
- Policy: reselecting an active top-level at its root is a no-op; selecting any top level while a child is open returns to that top-level root without accumulating history; Timeline/Method Back returns Dashboard; Location returns its top-level or Settings origin; Settings returns its originating top level; About returns Settings; Dashboard-root system Back delegates normally because the app handler is disabled.
- UI scope: six rendering-only, scrollable placeholders use externalized strings, headings, Material selected semantics, understandable Back labels, stable test tags, and decorative glyphs hidden from accessibility. No final Celestial Archive tokens/layout, domain calculation, location, persistence, settings storage, or methodology content was introduced.
- Verification: pure shell tests passed 7/7 after merge-readiness coverage closure and the full JVM suite passed 9/9. Lint, debug APK, Android-test APK, install, merged Manifest/APK identity, cold launch, no-fatal, dependency/scope/privacy/secret/generated-output audits passed. The committed complete connected suite passed 6/6 on `emulator-5556` API 36.
- QA: two Medium evidence gaps initially required nested child-origin recreation and proof of actual landscape Activity recreation. Tests were corrected without a production change; independent QA rebuilt/installed and passed the corrected shell suite 5/5, then found zero open Critical, High, Medium, or Low findings. Lead accepted V1-M1-05.
- Boundary: activity/configuration recreation and exact nested origin restoration are proven. Full operating-system process-death, API-26 runtime, physical devices, final adaptive/accessibility/localization, and final visual evidence remain later gates and are not claimed by M1.

## M1 merge-readiness corrective evidence (2026-08-06)

- The initial milestone review found two Medium evidence gaps: direct deterministic JVM coverage for selecting top-level destinations while representative children were open, and stale current-state wording across authoritative documents.
- One table-driven reducer test now covers Location from Dashboard to Timeline, Settings from Timeline to Dashboard, and About through Settings from Method back to Method. Each case proves the exact canonical root, deterministic repetition, root reselection, and Back without retained child history. The reducer suite passed 7/7 repeatedly and the forced full JVM suite passed 9/9.
- At corrective checkpoint `580bab1`, current-state wording consistently recorded V1-M1-01 through V1-M1-05 Done, M1 In Review and unmerged, M2 and later tasks unstarted, provisional `minSdk 26`, implemented DEP-011–DEP-013, deferred DEP-014, and the bounded shell. Historical approval-time statements remained explicitly historical; no approval state changed.
- Forced project discovery, dependency resolution, JVM tests, lint, debug APK, and Android-test APK builds passed. APK and merged-Manifest inspection retained application ID `io.github.dinujaya77.jyotisha`, launcher `MainActivity`, label `Jyotisha`, minSdk 26, targetSdk 36, and compileSdk 36. No device was attached, so fresh installation and connected tests were unavailable; the committed API-36 connected result remains 6/6.
- The corrective scope audit found no production Android, Compose, resource, Manifest, Gradle, dependency, SDK, identity, signing, or application-behavior change. At that checkpoint M1 remained In Review pending repeat merge-readiness review and an explicit merge decision; the later final review passed and the owner then accepted the fast-forward into `dev`.
- Independent corrective QA passed with zero open Critical, High, Medium, or Low findings and confirmed the branch is ready for a fresh merge-readiness review.

## V1-M2-01 Celestial Archive color foundation (2026-08-06)

- Implementation: all 21 approved semantic roles have exact paired light/dark values. Later and pending alias the information pair; completed aliases the unavailable pair. Compose uses fixed approved-color-only Material schemes and exposes semantic roles at the theme boundary; dynamic platform color is disabled by removal from the Version 1.0 API.
- Verification: the focused semantic suite passed 7/7 in two forced runs and the complete JVM suite passed 16/16. Project discovery, debug runtime dependencies, lint, debug APK and Android-test APK builds passed. The APK and merged Manifest retain application ID `io.github.dinujaya77.jyotisha`, launcher `MainActivity`, label `Jyotisha`, minSdk 26, targetSdk 36 and compileSdk 36.
- Contrast: every tested normal-text pair meets 4.5:1 with a measured minimum of 6.68:1; outline/focus against background/surface meet 3.0:1 with a measured minimum of 4.40:1.
- Scope: no dependency, Gradle, SDK, identity, Manifest, resource, navigation, feature, calculation, location or persistence change occurred. No device was attached, so connected execution was unavailable rather than failed.
- Boundary at task completion: typography, spacing, shape, elevation, wash, motion, icon, illustration, reusable components and deterministic previews were then completed by V1-M2-02 through V1-M2-04. The final M2 suite completed automated semantics/non-colour/adaptive-fixture and API-36 evidence. M2 does not claim the deferred task-specific release evidence: V1-M9-01 closes manual OLED/grayscale/high-contrast and pressed/focus/disabled visual review, TalkBack, keyboard/D-pad/focus restoration, actual 200% font-scale, compact-landscape/adaptive inspection, screen magnification, non-colour recognition, localization/expansion, and final screenshot/manual visual evidence; V1-M9-02 supplies API-26 runtime and the complete API/state regression matrix; V1-M9-03 supplies oldest/newest physical-family-device and OEM performance/privacy evidence; V1-M9-04 supplies signing and reproducible APK install/update/rollback/update-lineage evidence; and V1-M9-05 performs final independent QA, owner family acceptance, and release validation.

## V1-M2-02 Celestial Archive design tokens (2026-08-06)

- Implementation checkpoint `f6fbe1c`: exact approved typography, spacing, responsive gutters, shapes and Material shape wiring, elevations, constrained wash, durations/easings, reduced-motion substitution, icon/illustration contracts, 2dp focus and 48dp touch-target tokens.
- Verification: focused token tests passed 8/8. Compile, lint and both APK builds passed without dependency, SDK, identity, Manifest, resource, architecture or feature expansion.

## V1-M2-03 reusable Celestial Archive components (2026-08-06)

- Implementation checkpoint `a353c4f`: domain-neutral timing folio, anchor row, provenance/status, timeline row, primary/secondary text actions and unavailable panel. Components use semantic tokens, flexible height, explicit text/non-colour cues and separate action semantics.
- Verification: presentation-model tests passed 4/4. Component semantics and interactions passed in the final connected suite; lint, compile and both APK builds passed. No domain calculation, live navigation, location, persistence or later feature behavior was introduced.

## V1-M2-04 deterministic design-system previews (2026-08-06)

- Implementation checkpoint `55c5d77`: eight deterministic synthetic component previews cover light/dark, 320dp portrait, 640×320 compact landscape, 720dp medium, 1000dp expanded, long English, selected/unselected, enabled/disabled, loading/error/unavailable and reduced-motion states. Fixtures use no services, private data or screenshot dependency.
- Verification: preview-fixture tests passed 7/7. The final forced M2 focused suite passed 26/26 and complete JVM suite passed 35/35; lint, debug APK and Android-test APK builds passed. On `emulator-5556` API 36, the complete connected suite passed 13/13. After reinstall, cold launch returned `Status: ok`, resumed `MainActivity`, and produced no `FATAL EXCEPTION` or ANR.
- Environment note: the first connected attempt encountered a stale test-package signing mismatch and the initial uninstall returned `DELETE_FAILED_INTERNAL_ERROR`; the subsequent Gradle run installed and executed all tests successfully. This was an emulator package-state issue, not an application defect.
- Independent QA: final read-only M2 QA found zero open Critical, High, Medium or Low findings and accepted all four tasks for documentation reconciliation and milestone review.
- Residual evidence: API-26 runtime, physical family devices, full operating-system process death, manual TalkBack and D-pad/focus restoration, actual 200%/compact-landscape visual inspection, grayscale/high-contrast/OLED and pressed/focus/disabled visual review, magnification, localization, screenshots, signing and release validation remain later gates.

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

## Phase B reconciliation QA (2026-08-02)

The independent QA Reviewer initially found zero Critical/High, two Medium, and zero Low issues in the reconciled v0.5 architecture/v0.4 design package. Corrections limited Version 1.0 execution to CP-001 plus CP-003 only after PA-003, kept CP-002 unconstructed until Version 1.1 gates, and aligned stale gate/QA status wording. The read-only recheck found zero open Critical, High, Medium, or Low issues. It confirmed approval consistency, bidirectional traceability, all seven Rahu states, Option C boundaries, no Android/Gradle changes, and no secret/private-coordinate indicators.

## SOLAR-001 evidence and review (2026-08-02)

- Read-only Android Architect confirmed Candidate A, no dependency, stateless pure API, coordinator-owned 32-entry in-memory cache target, off-main execution/cancellation generation, exact shared `DailyCalculationContext`, thread safety, under-one-second product threshold, 50 ms p95 solar engineering target, and replaceability without UI/ViewModel/Hora/Rahu/location changes.
- Read-only Business Analyst/source review confirmed USNO as the exact event-definition authority, NOAA as the equation-family/limitation source, NREL Revised January 2008 as the independent SPA validation authority, and Meeus as copyrighted lineage rather than copied code.
- Official evidence hashes were frozen for USNO, NOAA equations PDF/spreadsheet, NREL report, GeoNames `LK.zip`, and the pvlib 0.15.1 wheel. No NREL software source was copied or redistributed.
- Golden generation used Python only in an operating-system temporary directory. Nothing from that environment was added to the repository or Android build.
- A separate temporary transcription of the frozen SOL-R-006–SOL-R-009 equations was used only as a specification sanity check: all 30 proposed production anchors were within the preselected 60-second oracle tolerance, with maximum absolute difference 2.452 seconds. This is not Android implementation or acceptance evidence; the future Kotlin engine must independently pass the frozen set.
- Independent QA initially found two High, three Medium, and one Low documentation issues. Corrections unified the PA-004 source set, froze deterministic grazing/polar/multiple/failure precedence, moved SOL-G-007 to the exact lower range edge, added source-provenanced equation literals, defined historical wall-time display rounding, and corrected the StrictMath claim. A correction recheck found two High and one Medium fixture/scope issues; those were resolved with exact round-trip binary64 literals, named representable grazing bounds, and inclusion of SOLAR-INTERMEDIATE-001-v1.0 throughout PA-004. The final read-only recheck found zero open Critical, High, Medium, or Low issues; all 105 intermediate literals matched the independent transcription at 0 ULP.

## Blockers and unresolved decisions

- No blocker prevents completing M-000; it remains Done.
- The separately authorized baseline, architecture/design, and SOLAR-001 checkpoints are complete as `be1b132`, `7848f1b`, and `44d248d`. DA-004 and DP-001 are now explicitly Approved, and the owner authorized one documentation-only approval checkpoint. No Android/Gradle/dependency work or later automatic commit is authorized.
- Requirements, CP-001, CR-001–CR-010, and the linked validation strategy are approved under PA-001.
- ADR-001–ADR-009 and DEP-011–DEP-014 are approved under PA-002. DEP-011–DEP-013 are implemented under V1-M1-03; DEP-014 remains approved in principle but not added. SOLAR-001 is Approved under PA-004 and remains separately task-gated.
- The exact nine GeoNames product rows, Colombo row/default, hash, and approved coverage are recorded for completed V1-M4-04 in [its evidence](V1-M4-04_TOWN_CATALOG_EVIDENCE.md); private family-town coverage remains outside this approved subset. DEP-014 DataStore resolution/transitives/size and installed-path/backup proof, and Android provider/OEM behavior require later task evidence. DEP-011–DEP-013 evidence is complete under V1-M1-03. Permanent package identity is complete under V1-M1-02; signed update-lineage evidence remains a release gate.
- Direction A — Celestial Archive is selected under DA-001 and the final UI/theme/token/accessibility specification is Approved under DA-004. M2 produced the approved semantic-colour/token foundation, reusable domain-neutral components, deterministic previews and automated API-36 evidence. Manual screenshots, TalkBack, physical-device, final font-scale/adaptive/localization and release evidence remain later gates.
- `minSdk 26` is implemented provisionally; `compileSdk` and `targetSdk` remain 36. API-26 runtime and actual oldest/newest family-device verification remain required before the final support claim.
- Trusted Sri Lankan traditional authority, independent astronomical tolerance evidence, physical family-device testing, and signed-APK/privacy checks block release approval only.
- Change Request CR-001, PA-002 architecture, and DA-001 Direction A are approved; their subordinate domain/data/UI/implementation/release gates remain explicit.
- CP-002 fixed 60-minute coverage at following sunrise, day/night behavior, the full Panchama matrix, cross-boundary behavior, and civil-versus-elapsed duration require traditional approval and block implementation.
- CP-003 daytime Rahu is Blocked from approval pending a selected Sri Lankan authority, seven source-backed weekday golden vectors including non-06:00 anchors and remainder behavior, boundary/date/zone/leap vectors, canonical terminology, and exact source versions/hashes. A nighttime variant is recognized but deferred/unselected.
- Reviewed Sinhala spellings, transliterations, aliases, and user-facing Panchama grammar remain pending.

## Current stop point

## Final UI and delivery planning QA (2026-08-02)

The independent read-only QA recheck initially found zero Critical/High, five Medium, and zero Low issues. Corrections removed stale PA-004 language, made non-colour theme aliases/layout/motion tokens deterministic, made Dashboard location actions source-specific, restored CR-008 active-zone coverage in M6, and normalized the IA inventory/Now–Dashboard/Day–Timeline aliases. The final recheck found **zero open Critical, High, Medium, or Low findings**. It confirmed 39 complete tasks, full FR-001–FR-012/NFR-001–NFR-015 task/test traceability, M1–M4 independence from CP-003, M5/PA-004 alignment, M6/CP-001/CR-001–010 alignment, M8/PA-003 blocking, V1.1-only Kala/Panchama, exact DA-004/DP-001 gates, no Android/Gradle/dependency diff, and no secret/private-data indicator.

V1-M1-01 through V1-M3-04 are Done; M1, M2, and M3 are explicitly Lead/owner accepted and incorporated into `dev`. Final independent M3 QA found zero open findings. M4 is In Progress in corrective QA: V1-M4-01 through V1-M4-05 are implementation-complete, not accepted or merged; M5 and later tasks remain unstarted (or Blocked where recorded) pending separate explicit authorization. Mandatory deferred ownership is unchanged: V1-M9-01 accessibility/adaptive/localization/manual visual; V1-M9-02 API runtime and complete API/state regression; V1-M9-03 physical-family/OEM/performance/privacy; V1-M9-04 signing/reproducible APK/install-update-rollback/update lineage; V1-M9-05 final QA/family acceptance/release validation.
