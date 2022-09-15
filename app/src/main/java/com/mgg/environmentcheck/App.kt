package com.mgg.environmentcheck

import android.app.Application
import android.content.Context
import android.os.Build
import com.hjq.toast.ToastUtils
import com.mgg.core.CoreApplication
import org.lsposed.hiddenapibypass.HiddenApiBypass

class App : CoreApplication() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApiBypass.addHiddenApiExemptions("")
        }
        app = this
        QtNative.setClassLoader(this.classLoader)
        System.loadLibrary("environmentcheck")
        // 初始化 Toast 框架
        ToastUtils.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    companion object {
        //noinspection StaticFieldLeak
        lateinit var app: Application
    }
}
