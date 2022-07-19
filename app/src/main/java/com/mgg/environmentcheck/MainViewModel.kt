package com.mgg.environmentcheck

import com.fbs.app.main.viewmodel.Property
import timber.log.Timber
import java.util.function.Consumer

class MainViewModel() : QtNativeViewModel() {
    private val setPropDispatcher1:HashMap<Int, ViewModelHandleProp> = HashMap()
    private val setPropDispatcher2:HashMap<Int, Consumer<String>> = HashMap()
    init {
        bindSetPropDispatcher()
        bindJavaViewModel()
    }

    private fun bindSetPropDispatcher() {
        setPropDispatcher1.put(Property.SHOWTOAST) { obj: QtNativeViewModel, params: String? -> obj.showToast(params) }
        setPropDispatcher2.put(Property.UIDATA) { params -> setUIData(params) }
        setPropDispatcher2.put(Property.SHOWEMPTY) { params -> showEmpty(params) }
        setPropDispatcher2.put(Property.SHOWERROR) { params -> showError(params) }
    }

    private fun setUIData(params: String) {
    }

    private fun showEmpty(params: String) {
    }

    private fun showError(params: String) {
    }

    override fun setProp(key: Int, value: String?) {
        super.setProp(key, value)
        Timber.e("setProp(String key = $key String value = $value)")
        // 从逻辑分派Dispatcher中获得业务逻辑代码，result变量是一段lambda表达式
        if (value != null) {
            setPropDispatcher2[key]?.accept(value)
            setPropDispatcher1[key]?.setPropDispatcher(this, value)
        }
    }

    public override fun getViewModelType(): String {
        return MainViewModel::class.java.name
    }
}