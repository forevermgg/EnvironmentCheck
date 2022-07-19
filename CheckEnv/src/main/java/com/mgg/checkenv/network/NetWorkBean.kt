package com.mgg.checkenv.network

import android.util.Log
import com.mgg.checkenv.module.BaseBean
import com.mgg.checkenv.module.BaseData
import org.json.JSONObject

open class NetWorkBean : BaseBean() {
    /**
     * 网络类型
     */
    var type: String? = null

    /**
     * 网络是否可用
     */
    var isNetworkAvailable = false

    /**
     * 是否开启数据流量
     */
    var isHaveIntent = false

    /**
     * 是否是飞行模式
     */
    var isFlightMode = false

    /**
     * NFC功能是否开启
     */
    var isNFCEnabled = false

    /**
     * 是否开启热点
     */
    var isHotspotEnabled = false

    /**
     * 热点账号
     */
    var hotspotSSID: String? = null

    /**
     * 热点密码
     */
    var hotspotPwd: String? = null

    /**
     * 热点加密类型
     */
    var encryptionType: String? = null
    var isVpn = false
    public override fun toJSONObject(): JSONObject {
        try {
            jsonObject.put(BaseData.NetWork.TYPE, isEmpty(type))
            jsonObject.put(BaseData.NetWork.NETWORK_AVAILABLE, isNetworkAvailable)
            jsonObject.put(BaseData.NetWork.HAVE_INTENT, isHaveIntent)
            jsonObject.put(BaseData.NetWork.IS_FLIGHT_MODE, isFlightMode)
            jsonObject.put(BaseData.NetWork.IS_NFC_ENABLED, isNFCEnabled)
            jsonObject.put(BaseData.NetWork.IS_HOTSPOT_ENABLED, isHotspotEnabled)
            jsonObject.put(BaseData.NetWork.HOTSPOT_SSID, isEmpty(hotspotSSID))
            jsonObject.put(BaseData.NetWork.HOTSPOT_PWD, isEmpty(hotspotPwd))
            jsonObject.put(BaseData.NetWork.ENCRYPTION_TYPE, isEmpty(encryptionType))
            jsonObject.put("isVpn", isVpn)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return super.toJSONObject()
    }

    companion object {
        private val TAG = NetWorkBean::class.java.simpleName
    }
}
