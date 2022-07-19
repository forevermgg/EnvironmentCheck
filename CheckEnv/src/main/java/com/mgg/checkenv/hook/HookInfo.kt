package com.mgg.checkenv.hook

import com.mgg.checkenv.ContextProvider
import org.json.JSONObject

class HookInfo : CheckHook() {
    /**
     * 判断是否有xposed等hook工具
     *
     * @return
     */
    fun isXposedHook(): JSONObject {
        return getXposedHook(ContextProvider.get().context)
    }
}
