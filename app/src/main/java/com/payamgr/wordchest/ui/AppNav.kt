package com.payamgr.wordchest.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.payamgr.wordchest.data.model.WordKey
import com.payamgr.wordchest.ui.page.home.Home
import com.payamgr.wordchest.ui.page.home.Home.homePage
import com.payamgr.wordchest.ui.page.worddetail.WordDetail.navigateToWordDetail
import com.payamgr.wordchest.ui.page.worddetail.WordDetail.wordDetail

object AppNav {
    @Composable
    fun Host(
        navController: NavHostController,
        push: (key: WordKey) -> Unit,
        viewModelBuilder: ViewModelBuilder = ViewModelBuilderImpl(),
    ) {
        NavHost(navController = navController, startDestination = Home.ROUTE) {
            homePage(
                viewModelBuilder = viewModelBuilder.home(),
                navigateToWordDetail = { word ->
                    push(WordKey.Word(word, true))
                    navController.navigateToWordDetail()
                },
            )
            wordDetail(
                viewModelBuilder = viewModelBuilder.wordDetail(),
                showWordDetail = push,
            )
        }
    }
}
