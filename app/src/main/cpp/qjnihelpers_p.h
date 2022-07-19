#ifndef QJNIHELPERS_H
#define QJNIHELPERS_H
#include <jni.h>

#include <functional>

QT_BEGIN_NAMESPACE

namespace QtAndroidPrivate {
jobject activity();
jobject service();
jobject context();
JavaVM *javaVM();
jint initJNI(JavaVM *vm, JNIEnv *env);
jclass findClass(const char *className, JNIEnv *env);
jobject classLoader();
jint androidSdkVersion();
}  // namespace QtAndroidPrivate

QT_END_NAMESPACE

#endif  // QJNIHELPERS_H
