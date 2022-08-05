//
// Created by centforever on 2022/8/5.
//

#include "DefaultTaskExecutor.h"

#include <memory>

#include "log/logging.h"

std::unique_ptr<DefaultTaskExecutor> DefaultTaskExecutor::Create(
    FOREVER::TaskRunners taskRunners) {
  auto shell = std::make_unique<DefaultTaskExecutor>(std::move(taskRunners));
  return shell;
}

DefaultTaskExecutor::DefaultTaskExecutor(FOREVER::TaskRunners task_runners)
    : task_runners_(std::move(task_runners)) {
  FOREVER_LOG(ERROR) << "DefaultTaskExecutor";
}

DefaultTaskExecutor::~DefaultTaskExecutor() {
  FOREVER_LOG(ERROR) << "~DefaultTaskExecutor";
}

void DefaultTaskExecutor::executeOnDiskIO(const FOREVER::closure &task) {
  task_runners_.GetIOTaskRunner()->PostTask(task);
}

void DefaultTaskExecutor::postToMainThread(const FOREVER::closure &task) {
  task_runners_.GetPlatformTaskRunner()->PostTask(task);
}

void DefaultTaskExecutor::executeOnMainThread(const FOREVER::closure &task) {
  task_runners_.GetPlatformTaskRunner()->PostTask(task);
}

bool DefaultTaskExecutor::isMainThread() {
  return std::this_thread::get_id() == main_thread_;
}
