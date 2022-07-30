// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_WAKEABLE_H_
#define FOREVER_WAKEABLE_H_

namespace FOREVER {

/// Interface over the ability to \p WakeUp a \p fml::MessageLoopImpl.
/// \see fml::MessageLoopTaskQueues
class Wakeable {
 public:
  virtual ~Wakeable() {}

  virtual void WakeUp(FOREVER::TimePoint time_point) = 0;
};

}  // namespace FOREVER

#endif  // FOREVER_WAKEABLE_H_
