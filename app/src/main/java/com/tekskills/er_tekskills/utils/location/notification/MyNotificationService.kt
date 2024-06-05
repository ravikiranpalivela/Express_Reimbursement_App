package com.tekskills.er_tekskills.utils.location.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.LocationResult
import com.tekskills.er_tekskills.BuildConfig
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.TaskInfo
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import java.net.URL

object MyNotificationService {
    fun createLocationReceivedNotification(
        context: Context?,
        location: LocationResult?,
        date: String
    ): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Notification.Builder(context, "default")
                .setContentTitle("Location: $date")
                .setSmallIcon(
                    Icon.createWithResource(
                        context,
                        android.R.drawable.ic_menu_mylocation
                    )
                )
                .setContentText("latitude: ${location?.lastLocation?.latitude} longitude: ${location?.lastLocation?.longitude}")
                .setSubText("lastLocation")
                .build()
        } else {
            Notification.Builder(context)
                .setContentTitle("Location: $date")
                .setSmallIcon(
                    Icon.createWithResource(
                        context,
                        android.R.drawable.ic_menu_mylocation
                    )
                )
                .setContentText("latitude: ${location?.lastLocation?.latitude} longitude: ${location?.lastLocation?.longitude}")
                .setSubText("lastLocation")
                .build()
        }
    }

    fun createWorkerNotification(context: Context?, date: String): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Notification.Builder(context, "default")
                .setContentTitle("Updated Location Data: $date")
                .setSmallIcon(
                    Icon.createWithResource(
                        context,
                        R.mipmap.ic_launcher
                    )
                )
                .setSubText("lastLocation")
                .build()
        } else {
            Notification.Builder(context)
                .setContentTitle("Worker executed: $date")
                .setSmallIcon(
                    Icon.createWithResource(
                        context,
                        android.R.drawable.ic_menu_mylocation
                    )
                )
                .setSubText("lastLocation")
                .build()
        }
    }

    fun createPostNotification(
        context: Context,
        intent: PendingIntent,
        taskInfo: TaskInfo,
        dataString: String
    ): Notification {
        val builder = NotificationCompat.Builder(context, "default")
        builder.setContentTitle(
            taskInfo.visitPurpose
        ).setSmallIcon(R.drawable.check_in)

        try {
//            val intent = Intent(context, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
////            mNotificationManager =
////                ctx!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            val contentIntent = PendingIntent.getActivity(
//                context, 0,
//                intent, PendingIntent.FLAG_MUTABLE
//            )
//            val mNotificationManager =
//                getSystemService(NOTIFICATION_SERVICE) as NotificationManager?

            val packageName = BuildConfig.APPLICATION_ID

            val contentViewBig = RemoteViews(packageName, R.layout.custom_notification)
            val contentViewSmall =
                RemoteViews(packageName, R.layout.custom_notification_small)
            contentViewBig.setTextViewText(R.id.title, taskInfo.customerName)
            contentViewSmall.setTextViewText(R.id.title, taskInfo.customerName)
            contentViewBig.setTextViewText(R.id.text, taskInfo.visitPurpose)
            contentViewSmall.setTextViewText(R.id.text, taskInfo.visitPurpose)
            contentViewSmall.setTextViewText(R.id.button, dataString)
            return builder
                .setSmallIcon(R.drawable.location_image_icons8)
                .setCustomContentView(contentViewSmall)
                .setCustomBigContentView(contentViewSmall)
                .setContentTitle(taskInfo.customerName)
                .setContentIntent(intent)
                .setAutoCancel(true)
                .build()
//                    .setWhen(`when`)
//        return builder.setContentText(taskInfo.visitPurpose)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(intent)
//            .setAutoCancel(true)
//            .build()

        } catch (t: Throwable) {
            Log.d("MYFCMLIST", "Error parsing FCM message", t)
            return builder.build()
        }


//        val notificationManager =
//            NotificationManagerCompat.from(context)
//        notificationManager.cancelAll()
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        notificationManager.notify(id.toInt(), notification)
    }


    fun createGeofenceNotification(context: Context?, notificationDetails: String): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, "default")
                .setContentTitle(notificationDetails)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .build()
        } else {
            Notification.Builder(context)
                .setContentTitle(notificationDetails)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .build()
        }
    }
}