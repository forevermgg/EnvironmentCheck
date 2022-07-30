// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_DELAYED_TASK_H_
#define FOREVER_DELAYED_TASK_H_

#include <queue>

#include "closure.h"
#include "task_source_grade.h"
#include "timer/time_point.h"

namespace FOREVER {

class DelayedTask {
 public:
  DelayedTask(size_t order, const FOREVER::closure& task,
              FOREVER::TimePoint target_time,
              FOREVER::TaskSourceGrade task_source_grade);

  DelayedTask(const DelayedTask& other);

  ~DelayedTask();

  const FOREVER::closure& GetTask() const;

  FOREVER::TimePoint GetTargetTime() const;

  FOREVER::TaskSourceGrade GetTaskSourceGrade() const;

  bool operator>(const DelayedTask& other) const;

 private:
  size_t order_;
  FOREVER::closure task_;
  FOREVER::TimePoint target_time_;
  FOREVER::TaskSourceGrade task_source_grade_;
};

using DelayedTaskQueue =
    std::priority_queue<DelayedTask, std::deque<DelayedTask>,
                        std::greater<DelayedTask>>;

}  // namespace FOREVER

#endif  // FOREVER_DELAYED_TASK_H_
