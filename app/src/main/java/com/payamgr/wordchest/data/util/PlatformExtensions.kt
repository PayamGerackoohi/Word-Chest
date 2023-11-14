package com.payamgr.wordchest.data.util

import com.payamgr.wordchest.data.model.Output

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

fun <T> List<T>.addOrTruncate(index: Int, item: T): Output<List<T>> {
    val currentSize = size
    if (index < 0 || index > currentSize) {
        return Output.Error("Invalid operation: index: $index, currentSize: $currentSize")
    }
    val list = if (index < currentSize) take(index) else this
    return Output.Success(list + item)
}
