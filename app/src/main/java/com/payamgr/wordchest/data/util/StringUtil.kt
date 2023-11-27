package com.payamgr.wordchest.data.util

import com.payamgr.wordchest.data.model.SentencePart
import kotlin.random.Random

/**
 * A utility class for string data manipulation
 */
object StringUtil {
    /**
     * The default list of characters, used for random string generation.
     */
    private val chars: List<Char> by lazy { ('a'..'z').toList() }

    /**
     * A random string with [size].
     * @param size       the demanded size of the string
     * @param capitalize whether the first character should be capitalized
     * @param extraChars the extra characters other than [chars]
     * @return demanded random string
     */
    fun randomString(size: Int, capitalize: Boolean, vararg extraChars: Char): String =
        if (size <= 0)
            ""
        else
            mergeChars(extraChars.toList()).let { range ->
                List(size) { index ->
                    range.random().let {
                        if (capitalize && index == 0) it.uppercase()
                        else it
                    }
                }.joinToString("")
            }

    private fun mergeChars(extraChars: List<Char>) = if (extraChars.isEmpty()) chars
    else chars + extraChars

    /**
     * A random string with [count] number of words.
     * @param count the number of the words separated by space character
     * @return demanded sentence
     */
    fun randomSentence(count: Int): String = if (count <= 0) "" else List(count) { index ->
        randomString(Random.nextInt(1, 10), index == 0)
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

    /**
     * Extracts the words inside a sentence.
     * @return the list of [SentencePart]s
     * @see SentencePart
     */
    fun String.extractWords(): List<SentencePart> {
        if (isNullOrEmpty()) return listOf()
        val list = mutableListOf<SentencePart>()
        val sb = StringBuilder()
        var isLetter = false
        forEach {
            if (isLetter xor it.isLetter()) {
                sb.wrapInto(list, isLetter)
                isLetter = it.isLetter()
            }
            sb.append(it)
        }
        sb.wrapInto(list, isLetter)
        return list
    }

    private fun StringBuilder.wrapInto(list: MutableList<SentencePart>, isLetter: Boolean) {
        val part = toString()
        if (part.isNotEmpty())
            list.add(SentencePart(part, isLetter))
        clear()
    }
}
