// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_CONCURRENT_MESSAGE_LOOP_H_
#define FOREVER_CONCURRENT_MESSAGE_LOOP_H_

#include <condition_variable>
#include <map>
#include <queue>
#include <thread>

#include "closure.h"
#include "macros.h"
#include "task_runner.h"

namespace FOREVER {

class ConcurrentTaskRunner;

class ConcurrentMessageLoop
    : public std::enable_shared_from_this<ConcurrentMessageLoop> {
 public:
  static std::shared_ptr<ConcurrentMessageLoop> Create(
      size_t worker_count = std::thread::hardware_concurrency());

  ~ConcurrentMessageLoop();

  size_t GetWorkerCount() const;

  std::shared_ptr<ConcurrentTaskRunner> GetTaskRunner();

  void Terminate();

  void PostTaskToAllWorkers(FOREVER::closure task);

 private:
  friend ConcurrentTaskRunner;

  size_t worker_count_ = 0;
  std::vector<std::thread> workers_;
  std::mutex tasks_mutex_;
  std::condition_variable tasks_condition_;
  std::queue<FOREVER::closure> tasks_;
  std::vector<std::thread::id> worker_thread_ids_;
  std::map<std::thread::id, std::vector<FOREVER::closure>> thread_tasks_;
  bool shutdown_ = false;

  explicit ConcurrentMessageLoop(size_t worker_count);

  void WorkerMain();

  void PostTask(const FOREVER::closure& task);

  bool HasThreadTasksLocked() const;

  std::vector<FOREVER::closure> GetThreadTasksLocked();

  FOREVER_DISALLOW_COPY_AND_ASSIGN(ConcurrentMessageLoop);
};

class ConcurrentTaskRunner : public BasicTaskRunner {
 public:
  explicit ConcurrentTaskRunner(std::weak_ptr<ConcurrentMessageLoop> weak_loop);

  virtual ~ConcurrentTaskRunner();

  void PostTask(const FOREVER::closure& task) override;

 private:
  friend ConcurrentMessageLoop;

  std::weak_ptr<ConcurrentMessageLoop> weak_loop_;

  FOREVER_DISALLOW_COPY_AND_ASSIGN(ConcurrentTaskRunner);
};

}  // namespace FOREVER

#endif  // FOREVER_CONCURRENT_MESSAGE_LOOP_H_
