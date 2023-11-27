package com.payamgr.wordchest.data.state

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.payamgr.wordchest.data.model.Word

/**
 * The state of the home view-model
 * @property search the queried word
 * @property words  the list of words, mostly related to the [search]
 * @see Word
 */
data class HomeState(
    @PersistState val search: String = "",
    val words: List<Word> = listOf(),
) : MavericksState
