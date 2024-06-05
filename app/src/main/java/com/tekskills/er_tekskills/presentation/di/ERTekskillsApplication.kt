package com.tekskills.er_tekskills.presentation.di

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.utils.CTConfig
import com.tekskills.er_tekskills.utils.Common.Companion.THEME
import com.tekskills.er_tekskills.utils.ConfigProvider
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ERTekskillsApplication : Application(){
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        if(sharedPreferences.getBoolean(THEME, false))
             AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

//        if(BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        ConfigProvider.setConfiguration(
            CTConfig(
                this,
                getString(R.string.serverUrl)
            )
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("default", "default", importance).apply {
                description = "default"
            }
            val notificationManager: NotificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }
}