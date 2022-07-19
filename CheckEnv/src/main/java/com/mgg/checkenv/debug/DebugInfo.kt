package com.mgg.checkenv.debug

import com.mgg.checkenv.ContextProvider
import org.json.JSONObject

class DebugInfo : CheckDebug() {
    /**
     * 调试模式判断
     *
     * @return
     */
    fun getDebuggingData(): JSONObject {
        return getDebugData(ContextProvider.get().context)
    }
}
