//
// Created by centforever on 2022/7/10.
//

#include "MainViewModel.h"

#include <sstream>
#include <thread>

#include "generated/main_viewmodel_generated.h"
#include "log_utils.h"
#include "message_loop.h"
#include "task_runners.h"

using namespace com::fbs::app;
using namespace flatbuffers;

MainViewModel::MainViewModel() {
  LOGE("MainViewModel")
  FOREVER::MessageLoop::EnsureInitializedForCurrentThread();
  std::unique_ptr<FOREVER::ThreadHost> threadhost;
  FOREVER::RefPtr<FOREVER::TaskRunner> platform_task_runner;
  FOREVER::RefPtr<FOREVER::TaskRunner> raster_task_runner;
  FOREVER::RefPtr<FOREVER::TaskRunner> ui_task_runner;
  FOREVER::RefPtr<FOREVER::TaskRunner> io_task_runner;
  thread_host_ = std::make_unique<FOREVER::ThreadHost>(
      "test",
      FOREVER::ThreadHost::Type::Platform | FOREVER::ThreadHost::Type::IO |
          FOREVER::ThreadHost::Type::UI | FOREVER::ThreadHost::Type::RASTER);
  platform_task_runner = FOREVER::MessageLoop::GetCurrent().GetTaskRunner();
  raster_task_runner = thread_host_->raster_thread->GetTaskRunner();
  ui_task_runner = thread_host_->ui_thread->GetTaskRunner();
  io_task_runner = thread_host_->io_thread->GetTaskRunner();
  FOREVER::TaskRunners task_runners("test", platform_task_runner,
                                    raster_task_runner, ui_task_runner,
                                    io_task_runner);
}

MainViewModel::~MainViewModel() {
  LOGE("~MainViewModel")
  thread_host_.reset();
}

std::thread::id main_thread_id = std::this_thread::get_id();

void is_main_thread() {
  if (main_thread_id == std::this_thread::get_id()) {
    LOGE("This is the main thread.\n");
  } else {
    LOGE("This is not the main thread.\n");
  }
}

void MainViewModel::bind() {
  ViewModel::bind();
  LOGE("MainViewModel::bind()")
  setProp(main::viewmodel::Property_SHOW_TOAST, "1");
  showToast("FOREVER");
  setProp(main::viewmodel::Property_UI_DATA, "test ui data");
  showLoading("FOREVER");
  bool done = false;
  thread_host_->io_thread->GetTaskRunner()->PostTask([&done]() {
    done = true;
    is_main_thread();
  });
  is_main_thread();
}

void MainViewModel::unBind() {
  ViewModel::unBind();
  LOGE("MainViewModel::unBind()")
  // hiddenLoading();
}

void MainViewModel::handle(const int key, const std::string &value) {
  ViewModel::handle(key, value);
}
