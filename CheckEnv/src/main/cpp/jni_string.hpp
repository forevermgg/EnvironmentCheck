//
// Created by centforever on 2022/4/15.
//
#include <codecvt>
#include <locale>
#include <string>

#ifndef JNI_STRING_UTIL
#define JNI_STRING_UTIL
namespace FOREVER {
namespace STRING_CONVERT {
static std::string UTF16StringToUTF8String(const char16_t *chars, size_t len) {
  std::u16string u16_string(chars, len);
  return std::wstring_convert<std::codecvt_utf8_utf16<char16_t>, char16_t>{}
      .to_bytes(u16_string);
}

static std::string JavaStringToString(JNIEnv *env, jstring str) {
  if (env == nullptr || str == nullptr) {
    return "";
  }
  const jchar *chars = env->GetStringChars(str, NULL);
  if (chars == nullptr) {
    return "";
  }
  std::string u8_string = UTF16StringToUTF8String(
      reinterpret_cast<const char16_t *>(chars), env->GetStringLength(str));
  env->ReleaseStringChars(str, chars);
  return u8_string;
}

static std::u16string UTF8StringToUTF16String(const std::string &string) {
  return std::wstring_convert<std::codecvt_utf8_utf16<char16_t>, char16_t>{}
      .from_bytes(string);
}

static jstring StringToJavaString(JNIEnv *env, const std::string &u8_string) {
  std::u16string u16_string = UTF8StringToUTF16String(u8_string);
  auto result = env->NewString(
      reinterpret_cast<const jchar *>(u16_string.data()), u16_string.length());
  return result;
}
} // namespace STRING_CONVERT
} // namespace FOREVER
#endif  // JNI_STRING_UTIL
