package com.tekskills.er_tekskills.data.local

import android.content.Context
import com.tekskills.er_tekskills.utils.Common.Companion.MANAGER
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_DEFAULT
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_ROLE_TYPE
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_TOKEN
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_TOKEN_DATA


class SharedPrefManager private constructor(private val mCtx: Context) {

    val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    val isLoggedIn: Boolean
        get() {
            return sharedPreferences.getInt("id", -1) != -1
        }

    var isFirstTimeLaunch: String?
        get() {
            return sharedPreferences.getString(PREF_TOKEN_DATA, PREF_DEFAULT)
        }
        set(isFirstTime) {
            val editor = sharedPreferences.edit()
            editor.putString(PREF_TOKEN_DATA, isFirstTime)
            editor.apply()
        }

    fun saveAuthToken(auth_token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(PREF_TOKEN, auth_token)
        editor.apply()
    }

    fun getAuthToken(): String {
        return sharedPreferences.getString(PREF_TOKEN, PREF_DEFAULT)!!
    }

    fun saveUserRole(subscription: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putString(PREF_ROLE_TYPE,if(subscription) MANAGER else PREF_DEFAULT )
        editor.apply()
    }

    fun getUserRole(): String {
        return sharedPreferences.getString(PREF_ROLE_TYPE, PREF_DEFAULT)!!
    }

    fun savePrefData(key: String,value: String)
    {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getPrefData(key: String,defValue: String): String
    {
        return sharedPreferences.getString(key, defValue)!!
    }

    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private val SHARED_PREF_NAME = "my_pexcel_shared_pref"
        private var mInstance: SharedPrefManager? = null

        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

}