package com.mgg.environmentcheck

import com.fbs.app.main.viewmodel.Property
import timber.log.Timber
import java.util.function.Consumer

class MainViewModel : QtNativeViewModel() {
    override fun getViewModelType(): String {
        return MainViewModel::class.java.name
    }

    override fun bindSetPropDispatcher() {
        setPropDispatcherByInterface[Property.SHOWTOAST] = object : ViewModelHandleProp {
            override fun setPropDispatcher(viewModel: QtNativeViewModel?, value: String?) {
                showToast(value)
            }
        }
        setPropDispatcherByFunction[Property.SHOWEMPTY] = Consumer<String> { t -> showEmpty(t) }
        setPropDispatcherByFunction[Property.UIDATA] = Consumer<String> { params -> setUIData(params) }
        setPropDispatcherByFunction[Property.SHOWERROR] = Consumer<String> { params -> showError(params) }
    }

    @BindNativeSetPropFunctionAnnotation(functionId = Property.UIDATA)
    private fun setUIData(params: String) {
        Timber.e(params)
    }

    @BindNativeSetPropFunctionAnnotation(functionId = Property.SHOWEMPTY)
    private fun showEmpty(params: String) {
        Timber.e(params)
    }

    @BindNativeSetPropFunctionAnnotation(functionId = Property.SHOWERROR)
    private fun showError(params: String) {
        Timber.e(params)
    }

    override fun setProp(key: Int, value: String?) {
        super.setProp(key, value)
        if (value != null) {
            this.kotlinHandleSetPropFunction(key, value)
        }
    }
}