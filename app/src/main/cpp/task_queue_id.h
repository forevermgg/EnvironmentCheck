// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_TASK_QUEUE_ID_H_
#define FOREVER_TASK_QUEUE_ID_H_

namespace FOREVER {

/**
 * `MessageLoopTaskQueues` task dispatcher's internal task queue identifier.
 */
class TaskQueueId {
 public:
  /// This constant indicates whether a task queue has been subsumed by a task
  /// runner.
  static const size_t kUnmerged;

  /// Intializes a task queue with the given value as it's ID.
  explicit TaskQueueId(size_t value) : value_(value) {}

  operator size_t() const {  // NOLINT(google-explicit-constructor)
    return value_;
  }

 private:
  size_t value_ = kUnmerged;
};

}  // namespace FOREVER

#endif  // FOREVER_TASK_QUEUE_ID_H_
