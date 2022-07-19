#include <jni.h>

#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_mgg_checkenv_CheckEnv_stringFromJNI(JNIEnv *env, jobject) {
  std::string cpuAbi = "check env";
  return env->NewStringUTF(cpuAbi.c_str());
}
