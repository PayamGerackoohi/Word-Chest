package com.payamgr.wordchest.data

import androidx.test.filters.SmallTest
import com.payamgr.wordchest.data.model.FakeWord
import com.payamgr.wordchest.data.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@SmallTest
@OptIn(ExperimentalCoroutinesApi::class)
class WordRepositoryTest {
    private val fakeWord = FakeWord()
    private val utd = UnconfinedTestDispatcher()
    private lateinit var repository: WordRepository

    @BeforeEach
    fun setup() {
        repository = WordRepositoryImpl(utd, fakeWord)
        Dispatchers.setMain(utd)
    }

    @AfterEach
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("Search for Word Test")
    inner class SearchForTest {
        @Test
        fun `empty key test`() = runTest {
            assertThat(repository.searchFor("")).isEmpty()
        }

        @Test
        fun `passing a key test`() = runTest {
            val key = "aaa"
            val valueRegex = Regex("$key[a-z ]*")
            repository.searchFor(key).forEach {
                if (it.value == "sample") assertThat(it.shortDescription).isEqualTo("A small part or quantity intended to show what the whole is like: ")
                else {
                    assertThat(valueRegex.matches(it.value)).`as`("Value: %s", it.value).isTrue
                    assertThat(it.shortDescription).endsWith(".")
                    assertThat(it.shortDescription.split(" ")).hasSize(7)
                }
            }
        }
    }

    @Nested
    @DisplayName("Word of Layer Test")
    inner class WordOfTest {
        @Test
        fun `initial state`() = runTest {
            assertThat(repository.wordHistory.value).isEqualTo(listOf<String>())
            assertThat(repository.wordOf(-1)).isEqualTo("")
            assertThat(repository.wordOf(0)).isEqualTo("")
            assertThat(repository.wordOf(1)).isEqualTo("")
        }
    }

    @Nested
    @DisplayName("Push Layer with Word Test")
    inner class PushTest {
        @Test
        fun `push negative layer`() = runTest {
            val originalOut = System.out
            val outputStream = ByteArrayOutputStream()
            try {
                System.setOut(PrintStream(outputStream))
                repository.push(-1, "")
                assertThat(repository.wordHistory.value).isEqualTo(listOf<String>())
                assertThat(repository.wordOf(-1)).isEqualTo("")
                assertThat(repository.wordOf(0)).isEqualTo("")
                assertThat(repository.wordOf(1)).isEqualTo("")
                assertThat(outputStream.toString().trim())
                    .isEqualTo("*** WordRepositoryImpl::push: Invalid operation: index: -1, currentSize: 0")
            } finally {
                System.setOut(originalOut)
            }
        }

        @Test
        fun `push layer 1`() = runTest {
            repository.push(1, "aaa")
            assertThat(repository.wordHistory.value).isEqualTo(listOf<String>())
            assertThat(repository.wordOf(-1)).isEqualTo("")
            assertThat(repository.wordOf(0)).isEqualTo("")
            assertThat(repository.wordOf(1)).isEqualTo("")
        }

        @Test
        fun `push layers`() = runTest {
            repository.push(0, "aaa")
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa"))
            assertThat(repository.wordOf(-1)).isEqualTo("")
            assertThat(repository.wordOf(0)).isEqualTo("aaa")
            assertThat(repository.wordOf(1)).isEqualTo("")

            repository.push(1, "bbb")
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa", "bbb"))
            assertThat(repository.wordOf(-1)).isEqualTo("")
            assertThat(repository.wordOf(0)).isEqualTo("aaa")
            assertThat(repository.wordOf(1)).isEqualTo("bbb")
            assertThat(repository.wordOf(2)).isEqualTo("")

            repository.push(2, "ccc")
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa", "bbb", "ccc"))
            assertThat(repository.wordOf(-1)).isEqualTo("")
            assertThat(repository.wordOf(0)).isEqualTo("aaa")
            assertThat(repository.wordOf(1)).isEqualTo("bbb")
            assertThat(repository.wordOf(2)).isEqualTo("ccc")
            assertThat(repository.wordOf(3)).isEqualTo("")

            repository.push(-1, "ddd")
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa", "bbb", "ccc"))
            assertThat(repository.wordOf(-1)).isEqualTo("")
            assertThat(repository.wordOf(0)).isEqualTo("aaa")
            assertThat(repository.wordOf(1)).isEqualTo("bbb")
            assertThat(repository.wordOf(2)).isEqualTo("ccc")
            assertThat(repository.wordOf(3)).isEqualTo("")

            repository.push(1, "ddd")
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa", "ddd"))
            assertThat(repository.wordOf(-1)).isEqualTo("")
            assertThat(repository.wordOf(0)).isEqualTo("aaa")
            assertThat(repository.wordOf(1)).isEqualTo("ddd")
            assertThat(repository.wordOf(2)).isEqualTo("")

            repository.push(0, "eee")
            assertThat(repository.wordHistory.value).isEqualTo(listOf("eee"))
            assertThat(repository.wordOf(-1)).isEqualTo("")
            assertThat(repository.wordOf(0)).isEqualTo("eee")
            assertThat(repository.wordOf(1)).isEqualTo("")
        }
    }

    @Nested
    @DisplayName("Details at Layer Test")
    inner class DetailsAtTest {
        @Test
        fun `initial state`() = runTest {
            assertThat(repository.detailsAt(0)).isEqualTo(listOf<Word.Data>())
        }

        @Test
        fun `push and check details`() = runTest {
            repository.push(0, "aaa")
            assertThat(repository.detailsAt(-1)).isEqualTo(listOf<Word.Data>())
            assertThat(repository.detailsAt(0).first().word).isEqualTo("aaa")
            assertThat(repository.detailsAt(1)).isEqualTo(listOf<Word.Data>())

            repository.push(1, "bbb")
            assertThat(repository.detailsAt(-1)).isEqualTo(listOf<Word.Data>())
            assertThat(repository.detailsAt(0).first().word).isEqualTo("aaa")
            assertThat(repository.detailsAt(1).first().word).isEqualTo("bbb")
            assertThat(repository.detailsAt(2)).isEqualTo(listOf<Word.Data>())
        }
    }
}
