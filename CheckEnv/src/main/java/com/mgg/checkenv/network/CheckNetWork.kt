@file:Suppress("DEPRECATION")

package com.mgg.checkenv.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.nfc.NfcManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import com.mgg.checkenv.module.BaseData.Companion.UNKNOWN_PARAM
import org.json.JSONObject

open class CheckNetWork {
    companion object {
        private val TAG = CheckNetWork::class.java.simpleName
    }

    fun getMobNetWork(context: Context): JSONObject {
        val netWorkBean = NetWorkBean()
        try {
            netWorkBean.type = networkTypeALL(context)
            netWorkBean.isNetworkAvailable = isNetworkAvailable(context)
            netWorkBean.isHaveIntent = haveIntent(context)
            netWorkBean.isFlightMode = getAirplaneMode(context)
            netWorkBean.isNFCEnabled = hasNfc(context)
            netWorkBean.isHotspotEnabled = isWifiApEnabled(context)
            val mWifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
                    ?: return netWorkBean.toJSONObject()
            val config = getHotPotConfig(mWifiManager) ?: return netWorkBean.toJSONObject()
            netWorkBean.hotspotSSID = config.SSID
            netWorkBean.hotspotPwd = config.preSharedKey
            netWorkBean.encryptionType = if (config.allowedKeyManagement[4]) "WPA2_PSK" else "NONE"
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return netWorkBean.toJSONObject()
    }

    private fun getAirplaneMode(context: Context): Boolean {
        val isAirplaneMode = Settings.System.getInt(
            context.contentResolver,
            Settings.System.AIRPLANE_MODE_ON, 0
        )
        return isAirplaneMode == 1
    }

    private fun hasNfc(context: Context?): Boolean {
        var bRet = false
        if (context == null) {
            return false
        }
        val manager = context.getSystemService(Context.NFC_SERVICE) as? NfcManager
            ?: return false
        val adapter = manager.defaultAdapter
        if (adapter != null && adapter.isEnabled) {
            // adapter存在，能启用
            bRet = true
        }
        return bRet
    }

    /**
     * 热点开关是否打开
     */
    private fun isWifiApEnabled(context: Context): Boolean {
        try {
            val mWifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val method = mWifiManager.javaClass.getMethod("isWifiApEnabled")
            method.isAccessible = true
            return method.invoke(mWifiManager) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 获取当前热点配置
     *
     * @return
     */
    private fun getHotPotConfig(mWifiManager: WifiManager): WifiConfiguration? {
        return try {
            @SuppressLint("PrivateApi") val method =
                WifiManager::class.java.getDeclaredMethod("getWifiApConfiguration")
            method.isAccessible = true
            method.invoke(mWifiManager) as? WifiConfiguration
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 是否有数据网络接入
     *
     * @param context
     * @return
     */
    private fun haveIntent(context: Context): Boolean {
        var mobileDataEnabled = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return false
        try {
            val cmClass = Class.forName(cm.javaClass.name)
            val method = cmClass.getDeclaredMethod("getMobileDataEnabled")
            method.isAccessible = true
            // get the setting for "mobile data"
            mobileDataEnabled = method.invoke(cm) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mobileDataEnabled
    }

    @SuppressLint("MissingPermission")
    private fun getVpnData(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return false
            cm.getNetworkInfo(ConnectivityManager.TYPE_VPN)!!.isConnectedOrConnecting
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 拿到具体的网络类型
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    private fun networkTypeALL(context: Context): String {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        if (manager != null) {
            val networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
                return "WIFI"
            }
        }
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
                ?: return "unknown"
        return when (telephonyManager.networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> "2G"
            TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"
            TelephonyManager.NETWORK_TYPE_LTE -> "4G"
            else -> UNKNOWN_PARAM
        }
    }

    /**
     * 网络是否可用
     */
    @SuppressLint("MissingPermission")
    private fun isNetworkAvailable(context: Context): Boolean {
        try {
            val mgr = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return false
            val networks = mgr.allNetworks
            var networkInfo: NetworkInfo?
            for (mNetwork in networks) {
                networkInfo = mgr.getNetworkInfo(mNetwork)
                if (networkInfo!!.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        } catch (e: Exception) {
            return true
        }
        return false
    }
}
