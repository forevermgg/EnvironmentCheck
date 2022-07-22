#include <android/log.h>
#include <jni.h>

#include <string>
#include <json/value.h>

#include "log_utils.h"
#include "qjniobject.h"
#include "http.h"
#include "java_interop.h"
#include "json/json.h"
#include "logging.h"

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

std::vector<std::string> get_change_titles(const std::string& cacert_path) {
  std::string error;
  auto result = curlssl::http::Client(cacert_path)
          .get(
                  "http://android-review.googlesource.com/changes/"
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
  for (const auto& change : root) {
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
Java_com_mgg_environmentcheck_MainActivity_getGerritChanges(JNIEnv *env, jobject thiz, jstring cacert_java) {
  if (cacert_java == nullptr) {
    curlssl::logging::FatalError(env, "cacert argument cannot be null");
  }

  const std::string cacert =
          curlssl::jni::Convert<std::string>::from(env, cacert_java);
  return curlssl::jni::Convert<jobjectArray, jstring>::from(env,
                                                   get_change_titles(cacert));
}