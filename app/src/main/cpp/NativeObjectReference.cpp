#include <jni.h>

#include <iostream>

typedef void (*FinalizeFunc)(jlong);

extern "C" JNIEXPORT void JNICALL
Java_com_mgg_environmentcheck_NativeObjectReference_nativeCleanUp(
    JNIEnv *env, jclass clazz, jlong finalizer_ptr, jlong native_ptr) {
  auto finalize_func = reinterpret_cast<FinalizeFunc>(finalizer_ptr);
  finalize_func(native_ptr);
  std::cout << "finalize_func" << std::endl;
}
