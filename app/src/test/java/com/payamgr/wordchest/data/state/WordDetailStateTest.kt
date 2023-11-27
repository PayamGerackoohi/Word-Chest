package com.payamgr.wordchest.data.state

import androidx.test.filters.SmallTest
import com.airbnb.mvrx.PersistState
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.model.fake.FakeWord
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@SmallTest
class WordDetailStateTest {
    private val data = FakeWord().dummyListOf("aaa")

    @Nested
    inner class Constructors {
        @Test
        fun empty() {
            WordDetailState().apply {
                assertThat(word).isEqualTo("")
                assertThat(layer).isEqualTo(0)
                assertThat(data).isEmpty()
                assertThat(historyList).isEmpty()
                assertThat(searchKey).isEqualTo("")
                assertThat(searchResults).isEmpty()
                assertThat(canPop).isFalse
            }
        }

        @Test
        fun word() {
            WordDetailState(word = "aaa").apply {
                assertThat(word).isEqualTo("aaa")
                assertThat(layer).isEqualTo(0)
                assertThat(data).isEmpty()
                assertThat(historyList).isEmpty()
                assertThat(searchKey).isEqualTo("")
                assertThat(searchResults).isEmpty()
                assertThat(canPop).isFalse
            }
        }

        @Test
        fun layer() {
            WordDetailState(layer = 1).apply {
                assertThat(word).isEqualTo("")
                assertThat(layer).isEqualTo(1)
                assertThat(data).isEmpty()
                assertThat(historyList).isEmpty()
                assertThat(searchKey).isEqualTo("")
                assertThat(searchResults).isEmpty()
                assertThat(canPop).isTrue
            }
        }

        @Test
        fun data() {
            WordDetailState(data = data).apply {
                assertThat(word).isEqualTo("")
                assertThat(layer).isEqualTo(0)
                assertThat(data).isEqualTo(data)
                assertThat(historyList).isEmpty()
                assertThat(searchKey).isEqualTo("")
                assertThat(searchResults).isEmpty()
                assertThat(canPop).isFalse
            }
        }

        @Test
        fun `history list`() {
            WordDetailState(historyList = listOf("aaa")).apply {
                assertThat(word).isEqualTo("")
                assertThat(layer).isEqualTo(0)
                assertThat(data).isEmpty()
                assertThat(historyList).isEqualTo(listOf("aaa"))
                assertThat(searchKey).isEqualTo("")
                assertThat(searchResults).isEmpty()
                assertThat(canPop).isFalse
            }
        }

        @Test
        fun `search key`() {
            WordDetailState(searchKey = "aaa").apply {
                assertThat(word).isEqualTo("")
                assertThat(layer).isEqualTo(0)
                assertThat(data).isEmpty()
                assertThat(historyList).isEmpty()
                assertThat(searchKey).isEqualTo("aaa")
                assertThat(searchResults).isEmpty()
                assertThat(canPop).isFalse
            }
        }

        @Test
        fun `search results`() {
            WordDetailState(searchResults = listOf(Word("aaa", "bbb"))).apply {
                assertThat(word).isEqualTo("")
                assertThat(layer).isEqualTo(0)
                assertThat(data).isEmpty()
                assertThat(historyList).isEmpty()
                assertThat(searchKey).isEqualTo("")
                assertThat(searchResults).isEqualTo(listOf(Word("aaa", "bbb")))
                assertThat(canPop).isFalse
            }
        }

        @Test
        fun `layer + data`() {
            WordDetailState(layer = 1, data = data).apply {
                assertThat(word).isEqualTo("")
                assertThat(layer).isEqualTo(1)
                assertThat(data).isEqualTo(data)
                assertThat(historyList).isEmpty()
                assertThat(searchKey).isEqualTo("")
                assertThat(searchResults).isEmpty()
                assertThat(canPop).isTrue
            }
        }
    }
}
