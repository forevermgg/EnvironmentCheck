#include <android/log.h>
#include <jni.h>
#include <unistd.h>

#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <filesystem>
#include <string>

#include "jni_string.hpp"
#include "mobile_info.h"

//
// Created by centforever on 2022/3/31.
//
#define LOGE(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, "CheckSign", __VA_ARGS__))

#define UNKNOWN "$unknown"

/**
 * 判断文件是否存在
 * path: 路径
 * 返回值 1:文件存在; 0:文件不存在
 */
int exists(const char *path) { return access(path, F_OK) == 0 ? 1 : 0; }

int errorCatch(JNIEnv *env) {
  if (env->ExceptionCheck()) {
    env->ExceptionDescribe();
    env->ExceptionClear();
    return 1;
  }
  return -1;
}

/**
 * emulator64_arm64:/proc $ ls
1      117    139    152    192    21     22840  264  279  294  308   352  369
387  449  6     992          dynamic_debug  locks         swaps 10     12     14
154    193    21480  22868  265  28   295  309   353  37   389  450  679   9936
execdomains    mdstat        sys 1012   122    140    1562   19382  21482  22916
266  280  296  31    354  370  39   452  8     asound       filesystems meminfo
sysrq-trigger 10140  123    142    1572   194    21499  22937  267  281  297 310
355  371  391  453  824   bootconfig   fs             misc          thread-self
10141  124    143    16     195    21575  22944  268  282  298  311   356  373
393  454  83    buddyinfo    interrupts     modules       timer_list 1025 12414
144    1632   199    216    22953  269  283  299  312   357  375  394  455  832
bus          iomem          mounts        tty 1052   125    145    167    19919
217    22959  27   284  3    313   358  377  4    460  833   cgroups ioports net
uid_cputime 1079   126    146    17     2      218    22997  270  285  30   314
359  378  40   461  84    cmdline      irq            pagetypeinfo  uid_io 109
12636  1463   172    20     219    23     271  286  300  315   36   379  401 462
85    config.gz    kallsyms       partitions    uid_procstat 11     13     147
179    200    22     241    272  287  301  316   360  38   405  463  86 consoles
key-users      pressure      uptime 112    131    148    18     201    22168 242
273  288  302  3168  361  380  41   464  87    cpuinfo      keys sched_debug
version 1128   132    149    188    202    22170  249    274  289  303  3314 362
381  413  465  8710  crypto       kmsg           schedstat     vmallocinfo 113
133    1492   189    203    22233  25     275  290  304  345   365  382  443 467
8721  device-tree  kpagecgroup    self          vmstat 114    135    15     190
204    22333  250    276  291  305  349   366  383  445  468  88    devices
kpagecount     slabinfo      zoneinfo 115    137    150    191    208    22495
256    277  292  306  350   367  384  446  482  8921  diskstats    kpageflags
softirqs 116    138    15102  19135  20914  22525  26     278  293  307  351 368
386  447  552  8996  driver       loadavg        stat emulator64_arm64:/proc $
 * 获取当前 app 包名
 * @param packageName
 * @return
 */
int getPackageName(char *packageName) {
  char path[64];
  sprintf(path, "/proc/%d/cmdline", getpid());

  FILE *f;
  f = fopen(path, "r");
  if (f == nullptr) {
    return 1;
  }
  fgets(packageName, BUF_SIZE_64, f);
  LOGE("checkMoreOpenByUid:%s", packageName);
  fclose(f);
  return 0;
}

/**
 * 0. 多开检测 false
 * 1. 多开检测 true
 * 2. 检测失败（$unknown）
 * 检测多开, 若可访问规定目录则为正常，否则为多开环境
 * @return
 */
int moreOpenCheck() {
  // 判断是否支持ls命令
  if (exists("/system/bin/ls")) {
    char packageName[BUF_SIZE_64] = UNKNOWN;
    if (getPackageName(packageName) != 0) {
      return 2;
    }
    char path[BUF_SIZE_128];
    sprintf(path, "ls /data/data/%s", packageName);
    FILE *f;
    f = popen(path, "r");
    if (f == nullptr) {
      LOGE("file pointer is nullptr.");
      return 2;
    } else {
      // 读取 shell 命令内容
      char buff[BUF_SIZE_32];
      if (fgets(buff, BUF_SIZE_32, f) == nullptr) {
        LOGE("ls data error: %s", strerror(errno));
        pclose(f);
        return 1;
      }
      LOGE("ls data: %s", buff);
      if (strlen(buff) == 0) {
        pclose(f);
        return 1;
      } else {
        pclose(f);
        return 0;
      }
    }
  } else {
    return 2;
  }
}

extern "C" JNIEXPORT jint JNICALL
Java_com_mgg_checkenv_multiopen_CheckMultiOpen_checkByShellCommandAccessInternalStorageDirectoryOpenCheck(
    JNIEnv *, jobject) {
  return moreOpenCheck();
}

/**
 * 获取包名
 * @param env <JNI函数>
 * @return string
 */
jstring getPackageName(JNIEnv *env, jobject context) {
  jclass clazz = env->GetObjectClass(context);
  if (clazz == nullptr) {
    LOGE("getPackageName: the Context class is nullptr.");
    errorCatch(env);
    return nullptr;
  }
  jmethodID mId =
      env->GetMethodID(clazz, "getPackageName", "()Ljava/lang/String;");
  if (mId == nullptr) {
    LOGE("getPackageName: the getPackageName method is nullptr.");
    errorCatch(env);
    return nullptr;
  }
  auto packageName = (jstring)env->CallObjectMethod(context, mId);
  env->DeleteLocalRef(clazz);
  return packageName;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_mgg_checkenv_multiopen_CheckMultiOpen_checkByShellCommandAccessInternalStorageDirectoryOpenCheckExt(
    JNIEnv *env, jobject, jobject context) {
  // 判断是否支持ls命令
  if (exists("/system/bin/ls")) {
    char path[BUF_SIZE_256] = {0};
    jstring packageName = getPackageName(env, context);
    if (packageName == nullptr) {
      return -1;
    }
    const char *name = env->GetStringUTFChars(packageName, 0);
    sprintf(path, "ls /data/data/%s", name);
    LOGE("moreOpenCheck: path %s", path);
    FILE *f;
    f = popen(path, "r");
    if (f != nullptr) {
      char buff[BUF_SIZE_32];
      if (fgets(buff, BUF_SIZE_32, f)) {
        if (strlen(buff) != 0) {
          LOGE("moreOpenCheck: buff %s", buff);
          pclose(f);
          env->ReleaseStringUTFChars(packageName, name);
          return 0;
        }
      }
      pclose(f);
      env->ReleaseStringUTFChars(packageName, name);
      return 1;
    } else {
      LOGE("file pointer is nullptr.");
      env->ReleaseStringUTFChars(packageName, name);
      return -1;
    }
  } else {
    return -1;
  }
}

// 反系统级应用多开
bool isSystemDualApp() { return 0 != getuid() / 100000; }

// 反用户级应用多开
bool isUserDualApp(const std::string &dataDir) {
  return 0 == access((dataDir + "/../").c_str(), R_OK);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_mgg_checkenv_multiopen_CheckMultiOpen_isNativeCheckSystemDualApp(
    JNIEnv *, jobject) {
  return isSystemDualApp();
}
extern "C" JNIEXPORT jboolean JNICALL
Java_com_mgg_checkenv_multiopen_CheckMultiOpen_isNativeCheckUserDualAppByDataDir(
    JNIEnv *env, jobject, jstring data_dir) {
  return isUserDualApp(
      FOREVER::STRING_CONVERT::JavaStringToString(env, data_dir));
}

extern "C" JNIEXPORT jint JNICALL
Java_com_mgg_checkenv_multiopen_CheckMultiOpen_checkMoreOpenByUid(JNIEnv *,
                                                                  jobject) {
  return checkMoreOpenByUid();
}
