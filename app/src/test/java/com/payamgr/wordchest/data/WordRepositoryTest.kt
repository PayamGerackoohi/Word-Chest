package com.payamgr.wordchest.data

import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@SmallTest
@OptIn(ExperimentalCoroutinesApi::class)
class WordRepositoryTest {
    @Nested
    @DisplayName("SearchFor Test")
    inner class SearchForTest {
        @Test
        fun `empty key test`() = runTest {
            WordRepositoryImpl(UnconfinedTestDispatcher(testScheduler)).apply {
                assertThat(searchFor("")).isEmpty()
            }
        }

        @Test
        fun `passing a key test`() = runTest {
            WordRepositoryImpl(UnconfinedTestDispatcher()).apply {
                val key = "aaa"
                val valueRegex = Regex("$key[a-z ]*")
                searchFor(key).forEach {
                    assertThat(valueRegex.matches(it.value))
                        .`as`("Value: %s", it.value)
                        .isTrue
                    assertThat(it.shortDescription.length).isEqualTo(20)
                }
            }
        }
    }
}
