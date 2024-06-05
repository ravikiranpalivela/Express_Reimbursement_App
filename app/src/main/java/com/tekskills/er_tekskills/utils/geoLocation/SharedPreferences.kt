package com.tekskills.er_tekskills.utils.geoLocation

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.github.florent37.application.provider.ApplicationProvider.application

val sharedPreferencesData: SharedPreferences?
    get() {
        return PreferenceManager
            .getDefaultSharedPreferences(application?.safeContext() ?: return null)
    }