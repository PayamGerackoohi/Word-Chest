package com.payamgr.wordchest.ui.modules

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class WordChestTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun test() {
        val inputTag = "word input"
        var key by mutableStateOf("aaa")

        rule.setContent { WordChest.WordInput(search = key, onSearchChanged = { key = it }) }

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
}
