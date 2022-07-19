package com.mgg.checkenv.module

class BaseData {
    companion object {
        val UNKNOWN_PARAM = "unknown"
    }

    object Aduio {
        const val MAX_VOICE_CALL = "maxVoiceCall"
        const val MIN_VOICE_CALL = "minVoiceCall"
        const val CURRENT_VOICE_CALL = "currentVoiceCall"
        const val MAX_SYSTEM = "maxSystem"
        const val MIN_SYSTEM = "minSystem"
        const val CURRENT_SYSTEM = "currentSystem"
        const val MAX_RING = "maxRing"
        const val MIN_RING = "minRing"
        const val CURRENT_RING = "currentRing"
        const val MAX_MUSIC = "maxMusic"
        const val MIN_MUSIC = "minMusic"
        const val CURRENT_MUSIC = "currentMusic"
        const val MAX_ALARM = "maxAlarm"
        const val MIN_ALARM = "minAlarm"
        const val CURRENT_ALARM = "currentAlarm"
        const val MAX_NOTIFICATIONS = "maxNotifications"
        const val MIN_NOTIFICATIONS = "minNotifications"
        const val CURRENT_NOTIFICATIONS = "currentNotifications"
        const val MAX_ACCESSIBILITY = "maxAccessibility"
        const val MIN_ACCESSIBILITY = "minAccessibility"
        const val CURRENT_ACCESSIBILITY = "currentAccessibility"
        const val MAX_DTMF = "maxDTMF"
        const val MIN_DTMF = "minDTMF"
        const val CURRENT_DTMF = "currentDTMF"
    }

    object Battery {
        const val BR = "br"
        const val STATUS = "status"
        const val PLUG_STATE = "plugState"
        const val HEALTH = "health"
        const val PRESENT = "present"
        const val TECHNOLOGY = "technology"
        const val TEMPERATURE = "temperature"
        const val VOLTAGE = "voltage"
        const val POWER = "power"
    }

    object AppList {
        const val PACKAGE_NAME = "packageName"
        const val VERSION_NAME = "versionName"
        const val IS_SYSTEM = "isSystem"
        const val VERSION_CODE = "versionCode"
    }

    object Debug {
        const val IS_OPEN_DEBUG = "isOpenDebug"
        const val IS_DEBUG_VERSION = "isDebugVersion"
        const val IS_DEBUGGING = "isDebugging"
        const val IS_READ_PROC_STATUS = "isReadProcStatus"
        const val IS_ALLOW_MOCK_LOCATION = "isAllowMockLocation"
    }

    object MoreOpen {
        const val CHECK_BY_PRIVATE_FILE_PATH = "checkByPrivateFilePath"
        const val CHECK_BY_SHELL_COMMAND = "checkByShellCommandAccessInternalStorage"
        const val IS_SYSTEM_DUAL_APP = "isSystemDualApp"
        const val IS_USER_DUAL_APP = "isUserDualApp"
    }

    object Hook {
        const val IS_HAVE_XPOSED = "xposedInfo"
        const val IS_HAVE_SUBSTRATE = "substrateInfo"
        const val IS_HAVE_FRIDA = "fridaInfo"

        object Xposed {
            const val CHECK_XPOSED_PACKAGE = "checkXposedPackage"
            const val CHECK_XPOSED_HOOK_METHOD = "checkXposedHookMethod"
            const val CHECK_XPOSED_JARS = "checkXposedJars"
            const val CHECK_CLASSLOADER = "checkClassLoader"
            const val CHECK_NATIVE_METHOD = "checkNativeMethod"
            const val CHECK_SYSTEM = "checkSystem"
            const val CHECK_EXEC_LIB = "checkExecLib"
            const val CHECK_XPOSED_BRIDGE = "checkXposedBridge"
        }

        object Substrate {
            const val CHECK_SUBSTRATE_PACKAGE = "checkSubstratePackage"
            const val CHECK_SUBSTRATE_HOOK_METHOD = "checkSubstrateHookMethod"
            const val CHECK_SUBSTRATE_JARS = "checkSubstrateJars"
        }

        object Frida {
            const val CHECK_RUNNING_PROCESSES = "checkRunningProcesses"
            const val CHECK_FRIDA_JARS = "checkFridaJars"
        }
    }

    object Band {
        const val BASE_BAND = "baseBand"
        const val INNER_BAND = "innerBand"
        const val LINUX_BAND = "linuxBand"
        const val DETAIL_LINUX_BAND = "detailLinuxBand"
    }

    object Local {
        const val COUNTRY = "country"
        const val LANGUAGE = "language"
    }

    object Bluetooth {
        const val BLUETOOTH_ADDRESS = "bluetoothAddress"
        const val IS_ENABLED = "isEnabled"
        const val DEVICE = "device"
        const val PHONE_NAME = "phoneName"

        object Device {
            var ADDRESS = "address"
            var NAME = "name"
        }
    }

    object Settings {
        const val ANDROID_ID = "androidId"
        const val SCREEN_OFF_TIMEOUT = "screenOffTimeout"
        const val SOUND_EFFECTS_ENABLED = "soundEffectsEnabled"
        const val SCREEN_BRIGHTNESS_MODE = "screenBrightnessMode"
        const val DEVELOPMENT_SETTINGS_ENABLED = "developmentSettingsEnabled"
        const val ACCELEROMETER_ROTATION = "accelerometerRotation"
        const val LOCK_PATTERN_VISIBLE_PATTERN = "lockPatternVisiblePattern"
        const val LOCK_PATTERN_AUTOLOCK = "lockPatternAutoLock"
        const val USB_MASS_STORAGE_ENABLED = "usbMassStorageEnabled"
    }

    object Screen {
        const val DENSITY_SCALE = "densityScale"
        const val DENSITY_DPI = "densityDpi"
        const val WIDTH = "width"
        const val HEIGHT = "height"
        const val IS_SCREEN_AUTO = "isScreenAuto"
        const val SCREEN_BRIGHTNESS = "screenBrightness"
        const val IS_SCREEN_AUTO_CHANGE = "isScreenAutoChange"
        const val CHECK_HIDE_STATUSBAR = "checkHideStatusBar"
        const val CHECK_HAS_NAVIGATIONBAR = "checkHasNavigationBar"
        const val GET_STATUSBAR_HEIGHT = "getStatusBarHeight"
        const val GET_NAVIGATIONBAR_HEIGHT = "getNavigationBarHeight"
        const val IS_WINDOW_NOTCH = "isWindowNotch"
        const val WINDOW_NOTCH_HEIGHT = "windowNotchHeight"
    }

    object Cpu {
        const val AVALIABLE_PROCESSORS = "availableProcessors"
        const val CPU_DESKTOP = "checkIsDesktopCpu"
        const val CPU_CORES = "cpuCores"
        const val CPU_ABI = "cpuAbi"
    }

    object Build {
        const val BOARD = "board"
        const val BOOTLOADER = "bootloader"
        const val BRAND = "brand"
        const val DEVICE = "device"
        const val DISPLAY = "display"
        const val FINGERPRINT = "fingerprint"
        const val HARDWARE = "hardware"
        const val HOST = "host"
        const val ID = "id"
        const val MANUFACTURER = "manufacturer"
        const val MODEL = "model"
        const val PRODUCT = "product"
        const val RADIO = "radio"
        const val SERIAL = "serial"
        const val TAGS = "tags"
        const val TIME = "time"
        const val TYPE = "type"
        const val USER = "user"
        const val OS_VERSION = "osVersion"
        const val RELEASE_VERSION = "releaseVersion"
        const val CODE_NAME = "codeName"
        const val INCREMENTAL = "incremental"
        const val SDK_INT = "sdkInt"
        const val PREVIEW_SDK_INT = "previewSdkInt"
        const val SECURITY_PATCH = "securityPatch"
    }

    object App {
        const val APP_NAME = "appName"
        const val LAUNCHER_APP_NAME = "launcherAppName"
        const val FIRST_INSTALL_TIME = "firstInstallTime"
        const val LAST_UPDATE_TIME = "lastUpdateTime"
        const val PACKAGE_NAME = "packageName"
        const val PACKAGE_SIGN = "packageSign"
        const val APP_VERSION_CODE = "appVersionCode"
        const val APP_VERSION_NAME = "appVersionName"
        const val APP_TARGET_SDK_VERSION = "targetSdkVersion"
        const val APP_MIN_SDK_VERSION = "minSdkVersion"
        const val APP_DESCRIPTION = "description"
        const val APP_ICON = "icon"
    }

    object Camera {
        const val CAMERA_INFO = "cameraInfo"

        object CameraInfo {
            const val CAMERA_FACING = "cameraFacing"
            const val CAMERA_LEVEL = "cameraLevel"
            const val CAMERA_FLASH_INFO = "cameraFlashInfo"
            const val OUTPUT_FORMATS = "outputFormats"
        }
    }

    object Memory {
        const val RAM_MEMORY = "ramMemoryTotal"
        const val RAM_AVAIL_MEMORY = "ramMemoryAvailable"
        const val ROM_MEMORY_AVAILABLE = "romMemoryAvailable"
        const val ROM_MEMORY_TOTAL = "romMemoryTotal"
        const val SDCARD_MEMORY_AVAILABLE = "sdCardMemoryAvailable"
        const val SDCARD_MEMORY_TOTAL = "sdCardMemoryTotal"
        const val SDCARD_REAL_MEMORY_TOTAL = "sdCardRealMemoryTotal"
    }

    object Emulator {
        const val CHECK_BUILD = "checkBuild"
        const val CHECK_PKG = "checkPkg"
        const val CHECK_PIPES = "checkPipes"
        const val CHECK_QEMU_DRIVER_FILE = "checkQEmuDriverFile"
        const val CHECK_HAS_LIGHT_SENSOR_MANAGER = "checkHasLightSensorManager"
        const val CHECK_CPU_INFO = "checkCpuInfo"
    }

    object NetWork {
        const val TYPE = "type"
        const val NETWORK_AVAILABLE = "networkAvailable"
        const val HAVE_INTENT = "haveIntent"
        const val IS_FLIGHT_MODE = "isFlightMode"
        const val IS_NFC_ENABLED = "isNFCEnabled"
        const val IS_HOTSPOT_ENABLED = "isHotspotEnabled"
        const val HOTSPOT_SSID = "hotspotSSID"
        const val HOTSPOT_PWD = "hotspotPwd"
        const val ENCRYPTION_TYPE = "encryptionType"
    }

    object Signal {
        const val TYPE = "type"
        const val BSSID = "bssid"
        const val SSID = "ssid"
        const val N_IP_ADDRESS = "ipAddress"
        const val N_IP_ADDRESS_IPV6 = "ipAddressIpv6"
        const val MAC_ADDRESS = "macAddress"
        const val NETWORK_ID = "networkId"
        const val LINK_SPEED = "linkSpeed"
        const val RSSI = "rssi"
        const val LEVEL = "level"
        const val SUPPLICANT_STATE = "supplicantState"
        const val PROXY = "proxy"
        const val PROXY_ADDRESS = "proxyAddress"
        const val PROXY_PORT = "proxyPort"
    }

    object SimCard {
        const val SIM1_IMEI = "sim1Imei"
        const val SIM2_IMEI = "sim2Imei"
        const val SIM1_IMSI = "sim1Imsi"
        const val SIM2_IMSI = "sim2Imsi"
        const val SIM_SLOT_INDEX = "simSlotIndex"
        const val MEID = "meid"
        const val SIM1_IMSI_OPERATOR = "sim1ImsiOperator"
        const val SIM2_IMSI_OPERATOR = "sim2ImsiOperator"
        const val SIM1_READY = "sim1Ready"
        const val SIM2_READY = "sim2Ready"
        const val IS_TWO_CARD = "isTwoCard"
        const val IS_HAVE_CARD = "isHaveCard"
        const val OPERATOR = "operator"
    }

    object SDCard {
        const val IS_SDCARD_ENABLE = "isSDCardEnable"
        const val SDCARD_PATH = "getSDCardPath"
    }
}
