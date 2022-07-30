// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#include "unique_fd.h"

#include "eintr_wrapper.h"

namespace FOREVER {
namespace internal {
namespace os_unix {

void UniqueFDTraits::Free(int fd) {
  close(fd);
}

void UniqueDirTraits::Free(DIR* dir) {
  closedir(dir);
}

}  // namespace os_unix
}  // namespace internal
}  // namespace FOREVER
