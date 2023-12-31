package com.payamgr.wordchest.ui.page.home

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.payamgr.wordchest.data.WordRepository
import com.payamgr.wordchest.data.hilt.ConstantsModule
import com.payamgr.wordchest.data.state.HomeState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

class HomeVMImpl @AssistedInject constructor(
    @Assisted initialState: HomeState,
    private val repository: WordRepository,
    @ConstantsModule.SearchDelay private val searchDelay: Long,
) : HomeVM(initialState) {

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<HomeVMImpl, HomeState>

    init {
        subscribeToStateOnSearch()
    }

    override fun onSearchChanged(search: String) = setState { copy(search = search) }

    private fun subscribeToStateOnSearch() = onEach(HomeState::search) {
        delay(searchDelay)
        searchFor(it)
    }

    private suspend fun searchFor(key: String) {
        val words = repository.searchFor(key)
        setState { copy(words = words) }
    }
}
