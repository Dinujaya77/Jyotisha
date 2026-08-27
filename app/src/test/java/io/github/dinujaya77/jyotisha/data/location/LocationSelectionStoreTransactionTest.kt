package io.github.dinujaya77.jyotisha.data.location

import io.github.dinujaya77.jyotisha.domain.location.GeoCoordinates
import io.github.dinujaya77.jyotisha.domain.location.LocationSource
import io.github.dinujaya77.jyotisha.domain.location.PermissionPrecision
import java.io.File
import java.nio.file.Files
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** T-FR-007 / V1-M4-03: real DataStore edit/read coverage without direct kotlinx APIs. */
class LocationSelectionStoreTransactionTest {
    @Test
    fun storeWritesAndReadsEachSelectionModeAndResolvesAnEmptyResetRecord() {
        val device = PersistedLocationSelection.Device(
            coordinates = GeoCoordinates(6.9, 79.8),
            accuracyMeters = 20_000.0,
            permissionPrecision = PermissionPrecision.APPROXIMATE,
            acquiredAtEpochMillis = 40L,
            source = LocationSource.CURRENT_DEVICE,
        )
        val manual = PersistedLocationSelection.ManualTown("public-town-id", 50L)

        runSuspend {
            val deviceStore = createStore()
            assertTrue(deviceStore.selectDevice(device))
            assertEquals(device, deviceStore.read().selection)

            val manualStore = createStore()
            assertTrue(manualStore.selectManualTown(manual.townId, manual.selectedAtEpochMillis))
            val manualRead = manualStore.read()
            assertEquals(manual, manualRead.selection)
            assertNull(manualRead.recoveryWarning)

            val defaultStore = createStore()
            assertTrue(defaultStore.selectDefault())
            assertEquals(PersistedLocationSelection.Default(), defaultStore.read().selection)

            val resetStore = createStore()
            resetStore.resetLocationData()
            val resetRead = resetStore.read()
            assertEquals(PersistedLocationSelection.Default(), resetRead.selection)
            assertNull(resetRead.recoveryWarning)
        }
    }

    private fun createStore(): LocationSelectionStore {
        val directory = Files.createTempDirectory("jyotisha-location-store-test").toFile()
        return LocationSelectionStore.create(File(directory, LOCATION_DATA_FILE_NAME))
    }

    private fun <T> runSuspend(block: suspend () -> T): T {
        val latch = CountDownLatch(1)
        var result: Result<T>? = null
        block.startCoroutine(
            object : Continuation<T> {
                override val context = EmptyCoroutineContext

                override fun resumeWith(outcome: Result<T>) {
                    result = outcome
                    latch.countDown()
                }
            },
        )
        check(latch.await(10, TimeUnit.SECONDS)) { "Timed out waiting for DataStore test operation." }
        return checkNotNull(result).getOrThrow()
    }
}
