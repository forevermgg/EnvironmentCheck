package com.mgg.environmentcheck

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleObserver
import timber.log.Timber

class TestView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs), LifecycleObserver , MVVMView<TestView> {
    init {
        LayoutInflater.from(context).inflate(R.layout.item_letter, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        Timber.tag("mgg").e("LifecycleWindow :onFinishInflate")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Timber.tag("mgg").e("LifecycleWindow :onAttachedToWindow%s", this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Timber.tag("mgg").e("LifecycleWindow :onDetachedFromWindow%s", this)
    }


    fun onCreate() {
        Timber.tag("mgg").e("LifecycleWindow :onCreate%s", this)
    }

    fun onDestroy() {
        Timber.tag("mgg").e("LifecycleWindow :onDestroy%s", this)
    }

    override fun viewType(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TestView) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}