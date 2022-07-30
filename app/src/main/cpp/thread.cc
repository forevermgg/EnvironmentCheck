// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#define FML_USED_ON_EMBEDDER

#include "thread.h"

#include <memory>
#include <string>
#include <utility>
#include "message_loop.h"
#include "synchronization/waitable_event.h"
#include <pthread.h>

namespace FOREVER {
void SetThreadName(const std::string& name) {
  if (name == "") {
    return;
  }
  pthread_setname_np(pthread_self(), name.c_str());
}

void Thread::SetCurrentThreadName(const Thread::ThreadConfig& config) {
  SetThreadName(config.name);
}

Thread::Thread(const std::string& name)
    : Thread(Thread::SetCurrentThreadName, ThreadConfig(name)) {}

Thread::Thread(const ThreadConfigSetter& setter, const ThreadConfig& config)
    : joined_(false) {
  FOREVER::AutoResetWaitableEvent latch;
  FOREVER::RefPtr<FOREVER::TaskRunner> runner;

  thread_ = std::make_unique<std::thread>(
      [&latch, &runner, setter, config]() -> void {
        setter(config);
        FOREVER::MessageLoop::EnsureInitializedForCurrentThread();
        auto& loop = MessageLoop::GetCurrent();
        runner = loop.GetTaskRunner();
        latch.Signal();
        loop.Run();
      });
  latch.Wait();
  task_runner_ = runner;
}

Thread::~Thread() {
  Join();
}

FOREVER::RefPtr<FOREVER::TaskRunner> Thread::GetTaskRunner() const {
  return task_runner_;
}

void Thread::Join() {
  if (joined_) {
    return;
  }
  joined_ = true;
  task_runner_->PostTask([]() { MessageLoop::GetCurrent().Terminate(); });
  thread_->join();
}

}  // namespace FOREVER
