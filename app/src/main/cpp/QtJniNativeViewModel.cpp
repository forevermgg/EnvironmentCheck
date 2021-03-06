#include <jni.h>

#include <cassert>
#include <chrono>
#include <ctime>
#include <iostream>
#include <string>
#include <variant>

#include "MainViewModel.h"
#include "jni_string.hpp"
#include "log_utils.h"

static void finalize_mixed(jlong ptr) {
  delete reinterpret_cast<ViewModel *>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_mgg_environmentcheck_QtNativeViewModel_nativeGetFinalizerPtr(
    JNIEnv *env, jclass clazz) {
  return reinterpret_cast<jlong>(&finalize_mixed);
}

extern "C" JNIEXPORT void JNICALL
Java_com_mgg_environmentcheck_QtNativeViewModel_nativeRelease(
    JNIEnv *env, jclass clazz, jlong nativePeer) {
  auto value = reinterpret_cast<ViewModel *>(nativePeer);
  value->unBind();
  delete value;
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_mgg_environmentcheck_QtNativeViewModel_nativeCreate(
    JNIEnv *env, jclass clazz, jstring view_model_type) {
  LOGE("QtNativeViewModel_nativeCreate");
  return reinterpret_cast<jlong>(new MainViewModel());
}

extern "C" JNIEXPORT void JNICALL
Java_com_mgg_environmentcheck_QtNativeViewModel_nativeBind(
    JNIEnv *env, jclass clazz, jlong nativePeer, jobject bind_java_view_model) {
  LOGE("QtNativeViewModel_nativeBind");
  auto viewModel = reinterpret_cast<ViewModel *>(nativePeer);
  viewModel->setJavaViewModel(bind_java_view_model);
  viewModel->bind();
}

extern "C" JNIEXPORT void JNICALL
Java_com_mgg_environmentcheck_QtNativeViewModel_nativeUnBind(JNIEnv *env,
                                                             jclass clazz,
                                                             jlong native_ptr) {
  auto viewModel = reinterpret_cast<ViewModel *>(native_ptr);
  viewModel->setViewModelAttached(false);
}

extern "C" JNIEXPORT void JNICALL
Java_com_mgg_environmentcheck_QtNativeViewModel_nativeHandleIntKey(
    JNIEnv *env, jclass clazz, jlong native_ptr, jint key, jstring value) {
  auto viewModel = reinterpret_cast<ViewModel *>(native_ptr);
  viewModel->handle(
      (int)key,
      FOREVER::STRING_CONVERT::JavaStringToString(env, value).c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_mgg_environmentcheck_QtNativeViewModel_nativeHandleStringKey(
    JNIEnv *env, jclass clazz, jlong native_ptr, jstring key, jstring value) {
  auto viewModel = reinterpret_cast<ViewModel *>(native_ptr);
  viewModel->handle(
      FOREVER::STRING_CONVERT::JavaStringToString(env, key).c_str(),
      FOREVER::STRING_CONVERT::JavaStringToString(env, value).c_str());
}