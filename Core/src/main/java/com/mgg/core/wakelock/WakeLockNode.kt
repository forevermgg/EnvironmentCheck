package com.mgg.core.wakelock

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.PowerManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

private object WakeLockNodeStateElementKey
    : CoroutineContext.Key<WakeLockNode>

private class WakeLockNode(private val wakeLock: PowerManager.WakeLock) :
    AbstractCoroutineContextElement(WakeLockNodeStateElementKey) {

    init {
        wakeLock.setReferenceCounted(true)
    }

    fun acquire() {
        wakeLock.acquire()
    }

    fun release() {
        wakeLock.release()
    }
}

fun PowerManager.WakeLock.asCoroutineContext(): CoroutineContext {
    return WakeLockNode(this)
}

suspend fun <R> withWakeLock(block: suspend () -> R) : R {
    val node = currentCoroutineContext()[WakeLockNodeStateElementKey]?: return block()
    return try {
        node.acquire()
        block()
    } finally {
        node.release()
    }
}

// sample
@SuppressLint("InvalidWakeLockTag")
fun launchTask(application: Application) {
    val powerManager = application.getSystemService(Context.POWER_SERVICE) as PowerManager
    val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "mgg")
    GlobalScope.launch(wakeLock.asCoroutineContext()) {
        doLongRunningTask()
    }
}

suspend fun doLongRunningTask() {
    withWakeLock {

    }
}