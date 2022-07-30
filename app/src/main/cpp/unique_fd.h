// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_UNIQUE_FD_H_
#define FOREVER_UNIQUE_FD_H_
#include <dirent.h>
#include <unistd.h>

#include "unique_object.h"

namespace FOREVER {
namespace internal {

namespace os_unix {

struct UniqueFDTraits {
  static int InvalidValue() { return -1; }
  static bool IsValid(int value) { return value >= 0; }
  static void Free(int fd);
};

struct UniqueDirTraits {
  static DIR* InvalidValue() { return nullptr; }
  static bool IsValid(DIR* value) { return value != nullptr; }
  static void Free(DIR* dir);
};

}  // namespace os_unix

}  // namespace internal

using UniqueFD = UniqueObject<int, internal::os_unix::UniqueFDTraits>;
using UniqueDir = UniqueObject<DIR*, internal::os_unix::UniqueDirTraits>;

}  // namespace FOREVER

#endif  // FOREVER_UNIQUE_FD_H_
