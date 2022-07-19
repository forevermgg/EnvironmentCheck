package com.mgg.core

import android.app.Application
import com.jakewharton.processphoenix.ProcessPhoenix
import com.mgg.core.timber.ThreadAwareDebugTree
import jonathanfinerty.once.Once
import timber.log.Timber

open class CoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (ProcessPhoenix.isPhoenixProcess(this)) {
            return
        }
        Once.initialise(this)
        Timber.plant(ThreadAwareDebugTree())
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}
