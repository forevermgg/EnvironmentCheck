// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
#include "time_point.h"
#include <atomic>
#include <chrono>

namespace FOREVER {

namespace {
std::atomic<TimePoint::ClockSource> gSteadyClockSource;
}

template <typename Clock, typename Duration>
static int64_t NanosSinceEpoch(
    std::chrono::time_point<Clock, Duration> time_point) {
  const auto elapsed = time_point.time_since_epoch();
  return std::chrono::duration_cast<std::chrono::nanoseconds>(elapsed).count();
}

void TimePoint::SetClockSource(ClockSource source) {
  gSteadyClockSource = source;
}

TimePoint TimePoint::Now() {
  if (gSteadyClockSource) {
    return gSteadyClockSource.load()();
  }
  const int64_t nanos = NanosSinceEpoch(std::chrono::steady_clock::now());
  return TimePoint(nanos);
}

TimePoint TimePoint::CurrentWallTime() {
  const int64_t nanos = NanosSinceEpoch(std::chrono::system_clock::now());
  return TimePoint(nanos);
}
}  // namespace FOREVER
