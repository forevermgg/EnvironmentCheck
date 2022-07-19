//
// Created by centforever on 2022/7/10.
//

#ifndef ENVIRONMENTCHECK_MAINVIEWMODEL_H
#define ENVIRONMENTCHECK_MAINVIEWMODEL_H

#include "ViewModel.h"

class MainViewModel : ViewModel {
 public:
  MainViewModel();
  ~MainViewModel();
  virtual void unBind() override;
  virtual void bind() override;
  virtual void handle(const std::string &key, const std::string &value);
  virtual void handle(const int key, const std::string &value);
};

#endif  // ENVIRONMENTCHECK_MAINVIEWMODEL_H
