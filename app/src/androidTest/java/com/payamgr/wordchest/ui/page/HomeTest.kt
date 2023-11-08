package com.payamgr.wordchest.ui.page

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.filters.MediumTest
import com.airbnb.mvrx.mocking.MockableMavericks
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.state.HomeState
import com.payamgr.wordchest.util.app
import com.payamgr.wordchest.util.assertHasRole
import org.junit.Rule
import org.junit.Test

@MediumTest
class HomeTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun appHeaderTest() {
        rule.setContent { Home.AppHeader() }
        rule.onNodeWithText("Word Search").assertIsDisplayed()
        rule.onNodeWithContentDescription("Select Language")
            .assertIsDisplayed()
            .assertHasRole(Role.Button)
    }

    @Test
    fun wordInputTest() {
        val inputTag = "word input"
        var key by mutableStateOf("aaa")

        rule.setContent { Home.WordInput(search = key, onSearchChanged = { key = it }) }

        // Check if 'aaa' is displayed
        rule.onNodeWithTag(inputTag)
            .assertTextEquals("aaa")
            .assertIsDisplayed()
            // Clear the text
            .performTextClearance()

        // Check if the text is cleared
        rule.onNodeWithTag(inputTag)
            .assertTextEquals("")

        // Insert 'bbb'
        rule.onNodeWithTag(inputTag)
            .performTextInput("bbb")

        // Check if 'bbb' is displayed
        rule.onNodeWithText("bbb")
            .assertIsDisplayed()
    }

    @Test
    fun wordCardTest() {
        val word = Word("Payam", "Gr")
        rule.setContent { Home.WordCard(word) }
        rule.onNodeWithContentDescription("Payam: Gr").assertIsDisplayed()
    }

    @Test
    fun wordListTest() {
        val list = listOf(
            Word("a", "aaa"),
            Word("b", "bbb"),
        )

        rule.setContent { Home.WordList(list) }

        list.map { it.run { "$value: $shortDescription" } }.forEach {
            rule.onNodeWithContentDescription(it).assertIsDisplayed()
        }
    }

    @Test
    fun pageTest() {
        MockableMavericks.initialize(app)
        val viewModel = makeFakeHomeViewModel()
        rule.setContent { Home.Page(viewModel) }

        // I) Initial state check
        // Check if the appBar is displayed
        rule.onNodeWithText("Word Search").assertIsDisplayed()
        rule.onNodeWithContentDescription("Select Language").assertIsDisplayed()

        // Check if the input is empty
        rule.onNodeWithTag("word input").assertTextEquals("")

        // Check if the list is empty
        rule.onNodeWithTag("word list").onChildren().assertCountEquals(0)

        // II) New state check
        // Insert 'abc'
        rule.onNodeWithTag("word input").performTextInput("abc")

        // Check if 'abc' is set in the input
        rule.onNodeWithTag("word input").assertTextEquals("abc")

        // check the content of the list
        rule.onNodeWithTag("word list")
            .onChildren()
            .assertCountEquals(3).apply {
                get(0).assertContentDescriptionEquals("abc-1: abc")
                get(1).assertContentDescriptionEquals("abc-2: abcabc")
                get(2).assertContentDescriptionEquals("abc-3: abcabcabc")
            }
    }

    private fun makeFakeHomeViewModel() = object : HomeViewModel(HomeState()) {
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
}
