package com.mgg.kotlinextensions.utils

import java.lang.ref.WeakReference
import java.util.concurrent.CancellationException
import kotlin.coroutines.intrinsics.intercepted
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

/**
Ref<T> uses the WeakReference under the hood
val ref: Ref<MyActivity> = this.asReference()
 */
@Suppress("unused")
fun <T : Any> T.asReference() = Ref(this)

@Suppress("unused")
class Ref<out T : Any> internal constructor(obj: T) {
    private val weakRef = WeakReference(obj)

    suspend operator fun invoke(): T {
        return suspendCoroutineUninterceptedOrReturn {
            it.intercepted()
            weakRef.get() ?: throw CancellationException()
        }
    }
}
