package com.mgg.kotlinextensions.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}