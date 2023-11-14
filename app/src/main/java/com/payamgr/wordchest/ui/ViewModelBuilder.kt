package com.payamgr.wordchest.ui

import androidx.compose.runtime.Composable
import com.airbnb.mvrx.compose.mavericksViewModel
import com.payamgr.wordchest.ui.page.home.HomeViewModel
import com.payamgr.wordchest.ui.page.worddetail.WordDetailViewModel

open class ViewModelBuilder {
    open fun home(): @Composable () -> HomeViewModel = { mavericksViewModel() }
    open fun wordDetail(): @Composable () -> WordDetailViewModel = { mavericksViewModel() }
}

class ViewModelBuilderImpl : ViewModelBuilder()
