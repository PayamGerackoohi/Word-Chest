package com.payamgr.wordchest.data.state

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.payamgr.wordchest.data.model.Word

/**
 * The state of the word detail view-model
 * @property word          the word queried
 * @property layer         the layer of the word in the history stack
 * @property data          list of the definitions of the [word]
 * @property historyList   the list of the stacked words
 * @property searchKey     the queried word for the inner search section
 * @property searchResults a list of most related definitions for the inner search section
 * @property canPop        if true, the word detail can pop back to the home page. Otherwise
 *                         it must stay on the page and just the data would be changed
 * @see Word
 * @see Word.Data
 */
data class WordDetailState(
    @PersistState val word: String = "",
    @PersistState val layer: Int = 0,
    val data: List<Word.Data> = listOf(),
    val historyList: List<String> = listOf(),
    @PersistState val searchKey: String = "",
    val searchResults: List<Word> = listOf(),
) : MavericksState {
    val canPop get() = layer > 0
}
