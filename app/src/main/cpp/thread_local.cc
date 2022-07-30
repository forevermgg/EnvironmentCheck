// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#include <iostream>
#include "thread_local.h"
#include "logging.h"

namespace FOREVER {
namespace internal {

ThreadLocalPointer::ThreadLocalPointer(void (*destroy)(void*)) {
  FOREVER_CHECK(pthread_key_create(&key_, destroy) == 0);
}

ThreadLocalPointer::~ThreadLocalPointer() {
  FOREVER_CHECK(pthread_key_delete(key_) == 0);
}

void* ThreadLocalPointer::get() const {
  return pthread_getspecific(key_);
}

void* ThreadLocalPointer::swap(void* ptr) {
  void* old_ptr = get();
  int err = pthread_setspecific(key_, ptr);
  if (err) {
    FOREVER_CHECK(false) << "pthread_setspecific failed (" << err
                << "): " << strerror(err);
    FOREVER_CHECK(false);
  }
  return old_ptr;
}

}  // namespace internal
}  // namespace FOREVER

