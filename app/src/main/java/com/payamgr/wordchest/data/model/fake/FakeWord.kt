package com.payamgr.wordchest.data.model.fake

import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.model.Word.*
import com.payamgr.wordchest.data.model.Word.Data.*
import com.payamgr.wordchest.data.model.Word.Data.Section.*
import com.payamgr.wordchest.data.model.Word.Data.Section.Type.*
import com.payamgr.wordchest.data.model.Word.Data.Section.Definition.*
import com.payamgr.wordchest.data.model.Word.Data.Section.Definition.Part.Sub.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeWord @Inject constructor() {
    private val wordDataMap by lazy { WordDataMap.build() }
    private var index = 0
    private val keys by lazy { wordDataMap.map { it.key } }
    private val words by lazy {
        wordDataMap.map {
            Word(
                it.key,
                it.value.first()
                    .sections.first()
                    .definitions.first()
                    .value
                    .subs.first { s -> s is Normal }
                    .content
            )
        }
    }

    /**
     * Some random word
     * @see Word
     */
    val someWord: Word
        get() {
            val word = words[index]
            incrementIndex()
            return word
        }

    /**
     * The detailed definitions for the searched word
     */
    fun detailsFor(word: String) = wordDataMap[word] ?: dummyListOf(word)

    /**
     * A random list of realistic word data
     * @see Word.Data
     */
    val wordData: List<Data>
        get() {
            val key = keys[index]
            incrementIndex()
            return wordDataMap[key]!!
        }

    /**
     * A random list of simplified word data
     * @param word the requested word
     * @return a list of [Word.Data]
     * @see Word.Data
     */
    fun dummyListOf(word: String) = listOf(
        Data(
            word,
            word,
            Section(
                Noun,
                Definition(
                    Part(
                        Normal("A good \"$word\" is good and a bad \"$word\" is bad \uD83D\uDE06")
                    )
                )
            )
        )
    )

    fun incrementIndex() {
        val i = index + 1
        index = if (i >= keys.size) 0 else i
    }
}
