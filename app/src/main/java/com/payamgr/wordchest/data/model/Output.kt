package com.payamgr.wordchest.data.model

sealed class Output<out T> {
    data class Success<T>(val data: T) : Output<T>()
    data class Error(val message: String) : Output<Nothing>()
}
