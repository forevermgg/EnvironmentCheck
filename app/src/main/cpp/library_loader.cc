// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// This is called by the VM when the shared library is first loaded.
JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved) {
  // Initialize the Java VM.
  FOREVER::jni::InitJavaVM(vm);

  JNIEnv* env = FOREVER::jni::AttachCurrentThread();
  bool result = false;

  // Register FlutterMain.
  result = flutter::FlutterMain::Register(env);
  FOREVER_CHECK(result);

  // Register PlatformView
  result = flutter::PlatformViewAndroid::Register(env);
  FOREVER_CHECK(result);

  // Register VSyncWaiter.
  result = flutter::VsyncWaiterAndroid::Register(env);
  FOREVER_CHECK(result);

  // Register AndroidImageDecoder.
  result = flutter::AndroidImageGenerator::Register(env);
  FOREVER_CHECK(result);

  return JNI_VERSION_1_6;
}
