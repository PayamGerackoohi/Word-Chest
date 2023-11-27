package com.payamgr.wordchest.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.Mavericks
import com.payamgr.wordchest.data.WordRepository
import com.payamgr.wordchest.ui.theme.WordChestTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository: WordRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mavericks.initialize(this)
        setContent {
            WordChestTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNav.Host(
                        navController = rememberNavController(),
                        push = { key -> lifecycleScope.launch { repository.push(key) } },
                    )
                }
            }
        }
    }
}
