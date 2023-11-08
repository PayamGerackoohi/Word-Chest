package com.payamgr.wordchest.data.util

object StringUtil {
    private val chars = listOf(' ') + ('a'..'z')
    fun randomString(size: Int): String = (1..size).map { chars.random() }.joinToString("")
}
