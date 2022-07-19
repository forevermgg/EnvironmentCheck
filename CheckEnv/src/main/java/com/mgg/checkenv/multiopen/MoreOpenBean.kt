package com.mgg.checkenv.multiopen

import android.util.Log
import com.mgg.checkenv.module.BaseBean
import com.mgg.checkenv.module.BaseData
import org.json.JSONObject

open class MoreOpenBean : BaseBean() {
    /**
     * 检测私有路径判断是否有多开
     */
    var isCheckByPrivateFilePath = false
    var isCheckLs = false
    var isSystemDualApp = false
    var isUserDualApp = false
    public override fun toJSONObject(): JSONObject {
        try {
            jsonObject.put(BaseData.MoreOpen.CHECK_BY_PRIVATE_FILE_PATH, isCheckByPrivateFilePath)
            jsonObject.put(BaseData.MoreOpen.CHECK_BY_SHELL_COMMAND, isCheckLs)
            jsonObject.put(BaseData.MoreOpen.IS_SYSTEM_DUAL_APP, isSystemDualApp)
            jsonObject.put(BaseData.MoreOpen.IS_USER_DUAL_APP, isUserDualApp)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return super.toJSONObject()
    }

    companion object {
        private val TAG = MoreOpenBean::class.java.simpleName
    }
}
