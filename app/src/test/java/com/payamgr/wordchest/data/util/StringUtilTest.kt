package com.payamgr.wordchest.data.util

import androidx.test.filters.SmallTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@SmallTest
class StringUtilTest {
    @Nested
    @DisplayName("Random String Test")
    inner class RandomStringTest {
        @Test
        fun `negative size test`() {
            val size = -1
            val result = StringUtil.randomString(size)
            assertThat(result.length).isEqualTo(0)
        }

        @Test
        fun `zero size test`() {
            val size = 0
            val result = StringUtil.randomString(size)
            assertThat(result.length).isEqualTo(size)
        }

        @Test
        fun `positive size test`() {
            val chars = listOf(' ') + ('a'..'z')
            (1..5).forEach { size ->
                repeat(5) {
                    val result = StringUtil.randomString(size)
                    assertThat(result.length)
                        .`as`("%s", size)
                        .isEqualTo(size)
                    result.forEach {
                        assertThat(chars)
                            .`as`("%s", size)
                            .contains(it)
                    }
                }
            }
        }
    }
}
