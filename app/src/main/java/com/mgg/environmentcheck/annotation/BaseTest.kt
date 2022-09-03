package com.mgg.environmentcheck.annotation

import com.mgg.environmentcheck.kotlinHandleSetPropFunction

open class BaseTest() {
    fun handle(key: Int, value: String) {
        this.kotlinHandleSetPropFunction(key, value)
    }
}