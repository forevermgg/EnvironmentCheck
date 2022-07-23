#ifndef _LOG_UTILS_H_
#define _LOG_UTILS_H_

#include <android/log.h>
#include <string.h>
#include <sys/syscall.h>

#define DEBUG  // 可以通过 CmakeLists.txt
// 等方式来定义在这个宏，实现动态打开和关闭LOG

// Windows 和 Linux 这两个宏是在 CMakeLists.txt 通过 ADD_DEFINITIONS 定义的
#ifdef Windows
#define __FILENAME__ \
  (strrchr(__FILE__, '\\') + 1)  // Windows下文件目录层级是'\\'
#elif Linux
#define __FILENAME__ (strrchr(__FILE__, '/') + 1)  // Linux下文件目录层级是'/'
#else
#define __FILENAME__ (strrchr(__FILE__, '/') + 1)  // 默认使用这种方式
#endif

#ifdef DEBUG
#define TAG "JNI"
#define LOGV(format, ...)                                                \
  __android_log_print(ANDROID_LOG_VERBOSE, TAG, "[%s][%s][%d]: " format, \
                      __FILENAME__, __FUNCTION__, __LINE__, ##__VA_ARGS__);
#define LOGD(format, ...)                                              \
  __android_log_print(ANDROID_LOG_DEBUG, TAG, "[%s][%s][%d]: " format, \
                      __FILENAME__, __FUNCTION__, __LINE__, ##__VA_ARGS__);
#define LOGI(format, ...)                                             \
  __android_log_print(ANDROID_LOG_INFO, TAG, "[%s][%s][%d]: " format, \
                      __FILENAME__, __FUNCTION__, __LINE__, ##__VA_ARGS__);
#define LOGW(format, ...)                                             \
  __android_log_print(ANDROID_LOG_WARN, TAG, "[%s][%s][%d]: " format, \
                      __FILENAME__, __FUNCTION__, __LINE__, ##__VA_ARGS__);
#define LOGE(format, ...)                                              \
  __android_log_print(ANDROID_LOG_ERROR, TAG, "[%s][%s][%d]: " format, \
                      __FILENAME__, __FUNCTION__, __LINE__, ##__VA_ARGS__);
#else
#define LOGV(format, ...) ;
#define LOGD(format, ...) ;
#define LOGI(format, ...) ;
#define LOGW(format, ...) ;
#define LOGE(format, ...) ;
#endif  // DEBUG

#endif  // _LOG_UTILS_H_