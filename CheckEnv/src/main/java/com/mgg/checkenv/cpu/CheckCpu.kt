package com.mgg.checkenv.cpu

import android.os.Build
import android.util.Log
import com.mgg.checkenv.module.CommandResult
import com.mgg.checkenv.utils.ShellUtils
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*

open class CheckCpu {
    companion object {
        private val TAG = CheckCpu::class.java.simpleName
    }

    /**
     * 查看 CPU 信息 [不需要 root 权限]
     * // https://github.com/camelsmith/CamelUtilsLibrary/blob/e8dbdf82ba7de45cd28726e7b03ff09b542a1aa9/utils/src/main/java/org/camel/utilslibrary/ADBUtils.java
     * @return 命令执行结果
     * https://github.com/CoreELEC/xbmc/blob/05f406812c7cc46a58e52810dd8ff25740e78cc1/xbmc/addons/addoninfo/AddonInfoBuilder.cpp
     */
/* m1 目前arm64
adb shell cat /proc/cpuinfo                                                                                                                                                 ─╯
processor       : 0
BogoMIPS        : 48.00
Features        : fp asimd evtstrm aes pmull sha1 sha2 crc32 atomics fphp asimdhp
cpuid asimdrdm jscvt fcma lrcpc dcpop sha3 asimddp sha512 asimdfhm dit uscat
ilrcpc flagm ssbs sb paca pacg dcpodp flagm2 frint
CPU implementer : 0x00
CPU architecture: 8
CPU variant     : 0x0
CPU part        : 0x000
CPU revision    : 0
*/
    fun showCpuInfo(): CommandResult? {
        return ShellUtils.execCmd("cat /proc/cpuinfo", false)
    }

    external fun nativeCheckCpuAbi(): String

    private fun readCpuInfo(): String {
        var result = ""
        try {
            val args = arrayOf("/system/bin/cat", "/proc/cpuinfo")
            val cmd = ProcessBuilder(*args)
            val process = cmd.start()
            val sb = StringBuffer()
            var readLine: String?
            val responseReader =
                BufferedReader(InputStreamReader(process.inputStream, StandardCharsets.UTF_8))
            while (responseReader.readLine().also { readLine = it } != null) {
                sb.append(readLine)
            }
            responseReader.close()
            result = sb.toString().lowercase(Locale.getDefault())
        } catch (ex: IOException) {
            Log.e(TAG, ex.toString())
        }
        return result
    }

    fun getAvailableProcessors(): Int {
        return Runtime.getRuntime().availableProcessors()
    }

    external fun getCpuCores(): Int

    open fun getCpuInfo(): JSONObject? {
        Log.e(TAG, showCpuInfo().toString())
        Log.e(TAG, readCpuInfo())
        val cpuBean = CpuBean()
        try {
            cpuBean.cpuAbi = putCpuAbi()
            cpuBean.cpuCores = getCpuCores()
            cpuBean.availableProcessors = getAvailableProcessors()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return cpuBean.toJSONObject()
    }

    private fun putCpuAbi(): String? {
        val abis: Array<String> =
            Build.SUPPORTED_ABIS
        val stringBuilder = StringBuilder()
        for (abi in abis) {
            stringBuilder.append(abi)
            stringBuilder.append(",")
        }
        try {
            return stringBuilder.toString().substring(0, stringBuilder.toString().length - 1)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return null
    }
}
