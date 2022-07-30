//
// Created by centforever on 2022/3/31.
// https://song-dev.github.io/2019/12/28/root-check/
//
#include <jni.h>
#include <unistd.h>

#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <string>

#include "common.h"

extern "C" JNIEXPORT jboolean JNICALL
Java_com_mgg_checkenv_root_CheckRoot_nativeCheckIsRoot(JNIEnv *, jobject) {
  int result = 0;
  const char *path[] = {
      "/system/bin/su",
      "/system/xbin/su",
      "/system/bin/.ext/su",
      "/system/bin/failsafe/su",
      "/system/sd/xbin/su",
      "/system/usr/we-need-root/su",
      "/system/xbin/busybox",
      "/system/bin/busybox",
      "/cache/su",
      "/data/su",
      "/dev/su",
      "/data/local/tmp/busybox",
      "/data/local/su",
      "/data/local/bin/su",
      "/data/local/xbin/su",
      "/sbin/su",
      "/su/bin/su",
  };
  // 判断上述文件是否存在并且拥有可执行的权限
  int len = sizeof(path) / sizeof(path[0]);
  for (int i = 0; i < len; i++) {
    if (access(path[i], F_OK | X_OK) == 0) {
      result++;
    }
  }
  return result > 0;
}
