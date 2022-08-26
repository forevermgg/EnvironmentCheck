package com.mgg.kotlinextensions.utils

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

fun LifecycleOwner.lifecycleHandler(
    looper: Looper = Looper.getMainLooper(),
    callback: Handler.Callback? = null
): LifecycleHandler = LifecycleHandler(this, looper, callback)

class LifecycleHandler(
    private val owner: LifecycleOwner,
    looper: Looper = Looper.getMainLooper(),
    callback: Callback? = null
) : Handler(looper, callback), DefaultLifecycleObserver {
    init {
        owner.lifecycle.addObserver(this)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
        removeCallbacksAndMessages(null)
    }
}

fun LifecycleOwner.lifecycleHandlerThread(
    callback: Handler.Callback? = null
): LifecycleThreadHandler = LifecycleThreadHandler(this, callback)

class LifecycleThreadHandler(
    private val owner: LifecycleOwner,
    callback: Handler.Callback? = null
) : DefaultLifecycleObserver {
    private var handlerThread: HandlerThread? = null
    private var handler: Handler? = null

    init {
        owner.lifecycle.addObserver(this)
        handlerThread = HandlerThread("LifecycleHandlerThread")
        handlerThread?.start()
        handlerThread?.looper?.let {
            handler = Handler(it, callback)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
        handler?.removeCallbacksAndMessages(null)
        handler = null
        handlerThread?.quitSafely()
        handlerThread = null
    }
}
