package com.mgg.environmentcheck

import kotlin.reflect.full.declaredFunctions

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class BindNativeSetPropFunctionAnnotation(val functionId: Int)

fun Any.kotlinHandleSetPropFunction(key: Int, value: String) {
    val kClass = this::class
    val declaredFunctions = kClass.declaredFunctions
    for (f in declaredFunctions) {
        f.annotations.forEach {
            if (it is BindNativeSetPropFunctionAnnotation) {
                val id = it.functionId
                if (id == key) {
                    f.call(this, value)
                }
            }
        }
    }
}