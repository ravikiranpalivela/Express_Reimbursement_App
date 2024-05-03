package com.tekskills.er_tekskills.presentation.di

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.utils.CTConfig
import com.tekskills.er_tekskills.utils.Common.Companion.THEME
import com.tekskills.er_tekskills.utils.ConfigProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PSVTekskillsApplication : Application(){
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        if(sharedPreferences.getBoolean(THEME, false))
             AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        ConfigProvider.setConfiguration(
            CTConfig(
                this,
                getString(R.string.serverUrl)
            )
        )
    }
}