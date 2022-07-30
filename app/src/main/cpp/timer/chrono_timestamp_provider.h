// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_TIME_CHRONO_TIMESTAMP_PROVIDER_H_
#define FOREVER_TIME_CHRONO_TIMESTAMP_PROVIDER_H_

#include "time_point.h"
#include "timestamp_provider.h"

namespace FOREVER {

/// TimestampProvider implementation that is backed by std::chrono::steady_clock
/// meant to be used only in tests for `fml`. Other components needing the
/// current time ticks since epoch should instantiate their own time stamp
/// provider backed by Dart clock.
class ChronoTimestampProvider : TimestampProvider {
 public:
  static ChronoTimestampProvider& Instance() {
    static ChronoTimestampProvider instance;
    return instance;
  }

  ~ChronoTimestampProvider() override;

  FOREVER::TimePoint Now() override;

 private:
  ChronoTimestampProvider();

  ChronoTimestampProvider(const ChronoTimestampProvider&) = delete;
  ChronoTimestampProvider& operator=(const ChronoTimestampProvider&) = delete;
};

FOREVER::TimePoint ChronoTicksSinceEpoch();

}  // namespace FOREVER

#endif  // FOREVER_TIME_CHRONO_TIMESTAMP_PROVIDER_H_
