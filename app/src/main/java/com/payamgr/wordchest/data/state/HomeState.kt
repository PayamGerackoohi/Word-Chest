package com.payamgr.wordchest.data.state

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.payamgr.wordchest.data.model.Word

data class HomeState(
    @PersistState val search: String = "",
    @PersistState val words: List<Word> = listOf(),
) : MavericksState
