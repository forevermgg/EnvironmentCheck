#include <jni.h>

#include "mobile_info.h"

//
// Created by centforever on 2022/4/21.
//
extern "C" JNIEXPORT jint JNICALL
Java_com_mgg_checkenv_hook_CheckHook_checkSubstrateBySo(JNIEnv *, jobject) {
  return checkSubstrateBySo();
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_mgg_checkenv_hook_CheckHook_checkHookByMap(JNIEnv *env, jobject) {
  return env->NewStringUTF(checkHookByMap().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_mgg_checkenv_hook_CheckHook_checkHookByPackage(JNIEnv *env, jobject) {
  return env->NewStringUTF(checkHookByPackage().c_str());
}
