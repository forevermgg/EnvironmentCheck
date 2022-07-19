package com.mgg.checkenv.debug

import android.util.Log
import com.mgg.checkenv.module.BaseBean
import com.mgg.checkenv.module.BaseData
import org.json.JSONObject

class DebugBean : BaseBean() {
    private val TAG = DebugBean::class.java.simpleName

    /**
     * 是否开启了调试模式
     */
    private var isOpenDebug = false

    /**
     * 是否是Debug版本
     */
    private var isDebugVersion = false

    /**
     * 是否正在调试
     */
    private var isDebugging = false

    /**
     * 读取id判断是否在调试
     */
    private var isReadProcStatus = false

    fun isOpenDebug(): Boolean {
        return isOpenDebug
    }

    fun setOpenDebug(openDebug: Boolean) {
        isOpenDebug = openDebug
    }

    fun isDebugVersion(): Boolean {
        return isDebugVersion
    }

    fun setDebugVersion(debugVersion: Boolean) {
        isDebugVersion = debugVersion
    }

    fun isDebugging(): Boolean {
        return isDebugging
    }

    fun setDebugging(debugging: Boolean) {
        isDebugging = debugging
    }

    fun isReadProcStatus(): Boolean {
        return isReadProcStatus
    }

    fun setReadProcStatus(readProcStatus: Boolean) {
        isReadProcStatus = readProcStatus
    }


    public override fun toJSONObject(): JSONObject {
        try {
            jsonObject.put(BaseData.Debug.IS_OPEN_DEBUG, isOpenDebug)
            jsonObject.put(BaseData.Debug.IS_DEBUG_VERSION, isDebugVersion)
            jsonObject.put(BaseData.Debug.IS_DEBUGGING, isDebugging)
            jsonObject.put(BaseData.Debug.IS_READ_PROC_STATUS, isReadProcStatus)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return super.toJSONObject()
    }
}
