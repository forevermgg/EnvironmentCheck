package com.mgg.kotlinextensions.view

import android.view.View
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import kotlinx.coroutines.*

fun View.delayOnLifecycle(
    duration: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: () -> Unit
): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
    lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
        delay(duration)
        block()
    }
}

fun View.delayOnLifecycleSuspended(
    duration: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: suspend () -> Unit
): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
    lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
        delay(duration)
        block()
    }
}
