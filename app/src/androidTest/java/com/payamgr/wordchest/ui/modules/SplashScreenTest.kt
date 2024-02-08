package com.payamgr.wordchest.ui.modules

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import com.payamgr.wordchest.ui.theme.WordChestTheme
import com.payamgr.wordchest.util.Screenshot
import com.payamgr.wordchest.util.hideActionBar
import com.payamgr.wordchest.util.take
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//class SplashScreenTest {
//    @get:Rule
//    val rule = createComposeRule()
//}

abstract class ActivityTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        rule.hideActionBar()
    }
}

class SplashScreenActivityTest : ActivityTest() {
    @Test
    fun moduleTest() = runTest {
        val onEnd = mockk<() -> Unit>()
        val mutex = Mutex(true)
        var isOnEndCalled = false
        justRun { onEnd() }

        rule.setContent {
            WordChestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SplashScreen.Module(
                        duration = 10,
                        onEnd = {
                            onEnd()
                            isOnEndCalled = true
                        },
                        start = { mutex.lock() }
                    )
                }
            }
        }

        // Verify the initial state
        rule.onNodeWithContentDescription("Splashscreen").isDisplayed()
        rule.onNodeWithTag("SplashScreen.IconBackground").isDisplayed()
        rule.onNodeWithContentDescription("Animated Vector: Splashscreen Icon").isDisplayed()

        Screenshot.Splashscreen.take()

        // - Verify the initial state transition from 'Beginning' to 'End'
        rule.onNode(hasStateDescription("Beginning")).assertExists()
        mutex.unlock()
        rule.onNode(hasStateDescription("End")).assertExists()

        // Verify the 'onEnd' callback is called after 10ms
        rule.waitUntil { isOnEndCalled }
        verify { onEnd() }

        // - The animated vector state is the 'End'
        rule.onNode(hasStateDescription("End")).assertExists()

        // confirmVerified() // UNSTABLE
    }
}
