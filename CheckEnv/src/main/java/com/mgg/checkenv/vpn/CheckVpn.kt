package com.mgg.checkenv.vpn

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

@SuppressLint("MissingPermission")
fun Context.isUsingVpn(): Boolean? {
    var vpnInUse = false
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val activeNetwork = connectivityManager?.activeNetwork
        val caps = connectivityManager?.getNetworkCapabilities(activeNetwork)
        return caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    } else {
        @Suppress("deprecation")
        val networks = connectivityManager?.allNetworks
        networks?.let {
            for (i in networks.indices) {
                val caps = connectivityManager.getNetworkCapabilities(networks[i])
                if (caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true) {
                    vpnInUse = true
                    break
                }
            }
        }
    }
    return vpnInUse
}
