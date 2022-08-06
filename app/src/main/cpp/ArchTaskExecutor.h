//
// Created by centforever on 2022/8/5.
//

#ifndef ENVIRONMENTCHECK_ARCHTASKEXECUTOR_H
#define ENVIRONMENTCHECK_ARCHTASKEXECUTOR_H

#include "DefaultTaskExecutor.h"
#include "TaskExecutor.h"
#include "task_runners.h"
#include "thread_host.h"

class ArchTaskExecutor final
    : TaskExecutor,
      public std::enable_shared_from_this<ArchTaskExecutor> {
 public:
  static ArchTaskExecutor* Current();
  std::weak_ptr<ArchTaskExecutor> GetWeakPtr();
  ArchTaskExecutor();
  ~ArchTaskExecutor();
  virtual void executeOnDiskIO(const FOREVER::closure& task) override;
  virtual void postToMainThread(const FOREVER::closure& task) override;
  virtual void executeOnMainThread(const FOREVER::closure& task) override;
  virtual bool isMainThread() override;

 private:
  std::shared_ptr<FOREVER::ThreadHost> thread_host_ = nullptr;
  std::unique_ptr<DefaultTaskExecutor> default_task_executor_;
  FOREVER_DISALLOW_COPY_AND_ASSIGN(ArchTaskExecutor);
};

#endif  // ENVIRONMENTCHECK_ARCHTASKEXECUTOR_H
