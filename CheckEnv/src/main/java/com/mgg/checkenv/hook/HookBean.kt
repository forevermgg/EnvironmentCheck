package com.mgg.checkenv.hook

import android.util.Log
import com.mgg.checkenv.module.BaseBean
import com.mgg.checkenv.module.BaseData
import org.json.JSONObject

/**
 * @author guxiaonian
 */
class HookBean : BaseBean() {
    /**
     * Xposed详细信息
     */
    var isHaveXposed: JSONObject? = null

    /**
     * Substrate详细信息
     */
    var isHaveSubstrate: JSONObject? = null

    /**
     * Frida详细信息
     */
    var isHaveFrida: JSONObject? = null
    public override fun toJSONObject(): JSONObject {
        try {
            jsonObject.put(BaseData.Hook.IS_HAVE_XPOSED, isHaveXposed)
            jsonObject.put(BaseData.Hook.IS_HAVE_SUBSTRATE, isHaveSubstrate)
            jsonObject.put(BaseData.Hook.IS_HAVE_FRIDA, isHaveFrida)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return super.toJSONObject()
    }

    class XposedBean : BaseBean() {
        /**
         * 包名检测
         */
        var isCheckXposedPackage = false

        /**
         * 检测调用栈中的可疑方法
         */
        var isCheckXposedHookMethod = false

        /**
         * 检测内存中可疑的jars
         */
        var isCheckXposedJars = false

        /**
         * 检测载入Xposed工具类
         */
        var isCheckClassLoader = false

        /**
         * 新增判断系统方法调用钩子
         */
        var isCheckNativeMethod = false

        /**
         * 虚拟检测Xposed环境
         */
        var isCheckSystem = false

        /**
         * 寻找Xposed运行库文件
         */
        var isCheckExecLib = false

        /**
         * 环境变量特征字判断
         */
        var isCheckXposedBridge = false
        private var cMap = false
        private var cPackage = false
        fun iscMap(): Boolean {
            return cMap
        }

        fun setcMap(cMap: Boolean) {
            this.cMap = cMap
        }

        fun iscPackage(): Boolean {
            return cPackage
        }

        fun setcPackage(cPackage: Boolean) {
            this.cPackage = cPackage
        }

        public override fun toJSONObject(): JSONObject {
            try {
                jsonObject.put(BaseData.Hook.Xposed.CHECK_XPOSED_PACKAGE, isCheckXposedPackage)
                jsonObject.put(
                    BaseData.Hook.Xposed.CHECK_XPOSED_HOOK_METHOD,
                    isCheckXposedHookMethod
                )
                jsonObject.put(BaseData.Hook.Xposed.CHECK_XPOSED_JARS, isCheckXposedJars)
                jsonObject.put(BaseData.Hook.Xposed.CHECK_CLASSLOADER, isCheckClassLoader)
                jsonObject.put(BaseData.Hook.Xposed.CHECK_NATIVE_METHOD, isCheckNativeMethod)
                jsonObject.put(BaseData.Hook.Xposed.CHECK_SYSTEM, isCheckSystem)
                jsonObject.put(BaseData.Hook.Xposed.CHECK_EXEC_LIB, isCheckExecLib)
                jsonObject.put(BaseData.Hook.Xposed.CHECK_XPOSED_BRIDGE, isCheckXposedBridge)
                jsonObject.put("cMap", cMap)
                jsonObject.put("cPackage", cPackage)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
            return super.toJSONObject()
        }
    }

    class SubstrateBean : BaseBean() {
        /**
         * 包名检测
         */
        var isCheckSubstratePackage = false

        /**
         * 检测调用栈中的可疑方法
         */
        var isCheckSubstrateHookMethod = false

        /**
         * 检测内存中可疑的jars
         */
        var isCheckSubstrateJars = false
        private var cSo = false
        private var cMap = false
        private var cPackage = false
        fun iscSo(): Boolean {
            return cSo
        }

        fun setcSo(cSo: Boolean) {
            this.cSo = cSo
        }

        fun iscMap(): Boolean {
            return cMap
        }

        fun setcMap(cMap: Boolean) {
            this.cMap = cMap
        }

        fun iscPackage(): Boolean {
            return cPackage
        }

        fun setcPackage(cPackage: Boolean) {
            this.cPackage = cPackage
        }

        public override fun toJSONObject(): JSONObject {
            try {
                jsonObject.put(
                    BaseData.Hook.Substrate.CHECK_SUBSTRATE_PACKAGE,
                    isCheckSubstratePackage
                )
                jsonObject.put(
                    BaseData.Hook.Substrate.CHECK_SUBSTRATE_HOOK_METHOD,
                    isCheckSubstrateHookMethod
                )
                jsonObject.put(BaseData.Hook.Substrate.CHECK_SUBSTRATE_JARS, isCheckSubstrateJars)
                jsonObject.put("cSo", cSo)
                jsonObject.put("cMap", cMap)
                jsonObject.put("cPackage", cPackage)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
            return super.toJSONObject()
        }
    }

    class FridaBean : BaseBean() {
        /**
         * 检测进程信息
         */
        var isCheckRunningProcesses = false

        /**
         * 检测内存中可疑的jars
         */
        var isCheckFridaJars = false
        private var cMap = false
        fun iscMap(): Boolean {
            return cMap
        }

        fun setcMap(cMap: Boolean) {
            this.cMap = cMap
        }

        public override fun toJSONObject(): JSONObject {
            try {
                jsonObject.put(BaseData.Hook.Frida.CHECK_RUNNING_PROCESSES, isCheckRunningProcesses)
                jsonObject.put(BaseData.Hook.Frida.CHECK_FRIDA_JARS, isCheckFridaJars)
                jsonObject.put("cMap", cMap)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
            return super.toJSONObject()
        }
    }

    companion object {
        private val TAG = HookBean::class.java.simpleName
    }
}
