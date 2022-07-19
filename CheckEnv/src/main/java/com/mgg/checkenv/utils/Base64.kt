package com.mgg.checkenv.utils

object Base64 {
    external fun encode(orig : String) :String
    external fun decode(orig : String) :String
    external fun encodeWithPadding(orig : String) :String
    external fun encodeUrlSafe(orig : String) :String
    external fun encodeUrlSafeWithPadding(orig : String) :String
}