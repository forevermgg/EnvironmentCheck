//
// Created by centforever on 2022/8/5.
//

#include "ArchTaskExecutor.h"

#include <pthread.h>
#include <sys/resource.h>
#include <sys/time.h>

#include <memory>
#include <optional>
#include <sstream>
#include <string>
#include <utility>

ArchTaskExecutor::ArchTaskExecutor() {
  thread_host_ = std::make_unique<FOREVER::ThreadHost>(
      "Forever_ArchTaskExecutor",
      FOREVER::ThreadHost::Type::IO | FOREVER::ThreadHost::Type::Platform |
          FOREVER::ThreadHost::Type::UI | FOREVER::ThreadHost::Type::RASTER);
  FOREVER::MessageLoop::EnsureInitializedForCurrentThread();
  FOREVER::RefPtr<FOREVER::TaskRunner> platform_task_runner;
  FOREVER::RefPtr<FOREVER::TaskRunner> raster_task_runner;
  FOREVER::RefPtr<FOREVER::TaskRunner> ui_task_runner;
  FOREVER::RefPtr<FOREVER::TaskRunner> io_task_runner;
  platform_task_runner = FOREVER::MessageLoop::GetCurrent().GetTaskRunner();
  raster_task_runner = thread_host_->raster_thread->GetTaskRunner();
  ui_task_runner = thread_host_->ui_thread->GetTaskRunner();
  io_task_runner = thread_host_->io_thread->GetTaskRunner();
  FOREVER::TaskRunners task_runners("Forever_ArchTaskExecutor",
                                    platform_task_runner, raster_task_runner,
                                    ui_task_runner, io_task_runner);
  default_task_executor_ = DefaultTaskExecutor::Create(task_runners);
  FOREVER_LOG(ERROR) << "ArchTaskExecutor::ArchTaskExecutor()";
}

ArchTaskExecutor::~ArchTaskExecutor() {
  FOREVER_LOG(ERROR) << "ArchTaskExecutor::~ArchTaskExecutor()";
  default_task_executor_.reset();
  thread_host_.reset();
}

void ArchTaskExecutor::executeOnDiskIO(const FOREVER::closure &task) {
  default_task_executor_->executeOnDiskIO(task);
}

void ArchTaskExecutor::postToMainThread(const FOREVER::closure &task) {
  default_task_executor_->postToMainThread(task);
}

void ArchTaskExecutor::executeOnMainThread(const FOREVER::closure &task) {
  default_task_executor_->executeOnMainThread(task);
}

bool ArchTaskExecutor::isMainThread() {
  return default_task_executor_->isMainThread();
}
