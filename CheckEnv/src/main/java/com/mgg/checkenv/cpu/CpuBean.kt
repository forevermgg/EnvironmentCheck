package com.mgg.checkenv.cpu

import android.util.Log
import com.mgg.checkenv.module.BaseBean
import com.mgg.checkenv.module.BaseData
import org.json.JSONObject

class CpuBean : BaseBean() {

    /**
     * CPU核数
     */
    var cpuCores = 0
    var availableProcessors = 0
    /**
     * CPU架构
     */
    var cpuAbi: String? = null
    public override fun toJSONObject(): JSONObject {
        try {
            jsonObject.put(BaseData.Cpu.CPU_CORES, cpuCores)
            jsonObject.put(BaseData.Cpu.CPU_ABI, isEmpty(cpuAbi))
            jsonObject.put(BaseData.Cpu.AVALIABLE_PROCESSORS, availableProcessors)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return super.toJSONObject()
    }

    companion object {
        private val TAG = CpuBean::class.java.simpleName
    }
}
