package com.mgg.environmentcheck;

import java.io.File;

public class SnappyLoader {
	private static final File nativeLibFile = null;
	private static boolean isLoaded = false;
	private static volatile SnappyNative snappyApi = null;
	
	static void cleanUpExtractedNativeLib() {
		if (nativeLibFile != null && nativeLibFile.exists()) {
			boolean deleted = nativeLibFile.delete();
			if (!deleted) {
				// Deleting native lib has failed, but it's not serious so simply ignore it here
			}
			snappyApi = null;
		}
	}
	
	/**
	 * Set the `snappyApi` instance.
	 *
	 * @param nativeCode
	 */
	static synchronized void setSnappyApi(SnappyNative nativeCode) {
		snappyApi = nativeCode;
	}
	
	static synchronized SnappyNative loadSnappyApi() {
		if (snappyApi != null) {
			return snappyApi;
		}
		loadNativeLibrary();
		setSnappyApi(new SnappyNative());
		return snappyApi;
	}
	
	/**
	 * Load a native library of snappy-java
	 */
	private synchronized static void loadNativeLibrary() {
		if (!isLoaded) {
			// Load preinstalled snappyjava (in the path -Djava.library.path)
			System.loadLibrary("snappy-android");
			isLoaded = true;
		}
	}
}