@file:Suppress("DEPRECATION")

package com.yackeensolution.mystore

import android.content.Context
import android.content.res.Configuration
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.core.view.ViewCompat
import java.util.*

object Utils {

    fun isValidNumber(text: EditText?, error: String): Boolean {
        return if (!TextUtils.isEmpty(text!!.text) && Patterns.PHONE.matcher(text.text).matches()) {
            text.error = null
            true
        } else {
            text.error = error
            false
        }
    }

    fun isValidPassword(text: EditText?, error: String): Boolean {
        return if (!TextUtils.isEmpty(text!!.text) && text.text.length > 6) {
            text.error = null
            true
        } else {
            text.error = error
            false
        }
    }

    fun isValidEmail(text: EditText?, description: String): Boolean {
        return if (!TextUtils.isEmpty(text!!.text) && Patterns.EMAIL_ADDRESS.matcher(text.text).matches()) {
            text.error = null
            true
        } else {
            text.error = "Please Enter $description"
            false
        }
    }

    fun isValueSet(text: EditText?, error: String): Boolean {
        return if (TextUtils.isEmpty(text!!.text)) {
            text.error = error
            false
        } else {
            text.error = null
            true
        }
    }

    fun setLocale(mContext: Context) {
        val locale = Locale(SaveSharedPreference.getLanguage(mContext))
        val configuration = Configuration()
        configuration.setLocale(locale)
        mContext.resources.updateConfiguration(configuration, mContext.resources.displayMetrics)
    }

    fun rtlSupport(mContext: Context, view: View) {
        if (SaveSharedPreference.getLanguage(mContext) == "ar") {
            ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_RTL)
        } else if (SaveSharedPreference.getLanguage(mContext) == "en") {
            ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR)
        }
    }
}
