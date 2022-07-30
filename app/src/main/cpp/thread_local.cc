// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#include <iostream>
#include "thread_local.h"

namespace FOREVER {
namespace internal {

ThreadLocalPointer::ThreadLocalPointer(void (*destroy)(void*)) {
  assert(pthread_key_create(&key_, destroy) == 0);
}

ThreadLocalPointer::~ThreadLocalPointer() {
  assert(pthread_key_delete(key_) == 0);
}

void* ThreadLocalPointer::get() const {
  return pthread_getspecific(key_);
}

void* ThreadLocalPointer::swap(void* ptr) {
  void* old_ptr = get();
  int err = pthread_setspecific(key_, ptr);
  if (err) {
    std::cout << "pthread_setspecific failed (" << err
                << "): " << strerror(err);
    assert(false);
  }
  return old_ptr;
}

}  // namespace internal
}  // namespace FOREVER

