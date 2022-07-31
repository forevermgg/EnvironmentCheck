package com.mgg.environmentcheck;

import android.annotation.SuppressLint;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.ViewModel;

import com.fbs.app.toast.ToastParams;
import com.hjq.toast.ToastUtils;
import com.mgg.checkenv.ContextProvider;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

import timber.log.Timber;

@Keep
public abstract class QtNativeViewModel extends ViewModel implements NativeObject {
	private static final long nativeFinalizerPtr = nativeGetFinalizerPtr();
	private final Object mutex = new Object();
	private ReentrantReadWriteLock shellHolderLock = new ReentrantReadWriteLock();
	private long nativePtr = 0L;
	private boolean isRelease = false;
	private boolean isViewModelAttached = false;
	@NonNull
	private final Looper mainLooper;
	public ConcurrentHashMap<Integer, ViewModelHandleProp> setPropDispatcherByInterface = new ConcurrentHashMap();
	public ConcurrentHashMap<Integer, Consumer<String>> setPropDispatcherByFunction = new ConcurrentHashMap<>();
	
	public QtNativeViewModel() {
		mainLooper = Looper.getMainLooper();
		Timber.e("QtNativeViewModel Create");
		nativePtr = nativeCreate(getViewModelType());
		// NativeContext.dummyContext.addReference(this);
		Timber.e("QtNativeViewModel Create nativePtr = %s", String.valueOf(nativePtr));
		bindSetPropDispatcher();
		bindJavaViewModel();
	}
	
	private static native long nativeGetFinalizerPtr();
	
	private static native long nativeCreate(String viewModelType);
	
	private static native void nativeBind(final long nativePtr, QtNativeViewModel bindJavaViewModel);
	
	private static native void nativeUnBind(final long nativePtr);
	
	private static native void nativeHandleIntKey(final long nativePtr, int key, String value);
	
	private static native long nativeRelease(final long nativePtr);
	
	@Override
	protected void onCleared() {
		super.onCleared();
		Timber.e("QtNativeViewModel onCleared");
		dispose();
	}
	
	private void dispose() {
		synchronized (mutex) {
			if (!isRelease) {
				isViewModelAttached = false;
				setPropDispatcherByInterface.clear();
				setPropDispatcherByFunction.clear();
				nativeUnBind(nativePtr);
				nativePtr = nativeRelease(nativePtr);
				isRelease = true;
				Timber.e("QtNativeViewModel onCleared nativeRelease");
			}
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Timber.e("QtNativeViewModel finalize");
		dispose();
	}
	
	abstract String getViewModelType();
	
	abstract void bindSetPropDispatcher();
	
	public void bindJavaViewModel() {
		nativeBind(nativePtr, this);
	}
	
	public boolean isMainThread() {
		return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
	}
	
	public boolean isViewModelAttached() {
		return isViewModelAttached;
	}
	
	private void ensureRunningOnMainThread() {
		if (Looper.myLooper() != mainLooper) {
			throw new RuntimeException(
					"Methods marked with @UiThread must be executed on the main thread. Current thread: "
							+ Thread.currentThread().getName());
		}
	}
	
	private void ensureAttachedToNative() {
		if (nativePtr == 0) {
			throw new RuntimeException(
					"Cannot execute operation because ViewModel is not attached to native.");
		}
	}
	
	public void setViewModelAttached(boolean viewModelAttached) {
		isViewModelAttached = viewModelAttached;
	}
	
	@SuppressLint("RestrictedApi")
	public void showToast(String params) {
		if (!isViewModelAttached()) {
			return;
		}
		if (!isMainThread()) {
			ArchTaskExecutor.getInstance().executeOnMainThread(() -> {
				ToastUtils.show(params);
			});
		} else {
			ToastUtils.show(params);
		}
	}
	
	@SuppressLint("RestrictedApi")
	public void showToast(byte[] params) {
		if (!isViewModelAttached()) {
			return;
		}
		ToastParams toastParams = ToastParams.Companion.getRootAsToastParams(ByteBuffer.wrap(params));
		Timber.e("showToast: toastParams content = %s duration = %d", toastParams.getContent(), toastParams.getDuration());
		ArchTaskExecutor.getInstance().postToMainThread(new Runnable() {
			@Override
			public void run() {
				if (toastParams.getDuration() > 0) {
					Toast.makeText(ContextProvider.get().getContext(), toastParams.getContent(), toastParams.getDuration()).show();
				} else {
					Toast.makeText(ContextProvider.get().getContext(), toastParams.getContent(), toastParams.getSystemsDuration()).show();
				}
			}
		});
	}
	
	public void showLoading(byte[] params) {
		ensureAttachedToNative();
		ensureRunningOnMainThread();
		if (isViewModelAttached()) {
			Timber.e("showLoading");
		}
	}
	
	public void hiddenLoading() {
		ensureAttachedToNative();
		ensureRunningOnMainThread();
		if (isViewModelAttached()) {
			Timber.e("hiddenLoading");
		}
	}
	
	public void setProp(int key, String value) {
		if (!isViewModelAttached()) {
			return;
		}
		Timber.e("setProp(String key = " + key + " String value = " + value + ")");
		// 从逻辑分派Dispatcher中获得业务逻辑代码，result变量是一段lambda表达式
		synchronized (mutex) {
			if (value != null) {
				setPropDispatcherByFunction.get(key).accept(value);
				setPropDispatcherByInterface.get(key).setPropDispatcher(this, value);
			}
		}
	}
	
	public void handle(int key, String value) {
		if (!isViewModelAttached()) {
			return;
		}
		nativeHandleIntKey(nativePtr, key, value);
	}
	
	@Override
	public long getNativePtr() {
		return nativePtr;
	}
	
	@Override
	public long getNativeFinalizerPtr() {
		return nativeFinalizerPtr;
	}
	
	public interface ViewModelHandleProp {
		void setPropDispatcher(QtNativeViewModel viewModel, String value);
	}
}
