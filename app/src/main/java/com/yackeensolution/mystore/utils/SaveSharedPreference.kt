package com.yackeensolution.mystore.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SaveSharedPreference {
    private const val PREF_USER_Id = "userId"
    private const val PREF_USER_LANGUAGE = "language"

    private fun getSharedPreferences(ctx: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun setLanguage(ctx: Context, lang: String) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putString(PREF_USER_LANGUAGE, lang)
        editor.apply()
    }

    fun getLanguage(ctx: Context): String? {
        return getSharedPreferences(ctx).getString(PREF_USER_LANGUAGE, "en")
    }

    fun clearLanguage(ctx: Context) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putString(PREF_USER_LANGUAGE, "en")
        editor.apply()
    }

    fun setUserId(ctx: Context, userId: Int) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putInt(PREF_USER_Id, userId)
        editor.apply()
    }

    fun getUserId(ctx: Context): Int {
        return getSharedPreferences(ctx).getInt(PREF_USER_Id, -1)
    }

    fun clearUserId(ctx: Context) {
        val editor = getSharedPreferences(ctx).edit()
        editor.putInt(PREF_USER_Id, -1)
        editor.apply()
    }
}