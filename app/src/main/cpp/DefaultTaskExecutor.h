//
// Created by centforever on 2022/8/5.
//
#include <memory>
#include <mutex>
#include <thread>

#include "TaskExecutor.h"
#include "task_runners.h"
#include "thread_host.h"
#ifndef ENVIRONMENTCHECK_DEFAULTTASKEXECUTOR_H
#define ENVIRONMENTCHECK_DEFAULTTASKEXECUTOR_H

class DefaultTaskExecutor final : public TaskExecutor {
 public:
  static std::unique_ptr<DefaultTaskExecutor> Create(
      FOREVER::TaskRunners taskRunners);
  DefaultTaskExecutor(FOREVER::TaskRunners task_runners);
  ~DefaultTaskExecutor();
  virtual void executeOnDiskIO(const FOREVER::closure& task) override;
  virtual void postToMainThread(const FOREVER::closure& task) override;
  virtual void executeOnMainThread(const FOREVER::closure& task) override;
  virtual bool isMainThread() override;

 private:
  const FOREVER::TaskRunners task_runners_;
  std::thread::id main_thread_ = std::this_thread::get_id();
  FOREVER_DISALLOW_COPY_AND_ASSIGN(DefaultTaskExecutor);
};

#endif  // ENVIRONMENTCHECK_DEFAULTTASKEXECUTOR_H
