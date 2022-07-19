// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_HEX_CODEC_H_
#define FOREVER_HEX_CODEC_H_

#include <string_view>

namespace FOREVER {

std::string HexEncode(std::string_view input);

}  // namespace FOREVER

#endif  // FOREVER_HEX_CODEC_H_
