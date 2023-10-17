package com.example.crudsops

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast

internal object Utils {
    fun isValidEmail(target: String): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun displayToast(context: Context, message: String, length: Int) {
        Toast.makeText(
            context,
            message,
            length
        ).show()
    }
}