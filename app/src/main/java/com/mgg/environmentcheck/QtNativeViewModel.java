package com.mgg.environmentcheck;

import android.annotation.SuppressLint;
import android.os.Looper;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.ViewModel;

import com.fbs.app.ToastParams;
import com.hjq.toast.ToastUtils;

import org.json.JSONObject;

import java.nio.ByteBuffer;

import timber.log.Timber;

public abstract class QtNativeViewModel extends ViewModel implements NativeObject {
	private static final long nativeFinalizerPtr = nativeGetFinalizerPtr();
	private static final Object mutex = new Object();
	private long nativePtr = 0L;
	private boolean isRelease = false;
	
	public QtNativeViewModel() {
		Timber.e("QtNativeViewModel Create");
		nativePtr = nativeCreate(getViewModelType());
		NativeContext.dummyContext.addReference(this);
		Timber.e("QtNativeViewModel Create nativePtr = %s", String.valueOf(nativePtr));
	}
	
	private static native long nativeGetFinalizerPtr();
	
	private static native long nativeCreate(String viewModelType);
	
	private static native void nativeBind(final long nativePtr, QtNativeViewModel bindJavaViewModel);
	
	private static native void nativeUnBind(final long nativePtr);
	
	private static native void nativeHandleIntKey(final long nativePtr, int key, String value);
	
	private static native void nativeHandleStringKey(final long nativePtr, String key, String value);
	
	private static native void nativeRelease(final long nativePtr);
	
	@Override
	protected void onCleared() {
		super.onCleared();
		Timber.e("QtNativeViewModel onCleared");
		synchronized (mutex) {
			if (!isRelease) {
				nativeUnBind(nativePtr);
				nativeRelease(nativePtr);
				isRelease = true;
				Timber.e("QtNativeViewModel onCleared nativeRelease");
			}
		}
	}
	
	public void bindJavaViewModel() {
		nativeBind(nativePtr, this);
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Timber.e("QtNativeViewModel finalize");
		synchronized (mutex) {
			if (!isRelease) {
				nativeUnBind(nativePtr);
				nativeRelease(nativePtr);
				isRelease = true;
				Timber.e("QtNativeViewModel finalize nativeRelease");
			}
		}
	}
	
	abstract String getViewModelType();
	
	public boolean isMainThread() {
		return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
	}
	
	@SuppressLint("RestrictedApi")
	public void showToast(String params) {
		if (!isMainThread()) {
			ArchTaskExecutor.getInstance().executeOnMainThread(() -> {
				ToastUtils.show(params);
			});
		}
	}
	
	public void showToast(byte[] params) {
		ToastParams toastParams = ToastParams.Companion.getRootAsToastParams(ByteBuffer.wrap(params));
		Timber.e("showToast: toastParams content = %s duration = %d", toastParams.getContent(), toastParams.getDuration());
	}
	
	public void setProp(int key, String value) {
		Timber.e("setProp(String key = " + key + " String value = " + value + ")");
	}
	
	public void handle(String key, String value) {
		nativeHandleStringKey(nativePtr, key, value);
	}
	
	public void handle(int key, String value) {
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
