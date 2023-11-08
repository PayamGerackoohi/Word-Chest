package com.payamgr.wordchest.data.state

import androidx.test.filters.SmallTest
import com.payamgr.wordchest.data.model.Word
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@SmallTest
class HomeStateTest {
    @Nested
    inner class Constructors {
        @Test
        fun `empty test`() {
            val state = HomeState()
            assertThat(state.search).isEqualTo("")
            assertThat(state.words).isEmpty()
        }

        @Test
        fun `with search test`() {
            val state = HomeState("Payam")
            assertThat(state.search).isEqualTo("Payam")
            assertThat(state.words).isEmpty()
        }

        @Test
        fun `with words test`() {
            val state = HomeState(words = listOf(Word("P", "G")))
            assertThat(state.search).isEqualTo("")
            assertThat(state.words).isEqualTo(listOf(Word("P", "G")))
        }

        @Test
        fun `full test`() {
            val state = HomeState("Payam", listOf(Word("P", "G")))
            assertThat(state.search).isEqualTo("Payam")
            assertThat(state.words).isEqualTo(listOf(Word("P", "G")))
        }
    }
}
