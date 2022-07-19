package com.mgg.checkenv.emulator

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.opengl.GLES20
import android.os.Build
import android.text.TextUtils
import android.util.Log
import com.mgg.checkenv.callback.EmulatorCheckCallback
import com.mgg.checkenv.cpu.CheckCpu
import com.mgg.checkenv.module.EmulatorCheckResult
import com.mgg.checkenv.module.EmulatorCheckResult.Companion.RESULT_EMULATOR
import com.mgg.checkenv.module.EmulatorCheckResult.Companion.RESULT_MAYBE_EMULATOR
import com.mgg.checkenv.module.EmulatorCheckResult.Companion.RESULT_UNKNOWN
import com.mgg.checkenv.utils.CommandUtil
import java.util.*

class CheckEmulator private constructor() {

    companion object {
        fun getSingleInstance() = SingletonHolder.INSTANCE
    }

    private object SingletonHolder {
        val INSTANCE: CheckEmulator = CheckEmulator()
    }

    fun readSysProperty(context: Context, callback: EmulatorCheckCallback?): Boolean {
        var suspectCount = 0

        // 检测硬件名称
        val hardwareResult = checkFeaturesByHardware()
        when (hardwareResult.result) {
            RESULT_MAYBE_EMULATOR -> ++suspectCount
            RESULT_EMULATOR -> {
                callback?.findEmulator("hardware = " + hardwareResult.value)
                return true
            }
        }

        // 检测渠道
        val flavorResult = checkFeaturesByFlavor()
        when (flavorResult.result) {
            RESULT_MAYBE_EMULATOR -> ++suspectCount
            RESULT_EMULATOR -> {
                callback?.findEmulator("flavor = " + flavorResult.value)
                return true
            }
        }

        // 检测设备型号
        val modelResult = checkFeaturesByModel()
        when (modelResult.result) {
            RESULT_MAYBE_EMULATOR -> ++suspectCount
            RESULT_EMULATOR -> {
                callback?.findEmulator("model = " + modelResult.value)
                return true
            }
        }

        // 检测硬件制造商
        val manufacturerResult = checkFeaturesByManufacturer()
        when (manufacturerResult.result) {
            RESULT_MAYBE_EMULATOR -> ++suspectCount
            RESULT_EMULATOR -> {
                callback?.findEmulator("manufacturer = " + manufacturerResult.value)
                return true
            }
        }

        // 检测主板名称
        val boardResult = checkFeaturesByBoard()
        when (boardResult.result) {
            RESULT_MAYBE_EMULATOR -> ++suspectCount
            RESULT_EMULATOR -> {
                callback?.findEmulator("board = " + boardResult.value)
                return true
            }
        }

        // 检测主板平台
        val platformResult = checkFeaturesByPlatform()
        when (platformResult.result) {
            RESULT_MAYBE_EMULATOR -> ++suspectCount
            RESULT_EMULATOR -> {
                callback?.findEmulator("platform = " + platformResult.value)
                return true
            }
        }

        // 检测基带信息
        val baseBandResult = checkFeaturesByBaseBand()
        when (baseBandResult.result) {
            RESULT_MAYBE_EMULATOR -> suspectCount += 2 //模拟器基带信息为null的情况概率相当大
            RESULT_EMULATOR -> {
                callback?.findEmulator("baseBand = " + baseBandResult.value)
                return true
            }
        }

        // 检测传感器数量
        val sensorNumber = getSensorNumber(context)
        if (sensorNumber <= 7) ++suspectCount

        // 检测已安装第三方应用数量
        val userAppNumber = getUserAppNumber()
        if (userAppNumber <= 5) ++suspectCount

        // 检测是否支持闪光灯
        val supportCameraFlash = supportCameraFlash(context)
        if (!supportCameraFlash) ++suspectCount
        // 检测是否支持相机
        val supportCamera = supportCamera(context)
        if (!supportCamera) ++suspectCount
        // 检测是否支持蓝牙
        val supportBluetooth = supportBluetooth(context)
        if (!supportBluetooth) ++suspectCount

        // 检测光线传感器
        val hasLightSensor = hasLightSensor(context)
        if (!hasLightSensor) ++suspectCount

        // 检测进程组信息
        val cGroupResult = checkFeaturesByCgroup()
        if (cGroupResult.result == RESULT_MAYBE_EMULATOR) {
            ++suspectCount
        }
        // 检测Cpu数量
        val cpuCount = CheckCpu().getAvailableProcessors()
        if (cpuCount <= 1) {
            ++suspectCount
        }
        if (callback != null) {
            val stringBuffer: StringBuffer = StringBuffer("CheckEmulator Result")
                .append("\r\n").append("hardware = ").append(hardwareResult.value)
                .append("\r\n").append("flavor = ").append(flavorResult.value)
                .append("\r\n").append("model = ").append(modelResult.value)
                .append("\r\n").append("manufacturer = ").append(manufacturerResult.value)
                .append("\r\n").append("board = ").append(boardResult.value)
                .append("\r\n").append("platform = ").append(platformResult.value)
                .append("\r\n").append("baseBand = ").append(baseBandResult.value)
                .append("\r\n").append("sensorNumber = ").append(sensorNumber)
                .append("\r\n").append("userAppNumber = ").append(userAppNumber)
                .append("\r\n").append("supportCamera = ").append(supportCamera)
                .append("\r\n").append("supportCameraFlash = ").append(supportCameraFlash)
                .append("\r\n").append("supportBluetooth = ").append(supportBluetooth)
                .append("\r\n").append("hasLightSensor = ").append(hasLightSensor)
                .append("\r\n").append("cgroupResult = ").append(cGroupResult.value)
                .append("\r\n").append("cpuCount = ").append(cpuCount)
                .append("\r\n").append("suspectCount = ").append(suspectCount)
            callback.findEmulator(stringBuffer.toString())
        }
        //嫌疑值大于3，认为是模拟器
        return suspectCount > 3
    }

    private fun getUserAppNum(userApps: String): Int {
        if (TextUtils.isEmpty(userApps)) return 0
        val result = userApps.split("package:").toTypedArray()
        return result.size
    }

    private fun getProperty(propName: String): String? {
        val property: String? = CommandUtil.getSingleInstance().getProperty(propName)
        return if (TextUtils.isEmpty(property)) null else property
    }

    /**
     * 特征参数-硬件名称
     *
     * @return 0表示可能是模拟器，1表示模拟器，2表示可能是真机
     */
    private fun checkFeaturesByHardware(): EmulatorCheckResult {
        val hardware = getProperty("ro.hardware")
            ?: return EmulatorCheckResult(RESULT_MAYBE_EMULATOR, null)
        val result: Int = when (hardware.lowercase(Locale.getDefault())) {
            "ttvm", "nox", "cancro", "intel", "vbox", "vbox86", "android_x86" -> RESULT_EMULATOR
            else -> RESULT_UNKNOWN
        }
        return EmulatorCheckResult(result, hardware)
    }

    /**
     * 特征参数-渠道
     *
     * @return 0表示可能是模拟器，1表示模拟器，2表示可能是真机
     */
    private fun checkFeaturesByFlavor(): EmulatorCheckResult {
        val flavor = getProperty("ro.build.flavor")
            ?: return EmulatorCheckResult(RESULT_MAYBE_EMULATOR, null)
        val result: Int
        val tempValue = flavor.lowercase(Locale.getDefault())
        result = when {
            tempValue.contains("vbox") -> RESULT_EMULATOR
            tempValue.contains("sdk_gphone") -> RESULT_EMULATOR
            else -> RESULT_UNKNOWN
        }
        return EmulatorCheckResult(result, flavor)
    }

    /**
     * 特征参数-设备型号
     *
     * @return 0表示可能是模拟器，1表示模拟器，2表示可能是真机
     */
    private fun checkFeaturesByModel(): EmulatorCheckResult {
        val model = getProperty("ro.product.model")
            ?: return EmulatorCheckResult(RESULT_MAYBE_EMULATOR, null)
        val result: Int
        val tempValue = model.lowercase(Locale.getDefault())
        result = when {
            tempValue.contains("google_sdk") -> RESULT_EMULATOR
            tempValue.contains("emulator") -> RESULT_EMULATOR
            tempValue.contains("android sdk built for x86") -> RESULT_EMULATOR
            else -> RESULT_UNKNOWN
        }
        return EmulatorCheckResult(result, model)
    }

    /**
     * 特征参数-硬件制造商
     *
     * @return 0表示可能是模拟器，1表示模拟器，2表示可能是真机
     */
    private fun checkFeaturesByManufacturer(): EmulatorCheckResult {
        val manufacturer = getProperty("ro.product.manufacturer")
            ?: return EmulatorCheckResult(RESULT_MAYBE_EMULATOR, null)
        val result: Int
        val tempValue = manufacturer.lowercase(Locale.getDefault())
        result = when {
            tempValue.contains("genymotion") -> RESULT_EMULATOR
            tempValue.contains("netease") -> RESULT_EMULATOR //网易MUMU模拟器
            else -> RESULT_UNKNOWN
        }
        return EmulatorCheckResult(result, manufacturer)
    }

    /**
     * 特征参数-主板名称
     *
     * @return 0表示可能是模拟器，1表示模拟器，2表示可能是真机
     */
    private fun checkFeaturesByBoard(): EmulatorCheckResult {
        val board = getProperty("ro.product.board")
            ?: return EmulatorCheckResult(RESULT_MAYBE_EMULATOR, null)
        val result: Int
        val tempValue = board.lowercase(Locale.getDefault())
        result = when {
            tempValue.contains("android") -> RESULT_EMULATOR
            tempValue.contains("goldfish") -> RESULT_EMULATOR
            else -> RESULT_UNKNOWN
        }
        return EmulatorCheckResult(result, board)
    }

    /**
     * 特征参数-主板平台
     *
     * @return 0表示可能是模拟器，1表示模拟器，2表示可能是真机
     */
    private fun checkFeaturesByPlatform(): EmulatorCheckResult {
        val platform = getProperty("ro.board.platform")
            ?: return EmulatorCheckResult(RESULT_MAYBE_EMULATOR, null)
        val result: Int
        val tempValue = platform.lowercase(Locale.getDefault())
        result = when {
            tempValue.contains("android") -> RESULT_EMULATOR
            else -> RESULT_UNKNOWN
        }
        return EmulatorCheckResult(result, platform)
    }

    /**
     * 特征参数-基带信息
     *
     * @return 0表示可能是模拟器，1表示模拟器，2表示可能是真机
     */
    private fun checkFeaturesByBaseBand(): EmulatorCheckResult {
        val baseBandVersion = getProperty("gsm.version.baseband")
            ?: return EmulatorCheckResult(RESULT_MAYBE_EMULATOR, null)
        val result: Int = when {
            baseBandVersion.contains("1.0.0.0") -> RESULT_EMULATOR
            else -> RESULT_UNKNOWN
        }
        return EmulatorCheckResult(result, baseBandVersion)
    }

    /**
     * 获取传感器数量
     */
    private fun getSensorNumber(context: Context): Int {
        val sm = context.getSystemService(SENSOR_SERVICE) as SensorManager
        return sm.getSensorList(Sensor.TYPE_ALL).size
    }

    /**
     * 获取已安装第三方应用数量
     */
    private fun getUserAppNumber(): Int {
        val userApps: String = CommandUtil.getSingleInstance().exec("pm list package -3")
        return getUserAppNum(userApps)
    }

    /**
     * 是否支持相机
     */
    private fun supportCamera(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    /**
     * 是否支持闪光灯
     */
    private fun supportCameraFlash(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    /**
     * 是否支持蓝牙
     */
    private fun supportBluetooth(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
    }

    /**
     * 判断是否存在光传感器来判断是否为模拟器
     * 部分真机也不存在温度和压力传感器。其余传感器模拟器也存在。
     *
     * @return false为模拟器
     */
    private fun hasLightSensor(context: Context): Boolean {
        val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) //光线传感器
        return null != sensor
    }

    /**
     * 特征参数-进程组信息
     */
    private fun checkFeaturesByCgroup(): EmulatorCheckResult {
        val filter: String = CommandUtil.getSingleInstance().exec("cat /proc/self/cgroup")
        return EmulatorCheckResult(RESULT_UNKNOWN, filter)
    }

    fun checkBasic(): Boolean {
        var result = (
                Build.FINGERPRINT.startsWith("generic") ||
                        Build.MODEL.contains("google_sdk") ||
                        Build.MODEL.lowercase(Locale.getDefault()).contains("droid4x") ||
                        Build.MODEL.contains("Emulator") ||
                        Build.MODEL.contains("Android SDK built for x86") ||
                        Build.MANUFACTURER.contains("Genymotion") ||
                        Build.HARDWARE == "goldfish" ||
                        Build.HARDWARE == "vbox86" ||
                        Build.PRODUCT == "sdk" ||
                        Build.PRODUCT == "google_sdk" ||
                        Build.PRODUCT == "sdk_x86" ||
                        Build.PRODUCT == "vbox86p" ||
                        Build.BOARD.lowercase(Locale.getDefault()).contains("nox") ||
                        Build.BOOTLOADER.lowercase(Locale.getDefault()).contains("nox") ||
                        Build.HARDWARE.lowercase(Locale.getDefault()).contains("nox") ||
                        Build.PRODUCT.lowercase(Locale.getDefault()).contains("nox")
                )
        if (result) return true
        result = result or (Build.BRAND.startsWith("generic") &&
                Build.DEVICE.startsWith("generic"))
        if (result) return true
        result = result or ("google_sdk" == Build.PRODUCT)
        return result
    }

    /**
     * Prints all Build.* parameters used in [.isEmulator] method to logcat.
     */
    fun logcat() {
        Log.e("CheckEmulator", getDeviceListing())
    }

    /**
     * Returns string with human-readable listing of Build.* parameters used in [.isEmulator] method.
     *
     * @return all involved Build.* parameters and its values
     */
    private fun getDeviceListing(): String {
        return """
             Build.PRODUCT: ${Build.PRODUCT}
             Build.MANUFACTURER: ${Build.MANUFACTURER}
             Build.BRAND: ${Build.BRAND}
             Build.BOARD: ${Build.BOARD}
             Build.DEVICE: ${Build.DEVICE}
             Build.MODEL: ${Build.MODEL}
             Build.HARDWARE: ${Build.HARDWARE}
             Build.FINGERPRINT: ${Build.FINGERPRINT}
             Build.TAGS: ${Build.TAGS}
             GL_RENDERER: ${GLES20.glGetString(GLES20.GL_RENDERER)}
             GL_VENDOR: ${GLES20.glGetString(GLES20.GL_VENDOR)}
             GL_VERSION: ${GLES20.glGetString(GLES20.GL_VERSION)}
             GL_EXTENSIONS: ${GLES20.glGetString(GLES20.GL_EXTENSIONS)}
             """.trimIndent()
    }
}
