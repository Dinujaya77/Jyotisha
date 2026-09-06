package io.github.dinujaya77.jyotisha.ui.location

import io.github.dinujaya77.jyotisha.ChildDestination
import io.github.dinujaya77.jyotisha.ShellAction
import io.github.dinujaya77.jyotisha.ShellState
import io.github.dinujaya77.jyotisha.reduceShellState
import io.github.dinujaya77.jyotisha.transitionShellState
import io.github.dinujaya77.jyotisha.data.location.LocationFallback
import io.github.dinujaya77.jyotisha.data.location.LocationSelectionPersistence
import io.github.dinujaya77.jyotisha.data.location.LocationSelectionRepository
import io.github.dinujaya77.jyotisha.data.location.LocationSelectionOperations
import io.github.dinujaya77.jyotisha.data.location.LocationSelectionState
import io.github.dinujaya77.jyotisha.data.location.PersistedLocationSelection
import io.github.dinujaya77.jyotisha.data.location.PersistedLocationSelectionRead
import io.github.dinujaya77.jyotisha.data.location.TownCatalog
import io.github.dinujaya77.jyotisha.domain.location.ForegroundLocationPermission
import io.github.dinujaya77.jyotisha.domain.location.DeviceLocationFix
import io.github.dinujaya77.jyotisha.domain.location.LocationProvenance
import io.github.dinujaya77.jyotisha.domain.location.LocationSource
import io.github.dinujaya77.jyotisha.domain.location.PermissionPrecision
import io.github.dinujaya77.jyotisha.domain.location.SelectedLocation
import io.github.dinujaya77.jyotisha.domain.location.SelectedLocationMode
import io.github.dinujaya77.jyotisha.platform.location.LocationPlatform
import io.github.dinujaya77.jyotisha.platform.location.LocationPlatformCancellation
import io.github.dinujaya77.jyotisha.platform.location.LocationPlatformState
import io.github.dinujaya77.jyotisha.platform.location.LocationTimeoutScheduler
import io.github.dinujaya77.jyotisha.platform.location.OneShotDeviceLocationProvider
import io.github.dinujaya77.jyotisha.platform.location.TimeoutHandle
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.util.concurrent.CountDownLatch
import java.util.Collections
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** T-FR-004/005/007: lifecycle cancellation and permission reconciliation are controller-owned. */
class LocationRuntimeControllerTest {
    @Test
    fun cancellationBetweenEligibilityCheckAndCoroutineStartDoesNotLaunchAcquisition() {
        val repository = DeferredRestoreRepository().apply { deferRestore = false }
        val interleavingReached = CountDownLatch(1)
        val worker = Executors.newSingleThreadExecutor()
        lateinit var controller: LocationRuntimeController
        controller = LocationRuntimeController(
            repository = repository,
            permission = { ForegroundLocationPermission.PRECISE },
            mainExecutor = Executor { it.run() },
            onState = {},
            worker = worker,
            afterCurrentGenerationCheck = {
                interleavingReached.countDown()
                controller.onBackgrounded()
            },
        )

        controller.useCurrentLocation()

        assertTrue(interleavingReached.await(5, TimeUnit.SECONDS))
        worker.shutdown()
        assertTrue(worker.awaitTermination(5, TimeUnit.SECONDS))
        assertEquals(1, repository.cancelCount)
        assertEquals(0, repository.refreshCount)
        controller.close()
    }

    @Test
    fun backgroundingCancelsAndSuppressesAnAcquisitionQueuedBehindRestore() {
        val repository = DeferredRestoreRepository()
        val controller = controller(repository, ForegroundLocationPermission.PRECISE)

        controller.restore()
        assertTrue(repository.restoreStarted.await(5, TimeUnit.SECONDS))
        controller.useCurrentLocation()
        controller.onBackgrounded()
        repository.releaseRestore()

        assertTrue(repository.restoreCompleted.await(5, TimeUnit.SECONDS))
        assertEquals(1, repository.cancelCount)
        assertEquals(0, repository.refreshCount)
        controller.close()
    }

    @Test
    fun foregroundResumeReconcilesTheCurrentPermissionWithoutStartingAcquisition() {
        val repository = DeferredRestoreRepository().apply { deferRestore = false }
        val permissions = mutableListOf<ForegroundLocationPermission>()
        val controller = LocationRuntimeController(
            repository = repository,
            permission = { ForegroundLocationPermission.APPROXIMATE.also(permissions::add) },
            mainExecutor = Executor { it.run() },
            onState = {},
        )

        controller.onForegrounded()
        assertTrue(repository.restoreCompleted.await(5, TimeUnit.SECONDS))
        assertEquals(listOf(ForegroundLocationPermission.APPROXIMATE), permissions)
        assertEquals(0, repository.refreshCount)
        controller.close()
    }

    @Test
    fun configurationRecreationResumeKeepsAnActiveForegroundRequest() {
        val repository = CallbackOnCancellationRepository()
        val controller = LocationRuntimeController(
            repository = repository,
            permission = { ForegroundLocationPermission.PRECISE },
            mainExecutor = Executor { it.run() },
            onState = {},
            worker = Executors.newSingleThreadExecutor(),
        )

        controller.useCurrentLocation()
        assertTrue(repository.refreshStarted.await(5, TimeUnit.SECONDS))
        controller.onForegrounded()

        assertEquals(0, repository.cancelCount)
        controller.onBackgrounded()
        assertEquals(1, repository.cancelCount)
        controller.close()
    }

    @Test
    fun routeExitCancelsAnActiveForegroundRequestWhileConfigurationRecreationDoesNot() {
        val repository = CallbackOnCancellationRepository()
        val controller = LocationRuntimeController(
            repository = repository,
            permission = { ForegroundLocationPermission.PRECISE },
            mainExecutor = Executor { it.run() },
            onState = {},
            worker = Executors.newSingleThreadExecutor(),
        )

        controller.useCurrentLocation()
        assertTrue(repository.refreshStarted.await(5, TimeUnit.SECONDS))
        controller.onForegrounded() // The Activity recreation path retains the request.
        assertEquals(0, repository.cancelCount)

        controller.cancelForRouteExit()
        assertEquals(1, repository.cancelCount)
        controller.close()
    }

    @Test
    fun viewModelClearCancelsAnAcquisitionThatIsStillStarting() {
        val repository = CallbackOnCancellationRepository()
        val controller = LocationRuntimeController(
            repository = repository,
            permission = { ForegroundLocationPermission.PRECISE },
            mainExecutor = Executor { it.run() },
            onState = {},
            worker = Executors.newSingleThreadExecutor(),
        )

        controller.useCurrentLocation()
        assertTrue(repository.refreshStarted.await(5, TimeUnit.SECONDS))

        controller.close()

        assertEquals(1, repository.cancelCount)
    }

    @Test
    fun backgroundDuringProviderPreflightCancelsBeforePlatformAcquisitionStarts() {
        val fixture = controllerPreflightFixture()

        fixture.controller.useCurrentLocation()
        assertTrue(fixture.platform.preflightEntered.await(5, TimeUnit.SECONDS))

        fixture.controller.onBackgrounded()
        fixture.platform.releasePreflight()
        fixture.controller.close()

        assertTrue(fixture.worker.awaitTermination(5, TimeUnit.SECONDS))
        assertTrue(fixture.platform.cancellation!!.cancelled)
        assertEquals(0, fixture.platform.platformRequestCount)
    }

    @Test
    fun viewModelClearDuringProviderPreflightCancelsBeforePlatformAcquisitionStarts() {
        val fixture = controllerPreflightFixture()

        fixture.controller.useCurrentLocation()
        assertTrue(fixture.platform.preflightEntered.await(5, TimeUnit.SECONDS))

        fixture.controller.close()
        fixture.platform.releasePreflight()

        assertTrue(fixture.worker.awaitTermination(5, TimeUnit.SECONDS))
        assertTrue(fixture.platform.cancellation!!.cancelled)
        assertEquals(0, fixture.platform.platformRequestCount)
    }

    @Test
    fun locationToSettingsShellExitCancelsWhileSameLocationRouteDoesNot() {
        val repository = CallbackOnCancellationRepository()
        val controller = LocationRuntimeController(
            repository = repository,
            permission = { ForegroundLocationPermission.PRECISE },
            mainExecutor = Executor { it.run() },
            onState = {},
            worker = Executors.newSingleThreadExecutor(),
        )
        val settings = ShellState(child = ChildDestination.Settings)
        val locationFromSettings = reduceShellState(settings, ShellAction.OpenLocation)

        controller.useCurrentLocation()
        assertTrue(repository.refreshStarted.await(5, TimeUnit.SECONDS))

        assertEquals(
            locationFromSettings,
            transitionShellState(
                locationFromSettings,
                ShellAction.OpenLocation,
                controller::cancelForRouteExit,
            ),
        )
        assertEquals(0, repository.cancelCount)
        assertEquals(
            settings,
            transitionShellState(
                locationFromSettings,
                ShellAction.Back,
                controller::cancelForRouteExit,
            ),
        )
        assertEquals(1, repository.cancelCount)
        controller.close()
    }

    @Test
    fun precisePermissionUpgradeRequestsPermissionOnlyForApproximateAccess() {
        ForegroundLocationPermission.entries.forEach { currentPermission ->
            val repository = DeferredRestoreRepository()
            val states = mutableListOf<LocationRuntimeState>()
            val controller = LocationRuntimeController(
                repository = repository,
                permission = { currentPermission },
                mainExecutor = Executor { it.run() },
                onState = states::add,
            )

            controller.requestPrecisePermissionUpgrade()

            assertEquals(
                if (currentPermission == ForegroundLocationPermission.APPROXIMATE) {
                    listOf(LocationAcquisition.REQUEST_PERMISSION)
                } else {
                    emptyList()
                },
                states.map(LocationRuntimeState::acquisition),
            )
            assertEquals(0, repository.refreshCount)
            controller.close()
        }
    }

    @Test
    fun manualDefaultAndResetSynchronouslyCancelRefreshBeforeTheirQueuedSelection() {
        assertSupersedingSelection("manual") { selectTown(TownCatalog.defaultTown.stableId) }
        assertSupersedingSelection("default") { selectDefault() }
        assertSupersedingSelection("reset") { reset() }
    }

    @Test
    fun deniedPermissionPublishesNeutralRecoveryWithoutAnAutomaticPrompt() {
        val repository = DeferredRestoreRepository().apply { deferRestore = false }
        val states = Collections.synchronizedList(mutableListOf<LocationRuntimeState>())
        val neutralRecoveryPublished = CountDownLatch(1)
        val controller = LocationRuntimeController(
            repository = repository,
            permission = { ForegroundLocationPermission.NONE },
            mainExecutor = Executor { it.run() },
            onState = { state ->
                states += state
                if (state.acquisition == LocationAcquisition.PERMISSION_UNAVAILABLE &&
                    state.selection != null
                ) {
                    neutralRecoveryPublished.countDown()
                }
            },
        )

        controller.onPermissionResult()

        assertTrue(repository.restoreCompleted.await(5, TimeUnit.SECONDS))
        assertTrue(neutralRecoveryPublished.await(5, TimeUnit.SECONDS))
        assertEquals(LocationAcquisition.PERMISSION_UNAVAILABLE, states.last().acquisition)
        assertEquals(SelectedLocationMode.DEFAULT, states.last().selection?.selectedMode)
        assertTrue(
            synchronized(states) {
                states.none { it.acquisition == LocationAcquisition.REQUEST_PERMISSION }
            },
        )
        assertEquals(0, repository.refreshCount)
        controller.close()
    }

    @Test
    fun restoreAndSelectionFailuresPublishErrorInsteadOfLeavingLoadingState() {
        assertFailurePublishesError { restore() }
        assertFailurePublishesError { selectDefault() }
    }

    private fun assertSupersedingSelection(
        expectedSelection: String,
        action: LocationRuntimeController.() -> Unit,
    ) {
        val repository = CallbackOnCancellationRepository()
        val publishedStates = Collections.synchronizedList(mutableListOf<LocationRuntimeState>())
        val controller = LocationRuntimeController(
            repository = repository,
            permission = { ForegroundLocationPermission.PRECISE },
            mainExecutor = Executor { it.run() },
            onState = publishedStates::add,
            worker = Executors.newSingleThreadExecutor(),
        )

        controller.useCurrentLocation()
        assertTrue(repository.refreshStarted.await(5, TimeUnit.SECONDS))

        controller.action()

        assertEquals(1, repository.cancelCount)
        assertTrue(repository.selectionCompleted.await(5, TimeUnit.SECONDS))
        assertEquals(
            listOf("cancel", expectedSelection),
            synchronized(repository.events) { repository.events.toList() },
        )
        assertTrue(
            synchronized(publishedStates) {
                publishedStates.none { it.selection?.selectedMode == SelectedLocationMode.CURRENT_DEVICE }
            },
        )
        controller.close()
    }

    private fun assertFailurePublishesError(action: LocationRuntimeController.() -> Unit) {
        val errorPublished = CountDownLatch(1)
        val controller = LocationRuntimeController(
            repository = ThrowingRepository(),
            permission = { ForegroundLocationPermission.PRECISE },
            mainExecutor = Executor { it.run() },
            onState = { state ->
                if (state.acquisition == LocationAcquisition.ERROR) errorPublished.countDown()
            },
            worker = Executors.newSingleThreadExecutor(),
        )

        controller.action()

        assertTrue(errorPublished.await(5, TimeUnit.SECONDS))
        controller.close()
    }

    private fun controller(
        repository: DeferredRestoreRepository,
        permission: ForegroundLocationPermission,
    ) = LocationRuntimeController(
        repository = repository,
        permission = { permission },
        mainExecutor = Executor { it.run() },
        onState = {},
        worker = Executors.newSingleThreadExecutor(),
    )

    private fun controllerPreflightFixture(): ControllerPreflightFixture {
        val platform = BlockingPreflightPlatform()
        val worker = Executors.newSingleThreadExecutor()
        val repository = LocationSelectionRepository(
            persistence = DefaultPersistence(),
            deviceLocationProvider = OneShotDeviceLocationProvider(
                platform = platform,
                timeoutScheduler = NoOpTimeoutScheduler(),
            ),
            clock = Clock.fixed(Instant.ofEpochMilli(1_800_000_000_000L), ZoneOffset.UTC),
            zoneSource = { "Asia/Colombo" },
        )
        return ControllerPreflightFixture(
            controller = LocationRuntimeController(
                repository = repository,
                permission = { ForegroundLocationPermission.PRECISE },
                mainExecutor = Executor { it.run() },
                onState = {},
                worker = worker,
            ),
            platform = platform,
            worker = worker,
        )
    }

    private data class ControllerPreflightFixture(
        val controller: LocationRuntimeController,
        val platform: BlockingPreflightPlatform,
        val worker: java.util.concurrent.ExecutorService,
    )

    private class DefaultPersistence : LocationSelectionPersistence {
        override suspend fun read() = PersistedLocationSelectionRead(PersistedLocationSelection.Default())
        override suspend fun selectDevice(selection: PersistedLocationSelection.Device) = true
        override suspend fun selectDeviceIfCurrent(
            selection: PersistedLocationSelection.Device,
            mayWrite: () -> Boolean,
        ) = mayWrite()
        override suspend fun selectManualTown(townId: String, selectedAtEpochMillis: Long) = true
        override suspend fun selectDefault(datasetVersion: String?) = true
        override suspend fun resetLocationData() = Unit
    }

    private class BlockingPreflightPlatform : LocationPlatform {
        val preflightEntered = CountDownLatch(1)
        var cancellation: FakePlatformCancellation? = null
        var platformRequestCount = 0
        private val release = CountDownLatch(1)

        override fun stateFor(permission: ForegroundLocationPermission): LocationPlatformState {
            preflightEntered.countDown()
            release.await(5, TimeUnit.SECONDS)
            return LocationPlatformState(
                apiLevel = 36,
                locationEnabled = true,
                enabledProviders = setOf("fused"),
                bestProvider = "fused",
            )
        }

        override fun createCancellation(): LocationPlatformCancellation =
            FakePlatformCancellation().also { cancellation = it }

        override fun requestCurrentLocation(
            provider: String,
            permission: ForegroundLocationPermission,
            cancellation: LocationPlatformCancellation,
            onLocation: (DeviceLocationFix?) -> Unit,
        ) {
            platformRequestCount += 1
        }

        override fun elapsedRealtimeMillis() = 1_000_000L

        fun releasePreflight() = release.countDown()
    }

    private class FakePlatformCancellation : LocationPlatformCancellation {
        var cancelled = false
        override fun cancel() {
            cancelled = true
        }
    }

    private class NoOpTimeoutScheduler : LocationTimeoutScheduler {
        override fun schedule(delayMillis: Long, action: () -> Unit): TimeoutHandle =
            object : TimeoutHandle {
                override fun cancel() = Unit
            }
    }

    private class DeferredRestoreRepository : LocationSelectionOperations {
        val restoreStarted = CountDownLatch(1)
        val restoreCompleted = CountDownLatch(1)
        var deferRestore = true
        var refreshCount = 0
        var cancelCount = 0
        private var release: (() -> Unit)? = null

        override suspend fun restore(currentPermission: ForegroundLocationPermission): LocationSelectionState {
            if (!deferRestore) {
                restoreCompleted.countDown()
                return defaultState()
            }
            return suspendCoroutine { continuation ->
                release = {
                    restoreCompleted.countDown()
                    continuation.resume(defaultState())
                }
                restoreStarted.countDown()
            }
        }

        override suspend fun refreshCurrentLocation(
            currentPermission: ForegroundLocationPermission,
        ): LocationSelectionState {
            refreshCount += 1
            return defaultState()
        }

        override suspend fun selectManualTown(townStableId: String) = defaultState()
        override suspend fun selectDefault() = defaultState()
        override suspend fun resetLocationData() = defaultState()
        override fun cancelCurrentLocationRequest() {
            cancelCount += 1
        }

        fun releaseRestore() = requireNotNull(release).invoke()

        private fun defaultState(): LocationSelectionState {
            val town = TownCatalog.defaultTown
            return LocationSelectionState(
                selectedLocation = SelectedLocation(
                    coordinates = io.github.dinujaya77.jyotisha.domain.location.GeoCoordinates(
                        town.latitudeE6 / 1_000_000.0,
                        town.longitudeE6 / 1_000_000.0,
                    ),
                    provenance = LocationProvenance(
                        source = LocationSource.DEFAULT,
                        zoneId = "Asia/Colombo",
                        datasetVersion = TownCatalog.provenance.datasetVersion,
                    ),
                ),
                selectedTown = town,
                selectedMode = SelectedLocationMode.DEFAULT,
                fallback = LocationFallback.DEFAULT,
                isFirstUse = false,
            )
        }
    }

    /** Simulates a provider callback that completes as cancellation is delivered. */
    private class CallbackOnCancellationRepository : LocationSelectionOperations {
        val refreshStarted = CountDownLatch(1)
        val selectionCompleted = CountDownLatch(1)
        val events = Collections.synchronizedList(mutableListOf<String>())
        var cancelCount = 0
        private var refreshContinuation: Continuation<LocationSelectionState>? = null

        override suspend fun restore(currentPermission: ForegroundLocationPermission) = defaultState()

        override suspend fun refreshCurrentLocation(
            currentPermission: ForegroundLocationPermission,
        ): LocationSelectionState = suspendCoroutine { continuation ->
            refreshContinuation = continuation
            refreshStarted.countDown()
        }

        override suspend fun selectManualTown(townStableId: String): LocationSelectionState =
            completeSelection("manual")

        override suspend fun selectDefault(): LocationSelectionState = completeSelection("default")

        override suspend fun resetLocationData(): LocationSelectionState = completeSelection("reset")

        override fun cancelCurrentLocationRequest() {
            cancelCount += 1
            events += "cancel"
            refreshContinuation?.also { continuation ->
                refreshContinuation = null
                continuation.resume(currentDeviceState())
            }
        }

        private fun completeSelection(name: String): LocationSelectionState {
            events += name
            selectionCompleted.countDown()
            return defaultState()
        }

        private fun currentDeviceState(): LocationSelectionState {
            val town = TownCatalog.defaultTown
            return LocationSelectionState(
                selectedLocation = SelectedLocation(
                    coordinates = io.github.dinujaya77.jyotisha.domain.location.GeoCoordinates(
                        town.latitudeE6 / 1_000_000.0,
                        town.longitudeE6 / 1_000_000.0,
                    ),
                    provenance = LocationProvenance(
                        source = LocationSource.CURRENT_DEVICE,
                        zoneId = "Asia/Colombo",
                        horizontalAccuracyMeters = 25.0,
                        permissionPrecision = PermissionPrecision.PRECISE,
                        acquisitionEpochMillis = 0L,
                    ),
                ),
                selectedTown = null,
                selectedMode = SelectedLocationMode.CURRENT_DEVICE,
                fallback = LocationFallback.CURRENT_DEVICE,
                isFirstUse = false,
            )
        }

        private fun defaultState(): LocationSelectionState {
            val town = TownCatalog.defaultTown
            return LocationSelectionState(
                selectedLocation = SelectedLocation(
                    coordinates = io.github.dinujaya77.jyotisha.domain.location.GeoCoordinates(
                        town.latitudeE6 / 1_000_000.0,
                        town.longitudeE6 / 1_000_000.0,
                    ),
                    provenance = LocationProvenance(
                        source = LocationSource.DEFAULT,
                        zoneId = "Asia/Colombo",
                        datasetVersion = TownCatalog.provenance.datasetVersion,
                    ),
                ),
                selectedTown = town,
                selectedMode = SelectedLocationMode.DEFAULT,
                fallback = LocationFallback.DEFAULT,
                isFirstUse = false,
            )
        }
    }

    private class ThrowingRepository : LocationSelectionOperations {
        override suspend fun restore(currentPermission: ForegroundLocationPermission): LocationSelectionState =
            throw IllegalStateException("restore failed")

        override suspend fun refreshCurrentLocation(
            currentPermission: ForegroundLocationPermission,
        ): LocationSelectionState = throw IllegalStateException("refresh failed")

        override suspend fun selectManualTown(townStableId: String): LocationSelectionState =
            throw IllegalStateException("town selection failed")

        override suspend fun selectDefault(): LocationSelectionState =
            throw IllegalStateException("default selection failed")

        override suspend fun resetLocationData(): LocationSelectionState =
            throw IllegalStateException("reset failed")

        override fun cancelCurrentLocationRequest() = Unit
    }
}
