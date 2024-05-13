package com.tekskills.er_tekskills.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by Ravi Kiran p
 */
object SharedPreferences {
    private var preferences: SharedPreferences? = null
    fun savePreferences(activity: Activity?, key: String?, value: String?) {
        if (preferences == null) preferences =
            PreferenceManager.getDefaultSharedPreferences(activity)
        preferences!!.edit().putString(key, value).commit()
    }

    fun getPreferences(activity: Context?, key: String?): String? {
        if (preferences == null) preferences =
            PreferenceManager.getDefaultSharedPreferences(activity)
        return preferences!!.getString(key, "")
    }

    fun saveBooleanPreferences(activity: Activity?, key: String?, value: Boolean) {
        if (preferences == null) preferences =
            PreferenceManager.getDefaultSharedPreferences(activity)
        preferences!!.edit().putBoolean(key, value).commit()
    }

    fun getBooleanPreferences(activity: Activity?, key: String?): Boolean {
        if (preferences == null) preferences =
            PreferenceManager.getDefaultSharedPreferences(activity)
        return preferences!!.getBoolean(key, false)
    }

    fun saveLongPreferences(activity: Activity?, key: String?, value: Long) {
        if (preferences == null) preferences =
            PreferenceManager.getDefaultSharedPreferences(activity)
        preferences!!.edit().putLong(key, value).commit()
    }

    fun getLongPreferences(activity: Activity?, key: String?): Long {
        if (preferences == null) preferences =
            PreferenceManager.getDefaultSharedPreferences(activity)
        return preferences!!.getLong(key, 0)
    }

    fun saveIntPreferences(context: Context?, key: String?, value: Int) {
        if (preferences == null) preferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        preferences!!.edit().putInt(key, value).commit()
    }

    fun getIntPreferences(activity: Activity?, key: String?): Int {
        if (preferences == null) preferences =
            PreferenceManager.getDefaultSharedPreferences(activity)
        return preferences!!.getInt(key, 0)
    }

    fun saveFcmInPreferences(context: Context?, key: String?, value: String?) {
        if (preferences == null) preferences =
            PreferenceManager.getDefaultSharedPreferences(context)
        preferences!!.edit().putString(key, value).commit()
    }

    fun clearAllPreferences(activity: Context?) {
        if (preferences == null) preferences =
            PreferenceManager.getDefaultSharedPreferences(activity)
        preferences!!.edit().clear().commit()
    }
}
