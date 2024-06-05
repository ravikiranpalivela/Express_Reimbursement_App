package com.tekskills.er_tekskills.utils.geoLocation

import android.content.Context
import com.tekskills.geolocator.geofencer.models.GeoFenceUpdateModule
import com.tekskills.geolocator.geofencer.models.Geofence
import timber.log.Timber

class NotificationWorker (private val context: Context): GeoFenceUpdateModule(context){
    override fun onGeofence(geofence: Geofence) {
        Timber.d("onGeofence $geofence")
        sendNotification(
            context = context,
            title = geofence.title,
            message = geofence.message
        )
    }

}