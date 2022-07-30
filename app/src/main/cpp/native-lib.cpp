#include <android/log.h>
#include <jni.h>
#include <json/value.h>

#include <string>

#include "http.h"
#include "java_interop.h"
#include "json/json.h"
#include "log_utils.h"
#include "logging.h"
#include "qjniobject.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_mgg_environmentcheck_MainActivity_stringFromJNI(JNIEnv *env,
                                                         jobject /* this */) {
#if defined(__arm__)
#if defined(__ARM_ARCH_7A__)
#if defined(__ARM_NEON__)
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a/NEON (hard-float)"
#else
#define ABI "armeabi-v7a/NEON"
#endif
#else
#if defined(__ARM_PCS_VFP)
#define ABI "armeabi-v7a (hard-float)"
#else
#define ABI "armeabi-v7a"
#endif
#endif
#else
#define ABI "armeabi"
#endif
#elif defined(__i386__)
#define ABI "x86"
#elif defined(__x86_64__)
#define ABI "x86_64"
#elif defined(__mips64) /* mips64el-* toolchain defines __mips__ too */
#define ABI "mips64"
#elif defined(__mips__)
#define ABI "mips"
#elif defined(__aarch64__)
#define ABI "arm64-v8a"
#else
#define ABI "unknown"
#endif
  return (*env).NewStringUTF("Hello from JNI !  Compiled with ABI " ABI ".");
}

std::vector<std::string> get_change_titles(const std::string &cacert_path) {
  std::string error;
  auto result = curlssl::http::Client(cacert_path)
                    .get(
                        "https://android-review.googlesource.com/changes/"
                        "?q=status:open&n=10",
                        &error);
  if (!result) {
    return {error.c_str()};
  }

  // Strip XSSI defense prefix:
  // https://gerrit-review.googlesource.com/Documentation/rest-api.html#output
  const std::string payload = result.value().substr(5);

  Json::Value root;
  std::istringstream(payload) >> root;
  std::vector<std::string> titles;
  for (const auto &change : root) {
    titles.push_back(change["subject"].asString());
  }
  return titles;
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

extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_mgg_environmentcheck_MainActivity_getGerritChanges(
    JNIEnv *env, jobject thiz, jstring cacert_java) {
  if (cacert_java == nullptr) {
    curlssl::logging::FatalError(env, "cacert argument cannot be null");
  }

  const std::string cacert =
      curlssl::jni::Convert<std::string>::from(env, cacert_java);
  return curlssl::jni::Convert<jobjectArray, jstring>::from(
      env, get_change_titles(cacert));
}