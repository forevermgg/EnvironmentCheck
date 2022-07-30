// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_SYNCHRONIZATION_SHARED_MUTEX_STD_H_
#define FOREVER_SYNCHRONIZATION_SHARED_MUTEX_STD_H_

#include <shared_mutex>

#include "../synchronization/shared_mutex.h"

namespace FOREVER {

class SharedMutexStd : public SharedMutex {
 public:
  virtual void Lock();
  virtual void LockShared();
  virtual void Unlock();
  virtual void UnlockShared();

 private:
  friend SharedMutex* SharedMutex::Create();
  SharedMutexStd() = default;

  std::shared_timed_mutex mutex_;
};

}  // namespace FOREVER

#endif  // FOREVER_SYNCHRONIZATION_SHARED_MUTEX_STD_H_
