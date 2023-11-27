package com.payamgr.wordchest.ui.page.worddetail

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.payamgr.wordchest.data.state.WordDetailState

/**
 * Word Detail View-Model
 */
abstract class WordDetailVM(initialState: WordDetailState) :
    MavericksViewModel<WordDetailState>(initialState) {
    companion object : MavericksViewModelFactory<WordDetailVM, WordDetailState> by hiltMavericksViewModelFactory()

    /**
     * Searches for the [word]
     * @param word the queried word
     */
    abstract fun search(word: String)

    /**
     * Requests to pop the current layer of the history stack.
     * @return nothing (just to enable the caller being single line)
     */
    abstract fun pop(): Any?
}
