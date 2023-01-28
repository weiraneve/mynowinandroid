package com.weiran.mynowinandroid.common.utils

import android.content.Context

object SharedPreferenceUtil {

    fun writeBoolean(context: Context, prefName: String?, name: String?, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(name, value).apply()
    }

    fun readBoolean(
        context: Context, prefName: String?,
        name: String?, defaultValue: Boolean
    ): Boolean {
        val sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(name, defaultValue)
    }

    const val SHARED_PREFERENCE_FILE = "preference"

}