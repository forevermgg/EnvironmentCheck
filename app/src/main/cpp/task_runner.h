// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_TASK_RUNNER_H_
#define FOREVER_TASK_RUNNER_H_

#include "closure.h"
#include "macros.h"
#include "memory/ref_counted.h"
#include "memory/ref_ptr.h"
#include "message_loop_task_queues.h"

namespace FOREVER {

class MessageLoopImpl;

/// An interface over the ability to schedule tasks on a \p TaskRunner.
class BasicTaskRunner {
 public:
  /// Schedules \p task to be executed on the TaskRunner's associated event
  /// loop.
  virtual void PostTask(const FOREVER::closure& task) = 0;
};

/// The object for scheduling tasks on a \p FOREVER::MessageLoop.
///
/// Typically there is one \p TaskRunner associated with each thread.  When one
/// wants to execute an operation on that thread they post a task to the
/// TaskRunner.
///
/// \see FOREVER::MessageLoop
class TaskRunner : public FOREVER::RefCountedThreadSafe<TaskRunner>,
                   public BasicTaskRunner {
 public:
  virtual ~TaskRunner();

  virtual void PostTask(const FOREVER::closure& task) override;

  virtual void PostTaskForTime(const FOREVER::closure& task,
                               FOREVER::TimePoint target_time);

  /// Schedules a task to be run on the MessageLoop after the time \p delay has
  /// passed.
  /// \note There is latency between when the task is schedule and actually
  /// executed so that the actual execution time is: now + delay +
  /// message_loop_latency, where message_loop_latency is undefined and could be
  /// tens of milliseconds.
  virtual void PostDelayedTask(const FOREVER::closure& task,
                               FOREVER::TimeDelta delay);

  /// Returns \p true when the current executing thread's TaskRunner matches
  /// this instance.
  virtual bool RunsTasksOnCurrentThread();

  /// Returns the unique identifier associated with the TaskRunner.
  /// \see FOREVER::MessageLoopTaskQueues
  virtual FOREVER::TaskQueueId GetTaskQueueId();

  /// Executes the \p task directly if the TaskRunner \p runner is the
  /// TaskRunner associated with the current executing thread.
  static void RunNowOrPostTask(FOREVER::RefPtr<TaskRunner> runner,
                               const FOREVER::closure& task);

 protected:
  explicit TaskRunner(FOREVER::RefPtr<MessageLoopImpl> loop);

 private:
  FOREVER::RefPtr<MessageLoopImpl> loop_;

  FOREVER_FRIEND_MAKE_REF_COUNTED(TaskRunner);
  FOREVER_FRIEND_REF_COUNTED_THREAD_SAFE(TaskRunner);
  FOREVER_DISALLOW_COPY_AND_ASSIGN(TaskRunner);
};

}  // namespace FOREVER

#endif  // FOREVER_TASK_RUNNER_H_
