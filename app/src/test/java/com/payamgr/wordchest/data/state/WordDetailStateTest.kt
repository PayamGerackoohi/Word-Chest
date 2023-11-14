package com.payamgr.wordchest.data.state

import androidx.test.filters.SmallTest
import com.payamgr.wordchest.data.model.FakeWord
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
                assertThat(layer).isEqualTo(null)
                assertThat(data).isEmpty()
            }
        }

        @Test
        fun layer() {
            WordDetailState(1).apply {
                assertThat(layer).isEqualTo(1)
                assertThat(data).isEmpty()
            }
        }

        @Test
        fun `data`() {
            WordDetailState(data = data).apply {
                assertThat(layer).isEqualTo(null)
                assertThat(data).isEqualTo(data)
            }
        }

        @Test
        fun `layer + data`() {
            WordDetailState(1, data).apply {
                assertThat(layer).isEqualTo(1)
                assertThat(data).isEqualTo(data)
            }
        }
    }
}
