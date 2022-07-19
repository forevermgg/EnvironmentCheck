package com.mgg.environmentcheck

import android.content.pm.PackageManager

object SystemServices {
    val packageManager: PackageManager by lazy { App.app.packageManager }
}
