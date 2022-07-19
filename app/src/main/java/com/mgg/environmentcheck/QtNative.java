package com.mgg.environmentcheck;

import android.os.Handler;
import android.os.Looper;

public class QtNative {
	private static final Runnable runPendingCppRunnablesRunnable = new Runnable() {
		@Override
		public void run() {
			runPendingCppRunnables();
		}
	};
	// mutex used to synchronize runnable operations
	public static Object m_mainActivityMutex = new Object();
	private static ClassLoader m_classLoader = null;
	
	public static ClassLoader classLoader() {
		return m_classLoader;
	}
	
	public static void setClassLoader(ClassLoader classLoader) {
		m_classLoader = classLoader;
	}
	
	private static void runPendingCppRunnablesOnAndroidThread() {
		synchronized (m_mainActivityMutex) {
			final Looper mainLooper = Looper.getMainLooper();
			final Thread looperThread = mainLooper.getThread();
			if (looperThread.equals(Thread.currentThread())) {
				runPendingCppRunnablesRunnable.run();
			} else {
				final Handler handler = new Handler(mainLooper);
				handler.post(runPendingCppRunnablesRunnable);
			}
		}
	}
	
	public static native void runPendingCppRunnables();
}
