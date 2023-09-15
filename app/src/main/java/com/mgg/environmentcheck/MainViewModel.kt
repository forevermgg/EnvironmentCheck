package com.mgg.environmentcheck

import com.fbs.app.main.viewmodel.Property
import timber.log.Timber
import java.util.function.Consumer

class MainViewModel : QtNativeViewModel() {
    override fun getViewModelType(): String {
        return MainViewModel::class.java.name
    }

    override fun bindSetPropDispatcher() {
        setPropDispatcherByInterface[Property.SHOW_TOAST] = object : ViewModelHandleProp {
            override fun setPropDispatcher(viewModel: QtNativeViewModel?, value: String?) {
                showToast(value)
            }
        }
        setPropDispatcherByFunction[Property.SHOW_EMPTY] = Consumer<String> { t -> showEmpty(t) }
        setPropDispatcherByFunction[Property.UI_DATA] = Consumer<String> { params -> setUIData(params) }
        setPropDispatcherByFunction[Property.SHOW_ERROR] = Consumer<String> { params -> showError(params) }
    }

    @BindNativeSetPropFunctionAnnotation(functionId = Property.UI_DATA)
    private fun setUIData(params: String) {
        Timber.e(params)
    }

    @BindNativeSetPropFunctionAnnotation(functionId = Property.SHOW_EMPTY)
    private fun showEmpty(params: String) {
        Timber.e(params)
    }

    @BindNativeSetPropFunctionAnnotation(functionId = Property.SHOW_ERROR)
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