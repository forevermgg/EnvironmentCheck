// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_TIME_TIMESTAMP_PROVIDER_H_
#define FOREVER_TIME_TIMESTAMP_PROVIDER_H_

#include <cstdint>

#include "time_point.h"

namespace FOREVER {

/// Pluggable provider of monotonic timestamps. Invocations of `Now` must return
/// unique values. Any two consecutive invocations must be ordered.
class TimestampProvider {
 public:
  virtual ~TimestampProvider(){};

  // Returns the number of ticks elapsed by a monotonic clock since epoch.
  virtual FOREVER::TimePoint Now() = 0;
};

}  // namespace FOREVER

#endif  // FOREVER_TIME_TIMESTAMP_PROVIDER_H_
