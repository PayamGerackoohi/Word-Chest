package com.payamgr.wordchest.ui.page.home

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.payamgr.wordchest.data.state.HomeState

/**
 * Home View-Model
 */
abstract class HomeVM(initialState: HomeState) : MavericksViewModel<HomeState>(initialState) {
    companion object : MavericksViewModelFactory<HomeVM, HomeState> by hiltMavericksViewModelFactory()

    /**
     * Word search input change callback
     * @param search the queried word
     */
    abstract fun onSearchChanged(search: String)
}
