// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_SIZE_H_
#define FOREVER_SIZE_H_

#include <cstddef>

namespace FOREVER {

template <typename T, std::size_t N>
constexpr std::size_t size(T (&array)[N]) {
  return N;
}

}  // namespace FOREVER

#endif  // FOREVER_SIZE_H_
