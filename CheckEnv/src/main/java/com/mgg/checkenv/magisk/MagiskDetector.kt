package com.mgg.checkenv.magisk

object MagiskDetector {
    // ==1存在   ==0不存在
    external fun haveMagicMount(): Int
}