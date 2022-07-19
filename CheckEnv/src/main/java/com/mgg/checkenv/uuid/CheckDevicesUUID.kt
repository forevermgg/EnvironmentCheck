package com.mgg.checkenv.uuid

import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.provider.Settings
import com.mgg.checkenv.ContextProvider
import com.mgg.checkenv.utils.CommandUtil

// https://bbs.pediy.com/thread-270116.htm
open class CheckDevicesUUID {
    companion object {
        /*adb shell stat -f /                                                                                                                                                         ─╯
        File: "/"
        ID: 6d60597e30c8cf25 Namelen: 255    Type: unknown
        Block Size: 4096    Fundamental block size: 4096
        Blocks: Total: 517055   Free: 0 Available: 0
        Inodes: Total: 18446744073709551615     Free: 18446744073709551615*/
        @JvmStatic
        fun checkStatID(): String {
            val data: String = CommandUtil.getSingleInstance().exec("stat -f /")
            Log.e("CheckDevicesUUID", "result:$data")
            return if (TextUtils.isEmpty(data)) {
                ""
            } else {
                if (data.contains("ID: ") && data.contains(" Namelen")) {
                    val result = data.substring(data.indexOf("ID: ", 0), data.indexOf(" Namelen", 0))
                    result
                } else {
                    ""
                }
            }
        }

        @JvmStatic
        fun checkAndroidID(): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Settings.System.getString(
                    ContextProvider.get().context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            } else {
                Settings.System.getString(
                    ContextProvider.get().context.contentResolver,
                    Settings.System.ANDROID_ID
                )
            }
        }

        @JvmStatic
        fun checkFonts(): String {
            return CommandUtil.getSingleInstance().exec(
                "ls /system/fonts"
            )
        }

        // adb 运行 ：ip address show wlan0 可以
        // 代码执行不行
        @JvmStatic
        fun getLinkEther(): String {
            val data: String = CommandUtil.getSingleInstance().exec("ip address show wlan0")
            Log.e("CheckDevicesUUID", "link/ether:$data")
            return if (TextUtils.isEmpty(data)) {
                ""
            } else {
                if (data.contains("link/ether") && data.contains("brd")) {
                    val result = data.substring(data.indexOf("link/ether", 0), data.indexOf("brd", 0))
                    result
                } else {
                    ""
                }
            }
        }

        @JvmStatic
        fun toStringChecked(): String {
            val checkDevicesUUID = CheckDevicesUUID()
            return "CheckDevicesUUID() " +
                    "\n checkStatID:" + checkStatID() +
                    "\n checkAndroidID:" + checkAndroidID() +
                    "\n checkRandomBootId:" + checkDevicesUUID.nativeCheckRandomBootId() +
                    "\n checkRandomUUId:" + checkDevicesUUID.nativeCheckRandomUUId() +
                    "\n getLinkEther:" + getLinkEther()
        }
    }

    external fun nativeCheckRandomBootId(): String
    external fun nativeCheckRandomUUId(): String
}
