package com.tekskills.er_tekskills.utils.geoLocation

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import com.google.android.gms.location.LocationResult
import com.tekskills.geolocator.geofencer.Geofencer
import com.tekskills.geolocator.geofencer.models.LocationTrackerUpdateModule
import timber.log.Timber

class LocationTrackerWorker(val context: Context) : LocationTrackerUpdateModule(context) {

    override fun onLocationResult(locationResult: LocationResult) {

        Timber.v("locationResult=$locationResult")

        sharedPreferencesData?.edit {
            putString(USER_LOCATION_KEY, locationResult.toJson())
        }
    }

    companion object {
        const val USER_LOCATION_KEY = "user_location"
    }
}
