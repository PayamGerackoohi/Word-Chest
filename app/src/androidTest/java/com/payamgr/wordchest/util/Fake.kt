package com.payamgr.wordchest.util

import androidx.compose.runtime.Composable
import com.payamgr.wordchest.data.WordRepository
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.state.HomeState
import com.payamgr.wordchest.data.state.WordDetailState
import com.payamgr.wordchest.ui.ViewModelBuilder
import com.payamgr.wordchest.ui.page.home.HomeViewModel
import com.payamgr.wordchest.ui.page.worddetail.WordDetailViewModel
import kotlinx.coroutines.flow.MutableStateFlow

open class FakeRepository : WordRepository {
    override val wordHistory = MutableStateFlow<List<String>>(listOf())
    override suspend fun wordOf(layer: Int): String = ""
    override suspend fun detailsAt(layer: Int): List<Word.Data> = listOf()
    override suspend fun push(layer: Int, word: String) {}
    override suspend fun searchFor(word: String): List<Word> = if (word.isBlank()) listOf()
    else List(3) { Word("Word $it", "Description $it") }
}

open class FakeViewModelBuilder : ViewModelBuilder() {
    override fun home(): @Composable () -> HomeViewModel = { FakeHomeViewModel() }
    override fun wordDetail(): @Composable () -> WordDetailViewModel = { FakeWordDetailViewModel() }
}

open class FakeHomeViewModel(initialState: HomeState = HomeState()) : HomeViewModel(initialState) {
    override fun onSearchChanged(search: String) = setState {
        copy(
            search = search,
            words = if (search.isBlank())
                listOf()
            else List(3) {
                val n = it + 1
                Word("$search-$n", search.repeat(n))
            }
        )
    }
}

open class FakeWordDetailViewModel(initialState: WordDetailState = WordDetailState()) :
    WordDetailViewModel(initialState) {
    override fun set(layer: Int) {}
}
