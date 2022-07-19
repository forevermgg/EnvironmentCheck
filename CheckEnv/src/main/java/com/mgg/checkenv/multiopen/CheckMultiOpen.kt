package com.mgg.checkenv.multiopen
// https://song-dev.github.io/2019/12/23/more-open-check/
// https://bbs.pediy.com/thread-255212.htm

import android.annotation.SuppressLint
import android.content.Context
import android.os.Process
import android.util.Log
import com.mgg.checkenv.CheckEnv
import org.json.JSONObject
import java.io.File

open class CheckMultiOpen {
    companion object {
        private val TAG = MoreOpenInfo::class.java.simpleName
    }

    /**
     * 反系统级应用多开
     */
    fun isSystemDualApp(): Boolean {
        return 0 != Process.myUid() / 100000 || isNativeCheckSystemDualApp()
    }

    private external fun isNativeCheckSystemDualApp(): Boolean

    /**
     * 反用户级应用多开
     */
    fun isUserDualApp(dataDir: String): Boolean {
        return File(dataDir + File.separator.toString() + "..").canRead() || isNativeCheckUserDualAppByDataDir(
            dataDir
        )
    }

    private external fun isNativeCheckUserDualAppByDataDir(dataDir: String): Boolean

    /**
     * 判断当前私有路径是否是标准路径
     *
     * @param context
     * @return
     */
    @SuppressLint("SdCardPath")
    fun checkByPathCheckOpenCheck(context: Context): Boolean {
        // 获取内部存储目录路径
        val filesDir = context.filesDir.absolutePath
        val packageName = context.packageName
        val normalPathOne = "/data/data/$packageName/files"
        val normalPathTwo = "/data/user/0/$packageName/files"
        // 当前存储目录路径和正常存储目录路径比对
        return normalPathOne != filesDir && normalPathTwo != filesDir
    }

    open fun checkVirtualInfo(context: Context?): JSONObject? {
        val moreOpenBean = MoreOpenBean()
        try {
            moreOpenBean.isCheckByPrivateFilePath =
                context?.let { checkByPathCheckOpenCheck(it) } == true
            moreOpenBean.isSystemDualApp = isSystemDualApp()
            moreOpenBean.isUserDualApp = context?.let { isUserDualApp(it.applicationInfo.dataDir) } == true
            moreOpenBean.isCheckLs = checkMoreOpenByUid() == 1 || checkByShellCommandAccessInternalStorageDirectoryOpenCheck() == 1 ||
                    CheckEnv.getContext()?.let {
                        checkByShellCommandAccessInternalStorageDirectoryOpenCheckExt(
                            it
                        )
                    } == 1
        } catch (e: java.lang.Exception) {
            Log.e(TAG, e.toString())
        }
        return moreOpenBean.toJSONObject()
    }

    external fun checkMoreOpenByUid(): Int

    // shell 命令执行在单独进程，不受宿主控制。故可以通过 shell 命令访问内部存储目录，若可以访问则正常，否则为多开环境。
    external fun checkByShellCommandAccessInternalStorageDirectoryOpenCheck(): Int

    // shell 命令执行在单独进程，不受宿主控制。故可以通过 shell 命令访问内部存储目录，若可以访问则正常，否则为多开环境。
    external fun checkByShellCommandAccessInternalStorageDirectoryOpenCheckExt(context: Context): Int
}
