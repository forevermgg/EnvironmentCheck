// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// Internal implementation details for ref_counted.h.

#ifndef FOREVER_MEMORY_REF_COUNTED_INTERNAL_H_
#define FOREVER_MEMORY_REF_COUNTED_INTERNAL_H_

#include <atomic>
#include <cassert>

#include "../log/logging.h"
#include "../macros.h"

namespace FOREVER {
namespace internal {

// See ref_counted.h for comments on the public methods.
class RefCountedThreadSafeBase {
 public:
  // AddRef，增加引用计数，同时确保操作的原子性
  void AddRef() const {
#ifndef NDEBUG
    FOREVER_DCHECK(!adoption_required_);
    FOREVER_DCHECK(!destruction_started_);
#endif
    // std::memory_order_relaxed，仅保证此操作的原子性
    ref_count_.fetch_add(1u, std::memory_order_relaxed);
  }

  bool HasOneRef() const {
    return ref_count_.load(std::memory_order_acquire) == 1u;
  }

  void AssertHasOneRef() const { FOREVER_DCHECK(HasOneRef()); }

 protected:
  RefCountedThreadSafeBase();
  ~RefCountedThreadSafeBase();

  // Returns true if the object should self-delete.
  // Release，减少引用计数，同时确保操作的原子性
  bool Release() const {
#ifndef NDEBUG
    FOREVER_DCHECK(!adoption_required_);
    FOREVER_DCHECK(!destruction_started_);
#endif
    FOREVER_DCHECK(ref_count_.load(std::memory_order_acquire) != 0u);
    // TODO(vtl): We could add the following:
    //     if (ref_count_.load(std::memory_order_relaxed) == 1u) {
    // #ifndef NDEBUG
    //       destruction_started_= true;
    // #endif
    //       return true;
    //     }
    // This would be correct. On ARM (an Nexus 4), in *single-threaded* tests,
    // this seems to make the destruction case marginally faster (barely
    // measurable), and while the non-destruction case remains about the same
    // (possibly marginally slower, but my measurements aren't good enough to
    // have any confidence in that). I should try multithreaded/multicore tests.
    // 同样的，在Release函数中，也会使用到确保操作原子性的调用。
    if (ref_count_.fetch_sub(1u, std::memory_order_release) == 1u) {
      std::atomic_thread_fence(std::memory_order_acquire);
#ifndef NDEBUG
      destruction_started_ = true;
#endif
      return true;
    }
    return false;
  }

#ifndef NDEBUG
  void Adopt() {
    FOREVER_DCHECK(adoption_required_);
    adoption_required_ = false;
  }
#endif

 private:
  // atomic_uint_fast32_t 实际上是
  // std::atomic<uint_fast32_t>，表示当前类型可以进行原子操作。
  // ref_count_，在初始化时会默认为1u
  mutable std::atomic_uint_fast32_t ref_count_;

#ifndef NDEBUG
  mutable bool adoption_required_;
  mutable bool destruction_started_;
#endif

  FOREVER_DISALLOW_COPY_AND_ASSIGN(RefCountedThreadSafeBase);
};

inline RefCountedThreadSafeBase::RefCountedThreadSafeBase()
    : ref_count_(1u)
#ifndef NDEBUG
      ,
      adoption_required_(true),
      destruction_started_(false)
#endif
{
}

inline RefCountedThreadSafeBase::~RefCountedThreadSafeBase() {
#ifndef NDEBUG
  FOREVER_DCHECK(!adoption_required_);
  // Should only be destroyed as a result of |Release()|.
  FOREVER_DCHECK(!destruction_started_);
#endif
}

}  // namespace internal
}  // namespace FOREVER

#endif  // FOREVER_MEMORY_REF_COUNTED_INTERNAL_H_
