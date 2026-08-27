package io.github.dinujaya77.jyotisha.data.location

import io.github.dinujaya77.jyotisha.R
import io.github.dinujaya77.jyotisha.domain.location.LocationPolicy

/** Immutable offline record from the reviewed GeoNames LK snapshot. */
data class TownRecord(
    val stableId: String,
    val geonamesId: Long,
    val englishNameResourceKey: Int,
    val futureSinhalaNameResourceKey: Int?,
    val latitudeE6: Int,
    val longitudeE6: Int,
    val zoneId: String,
    val sourceDatasetVersion: String,
    val sourceFeatureCode: String,
    val sourceCountryCode: String,
)

data class TownCatalogProvenance(
    val datasetVersion: String,
    val sourceUrl: String,
    val sha256: String,
    val downloadedAtUtc: String,
    val etag: String,
    val extractionRule: String,
    val licence: String,
    val datum: String,
    val attribution: String,
)

/**
 * V1-M4-04's complete owner-approved, offline GeoNames LK subset.
 *
 * No runtime download, geocoder, alternate-name, or Sinhala label is bundled here. Manual
 * selection wiring belongs to V1-M4-05; this object only freezes validated source data.
 */
object TownCatalog {
    const val DEFAULT_TOWN_STABLE_ID = "geonames:1248991"

    val provenance = TownCatalogProvenance(
        datasetVersion = "geonames-lk-2026-08-27T015014Z",
        sourceUrl = "https://download.geonames.org/export/dump/LK.zip",
        sha256 = "27F1ED8CBCB1CF6FA73FED20659B9559186D6DC227434BA4AC749E910DF553A2",
        downloadedAtUtc = "2026-08-27T01:50:14Z",
        etag = "\"172016-659fd8e989639\"",
        extractionRule = "LK.txt rows where featureClass=P and exact English name, selecting listed geoname IDs",
        licence = "CC BY 4.0",
        datum = "WGS84",
        attribution = "GeoNames",
    )

    val towns: List<TownRecord> = listOf(
        town(1248991, R.string.town_name_colombo, 6_935_480, 79_848_680, "PPLC"),
        town(1241622, R.string.town_name_kandy, 7_290_600, 80_633_600, "PPLA"),
        town(1246294, R.string.town_name_galle, 6_046_100, 80_210_300, "PPLA"),
        town(1242833, R.string.town_name_jaffna, 9_668_450, 80_007_420, "PPLA"),
        town(1226260, R.string.town_name_trincomalee, 8_577_800, 81_228_900, "PPLA"),
        town(1237980, R.string.town_name_kurunegala, 7_483_900, 80_368_300, "PPLA"),
        town(1251081, R.string.town_name_anuradhapura, 8_312_230, 80_413_060, "PPLA"),
        town(1250615, R.string.town_name_badulla, 6_980_200, 81_057_700, "PPLA"),
        town(1228730, R.string.town_name_ratnapura, 6_685_800, 80_403_600, "PPLA"),
    )

    val defaultTown: TownRecord = towns.single { it.stableId == DEFAULT_TOWN_STABLE_ID }

    fun findByStableId(stableId: String): TownRecord? = towns.singleOrNull { it.stableId == stableId }

    private fun town(
        geonamesId: Long,
        englishNameResourceKey: Int,
        latitudeE6: Int,
        longitudeE6: Int,
        sourceFeatureCode: String,
    ) = TownRecord(
        stableId = "geonames:$geonamesId",
        geonamesId = geonamesId,
        englishNameResourceKey = englishNameResourceKey,
        futureSinhalaNameResourceKey = null,
        latitudeE6 = latitudeE6,
        longitudeE6 = longitudeE6,
        zoneId = LocationPolicy.ASIA_COLOMBO_ZONE_ID,
        sourceDatasetVersion = provenance.datasetVersion,
        sourceFeatureCode = sourceFeatureCode,
        sourceCountryCode = "LK",
    )
}
