package com.payamgr.wordchest.data.model

/**
 * Outcome of operations, which might be failed.
 */
sealed class Output<out T> {
    /** Success case wraps data */
    data class Success<T>(val data: T) : Output<T>()

    /** Error case wraps error message */
    data class Error(val message: String) : Output<Nothing>()
}
