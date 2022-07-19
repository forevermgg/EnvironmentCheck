#include <android/log.h>
#include <jni.h>

#include <string>

#include "log_utils.h"
#include "qjniobject.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_mgg_environmentcheck_MainActivity_stringFromJNI(JNIEnv *env,
                                                         jobject /* this */) {
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

jobject getApplication(JNIEnv *env) {
  jobject application = nullptr;
  jclass activity_thread_clz = env->FindClass("android/app/ActivityThread");
  if (activity_thread_clz != nullptr) {
    jmethodID currentApplication =
        env->GetStaticMethodID(activity_thread_clz, "currentApplication",
                               "()Landroid/app/Application;");
    if (currentApplication != nullptr) {
      application =
          env->CallStaticObjectMethod(activity_thread_clz, currentApplication);
    } else {
      printf("Cannot find method: currentApplication() in ActivityThread.");
    }
    env->DeleteLocalRef(activity_thread_clz);
  } else {
    printf("Cannot find class: android.app.ActivityThread");
  }
  return application;
}

extern "C" JNIEXPORT void JNICALL
Java_com_mgg_environmentcheck_MainActivity_testToast(JNIEnv *env,
                                                     jobject activity) {
  QJniObject javaString = QJniObject::fromString("test toast");
  QJniObject toast = QJniObject::callStaticObjectMethod(
      "android/widget/Toast", "makeText",
      "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/"
      "Toast;",
      getApplication(env), javaString.object(), jint(1));
  toast.callMethod<void>("show");
}