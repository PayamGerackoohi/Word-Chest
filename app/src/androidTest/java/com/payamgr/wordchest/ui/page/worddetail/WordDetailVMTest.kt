package com.payamgr.wordchest.ui.page.worddetail

import com.airbnb.mvrx.mocking.MockableMavericks
import com.airbnb.mvrx.test.MavericksTestRule
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.state.WordDetailState
import com.payamgr.wordchest.util.FakeRepository
import com.payamgr.wordchest.util.app
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WordDetailVMTest {
    @get:Rule
    val mavericksTestRule = MavericksTestRule()

    @Test
    fun initialState_Check() = runTest {
        MockableMavericks.initialize(app)
        val viewModel = WordDetailVMImpl(
            initialState = WordDetailState(),
            repository = object : FakeRepository() {
                override val wordHistory = MutableStateFlow(listOf("aaa", "bbb"))
                override val currentLayer = MutableStateFlow(10)
                override val currentWord = MutableStateFlow("ccc")
                override val details = MutableStateFlow(listOf(Word.Data("ddd", "eee")))
            },
            searchDelay = 0,
        )
        viewModel.awaitState().let { state ->
            assertThat(state.word).isEqualTo("ccc")
            assertThat(state.layer).isEqualTo(10)
            state.data.let { data ->
                assertThat(data).hasSize(1)
                data.first().let { first ->
                    assertThat(first.word).isEqualTo("ddd")
                    assertThat(first.pronunciation).isEqualTo("eee")
                }
            }
            assertThat(state.historyList).isEqualTo(listOf("bbb", "aaa"))
            assertThat(state.searchKey).isEmpty()
            assertThat(state.searchResults).isEmpty()
        }
    }

    @Test
    fun search_Test() = runTest {
        MockableMavericks.initialize(app)
        val viewModel = WordDetailVMImpl(
            initialState = WordDetailState(),
            repository = FakeRepository(),
            searchDelay = 0,
        )
        // verify initial state
        assertThat(viewModel.awaitState()).isEqualTo(WordDetailState())

        // search 'aaa'
        viewModel.search("aaa")

        // verify the search result
        viewModel.awaitState().let { state ->
            assertThat(state.searchKey).isEqualTo("aaa")
            assertThat(state.searchResults).hasSize(3)
            assertThat(state.searchResults.first()).isEqualTo(Word("aaa 0", "aaa - description 0"))
        }
    }

    @Test
    fun historyList_Test() = runTest {
        MockableMavericks.initialize(app)
        val history = MutableStateFlow<List<String>>(listOf())
        val viewModel = WordDetailVMImpl(
            initialState = WordDetailState(),
            repository = object : FakeRepository() {
                override val wordHistory = history
            },
            searchDelay = 0,
        )
        // verify initial state
        assertThat(viewModel.awaitState().historyList).isEmpty()

        // add 'aaa' to the history
        history.value = listOf("aaa")

        // verify the list is still empty
        assertThat(viewModel.awaitState().historyList).isEmpty()

        // add 'bbb' to the history
        history.value += listOf("bbb")

        // verify the list content
        viewModel.awaitState().historyList.let { list ->
            assertThat(list).hasSize(2)
            assertThat(list[0]).isEqualTo("bbb")
            assertThat(list[1]).isEqualTo("aaa")
        }
    }
}
