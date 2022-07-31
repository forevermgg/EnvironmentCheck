// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// A class for checking that the current thread is/isn't the same as an initial
// thread.

#ifndef FOREVER_MEMORY_WEAK_PTR_H_
#define FOREVER_MEMORY_WEAK_PTR_H_
#include <pthread.h>

#include "../log/logging.h"
#include "../macros.h"

namespace FOREVER {

// A simple class that records the identity of the thread that it was created
// on, and at later points can tell if the current thread is the same as its
// creation thread. This class is thread-safe.
//
// Note: Unlike Chromium's |base::ThreadChecker|, this is *not* Debug-only (so
// #ifdef it out if you want something Debug-only). (Rationale: Having a
// |CalledOnValidThread()| that lies in Release builds seems bad. Moreover,
// there's a small space cost to having even an empty class. )
class ThreadChecker final {
 public:
  ThreadChecker() : self_(pthread_self()) {}
  ~ThreadChecker() {}

  // Returns true if the current thread is the thread this object was created
  // on and false otherwise.
  bool IsCreationThreadCurrent() const {
    pthread_t current_thread = pthread_self();
    bool is_creation_thread_current = !!pthread_equal(current_thread, self_);
    return is_creation_thread_current;
  }

 private:
  pthread_t self_;
};

#if !defined(NDEBUG)
#define FOREVER_DECLARE_THREAD_CHECKER(c) FOREVER::ThreadChecker c
#define FOREVER_DCHECK_CREATION_THREAD_IS_CURRENT(c) \
  FOREVER_DCHECK(pthread_equal(c).IsCreationThreadCurrent())
#else
#define FOREVER_DECLARE_THREAD_CHECKER(c)
#define FOREVER_DCHECK_CREATION_THREAD_IS_CURRENT(c) ((void)0)
#endif

}  // namespace FOREVER

#endif  // FOREVER_MEMORY_WEAK_PTR_H_
