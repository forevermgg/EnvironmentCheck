package com.mgg.checkenv.cpu

import org.json.JSONObject

class CpuInfo : CheckCpu() {
    /**
     * CPU
     * @return
     */
    fun mobGetCpuInfo(): JSONObject? {
        return getCpuInfo()
    }
}
