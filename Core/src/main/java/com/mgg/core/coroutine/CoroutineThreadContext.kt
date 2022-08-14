@file:OptIn(DelicateCoroutinesApi::class)

package com.mgg.core.coroutine

import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.Executors

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

private  const val NUMBER_OF_THREADS = 20
val dispatcherFixedThreadPool = Executors
    .newFixedThreadPool(NUMBER_OF_THREADS)
    .asCoroutineDispatcher()

val dispatcherSingleThreadExecutor = Executors.newSingleThreadExecutor()
    .asCoroutineDispatcher()



