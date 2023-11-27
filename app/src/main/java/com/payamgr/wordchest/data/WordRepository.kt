package com.payamgr.wordchest.data

import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.model.WordKey
import kotlinx.coroutines.flow.StateFlow

/**
 * The repository responsible for word query and history data management.
 * @property currentWord  the word, that is currently being displayed
 * @property currentLayer the current layer of the displaying word in the [wordHistory]
 * @property details      the definitions of the [currentWord]
 * @property wordHistory  the history stack of the words currently searched
 */
interface WordRepository {
    val currentWord: StateFlow<String>
    val currentLayer: StateFlow<Int?>
    val details: StateFlow<List<Word.Data>>
    val wordHistory: StateFlow<List<String>>

    /**
     * Searches for words related to the queried [word].
     * @param word the word to be queried
     * @return list of the most related [Word]s
     * @see Word
     */
    suspend fun searchFor(word: String): List<Word>

    /**
     * Requests, that the [WordKey] to be pushed to the history.
     * @see WordKey
     */
    suspend fun push(key: WordKey)

    /**
     * Requests, that the word at the [currentLayer] of the [wordHistory] to be popped.
     * @return whether the request was valid or just discarded.
     */
    suspend fun pop(): Boolean
}
