package com.mgg.checkenv

import android.app.Application
import android.content.Context
import com.mgg.checkenv.callback.EmulatorCheckCallback
import com.mgg.checkenv.cpu.CpuInfo
import com.mgg.checkenv.debug.CheckDebug
import com.mgg.checkenv.debug.DebugInfo
import com.mgg.checkenv.emulator.CheckEmulator
import com.mgg.checkenv.hook.HookInfo
import com.mgg.checkenv.monkey.CheckMonkey
import com.mgg.checkenv.multiopen.MoreOpenInfo
import com.mgg.checkenv.network.NetWorkInfo
import com.mgg.checkenv.risk.checkRiskAppInstalled
import com.mgg.checkenv.root.CheckRoot
import com.mgg.checkenv.stack.StackSampler
import com.mgg.checkenv.vpn.isUsingVpn
import org.json.JSONObject

class CheckEnv {
    private object SingletonHolder {
        val INSTANCE: CheckEnv = CheckEnv()
    }

    /**
     * A native method that is implemented by the 'checkenv' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'checkenv' library on application startup.
        init {
            System.loadLibrary("checkenv")
        }

        fun getSingleInstance() = SingletonHolder.INSTANCE

        /**
         * 全局上下文
         */
        private var mApplication: Application? = null

        /**
         * 设置全局上下文 默认使用MobInitializer来进行初始化
         * 可以自行修改
         *
         * @param application 上下文
         */
        fun init(application: Application?) {
            mApplication = application
        }

        /**
         * 获取全局上下文
         *
         * @return 上下文
         */
        fun getContext(): Context? {
            val context: Context? = if (mApplication != null) {
                mApplication?.applicationContext
            } else {
                ContextProvider.get().context
            }
            return context
        }

        fun getApplication(): Application? {
            val application: Application? = if (mApplication != null) {
                mApplication
            } else {
                ContextProvider.get().application
            }
            return application
        }

        /**
         * 判断debug的信息
         */
        fun getDebugInfo(): JSONObject {
            return DebugInfo().getDebugData(getContext())
        }

        /**
         * 手机CPU信息
         */
        fun getCpuInfo(): JSONObject? {
            // checkWorkThread("currentThread is main, please use work Thread getCpuInfo")
            return CpuInfo().mobGetCpuInfo()
        }

        /**
         * 判断多开软件的信息
         */
        fun getMoreOpenInfo(): JSONObject? {
            return MoreOpenInfo().checkVirtual()
        }


        /**
         * 手机是否root
         */
        fun isRoot(): Boolean {
            return CheckRoot.checkIsRoot() ||
                    CheckRoot.nativeCheckIsRoot()
        }

        /**
         * 手机基本信息 netWork
         */
        fun getNetWorkInfo(): JSONObject {
            return NetWorkInfo().mobGetMobNetWork()
        }

        /**
         * 判断是否包含Hook工具
         */
        fun getHookInfo(): JSONObject {
            return HookInfo().isXposedHook()
        }

        /**
         * 判断当前线程的堆栈信息
         *
         * @param thread 所要获取的线程
         */
        fun getThreadStackInfo(thread: Thread): String {
            return StackSampler.getStackInfo(thread)
        }
    }

    fun hasDebug(): Boolean {
        val checkDebug = CheckDebug()
        return checkDebug.nativeReadTracerPid() ||
                checkDebug.isReadTracerPid() ||
                checkDebug.isBeingDebugged()
    }

    fun isRoot(): Boolean {
        return CheckRoot.checkIsRoot() ||
                CheckRoot.nativeCheckIsRoot()
    }

    fun hasVpn(): Boolean {
        getContext()?.isUsingVpn()?.let {
            return it
        }
        return false
    }

    fun hasMonkey(): Boolean {
        return CheckMonkey.isUserAMonkey()
    }

    fun checkIsRunningInEmulator(callback: EmulatorCheckCallback?) {
        getContext()?.let {
            CheckEmulator.getSingleInstance().readSysProperty(it, callback)
        }
    }

    fun checkRiskAppInstalled(packageName: String?): Boolean? {
        return getContext()?.checkRiskAppInstalled(packageName)
    }
}
