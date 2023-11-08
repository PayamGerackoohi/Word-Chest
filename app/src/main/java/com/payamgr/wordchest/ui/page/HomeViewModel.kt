package com.payamgr.wordchest.ui.page

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.payamgr.wordchest.data.state.HomeState

abstract class HomeViewModel(initialState: HomeState) : MavericksViewModel<HomeState>(initialState) {
    abstract fun onSearchChanged(search: String)

    companion object : MavericksViewModelFactory<HomeViewModel, HomeState> by hiltMavericksViewModelFactory()
}
