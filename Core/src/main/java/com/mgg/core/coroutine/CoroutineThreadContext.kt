@file:OptIn(DelicateCoroutinesApi::class)

package com.mgg.core.coroutine

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.View
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.android.asCoroutineDispatcher
import timber.log.Timber
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

val handler = CoroutineExceptionHandler { _, exception ->
    Timber.e("Caught $exception")
}

// https://www.jianshu.com/p/76d2f47b900d
val singleIOThreadContext = newSingleThreadContext("CoroutineIoScope")

val singleDBThreadContext = newSingleThreadContext("CoroutineDbScope")

val singleFileThreadContext = newSingleThreadContext("CoroutineFileScope")

val coroutineIoScope = CoroutineScope(singleIOThreadContext)

val coroutineDbScope = CoroutineScope(singleDBThreadContext)

val coroutineFileScope = CoroutineScope(singleFileThreadContext)

val coroutineMainScope = MainScope()

fun singleThreadDispatcherByHandlerThread(): CoroutineDispatcher {
    val thread = HandlerThread("CoreSingleThreadDispatcher")
    thread.start()
    return Handler(thread.looper).asCoroutineDispatcher()
}

fun mainDispatcher(): CoroutineDispatcher {
    return Handler(Looper.getMainLooper()).asCoroutineDispatcher()
}

fun singleThreadDispatcherByExecutors(): CoroutineDispatcher {
    return Executors.newSingleThreadExecutor().asCoroutineDispatcher()
}

private const val NUMBER_OF_THREADS = 20
val dispatcherFixedThreadPool = Executors
    .newFixedThreadPool(NUMBER_OF_THREADS)
    .asCoroutineDispatcher()

val dispatcherSingleThreadExecutor = Executors.newSingleThreadExecutor()
    .asCoroutineDispatcher()

fun View.onClickAutoDisposable(
    context: CoroutineContext = Dispatchers.Main,
    handler: suspend CoroutineScope.(v: android.view.View?) -> Unit
) {
    setOnClickListener { v ->
        GlobalScope.launch(context, CoroutineStart.DEFAULT) {
            handler(v)
        }.asAutoDisposable(v)
    }
}

fun View.onClickLifecycleScope(
    context: CoroutineContext = Dispatchers.Main,
    handler: suspend CoroutineScope.(v: android.view.View?) -> Unit
) {
    setOnClickListener { v ->
        v.findViewTreeLifecycleOwner()?.lifecycleScope?.launch(context, CoroutineStart.DEFAULT) {
            handler(v)
        }
    }
}


fun Job.asAutoDisposable(view: View) = AutoDisposableJob(view, this)

class AutoDisposableJob(private val view: View, private val wrapped: Job) : Job by wrapped,
    View.OnAttachStateChangeListener {
    override fun onViewAttachedToWindow(v: View?) = Unit

    override fun onViewDetachedFromWindow(v: View?) {
        // 当 View 被移除的时候，取消协程
        cancel()
        view.removeOnAttachStateChangeListener(this)
    }

    private fun isViewAttached() = view.isAttachedToWindow || view.windowToken != null

    init {
        if (isViewAttached()) {
            view.addOnAttachStateChangeListener(this)
        } else {
            cancel()
        }
        invokeOnCompletion {
            view.removeOnAttachStateChangeListener(this)
        }
    }
}


