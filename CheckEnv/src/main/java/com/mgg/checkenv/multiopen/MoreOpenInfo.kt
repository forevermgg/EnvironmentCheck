package com.mgg.checkenv.multiopen

import com.mgg.checkenv.ContextProvider
import org.json.JSONObject

class MoreOpenInfo : CheckMultiOpen() {
    fun checkVirtual(): JSONObject? {
        return checkVirtualInfo(ContextProvider.get().context)
    }
}
