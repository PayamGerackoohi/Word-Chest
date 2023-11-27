package com.payamgr.wordchest.util

import androidx.compose.runtime.Composable
import com.payamgr.wordchest.data.WordRepository
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.model.Word.Data.Section
import com.payamgr.wordchest.data.model.Word.Data.Section.Type
import com.payamgr.wordchest.data.model.Word.Data.Section.Definition
import com.payamgr.wordchest.data.model.Word.Data.Section.Definition.Part
import com.payamgr.wordchest.data.model.Word.Data.Section.Definition.Part.Sub
import com.payamgr.wordchest.data.model.WordKey
import com.payamgr.wordchest.data.state.HomeState
import com.payamgr.wordchest.data.state.WordDetailState
import com.payamgr.wordchest.ui.ViewModelBuilder
import com.payamgr.wordchest.ui.page.home.HomeVM
import com.payamgr.wordchest.ui.page.worddetail.WordDetailVM
import kotlinx.coroutines.flow.MutableStateFlow

open class FakeRepository : WordRepository {
    override val wordHistory = MutableStateFlow<List<String>>(listOf())
    override val currentLayer = MutableStateFlow(0)
    override val currentWord = MutableStateFlow("")
    override val details = MutableStateFlow<List<Word.Data>>(listOf())
    override suspend fun searchFor(word: String): List<Word> = if (word.isBlank()) listOf()
    else List(3) { Word("$word $it", "$word - description $it") }

    override suspend fun push(key: WordKey) {}
    override suspend fun pop(): Boolean = false
}

class FakeStates {
    val home by lazy { HomeState() }
    val wordDetail by lazy { WordDetailState() }
}

class FakeViewModels(states: FakeStates) {
    val home by lazy { FakeHomeViewModel(states.home) }
    val wordDetail by lazy { FakeWordDetailViewModel(states.wordDetail) }
}

open class FakeViewModelBuilder : ViewModelBuilder() {
    private val states = FakeStates()
    private val viewModels = FakeViewModels(states)

    override fun home(): @Composable () -> HomeVM = { viewModels.home }
    override fun wordDetail(): @Composable () -> WordDetailVM = { viewModels.wordDetail }
}

open class FakeHomeViewModel(initialState: HomeState = HomeState()) : HomeVM(initialState) {
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
    WordDetailVM(initialState) {
    override fun search(word: String) = setState {
        copy(
            searchKey = word,
            searchResults = (1..3).map { Word("$word - $it", "") },
        )
    }

    override fun pop() {}
}

object FakeWordData {
    private val part get() = Part(Sub.Normal("ccc ddd"))
    private val definition get() = Definition(part)
    private val section get() = Section(Type.Noun, definition)
    private val data get() = Word.Data("aaa", "bbb", section)
    val dataList get() = listOf(data)
}
