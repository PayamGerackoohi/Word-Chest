package com.payamgr.wordchest.data.util

import androidx.test.filters.SmallTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

@SmallTest
class PlatformExtensionsKtTest {
    @Test
    fun `forEachThis test`() {
        val list = listOf(1, 2, 3)
        val result = buildString {
            append("[")
            var isFirst = true
            list.forEachThis {
                if (isFirst)
                    isFirst = false
                else
                    append(", ")
                append(this)
            }
            append("]")
        }
        assertThat(result).isEqualTo("[1, 2, 3]")
    }

    @Test
    fun `pairPartition test`() {
        data class Data(val range: Int, val parts: Int, val result: List<Pair<Int, Int>>)
        listOf(
            Data(1000, -1, listOf()),
            Data(1000, 0, listOf()),
            Data(1000, 1, listOf(500 to 1000)),
            Data(1000, 2, listOf(250 to 500, 750 to 1000)),
            Data(1000, 3, listOf(170 to 336, 502 to 668, 834 to 1000)),
            Data(1000, 4, listOf(125 to 250, 375 to 500, 625 to 750, 875 to 1000)),

            Data(0, -1, listOf()),
            Data(0, 0, listOf()),
            Data(0, 1, listOf()),

            Data(-1000, -1, listOf()),
            Data(-1000, 0, listOf()),
            Data(-1000, 1, listOf(-500 to -1000)),
            Data(-1000, 2, listOf(-250 to -500, -750 to -1000)),
            Data(-1000, 3, listOf(-170 to -336, -502 to -668, -834 to -1000)),
            Data(-1000, 4, listOf(-125 to -250, -375 to -500, -625 to -750, -875 to -1000)),
        ).forEachThis {
            assertThat(range.pairPartition(parts))
                .`as`("%s", this)
                .isEqualTo(result)
        }
    }
}
