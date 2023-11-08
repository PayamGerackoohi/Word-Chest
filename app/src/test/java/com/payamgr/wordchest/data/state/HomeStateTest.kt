package com.payamgr.wordchest.data.state

import androidx.test.filters.SmallTest
import com.payamgr.wordchest.data.model.Word
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

@SmallTest
class HomeStateTest {
    @Test
    fun `empty constructor test`() {
        val state = HomeState()
        assertThat(state.search).isEqualTo("")
        assertThat(state.words).isEmpty()
    }

    @Test
    fun `constructor with initial data test`() {
        val state = HomeState("Payam", listOf(Word("P", "G")))
        assertThat(state.search).isEqualTo("Payam")
        assertThat(state.words).isEqualTo(listOf(Word("P", "G")))
    }
}
