# V1-M4-04 Town Catalogue Evidence

| Field | Evidence |
|---|---|
| Task | V1-M4-04 — Bundled Sri Lankan town catalogue |
| Scope | Immutable offline catalogue only; no UI, repository, persistence selection, location-provider, network, or Sinhala-label change |
| Dataset version | `geonames-lk-2026-08-27T015014Z` |
| Source | `https://download.geonames.org/export/dump/LK.zip` |
| Download/snapshot UTC | `2026-08-27T01:50:14Z` |
| ETag | `"172016-659fd8e989639"` |
| SHA-256 | `27F1ED8CBCB1CF6FA73FED20659B9559186D6DC227434BA4AC749E910DF553A2` |
| Licence / datum / attribution | CC BY 4.0 / WGS84 / GeoNames |
| Extraction | `LK.txt` rows where `featureClass=P` and exact English name, selecting the approved GeoNames IDs |
| Default | Colombo, `geonames:1248991` |
| Validation scope | Automated source-row equality, stable-ID uniqueness, global coordinate range, `LK` source-country containment, exact `Asia/Colombo`, resource-key compilation, default mapping, provenance/hash/version, and no-extra-row checks |
| Automated reviewer | Codex developer automated review |
| Owner catalogue-data decision | 2026-08-27; exact nine rows/default/source facts approved. 2026-08-29 UI-004 exception additionally approves town-only display plus selection/provenance/status for this frozen catalogue; no province/region data, UI schema expansion, coordinate/ID/provenance change, networking, or Sinhala-label authorization. |
| Independent reviewer / review date | Codex (AI agent) / 2026-08-27 |
| Independent review method | Automated hash-verified GeoNames `LK.zip` row comparison plus independent OpenStreetMap Nominatim reverse-geographic review (public API) |

## Per-row source and independent map review

| City | Stable ID | GeoNames ID | GeoNames source lat/lon | E6 lat/lon | Feature | Source equality | Zone | Sri Lanka | OSM Nominatim sanity-only result | Map sanity | Snapshot hash | Method/ref |
|---|---|---:|---|---|---|---|---|---|---|---|---|---|
| Colombo | `geonames:1248991` | 1248991 | 6.935480, 79.848680 | 6935480, 79848680 | PPLC | PASS | Asia/Colombo PASS | PASS | relation 16132; 6.9388614, 79.8542005; Sri Lanka | PASS | `27F1ED8CBCB1CF6FA73FED20659B9559186D6DC227434BA4AC749E910DF553A2` | LK.txt exact-name/ID row; public Nominatim review |
| Kandy | `geonames:1241622` | 1241622 | 7.290600, 80.633600 | 7290600, 80633600 | PPLA | PASS | Asia/Colombo PASS | PASS | node 2908772560; 7.2931208, 80.6350358; Sri Lanka | PASS | `27F1ED8CBCB1CF6FA73FED20659B9559186D6DC227434BA4AC749E910DF553A2` | LK.txt exact-name/ID row; public Nominatim review |
| Galle | `geonames:1246294` | 1246294 | 6.046100, 80.210300 | 6046100, 80210300 | PPLA | PASS | Asia/Colombo PASS | PASS | relation 5345077; 6.0328139, 80.2149550; Sri Lanka | PASS | `27F1ED8CBCB1CF6FA73FED20659B9559186D6DC227434BA4AC749E910DF553A2` | LK.txt exact-name/ID row; public Nominatim review |
| Jaffna | `geonames:1242833` | 1242833 | 9.668450, 80.007420 | 9668450, 80007420 | PPLA | PASS | Asia/Colombo PASS | PASS | node 2050947471; 9.6650930, 80.0093029; Sri Lanka | PASS | `27F1ED8CBCB1CF6FA73FED20659B9559186D6DC227434BA4AC749E910DF553A2` | LK.txt exact-name/ID row; public Nominatim review |
| Trincomalee | `geonames:1226260` | 1226260 | 8.577800, 81.228900 | 8577800, 81228900 | PPLA | PASS | Asia/Colombo PASS | PASS | node 335253618; 8.5764250, 81.2344952; Sri Lanka | PASS | `27F1ED8CBCB1CF6FA73FED20659B9559186D6DC227434BA4AC749E910DF553A2` | LK.txt exact-name/ID row; public Nominatim review |
| Kurunegala | `geonames:1237980` | 1237980 | 7.483900, 80.368300 | 7483900, 80368300 | PPLA | PASS | Asia/Colombo PASS | PASS | way 512253127; 7.4884098, 80.3648003; Sri Lanka | PASS | `27F1ED8CBCB1CF6FA73FED20659B9559186D6DC227434BA4AC749E910DF553A2` | LK.txt exact-name/ID row; public Nominatim review |
| Anuradhapura | `geonames:1251081` | 1251081 | 8.312230, 80.413060 | 8312230, 80413060 | PPLA | PASS | Asia/Colombo PASS | PASS | node 566574942; 8.3349850, 80.4106096; Sri Lanka | PASS | `27F1ED8CBCB1CF6FA73FED20659B9559186D6DC227434BA4AC749E910DF553A2` | LK.txt exact-name/ID row; public Nominatim review |
| Badulla | `geonames:1250615` | 1250615 | 6.980200, 81.057700 | 6980200, 81057700 | PPLA | PASS | Asia/Colombo PASS | PASS | node 1945604886; 6.9900353, 81.0570315; Sri Lanka | PASS | `27F1ED8CBCB1CF6FA73FED20659B9559186D6DC227434BA4AC749E910DF553A2` | LK.txt exact-name/ID row; public Nominatim review |
| Ratnapura | `geonames:1228730` | 1228730 | 6.685800, 80.403600 | 6685800, 80403600 | PPLA | PASS | Asia/Colombo PASS | PASS | node 637897018; 6.6803691, 80.4022975; Sri Lanka | PASS | `27F1ED8CBCB1CF6FA73FED20659B9559186D6DC227434BA4AC749E910DF553A2` | LK.txt exact-name/ID row; public Nominatim review |

The full per-row hash is the snapshot SHA-256 in the evidence header. Nominatim values are independent reverse-geographic sanity evidence only: the frozen GeoNames coordinates remain authoritative for the product catalogue. All returned Sri Lanka; no source or transcription defect was found.

## Developer verification

- `:app:testDebugUnitTest --tests "io.github.dinujaya77.jyotisha.data.location.TownCatalogTest"`: PASS, 4/4.
- `:app:assembleDebug`: PASS.
- Historical V1-M4-04 task run: `:app:lintDebug` failed on one pre-existing V1-M4-02 `MissingPermission` error at `AndroidLocationPlatform.kt:70`, present from baseline `af430fc`; it reported 22 warnings and zero V1-M4-04-owned errors. Later corrective work resolved the error, and the recorded final M4 lint result passed with zero errors. No out-of-scope provider change was made by V1-M4-04.

The catalogue contains exactly these nine owner-approved public towns. Every
`futureSinhalaNameResourceKey` is explicitly `null`; no Sinhala resource is included. No private
family town, unreviewed coordinate, alternate name, downloaded archive, or runtime source is
included. Source-row equality is asserted by `TownCatalogTest` against the frozen facts above.
