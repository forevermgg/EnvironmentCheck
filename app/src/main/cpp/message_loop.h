// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_MESSAGE_LOOP_H_
#define FOREVER_MESSAGE_LOOP_H_

#include "macros.h"
#include "task_runner.h"

namespace FOREVER {

class TaskRunner;
class MessageLoopImpl;

/// An event loop associated with a thread.
///
/// This class is the generic front-end to the MessageLoop, differences in
/// implementation based on the running platform are in the subclasses of
/// flutter::MessageLoopImpl (ex flutter::MessageLoopAndroid).
///
/// For scheduling events on the message loop see flutter::TaskRunner.
///
/// \see FOREVER::TaskRunner
/// \see FOREVER::MessageLoopImpl
/// \see FOREVER::MessageLoopTaskQueues
/// \see FOREVER::Wakeable
class MessageLoop {
 public:
  static MessageLoop& GetCurrent();

  void Run();

  void Terminate();

  void AddTaskObserver(intptr_t key, const FOREVER::closure& callback);

  void RemoveTaskObserver(intptr_t key);

  FOREVER::RefPtr<FOREVER::TaskRunner> GetTaskRunner() const;

  // Exposed for the embedder shell which allows clients to poll for events
  // instead of dedicating a thread to the message loop.
  void RunExpiredTasksNow();

  static void EnsureInitializedForCurrentThread();

  /// Returns true if \p EnsureInitializedForCurrentThread has been called on
  /// this thread already.
  static bool IsInitializedForCurrentThread();

  ~MessageLoop();

  /// Gets the unique identifier for the TaskQueue associated with the current
  /// thread.
  /// \see FOREVER::MessageLoopTaskQueues
  static TaskQueueId GetCurrentTaskQueueId();

 private:
  friend class TaskRunner;
  friend class MessageLoopImpl;

  FOREVER::RefPtr<MessageLoopImpl> loop_;
  FOREVER::RefPtr<FOREVER::TaskRunner> task_runner_;

  MessageLoop();

  FOREVER::RefPtr<MessageLoopImpl> GetLoopImpl() const;

  FOREVER_DISALLOW_COPY_AND_ASSIGN(MessageLoop);
};

}  // namespace FOREVER

#endif  // FOREVER_MESSAGE_LOOP_H_
