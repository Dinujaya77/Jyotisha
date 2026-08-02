# Dependency Register

| Field | Value |
|---|---|
| Status | Approved |
| Version | 0.6 |
| Last updated | 2026-08-02 |
| Owner role | Android Architect |
| Approval state | DEP-011–DEP-014 remain approved in principle under PA-002; PA-004 approves SOLAR-001 with zero additional Android/Gradle dependencies |

| ID | Dependency | Version source | Scope/purpose | License/security notes | Decision |
|---|---|---|---|---|---|
| DEP-001 | Android Gradle Plugin | 8.11.2 | Build plugin | Review with Gradle/JDK compatibility | Existing |
| DEP-002 | Kotlin Android + Compose plugins | 2.0.21 | Kotlin/Compose compilation | Keep versions aligned | Existing |
| DEP-003 | AndroidX Core KTX | 1.17.0 | Android Kotlin extensions | Review transitive baseline | Existing |
| DEP-004 | Lifecycle Runtime KTX | 2.9.2 | Lifecycle runtime | No ViewModel dependency yet | Existing |
| DEP-005 | Activity Compose | 1.10.1 | Compose activity host | — | Existing |
| DEP-006 | Compose BOM | 2024.09.00 | Compose version alignment | Verify compatibility before upgrades | Existing |
| DEP-007 | Compose UI/graphics/tooling-preview + Material 3 | BOM-managed | UI foundation | Tooling implementation is debug-only | Existing |
| DEP-008 | JUnit | 4.13.2 | Local unit tests | Test-only | Existing |
| DEP-009 | AndroidX Test JUnit / Espresso | 1.3.0 / 3.7.0 | Instrumented tests | Test-only | Existing |
| DEP-010 | Compose UI test JUnit4/manifest | BOM-managed | Compose UI tests | Test/debug only | Existing |
| DEP-011 | AndroidX Lifecycle ViewModel KTX | 2.9.2, aligned with existing Lifecycle family | ViewModel and coroutine-aware state holder | AndroidX; no new permission/data flow | Approved in principle under PA-002; not added |
| DEP-012 | AndroidX Lifecycle ViewModel Compose | 2.9.2, aligned with existing Lifecycle family | Obtain/use ViewModel from Compose | AndroidX; Compose bridge only | Approved in principle under PA-002; not added |
| DEP-013 | AndroidX Lifecycle Runtime Compose | 2.9.2, aligned with existing Lifecycle family | Lifecycle-aware `StateFlow` collection | AndroidX; avoids collecting UI state while stopped | Approved in principle under PA-002; not added |
| DEP-014 | AndroidX DataStore Preferences | 1.2.1 stable | Transactional asynchronous versioned location record in `noBackupFilesDir` | AndroidX; complete location-data directory must be excluded/tested for backup/transfer | Approved in principle under PA-002; not added |

New dependencies require an ADR or documented approval, purpose, alternatives, maintenance health, license, security/privacy impact, size/performance cost, version source, and verification plan.

## Required dependency evidence

| ID | License/maintenance/version provenance | Compatibility and transitive review | Size/performance/privacy | Required implementation evidence |
|---|---|---|---|---|
| DEP-011 | Apache-2.0 AndroidX; reuse existing Lifecycle 2.9.2 from Google Maven/official Lifecycle release record rather than upgrade | Resolve with Kotlin 2.0.21, AGP 8.11.2, API 26, and existing Compose set; record full runtime/compile transitive delta | Expected small state-holder/coroutine bridge; no permission or external data flow; exact APK/startup impact not yet measured | `dependencies`/`dependencyInsight`, license inventory, API 26 compile/test, before/after APK and startup/calc measurement, vulnerability/advisory review |
| DEP-012 | Apache-2.0 AndroidX; same maintained Lifecycle 2.9.2 family/provenance | Verify against Compose BOM 2024.09.00 and ViewModel KTX; no hidden Navigation/Hilt assumption | Compose adapter only; no location/storage/network behavior; exact size unknown until resolved | Resolution graph, license/advisory scan, Compose compile/UI smoke, APK delta |
| DEP-013 | Apache-2.0 AndroidX; same maintained Lifecycle 2.9.2 family/provenance | Verify lifecycle-runtime-compose APIs against resolved Compose runtime and API 26 | Lifecycle-aware collection should reduce stopped-state work; no new permission/data flow; exact size/perf unknown | Resolution graph, stop/resume collection tests, license/advisory scan, APK/runtime measurement |
| DEP-014 | Apache-2.0 AndroidX; DataStore 1.2.1 is the official stable release recorded by AndroidX | Kotlin 2.0 project satisfies published modern Kotlin baseline; verify AGP/API 26, coroutines, file-storage transitives, and R8 rules | Persists sensitive coordinates locally; dedicated directory exclusion/migration/corruption controls required; exact APK/I/O impact unknown | Resolution/transitive/license/advisory report, API 26 read/write/migration/corruption tests, main-thread and startup measurement, APK delta, installed-path/backup/transfer proof |

“Expected” is not acceptance evidence. Phase C must record measured dependency and APK/transitive changes before Lead acceptance; architecture approval alone adds no artifact.

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

No dependency was added in Phase A or B. PA-002 authorizes DEP-011–DEP-014 in principle only; the Android Developer must still receive an approved task, add exact catalog entries, verify resolution/licenses/transitives, and update this register with the implemented evidence.

Direct coroutine APIs must not rely silently on transitive artifacts. Before implementation, the dependency task must either register/approve exact production and test coroutine artifacts with resolved versions, licenses and transitive evidence, or constrain the design to APIs exposed by already approved direct dependencies. No coroutine dependency is approved or added by this reconciliation.

## SOLAR-001 dependency impact

`NOAA-MEEUS-001-v1.0` is independently written pure Kotlin using the standard library/`StrictMath`; it adds no production, test, Gradle, version-catalogue, network, native, or data dependency. This zero-dependency decision was approved under PA-004 on 2026-08-02.

Pinned pvlib 0.15.1 (BSD-3-Clause) was installed only in an operating-system temporary directory to generate frozen independent NREL-SPA values. Its wheel hash and host environment are evidence in SRC-022 and `VALIDATION_CASES.md`; it is not present in the repository, Android APK, Gradle graph, or dependency register as an application artifact. NREL's downloadable C code is not copied, ported, redistributed, or bundled.

## Sources for approved-in-principle versions

- [AndroidX DataStore releases](https://developer.android.com/jetpack/androidx/releases/datastore)
- [AndroidX Lifecycle releases](https://developer.android.com/jetpack/androidx/releases/lifecycle)
- [Astronomy Engine repository/license](https://github.com/cosinekitty/astronomy)
- [Solarpositioning repository](https://github.com/KlausBrunner/solarpositioning)
