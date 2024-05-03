package com.tekskills.er_tekskills.utils

import android.content.Context
import android.net.ConnectivityManager

object AppUtil {

    fun utlIsNetworkAvailable(): Boolean {

        val connectivityManager = ConfigProvider.getConfiguration().appContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager?

        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
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