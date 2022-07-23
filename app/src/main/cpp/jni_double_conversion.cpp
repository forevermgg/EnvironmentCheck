#include <jni.h>
#include <ieee.h>
#include "double-to-string.h"
#include "string-to-double.h"
#include "jni_string.hpp"

using namespace double_conversion;
extern "C" JNIEXPORT jstring JNICALL
Java_com_mgg_environmentcheck_DoubleConversion_nativeDoubleToString(JNIEnv *env, jclass clazz, jdouble value) {
    const int kBufferSize = 168;
    char buffer[kBufferSize];
    StringBuilder builder(buffer, kBufferSize);
    int flags = DoubleToStringConverter::NO_FLAGS |
                DoubleToStringConverter::EMIT_POSITIVE_EXPONENT_SIGN |
                DoubleToStringConverter::EMIT_TRAILING_DECIMAL_POINT |
                DoubleToStringConverter::EMIT_TRAILING_ZERO_AFTER_POINT |
                DoubleToStringConverter::UNIQUE_ZERO |
                DoubleToStringConverter::NO_TRAILING_ZERO;
    DoubleToStringConverter converter(flags, "Infinity", "NaN", 'e', 0, 0, 0, 0);
    converter.ToFixed(0.0, 0,&builder);
    return FOREVER::STRING_CONVERT::StringToJavaString(env, builder.Finalize());
}


extern "C" JNIEXPORT jdouble JNICALL
Java_com_mgg_environmentcheck_DoubleConversion_nativeStringToDouble(JNIEnv *env, jclass clazz, jstring value) {
    int flags = StringToDoubleConverter::NO_FLAGS |
                StringToDoubleConverter::ALLOW_HEX |
                StringToDoubleConverter::ALLOW_OCTALS |
                StringToDoubleConverter::ALLOW_TRAILING_JUNK |
                StringToDoubleConverter::ALLOW_LEADING_SPACES |
                StringToDoubleConverter::ALLOW_TRAILING_SPACES |
                StringToDoubleConverter::ALLOW_SPACES_AFTER_SIGN |
                StringToDoubleConverter::ALLOW_CASE_INSENSITIVITY |
                StringToDoubleConverter::ALLOW_HEX_FLOATS;
    auto msg = FOREVER::STRING_CONVERT::JavaStringToString(env,value).c_str();
    int length = static_cast<int>(strlen(msg));
    StringToDoubleConverter converter(flags, 0, Double::NaN(),
                                      NULL, NULL, StringToDoubleConverter::kNoSeparator);
    int processed_characters_count16;
    double result = converter.StringToDouble(msg, length, &processed_characters_count16);
    bool processed_all = (length == processed_characters_count16);
    return jdouble(result);
}