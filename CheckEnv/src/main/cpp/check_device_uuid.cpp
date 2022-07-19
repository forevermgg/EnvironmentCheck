#include <jni.h>

#include "jni_string.hpp"
#include "mobile_info.h"
//
// Created by centforever on 2022/5/26.
//
extern "C" JNIEXPORT jstring JNICALL
Java_com_mgg_checkenv_uuid_CheckDevicesUUID_nativeCheckRandomUUId(JNIEnv *env,
                                                                  jobject) {
  return FOREVER::STRING_CONVERT::StringToJavaString(env, getUuid());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_mgg_checkenv_uuid_CheckDevicesUUID_nativeCheckRandomBootId(JNIEnv *env,
                                                                    jobject) {
  return FOREVER::STRING_CONVERT::StringToJavaString(env, getBootId());
}
