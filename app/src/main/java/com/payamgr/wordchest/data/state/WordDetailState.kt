package com.payamgr.wordchest.data.state

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.payamgr.wordchest.data.model.Word

data class WordDetailState(
    @PersistState val layer: Int? = null,
    val data: List<Word.Data> = listOf(),
) : MavericksState
