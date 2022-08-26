package com.mgg.kotlinextensions.view

import android.view.View
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner

fun View.getViewTreeLog() = buildString {
    append("ViewTreeLifecycleOwner : ${findViewTreeLifecycleOwner()}")
    append(System.lineSeparator())
    append("ViewTreeViewModelStoreOwner : ${findViewTreeViewModelStoreOwner()}")
}

fun View.getViewTreeOwnerLog() = "ViewTreeLifecycleOwner : ${findViewTreeLifecycleOwner()}"