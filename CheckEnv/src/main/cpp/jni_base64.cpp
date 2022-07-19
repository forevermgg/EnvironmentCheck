#include <jni.h>
#include <string>
#include "base64.h"
#include "common.h"
#include "jni_string.hpp"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_mgg_checkenv_utils_Base64_encode(JNIEnv *env, jobject thiz, jstring orig) {
    std::string orig_str = FOREVER::STRING_CONVERT::JavaStringToString(env, orig);
    std::string encoded;
    bool success = FOREVER::INTERNAL::Base64Encode(orig_str, &encoded);
    if (success) {
        return FOREVER::STRING_CONVERT::StringToJavaString(env, encoded);
    } else {
        return nullptr;
    }
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_mgg_checkenv_utils_Base64_decode(JNIEnv *env, jobject thiz, jstring encode) {
    std::string encode_str = FOREVER::STRING_CONVERT::JavaStringToString(env, encode);
    std::string decoded;
    bool success = FOREVER::INTERNAL::Base64Decode(encode_str, &decoded);
    if (success) {
        return FOREVER::STRING_CONVERT::StringToJavaString(env, decoded);
    } else {
        return nullptr;
    }
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_mgg_checkenv_utils_Base64_encodeWithPadding(JNIEnv *env, jobject thiz, jstring orig) {
    std::string orig_str = FOREVER::STRING_CONVERT::JavaStringToString(env, orig);
    std::string encoded;
    bool success = FOREVER::INTERNAL::Base64EncodeWithPadding(orig_str, &encoded);
    if (success) {
        return FOREVER::STRING_CONVERT::StringToJavaString(env, encoded);
    } else {
        return nullptr;
    }
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_mgg_checkenv_utils_Base64_encodeUrlSafe(JNIEnv *env, jobject thiz, jstring orig) {
    std::string orig_str = FOREVER::STRING_CONVERT::JavaStringToString(env, orig);
    std::string encoded;
    bool success = FOREVER::INTERNAL::Base64EncodeUrlSafe(orig_str, &encoded);
    if (success) {
        return FOREVER::STRING_CONVERT::StringToJavaString(env, encoded);
    } else {
        return nullptr;
    }
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_mgg_checkenv_utils_Base64_encodeUrlSafeWithPadding(JNIEnv *env, jobject thiz, jstring orig) {
    std::string orig_str = FOREVER::STRING_CONVERT::JavaStringToString(env, orig);
    std::string encoded;
    bool success = FOREVER::INTERNAL::Base64EncodeUrlSafeWithPadding(orig_str, &encoded);
    if (success) {
        return FOREVER::STRING_CONVERT::StringToJavaString(env, encoded);
    } else {
        return nullptr;
    }
}