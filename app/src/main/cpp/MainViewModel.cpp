//
// Created by centforever on 2022/7/10.
//

#include "MainViewModel.h"

#include "generated/main_viewmodel_generated.h"
#include "log_utils.h"
using namespace com::fbs::app;
using namespace flatbuffers;

MainViewModel::MainViewModel(){
  LOGE("MainViewModel")
}

MainViewModel::~MainViewModel() {
  LOGE("~MainViewModel")
}

void MainViewModel::bind() {
  ViewModel::bind();
  LOGE("MainViewModel::bind()")
  setProp(main::viewmodel::Property_SHOW_TOAST, "1");
  showToast("FOREVER");
  setProp(main::viewmodel::Property_UI_DATA, "test ui data");
  showLoading("FOREVER");
}

void MainViewModel::unBind() {
  ViewModel::unBind();
  LOGE("MainViewModel::unBind()")
  hiddenLoading();
}

void MainViewModel::handle(const int key, const std::string &value) {
  ViewModel::handle(key, value);
}
