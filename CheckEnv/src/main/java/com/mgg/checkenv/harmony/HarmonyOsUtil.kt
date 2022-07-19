package com.mgg.checkenv.harmony

object HarmonyOsUtil {

  private val isHarmonyOs by lazy {
    runCatching {
      val clz = Class.forName("com.huawei.system.BuildEx")
      val method = clz.getMethod("getOsBrand")
      "harmony".equals(method.invoke(clz) as String?, ignoreCase = true)
    }.getOrDefault(false)
  }

  fun <T> wrapperStub(f: () -> T): T? {
    return try {
      f()
    } catch (e: Throwable) {
      null
    }
  }

  @JvmName("isHarmonyOs1")
  fun isHarmonyOs(): Boolean = isHarmonyOs
}
