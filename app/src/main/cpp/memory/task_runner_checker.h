// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_MEMORY_TASK_RUNNER_CHECKER_H_
#define FOREVER_MEMORY_TASK_RUNNER_CHECKER_H_

#include "../message_loop.h"
#include "../task_runner.h"

namespace FOREVER {

class TaskRunnerChecker final {
 public:
  TaskRunnerChecker();

  ~TaskRunnerChecker();

  bool RunsOnCreationTaskRunner() const;

  static bool RunsOnTheSameThread(TaskQueueId queue_a, TaskQueueId queue_b);

 private:
  TaskQueueId initialized_queue_id_;
  std::set<TaskQueueId> subsumed_queue_ids_;

  TaskQueueId InitTaskQueueId();
};

#if !defined(NDEBUG)
#define FOREVER_DECLARE_TASK_RUNNER_CHECKER(c) FOREVER::TaskRunnerChecker c
#define FOREVER_DCHECK_TASK_RUNNER_IS_CURRENT(c) \
  FOREVER_DCHECK((c).RunsOnCreationTaskRunner())
#else
#define FOREVER_DECLARE_TASK_RUNNER_CHECKER(c)
#define FOREVER_DCHECK_TASK_RUNNER_IS_CURRENT(c) ((void)0)
#endif

}  // namespace FOREVER

#endif  // FOREVER_MEMORY_THREAD_CHECKER_H_
