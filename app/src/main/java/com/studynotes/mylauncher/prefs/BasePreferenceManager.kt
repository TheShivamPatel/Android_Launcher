package com.studynotes.mylauncher.prefs

import android.content.Context

object BasePreferenceManager {
    fun getInt(context: Context, key: String?): Int? {
        val prefs =
            context.getSharedPreferences(SharedPrefsConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val value = prefs.getInt(key, Int.MIN_VALUE)
        return if (value == Int.MIN_VALUE) {
            null
        } else {
            value
        }
    }

    fun putInt(context: Context, key: String?, value: Int) {
        val prefs =
            context.getSharedPreferences(SharedPrefsConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putInt(key, value)
        edit.apply()
    }

    fun getLong(context: Context, key: String?): Long? {
        val prefs =
            context.getSharedPreferences(SharedPrefsConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val value = prefs.getLong(key, Long.MIN_VALUE)
        return if (value == Long.MIN_VALUE) {
            null
        } else {
            value
        }
    }

    fun putLong(context: Context, key: String?, value: Long) {
        val prefs =
            context.getSharedPreferences(SharedPrefsConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putLong(key, value)
        edit.apply()
    }

    fun getFloat(context: Context, key: String?): Float? {
        val prefs =
            context.getSharedPreferences(SharedPrefsConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val value = prefs.getFloat(key, Float.MIN_VALUE)
        return if (value == Float.MIN_VALUE) {
            null
        } else {
            value
        }
    }

    fun putFloat(context: Context, key: String?, value: Float) {
        val prefs =
            context.getSharedPreferences(SharedPrefsConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putFloat(key, value)
        edit.apply()
    }

    fun getBoolean(context: Context, key: String?, defValue: Boolean): Boolean {
        val prefs =
            context.getSharedPreferences(SharedPrefsConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, defValue)
    }

    fun putBoolean(context: Context, key: String?, value: Boolean) {
        val prefs =
            context.getSharedPreferences(SharedPrefsConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putBoolean(key, value)
        edit.apply()
    }

    fun getString(context: Context, key: String?): String {
        val prefs =
            context.getSharedPreferences(SharedPrefsConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        return prefs.getString(key, "") ?: ""
    }

    fun putString(context: Context, key: String?, value: String?) {
        val prefs =
            context.getSharedPreferences(SharedPrefsConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putString(key, value)
        edit.apply()
    }

    fun clear(context: Context) {
        val prefs =
            context.getSharedPreferences(SharedPrefsConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit().clear()
        editor.apply()
    }
}
