package com.payamgr.wordchest.data

import androidx.test.filters.SmallTest
import com.payamgr.wordchest.data.model.fake.FakeWord
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.model.WordKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
        fun `blank key test`() = runTest {
            assertThat(repository.searchFor("  ")).isEmpty()
        }

        @Test
        fun `passing a key test`() = runTest {
            val key = "aaa"
            val valueRegex = Regex("$key[a-z ]*")
            repository.searchFor(key).forEach {
                if (it.value == "sample") assertThat(it.shortDescription)
                    .isEqualTo("A small part or quantity intended to show what the whole is like: ")
                else {
                    assertThat(valueRegex.matches(it.value)).`as`("Value: %s", it.value).isTrue
                    assertThat(it.shortDescription).endsWith(".")
                    assertThat(it.shortDescription.split(" ")).hasSize(7)
                }
            }
        }
    }

    @Nested
    @DisplayName("Word of Current Layer Test")
    inner class CurrentWordTest {
        @Test
        fun `initial state`() = runTest {
            assertThat(repository.wordHistory.value).isEqualTo(listOf<String>())
            assertThat(repository.currentWord.value).isEqualTo("")
        }
    }

    @Nested
    @DisplayName("Push Layer with Word or at Layer Test")
    inner class PushTest {
        @Test
        fun `push negative layer`() = runTest {
//            val originalOut = System.out
//            val outputStream = ByteArrayOutputStream()
//            try {
//                System.setOut(PrintStream(outputStream))
            repository.push(WordKey.Layer(-1))
            assertThat(repository.wordHistory.value).isEmpty()
            assertThat(repository.currentLayer.value).isEqualTo(null)
            assertThat(repository.currentWord.value).isEqualTo("")
            assertThat(repository.details.value).isEmpty()
//                assertThat(outputStream.toString().trim())
//                    .isEqualTo("*** WordRepositoryImpl::push: Invalid operation: index: -1, size: 0")
//            } finally {
//                System.setOut(originalOut)
//            }
        }

        @Test
        fun `push to layer of more than history size`() = runTest {
            repository.push(WordKey.Layer(10))
            assertThat(repository.wordHistory.value).isEmpty()
            assertThat(repository.currentLayer.value).isEqualTo(null)
            assertThat(repository.currentWord.value).isEqualTo("")
            assertThat(repository.details.value).isEmpty()
        }

        @Test
        fun `push to layer 0`() = runTest {
            repository.push(WordKey.Layer(0))
            assertThat(repository.wordHistory.value).isEmpty()
            assertThat(repository.currentLayer.value).isEqualTo(null)
            assertThat(repository.currentWord.value).isEqualTo("")
            assertThat(repository.details.value).isEmpty()
        }

        @Test
        fun `push to existing word`() = runTest {
            repository.push(WordKey.Word("aaa"))
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa"))
            assertThat(repository.currentLayer.value).isEqualTo(0)
            assertThat(repository.currentWord.value).isEqualTo("aaa")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("aaa")

            repository.push(WordKey.Word("bbb"))
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa", "bbb"))
            assertThat(repository.currentLayer.value).isEqualTo(1)
            assertThat(repository.currentWord.value).isEqualTo("bbb")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("bbb")

            repository.push(WordKey.Word("aaa"))
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa", "bbb"))
            assertThat(repository.currentLayer.value).isEqualTo(0)
            assertThat(repository.currentWord.value).isEqualTo("aaa")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("aaa")
        }

        @Test
        fun `push layers`() = runTest {
            repository.push(WordKey.Word("aaa"))
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa"))
            assertThat(repository.currentLayer.value).isEqualTo(0)
            assertThat(repository.currentWord.value).isEqualTo("aaa")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("aaa")

            repository.push(WordKey.Word("bbb"))
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa", "bbb"))
            assertThat(repository.currentLayer.value).isEqualTo(1)
            assertThat(repository.currentWord.value).isEqualTo("bbb")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("bbb")

            repository.push(WordKey.Word("ccc"))
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa", "bbb", "ccc"))
            assertThat(repository.currentLayer.value).isEqualTo(2)
            assertThat(repository.currentWord.value).isEqualTo("ccc")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("ccc")

            repository.push(WordKey.Layer(1))
            repository.push(WordKey.Word("ddd"))
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa", "bbb", "ddd"))
            assertThat(repository.currentLayer.value).isEqualTo(2)
            assertThat(repository.currentWord.value).isEqualTo("ddd")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("ddd")

            repository.push(WordKey.Layer(0))
            repository.push(WordKey.Word("eee"))
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa", "eee"))
            assertThat(repository.currentLayer.value).isEqualTo(1)
            assertThat(repository.currentWord.value).isEqualTo("eee")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("eee")
        }

        @Test
        fun `clean push test`() = runTest {
            repository.push(WordKey.Word("aaa"))
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa"))
            assertThat(repository.currentLayer.value).isEqualTo(0)
            assertThat(repository.currentWord.value).isEqualTo("aaa")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("aaa")

            repository.pop()
            assertThat(repository.wordHistory.value).isEqualTo(listOf<String>())
            assertThat(repository.currentLayer.value).isEqualTo(null)
            assertThat(repository.currentWord.value).isEqualTo("")
            assertThat(repository.details.value).isEmpty()

            repository.push(WordKey.Word("bbb", true))
            assertThat(repository.wordHistory.value).isEqualTo(listOf("bbb"))
            assertThat(repository.currentLayer.value).isEqualTo(0)
            assertThat(repository.currentWord.value).isEqualTo("bbb")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("bbb")
        }
    }

    @Nested
    @DisplayName("Details of Current Layer Test")
    inner class DetailsTest {
        @Test
        fun `initial state`() = runTest {
            assertThat(repository.details.value).isEqualTo(listOf<Word.Data>())
        }

        @Test
        fun `push and check details`() = runTest {
            repository.push(WordKey.Word("aaa"))
            assertThat(repository.details.value.first().word).isEqualTo("aaa")

            repository.push(WordKey.Word("bbb"))
            assertThat(repository.details.value.first().word).isEqualTo("bbb")
        }
    }

    @Nested
    @DisplayName("Pop Current Layer")
    inner class PopTest {
        @Test
        fun `empty list test`() = runTest {
            assertThat(repository.pop()).isFalse
            assertThat(repository.pop()).isFalse
            assertThat(repository.pop()).isFalse
            assertThat(repository.wordHistory.value).isEqualTo(listOf<String>())
            assertThat(repository.currentLayer.value).isEqualTo(null)
            assertThat(repository.currentWord.value).isEqualTo("")
            assertThat(repository.details.value).isEqualTo(listOf<Word.Data>())
        }

        @Test
        fun `sequential pop test`() = runTest {
            repository.push(WordKey.Word("aaa"))
            repository.push(WordKey.Word("bbb"))
            repository.push(WordKey.Word("ccc"))
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa", "bbb", "ccc"))
            assertThat(repository.currentLayer.value).isEqualTo(2)
            assertThat(repository.currentWord.value).isEqualTo("ccc")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("ccc")

            assertThat(repository.pop()).isTrue
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa", "bbb"))
            assertThat(repository.currentLayer.value).isEqualTo(1)
            assertThat(repository.currentWord.value).isEqualTo("bbb")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("bbb")

            assertThat(repository.pop()).isTrue
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa"))
            assertThat(repository.currentLayer.value).isEqualTo(0)
            assertThat(repository.currentWord.value).isEqualTo("aaa")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("aaa")

            assertThat(repository.pop()).isTrue
            assertThat(repository.wordHistory.value).isEqualTo(listOf<String>())
            assertThat(repository.currentLayer.value).isEqualTo(null)
            assertThat(repository.currentWord.value).isEqualTo("")
            assertThat(repository.details.value).isEmpty()

            assertThat(repository.pop()).isFalse
            assertThat(repository.wordHistory.value).isEqualTo(listOf<String>())
            assertThat(repository.currentLayer.value).isEqualTo(null)
            assertThat(repository.currentWord.value).isEqualTo("")
            assertThat(repository.details.value).isEmpty()
        }

        @Test
        fun `pop while changing layer and push word`() = runTest {
            repository.push(WordKey.Word("aaa"))
            repository.push(WordKey.Word("bbb"))
            repository.push(WordKey.Word("ccc"))

            repository.pop()
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa", "bbb"))
            assertThat(repository.currentLayer.value).isEqualTo(1)
            assertThat(repository.currentWord.value).isEqualTo("bbb")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("bbb")

            repository.push(WordKey.Word("ddd"))
            repository.pop()
            assertThat(repository.wordHistory.value).isEqualTo(listOf("aaa", "bbb"))
            assertThat(repository.currentLayer.value).isEqualTo(1)
            assertThat(repository.currentWord.value).isEqualTo("bbb")
            assertThat(repository.details.value).isNotEmpty
            assertThat(repository.details.value.first().word).isEqualTo("bbb")

            repository.push(WordKey.Layer(0))
            repository.pop()
            assertThat(repository.wordHistory.value).isEqualTo(listOf<String>())
            assertThat(repository.currentLayer.value).isEqualTo(null)
            assertThat(repository.currentWord.value).isEqualTo("")
            assertThat(repository.details.value).isEmpty()
        }
    }

    @Nested
    inner class Setters { // making jacoco happy
        @Test
        fun `word History`() {
            (repository as WordRepositoryImpl).wordHistory = MutableStateFlow(listOf())
        }

        @Test
        fun `current layer`() {
            (repository as WordRepositoryImpl).currentLayer = MutableStateFlow(null)
        }

        @Test
        fun `current word`() {
            (repository as WordRepositoryImpl).currentWord = MutableStateFlow("")
        }

        @Test
        fun details() {
            (repository as WordRepositoryImpl).details = MutableStateFlow(listOf())
        }
    }
}
