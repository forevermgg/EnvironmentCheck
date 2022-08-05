//
// Created by centforever on 2022/8/5.
//
#include "log/logging.h"
#include "message_loop.h"
#include "task_runners.h"
#ifndef ENVIRONMENTCHECK_TASKEXECUTOR_H
#define ENVIRONMENTCHECK_TASKEXECUTOR_H

class TaskExecutor {
 public:
  virtual void executeOnDiskIO(const FOREVER::closure& task) = 0;
  virtual void postToMainThread(const FOREVER::closure& task) = 0;
  virtual void executeOnMainThread(const FOREVER::closure& task) = 0;
  virtual bool isMainThread() = 0;
};

#endif  // ENVIRONMENTCHECK_TASKEXECUTOR_H
