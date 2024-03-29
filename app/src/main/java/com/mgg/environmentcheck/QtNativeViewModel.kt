package com.mgg.environmentcheck

import android.annotation.SuppressLint
import android.os.Looper
import android.widget.Toast
import androidx.annotation.Keep
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.ViewModel
import com.fbs.app.toast.ToastParams.Companion.getRootAsToastParams
import com.hjq.toast.ToastUtils
import com.mgg.checkenv.ContextProvider
import timber.log.Timber
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.function.Consumer

@Keep
abstract class QtNativeViewModel : ViewModel(), NativeObject {
    private val mutex = Any()
    private val shellHolderLock = ReentrantReadWriteLock()
    private val mainLooper: Looper = Looper.getMainLooper()
    var setPropDispatcherByInterface: ConcurrentHashMap<Int, ViewModelHandleProp> =
        ConcurrentHashMap<Int, ViewModelHandleProp>()
    var setPropDispatcherByFunction =
        ConcurrentHashMap<Int, Consumer<String>>()
    private var nativePtr = 0L
    private var isRelease = false
    var isViewModelAttached = false

    override fun onCleared() {
        super.onCleared()
        Timber.e("QtNativeViewModel onCleared")
        dispose()
    }

    private fun dispose() {
        synchronized(mutex) {
            if (!isRelease) {
                isViewModelAttached = false
                setPropDispatcherByInterface.clear()
                setPropDispatcherByFunction.clear()
                nativeUnBind(nativePtr)
                nativePtr = nativeRelease(nativePtr)
                isRelease = true
                Timber.e("QtNativeViewModel onCleared nativeRelease")
            }
        }
    }

    abstract fun getViewModelType(): String
    abstract fun bindSetPropDispatcher()
    fun bindJavaViewModel() {
        nativeBind(nativePtr, this)
    }

    val isMainThread: Boolean
        get() = Looper.getMainLooper().thread.id == Thread.currentThread().id

    private fun ensureRunningOnMainThread() {
        if (Looper.myLooper() != mainLooper) {
            throw RuntimeException(
                "Methods marked with @UiThread must be executed on the main thread. Current thread: "
                        + Thread.currentThread().name
            )
        }
    }

    private fun ensureAttachedToNative() {
        if (nativePtr == 0L) {
            throw RuntimeException(
                "Cannot execute operation because ViewModel is not attached to native."
            )
        }
    }

    @SuppressLint("RestrictedApi")
    fun showToast(params: String?) {
        if (!isViewModelAttached) {
            return
        }
        if (!isMainThread) {
            ArchTaskExecutor.getInstance()
                .executeOnMainThread { ToastUtils.show(params) }
        } else {
            ToastUtils.show(params)
        }
    }

    @SuppressLint("RestrictedApi")
    fun showToast(params: ByteArray?) {
        if (!isViewModelAttached) {
            return
        }
        val toastParams = getRootAsToastParams(ByteBuffer.wrap(params))
        Timber.e(
            "showToast: toastParams content = %s duration = %d",
            toastParams.content, toastParams.duration
        )
        ArchTaskExecutor.getInstance()
            .postToMainThread {
                if (toastParams.duration > 0) {
                    Toast.makeText(
                        ContextProvider.get().context,
                        toastParams.content,
                        toastParams.duration
                    )
                        .show()
                } else {
                    Toast.makeText(
                        ContextProvider.get().context,
                        toastParams.content,
                        toastParams.systemsDuration
                    )
                        .show()
                }
            }
    }

    fun showLoading(params: ByteArray?) {
        ensureAttachedToNative()
        ensureRunningOnMainThread()
        if (isViewModelAttached) {
            Timber.e("showLoading")
        }
    }

    fun hiddenLoading() {
        ensureAttachedToNative()
        ensureRunningOnMainThread()
        if (isViewModelAttached) {
            Timber.e("hiddenLoading")
        }
    }

    open fun setProp(key: Int, value: String?) {
        Timber.e("setProp:%s", Arrays.toString(QtNativeViewModel::class.java.annotations))
        Timber.e("setProp:%s", Arrays.toString(QtNativeViewModel::class.java.declaredAnnotations))
        if (!isViewModelAttached) {
            return
        }
        Timber.e("setProp(String key = $key String value = $value)")
        // 从逻辑分派Dispatcher中获得业务逻辑代码，result变量是一段lambda表达式
        synchronized(mutex) {
            if (value != null) {
                setPropDispatcherByFunction[key]?.accept(value)
                setPropDispatcherByInterface[key]?.setPropDispatcher(this, value)
            }
        }
    }

    fun handle(key: Int, value: String) {
        if (!isViewModelAttached) {
            return
        }
        nativeHandleIntKey(nativePtr, key, value)
    }

    override fun getNativePtr(): Long {
        return nativePtr
    }

    override fun getNativeFinalizerPtr(): Long {
        return nativeFinalizerPtr
    }

    interface ViewModelHandleProp {
        fun setPropDispatcher(viewModel: QtNativeViewModel?, value: String?)
    }

    private val nativeFinalizerPtr = nativeGetFinalizerPtr()
    private external fun nativeGetFinalizerPtr(): Long
    private external fun nativeCreate(viewModelType: String): Long
    private external fun nativeBind(nativePtr: Long, bindJavaViewModel: QtNativeViewModel)
    private external fun nativeUnBind(nativePtr: Long)
    private external fun nativeHandleIntKey(nativePtr: Long, key: Int, value: String)
    private external fun nativeRelease(nativePtr: Long): Long

    init {
        Timber.e("QtNativeViewModel Create")
        nativePtr = nativeCreate(getViewModelType())
        // NativeContext.dummyContext.addReference(this);
        Timber.e("QtNativeViewModel Create nativePtr = %s", nativePtr.toString())
        bindSetPropDispatcher()
        bindJavaViewModel()
    }
}