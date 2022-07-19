package com.mgg.checkenv.debug

import android.content.Context
import android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE
import android.os.Debug
import android.provider.Settings
import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

open class CheckDebug {
    companion object {
        private const val TRACERPID = "TracerPid"
        private val TAG = CheckDebug::class.java.simpleName
    }

    /**
     * 检测app是否为debug版本
     *
     * @param context
     * @return
     */
    fun checkIsDebugVersion(context: Context?): Boolean {
        requireNotNull(context) {
            "context is null"
        }
        return (context.applicationInfo != null
                && context.applicationInfo.flags and FLAG_DEBUGGABLE != 0)
    }

    /**
     * java法检测是否连上调试器
     * @return
     */
    fun isBeingDebugged(): Boolean {
        return Debug.isDebuggerConnected()
    }

    /**
     * 开启了调试模式
     *
     * @param context
     * @return
     */
    private fun isOpenDebug(context: Context?): Boolean {
        try {
            requireNotNull(context) {
                "context is null"
            }
            // @Suppress("deprecation")
            return Settings.Secure.getInt(
                context.contentResolver,
                Settings.Global.ADB_ENABLED,
                0
            ) > 0
        } catch (e: java.lang.Exception) {
        }
        return false
    }

    /**
     * This is used by Alibaba to detect someone ptracing the application.
     *
     * Easy to circumvent, the usage ITW was a native thread constantly doing this every three seconds - and would cause
     * the application to crash if it was detected.
     *
     * @return
     */
    fun isReadTracerPid(): Boolean {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(InputStreamReader(FileInputStream("/proc/self/status")), 1000)
            var line: String
            while (reader.readLine().also { line = it } != null) {
                if (line.length > TRACERPID.length) {
                    if (line.substring(0, TRACERPID.length).equals(TRACERPID, ignoreCase = true)) {
                        if (Integer.decode(
                                line.substring(TRACERPID.length + 1).trim { it <= ' ' }) > 0
                        ) {
                            return true
                        }
                        break
                    }
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        } finally {
            reader?.close()
        }
        return false
    }

    /**
     * debug
     */
    external fun nativeReadTracerPid(): Boolean

    /**
     * 调试模式判断
     *
     * @return
     */
    fun getDebugData(context: Context?): JSONObject {
        val debugBean = DebugBean()
        try {
            debugBean.setOpenDebug(isOpenDebug(context))
            debugBean.setDebugVersion(checkIsDebugVersion(context))
            debugBean.setDebugging(isBeingDebugged())
            debugBean.setReadProcStatus(isReadTracerPid() || nativeReadTracerPid())
        } catch (e: java.lang.Exception) {
            Log.e(TAG, e.toString())
        }
        return debugBean.toJSONObject()
    }
}
