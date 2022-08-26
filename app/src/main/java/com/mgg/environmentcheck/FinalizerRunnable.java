package com.mgg.environmentcheck;

import java.lang.ref.ReferenceQueue;

import timber.log.Timber;

// Running in the FinalizingDaemon thread to free native objects.
class FinalizerRunnable implements Runnable {
  private final ReferenceQueue<NativeObject> referenceQueue;

  FinalizerRunnable(ReferenceQueue<NativeObject> referenceQueue) {
    this.referenceQueue = referenceQueue;
  }

  @Override
  public void run() {
    while (true) {
      try {
        NativeObjectReference reference = (NativeObjectReference) referenceQueue.remove();
        reference.cleanup();
      } catch (InterruptedException e) {
        // Restores the interrupted status.
        Thread.currentThread().interrupt();

        Timber.e(
            "The FinalizerRunnable thread has been interrupted."
                + " Native resources cannot be freed anymore");
        break;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
