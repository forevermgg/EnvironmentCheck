package com.mgg.checkenv.risk

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

fun Context.checkRiskAppInstalled(packageName: String?): Boolean {
    require(!(null == packageName || "" == packageName)) { "Package name cannot be null or empty !" }
    return try {
        val info: ApplicationInfo? = packageName?.let {
            packageManager.getApplicationInfo(
                it, PackageManager.GET_UNINSTALLED_PACKAGES
            )
        }
        null != info
    } catch (e: Exception) {
        false
    }
}
