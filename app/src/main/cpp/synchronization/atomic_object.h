// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_SYNCHRONIZATION_ATOMIC_OBJECT_H_
#define FOREVER_SYNCHRONIZATION_ATOMIC_OBJECT_H_

#include <mutex>

namespace FOREVER {

// A wrapper for an object instance that can be read or written atomically.
template <typename T>
class AtomicObject {
 public:
  AtomicObject() = default;
  explicit AtomicObject(T object) : object_(object) {}

  T Load() const {
    std::scoped_lock lock(mutex_);
    return object_;
  }

  void Store(const T& object) {
    std::scoped_lock lock(mutex_);
    object_ = object;
  }

 private:
  mutable std::mutex mutex_;
  T object_;
};

}  // namespace FOREVER

#endif  // FOREVER_SYNCHRONIZATION_ATOMIC_OBJECT_H_
