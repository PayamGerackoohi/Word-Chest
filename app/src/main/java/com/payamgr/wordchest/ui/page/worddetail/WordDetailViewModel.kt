package com.payamgr.wordchest.ui.page.worddetail

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.payamgr.wordchest.data.state.WordDetailState
import kotlinx.coroutines.flow.StateFlow

abstract class WordDetailViewModel(initialState: WordDetailState) :
    MavericksViewModel<WordDetailState>(initialState) {
    abstract fun set(layer: Int)

    companion object :
        MavericksViewModelFactory<WordDetailViewModel, WordDetailState> by hiltMavericksViewModelFactory()
}
