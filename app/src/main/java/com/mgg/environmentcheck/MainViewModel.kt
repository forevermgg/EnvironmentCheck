package com.mgg.environmentcheck

import com.fbs.app.main.viewmodel.Property
import timber.log.Timber

class MainViewModel : QtNativeViewModel() {
    public override fun getViewModelType(): String {
        return MainViewModel::class.java.name
    }

    override fun bindSetPropDispatcher() {
        setPropDispatcherByInterface.put(Property.SHOWTOAST) { obj: QtNativeViewModel, params: String? ->
            obj.showToast(params)
        }
        setPropDispatcherByFunction.put(Property.UIDATA) { params -> setUIData(params) }
        setPropDispatcherByFunction.put(Property.SHOWEMPTY) { params -> showEmpty(params) }
        setPropDispatcherByFunction.put(Property.SHOWERROR) { params -> showError(params) }
    }

    private fun setUIData(params: String) {
        Timber.e(params)
    }

    private fun showEmpty(params: String) {
        Timber.e(params)
    }

    private fun showError(params: String) {
        Timber.e(params)
    }
}