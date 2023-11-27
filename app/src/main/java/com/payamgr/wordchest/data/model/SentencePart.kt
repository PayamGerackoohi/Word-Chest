package com.payamgr.wordchest.data.model

/**
 * Part of a sentence, which can be either word or not.
 * It's used for making a sentence clickable, just for its words not the punctuations.
 * @property part a substring of the sentence, that is either word or not.
 * @property isWord whether the part is word or not
 */
data class SentencePart(val part: String, val isWord: Boolean = true)
