package com.payamgr.wordchest.data.state

import androidx.test.filters.SmallTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@SmallTest
class HomeStateTest {
    @Nested
    inner class Greet {
        @Test
        fun `default home state`() {
            val state = HomeState()
            assertThat(state.user).isEqualTo("")
            assertThat(state.greet()).isEqualTo("Hello!")
        }

        @Test
        fun `home state with blank user`() {
            val state = HomeState("  ")
            assertThat(state.user).isEqualTo("  ")
            assertThat(state.greet()).isEqualTo("Hello!")
        }

        @Test
        fun `home state with user`() {
            val state = HomeState("Payam")
            assertThat(state.user).isEqualTo("Payam")
            assertThat(state.greet()).isEqualTo("Hello Payam!")
        }
    }
}
