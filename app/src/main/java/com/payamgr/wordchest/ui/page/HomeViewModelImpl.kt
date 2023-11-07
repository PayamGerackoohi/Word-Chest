package com.payamgr.wordchest.ui.page

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.payamgr.wordchest.data.state.HomeState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class HomeViewModelImpl @AssistedInject constructor(
    @Assisted initialState: HomeState,
) : HomeViewModel(initialState) {
    @AssistedFactory
    interface Factory : AssistedViewModelFactory<HomeViewModelImpl, HomeState>
}
