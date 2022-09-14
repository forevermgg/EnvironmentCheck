package com.mgg.environmentcheck.annotation

import android.content.Context

internal interface IProvider {
    /**
     * Do your init work in this method, it well be call when processor has been load.
     *
     * @param context ctx
     */
    fun init(context: Context)
}