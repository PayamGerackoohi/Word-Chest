package com.payamgr.wordchest.data

import com.payamgr.wordchest.data.hilt.CoroutineModule
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.util.StringUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordRepositoryImpl @Inject constructor(
    @CoroutineModule.Default private val dispatcher: CoroutineDispatcher,
) : WordRepository {
    override suspend fun searchFor(word: String): List<Word> = withContext(dispatcher) {
        if (word.isBlank())
            listOf()
        else
            List(5) {
                Word(
                    word + StringUtil.randomString(it),
                    StringUtil.randomString(20),
                )
            }
    }
}
