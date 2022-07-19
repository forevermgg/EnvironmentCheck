package com.mgg.checkenv.module

import android.text.TextUtils
import org.json.JSONObject
import java.io.Serializable


open class BaseBean : Serializable {
    var jsonObject = JSONObject()


    protected open fun toJSONObject(): JSONObject {
        return jsonObject
    }

    protected fun isEmpty(value: String?): String? {
        return if (TextUtils.isEmpty(value)) {
            BaseData.UNKNOWN_PARAM
        } else value
    }

    protected fun isEmpty(value: CharSequence?): String? {
        return value?.toString() ?: BaseData.UNKNOWN_PARAM
    }
}
