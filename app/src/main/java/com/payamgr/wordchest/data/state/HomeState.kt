package com.payamgr.wordchest.data.state

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState

data class HomeState(
    @PersistState val user: String = "",
) : MavericksState {
    fun greet() = "Hello${if (user.isBlank()) "" else " $user"}!"
}
