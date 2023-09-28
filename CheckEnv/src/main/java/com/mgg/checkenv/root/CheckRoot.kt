package com.mgg.checkenv.root

import android.util.Log
import com.mgg.checkenv.utils.ShellUtils
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

/**
 * https://github.com/song-dev/device-info/blob/752d380c5f35aa32e82e533e77019d690944e49b/app/src/main/java/com/song/deviceinfo/utils/RootUtils.java
 */
object CheckRoot {
    private val TAG: String = CheckRoot::class.java.simpleName
    fun checkIsRoot(): Boolean {
        val result = ShellUtils.execCmd("", true)
        return result.result != -1 && (result.errorMsg == null || result.errorMsg.isEmpty())
    }

    external fun nativeCheckIsRoot(): Boolean

    fun mobileRoot(): Boolean {
        return existingRWPaths().isNotEmpty() ||
                existingDangerousProperties().isNotEmpty() ||
                existingRootFiles().isNotEmpty() ||
                checkWhichSu() ||
                checkBuildTag()
    }

    private val SU_PATHS = arrayOf(
        "/system/app/Superuser.apk",
        "/sbin/su",
        "/system/bin/su",
        "/system/sbin/su",
        "/system/xbin/su",
        "/data/local/xbin/su",
        "/data/local/bin/su",
        "/system/sd/xbin/su",
        "/system/bin/failsafe/su",
        "/data/local/su",
        "/su/bin/su"
    )

    private val PATHS_THAT_SHOULD_NOT_BE_WRITABLE = arrayOf(
        "/system",
        "/system/bin",
        "/system/sbin",
        "/system/xbin",
        "/vendor/bin",
        "/sbin",
        "/etc"
    )
    private val DANGEROUS_PROPERTIES: MutableMap<String, String> = HashMap()


    private fun existingRootFiles(): List<String> {
        val filesFound: MutableList<String> = ArrayList()
        for (path in SU_PATHS) {
            if (File(path).exists()) {
                filesFound.add(path)
            }
        }
        return filesFound
    }

    /**
     * Checks system properties for any dangerous properties that indicate root.
     *
     * @return - list of dangerous properties that indicate root
     */
    private fun existingDangerousProperties(): List<String> {
        DANGEROUS_PROPERTIES["[ro.debuggable]"] = "[1]"
        DANGEROUS_PROPERTIES["[ro.secure]"] = "[0]"
        val lines = propertiesReader()
        val propertiesFound: MutableList<String> = ArrayList()
        assert(lines != null)
        for (line in lines!!) {
            for (key in DANGEROUS_PROPERTIES.keys) {
                if (line.contains(key) && line.contains(DANGEROUS_PROPERTIES[key]!!)) {
                    propertiesFound.add(line)
                }
            }
        }
        return propertiesFound
    }

    /**
     * When you're root you can change the write permissions on common system directories.
     * This method checks if any of the paths in PATHS_THAT_SHOULD_NOT_BE_WRITABLE are writable.
     *
     * @return all paths that are writable
     */
    private fun existingRWPaths(): List<String> {
        val lines = mountReader()
        val pathsFound: MutableList<String> = ArrayList()
        assert(lines != null)
        for (line in lines!!) {
            // Split lines into parts
            val args = line.split(" ").toTypedArray()
            if (args.size < 4) {
                // If we don't have enough options per line, skip this and log an error
                continue
            }
            val mountPoint = args[1]
            val mountOptions = args[3]
            for (pathToCheck in PATHS_THAT_SHOULD_NOT_BE_WRITABLE) {
                if (mountPoint.equals(pathToCheck, ignoreCase = true)) {
                    // Split options out and compare against "rw" to avoid false positives
                    for (option in mountOptions.split(",").toTypedArray()) {
                        if ("rw".equals(option, ignoreCase = true)) {
                            pathsFound.add(pathToCheck)
                            break
                        }
                    }
                }
            }
        }
        return pathsFound
    }

    /**
     * Used for existingDangerousProperties().
     *
     * @return - list of system properties
     */
    private fun propertiesReader(): Array<String>? {
        var inputstream: InputStream? = null
        try {
            inputstream = Runtime.getRuntime().exec("getprop").inputStream
        } catch (e: IOException) {
            Log.i(TAG, e.toString())
        }
        if (inputstream == null) {
            return null
        }
        var allProperties = ""
        try {
            allProperties = Scanner(inputstream).useDelimiter("\\A").next()
        } catch (e: NoSuchElementException) {
            Log.i(TAG, e.toString())
        }
        return allProperties.split("\n").toTypedArray()
    }

    /**
     * Used for existingRWPaths().
     *
     * @return - list of directories and their properties
     */
    private fun mountReader(): Array<String>? {
        var inputstream: InputStream? = null
        try {
            inputstream = Runtime.getRuntime().exec("mount").inputStream
        } catch (e: IOException) {
            Log.i(TAG, e.toString())
        }
        if (inputstream == null) {
            return null
        }
        var allPaths = ""
        try {
            allPaths = Scanner(inputstream).useDelimiter("\\A").next()
        } catch (e: NoSuchElementException) {
            Log.i(TAG, e.toString())
        }
        return allPaths.split("\n").toTypedArray()
    }

    fun checkWhichSu(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            bufferedReader.readLine() != null
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }

    fun checkBuildTag(): Boolean {
        val buildTags = android.os.Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }
}
