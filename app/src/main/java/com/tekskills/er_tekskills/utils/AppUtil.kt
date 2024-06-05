package com.tekskills.er_tekskills.utils

import android.content.Context
import android.location.Location
import android.net.ConnectivityManager
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponse
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponseData
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

object AppUtil {

    fun utlIsNetworkAvailable(): Boolean {

        val connectivityManager = ConfigProvider.getConfiguration().appContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager?

        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun showSnackBar(view: View)
    {
        Snackbar.make(
            view,
            "Manager Approval Mandatory",
            Snackbar.LENGTH_SHORT
        )
            .show()
    }



    fun isWithinRange(targetLat: Double, targetLon: Double, latA: Double, lonA: Double, rangeInMeters: Float): Boolean {
        // Create Location objects for the points
        val locationA = Location("point A")
        locationA.latitude = latA
        locationA.longitude = lonA

        val targetLocation = Location("target location")
        targetLocation.latitude = targetLat
        targetLocation.longitude = targetLon

        // Calculate the distances from the target location to each point
        val distanceToA = targetLocation.distanceTo(locationA)

        // Check if the target location is within the specified range
        return distanceToA <= rangeInMeters
    }

    fun isWithinOutsideRange(targetLat: Double, targetLon: Double, latA: Double, lonA: Double, rangeInMeters: Float): Boolean {
        // Create Location objects for the points
        val locationA = Location("point A")
        locationA.latitude = latA
        locationA.longitude = lonA

        val targetLocation = Location("target location")
        targetLocation.latitude = targetLat
        targetLocation.longitude = targetLon

        // Calculate the distances from the target location to each point
        val distanceToA = targetLocation.distanceTo(locationA)

        // Check if the target location is within the specified range
        return distanceToA >= rangeInMeters
    }

    fun filterMeetingsTodayTomorrow(items: MeetingPurposeResponse): ArrayList<MeetingPurposeResponseData> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Calendar.getInstance().time
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.time
        val todayString = sdf.format(today)
        val tomorrowString = sdf.format(tomorrow)

        return items.filter {
//            if (it.visitTime != null) {
//                it.visitTime == todayString || it.visitTime == tomorrowString
//            } else {
            it.visitDate == todayString || it.visitDate == tomorrowString
//            }
        }.sortedBy { LocalDate.parse(it.visitDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
            .toCollection(ArrayList())
    }


//    fun hideStatusBar() {
//        getDecorView()
//            .setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        or View.SYSTEM_UI_FLAG_FULLSCREEN
//                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//            )
//    }

}