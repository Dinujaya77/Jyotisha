package io.github.dinujaya77.jyotisha.data.location

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.dinujaya77.jyotisha.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.xmlpull.v1.XmlPullParser
import java.io.File

/** T-FR-007 / T-NFR-PRIVACY static installed-path and backup-rule coverage for V1-M4-03. */
@RunWith(AndroidJUnit4::class)
class LocationStoragePolicyTest {
    @Test
    fun dataStoreFileUsesCredentialProtectedNoBackupLocationDataDirectory() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val file = LocationSelectionStore.locationDataStoreFile(context)
        val expected = File(
            File(context.noBackupFilesDir, LOCATION_DATA_DIRECTORY_NAME),
            LOCATION_DATA_FILE_NAME,
        )

        assertFalse(context.isDeviceProtectedStorage)
        assertEquals(expected.canonicalPath, file.canonicalPath)
    }

    @Test
    fun legacyCloudAndDeviceTransferRulesExcludeTheCompleteLocationDataDirectory() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val expectedExclusions = setOf(
            "root:no_backup/location-data",
            "device_root:no_backup/location-data",
        )

        assertTrue(exclusions(context.resources.getXml(R.xml.backup_rules)).containsAll(expectedExclusions))
        val extractionExclusions = exclusionsBySection(
            context.resources.getXml(R.xml.data_extraction_rules),
        )
        assertTrue(extractionExclusions.getValue("cloud-backup").containsAll(expectedExclusions))
        assertTrue(extractionExclusions.getValue("device-transfer").containsAll(expectedExclusions))
    }

    private fun exclusions(parser: XmlPullParser): Set<String> = buildSet {
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG && parser.name == "exclude") {
                add("${parser.getAttributeValue(null, "domain")}:${parser.getAttributeValue(null, "path")}")
            }
            parser.next()
        }
    }

    private fun exclusionsBySection(parser: XmlPullParser): Map<String, Set<String>> {
        val exclusions = mutableMapOf<String, MutableSet<String>>()
        var section: String? = null
        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> when (parser.name) {
                    "cloud-backup", "device-transfer" -> section = parser.name
                    "exclude" -> section?.let {
                        exclusions.getOrPut(it) { mutableSetOf() }.add(
                            "${parser.getAttributeValue(null, "domain")}:" +
                                parser.getAttributeValue(null, "path"),
                        )
                    }
                }

                XmlPullParser.END_TAG -> if (parser.name == section) section = null
            }
            parser.next()
        }
        return exclusions
    }
}
