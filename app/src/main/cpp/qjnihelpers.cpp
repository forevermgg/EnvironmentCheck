#include <android/log.h>

#include <deque>
#include <memory>

#include "qjnienvironment.h"
#include "qjnihelpers_p.h"
#include "qjniobject.h"

QT_BEGIN_NAMESPACE

namespace QtAndroidPrivate {}  // namespace QtAndroidPrivate

static JavaVM *g_javaVM = nullptr;
static jobject g_jActivity = nullptr;
static jobject g_jService = nullptr;
static jobject g_jClassLoader = nullptr;

jint QtAndroidPrivate::initJNI(JavaVM *vm, JNIEnv *env) {
  g_javaVM = vm;

  jclass jQtNative = env->FindClass("com/mgg/environmentcheck/QtNative");

  if (QJniEnvironment::checkAndClearExceptions(env)) return JNI_ERR;

  /*jmethodID activityMethodID =
      env->GetStaticMethodID(jQtNative, "activity", "()Landroid/app/Activity;");

  if (QJniEnvironment::checkAndClearExceptions(env)) return JNI_ERR;

  jobject activity = env->CallStaticObjectMethod(jQtNative, activityMethodID);

  if (QJniEnvironment::checkAndClearExceptions(env)) return JNI_ERR;*/

  /*jmethodID serviceMethodID =
      env->GetStaticMethodID(jQtNative, "service", "()Landroid/app/Service;");

  if (QJniEnvironment::checkAndClearExceptions(env)) return JNI_ERR;

  jobject service = env->CallStaticObjectMethod(jQtNative, serviceMethodID);

  if (QJniEnvironment::checkAndClearExceptions(env)) return JNI_ERR;*/

  jmethodID classLoaderMethodID = env->GetStaticMethodID(
      jQtNative, "classLoader", "()Ljava/lang/ClassLoader;");

  if (QJniEnvironment::checkAndClearExceptions(env)) return JNI_ERR;

  jobject classLoader =
      env->CallStaticObjectMethod(jQtNative, classLoaderMethodID);
  if (QJniEnvironment::checkAndClearExceptions(env)) return JNI_ERR;

  g_jClassLoader = env->NewGlobalRef(classLoader);
  env->DeleteLocalRef(classLoader);
  /*if (activity) {
    g_jActivity = env->NewGlobalRef(activity);
    env->DeleteLocalRef(activity);
  }
  if (service) {
    g_jService = env->NewGlobalRef(service);
    env->DeleteLocalRef(service);
  }*/
  env->DeleteLocalRef(jQtNative);
  if (QJniEnvironment::checkAndClearExceptions(env)) return JNI_ERR;
  return JNI_OK;
}

jobject QtAndroidPrivate::activity() { return g_jActivity; }

jobject QtAndroidPrivate::service() { return g_jService; }

jobject QtAndroidPrivate::context() {
  if (g_jActivity) return g_jActivity;
  if (g_jService) return g_jService;

  return 0;
}

JavaVM *QtAndroidPrivate::javaVM() { return g_javaVM; }

jobject QtAndroidPrivate::classLoader() { return g_jClassLoader; }

jint QtAndroidPrivate::androidSdkVersion() {
  static jint sdkVersion = 0;
  if (!sdkVersion)
    sdkVersion =
        QJniObject::getStaticField<jint>("android/os/Build$VERSION", "SDK_INT");
  return sdkVersion;
}

jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
  static const char logTag[] = "QtCore";
  static bool initialized = false;
  if (initialized) return JNI_VERSION_1_6;
  initialized = true;

  typedef union {
    JNIEnv *nenv;
    void *venv;
  } _JNIEnv;

  __android_log_print(ANDROID_LOG_INFO, logTag, "Start");

  _JNIEnv uenv;
  uenv.venv = nullptr;

  if (vm->GetEnv(&uenv.venv, JNI_VERSION_1_6) != JNI_OK) {
    __android_log_print(ANDROID_LOG_FATAL, logTag, "GetEnv failed");
    return JNI_ERR;
  }

  JNIEnv *env = uenv.nenv;
  const jint ret = QT_PREPEND_NAMESPACE(QtAndroidPrivate::initJNI(vm, env));
  if (ret != 0) {
    __android_log_print(ANDROID_LOG_FATAL, logTag, "initJNI failed");
    return ret;
  }

  return JNI_VERSION_1_6;
}

QT_END_NAMESPACE
