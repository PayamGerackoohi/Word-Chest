package com.payamgr.wordchest.ui.page.worddetail

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.payamgr.wordchest.data.WordRepository
import com.payamgr.wordchest.data.state.WordDetailState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class WordDetailViewModelImpl @AssistedInject constructor(
    @Assisted initialState: WordDetailState,
    private val repository: WordRepository,
) : WordDetailViewModel(initialState) {
    @AssistedFactory
    interface Factory : AssistedViewModelFactory<WordDetailViewModelImpl, WordDetailState>

    init {
        onEach(WordDetailState::layer) { layer ->
            if (layer != null) {
                val data = repository.detailsAt(layer)
//                val word = repository.wordOf(layer)
                setState { copy(data = data) }
//                setState { copy(data = data, word = word) }
            }
        }
    }

    override fun set(layer: Int) = setState { copy(layer = layer) }
}
