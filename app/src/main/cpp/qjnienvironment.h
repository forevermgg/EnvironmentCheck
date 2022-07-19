#ifndef QJNI_ENVIRONMENT_H
#define QJNI_ENVIRONMENT_H
#include <jni.h>

#include <memory>

#include "qglobal.h"
#include "qtnamespacemacros.h"

QT_BEGIN_NAMESPACE

class QJniEnvironmentPrivate;

class QJniEnvironment {
 public:
  QJniEnvironment();
  ~QJniEnvironment();
  bool isValid() const;
  JNIEnv *operator->() const;
  JNIEnv &operator*() const;
  JNIEnv *jniEnv() const;
  jclass findClass(const char *className);
  jmethodID findMethod(jclass clazz, const char *methodName,
                       const char *signature);
  jmethodID findStaticMethod(jclass clazz, const char *methodName,
                             const char *signature);
  jfieldID findField(jclass clazz, const char *fieldName,
                     const char *signature);
  jfieldID findStaticField(jclass clazz, const char *fieldName,
                           const char *signature);
  static JavaVM *javaVM();
  bool registerNativeMethods(const char *className,
                             const JNINativeMethod methods[], int size);
  bool registerNativeMethods(jclass clazz, const JNINativeMethod methods[],
                             int size);

  enum class OutputMode { Silent, Verbose };

  bool checkAndClearExceptions(OutputMode outputMode = OutputMode::Verbose);
  static bool checkAndClearExceptions(
      JNIEnv *env, OutputMode outputMode = OutputMode::Verbose);

 private:
  Q_DISABLE_COPY_MOVE(QJniEnvironment)
  std::shared_ptr<QJniEnvironmentPrivate> d;
};

QT_END_NAMESPACE
#endif  // QJNI_ENVIRONMENT_H
