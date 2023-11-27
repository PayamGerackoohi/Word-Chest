package com.payamgr.wordchest.data

import com.payamgr.wordchest.data.hilt.CoroutineModule
import com.payamgr.wordchest.data.model.fake.FakeWord
import com.payamgr.wordchest.data.model.Word
import com.payamgr.wordchest.data.model.WordKey
import com.payamgr.wordchest.data.util.AsyncGuard
import com.payamgr.wordchest.data.util.StringUtil
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
    override var wordHistory = MutableStateFlow(listOf<String>())
    override var currentLayer = MutableStateFlow<Int?>(null)
    override var currentWord = MutableStateFlow("")
    override var details = MutableStateFlow(listOf<Word.Data>())
    private val guard = AsyncGuard()

    override suspend fun searchFor(word: String): List<Word> = withPatcher {
        if (word.isBlank()) listOf()
        else listOf(fakeWord.someWord) + List(5) {
            Word(
                word + StringUtil.randomString(it, false, ' '),
                StringUtil.randomSentence(7),
            )
        }
    }

    override suspend fun push(key: WordKey): Unit = guard(dispatcher) {
        return@guard when (key) {
            is WordKey.Word -> push(key)
            is WordKey.Layer -> push(key)
        }
    }

    override suspend fun pop(): Boolean = guard(dispatcher, true) {
        wordHistory.run {
            if (value.isNotEmpty()) {
                value = value.take(currentLayer.value!!)
                currentLayer.value = value.size.let { if (it == 0) null else it - 1 }
                updateCurrentWord()
                updateDetails()
                true
            } else false
        }
    }

    private fun push(key: WordKey.Word) {
        val (word, clean) = key
        if (clean) currentLayer.value = null
        val index = wordHistory.value.indexOf(word)
        if (index == -1) {
            val layer = currentLayer.value.let { if (it == null) 0 else it + 1 }
            wordHistory.value = wordHistory.value.take(layer) + word
            currentLayer.value = layer
            currentWord.value = word
            updateDetails()
        } else {
            currentLayer.value = index
            updateCurrentWord()
            updateDetails()
        }
    }

    private fun push(key: WordKey.Layer) {
        val layer = key.layer
        if (0 <= layer && layer < wordHistory.value.size) {
            currentLayer.value = layer
            updateCurrentWord()
            updateDetails()
        }
    }

    private fun updateDetails() = currentWord.value.let { word ->
        details.value = if (word.isBlank()) listOf()
        else fakeWord.detailsFor(word)
    }

    private fun updateCurrentWord() = currentLayer.value.let { layer ->
        currentWord.value = if (layer == null) ""
        else wordHistory.value[layer]
    }

    private suspend fun <T> withPatcher(block: suspend CoroutineScope.() -> T): T = withContext(dispatcher, block)
}
