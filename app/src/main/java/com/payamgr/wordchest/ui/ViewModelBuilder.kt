package com.payamgr.wordchest.ui

import androidx.compose.runtime.Composable
import com.airbnb.mvrx.compose.mavericksViewModel
import com.payamgr.wordchest.ui.page.home.HomeVM
import com.payamgr.wordchest.ui.page.worddetail.WordDetailVM

open class ViewModelBuilder {
    open fun home(): @Composable () -> HomeVM = { mavericksViewModel() }
    open fun wordDetail(): @Composable () -> WordDetailVM = { mavericksViewModel() }
}

class ViewModelBuilderImpl : ViewModelBuilder()
