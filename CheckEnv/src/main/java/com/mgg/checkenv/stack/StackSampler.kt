package com.mgg.checkenv.stack

import java.lang.Thread
import java.lang.StringBuilder

object StackSampler {
    fun getStackInfo(thread: Thread): String {
        val stringBuilder = StringBuilder()
        for (stackTraceElement in thread.stackTrace) {
            stringBuilder
                .append(stackTraceElement.toString()).append("\n")
        }
        val result = stringBuilder.toString()
        return result.substring(0, result.length - 1)
    }
}
