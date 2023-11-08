package com.payamgr.wordchest.ui.page

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.payamgr.wordchest.data.WordRepository
import com.payamgr.wordchest.data.state.HomeState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

class HomeViewModelImpl @AssistedInject constructor(
    @Assisted initialState: HomeState,
    private val repository: WordRepository,
) : HomeViewModel(initialState) {
    companion object {
        private const val SEARCH_DELAY = 500L
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<HomeViewModelImpl, HomeState>

    init {
        subscribeToStateOnSearch()
    }

    override fun onSearchChanged(search: String) = setState { copy(search = search) }

    private fun subscribeToStateOnSearch() = onEach(HomeState::search) {
        delay(SEARCH_DELAY)
        searchFor(it)
    }

    private suspend fun searchFor(key: String) {
        val words = repository.searchFor(key)
        setState { copy(words = words) }
    }
}
