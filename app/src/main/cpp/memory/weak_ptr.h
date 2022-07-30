// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

// This file provides weak pointers and weak pointer factories that work like
// Chromium's |base::WeakPtr<T>| and |base::WeakPtrFactory<T>|.

#ifndef FOREVER_MEMORY_WEAK_PTR_H_
#define FOREVER_MEMORY_WEAK_PTR_H_

#include <utility>

#include "ref_counted.h"
#include "task_runner_checker.h"
#include "thread_checker.h"
#include "weak_ptr_internal.h"
// 弱指针的设计比较有意思，增加了一个 WeakPtrFactory
// 来持有真正的指针，每次获取一个 WeakPtr 时， 复制一份 WeakPtrFlag 来表示这个
// WeakPtr 所指向指针的生命周期。
namespace FOREVER {

struct DebugThreadChecker {
  FOREVER_DECLARE_THREAD_CHECKER(checker);
};

struct DebugTaskRunnerChecker {
  FOREVER_DECLARE_TASK_RUNNER_CHECKER(checker);
};

// Forward declaration, so |WeakPtr<T>| can friend it.
template <typename T>
class WeakPtrFactory;

// Class for "weak pointers" that can be invalidated. Valid weak pointers
// can only originate from a |WeakPtrFactory| (see below), though weak
// pointers are copyable and movable.
//
// Weak pointers are not in general thread-safe. They may only be *used* on
// a single thread, namely the same thread as the "originating"
// |WeakPtrFactory| (which can invalidate the weak pointers that it
// generates).
//
// However, weak pointers may be passed to other threads, reset on other
// threads, or destroyed on other threads. They may also be reassigned on
// other threads (in which case they should then only be used on the thread
// corresponding to the new "originating" |WeakPtrFactory|).
template <typename T>
class WeakPtr {
 public:
  WeakPtr() : ptr_(nullptr) {}

  // Copy constructor.
  // NOLINTNEXTLINE(google-explicit-constructor)
  WeakPtr(const WeakPtr<T>& r) = default;

  template <typename U>
  WeakPtr(const WeakPtr<U>& r)  // NOLINT(google-explicit-constructor)
      : ptr_(static_cast<T*>(r.ptr_)), flag_(r.flag_), checker_(r.checker_) {}

  // Move constructor.
  WeakPtr(WeakPtr<T>&& r) = default;

  template <typename U>
  WeakPtr(WeakPtr<U>&& r)  // NOLINT(google-explicit-constructor)
      : ptr_(static_cast<T*>(r.ptr_)),
        flag_(std::move(r.flag_)),
        checker_(r.checker_) {}

  virtual ~WeakPtr() = default;

  // The following methods are thread-friendly, in the sense that they may be
  // called subject to additional synchronization.

  // Copy assignment.
  WeakPtr<T>& operator=(const WeakPtr<T>& r) = default;

  // Move assignment.
  WeakPtr<T>& operator=(WeakPtr<T>&& r) = default;

  void reset() { flag_ = nullptr; }

  // The following methods should only be called on the same thread as the
  // "originating" |WeakPtrFactory|.

  explicit operator bool() const {
    CheckThreadSafety();
    return flag_ && flag_->is_valid();
  }

  T* get() const {
    CheckThreadSafety();
    return *this ? ptr_ : nullptr;
  }

  T& operator*() const {
    CheckThreadSafety();
    assert(*this);
    return *get();
  }

  T* operator->() const {
    CheckThreadSafety();
    assert(*this);
    return get();
  }

 protected:
  explicit WeakPtr(T* ptr,
                   FOREVER::RefPtr<FOREVER::internal::WeakPtrFlag>&& flag)
      : ptr_(ptr), flag_(std::move(flag)) {}

  virtual void CheckThreadSafety() const {
    FOREVER_DCHECK_CREATION_THREAD_IS_CURRENT(checker_.checker);
  }

 private:
  template <typename U>
  friend class WeakPtr;

  friend class WeakPtrFactory<T>;

  explicit WeakPtr(T* ptr,
                   FOREVER::RefPtr<FOREVER::internal::WeakPtrFlag>&& flag,
                   DebugThreadChecker checker)
      : ptr_(ptr), flag_(std::move(flag)), checker_(checker) {}
  T* ptr_;
  FOREVER::RefPtr<FOREVER::internal::WeakPtrFlag> flag_;
  DebugThreadChecker checker_;

  // Copy/move construction/assignment supported.
};

// Forward declaration, so |TaskRunnerAffineWeakPtr<T>| can friend it.
template <typename T>
class TaskRunnerAffineWeakPtrFactory;

// A weak pointer that can be used in different threads as long as
// the threads are belong to the same |TaskRunner|.
//
// It is still not in general thread safe as |WeakPtr|.
template <typename T>
class TaskRunnerAffineWeakPtr : public WeakPtr<T> {
 public:
  TaskRunnerAffineWeakPtr() : WeakPtr<T>() {}

  TaskRunnerAffineWeakPtr(const TaskRunnerAffineWeakPtr<T>& r) = default;

  template <typename U>
  // NOLINTNEXTLINE(google-explicit-constructor)
  TaskRunnerAffineWeakPtr(const TaskRunnerAffineWeakPtr<U>& r)
      : WeakPtr<T>(r), checker_(r.checker_) {}

  TaskRunnerAffineWeakPtr(TaskRunnerAffineWeakPtr<T>&& r) = default;

  template <typename U>
  // NOLINTNEXTLINE(google-explicit-constructor)
  TaskRunnerAffineWeakPtr(TaskRunnerAffineWeakPtr<U>&& r)
      : WeakPtr<T>(r), checker_(r.checker_) {}

  ~TaskRunnerAffineWeakPtr() = default;

  TaskRunnerAffineWeakPtr<T>& operator=(const TaskRunnerAffineWeakPtr<T>& r) =
      default;

  TaskRunnerAffineWeakPtr<T>& operator=(TaskRunnerAffineWeakPtr<T>&& r) =
      default;

 protected:
  void CheckThreadSafety() const override {
    FOREVER_DCHECK_TASK_RUNNER_IS_CURRENT(checker_.checker);
  }

 private:
  template <typename U>
  friend class TaskRunnerAffineWeakPtr;
  friend class TaskRunnerAffineWeakPtrFactory<T>;

  explicit TaskRunnerAffineWeakPtr(
      T* ptr, FOREVER::RefPtr<FOREVER::internal::WeakPtrFlag>&& flag,
      DebugTaskRunnerChecker checker)
      : WeakPtr<T>(ptr, std::move(flag)), checker_(checker) {}

  DebugTaskRunnerChecker checker_;
};

// Class that produces (valid) |WeakPtr<T>|s. Typically, this is used as a
// member variable of |T| (preferably the last one -- see below), and |T|'s
// methods control how weak pointers to it are vended. This class is not
// thread-safe, and should only be created, destroyed and used on a single
// thread.
//
// Example:
//
//  class Controller {
//   public:
//    Controller() : ..., weak_factory_(this) {}
//    ...
//
//    void SpawnWorker() { Worker::StartNew(weak_factory_.GetWeakPtr()); }
//    void WorkComplete(const Result& result) { ... }
//
//   private:
//    ...
//
//    // Member variables should appear before the |WeakPtrFactory|, to ensure
//    // that any |WeakPtr|s to |Controller| are invalidated before its member
//    // variables' destructors are executed.
//    WeakPtrFactory<Controller> weak_factory_;
//  };
//
//  class Worker {
//   public:
//    static void StartNew(const WeakPtr<Controller>& controller) {
//      Worker* worker = new Worker(controller);
//      // Kick off asynchronous processing....
//    }
//
//   private:
//    Worker(const WeakPtr<Controller>& controller) : controller_(controller) {}
//
//    void DidCompleteAsynchronousProcessing(const Result& result) {
//      if (controller_)
//        controller_->WorkComplete(result);
//    }
//
//    WeakPtr<Controller> controller_;
//  };
template <typename T>
class WeakPtrFactory {
 public:
  explicit WeakPtrFactory(T* ptr)
      : ptr_(ptr),
        flag_(FOREVER::MakeRefCounted<FOREVER::internal::WeakPtrFlag>()) {
    assert(ptr_);
  }

  ~WeakPtrFactory() {
    CheckThreadSafety();
    flag_->Invalidate();
  }

  // Gets a new weak pointer, which will be valid until this object is
  // destroyed.
  // 最关键的函数是 GetWeakPtr，可以从 WeakPtrFactory 获取任意多个
  // WeakPtr，但都不会增加实例的引用计数
  WeakPtr<T> GetWeakPtr() const {
    return WeakPtr<T>(ptr_, flag_.Clone(), checker_);
  }

 private:
  // Note: See weak_ptr_internal.h for an explanation of why we store the
  // pointer here, instead of in the "flag".
  T* const ptr_;
  FOREVER::RefPtr<FOREVER::internal::WeakPtrFlag> flag_;

  void CheckThreadSafety() const {
    FOREVER_DCHECK_CREATION_THREAD_IS_CURRENT(checker_.checker);
  }

  DebugThreadChecker checker_;

  FOREVER_DISALLOW_COPY_AND_ASSIGN(WeakPtrFactory);
};

// A type of |WeakPtrFactory| that produces |TaskRunnerAffineWeakPtr| instead of
// |WeakPtr|.
template <typename T>
class TaskRunnerAffineWeakPtrFactory {
 public:
  explicit TaskRunnerAffineWeakPtrFactory(T* ptr)
      : ptr_(ptr),
        flag_(FOREVER::MakeRefCounted<FOREVER::internal::WeakPtrFlag>()) {
    assert(ptr_);
  }

  ~TaskRunnerAffineWeakPtrFactory() {
    CheckThreadSafety();
    flag_->Invalidate();
  }

  // Gets a new weak pointer, which will be valid until this object is
  // destroyed.
  TaskRunnerAffineWeakPtr<T> GetWeakPtr() const {
    return TaskRunnerAffineWeakPtr<T>(ptr_, flag_.Clone(), checker_);
  }

 private:
  // Note: See weak_ptr_internal.h for an explanation of why we store the
  // pointer here, instead of in the "flag".
  T* const ptr_;
  FOREVER::RefPtr<FOREVER::internal::WeakPtrFlag> flag_;

  void CheckThreadSafety() const {
    FOREVER_DCHECK_TASK_RUNNER_IS_CURRENT(checker_.checker);
  }

  DebugTaskRunnerChecker checker_;

  FOREVER_DISALLOW_COPY_AND_ASSIGN(TaskRunnerAffineWeakPtrFactory);
};

}  // namespace FOREVER

#endif  // FOREVER_MEMORY_WEAK_PTR_H_
