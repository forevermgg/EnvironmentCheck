// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#define FOREVER_USED_ON_EMBEDDER

#include "delayed_task.h"

namespace FOREVER {

DelayedTask::DelayedTask(size_t order,
                         const FOREVER::closure& task,
                         FOREVER::TimePoint target_time,
                         FOREVER::TaskSourceGrade task_source_grade)
    : order_(order),
      task_(task),
      target_time_(target_time),
      task_source_grade_(task_source_grade) {}

DelayedTask::~DelayedTask() = default;

DelayedTask::DelayedTask(const DelayedTask& other) = default;

const FOREVER::closure& DelayedTask::GetTask() const {
  return task_;
}

FOREVER::TimePoint DelayedTask::GetTargetTime() const {
  return target_time_;
}

FOREVER::TaskSourceGrade DelayedTask::GetTaskSourceGrade() const {
  return task_source_grade_;
}

bool DelayedTask::operator>(const DelayedTask& other) const {
  if (target_time_ == other.target_time_) {
    return order_ > other.order_;
  }
  return target_time_ > other.target_time_;
}

}  // namespace FOREVER
