// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_TASK_SOURCE_GRADE_H_
#define FOREVER_TASK_SOURCE_GRADE_H_

namespace FOREVER {

/**
 * Categories of work dispatched to `MessageLoopTaskQueues` dispatcher. By
 * specifying the `TaskSourceGrade`, you indicate the task's importance to the
 * dispatcher.
 */
enum class TaskSourceGrade {
  /// This `TaskSourceGrade` indicates that a task is critical to user
  /// interaction.
  kUserInteraction,
  /// This `TaskSourceGrade` indicates that a task corresponds to servicing a
  /// dart micro task. These aren't critical to user interaction.
  kDartMicroTasks,
  /// The absence of a specialized `TaskSourceGrade`.
  kUnspecified,
};

}  // namespace FOREVER

#endif  // FOREVER_TASK_SOURCE_GRADE_H_
