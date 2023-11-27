package com.payamgr.wordchest.ui.page.worddetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.click
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.test.filters.MediumTest
import com.airbnb.mvrx.mocking.MockableMavericks
import com.payamgr.wordchest.data.model.HistoryActions
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.model.WordKey
import com.payamgr.wordchest.data.model.fake.FakeWord
import com.payamgr.wordchest.data.state.WordDetailState
import com.payamgr.wordchest.util.FakeWordData
import com.payamgr.wordchest.util.FakeWordDetailViewModel
import com.payamgr.wordchest.util.app
import com.payamgr.wordchest.util.assertHasRole
import io.mockk.called
import io.mockk.confirmVerified
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.*
import org.junit.Rule
import org.junit.Test

@MediumTest
class WordDetailTest {
    @get:Rule
    val rule = createComposeRule()

    private val dataList = FakeWord().wordData
    private val data = dataList.first()
    private val section = data.sections.first()
    private val definition = section.definitions.first()
    private val part = definition.value

    @Test
    fun part_Test() {
        val searchFor = mockk<(String) -> Unit>()
        justRun { searchFor(any()) }
        rule.setContent { WordDetail.Part(part, searchFor = searchFor) }
        // verify initial state
        rule.onNodeWithText("A small part or quantity intended to show what the whole is like: investigations involved analyzing samples of handwriting.")
            .assertIsDisplayed()
            // click on 'A'
            .performTouchInput { click(Offset.Zero) }
        // verify 'A' is clicked
        verify { searchFor("A") }
        confirmVerified(searchFor)
    }

    @Test
    fun samplePart_Test() {
        val searchFor = mockk<(String) -> Unit>()
        justRun { searchFor(any()) }
        rule.setContent { WordDetail.SamplePart(part, searchFor = searchFor) }
        rule.onNodeWithText("●")
            .assertIsDisplayed()
        rule.onNodeWithText("A small part or quantity intended to show what the whole is like: investigations involved analyzing samples of handwriting.")
            .assertIsDisplayed()
            // click on '●'
            .performTouchInput { click(Offset.Zero) }
        // verify click event is not fired
        verify { searchFor wasNot called }
        confirmVerified(searchFor)
    }

    @Test
    fun definition_Test() {
        rule.setContent { WordDetail.Definition(definition, "3") {} }

        rule.onNodeWithTag("section index")
            .assertIsDisplayed()
            .assertTextEquals("3")

        rule.onNodeWithTag("section value")
            .assertIsDisplayed()
            .assertTextEquals("A small part or quantity intended to show what the whole is like: investigations involved analyzing samples of handwriting.")

        rule.onNodeWithTag("section samples")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(4).apply {
                repeat(4) {
                    this[it]
                        .assert(hasTestTag("sample part"))
                        .assertTextContains("●")
                }
            }
    }

    @Test
    fun section_Test() {
        rule.setContent { WordDetail.Section(section) {} }

        rule.onNodeWithTag("word type")
            .assertTextEquals("noun")
        rule.onNodeWithTag("word definition")
            .assertIsDisplayed()
    }

    @Test
    fun indexItem_Test() {
        rule.setContent { WordDetail.IndexItem("23") }

        rule.onNodeWithText("23")
            .assertIsDisplayed()
    }

    @Test
    fun wordData_Test() {
        rule.setContent { WordDetail.WordData(data, "10") {} }

        rule.onNodeWithTag("word title")
            .assertTextEquals("sample")
        rule.onNodeWithTag("word index")
            .assertTextEquals("10")
        rule.onNodeWithTag("word pronunciation")
            .assertTextEquals("|ˈsæmpəl|")
        rule.onAllNodesWithTag("word section")
            .assertCountEquals(2)
            .onFirst()
            .assertIsDisplayed()
    }

    @Test
    fun searchSectionHeader_Test() {
        var word by mutableStateOf("aaa")
        val onClose = mockk<() -> Unit>()
        justRun { onClose() }
        rule.setContent {
            WordDetail.SearchSectionHeader(
                word = word,
                onWordChanged = { word = it },
                onFirstItem = {},
                onClose = onClose
            )
        }

        // verify initial state
        rule.onNodeWithTag("word input")
            .assertIsDisplayed()
            .assertTextEquals("aaa")
            // verify insert 'bbb' changes the word
            .apply { performTextClearance() }
            .performTextInput("bbb")
        assertThat(word).isEqualTo("bbb")

        // verify close button functionality
        rule.onNodeWithContentDescription("Close Search Section")
            .assertIsDisplayed()
            .performClick()
        verify { onClose() }

        confirmVerified(onClose)
    }

    @Test
    fun searchSectionItem_Test() {
        val word = "aaa"
        val onItemClick = mockk<(String) -> Unit>()
        justRun { onItemClick(any()) }
        rule.setContent { WordDetail.SearchSectionItem(word = word, onItemClick = onItemClick) }

        rule.onNodeWithContentDescription("aaa")
            .assertIsDisplayed()
            .assertHasRole(Role.Button)
            .performClick()
        verify { onItemClick("aaa") }
        confirmVerified(onItemClick)
    }

    @Test
    fun searchSection_Test() {
        var word by mutableStateOf("aaa")
        var list by mutableStateOf(listOf<Word>())
        val onClose = mockk<() -> Unit>()
        val onItemClick = mockk<(String) -> Unit>()
        justRun { onClose() }
        justRun { onItemClick(any()) }

        rule.setContent {
            WordDetail.SearchSection(
                word = word,
                list = list,
                onWordChanged = { word = it },
                onFirstItem = {},
                onClose = onClose,
                onItemClick = onItemClick
            )
        }

        // verify initial state
        rule.onNodeWithTag("search section")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(2)
            .apply {
                // verify the word input
                onFirst()
                    .assert(hasTestTag("word input"))
                    .assertIsDisplayed()
                    .assertTextEquals("aaa")

                // verify the close button
                this[1].assertContentDescriptionEquals("Close Search Section")
                    .assertIsDisplayed()
                    .performClick()
                verify { onClose() }
            }

        // verify inserting 'bbb' changes word
        rule.onNodeWithTag("word input")
            .apply { performTextClearance() }
            .performTextInput("bbb")
        assertThat(word).isEqualTo("bbb")

        // verify changing list shows the item
        list = listOf(Word("bbb", ""))
        rule.onNodeWithTag("search section")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(3)
            .apply {
                this[2]
                    .assertIsDisplayed()
                    .assertContentDescriptionEquals("bbb")
                    .performClick()
                verify { onItemClick("bbb") }
            }

        confirmVerified(onClose, onItemClick)
    }

    @Test
    fun pageContent_check() {
        val data = FakeWordData.dataList
        var showHistory by mutableStateOf(false)
        var historyList by mutableStateOf(listOf<String>())
        val onHistoryItemClicked = mockk<(Int) -> Unit>()
        val searchFor = mockk<(String) -> Unit>()
        justRun { onHistoryItemClicked(any()) }
        justRun { searchFor(any()) }
        rule.setContent {
            WordDetail.PageContent(
                data = data,
                historyActions = HistoryActions(
                    show = showHistory,
                    toggleShow = { showHistory = !showHistory },
                    list = historyList,
                    onClicked = onHistoryItemClicked,
                ),
                searchFor = searchFor,
                layer = 0,
            )
        }

        // verify initial state
        rule.onNodeWithTag("word data list")
            .onChildren()
            .assertCountEquals(2)
            .onFirst()
            .assert(hasTestTag("word data"))

        rule.onNodeWithTag("history list").assertDoesNotExist()
        rule.onNodeWithContentDescription("Show/Hide History").assertDoesNotExist()

        // verify history list is still not shown, even if has any item
        historyList = listOf("aa", "bb", "cc")
        rule.onNodeWithTag("history list")
            .assertExists()
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(0)

        // verify history is shown if showHistory is also true
        showHistory = true
        rule.onNodeWithTag("history list")
            .assertExists()
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(3)
            .apply {
                // verify history item click functionality
                historyList.forEachIndexed { index, item ->
                    this[index]
                        .assertTextEquals(item)
                        .assertIsDisplayed()
                        .performClick()
                    verify { onHistoryItemClicked(2 - index) }
                }
            }

        // verify toggling show history
        rule.onNodeWithContentDescription("Show/Hide History").performClick()
        rule.onNodeWithTag("history list")
            .assertExists()
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(0)
        assertThat(showHistory).isFalse

        // verify click to search functionality
        rule.onNodeWithText("ccc ddd")
            .assertIsDisplayed()
            .performTouchInput { click(Offset.Zero) }
        verify { searchFor("ccc") }

        confirmVerified(onHistoryItemClicked, searchFor)
    }

    @Test
    fun page_emptyState_check() {
        MockableMavericks.initialize(app)
        rule.setContent { WordDetail.Page({ _ -> }, FakeWordDetailViewModel()) }

        rule.onNodeWithTag("word data list")
            .onChildren()
            .assertCountEquals(0)

        rule.onNodeWithTag("search section")
            .assertExists()
            .assertIsNotDisplayed()
            .onChildren()
            .assertCountEquals(2)
    }

    @Test
    fun page_initialState_check() {
        MockableMavericks.initialize(app)
        rule.setContent {
            WordDetail.Page(
                { _ -> },
                FakeWordDetailViewModel(WordDetailState(data = FakeWordData.dataList))
            )
        }

        rule.onNodeWithTag("word data list")
            .onChildren()
            .assertCountEquals(2)
            .apply {
                onFirst().assert(hasTestTag("word data"))
                this[1].assert(hasTestTag("word section"))
            }
        rule.onNodeWithTag("search section")
            .assertExists()
            .assertIsNotDisplayed()
    }

    @Test
    fun page_searchSection_interaction() {
        MockableMavericks.initialize(app)
        val showWordDetail = mockk<(WordKey) -> Unit>()
        justRun { showWordDetail(any()) }
        rule.setContent {
            WordDetail.Page(
                showWordDetail = showWordDetail,
                viewModel = FakeWordDetailViewModel(WordDetailState(data = FakeWordData.dataList)),
            )
        }

        // verify initial state
        rule.onNodeWithTag("search section")
            .assertExists()
            .assertIsNotDisplayed()

        // click on 'ccc' and verify
        rule.onNodeWithText("ccc ddd")
            .assertIsDisplayed()
            .performTouchInput { click(Offset.Zero) }

        // verify the search section is partially open
        rule.onNodeWithTag("word input")
            .assertIsDisplayed()
            .assertTextEquals("ccc")
        rule.onAllNodesWithTag("search section item")
            .assertCountEquals(3)
            .onFirst()
            .assertContentDescriptionEquals("ccc - 1")
            .assertExists()
            .assertIsNotDisplayed()

        // drag up the search section
        rule.onNodeWithTag("search section")
            .performTouchInput { swipeUp() }
            .assertIsDisplayed()

        // verify the word list is shown
        rule.onAllNodesWithTag("search section item")
            .assertCountEquals(3)
            .onLast()
            .assertContentDescriptionEquals("ccc - 3")
            .assertExists()
            .assertIsDisplayed()

        // click on 'ccc - 2' item
        rule.onNodeWithContentDescription("ccc - 2").performClick()

        // verify the 'showWordDetail' is called
        verify { showWordDetail(WordKey.Word("ccc - 2")) }

        // swipe down the search section
        rule.onNodeWithTag("search section")
            .performTouchInput { swipeDown() }
            .assertIsNotDisplayed()

        // verify the search section is dismissed
        rule.onNodeWithTag("word input").assertIsNotDisplayed()

        confirmVerified(showWordDetail)
    }

    @Test
    fun page_historyList_interaction() {
        MockableMavericks.initialize(app)
        val showWordDetail = mockk<(WordKey) -> Unit>()
        justRun { showWordDetail(any()) }
        rule.setContent {
            WordDetail.Page(
                showWordDetail = showWordDetail,
                FakeWordDetailViewModel(WordDetailState(historyList = listOf("aaa", "bbb", "ccc")))
            )
        }
        // verify initial state
        rule.onNodeWithContentDescription("Show/Hide History")
            .assertIsDisplayed()
        rule.onNodeWithTag("history list")
            .assertExists()
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(0)

        // click on history button
        rule.onNodeWithContentDescription("Show/Hide History")
            .assertIsDisplayed()
            .performClick()

        // verify the list is displayed
        rule.onNodeWithTag("history list")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(3)
            .apply {
                onFirst().assertTextEquals("aaa").assertContentDescriptionEquals("Next: aaa")
                this[1].assertTextEquals("bbb").assertContentDescriptionEquals("Next: bbb")
                onLast().assertTextEquals("ccc").assertContentDescriptionEquals("Current: ccc")
            }

        // click on 'bbb'
        rule.onNodeWithContentDescription("Next: bbb").performClick()

        // verify showWordDetail wants to open layer 1
        verify { showWordDetail(WordKey.Layer(1)) }

        // verify the history list is closed
        rule.onNodeWithTag("history list")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(0)

        // open the history list
        rule.onNodeWithContentDescription("Show/Hide History").performClick()

        // verify the list is displayed
        rule.onNodeWithTag("history list")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(3)
            .onFirst()
            .assertTextEquals("aaa")

        // close the history list
        rule.onNodeWithContentDescription("Show/Hide History").performClick()

        // verify the list is dismissed
        rule.onNodeWithTag("history list")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(0)

        confirmVerified(showWordDetail)
    }
}
