// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_PLATFORM_ANDROID_MESSAGE_LOOP_ANDROID_H_
#define FOREVER_PLATFORM_ANDROID_MESSAGE_LOOP_ANDROID_H_

#include <android/looper.h>

#include <atomic>

#include "../macros.h"
#include "../message_loop_impl.h"
#include "../unique_fd.h"

namespace FOREVER {

struct UniqueLooperTraits {
  static ALooper* InvalidValue() { return nullptr; }
  static bool IsValid(ALooper* value) { return value != nullptr; }
  static void Free(ALooper* value) { ::ALooper_release(value); }
};

/// Android implementation of \p MessageLoopImpl.
///
/// This implemenation wraps usage of Android's \p looper.
/// \see https://developer.android.com/ndk/reference/group/looper
class MessageLoopAndroid : public MessageLoopImpl {
 private:
  FOREVER::UniqueObject<ALooper*, UniqueLooperTraits> looper_;
  FOREVER::UniqueFD timer_fd_;
  bool running_;

  MessageLoopAndroid();

  ~MessageLoopAndroid() override;

  void Run() override;

  void Terminate() override;

  void WakeUp(FOREVER::TimePoint time_point) override;

  void OnEventFired();

  FML_FRIEND_MAKE_REF_COUNTED(MessageLoopAndroid);
  FML_FRIEND_REF_COUNTED_THREAD_SAFE(MessageLoopAndroid);
  FOREVER_DISALLOW_COPY_AND_ASSIGN(MessageLoopAndroid);
};

}  // namespace FOREVER

#endif  // FOREVER_PLATFORM_ANDROID_MESSAGE_LOOP_ANDROID_H_
