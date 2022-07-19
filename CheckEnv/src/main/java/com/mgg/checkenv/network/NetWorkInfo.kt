package com.mgg.checkenv.network

import com.mgg.checkenv.ContextProvider
import org.json.JSONObject

class NetWorkInfo : CheckNetWork() {
    fun mobGetMobNetWork(): JSONObject {
        return getMobNetWork(ContextProvider.get().context)
    }
}
