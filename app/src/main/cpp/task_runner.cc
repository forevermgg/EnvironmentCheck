// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#define FOREVER_USED_ON_EMBEDDER

#include "task_runner.h"
#include "memory/task_runner_checker.h"

#include <utility>

#include "log/logging.h"
#include "message_loop.h"
#include "message_loop_impl.h"
#include "message_loop_task_queues.h"

namespace FOREVER {

TaskRunner::TaskRunner(FOREVER::RefPtr<MessageLoopImpl> loop)
    : loop_(std::move(loop)) {}

TaskRunner::~TaskRunner() = default;

void TaskRunner::PostTask(const FOREVER::closure& task) {
  loop_->PostTask(task, FOREVER::TimePoint::Now());
}

void TaskRunner::PostTaskForTime(const FOREVER::closure& task,
                                 FOREVER::TimePoint target_time) {
  loop_->PostTask(task, target_time);
}

void TaskRunner::PostDelayedTask(const FOREVER::closure& task,
                                 FOREVER::TimeDelta delay) {
  loop_->PostTask(task, FOREVER::TimePoint::Now() + delay);
}

TaskQueueId TaskRunner::GetTaskQueueId() {
  FOREVER_DCHECK(loop_);
  return loop_->GetTaskQueueId();
}

bool TaskRunner::RunsTasksOnCurrentThread() {
  if (!FOREVER::MessageLoop::IsInitializedForCurrentThread()) {
    return false;
  }

  const auto current_queue_id = MessageLoop::GetCurrentTaskQueueId();
  const auto loop_queue_id = loop_->GetTaskQueueId();

  return TaskRunnerChecker::RunsOnTheSameThread(current_queue_id,
                                                loop_queue_id);
}

void TaskRunner::RunNowOrPostTask(FOREVER::RefPtr<FOREVER::TaskRunner> runner,
                                  const FOREVER::closure& task) {
  FOREVER_DCHECK(runner);
  if (runner->RunsTasksOnCurrentThread()) {
    task();
  } else {
    runner->PostTask(std::move(task));
  }
}

}  // namespace FOREVER
