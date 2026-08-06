# Dependency Register

| Field | Value |
|---|---|
| Status | Approved |
| Version | 0.7 |
| Last updated | 2026-08-06 |
| Owner role | Android Architect |
| Approval state | DEP-011–DEP-013 implemented and verified under V1-M1-03; DEP-014 remains approved in principle but not added; PA-004 approves SOLAR-001 with zero additional Android/Gradle dependencies |

| ID | Dependency | Version source | Scope/purpose | License/security notes | Decision |
|---|---|---|---|---|---|
| DEP-001 | Android Gradle Plugin | 8.11.2 | Build plugin | Review with Gradle/JDK compatibility | Existing |
| DEP-002 | Kotlin Android + Compose plugins | 2.0.21 | Kotlin/Compose compilation | Keep versions aligned | Existing |
| DEP-003 | AndroidX Core KTX | 1.17.0 | Android Kotlin extensions | Review transitive baseline | Existing |
| DEP-004 | Lifecycle Runtime KTX | 2.9.2 | Lifecycle runtime | Existing baseline artifact; ViewModel artifacts are separately registered as DEP-011/012 | Existing |
| DEP-005 | Activity Compose | 1.10.1 | Compose activity host | — | Existing |
| DEP-006 | Compose BOM | 2024.09.00 | Compose version alignment | Verify compatibility before upgrades | Existing |
| DEP-007 | Compose UI/graphics/tooling-preview + Material 3 | BOM-managed | UI foundation | Tooling implementation is debug-only | Existing |
| DEP-008 | JUnit | 4.13.2 | Local unit tests | Test-only | Existing |
| DEP-009 | AndroidX Test JUnit / Espresso | 1.3.0 / 3.7.0 | Instrumented tests | Test-only | Existing |
| DEP-010 | Compose UI test JUnit4/manifest | BOM-managed | Compose UI tests | Test/debug only | Existing |
| DEP-011 | AndroidX Lifecycle ViewModel KTX | `androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.2` | ViewModel and coroutine-aware state holder | Apache-2.0 AndroidX; no new permission/data flow | Added and verified under V1-M1-03 |
| DEP-012 | AndroidX Lifecycle ViewModel Compose | `androidx.lifecycle:lifecycle-viewmodel-compose:2.9.2` | Obtain/use ViewModel from Compose | Apache-2.0 AndroidX; Compose bridge only | Added and verified under V1-M1-03 |
| DEP-013 | AndroidX Lifecycle Runtime Compose | `androidx.lifecycle:lifecycle-runtime-compose:2.9.2` | Lifecycle-aware `StateFlow` collection | Apache-2.0 AndroidX; avoids collecting UI state while stopped | Added and verified under V1-M1-03 |
| DEP-014 | AndroidX DataStore Preferences | 1.2.1 stable | Transactional asynchronous versioned location record in `noBackupFilesDir` | AndroidX; complete location-data directory must be excluded/tested for backup/transfer | Approved in principle under PA-002; not added |

New dependencies require an ADR or documented approval, purpose, alternatives, maintenance health, license, security/privacy impact, size/performance cost, version source, and verification plan.

## Required dependency evidence

| ID | License/maintenance/version provenance | Compatibility and transitive review | Size/performance/privacy | Required implementation evidence |
|---|---|---|---|---|
| DEP-011 | Apache-2.0 AndroidX POM; official maintained Lifecycle 2.9.2 release record | Verified with Kotlin 2.0.21, AGP 8.11.2, min SDK 26 and the existing Compose set; coroutines 1.8.1 remain transitive only | No permission/external data flow; APK comparison was non-isolated and is not attributed | Completed under V1-M1-03: dependency reports/insight, licence/advisory review, API-26 compile/metadata and API-36 runtime checks |
| DEP-012 | Apache-2.0 AndroidX POM; same maintained Lifecycle 2.9.2 provenance | Verified with Compose BOM 2024.09.00; serialization 1.7.3 and Compose runtime 1.7.8 resolved transitively; no Navigation/Hilt | Compose adapter only; no location/storage/network behavior; APK comparison non-isolated | Completed under V1-M1-03: resolution graph, licence/advisory review, compile/device smoke and size record |
| DEP-013 | Apache-2.0 AndroidX POM; same maintained Lifecycle 2.9.2 provenance | Verified against resolved Compose runtime 1.7.8 and API-26 compile/metadata | No new permission/data flow; runtime/connected smoke passed on API 36; API-26 runtime remains later evidence | Completed under V1-M1-03: resolution graph, licence/advisory review, build/runtime and size record |
| DEP-014 | Apache-2.0 AndroidX; DataStore 1.2.1 is the official stable release recorded by AndroidX | Kotlin 2.0 project satisfies published modern Kotlin baseline; verify AGP/API 26, coroutines, file-storage transitives, and R8 rules | Persists sensitive coordinates locally; dedicated directory exclusion/migration/corruption controls required; exact APK/I/O impact unknown | Resolution/transitive/license/advisory report, API 26 read/write/migration/corruption tests, main-thread and startup measurement, APK delta, installed-path/backup/transfer proof |

“Expected” is not acceptance evidence. Phase C must record measured dependency and APK/transitive changes before Lead acceptance; architecture approval alone adds no artifact.

## V1-M1-03 implementation evidence (2026-08-05)

- DEP-011, DEP-012, and DEP-013 are direct `implementation` dependencies declared through the existing Lifecycle 2.9.2 version-catalogue reference. Declared and resolved versions are 2.9.2; the Lifecycle family remains aligned.
- Relevant current transitives are `kotlinx-coroutines-android`/core 1.8.1, `kotlinx-serialization-core` 1.7.3 through ViewModel Compose, and Compose runtime/runtime-android/runtime-saveable 1.7.8 selected under the existing Compose BOM. These are resolved transitives, not approved direct application APIs; production source has no direct coroutine or serialization import and no direct coroutine dependency was added.
- AndroidX POM metadata and the official AndroidX repository identify Apache-2.0 licensing. The official Lifecycle release record shows 2.9.2 released 2025-07-16 and later 2.9.x maintenance releases. Searches of the public GitHub Advisory Database and NVD on 2026-08-05 found no advisory naming these three coordinates; this records the search result, not a guarantee, and no automated vulnerability scanner is configured.
- Gradle dependency reports and targeted `dependencyInsight` checks passed with Kotlin 2.0.21, AGP 8.11.2, compile/target SDK 36, min SDK 26, and the existing Compose set. DEP-014, Navigation, Hilt, Room, Play Services, networking, analytics, and unrelated test/dependency artifacts were not added.
- API-26 compile, merged-Manifest, dex, unit, lint, debug APK, and Android-test APK checks passed. Runtime install/launch and the connected test passed on API 36; API-26 runtime/device and physical oldest-family evidence remain NFR-009 release evidence.
- Measured APK sizes were 24,647,690 to 9,718,886 bytes for debug and 2,112,168 to 965,432 bytes for Android-test. The measurements used the same variants but not controlled identical clean conditions, so the large negative deltas are non-isolated and must not be attributed to these dependencies.

## Phase B evaluated alternatives

| Candidate | Current repository state | Assessment | Disposition |
|---|---|---|---|
| AndroidX `LocationManagerCompat.getCurrentLocation()` | Available through DEP-003 | API 26-compatible one-shot callback with cancellation/nullable result; provider/OEM behavior needs device tests | Recommended by ADR-002; no new dependency |
| Google Play Services `FusedLocationProviderClient.getCurrentLocation()` | Not present | Rich current-location controls but introduces Play-services availability, APK, maintenance, and provider privacy/data-flow review | Rejected for V1 by ADR-002 |
| Astronomy Engine Kotlin/JVM 2.1.19 | Not present; distributed from project/JitPack path | MIT, offline, broad Moon/planet capability and strong upstream tests; standard rise/set convention does not directly implement fixed CP-001 event and adds broad supply-chain/code surface | Rejected for V1 production; future Panchanga candidate |
| `net.e175.klaus:solarpositioning` (reported tag 2.0.13) | Not present | MIT, maintained Java SPA/Grena implementation with 1,000+ claimed tests and no runtime deps; documented `0.833°` correction is not exact CP-001 `0.8333°`; artifact, Java 17/API 26, desugaring/R8, APK and supply-chain evidence remain absent | Evaluated as Candidate C; not selected |
| SharedPreferences | Platform API | Small/no dependency, but synchronous API and weaker transactional/Flow/migration model | Rejected in favor of proportionate DEP-014 |
| Proto DataStore / Room | Not present | Typed/database capabilities exceed the single small record | Rejected as disproportionate |
| Navigation Compose / Hilt | Not present | Useful at larger scale, but three destinations and manual construction do not justify dependencies | Rejected for V1 unless later evidence changes scope |

No dependency was added in Phase A or B. Under the later explicitly started V1-M1-03 task, DEP-011–DEP-013 were added and verified. DEP-014 remains approved in principle only and still requires its own authorized implementation task and complete evidence.

Direct coroutine APIs must not rely silently on transitive artifacts. Before implementation, the dependency task must either register/approve exact production and test coroutine artifacts with resolved versions, licenses and transitive evidence, or constrain the design to APIs exposed by already approved direct dependencies. No coroutine dependency is approved or added by this reconciliation.

## SOLAR-001 dependency impact

`NOAA-MEEUS-001-v1.0` is independently written pure Kotlin using the standard library/`StrictMath`; it adds no production, test, Gradle, version-catalogue, network, native, or data dependency. This zero-dependency decision was approved under PA-004 on 2026-08-02.

Pinned pvlib 0.15.1 (BSD-3-Clause) was installed only in an operating-system temporary directory to generate frozen independent NREL-SPA values. Its wheel hash and host environment are evidence in SRC-022 and `VALIDATION_CASES.md`; it is not present in the repository, Android APK, Gradle graph, or dependency register as an application artifact. NREL's downloadable C code is not copied, ported, redistributed, or bundled.

## Sources for approved-in-principle versions

- [AndroidX DataStore releases](https://developer.android.com/jetpack/androidx/releases/datastore)
- [AndroidX Lifecycle releases](https://developer.android.com/jetpack/androidx/releases/lifecycle)
- [Astronomy Engine repository/license](https://github.com/cosinekitty/astronomy)
- [Solarpositioning repository](https://github.com/KlausBrunner/solarpositioning)
