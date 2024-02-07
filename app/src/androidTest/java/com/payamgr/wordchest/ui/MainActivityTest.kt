package com.payamgr.wordchest.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.click
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.test.espresso.Espresso
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test

@LargeTest
class MainActivityTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun home_initialState_check() {
        // For for the splashscreen animation to end
        rule.waitUntil {
            rule.onAllNodesWithText("Word Search").fetchSemanticsNodes().isNotEmpty()
        }

        rule.onNodeWithText("Word Search").assertIsDisplayed()
    }

    @Test
    fun navigation_homeToDetails_check() {
        rule.apply {
            // For for the splashscreen animation to end
            waitUntil {
                onAllNodesWithText("Word Search").fetchSemanticsNodes().isNotEmpty()
            }

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
    fun navigationAndPop_WordDetailsToWordDetails_Test() {
        // For for the splashscreen animation to end
        rule.waitUntil {
            rule.onAllNodesWithTag("word input").fetchSemanticsNodes().isNotEmpty()
        }

        // open details page
        rule.onNodeWithTag("word input").performTextInput("aaa")

        // wait until the viewModel loads the list
        rule.waitUntil {
            rule.onNodeWithTag("word list")
                .onChildren()
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // click the first result
        rule.onNodeWithTag("word list")
            .onChildren()
            .assertCountEquals(5)[1]
            .performClick()

        // wait until the data is loaded
        rule.waitUntil {
            rule.onNodeWithTag("word data list").onChildren().fetchSemanticsNodes().isNotEmpty()
        }

        // verify word title
        rule.onNodeWithTag("word title")
            .assertTextEquals("aaa")

        // open search section
        rule.onAllNodesWithTag("section value").onFirst().performTouchInput { click(Offset.Zero) }

        // search for 'bbb'
        rule.onNodeWithTag("word input")
            .apply {
                performTextClearance()
                performTextInput("bbb")
            }
        Espresso.closeSoftKeyboard()

        // expand the search section
        rule.onNodeWithTag("search section")
            .performTouchInput { swipeUp() }

        // wait until the search list appear
        rule.waitUntil {
            rule.onAllNodesWithTag("search section item").fetchSemanticsNodes().isNotEmpty()
        }

        // verify result is not empty
        rule.onAllNodesWithTag("search section item")[1]
            .assertIsDisplayed()
            // click on it
            .performClick()

        // verify details for 'bbb' is shown
        rule.onNodeWithTag("word title")
            .assertTextEquals("bbb")

        // open search section
        rule.onAllNodesWithTag("section value").onFirst().performTouchInput { click(Offset.Zero) }

        // search for 'ccc'
        rule.onNodeWithTag("word input")
            .apply {
                performTextClearance()
                performTextInput("ccc")
            }
        Espresso.closeSoftKeyboard()

        // expand the search section
        rule.onNodeWithTag("search section")
            .performTouchInput { swipeUp() }

        // wait until the search list appear
        rule.waitUntil {
            rule.onAllNodesWithTag("search section item").fetchSemanticsNodes().isNotEmpty()
        }

        // verify result is not empty
        rule.onAllNodesWithTag("search section item")[1]
            .assertIsDisplayed()
            // click on it
            .performClick()

        // verify details for 'ccc' is shown
        rule.onNodeWithTag("word title")
            .assertTextEquals("ccc")

        // verify that back press leads to 'bbb'
        Espresso.pressBack()
        rule.onNodeWithTag("word title")
            .assertTextEquals("bbb")

        // verify that back press leads to 'aaa'
        Espresso.pressBack()
        rule.onNodeWithTag("word title")
            .assertTextEquals("aaa")

        // verify that back press leads to home
        rule.onAllNodesWithText("Word Search").assertCountEquals(0)
        Espresso.pressBack()
        rule.onNodeWithText("Word Search").assertIsDisplayed()
    }

    @Test
    fun home_clickOnSelectLanguage_showsError() {
        // For for the splashscreen animation to end
        rule.waitUntil {
            rule.onAllNodesWithContentDescription("Select Language").fetchSemanticsNodes()
                .isNotEmpty()
        }

        rule.onNodeWithContentDescription("Select Language")
            .assertIsDisplayed()
            .performClick()
        rule.onNodeWithText("Not implemented yet!")
            .assertIsDisplayed()
    }
}
