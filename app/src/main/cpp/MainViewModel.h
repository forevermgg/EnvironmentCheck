//
// Created by centforever on 2022/7/10.
//

#ifndef ENVIRONMENTCHECK_MAINVIEWMODEL_H
#define ENVIRONMENTCHECK_MAINVIEWMODEL_H

#include "ViewModel.h"
#include "task_runners.h"
#include "thread_host.h"
#include "ArchTaskExecutor.h"

class MainViewModel : ViewModel {
 public:
  MainViewModel();
  ~MainViewModel();
  virtual void unBind() override;
  virtual void bind() override;
  virtual void handle(const int key, const std::string &value) override;

 private:
  std::unique_ptr<ArchTaskExecutor> arch_task_executor_;
};

#endif  // ENVIRONMENTCHECK_MAINVIEWMODEL_H
