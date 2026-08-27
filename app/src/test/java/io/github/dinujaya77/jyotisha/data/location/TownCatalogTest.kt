package io.github.dinujaya77.jyotisha.data.location

import io.github.dinujaya77.jyotisha.R
import io.github.dinujaya77.jyotisha.domain.location.LocationPolicy
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** T-FR-006 / V1-M4-04: exact frozen GeoNames LK catalogue validation. */
class TownCatalogTest {
    @Test
    fun catalogueContainsExactlyTheApprovedFrozenSourceRows() {
        assertEquals(expectedTowns.size, TownCatalog.towns.size)
        assertEquals(expectedTowns.map { it.geonamesId }.toSet().size, TownCatalog.towns.map { it.geonamesId }.toSet().size)
        assertEquals(expectedTowns.map { "geonames:${it.geonamesId}" }.toSet(), TownCatalog.towns.map { it.stableId }.toSet())

        expectedTowns.forEach { expected ->
            val actual = requireNotNull(TownCatalog.findByStableId("geonames:${expected.geonamesId}"))
            assertEquals("geonames:${expected.geonamesId}", actual.stableId)
            assertEquals(expected.geonamesId, actual.geonamesId)
            assertEquals(expected.resourceKey, actual.englishNameResourceKey)
            assertEquals(null, actual.futureSinhalaNameResourceKey)
            assertEquals(expected.latitudeE6, actual.latitudeE6)
            assertEquals(expected.longitudeE6, actual.longitudeE6)
            assertEquals(expected.featureCode, actual.sourceFeatureCode)
            assertEquals("LK", actual.sourceCountryCode)
            assertEquals(LocationPolicy.ASIA_COLOMBO_ZONE_ID, actual.zoneId)
            assertEquals(TownCatalog.provenance.datasetVersion, actual.sourceDatasetVersion)
        }
    }

    @Test
    fun catalogueCoordinatesZonesAndResourceKeysAreValid() {
        TownCatalog.towns.forEach { town ->
            assertTrue(town.latitudeE6 in -90_000_000..90_000_000)
            assertTrue(town.longitudeE6 in -180_000_000..180_000_000)
            assertEquals("LK", town.sourceCountryCode)
            assertEquals(LocationPolicy.ASIA_COLOMBO_ZONE_ID, town.zoneId)
            assertTrue(town.englishNameResourceKey != 0)
        }
    }

    @Test
    fun colomboIsTheExactDefaultAndProvenanceIsFrozen() {
        assertEquals("geonames:1248991", TownCatalog.DEFAULT_TOWN_STABLE_ID)
        assertEquals(1248991L, TownCatalog.defaultTown.geonamesId)
        assertEquals(R.string.town_name_colombo, TownCatalog.defaultTown.englishNameResourceKey)
        assertEquals("geonames-lk-2026-08-27T015014Z", TownCatalog.provenance.datasetVersion)
        assertEquals("https://download.geonames.org/export/dump/LK.zip", TownCatalog.provenance.sourceUrl)
        assertEquals(
            "27F1ED8CBCB1CF6FA73FED20659B9559186D6DC227434BA4AC749E910DF553A2",
            TownCatalog.provenance.sha256,
        )
        assertEquals("2026-08-27T01:50:14Z", TownCatalog.provenance.downloadedAtUtc)
        assertEquals("\"172016-659fd8e989639\"", TownCatalog.provenance.etag)
        assertEquals(
            "LK.txt rows where featureClass=P and exact English name, selecting listed geoname IDs",
            TownCatalog.provenance.extractionRule,
        )
        assertEquals("CC BY 4.0", TownCatalog.provenance.licence)
        assertEquals("WGS84", TownCatalog.provenance.datum)
        assertEquals("GeoNames", TownCatalog.provenance.attribution)
    }

    @Test
    fun englishNameResourcesContainOnlyTheApprovedEnglishNames() {
        val stringsXml = stringsXmlFile().readText()
        expectedTowns.forEach { expected ->
            assertTrue(
                "Missing English resource ${expected.resourceName}",
                stringsXml.contains("<string name=\"${expected.resourceName}\">${expected.englishName}</string>"),
            )
        }
        assertFalse(stringsXml.contains("town_name_si_"))
        assertTrue(TownCatalog.towns.all { it.futureSinhalaNameResourceKey == null })
    }

    private fun stringsXmlFile(): File = sequenceOf(
        File("src/main/res/values/strings.xml"),
        File("app/src/main/res/values/strings.xml"),
    ).firstOrNull(File::isFile).also { assertNotNull("English resources must exist.", it) }!!

    private data class ExpectedTown(
        val geonamesId: Long,
        val englishName: String,
        val resourceKey: Int,
        val resourceName: String,
        val latitudeE6: Int,
        val longitudeE6: Int,
        val featureCode: String,
    )

    private companion object {
        val expectedTowns = listOf(
            ExpectedTown(1248991, "Colombo", R.string.town_name_colombo, "town_name_colombo", 6_935_480, 79_848_680, "PPLC"),
            ExpectedTown(1241622, "Kandy", R.string.town_name_kandy, "town_name_kandy", 7_290_600, 80_633_600, "PPLA"),
            ExpectedTown(1246294, "Galle", R.string.town_name_galle, "town_name_galle", 6_046_100, 80_210_300, "PPLA"),
            ExpectedTown(1242833, "Jaffna", R.string.town_name_jaffna, "town_name_jaffna", 9_668_450, 80_007_420, "PPLA"),
            ExpectedTown(1226260, "Trincomalee", R.string.town_name_trincomalee, "town_name_trincomalee", 8_577_800, 81_228_900, "PPLA"),
            ExpectedTown(1237980, "Kurunegala", R.string.town_name_kurunegala, "town_name_kurunegala", 7_483_900, 80_368_300, "PPLA"),
            ExpectedTown(1251081, "Anuradhapura", R.string.town_name_anuradhapura, "town_name_anuradhapura", 8_312_230, 80_413_060, "PPLA"),
            ExpectedTown(1250615, "Badulla", R.string.town_name_badulla, "town_name_badulla", 6_980_200, 81_057_700, "PPLA"),
            ExpectedTown(1228730, "Ratnapura", R.string.town_name_ratnapura, "town_name_ratnapura", 6_685_800, 80_403_600, "PPLA"),
        )
    }
}
