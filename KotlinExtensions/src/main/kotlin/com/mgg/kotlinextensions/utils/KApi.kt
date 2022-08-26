package com.mgg.kotlinextensions.utils

import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES

fun isApi(api: Int) = VERSION.SDK_INT == api

fun isApiOrUp(api: Int) = VERSION.SDK_INT >= api

fun isLollipop() = isApi(VERSION_CODES.LOLLIPOP) // 21
fun isMarshmallow() = isApi(VERSION_CODES.M) // 23
fun isNougat() = isApi(VERSION_CODES.N)  // 24
fun isOreo() = isApi(VERSION_CODES.O) // 26
fun isP() = isApi(VERSION_CODES.P)  // 28
fun isQ() = isApi(VERSION_CODES.Q)  // 29
fun isR() = isApi(VERSION_CODES.R)  // 30
fun isS() = isApi(VERSION_CODES.S)  // 31
fun isSV2() = isApi(VERSION_CODES.S_V2)  // 32

fun isLollipopOrUp() = isApiOrUp(VERSION_CODES.LOLLIPOP)
fun isMarshmallowOrUp() = isApiOrUp(VERSION_CODES.M)
fun isNougatOrUp() = isApiOrUp(VERSION_CODES.N)
fun isOreoOrUp() = isApiOrUp(VERSION_CODES.O)
fun isSnowConeOrUp() = isApiOrUp(Build.VERSION_CODES.R)

inline fun aboveApi(api: Int, included: Boolean = false, functionBlock: () -> Unit) {
    if (Build.VERSION.SDK_INT > if (included) api - 1 else api) {
        functionBlock()
    }
}

inline fun belowApi(api: Int, included: Boolean = false, functionBlock: () -> Unit) {
    if (Build.VERSION.SDK_INT < if (included) api + 1 else api) {
        functionBlock()
    }
}