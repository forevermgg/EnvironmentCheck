package com.mgg.checkenv.hook

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Process
import android.text.TextUtils
import android.util.Log
import com.mgg.checkenv.hook.HookBean.*
import com.mgg.checkenv.utils.CommandUtil
import org.json.JSONObject
import java.io.BufferedReader
import java.io.FileReader
import java.lang.reflect.Modifier
import java.util.*

/**
 * https://song-dev.github.io/2019/12/29/hook-check/
 */
open class CheckHook {

    companion object {
        private val TAG = HookInfo::class.java.simpleName
    }

    /**
     * 判断是否有xposed等hook工具
     *
     * @param context
     * @return
     */
    fun getXposedHook(context: Context): JSONObject {
        val hookBean = HookBean()
        val xposedBean = XposedBean()
        val substrateBean = SubstrateBean()
        val fridaBean = FridaBean()
        try {
            chargeXposedPackage(context, xposedBean, substrateBean)
            chargeXposedHookMethod(xposedBean, substrateBean)
            chargeXposedJars(xposedBean, substrateBean, fridaBean)
            fridaBean.isCheckRunningProcesses = checkRunningProcesses(context)
            addMethod(xposedBean)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        substrateBean.setcSo(checkSubstrateBySo() == 1)
        val mapData: String = checkHookByMap()
        val packageData: String = checkHookByPackage()
        if (!TextUtils.isEmpty(mapData)) {
            if (mapData.contains("xposed")) {
                xposedBean.setcMap(true)
            }
            if (mapData.contains("frida")) {
                fridaBean.setcMap(true)
            }
            if (mapData.contains("substrate")) {
                substrateBean.setcMap(true)
            }
        }
        if (!TextUtils.isEmpty(packageData)) {
            if (packageData.contains("xposed")) {
                xposedBean.setcPackage(true)
            }
            if (packageData.contains("substrate")) {
                substrateBean.setcPackage(true)
            }
        }
        hookBean.isHaveXposed = xposedBean.toJSONObject()
        hookBean.isHaveSubstrate = substrateBean.toJSONObject()
        hookBean.isHaveFrida = fridaBean.toJSONObject()
        return hookBean.toJSONObject()
    }

    /**
     * Android 32 获取不到
     */
    private fun checkRunningProcesses(context: Context): Boolean {
        var returnValue = false
        // Get currently running application processes
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        @Suppress("deprecation")
        val list = activityManager.getRunningServices(300)
        if (list != null) { // return 0
            var tempName: String
            for (i in list.indices) {
                tempName = list[i].process
                if (tempName.contains("fridaserver")) {
                    returnValue = true
                }
            }
        }
        return returnValue
    }

    /**
     * 新增方法
     *
     * 是否安装了Xposed
     */
    private fun addMethod(xposedBean: XposedBean) {
        xposedBean.isCheckClassLoader = testClassLoader()
        xposedBean.isCheckNativeMethod = checkNativeMethod()
        xposedBean.isCheckSystem = checkSystem()
        xposedBean.isCheckExecLib = checkExecLib()
        xposedBean.isCheckXposedBridge = checkXposedBridge() == true
    }

    /**
     * 新增检测载入Xposed工具类
     * 方法参考自<url>https://github.com/w568w/XposedChecker/</url>
     *
     * @return 是否安装了Xposed
     */
    private fun testClassLoader(): Boolean {
        try {
            Class.forName("de.robv.android.xposed.XC_MethodHook")
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            ClassLoader.getSystemClassLoader()
                .loadClass("de.robv.android.xposed.XposedHelpers")
            return true
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 新增判断系统方法调用钩子
     * 方法参考自<url>https://github.com/w568w/XposedChecker/</url>
     *
     * @return 是否安装了Xposed
     */
    private fun checkNativeMethod(): Boolean {
        try {
            val method = Throwable::class.java.getDeclaredMethod("getStackTrace")
            return Modifier.isNative(method.modifiers)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 新增虚拟检测Xposed环境
     * 方法参考自<url>https://github.com/w568w/XposedChecker/</url>
     *
     * @return 是否安装了Xposed
     */
    private fun checkSystem(): Boolean {
        return System.getProperty("vxp") != null
    }

    /**
     * 寻找Xposed运行库文件
     * 方法参考自<url>https://github.com/w568w/XposedChecker/</url>
     *
     * @return 是否安装了Xposed
     */
    private fun checkExecLib(): Boolean {
        val result: String = CommandUtil.getSingleInstance().exec("ls /system/lib")
        return if (TextUtils.isEmpty(result)) {
            false
        } else result.contains("xposed")
    }

    /**
     * 环境变量特征字判断
     * 方法参考自<url>https://github.com/w568w/XposedChecker/</url>
     *
     * @return 是否安装了Xposed
     */
    private fun checkXposedBridge(): Boolean? {
        val result = System.getenv("CLASSPATH")
        return if (TextUtils.isEmpty(result)) {
            false
        } else result?.contains("XposedBridge")
    }

    /**
     * 检查包名是否存在
     *
     * @param context
     * @return
     */
    @SuppressLint("QueryPermissionsNeeded")
    private fun chargeXposedPackage(
        context: Context,
        xposedBean: XposedBean,
        substrateBean: SubstrateBean
    ) {
        val packageManager = context.applicationContext.packageManager
        val applicationInfoList =
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (item in applicationInfoList) {
            //新增包名检测 方法参考自<url>https://github.com/w568w/XposedChecker/</url>
            if ("de.robv.android.xposed.installer" == item.packageName || "io.va.exposed" == item.packageName) {
                xposedBean.isCheckXposedPackage = true
            }
            if ("com.saurik.substrate" == item.packageName) {
                substrateBean.isCheckSubstratePackage = true
            }
        }
    }

    /**
     * 检测调用栈中的可疑方法
     */
    private fun chargeXposedHookMethod(xposedBean: XposedBean, substrateBean: SubstrateBean) {
        try {
            throw Exception("Deteck hook")
        } catch (e: Exception) {
            var zygoteInitCallCount = 0
            for (item in e.stackTrace) {
                // 检测"com.android.internal.os.ZygoteInit"是否出现两次，如果出现两次，则表明Substrate框架已经安装
                if ("com.android.internal.os.ZygoteInit" == item.className) {
                    zygoteInitCallCount++
                    if (zygoteInitCallCount == 2) {
                        substrateBean.isCheckSubstrateHookMethod = true
                    }
                }
                if ("com.saurik.substrate.MS$2" == item.className && "invoke" == item.methodName) {
                    substrateBean.isCheckSubstrateHookMethod = true
                }
                if ("de.robv.android.xposed.XposedBridge" == item.className && "main" == item.methodName) {
                    xposedBean.isCheckXposedHookMethod = true
                }
                if ("de.robv.android.xposed.XposedBridge" == item.className && "handleHookedMethod" == item.methodName) {
                    xposedBean.isCheckXposedHookMethod = true
                }
            }
        }
    }

    /**
     * 检测内存中可疑的jars
     */
    private fun chargeXposedJars(
        xposedBean: XposedBean,
        substrateBean: SubstrateBean,
        fridaBean: FridaBean
    ) {
        val libraries: MutableSet<String> = HashSet()
        val mapsFilename = "/proc/" + Process.myPid() + "/maps"
        try {
            val reader = BufferedReader(FileReader(mapsFilename))
            var line: String
            while (reader.readLine().also { line = it } != null) {
                if (line.lowercase(Locale.getDefault()).contains("frida")) {
                    fridaBean.isCheckFridaJars = true
                }
                if (line.endsWith(".so") || line.endsWith(".jar")) {
                    val n = line.lastIndexOf(" ")
                    libraries.add(line.substring(n + 1))
                }
            }
            for (library in libraries) {
                if (library.contains("com.saurik.substrate")) {
                    substrateBean.isCheckSubstrateJars = true
                }
                if (library.contains("XposedBridge.jar")) {
                    xposedBean.isCheckXposedJars = true
                }
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        for (library in libraries) {
            Log.e("library", library)
        }
    }

    private external fun checkSubstrateBySo(): Int
    private external fun checkHookByMap(): String
    private external fun checkHookByPackage(): String
}
