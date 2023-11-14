package com.payamgr.wordchest.ui

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.filters.LargeTest
import com.payamgr.wordchest.R
import org.junit.Rule
import org.junit.Test

@LargeTest
class MainActivityTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun home_initialState_check() {
        rule.onNodeWithText("Word Search").assertIsDisplayed()
    }

    @Test
    fun navigation_homeToDetails_check() {
        rule.apply {
            // check initial state
            onNodeWithText("Word Search")
                .assertExists()
                .assertIsDisplayed()
            onNodeWithTag("word input")
                .assertTextEquals("")
                // insert 'aaa' into the 'word input'
                .performTextInput("aaa")

            // check the 'word input' has 'aaa'
            onNodeWithTag("word input").assertTextEquals("aaa")

            // wait until the viewModel loads the list
            waitUntil {
                onNodeWithTag("word list")
                    .onChildren()
                    .fetchSemanticsNodes()
                    .isNotEmpty()
            }

            // check if the 'word list' has items
            onNodeWithTag("word list")
                .assertIsDisplayed()
                .onChildren()
                .assertCountEquals(5)
                // click the first item
                .onFirst()
                .performClick()

            // check if the 'Word Details' page is opened
            onNodeWithText("Word Search").assertDoesNotExist()
            onNodeWithTag("word data list")
                .assertExists()
                .assertIsDisplayed()
                .onChildren()
                .assertCountEquals(3).apply {
                    onFirst().assert(hasTestTag("word data"))
                    repeat(2) {
                        this[1 + it].assert(hasTestTag("word section"))
                    }
                }

            // check if back press should returns to home
            Espresso.pressBack()
            onNodeWithTag("word data list").assertDoesNotExist()
            onNodeWithText("Word Search")
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun home_clickOnSelectLanguage_showsError() {
        rule.onNodeWithContentDescription("Select Language")
            .assertIsDisplayed()
            .performClick()
        rule.onNodeWithText("Not implemented yet!")
            .assertIsDisplayed()
    }
}
