@file:Suppress("DEPRECATION")

package com.mgg.checkenv.sign

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import java.security.MessageDigest
import java.util.*

class CheckSign {
    companion object {
        private val TAG: String = CheckSign::class.java.simpleName
    }

    fun getSignatureStr(context: Context): String {
        val signature = getSignature(context)
        return signature?.toByteArray()?.let {
            try {
                val md5 = MessageDigest.getInstance("MD5")
                val sha1 = MessageDigest.getInstance("SHA1")
                val sha256 = MessageDigest.getInstance("SHA256")
                val md5Key = md5.digest(it)
                val sha1Key = sha1.digest(it)
                val sha256Key = sha256.digest(it)
                String.format(
                    "MD5: %s\n\nSHA1: %s\n\nSHA256: %s",
                    byteArrayToString(md5Key),
                    byteArrayToString(sha1Key),
                    byteArrayToString(sha256Key)
                )
            } catch (e: Exception) {
                ""
            }
        }.toString()
    }

    @SuppressLint("PackageManagerGetSignatures")
    private fun getSignature(argContext: Context): Signature? {
        var signature: Signature? = null
        try {
            val packageName = argContext.packageName
            val packageManager = argContext.packageManager
            val packageInfo =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            val signatures = packageInfo.signatures
            signature = signatures[0]
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return signature
    }

    private fun byteArrayToString(array: ByteArray): String {
        val hexString = StringBuilder()
        for (i in array.indices) {
            val appendString = Integer.toHexString(0xFF and array[i].toInt()).toUpperCase(Locale.getDefault())
            if (appendString.length == 1) hexString.append("0")
            hexString.append(appendString)
            if (i < array.size - 1) hexString.append(":")
        }
        return hexString.toString()
    }

    external fun getSignature(context: Context?): String?

    external fun checkSignature(context: Context?, signatureMd5: String): String?
}
