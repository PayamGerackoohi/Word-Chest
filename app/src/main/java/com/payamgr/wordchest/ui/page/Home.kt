package com.payamgr.wordchest.ui.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.payamgr.wordchest.data.state.HomeState
import com.payamgr.wordchest.ui.theme.WordChestTheme


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WordChestTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val viewModel = HomeViewModelImpl(HomeState("Payam"))
            Home.Greeting(viewModel = viewModel)
        }
    }
}

object Home {
    @Composable
    fun Greeting(modifier: Modifier = Modifier, viewModel: HomeViewModel = mavericksViewModel()) {
        val state by viewModel.collectAsState()
        Text(
            text = state.greet(),
            modifier = modifier
        )
    }
}
