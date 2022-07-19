//
// Created by centforever on 2022/4/20.
//
#include "mobile_info.h"

#include <android/log.h>
// log标签
#define TAG "MobileInfo"
// 定义info信息
#define LOGE(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__))

using std::string;

string getBootId() {
  string bootId = FOREVER::COMMON::readFile("/proc/sys/kernel/random/boot_id");
  if (strlen(bootId.c_str()) == 0) {
    bootId = FOREVER::COMMON::shellExecute("cat /proc/sys/kernel/random/boot_id");
  }
  return bootId;
}

string getEntropyAvail() {
  string bootId = FOREVER::COMMON::readFile("/proc/sys/kernel/random/entropy_avail");
  if (strlen(bootId.c_str()) == 0) {
    bootId = FOREVER::COMMON::shellExecute("cat /proc/sys/kernel/random/entropy_avail");
  }
  return bootId;
}

string getPoolSize() {
  string bootId = FOREVER::COMMON::readFile("/proc/sys/kernel/random/poolsize");
  if (strlen(bootId.c_str()) == 0) {
    bootId = FOREVER::COMMON::shellExecute("cat /proc/sys/kernel/random/poolsize");
  }
  return bootId;
}

string getReadWakeupThreshold() {
  string bootId = FOREVER::COMMON::readFile("/proc/sys/kernel/random/read_wakeup_threshold");
  if (strlen(bootId.c_str()) == 0) {
    bootId = FOREVER::COMMON::shellExecute("cat /proc/sys/kernel/random/read_wakeup_threshold");
  }
  return bootId;
}

string getWriteWakeupThreshold() {
  string bootId = FOREVER::COMMON::readFile("/proc/sys/kernel/random/write_wakeup_threshold");
  if (strlen(bootId.c_str()) == 0) {
    bootId = FOREVER::COMMON::shellExecute("cat /proc/sys/kernel/random/write_wakeup_threshold");
  }
  return bootId;
}

string getUuid() {
  string bootId = FOREVER::COMMON::readFile("/proc/sys/kernel/random/uuid");
  if (strlen(bootId.c_str()) == 0) {
    bootId = FOREVER::COMMON::shellExecute("cat /proc/sys/kernel/random/uuid");
  }
  return bootId;
}

string getURandomMinReseedSecs() {
  string bootId = FOREVER::COMMON::readFile("/proc/sys/kernel/random/urandom_min_reseed_secs");
  if (strlen(bootId.c_str()) == 0) {
    bootId =
            FOREVER::COMMON::shellExecute("cat /proc/sys/kernel/random/urandom_min_reseed_secs");
  }
  return bootId;
}

string getKennel() {
  string kennel;
  kennel = FOREVER::COMMON::readFile("/proc/version");
  if (strlen(kennel.c_str()) == 0) {
    string r = FOREVER::COMMON::shellExecute("uname -r");
    string v = FOREVER::COMMON::shellExecute("uname -v");
    if (strlen(r.c_str()) != 0) {
      if (strlen(v.c_str()) != 0) {
        return r + " " + v;
      } else {
        return r;
      }
    }
  } else {
    return kennel;
  }
  return "";
}

int checkMoreOpenByUid() {
  if (FOREVER::COMMON::existsFile("/system/bin/ls")) {
    char path[BUF_SIZE_256];
    string name = FOREVER::COMMON::getPackageName(FOREVER::COMMON::getMyPid());
    sprintf(path, "ls /data/data/%s", name.c_str());
    LOGE("checkMoreOpenByUid:%s", name.c_str());
    string result = FOREVER::COMMON::shellExecute(path);

    if (result.empty()) {
      return 1;
    } else {
      return 0;
    }
  }
  return 0;
}

int checkSubstrateBySo() {
  void *imagehandle = dlopen("libsubstrate-dvm.so", RTLD_GLOBAL | RTLD_NOW);
  if (imagehandle != nullptr) {
    void *sym = dlsym(imagehandle, "MSJavaHookMethod");
    if (sym != nullptr) {
      dlclose(imagehandle);
      return 1;
    }
  }

  return 0;
}

/**
 * 检测主流 hook 框架: frida、Xposed、substrate
 * @return
 */
int frameCheck() {
  char path[BUF_SIZE_32];
  sprintf(path, "/proc/%d/maps", getpid());
  // 读取数据
  FILE *f;
  char buf[BUF_SIZE_512];
  f = fopen(path, "r");
  if (f != nullptr) {
    while (fgets(buf, BUF_SIZE_512, f)) {
      // fgets 当读取 (n-1)
      // 个字符时，或者读取到换行符时，或者到达文件末尾时，它会停止，具体视情况而定。
      LOGE("frameCheck: %s", buf);
      if (strstr(buf, "frida") || strstr(buf, "com.saurik.substrate") ||
          strstr(buf, "XposedBridge.jar")) {
        fclose(f);
        return 1;
      }
    }
  }
  fclose(f);
  return 0;
}

/**
 * 检测已加载到内存的 xhook 核心文件
 * @return
 */
int xHookCheck() {
  // 直接加载危险的so
  void *imagehandle = dlopen("libxhook.so", RTLD_GLOBAL | RTLD_NOW);
  if (imagehandle != nullptr) {
    void *sym = dlsym(imagehandle, "xhook_register");
    if (sym != nullptr) {
      dlclose(imagehandle);
      LOGE("find xhook");
      return 1;
    }
  }
  LOGE("not find xhook");
  return 0;
}

string checkHookByMap() {
  frameCheck();
  string data;
  string path = "/proc/self/maps";
  string mapsStr = FOREVER::COMMON::readFile(path);
  LOGE("checkHookByMap: %s", mapsStr.c_str());
  if (mapsStr.empty()) {
    mapsStr = FOREVER::COMMON::shellExecute("/proc/myself/maps");
    if (mapsStr.empty()) {
      return "";
    }
  }
  const char *maps = mapsStr.c_str();
  if (strstr(maps, "frida")) {
    data += "frida";
  }

  if (strstr(maps, "com.saurik.substrate")) {
    data += "substrate";
  }

  if (strstr(maps, "XposedBridge.jar")) {
    data += "xposed";
  }
  return data;
}

string checkHookByPackage() {
  string data;
  if (FOREVER::COMMON::existsFile("/data/data/de.robv.android.xposed.installer") ||
          FOREVER::COMMON::existsFile("/data/data/io.va.exposed")) {
    data += "xposed";
  }
  if (FOREVER::COMMON::existsFile("/data/data/com.saurik.substrate")) {
    data += "substrate";
  }
  return data;
}
