package com.payamgr.wordchest.data

import com.payamgr.wordchest.data.hilt.CoroutineModule
import com.payamgr.wordchest.data.model.FakeWord
import com.payamgr.wordchest.data.model.Output
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.util.StringUtil
import com.payamgr.wordchest.data.util.addOrTruncate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordRepositoryImpl @Inject constructor(
    @CoroutineModule.Default private val dispatcher: CoroutineDispatcher,
    private val fakeWord: FakeWord,
) : WordRepository {
    override var wordHistory = MutableStateFlow<List<String>>(listOf())

    override suspend fun searchFor(word: String): List<Word> = withPatcher {
        if (word.isBlank()) listOf()
        else listOf(fakeWord.someWord) + List(5) {
            Word(
                word + StringUtil.randomString(it, ' '),
                StringUtil.randomSentence(7),
            )
        }
    }

    override suspend fun push(layer: Int, word: String): Unit = withPatcher {
        wordHistory.value.addOrTruncate(layer, word).let {
            when (it) {
                is Output.Success -> wordHistory.value = it.data
                is Output.Error -> println("*** WordRepositoryImpl::push: ${it.message}")
            }
        }
    }

    override suspend fun wordOf(layer: Int): String = withPatcher { wordHistory.value.getOrNull(layer) ?: "" }

    override suspend fun detailsAt(layer: Int): List<Word.Data> = withPatcher {
        wordOf(layer).let { word ->
            if (word.isBlank()) listOf()
            else fakeWord.detailsFor(word)
        }
    }

    private suspend fun <T> withPatcher(block: suspend CoroutineScope.() -> T): T = withContext(dispatcher, block)
}
