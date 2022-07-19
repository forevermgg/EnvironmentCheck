package com.mgg.environmentcheck;

/**
 * This abstract class represents a native object from core.
 * It specifies the operations common to all such objects.
 * All Java classes wrapping a core class should implement NativeObject.
 */
public interface NativeObject {
	long NULLPTR = 0L;
	
	/**
	 * Gets the pointer of a native object.
	 *
	 * @return the native pointer.
	 */
	long getNativePtr();
	
	/**
	 * Gets the function pointer which points to the function to free the native object.
	 * The function should be defined like: {@code typedef void (*FinalizeFunc)(jlong ptr)}.
	 *
	 * @return the function pointer for freeing the native resource.
	 */
	long getNativeFinalizerPtr();
}