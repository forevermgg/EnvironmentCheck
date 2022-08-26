package com.mgg.environmentcheck;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * This class is used for holding the reference to the native pointers present in NativeObjects.
 * This is required as phantom references cannot access the original objects for this value. The
 * phantom references will be stored in a double linked list to avoid the reference itself gets
 * GCed. When the referent get GCed, the reference will be added to the ReferenceQueue. Loop in the
 * daemon thread will retrieve the phantom reference from the ReferenceQueue then dealloc the
 * referent and remove the reference from the double linked list. See {@link FinalizerRunnable} for
 * more implementation details.
 */
final class NativeObjectReference extends PhantomReference<NativeObject> {

  private static final ReferencePool referencePool = new ReferencePool();
  // The pointer to the native object to be handled
  private final long nativePtr;
  // The pointer to the native finalize function
  private final long nativeFinalizerPtr;
  private final NativeContext context;
  private NativeObjectReference prev;
  private NativeObjectReference next;

  NativeObjectReference(
      NativeContext context,
      NativeObject referent,
      ReferenceQueue<? super NativeObject> referenceQueue) {
    super(referent, referenceQueue);
    this.nativePtr = referent.getNativePtr();
    this.nativeFinalizerPtr = referent.getNativeFinalizerPtr();
    this.context = context;
    referencePool.add(this);
  }

  /** Calls the native finalizer function to free the given native pointer. */
  private static native void nativeCleanUp(long nativeFinalizer, long nativePointer);

  /** To dealloc native resources. */
  void cleanup() {
    synchronized (context) {
      nativeCleanUp(nativeFinalizerPtr, nativePtr);
    }
    // Remove the PhantomReference from the pool to free it.
    referencePool.remove(this);
  }

  // Linked list to keep the reference of the PhantomReference
  private static class ReferencePool {
    NativeObjectReference head;

    synchronized void add(NativeObjectReference ref) {
      ref.prev = null;
      ref.next = head;
      if (head != null) {
        head.prev = ref;
      }
      head = ref;
    }

    synchronized void remove(NativeObjectReference ref) {
      NativeObjectReference next = ref.next;
      NativeObjectReference prev = ref.prev;
      ref.next = null;
      ref.prev = null;
      if (prev != null) {
        prev.next = next;
      } else {
        head = next;
      }
      if (next != null) {
        next.prev = prev;
      }
    }
  }
}
