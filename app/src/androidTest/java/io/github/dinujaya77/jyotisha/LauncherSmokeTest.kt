package io.github.dinujaya77.jyotisha

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/** V1-M1-04 launcher compatibility smoke coverage for NFR-009 through NFR-012. */
@RunWith(AndroidJUnit4::class)
class LauncherSmokeTest {
    @Test
    fun launcherStartsMainActivityWithDisplayedRoot() {
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val expectedPackage = "io.github.dinujaya77.jyotisha"
        assertEquals(expectedPackage, targetContext.packageName)

        val launchIntent = requireNotNull(
            targetContext.packageManager.getLaunchIntentForPackage(expectedPackage),
        ) { "The target package must expose a launcher activity" }
        assertEquals(MainActivity::class.java.name, launchIntent.component?.className)

        ActivityScenario.launch<MainActivity>(launchIntent).use { scenario ->
            assertEquals(Lifecycle.State.RESUMED, scenario.state)
            onView(isRoot()).check(matches(isDisplayed()))
        }
    }
}
