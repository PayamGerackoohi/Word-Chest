package com.payamgr.wordchest.ui.page.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.filters.MediumTest
import com.airbnb.mvrx.mocking.MockableMavericks
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.ui.modules.ActivityTest
import com.payamgr.wordchest.ui.theme.WordChestTheme
import com.payamgr.wordchest.util.FakeHomeViewModel
import com.payamgr.wordchest.util.Screenshot
import com.payamgr.wordchest.util.app
import com.payamgr.wordchest.util.assertHasRole
import com.payamgr.wordchest.util.take
import io.mockk.confirmVerified
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

@MediumTest
class HomeTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun appHeaderTest() {
        val onBuzzError = mockk<(String) -> Unit>()
        justRun { onBuzzError(any()) }

        rule.setContent { Home.AppHeader(onBuzzError) }
        rule.onNodeWithText("Word Search").assertIsDisplayed()
        rule.onNodeWithContentDescription("Select Language")
            .assertIsDisplayed()
            .assertHasRole(Role.Button)
            .performClick()

        verify { onBuzzError(any()) }
        confirmVerified(onBuzzError)
    }

    @Test
    fun wordInputTest() {
        val inputTag = "word input"
        var key by mutableStateOf("aaa")

        rule.setContent {
            Home.WordInput(search = key, onSearchChanged = { key = it })
        }

        // Check if 'aaa' is displayed
        rule.onNodeWithTag(inputTag)
            .assertIsDisplayed()
            .assertTextEquals("aaa")
            // Clear the text
            .performTextClearance()

        // Check if the text is cleared
        rule.onNodeWithTag(inputTag).assertTextEquals("")

        // Insert 'bbb'
        rule.onNodeWithTag(inputTag).performTextInput("bbb")

        // Check if 'bbb' is displayed
        rule.onNodeWithText("bbb").assertIsDisplayed()
    }

    @Test
    fun wordCardTest() {
        val word = Word("Payam", "Gr")
        val onItemClick = mockk<() -> Unit>()
        justRun { onItemClick() }

        rule.setContent { Home.WordCard(word, onItemClick) }
        rule.onNodeWithContentDescription("Payam: Gr")
            .assertIsDisplayed()
            .performClick()

        verify { onItemClick() }
        confirmVerified(onItemClick)
    }

    @Test
    fun wordListTest() {
        val onItemClick = mockk<(String) -> Unit>()
        justRun { onItemClick(any()) }

        val list = listOf(
            Word("a", "aaa"),
            Word("b", "bbb"),
        )

        rule.setContent { Home.WordList(list, onItemClick) }

        list.forEach { (value, shortDescription) ->
            rule.onNodeWithContentDescription("$value: $shortDescription")
                .assertIsDisplayed()
                .performClick()
            verify { onItemClick(value) }
        }

        confirmVerified(onItemClick)
    }
}

@MediumTest
class HomeActivityTest : ActivityTest() {
    @Test
    fun pageTest() {
        val onItemClick = mockk<(String) -> Unit>()
        justRun { onItemClick(any()) }
        MockableMavericks.initialize(app)
        val viewModel = FakeHomeViewModel()

        rule.setContent {
            WordChestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home.Page(viewModel = viewModel, onItemClick = onItemClick)
                }
            }
        }

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
                get(0).assertContentDescriptionEquals("abc-1: abc").performClick()
                verify { onItemClick("abc-1") }

                get(1).assertContentDescriptionEquals("abc-2: abcabc").performClick()
                verify { onItemClick("abc-2") }

                get(2).assertContentDescriptionEquals("abc-3: abcabcabc").performClick()
                verify { onItemClick("abc-3") }
            }

        confirmVerified(onItemClick)

        Espresso.closeSoftKeyboard()
        Screenshot.Home.take()
    }
}
