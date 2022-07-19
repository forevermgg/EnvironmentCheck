package com.mgg.checkenv.internal

import android.os.Handler
import android.os.Looper

internal val mainHandler by lazy { Handler(Looper.getMainLooper()) }

internal val isMainThread: Boolean get() = Looper.getMainLooper().thread === Thread.currentThread()

internal fun checkMainThread() {
    check(isMainThread) {
        "Should be called from the main thread, not ${Thread.currentThread()}"
    }
}

internal fun checkNotMainThread() {
    check(!isMainThread) {
        "Should not be called from the main thread"
    }
}

internal fun checkMainThread(message: String?) {
    check(Looper.getMainLooper() == Looper.myLooper()) {
        message ?: "Current is not under main-thread!"
    }
}

internal fun checkWorkThread(message: String?) {
    check(Looper.getMainLooper() != Looper.myLooper()) {
        message ?: "Current is under main-thread!"
    }
}
