// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#include "string_conversion.h"

#include <codecvt>
#include <locale>
#include <string>

namespace FOREVER {

using Utf16StringConverter =
    std::wstring_convert<std::codecvt_utf8_utf16<char16_t>, char16_t>;

std::string Utf16ToUtf8(const std::u16string_view string) {
  Utf16StringConverter converter;
  return converter.to_bytes(string.data());
}

std::u16string Utf8ToUtf16(const std::string_view string) {
  Utf16StringConverter converter;
  return converter.from_bytes(string.data());
}

}  // namespace FOREVER
