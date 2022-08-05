//
// Created by centforever on 2022/8/5.
//

#ifndef ENVIRONMENTCHECK_ARCHTASKEXECUTOR_H
#define ENVIRONMENTCHECK_ARCHTASKEXECUTOR_H

#include "DefaultTaskExecutor.h"
#include "TaskExecutor.h"
#include "task_runners.h"
#include "thread_host.h"

class ArchTaskExecutor final : public TaskExecutor {
 public:
  ArchTaskExecutor();
  ~ArchTaskExecutor();
  virtual void executeOnDiskIO(const FOREVER::closure& task) override;
  virtual void postToMainThread(const FOREVER::closure& task) override;
  virtual void executeOnMainThread(const FOREVER::closure& task) override;
  virtual bool isMainThread() override;

 private:
  std::shared_ptr<FOREVER::ThreadHost> thread_host_ = nullptr;
  std::unique_ptr<DefaultTaskExecutor> default_task_executor_;
};

#endif  // ENVIRONMENTCHECK_ARCHTASKEXECUTOR_H
