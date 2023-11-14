package com.payamgr.wordchest.ui.page.worddetail

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.airbnb.mvrx.mocking.MockableMavericks
import com.payamgr.wordchest.data.model.FakeWord
import com.payamgr.wordchest.data.state.WordDetailState
import com.payamgr.wordchest.util.FakeWordDetailViewModel
import com.payamgr.wordchest.util.app
import org.junit.Rule
import org.junit.Test

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
        rule.setContent { WordDetail.Part(part) }
        rule.onNodeWithText("A small part or quantity intended to show what the whole is like: investigations involved analyzing samples of handwriting.")
            .assertIsDisplayed()
    }

    @Test
    fun samplePart_Test() {
        rule.setContent { WordDetail.SamplePart(part) }
        rule.onNodeWithText("●")
            .assertIsDisplayed()
        rule.onNodeWithText("A small part or quantity intended to show what the whole is like: investigations involved analyzing samples of handwriting.")
            .assertIsDisplayed()
    }

    @Test
    fun definition_Test() {
        rule.setContent { WordDetail.Definition(definition, "3") }

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
        rule.setContent { WordDetail.Section(section) }

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
        rule.setContent { WordDetail.WordData(data, "10") }

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
    fun page_emptyState_check() {
        MockableMavericks.initialize(app)
        rule.setContent { WordDetail.Page(FakeWordDetailViewModel()) }

        rule.onNodeWithTag("word data list")
            .onChildren()
            .assertCountEquals(0)
    }

    @Test
    fun page_initialState_check() {
        MockableMavericks.initialize(app)
        rule.setContent { WordDetail.Page(FakeWordDetailViewModel(WordDetailState(data = dataList))) }

        rule.onNodeWithTag("word data list")
            .onChildren()
            .assertCountEquals(3).apply {
                onFirst().assert(hasTestTag("word data"))
                repeat(2) {
                    this[1 + it].assert(hasTestTag("word section"))
                }
            }
    }
}
