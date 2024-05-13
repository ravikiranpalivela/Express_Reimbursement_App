package com.tekskills.er_tekskills.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.tekskills.er_tekskills.presentation.activities.LoginActivity
import com.tekskills.er_tekskills.presentation.activities.MainActivity

/**
 * Created by Ravi Kiran p
 */
class SessionManager(// Context
    var _context: Context
) {
    // Shared Preferences
    var pref: SharedPreferences

    // Editor for Shared preferences
    var editor: SharedPreferences.Editor

    // Shared pref mode
    var PRIVATE_MODE = 0

    // Constructor
    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    /**
     * Create login session
     */
    fun createLoginSession(name: String?, email: String?) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true)

        // Storing name in pref
        editor.putString(KEY_NAME, name)

        // Storing email in pref
        editor.putString(KEY_EMAIL, email)

        // commit changes
        editor.commit()
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    fun checkLogin() {
        // Check login status
        if (!isLoggedIn) {
            // user is not logged in redirect him to Login Activity
            val i = Intent(_context, LoginActivity::class.java)
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            // Staring Login Activity
            _context.startActivity(i)
        } else {
            // user is logged in redirect him to Home Activity
            val i = Intent(_context, MainActivity::class.java) //HomeActivity
            /* // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/

            // Staring Login Activity
            _context.startActivity(i)
        }
    }

    val userDetails: HashMap<String, String>
        /**
         * Get stored session data
         */
        get() {
            val user = HashMap<String, String>()
            // user name
            user[KEY_NAME] = pref.getString(KEY_NAME, null)!!

            // user email id
            user[KEY_EMAIL] =
                pref.getString(KEY_EMAIL, null)!!

            // return user
            return user
        }

    /**
     * Clear session details
     */
    fun logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear()
        editor.commit()
        com.tekskills.er_tekskills.utils.SharedPreferences.clearAllPreferences(_context)
        com.tekskills.er_tekskills.data.model.SharedPreferences.clearAllPreferences(_context)
        // After logout redirect user to Loing Activity
        val i = Intent(_context, LoginActivity::class.java)
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // Staring Login Activity
        _context.startActivity(i)
    }

    val isLoggedIn: Boolean
        /**
         * Quick check for login
         */
        get() = pref.getBoolean(IS_LOGIN, false)

    companion object {
        // Sharedpref file name
        private const val PREF_NAME = "Tekskills"

        // All Shared Preferences Keys
        private const val IS_LOGIN = "IsLoggedIn"

        // User name (make variable public to access from outside)
        const val KEY_NAME = "name"

        // Email address (make variable public to access from outside)
        const val KEY_EMAIL = "email"
    }
}
