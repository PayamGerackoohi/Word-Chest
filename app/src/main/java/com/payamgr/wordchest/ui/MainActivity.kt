package com.payamgr.wordchest.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.Mavericks
import com.payamgr.wordchest.data.WordRepository
import com.payamgr.wordchest.data.hilt.provideAppModule
import com.payamgr.wordchest.ui.page.splashscreen.Splashscreen
import com.payamgr.wordchest.ui.theme.WordChestTheme
import com.payamgr.wordchest.ui.util.isAndroidSdk31plus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository: WordRepository

    /**
     * Normal @Inject provides the object after "super.onCreate", so [provideAppModule] is used.
     */
    private val splashScreenController by lazy { provideAppModule().splashScreenController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (isAndroidSdk31plus) setupSplashScreen()
        super.onCreate(savedInstanceState)
        Mavericks.initialize(this)
        setContent {
            WordChestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Splashscreen.Page(
                        isUsingGoogleSplashScreen = isAndroidSdk31plus,
                        duration = splashScreenController.totalTime,
                        keepSplashScreen = splashScreenController.keep,
                        hideSplashScreen = splashScreenController::hide,
                    ) {
                        AppNav.Host(
                            navController = rememberNavController(),
                            push = { key -> lifecycleScope.launch { repository.push(key) } },
                        )
                    }
                }
            }
        }
    }

    private fun setupSplashScreen() {
        installSplashScreen().setKeepOnScreenCondition { splashScreenController.keep }
        lifecycleScope.launch {
            delay(splashScreenController.duration)
            splashScreenController.hide()
        }
    }
}
