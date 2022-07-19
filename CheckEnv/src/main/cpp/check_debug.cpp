#include <android/log.h>
#include <asm-generic/fcntl.h>
#include <fcntl.h>
#include <jni.h>
#include <sys/ptrace.h>
#include <unistd.h>

#include <cstdio>
#include <cstring>
#include <string>

#include "common.h"

#define LOGE(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, "CheckDebug", __VA_ARGS__))
//
// Created by centforever on 2022/3/31.
//
void SearchObjProcess();
void CheckParents();
void ptraceCheck();
extern "C" JNIEXPORT jboolean JNICALL
Java_com_mgg_checkenv_debug_CheckDebug_nativeReadTracerPid(JNIEnv *, jobject) {
  SearchObjProcess();
  CheckParents();
  ptraceCheck();
  char file_path[128] = "\0";
  char buffer[BUF_SIZE_1024] = "\0";
  // 获取当前进程的pid
  int pid = getpid();
  sprintf(file_path, "/proc/%d/status", pid);
  FILE *fp = fopen(file_path, "r");
  if (fp == nullptr) {
    return -2;
  } else {
    while (fgets(buffer, BUF_SIZE_1024, fp)) {
      if (strncmp(buffer, "TracerPid", 9) == 0) {
        int status = atoi(&buffer[10]);
        LOGE("TracerPid %s %d\n", buffer, status);
        if (status != 0) {
          fclose(fp);
          return -1;
        }
      }
    }
  }
  return 0;
}

void SearchObjProcess() {
  FILE *pFile = nullptr;
  char buf[0x1000] = {0};  // 执行命令
  pFile = popen("ps", "r");
  if (nullptr == pFile) {
    LOGE("SearchObjProcess popen打开命令失败!\n");
    return;
  }
  // 获取结果
  LOGE("popen方案:\n");
  while (fgets(buf, sizeof(buf), pFile)) {
    // 打印进程
    LOGE("遍历进程:%s\n", buf);
    // 查找子串
    char *strA;
    char *strB;
    char *strC;
    char *strD;
    strA = strstr(buf, "android_server");
    strB = strstr(buf, "gdbserver");
    strC = strstr(buf, "gdb");
    strD = strstr(buf, "fuwu");
    if (strA || strB || strC || strD) {
      // 执行到这里，判定为调试状态
      LOGE("发现目标进程:%s\n", buf);
    }
  }
  pclose(pFile);
}

/**
  adb shell /proc/351/cmdline
  /system/bin/sh: /proc/351/cmdline: can't execute: Permission denied
  adb shell cat  /proc/351/cmdline
  zygote64%
 */
void CheckParents() {
  std::string path = "/proc/" + FOREVER::COMMON::getParentPid() + "/cmdline";
  std::string result = FOREVER::COMMON::readFile(path);
  const char *find = strstr(result.c_str(), "zygote");
  if (find) {
    // 执行到这里，判定为调试状态
    LOGE("父进程cmdline发现zygote进程:%s\n", result.c_str());
  } else {
    LOGE("父进程cmdline没有zygote子串:%s\n", result.c_str());
  }
}

// 单线程ptrace
void ptraceCheck() {
  // ptrace如果被调试返回值为-1，如果正常运行，返回值为0
  int iRet = ptrace(PTRACE_TRACEME, 0, 0, 0);
  if (-1 == iRet) {
    LOGE("ptrace失败，进程正在被调试\n");
    return;
  } else {
    LOGE("ptrace的返回值为:%d\n", iRet);
    return;
  }
}
