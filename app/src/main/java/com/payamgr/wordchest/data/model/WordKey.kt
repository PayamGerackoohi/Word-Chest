package com.payamgr.wordchest.data.model

/**
 * A key used for querying the word
 */
sealed class WordKey {
    /**
     * Word type key
     * @property word  the word to be queried
     * @property clean if true, the previous search results should be cleaned
     */
    data class Word(val word: String, val clean: Boolean = false) : WordKey()

    /**
     * Layer type key
     * @property layer the layer of the word in the history is queried
     */
    data class Layer(val layer: Int) : WordKey()
}
