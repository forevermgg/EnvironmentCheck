// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_MESSAGE_LOOP_IMPL_H_
#define FOREVER_MESSAGE_LOOP_IMPL_H_

#include <atomic>
#include <deque>
#include <map>
#include <mutex>
#include <queue>
#include <utility>

#include "closure.h"
#include "delayed_task.h"
#include "macros.h"
#include "memory/ref_counted.h"
#include "message_loop_task_queues.h"
#include "wakeable.h"

namespace FOREVER {

/// An abstract class that represents the differences in implementation of a \p
/// FOREVER::MessageLoop depending on the platform.
/// \see FOREVER::MessageLoop
/// \see FOREVER::MessageLoopAndroid
/// \see FOREVER::MessageLoopDarwin
class MessageLoopImpl : public Wakeable,
                        public FOREVER::RefCountedThreadSafe<MessageLoopImpl> {
 public:
  static FOREVER::RefPtr<MessageLoopImpl> Create();

  virtual ~MessageLoopImpl();

  virtual void Run() = 0;

  virtual void Terminate() = 0;

  void PostTask(const FOREVER::closure& task, FOREVER::TimePoint target_time);

  void AddTaskObserver(intptr_t key, const FOREVER::closure& callback);

  void RemoveTaskObserver(intptr_t key);

  void DoRun();

  void DoTerminate();

  virtual TaskQueueId GetTaskQueueId() const;

 protected:
  // Exposed for the embedder shell which allows clients to poll for events
  // instead of dedicating a thread to the message loop.
  friend class MessageLoop;

  void RunExpiredTasksNow();

  void RunSingleExpiredTaskNow();

 protected:
  MessageLoopImpl();

 private:
  FOREVER::RefPtr<MessageLoopTaskQueues> task_queue_;
  TaskQueueId queue_id_;

  std::atomic_bool terminated_;

  void FlushTasks(FlushType type);

  FOREVER_DISALLOW_COPY_AND_ASSIGN(MessageLoopImpl);
};

}  // namespace FOREVER

#endif  // FOREVER_MESSAGE_LOOP_IMPL_H_
