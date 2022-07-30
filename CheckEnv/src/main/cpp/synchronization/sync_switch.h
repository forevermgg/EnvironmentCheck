// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_SYNCHRONIZATION_SYNC_SWITCH_H_
#define FOREVER_SYNCHRONIZATION_SYNC_SWITCH_H_

#include <forward_list>
#include <functional>
#include <memory>

#include "../macros.h"
#include "../synchronization/shared_mutex.h"

namespace FOREVER {

/// A threadsafe structure that allows you to switch between 2 different
/// execution paths.
///
/// Execution and setting the switch is exclusive, i.e. only one will happen
/// at a time.
class SyncSwitch {
 public:
  /// Represents the 2 code paths available when calling |SyncSwitch::Execute|.
  struct Handlers {
    /// Sets the handler that will be executed if the |SyncSwitch| is true.
    Handlers& SetIfTrue(const std::function<void()>& handler);

    /// Sets the handler that will be executed if the |SyncSwitch| is false.
    Handlers& SetIfFalse(const std::function<void()>& handler);

    std::function<void()> true_handler = [] {};
    std::function<void()> false_handler = [] {};
  };

  /// Create a |SyncSwitch| with the specified value.
  ///
  /// @param[in]  value  Default value for the |SyncSwitch|.
  explicit SyncSwitch(bool value = false);

  /// Diverge execution between true and false values of the SyncSwitch.
  ///
  /// This can be called on any thread.  Note that attempting to call
  /// |SetSwitch| inside of the handlers will result in a self deadlock.
  ///
  /// @param[in]  handlers  Called for the correct value of the |SyncSwitch|.
  void Execute(const Handlers& handlers) const;

  /// Set the value of the SyncSwitch.
  ///
  /// This can be called on any thread.
  ///
  /// @param[in]  value  New value for the |SyncSwitch|.
  void SetSwitch(bool value);

 private:
  mutable std::unique_ptr<FOREVER::SharedMutex> mutex_;
  bool value_;

  FOREVER_DISALLOW_COPY_AND_ASSIGN(SyncSwitch);
};

}  // namespace FOREVER

#endif  // FOREVER_SYNCHRONIZATION_SYNC_SWITCH_H_
