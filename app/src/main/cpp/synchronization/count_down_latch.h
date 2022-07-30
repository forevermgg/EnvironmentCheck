// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_SYNCHRONIZATION_COUNT_DOWN_LATCH_H_
#define FOREVER_SYNCHRONIZATION_COUNT_DOWN_LATCH_H_

#include <atomic>

#include "../macros.h"
#include "../synchronization/waitable_event.h"

namespace FOREVER {

class CountDownLatch {
 public:
  explicit CountDownLatch(size_t count);

  ~CountDownLatch();

  void Wait();

  void CountDown();

 private:
  std::atomic_size_t count_;
  ManualResetWaitableEvent waitable_event_;

  FOREVER_DISALLOW_COPY_AND_ASSIGN(CountDownLatch);
};

}  // namespace FOREVER

#endif  // FOREVER_SYNCHRONIZATION_COUNT_DOWN_LATCH_H_
