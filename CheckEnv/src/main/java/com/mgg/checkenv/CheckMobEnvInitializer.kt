package com.mgg.checkenv

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log


class CheckMobEnvInitializer : ContentProvider() {
    override fun onCreate(): Boolean {
        val context = context
        if (context == null) {
            Log.e(TAG, "Context is null,Please init this!")
        }
        if (context != null) {
            mContext = context
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context
        val TAG: String = CheckMobEnvInitializer::class.java.simpleName
    }
}
