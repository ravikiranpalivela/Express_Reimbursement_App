package com.tekskills.er_tekskills.utils.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

//fun executeUnderLocationPermission(
//    context: Context,
//    locationResultLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
//    happyPathCallback: () -> Unit
//) {
//    if (ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED
//    ) {
//
//        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
//
//        locationResultLauncher.launch(permissions)
//        return
//    }
//
//    if (ContextCompat.checkSelfPermission(
//            context,
//            Manifest.permission.ACCESS_BACKGROUND_LOCATION
//        ) != PackageManager.PERMISSION_GRANTED
//    ) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            locationResultLauncher.launch(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
//        }
//        happyPathCallback()
//        return
//    }
//
//    happyPathCallback()
//}


fun executeUnderLocationPermission(
    context: Context,
    happyPathCallback: () -> Unit
) {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {

        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_FINE_LOCATION
        )
        return
    }

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                PERMISSION_REQUEST_BACKGROUND_LOCATION
            )
        }
        happyPathCallback()
        return
    }

    happyPathCallback()
}

// Define these constants as per your requirement
const val PERMISSION_REQUEST_FINE_LOCATION = 100
const val PERMISSION_REQUEST_BACKGROUND_LOCATION = 101