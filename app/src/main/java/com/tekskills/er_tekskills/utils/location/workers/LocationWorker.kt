package com.tekskills.er_tekskills.utils.location.workers

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tekskills.er_tekskills.utils.location.receiver.LocationUpdateReceiver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.tekskills.er_tekskills.utils.location.notification.MyNotificationService
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.DateFormat
import java.util.Calendar
import kotlin.coroutines.resume
import kotlin.random.Random

class LocationWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private fun buildLocationRequest(): LocationRequest {

        return LocationRequest
            .Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, UPDATE_INTERVAL)
            .setMinUpdateDistanceMeters(MIN_DISTANCE)
            .build()
    }

    override suspend fun doWork(): Result = suspendCancellableCoroutine { continuation ->
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val calendar = Calendar.getInstance()
            val date = DateFormat.getInstance().format(calendar.time)

            val locationRequest = buildLocationRequest()

            val callbackIntent = Intent(applicationContext, LocationUpdateReceiver::class.java)

            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                callbackIntent,
                PendingIntent.FLAG_MUTABLE
            )

//            val notification = MyNotificationService
//                .createWorkerNotification(applicationContext, date)
//
//            val notificationManager =
//                applicationContext.getSystemService(NotificationManager::class.java) as NotificationManager
//
//            notificationManager.notify(Random.nextInt(), notification)
            fusedLocationClient.requestLocationUpdates(locationRequest, pendingIntent)
            fusedLocationClient.flushLocations()

            continuation.resume(Result.success())
            return@suspendCancellableCoroutine
        }
        continuation.resume(Result.success())
    }

    companion object {
        const val UPDATE_INTERVAL = (1000 * 60 * 15).toLong() // 5 minutes
        const val MIN_DISTANCE = 5f // 5 meters
    }
}