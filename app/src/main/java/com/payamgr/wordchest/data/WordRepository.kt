package com.payamgr.wordchest.data

import com.payamgr.wordchest.data.model.Word
import kotlinx.coroutines.flow.StateFlow

interface WordRepository {
    val wordHistory: StateFlow<List<String>>
    suspend fun wordOf(layer: Int): String
    suspend fun searchFor(word: String): List<Word>
    suspend fun detailsAt(layer: Int): List<Word.Data>
    suspend fun push(layer: Int, word: String)
}
