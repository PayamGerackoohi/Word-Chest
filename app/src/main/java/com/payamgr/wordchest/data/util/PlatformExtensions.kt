package com.payamgr.wordchest.data.util

fun <T> Iterable<T>.forEachThis(action: T.() -> Unit): Unit = forEach { it.action() }

fun Int.pairPartition(parts: Int): List<Pair<Int, Int>> {
    if (parts <= 0 || this == 0)
        return listOf()
    val step = this / parts / 2
    var part = this
    return List(parts) {
        val b = part
        part -= step
        val a = part
        part -= step
        a to b
    }.reversed()
}
