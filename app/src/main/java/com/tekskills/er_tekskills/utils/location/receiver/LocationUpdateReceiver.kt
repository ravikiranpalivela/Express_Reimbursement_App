package com.tekskills.er_tekskills.utils.location.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.navigation.NavDeepLinkBuilder
import com.tekskills.er_tekskills.utils.location.helper.ActivityServiceHelper.isAppInForeground
import com.google.android.gms.location.LocationResult
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.AddLocationCoordinates
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.er_tekskills.data.model.TaskInfo
import com.tekskills.er_tekskills.data.repository.MainRepository
import com.tekskills.er_tekskills.domain.TaskCategoryRepository
import com.tekskills.er_tekskills.presentation.activities.LocationLiveData
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.fragments.CheckINFragmentArgs
import com.tekskills.er_tekskills.utils.AppUtil.filterMeetingsTodayTomorrow
import com.tekskills.er_tekskills.utils.AppUtil.utlIsNetworkAvailable
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_DEFAULT
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_TOKEN
import com.tekskills.er_tekskills.utils.SuccessResource
import com.tekskills.er_tekskills.utils.location.notification.MyNotificationService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.HashMap
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class LocationUpdateReceiver @Inject constructor() : BroadcastReceiver() {

    @Inject
    lateinit var mainRepository: MainRepository

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var taskCategoryRepository: TaskCategoryRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        val calendar = Calendar.getInstance()
        val date = DateFormat.getInstance().format(calendar.time)

        val location = intent?.let { LocationResult.extractResult(it) }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                location?.let { data ->
                    val userCoordinates = AddLocationCoordinates(
                        longitude = data.lastLocation?.longitude.toString(),
                        lattitude = data.lastLocation?.latitude.toString()
                    )
                    LocationLiveData._resAddUserCoordinates.postValue(SuccessResource.loading(null))
                    if (utlIsNetworkAvailable()) {

                        mainRepository.getMeetingPurposeByStatus(
                            "Bearer ${checkIfUserLogin()}","Today"
                        ).let {
                            it.body()?.let {
                                filterMeetingsTodayTomorrow(it)?.let { listData ->
                                    listData.forEach { meetingData ->
                                        parseJsonToTaskInfo(meetingData).let {
                                            taskCategoryRepository.insertOrUpdateTaskInfo(it)
                                        }
                                    }

                                    taskCategoryRepository.getRangeItems(
                                        data.lastLocation?.latitude!!,
                                        data.lastLocation?.longitude!!,
                                        1000.0
                                    ).let { rangeItems ->
                                        rangeItems.forEach { taskInfo ->
                                            if (taskInfo.checkInTime.trim().isEmpty()
                                                && taskInfo.checkOutTime.trim().isEmpty()
                                            ) {

                                                val arg = CheckINFragmentArgs(taskInfo.TaskID.toString()).toBundle()

                                                val pendingIntent = NavDeepLinkBuilder(context!!)
                                                    .setComponentName(MainActivity::class.java)
                                                    .setGraph(R.navigation.nav_graph)
                                                    .setDestination(R.id.new_check_in)
                                                    .setArguments(arg)
                                                    .createPendingIntent()

                                                val notification = MyNotificationService.createPostNotification(
                                                    context,
                                                    pendingIntent,
                                                    taskInfo,
                                                    "Check In"
                                                )

//                                val notification =
//                                    MyNotificationService.createLocationReceivedNotification(context, location, date)

                                                val notificationManager =
                                                    context?.getSystemService(NotificationManager::class.java) as NotificationManager

                                                notificationManager.notify(Random.nextInt(), notification)

                                            }
//                            else if (taskInfo.checkInTime != null && taskInfo.checkOutTime.trim().isEmpty()) {
//
//                                val requestBody: MutableMap<String, RequestBody> = HashMap()
//                                requestBody["latitude"] =
//                                    data.lastLocation?.latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
//                                requestBody["longitude"] =
//                                    data.lastLocation?.longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
//                                mainRepository.putUserMeetingCheckOUT("Bearer ${checkIfUserLogin()}", taskInfo.TaskID.toString(),requestBody)
//                            }
//                            else {
//
//                            }
                                        }
                                    }

                                    taskCategoryRepository.getOutsideRangeItems(
                                        data.lastLocation?.latitude!!,
                                        data.lastLocation?.longitude!!,
                                        250.0
                                    ).let { rangeItems ->
                                        rangeItems.forEach { taskInfo ->
//                            Toast.makeText(context!!, "task item details ${taskInfo.visitPurpose}", Toast.LENGTH_SHORT).show()
//                            if (taskInfo.checkInTime.trim().isEmpty() && taskInfo.checkOutTime.trim().isEmpty()) {
//
//                                val arg = CheckINFragmentArgs(taskInfo.TaskID.toString()).toBundle()
//
//                                val pendingIntent = NavDeepLinkBuilder(context!!)
//                                    .setGraph(R.navigation.nav_graph)
//                                    .setDestination(R.id.new_check_in)
//                                    .setArguments(arg)
//                                    .createPendingIntent()
//
//                                val notification = MyNotificationService.createPostNotification(context,pendingIntent,taskInfo)
//
////                                val notification =
////                                    MyNotificationService.createLocationReceivedNotification(context, location, date)
//
//                                val notificationManager =
//                                    context?.getSystemService(NotificationManager::class.java) as NotificationManager
//
//                                notificationManager.notify(Random.nextInt(), notification)
//
//                            } else
                                            if (taskInfo.checkInTime.trim()
                                                    .isNotEmpty() && taskInfo.checkOutTime.trim()
                                                    .isEmpty()
                                            ) {

                                                val requestBody: MutableMap<String, RequestBody> = HashMap()
                                                requestBody["latitude"] =
                                                    data.lastLocation?.latitude.toString()
                                                        .toRequestBody("text/plain".toMediaTypeOrNull())
                                                requestBody["longitude"] =
                                                    data.lastLocation?.longitude.toString()
                                                        .toRequestBody("text/plain".toMediaTypeOrNull())
                                                mainRepository.putUserMeetingCheckOUT(
                                                    "Bearer ${checkIfUserLogin()}",
                                                    taskInfo.TaskID.toString(),
                                                    requestBody
                                                ).let {
                                                    if (it.isSuccessful) {
                                                        val arg =
                                                            CheckINFragmentArgs(taskInfo.TaskID.toString()).toBundle()

                                                        val pendingIntent = NavDeepLinkBuilder(context!!)
                                                            .setComponentName(MainActivity::class.java)
                                                            .setGraph(R.navigation.nav_graph)
                                                            .setDestination(R.id.new_add_mom_meeting)
                                                            .setArguments(arg)
                                                            .createPendingIntent()

                                                        val notification =
                                                            MyNotificationService.createPostNotification(
                                                                context,
                                                                pendingIntent,
                                                                taskInfo,
                                                                "Add MOM"
                                                            )
                                                        val notificationManager =
                                                            context?.getSystemService(NotificationManager::class.java) as NotificationManager

                                                        notificationManager.notify(Random.nextInt(), notification)
                                                    }

//                                val notification =
//                                    MyNotificationService.createLocationReceivedNotification(context, location, date)


                                                }
                                            }
//                            else {
//
//                            }
                                        }
                                    }
                                }
                            }
                        }

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

//        location?.let {
//            val notification =
//                MyNotificationService.createLocationReceivedNotification(context, location, date)
//
//            val notificationManager =
//                context?.getSystemService(NotificationManager::class.java) as NotificationManager
//
//            notificationManager.notify(Random.nextInt(), notification)
//
//            LocationLiveData.setLocationData(it)
//        }
        Log.i(
            TAG,
            "onReceive: lastLocale: latitude - ${location?.lastLocation?.latitude} longitude - ${location?.lastLocation?.longitude}"
        )

    }


    fun parseJsonToTaskInfo(responseData: MeetingPurposeResponseData): TaskInfo {

        val userCoordinates = responseData.userCordinates

        return TaskInfo(
            id = responseData.id,
            purposeOfVisit = responseData.visitPurpose ?: "",
            date = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).parse(responseData.visitDate),
            priority = 1, // Assign appropriate value
            status = responseData.status == "Completed",
            category = "", // Assign appropriate value
            clientContactPerson = "", // Assign appropriate value
            clientContactPos = "", // Assign appropriate value
            opportunityDesc = "",
            TaskID = responseData.id,
            customerName = responseData.customerName ?: "",
            custmerEmail = responseData.custmerEmail ?: "",
            modeOfTravel = responseData.modeOfTravel ?: "",
            customerContactName = responseData.customerContactName ?: "",
            customerPhone = responseData.customerPhone.toString(),
            visitDate = responseData.visitDate,
            visitTime = responseData.visitTime,
            visitPurpose = responseData.visitPurpose,
            source = userCoordinates.source,
            sourceLatitude = userCoordinates.sourceLatitude ?: "0.00",
            sourceLongitude = userCoordinates.sourceLongitude ?: "0.00",
            destination = userCoordinates.destination ?: "",
            destinationLatitude = userCoordinates.destinationLatitude ?: "0.00",
            destinationLongitude = userCoordinates.destinationLongitude ?: "0.00",
            totalDistance = userCoordinates.totalDistance,
            checkInTime = userCoordinates.checkInTime ?: "",
            checkInCordinates = userCoordinates.checkInCordinates ?: "",
            checkOutTime = userCoordinates.checkOutTime ?: "",
            checkOutCordinates = userCoordinates.checkOutCordinates ?: "",
            mapTime = userCoordinates.mapTime
        )
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