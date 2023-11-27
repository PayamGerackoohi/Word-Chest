package com.payamgr.wordchest.ui.modules

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.payamgr.wordchest.util.assertHasRole
import io.mockk.confirmVerified
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class HistoryListTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun listTest() {
        val list = listOf("aaa", "bbb", "ccc")
        var expand by mutableStateOf(false)
        var currentLayer by mutableStateOf(0)
        val onItemClicked = mockk<(Int) -> Unit>()
        justRun { onItemClicked(any()) }
        rule.setContent {
            History.List(expand = expand, list = list, currentLayer = currentLayer, onItemClicked = onItemClicked)
        }
        // verify initial state
        rule.onNodeWithTag("history list")
            .assertExists()
            .assertIsNotDisplayed()
            .onChildren()
            .assertCountEquals(0)

        // verify expansion state
        expand = true

        rule.onNodeWithTag("history list")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(3)

        rule.onNodeWithText("aaa")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Next: aaa")
            .performClick()
        verify { onItemClicked(2) }

        rule.onNodeWithText("bbb")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Next: bbb")
            .performClick()
        verify { onItemClicked(1) }

        rule.onNodeWithText("ccc")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Current: ccc")
            .performClick()
        verify { onItemClicked(0) }

        // verify layer 1 state
        currentLayer = 1
        rule.onNodeWithText("aaa")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Next: aaa")

        rule.onNodeWithText("bbb")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Current: bbb")

        rule.onNodeWithText("ccc")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Previous: ccc")

        // verify layer 2 state
        currentLayer = 2
        rule.onNodeWithText("aaa")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Current: aaa")

        rule.onNodeWithText("bbb")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Previous: bbb")

        rule.onNodeWithText("ccc")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Previous: ccc")

        confirmVerified(onItemClicked)
    }

    @Test
    fun item_currentState_Test() {
        val onClick = mockk<() -> Unit>()
        justRun { onClick() }
        rule.setContent {
            History.Item(data = "aaa", state = History.ItemState.Current, onClick = onClick)
        }

        rule.onNodeWithText("aaa")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Current: aaa")
            .assertHasRole(Role.Button)
            .performClick()

        verify { onClick() }
        confirmVerified(onClick)
    }

    @Test
    fun item_previousState_Test() {
        val onClick = mockk<() -> Unit>()
        justRun { onClick() }
        rule.setContent {
            History.Item(data = "aaa", state = History.ItemState.Previous, onClick = onClick)

        }

        rule.onNodeWithText("aaa")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Previous: aaa")
            .assertHasRole(Role.Button)
            .performClick()

        verify { onClick() }
        confirmVerified(onClick)
    }

    @Test
    fun item_nextState_Test() {
        val onClick = mockk<() -> Unit>()
        justRun { onClick() }
        rule.setContent {
            History.Item(data = "aaa", state = History.ItemState.Next, onClick = onClick)

        }

        rule.onNodeWithText("aaa")
            .assertIsDisplayed()
            .assertContentDescriptionEquals("Next: aaa")
            .assertHasRole(Role.Button)
            .performClick()

        verify { onClick() }
        confirmVerified(onClick)
    }
}
