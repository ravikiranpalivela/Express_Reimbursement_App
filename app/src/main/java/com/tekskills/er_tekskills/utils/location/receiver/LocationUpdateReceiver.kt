package com.tekskills.er_tekskills.utils.location.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.tekskills.er_tekskills.utils.location.helper.ActivityServiceHelper.isAppInForeground
import com.google.android.gms.location.LocationResult
import com.tekskills.er_tekskills.data.model.AddLocationCoordinates
import com.tekskills.er_tekskills.data.repository.MainRepository
import com.tekskills.er_tekskills.presentation.activities.LocationLiveData
import com.tekskills.er_tekskills.utils.AppUtil.utlIsNetworkAvailable
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_DEFAULT
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_TOKEN
import com.tekskills.er_tekskills.utils.SuccessResource
import com.tekskills.er_tekskills.utils.location.notification.MyNotificationService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class LocationUpdateReceiver @Inject constructor() : BroadcastReceiver() {

    @Inject
    lateinit var mainRepository: MainRepository

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onReceive(context: Context?, intent: Intent?) {
        val calendar = Calendar.getInstance()
        val date = DateFormat.getInstance().format(calendar.time)

        val location = intent?.let { LocationResult.extractResult(it) }

        location?.let {
            val notification =
                MyNotificationService.createLocationReceivedNotification(context, location, date)

            val notificationManager =
                context?.getSystemService(NotificationManager::class.java) as NotificationManager

            notificationManager.notify(Random.nextInt(), notification)

            LocationLiveData.setLocationData(it)
        }
        Log.i(
            TAG,
            "onReceive: lastLocale: latitude - ${location?.lastLocation?.latitude} longitude - ${location?.lastLocation?.longitude}"
        )

        GlobalScope.launch(Dispatchers.IO) {
            try {
                location?.let { data ->
                    val userCoordinates = AddLocationCoordinates(
                        longitude = data.lastLocation?.longitude.toString(),
                        lattitude = data.lastLocation?.latitude.toString()
                    )
                    LocationLiveData._resAddUserCoordinates.postValue(SuccessResource.loading(null))
                    if (utlIsNetworkAvailable()) {
                        mainRepository.addUserCoordinates(
                            "Bearer ${checkIfUserLogin()}",
                            userCoordinates
                        ).let {
                            if (it.isSuccessful) {
                                Log.d(TAG, "Location data sent successfully")
                                LocationLiveData._resAddUserCoordinates.postValue(
                                    SuccessResource.success(
                                        it.body()
                                    )
                                )
                            } else {
                                saveLocationPrefs(context!!, location, date)
                                Log.e(
                                    TAG,
                                    "Failed to send location data: ${it.errorBody()?.string()}"
                                )
                                LocationLiveData._resAddUserCoordinates.postValue(
                                    SuccessResource.error(
                                        it.errorBody().toString(),
                                        null
                                    )
                                )
                            }
                        }
                    } else {
                        saveLocationPrefs(context!!, location, date)
                    }
                }
            } catch (e: Exception) {
                saveLocationPrefs(context!!, location, date)
                // Exception occurred
                Log.e(TAG, "Exception occurred while sending location data: ${e.message}")
            }
        }
    }

    private fun saveLocationPrefs(context: Context, location: LocationResult?, date: String) {
        val editor = sharedPreferences.edit()
        val oldLocation = sharedPreferences.getString("last_location", "{}") ?: "{}"

        val jsonObject = JSONObject(oldLocation)

        if (!jsonObject.has("last_location")) {
            // Add the "last_location" key and value.
            jsonObject.put("last_location", JSONArray())
        }

        val locationArray = jsonObject.getJSONArray("last_location")


        locationArray.put(
            JSONObject()
                .put("latitude", location?.lastLocation?.latitude.toString())
                .put("longitude", location?.lastLocation?.longitude.toString())
                .put("time", date)
                .put("background", !isAppInForeground(context))
        )

        editor?.putString("last_location", jsonObject.toString())

        editor?.apply()
    }

    companion object {
        private const val TAG = "LocationUpdateReceiver"
    }

    fun checkIfUserLogin(): String {
        return sharedPreferences.getString(PREF_TOKEN, PREF_DEFAULT)!!
    }
}