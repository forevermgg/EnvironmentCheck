// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef FOREVER_STRING_CONVERSION_H_
#define FOREVER_STRING_CONVERSION_H_

#include <string>

namespace FOREVER {

// Returns a UTF-8 encoded equivalent of a UTF-16 encoded input string.
std::string Utf16ToUtf8(const std::u16string_view string);

// Returns a UTF-16 encoded equivalent of a UTF-8 encoded input string.
std::u16string Utf8ToUtf16(const std::string_view string);

}  // namespace FOREVER

#endif  // FOREVER_STRING_CONVERSION_H_
