package com.payamgr.wordchest.data.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AsyncGuardTest {
    private lateinit var guard: AsyncGuard
    private val utd = UnconfinedTestDispatcher()

    @BeforeEach
    fun setup() {
        guard = AsyncGuard()
        Dispatchers.setMain(utd)
    }

    @AfterEach
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Unit Output - check busy mode rejection`() = runTest {
        var result = 0
        joinAll(
            launch {
                guard {
                    delay(1_000)
                    result++
                }
            },
            launch { guard { result += 10 } },
            launch {
                delay(10_000)
                guard { result += 100 }
            },
        )
        assertThat(result).isEqualTo(101)
    }

    @Test
    fun `Unit Output - check exception state recovery`() = runTest {
        var result = 0
        joinAll(
            launch {
                try {
                    guard {
                        delay(1_000)
                        throw Exception("Error!")
                    }
                } catch (e: Exception) {
                    assertThat(e.message).isEqualTo("Error!")
                }
            },
            launch { guard { result += 10 } },
            launch {
                delay(10_000)
                guard { result += 100 }
            },
        )
        assertThat(result).isEqualTo(100)
    }

    @Test
    fun `With Dispatcher - Unit Output - check busy mode rejection`() = runTest {
        var result = 0
        joinAll(
            launch {
                guard(utd) {
                    delay(1_000)
                    result++
                }
            },
            launch { guard(utd) { result += 10 } },
            launch {
                delay(10_000)
                guard(utd) { result += 100 }
            },
        )
        assertThat(result).isEqualTo(101)
    }

    @Test
    fun `With Dispatcher - Unit Output - check exception state recovery`() = runTest {
        var result = 0
        joinAll(
            launch {
                try {
                    guard(utd) {
                        delay(1_000)
                        throw Exception("Error!")
                    }
                } catch (e: Exception) {
                    assertThat(e.message).isEqualTo("Error!")
                }
            },
            launch { guard(utd) { result += 10 } },
            launch {
                delay(10_000)
                guard(utd) { result += 100 }
            },
        )
        assertThat(result).isEqualTo(100)
    }

    @Test
    fun `Int Output - check busy mode rejection`() = runTest {
        var result = 0
        joinAll(
            launch {
                result += guard(0) {
                    delay(1_000)
                    1
                }
            },
            launch { result += guard(0) { 10 } },
            launch {
                delay(10_000)
                result += guard(0) { 100 }
            },
        )
        assertThat(result).isEqualTo(101)
    }

    @Test
    fun `Int Output - check exception state recovery`() = runTest {
        var result = 0
        joinAll(
            launch {
                try {
                    result += guard(0) {
                        delay(1_000)
                        throw Exception("Error!")
                    }
                } catch (e: Exception) {
                    assertThat(e.message).isEqualTo("Error!")
                }
            },
            launch { result += guard(0) { 10 } },
            launch {
                delay(10_000)
                result += guard(0) { 100 }
            },
        )
        assertThat(result).isEqualTo(100)
    }

    @Test
    fun `With Dispatcher - Int Output - check busy mode rejection`() = runTest {
        var result = 0
        joinAll(
            launch {
                result += guard(utd, 0) {
                    delay(1_000)
                    1
                }
            },
            launch { result += guard(utd, 0) { 10 } },
            launch {
                delay(10_000)
                result += guard(utd, 0) { 100 }
            },
        )
        assertThat(result).isEqualTo(101)
    }

    @Test
    fun `With Dispatcher - Int Output - check exception state recovery`() = runTest {
        var result = 0
        joinAll(
            launch {
                try {
                    result += guard(utd, 0) {
                        delay(1_000)
                        throw Exception("Error!")
                    }
                } catch (e: Exception) {
                    assertThat(e.message).isEqualTo("Error!")
                }
            },
            launch { result += guard(utd, 0) { 10 } },
            launch {
                delay(10_000)
                result += guard(utd, 0) { 100 }
            },
        )
        assertThat(result).isEqualTo(100)
    }
}
