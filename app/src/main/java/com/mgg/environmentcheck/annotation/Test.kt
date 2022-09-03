package com.mgg.environmentcheck.annotation

import com.mgg.environmentcheck.BindNativeSetPropFunctionAnnotation
import timber.log.Timber

class Test : BaseTest() {

    @BindNativeSetPropFunctionAnnotation(functionId = 1)
    fun test1(message: String) {
        Timber.e("BindNativeSetPropFunctionAnnotation exe 1 $message")
    }

    @BindNativeSetPropFunctionAnnotation(functionId = 2)
    fun test2(message: String) {
        Timber.e("BindNativeSetPropFunctionAnnotation exe 2 $message")
    }
}