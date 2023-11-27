package com.payamgr.wordchest.data.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * It Makes sure that just one invocations runs
 * The other interrupting calls would be discarded. It also makes sure the exception occurrence does not
 * corrupt the state of the guard
 * @property isBusy the state of the guard, that makes sure just one process is ongoing
 */
class AsyncGuard {
    private var isBusy: Boolean = false

    /**
     * The safe closure of the guard with no returning object
     * @param action the action, that runs inside the safe closure
     */
    suspend operator fun invoke(action: suspend () -> Unit) {
        if (isBusy) return
        try {
            isBusy = true
            action()
        } finally {
            isBusy = false
        }
    }

    /**
     * The safe closure of the guard with some returning object
     * @param default the default value of type [T], returned in case of exception
     * @param action  the action, that runs inside the safe closure and returns [T]
     * @return [T]    the [action] return type
     */
    suspend operator fun <T> invoke(default: T, action: suspend () -> T): T {
        if (isBusy) return default
        try {
            isBusy = true
            return action()
        } finally {
            isBusy = false
        }
    }

    /**
     * The safe closure of the guard with no returning object, running on a [dispatcher]
     * @param dispatcher the coroutine dispatcher to run the action on it
     * @param action     the action, that runs inside the safe closure
     */
    suspend operator fun invoke(dispatcher: CoroutineDispatcher, action: suspend () -> Unit) =
        withContext(dispatcher) {
            if (isBusy) return@withContext
            try {
                isBusy = true
                action()
            } finally {
                isBusy = false
            }
        }

    /**
     * The safe closure of the guard with some returning object, running on a [dispatcher]
     * @param dispatcher the coroutine dispatcher to run the action on it
     * @param default    the default value of type [T], returned in case of exception
     * @param action     the action, that runs inside the safe closure and returns [T]
     * @return [T]       the [action] return type
     */
    suspend operator fun <T> invoke(
        dispatcher: CoroutineDispatcher,
        default: T,
        action: suspend () -> T,
    ): T = withContext(dispatcher) {
        if (isBusy) return@withContext default
        try {
            isBusy = true
            return@withContext action()
        } finally {
            isBusy = false
        }
    }
}
