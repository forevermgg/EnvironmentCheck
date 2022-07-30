// Copyright 2013 The FOREVER Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_COMMON_TASK_RUNNERS_H_
#define FOREVER_COMMON_TASK_RUNNERS_H_

#include <string>

#include "macros.h"
#include "task_runner.h"

namespace FOREVER {

class TaskRunners {
 public:
  TaskRunners(std::string label, FOREVER::RefPtr<FOREVER::TaskRunner> platform,
              FOREVER::RefPtr<FOREVER::TaskRunner> raster,
              FOREVER::RefPtr<FOREVER::TaskRunner> ui,
              FOREVER::RefPtr<FOREVER::TaskRunner> io);

  TaskRunners(const TaskRunners& other);

  ~TaskRunners();

  const std::string& GetLabel() const;

  FOREVER::RefPtr<FOREVER::TaskRunner> GetPlatformTaskRunner() const;

  FOREVER::RefPtr<FOREVER::TaskRunner> GetUITaskRunner() const;

  FOREVER::RefPtr<FOREVER::TaskRunner> GetIOTaskRunner() const;

  FOREVER::RefPtr<FOREVER::TaskRunner> GetRasterTaskRunner() const;

  bool IsValid() const;

 private:
  const std::string label_;
  FOREVER::RefPtr<FOREVER::TaskRunner> platform_;
  FOREVER::RefPtr<FOREVER::TaskRunner> raster_;
  FOREVER::RefPtr<FOREVER::TaskRunner> ui_;
  FOREVER::RefPtr<FOREVER::TaskRunner> io_;
};

}  // namespace FOREVER

#endif  // FOREVER_COMMON_TASK_RUNNERS_H_
