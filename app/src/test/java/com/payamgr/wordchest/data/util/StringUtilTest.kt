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
            val su = StringUtil
            val size = -1
            assertThat(su.randomString(size).length).isEqualTo(0)
        }

        @Test
        fun `0 size test`() {
            val size = 0
            val result = StringUtil.randomString(size)
            assertThat(result.length).isEqualTo(size)
        }

        @Test
        fun `positive size test`() {
            val chars = ('a'..'z').toList()
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

        @Test
        fun `extraChars test`() {
            val size = 5
            val chars = listOf(' ', 'A', 'B', '.', ',') + ('a'..'z')
            repeat(10) {
                val string = StringUtil.randomString(size, ' ', 'A', 'B', '.', ',')
                assertThat(string.length).isEqualTo(size)
                string.forEach { assertThat(chars).contains(it) }
            }
        }

        @Test
        fun `capitalize test`() {
            val size = 5
            repeat(10) {
                val string = StringUtil.randomString(size, capitalize = true)
                assertThat(string.first()).isUpperCase
            }
        }

        @Test
        fun `not capitalize test`() {
            val size = 5
            repeat(10) {
                val string = StringUtil.randomString(size)
                assertThat(string.first()).isLowerCase()
            }
        }

        @Test
        fun `extraChars + capitalize test`() {
            val size = 5
            val chars = listOf(' ', 'A', 'B', '.', ',') + ('a'..'z')
            repeat(10) {
                val string = StringUtil.randomString(size, ' ', 'A', 'B', '.', ',', capitalize = true)
                assertThat(string.length).isEqualTo(size)
                string.forEach { assertThat(chars).contains(it.lowercaseChar()) }
                if (string.first().isLetter())
                    assertThat(string.first()).isUpperCase
            }
        }
    }

    @Nested
    @DisplayName("Random Sentence Test")
    inner class RandomSentenceTest {
        @Test
        fun `negative count test`() {
            assertThat(StringUtil.randomSentence(-1)).isEqualTo("")
        }

        @Test
        fun `0 count test`() {
            assertThat(StringUtil.randomSentence(0)).isEqualTo("")
        }

        @Test
        fun `positive count test`() {
            val count = 5
            val chars = ('a'..'z').toList()
            repeat(10) {
                val sentence = StringUtil.randomSentence(count)
                // assert first word is capitalized
                assertThat(sentence.first()).isUpperCase
                assertThat(sentence.drop(1)).isLowerCase

                // assert that sentence ends with '.'
                assertThat(sentence.last()).isEqualTo('.')

                // assert word counts
                val words = sentence.dropLast(1).split(' ')
                assertThat(words.size).isEqualTo(count)

                // check words validity
                var isFirstChar = true
                words.forEach { word ->
                    assertThat(word.length).isBetween(1, 9)
                    word.forEach { char ->
                        if (isFirstChar) {
                            isFirstChar = false
                            assertThat(chars).contains(char.lowercaseChar())
                        } else
                            assertThat(chars).contains(char)
                    }
                }
            }
        }
    }
}
