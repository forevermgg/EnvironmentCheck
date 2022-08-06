//
// Created by centforever on 2022/7/10.
//

#include "MainViewModel.h"

#include <sstream>
#include <thread>

#include "ArchTaskExecutor.h"
#include "generated/main_viewmodel_generated.h"
#include "log/log_utils.h"
#include "log/logging.h"
#include "message_loop.h"
#include "task_runners.h"

using namespace com::fbs::app;
using namespace flatbuffers;
MainViewModel::MainViewModel() {
  LOGE("MainViewModel")
  arch_task_executor_ = std::make_unique<ArchTaskExecutor>();
  arch_task_executor_->executeOnMainThread([]() {
    FOREVER_LOG(ERROR) << "executeOnMainThread: " << std::this_thread::get_id();
  });
  arch_task_executor_->executeOnDiskIO([]() {
    FOREVER_LOG(ERROR) << "executeOnDiskIO: " << std::this_thread::get_id();
  });
}

MainViewModel::~MainViewModel() {
  LOGE("~MainViewModel")
  arch_task_executor_.reset();
}

void MainViewModel::bind() {
  ViewModel::bind();
  LOGE("MainViewModel::bind()")
  setProp(main::viewmodel::Property_SHOW_TOAST, "1");
  setProp(main::viewmodel::Property_UI_DATA, "test ui data");
  showLoading("FOREVER");
  FOREVER_LOG(ERROR) << "main_thread Ran on thread: "
                     << std::this_thread::get_id();
  hiddenLoading();
}

void MainViewModel::unBind() {
  ViewModel::unBind();
  LOGE("MainViewModel::unBind()")
}

void MainViewModel::handle(const int key, const std::string &value) {
  ViewModel::handle(key, value);
}
