package com.mgg.environmentcheck;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Locale;

public class SnappyNative {
  public native String nativeLibraryVersion();

  // ------------------------------------------------------------------------
  // Generic compression/decompression routines.
  // ------------------------------------------------------------------------
  public native long rawCompress(long inputAddr, long inputSize, long destAddr) throws IOException;

  public native long rawUncompress(long inputAddr, long inputSize, long destAddr)
      throws IOException;

  public native int rawCompress(
      ByteBuffer input, int inputOffset, int inputLength, ByteBuffer compressed, int outputOffset)
      throws IOException;

  public native int rawCompress(
      Object input, int inputOffset, int inputByteLength, Object output, int outputOffset)
      throws IOException;

  public native int rawUncompress(
      ByteBuffer compressed,
      int inputOffset,
      int inputLength,
      ByteBuffer uncompressed,
      int outputOffset)
      throws IOException;

  public native int rawUncompress(
      Object input, int inputOffset, int inputLength, Object output, int outputOffset)
      throws IOException;

  // Returns the maximal size of the compressed representation of
  // input data that is "source_bytes" bytes in length;
  public native int maxCompressedLength(int source_bytes);

  // This operation takes O(1) time.
  public native int uncompressedLength(ByteBuffer compressed, int offset, int len)
      throws IOException;

  public native int uncompressedLength(Object input, int offset, int len) throws IOException;

  public native long uncompressedLength(long inputAddr, long len) throws IOException;

  public native boolean isValidCompressedBuffer(ByteBuffer compressed, int offset, int len)
      throws IOException;

  public native boolean isValidCompressedBuffer(Object input, int offset, int len)
      throws IOException;

  public native boolean isValidCompressedBuffer(long inputAddr, long offset, long len)
      throws IOException;

  public native void arrayCopy(Object src, int offset, int byteLength, Object dest, int dOffset)
      throws IOException;

  public void throw_error(int errorCode) throws IOException {
    throw new IOException(
        String.format(
            Locale.getDefault(), "%s(%d)", SnappyErrorCode.getErrorMessage(errorCode), errorCode));
  }
}
