package com.tekskills.er_tekskills.presentation.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_DEFAULT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        setFullscreen()
//        setVersion()
        setTransition()
    }

    private fun setFullscreen() {
        supportActionBar?.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

//    @SuppressLint("SetTextI18n")
//    private fun setVersion(){
//        val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)
//        val versionNow = packageInfo.versionName
//        val tvVersion = findViewById<TextView>(R.id.tv_version)
//        tvVersion.text = "Versi $versionNow"
//    }

    private fun setTransition() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if (viewModel.checkIfUserLogin() == PREF_DEFAULT)
                Intent(this, LoginActivity::class.java)
            else
                Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000L) //delay 2 second
    }
}