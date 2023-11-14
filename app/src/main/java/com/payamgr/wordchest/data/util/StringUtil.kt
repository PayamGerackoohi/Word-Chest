package com.payamgr.wordchest.data.util

import kotlin.random.Random

object StringUtil {
    private val chars: List<Char> by lazy { ('a'..'z').toList() }

    fun randomString(size: Int, vararg extraChars: Char, capitalize: Boolean = false): String = if (size <= 0) ""
    else mergeChars(extraChars.toList()).let { range ->
        List(size) { index ->
            range.random().let {
                if (capitalize && index == 0) it.uppercase()
                else it
            }
        }.joinToString("")
    }

    private fun mergeChars(extraChars: List<Char>) = if (extraChars.isEmpty()) chars
    else chars + extraChars

    fun randomSentence(count: Int): String = if (count <= 0) "" else List(count) { index ->
        randomString(Random.nextInt(1, 10), capitalize = index == 0)
    }.let { list ->
        var isFirst = true
        buildString {
            list.forEach {
                if (isFirst) isFirst = false
                else append(" ")
                append(it)
            }
            append(".")
        }
    }
}
