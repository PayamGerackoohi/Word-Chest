package com.payamgr.wordchest.data.util

import androidx.test.filters.SmallTest
import com.payamgr.wordchest.data.model.SentencePart
import com.payamgr.wordchest.data.util.StringUtil.extractWords
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

typealias SP = SentencePart

@SmallTest
class StringUtilTest {
    @Nested
    @DisplayName("Random String Test")
    inner class RandomStringTest {
        @Test
        fun `negative size test`() {
            assertThat(StringUtil.randomString(-1, false).length).isEqualTo(0)
        }

        @Test
        fun `0 size test`() {
            val result = StringUtil.randomString(0, false)
            assertThat(result.length).isEqualTo(0)
        }

        @Test
        fun `positive size test`() {
            val chars = ('a'..'z').toList()
            (1..5).forEach { size ->
                repeat(5) {
                    val result = StringUtil.randomString(size, false)
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
                val string = StringUtil.randomString(size, false, ' ', 'A', 'B', '.', ',')
                assertThat(string.length).isEqualTo(size)
                string.forEach { assertThat(chars).contains(it) }
            }
        }

        @Test
        fun `capitalize test`() {
            val size = 5
            repeat(10) {
                val string = StringUtil.randomString(size, true)
                assertThat(string.first()).isUpperCase
            }
        }

        @Test
        fun `not capitalize test`() {
            val size = 5
            repeat(10) {
                val string = StringUtil.randomString(size, false)
                assertThat(string.first()).isLowerCase()
            }
        }

        @Test
        fun `extraChars + capitalize test`() {
            val size = 5
            val chars = listOf(' ', 'A', 'B', '.', ',') + ('a'..'z')
            repeat(10) {
                val string = StringUtil.randomString(size, true, ' ', 'A', 'B', '.', ',')
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

    @Nested
    @DisplayName("Extract Words Test")
    inner class ExtractWordsTest {
        @Test
        fun `empty string test`() {
            assertThat("".extractWords()).isEqualTo(listOf<SentencePart>())
        }

        @Test
        fun `blank string test`() {
            assertThat("   ".extractWords()).isEqualTo(listOf(SP("   ", false)))
        }

        @Test
        fun `string with space test`() {
            data class Data(val input: String, val result: List<SP>)
            listOf(
                Data("aaa", listOf(SP("aaa"))),
                Data("aaa bbb", listOf(SP("aaa"), SP(" ", false), SP("bbb"))),
                Data(" aaa BBB", listOf(SP(" ", false), SP("aaa"), SP(" ", false), SP("BBB"))),
            ).forEachThis {
                assertThat(input.extractWords())
                    .`as`("input: %s", input)
                    .isEqualTo(result)
            }
        }

        @Test
        fun `random string test`() {
            val input = "pai3s du4 fya5si 6fd,fu.   aa;vv a's;'dk [h]f ] j\\as d?f"
            val result = listOf(
                SP("pai"),
                SP("3", false),
                SP("s"),
                SP(" ", false),
                SP("du"),
                SP("4 ", false),
                SP("fya"),
                SP("5", false),
                SP("si"),
                SP(" 6", false),
                SP("fd"),
                SP(",", false),
                SP("fu"),
                SP(".   ", false),
                SP("aa"),
                SP(";", false),
                SP("vv"),
                SP(" ", false),
                SP("a"),
                SP("'", false),
                SP("s"),
                SP(";'", false),
                SP("dk"),
                SP(" [", false),
                SP("h"),
                SP("]", false),
                SP("f"),
                SP(" ] ", false),
                SP("j"),
                SP("\\", false),
                SP("as"),
                SP(" ", false),
                SP("d"),
                SP("?", false),
                SP("f"),
            )
            assertThat(input.extractWords()).isEqualTo(result)
        }
    }
}
