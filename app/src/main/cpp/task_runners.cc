// Copyright 2013 The FOREVER Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#include "task_runners.h"

#include <utility>

namespace FOREVER {

TaskRunners::TaskRunners(std::string label,
                         FOREVER::RefPtr<FOREVER::TaskRunner> platform,
                         FOREVER::RefPtr<FOREVER::TaskRunner> raster,
                         FOREVER::RefPtr<FOREVER::TaskRunner> ui,
                         FOREVER::RefPtr<FOREVER::TaskRunner> io)
    : label_(std::move(label)),
      platform_(std::move(platform)),
      raster_(std::move(raster)),
      ui_(std::move(ui)),
      io_(std::move(io)) {}

TaskRunners::TaskRunners(const TaskRunners& other) = default;

TaskRunners::~TaskRunners() = default;

const std::string& TaskRunners::GetLabel() const {
  return label_;
}

FOREVER::RefPtr<FOREVER::TaskRunner> TaskRunners::GetPlatformTaskRunner() const {
  return platform_;
}

FOREVER::RefPtr<FOREVER::TaskRunner> TaskRunners::GetUITaskRunner() const {
  return ui_;
}

FOREVER::RefPtr<FOREVER::TaskRunner> TaskRunners::GetIOTaskRunner() const {
  return io_;
}

FOREVER::RefPtr<FOREVER::TaskRunner> TaskRunners::GetRasterTaskRunner() const {
  return raster_;
}

bool TaskRunners::IsValid() const {
  return platform_ && raster_ && ui_ && io_;
}

}  // namespace FOREVER
