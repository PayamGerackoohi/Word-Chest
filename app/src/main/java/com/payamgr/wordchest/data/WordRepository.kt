package com.payamgr.wordchest.data

import com.payamgr.wordchest.data.model.Word

interface WordRepository {
    suspend fun searchFor(word: String): List<Word>
}
