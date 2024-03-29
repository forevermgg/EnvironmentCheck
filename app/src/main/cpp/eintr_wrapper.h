// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_EINTR_WRAPPER_H_
#define FOREVER_EINTR_WRAPPER_H_
#include <errno.h>

#define FOREVER_HANDLE_EINTR(x)                             \
  ({                                                        \
    decltype(x) eintr_wrapper_result;                       \
    do {                                                    \
      eintr_wrapper_result = (x);                           \
    } while (eintr_wrapper_result == -1 && errno == EINTR); \
    eintr_wrapper_result;                                   \
  })

#define FOREVER_IGNORE_EINTR(x)                           \
  ({                                                      \
    decltype(x) eintr_wrapper_result;                     \
    do {                                                  \
      eintr_wrapper_result = (x);                         \
      if (eintr_wrapper_result == -1 && errno == EINTR) { \
        eintr_wrapper_result = 0;                         \
      }                                                   \
    } while (0);                                          \
    eintr_wrapper_result;                                 \
  })

#endif  // FOREVER_EINTR_WRAPPER_H_
