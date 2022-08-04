// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#include "../synchronization/semaphore.h"
#include "../eintr_wrapper.h"
#include "../log/logging.h"
#include <semaphore.h>

namespace FOREVER {

class PlatformSemaphore {
 public:
  explicit PlatformSemaphore(uint32_t count)
      : valid_(::sem_init(&sem_, 0 /* not shared */, count) == 0) {}

  ~PlatformSemaphore() {
    if (valid_) {
      int result = ::sem_destroy(&sem_);
      (void)result;
      // Can only be EINVAL which should not be possible since we checked for
      // validity.
      FOREVER_DCHECK(result == 0);
    }
  }

  bool IsValid() const { return valid_; }

  bool Wait() {
    if (!valid_) {
      return false;
    }

    return FOREVER_HANDLE_EINTR(::sem_wait(&sem_)) == 0;
  }

  bool TryWait() {
    if (!valid_) {
      return false;
    }

    return FOREVER_HANDLE_EINTR(::sem_trywait(&sem_)) == 0;
  }

  void Signal() {
    if (!valid_) {
      return;
    }

    ::sem_post(&sem_);

    return;
  }

 private:
  bool valid_;
  sem_t sem_;

  FOREVER_DISALLOW_COPY_AND_ASSIGN(PlatformSemaphore);
};

}  // namespace FOREVER
namespace FOREVER {

Semaphore::Semaphore(uint32_t count) : _impl(new PlatformSemaphore(count)) {}

Semaphore::~Semaphore() = default;

bool Semaphore::IsValid() const {
  return _impl->IsValid();
}

bool Semaphore::Wait() {
  return _impl->Wait();
}

bool Semaphore::TryWait() {
  return _impl->TryWait();
}

void Semaphore::Signal() {
  return _impl->Signal();
}

}  // namespace FOREVER
