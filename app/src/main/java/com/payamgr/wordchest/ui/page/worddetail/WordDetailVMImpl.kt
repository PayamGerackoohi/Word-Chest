package com.payamgr.wordchest.ui.page.worddetail

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.payamgr.wordchest.data.WordRepository
import com.payamgr.wordchest.data.hilt.ConstantsModule
import com.payamgr.wordchest.data.state.WordDetailState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WordDetailVMImpl @AssistedInject constructor(
    @Assisted initialState: WordDetailState,
    private val repository: WordRepository,
    @ConstantsModule.SearchDelay private val searchDelay: Long,
) : WordDetailVM(initialState) {
    @AssistedFactory
    interface Factory : AssistedViewModelFactory<WordDetailVMImpl, WordDetailState>

    init {
        observeCurrentWord()
        observeLayer()
        observeDetails()
        observeSearchKey()
        observeWordHistory()
    }

    private fun observeWordHistory() = viewModelScope.launch {
        repository.wordHistory.collectLatest {
            setState { copy(historyList = if (it.size < 2) listOf() else it.reversed()) }
        }
    }

    private fun observeCurrentWord() = viewModelScope.launch {
        repository.currentWord.collectLatest { word ->
            setState { copy(word = word) }
        }
    }

    private fun observeLayer() = viewModelScope.launch {
        repository.currentLayer.collectLatest { layer ->
            setState { copy(layer = layer ?: 0) }
        }
    }

    private fun observeDetails() = viewModelScope.launch {
        repository.details.collectLatest { details ->
            setState { copy(data = details) }
        }
    }

    private fun observeSearchKey() = onEach(WordDetailState::searchKey) { word ->
        delay(searchDelay)
        val list = repository.searchFor(word)
        setState { copy(searchResults = list) }
    }

    override fun search(word: String) = setState { copy(searchKey = word) }

    override fun pop() = viewModelScope.launch { repository.pop() }
}
