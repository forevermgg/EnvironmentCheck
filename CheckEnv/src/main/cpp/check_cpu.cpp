#include <jni.h>
#include <unistd.h>

#include <string>
//
// Created by centforever on 2022/3/31.
//

// __arm__     armeabi
// __arm__     armeabi-v7
// __aarch64__ arm64-v8a
// __i386__    x86
// __x86_64__  x86_64
extern "C" JNIEXPORT jstring JNICALL
Java_com_mgg_checkenv_cpu_CheckCpu_nativeCheckCpuAbi(JNIEnv *env, jobject) {
  std::string cpuAbi;
#if defined(__i386__)
  cpuAbi = "x86";
#elif defined(__x86_64__)
  cpuAbi = "x86_64";
#elif defined(__aarch64__)
  cpuAbi = "arm64-v8a";
#elif defined(__arm__)
  cpuAbi = "armeabi-v7";
#else
  cpuAbi = "un know";
#endif
  return env->NewStringUTF(cpuAbi.c_str());
}

/*
而在Linux平台下，我们可以使用sysconf()来获取处理器核数。

sysconf( )有unistd.h提供，要使用该函数需要#include<unistd.h>
其参数可以是 _SC_NPROCESSORS_CONF，也可以是 _SC_NPROCESSORS_ONLN。两者差异：

_SC_NPROCESSORS_CONF --
返回系统可以使用的核数，但是其值会包括系统中禁用的核的数目，因此该值并不代表当前系统中可用的核数。

_SC_NPROCESSORS_ONLN -- 返回值真正的代表了系统 当前可用的核数
*/
uint32_t GetCPUCount() {
  static uint32_t ncpus = 0;
  // _SC_NPROCESSORS_CONF and _SC_NPROCESSORS_ONLN are common, but not
  // standard.
  if (ncpus == 0) {
#if defined(_SC_NPROCESSORS_CONF)
    long n = sysconf(_SC_NPROCESSORS_CONF);
    ncpus = (n > 0) ? uint32_t(n) : 1;
#elif defined(_SC_NPROCESSORS_ONLN)
    long n = sysconf(_SC_NPROCESSORS_ONLN);
    ncpus = (n > 0) ? uint32_t(n) : 1;
#else
    ncpus = 1;
#endif
  }
  return ncpus;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_mgg_checkenv_cpu_CheckCpu_getCpuCores(JNIEnv *, jobject) {
  return (jint)GetCPUCount();
}
