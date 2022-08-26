package com.mgg.kotlinextensions.utils

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun awaitAll(vararg blocks: suspend () -> Unit) = coroutineScope {
    blocks.forEach {
        launch {
            it()
        }
    }
}