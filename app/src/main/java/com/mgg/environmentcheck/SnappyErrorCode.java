package com.mgg.environmentcheck;

/**
 * Error codes of snappy-java
 *
 * @author leo
 */
public enum SnappyErrorCode {
	
	// DO NOT change these error code IDs because these numbers are used inside SnappyNative.cpp
	UNKNOWN(0),
	FAILED_TO_LOAD_NATIVE_LIBRARY(1),
	PARSING_ERROR(2),
	NOT_A_DIRECT_BUFFER(3),
	OUT_OF_MEMORY(4),
	FAILED_TO_UNCOMPRESS(5),
	EMPTY_INPUT(6),
	INCOMPATIBLE_VERSION(7),
	INVALID_CHUNK_SIZE(8);
	
	public final int id;
	
	SnappyErrorCode(int id) {
		this.id = id;
	}
	
	public static SnappyErrorCode getErrorCode(int id) {
		for (SnappyErrorCode code : SnappyErrorCode.values()) {
			if (code.id == id) {
				return code;
			}
		}
		return UNKNOWN;
	}
	
	public static String getErrorMessage(int id) {
		return getErrorCode(id).name();
	}
}
