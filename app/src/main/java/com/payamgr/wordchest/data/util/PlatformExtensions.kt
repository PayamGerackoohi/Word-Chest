package com.payamgr.wordchest.data.util

import com.payamgr.wordchest.data.model.Output

/**
 * Performs the given [action] on each element, in scope of the item.
 */
fun <T> Iterable<T>.forEachThis(action: T.() -> Unit): Unit = forEach { it.action() }

/**
 * Makes a list of sub-ranges out of an integer.
 * It represents making a sequential on-off ranges through range of [0, [this]].
 * @param parts the count of the "on ranges" items
 * @result the list of "on ranges"
 */
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

/**
 * Adds the [item] at the [index] to [this] list and truncates later indices.
 * @param index the index at which the item is going to be inserted
 * @param item  the item to be inserted
 * @return if the operation is successful, the new list is returned. Otherwise an error is returned.
 */
fun <T> List<T>.addOrTruncate(index: Int, item: T): Output<List<T>> {
    if (index < 0 || index > size) {
        return Output.Error("Invalid operation: index: $index, size: $size")
    }
    val list = if (index < size) take(index) else this
    return Output.Success(list + item)
}
