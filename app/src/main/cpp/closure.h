// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_CLOSURE_H_
#define FOREVER_CLOSURE_H_

#include <functional>

#include "macros.h"

namespace FOREVER {

using closure = std::function<void()>;

//------------------------------------------------------------------------------
/// @brief      Wraps a closure that is invoked in the destructor unless
///             released by the caller.
///
///             This is especially useful in dealing with APIs that return a
///             resource by accepting ownership of a sub-resource and a closure
///             that releases that resource. When such APIs are chained, each
///             link in the chain must check that the next member in the chain
///             has accepted the resource. If not, it must invoke the closure
///             eagerly. Not doing this results in a resource leak in the
///             erroneous case. Using this wrapper, the closure can be released
///             once the next call in the chain has successfully accepted
///             ownership of the resource. If not, the closure gets invoked
///             automatically at the end of the scope. This covers the cases
///             where there are early returns as well.
///
class ScopedCleanupClosure {
 public:
  ScopedCleanupClosure() = default;

  explicit ScopedCleanupClosure(const FOREVER::closure& closure)
      : closure_(closure) {}

  ~ScopedCleanupClosure() {
    if (closure_) {
      closure_();
    }
  }

  FOREVER::closure SetClosure(const FOREVER::closure& closure) {
    auto old_closure = closure_;
    closure_ = closure;
    return old_closure;
  }

  FOREVER::closure Release() {
    FOREVER::closure closure = closure_;
    closure_ = nullptr;
    return closure;
  }

 private:
  FOREVER::closure closure_;

  FOREVER_DISALLOW_COPY_AND_ASSIGN(ScopedCleanupClosure);
};

}  // namespace FOREVER

#endif  // FOREVER_CLOSURE_H_
