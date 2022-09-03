#include <jni.h>

#include <cassert>
#include <chrono>
#include <ctime>
#include <iostream>
#include <string>
#include <variant>

#include "MainViewModel.h"
#include "jni_string.hpp"
#include "log/log_utils.h"

static void finalize_mixed(jlong ptr) {
  delete reinterpret_cast<ViewModel *>(ptr);
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_mgg_environmentcheck_QtNativeViewModel_nativeGetFinalizerPtr(
    JNIEnv *env, jobject clazz) {
  return reinterpret_cast<jlong>(&finalize_mixed);
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_mgg_environmentcheck_QtNativeViewModel_nativeRelease(
    JNIEnv *env, jobject clazz, jlong nativePeer) {
  auto value = reinterpret_cast<ViewModel *>(nativePeer);
  delete value;
  return 0;
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_mgg_environmentcheck_QtNativeViewModel_nativeCreate(
    JNIEnv *env, jobject clazz, jstring view_model_type) {
  LOGE("QtNativeViewModel_nativeCreate");
  return reinterpret_cast<jlong>(new MainViewModel());
}

extern "C" JNIEXPORT void JNICALL
Java_com_mgg_environmentcheck_QtNativeViewModel_nativeBind(
    JNIEnv *env, jobject clazz, jlong nativePeer,
    jobject bind_java_view_model) {
  LOGE("QtNativeViewModel_nativeBind");
  auto viewModel = reinterpret_cast<ViewModel *>(nativePeer);
  viewModel->setJavaViewModel(bind_java_view_model);
  viewModel->setViewModelAttached(true);
  viewModel->bind();
}

extern "C" JNIEXPORT void JNICALL
Java_com_mgg_environmentcheck_QtNativeViewModel_nativeUnBind(JNIEnv *env,
                                                             jobject clazz,
                                                             jlong native_ptr) {
  auto viewModel = reinterpret_cast<ViewModel *>(native_ptr);
  viewModel->setViewModelAttached(false);
  viewModel->unBind();
}

extern "C" JNIEXPORT void JNICALL
Java_com_mgg_environmentcheck_QtNativeViewModel_nativeHandleIntKey(
    JNIEnv *env, jobject clazz, jlong native_ptr, jint key, jstring value) {
  auto viewModel = reinterpret_cast<ViewModel *>(native_ptr);
  viewModel->handle(
      (int)key,
      FOREVER::STRING_CONVERT::JavaStringToString(env, value).c_str());
}
