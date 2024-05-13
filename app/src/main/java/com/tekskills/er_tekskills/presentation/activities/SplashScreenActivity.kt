package com.tekskills.er_tekskills.presentation.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources.NotFoundException
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.services.SmsObserver
import com.tekskills.er_tekskills.utils.SessionManager

/**
 * Created by Ravi kiran P
 */
class SplashScreenActivity : AppCompatActivity() {
    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124

    /** Duration of wait  */
    private val SPLASH_DISPLAY_LENGTH = 2000
    var mySharedPreferences: SharedPreferences? = null
    var session: SessionManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mySharedPreferences = applicationContext.getSharedPreferences("MyPref", MODE_PRIVATE)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        setContentView(R.layout.activity_splash)
        try {
            val SMS_STATUS_URI = Uri.parse("content://sms")
            val smsSentObserver = SmsObserver(Handler(), this)
            this.contentResolver.registerContentObserver(SMS_STATUS_URI, true, smsSentObserver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions()
        } else {
            displaySplash()
        }
    }

    private fun displaySplash() {
        try {
            session = SessionManager(this@SplashScreenActivity)
            //session.checkLogin();
            Handler().postDelayed({
                session!!.checkLogin()
                finish()
            }, SPLASH_DISPLAY_LENGTH.toLong())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun requestPermissions() {
        val permissionsNeeded: MutableList<String> = ArrayList()
        val permissionsList: MutableList<String> = ArrayList()
        if (addPermission(
                permissionsList,
                Manifest.permission.READ_CALENDAR
            )
        ) permissionsNeeded.add("Read Calendar")
        if (addPermission(
                permissionsList,
                Manifest.permission.WRITE_CALENDAR
            )
        ) permissionsNeeded.add("Write Calendar")
        if (addPermission(
                permissionsList,
                Manifest.permission.INTERNET
            )
        ) permissionsNeeded.add("Internet")
        if (addPermission(
                permissionsList,
                Manifest.permission.READ_PHONE_STATE
            )
        ) permissionsNeeded.add("Read phone State")
        if (addPermission(
                permissionsList,
                Manifest.permission.PROCESS_OUTGOING_CALLS
            )
        ) permissionsNeeded.add("Process Outgoing Calls")
        if (addPermission(
                permissionsList,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) permissionsNeeded.add("Write External Storage")
        if (addPermission(permissionsList, Manifest.permission.RECORD_AUDIO)) permissionsNeeded.add(
            "Record Audio"
        )
        if (addPermission(
                permissionsList,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) permissionsNeeded.add("Read External Storage")
        if (addPermission(
                permissionsList,
                Manifest.permission.ACCESS_NETWORK_STATE
            )
        ) permissionsNeeded.add("Access Network State")
        if (addPermission(
                permissionsList,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) permissionsNeeded.add("Access Coarse Location")
        if (addPermission(
                permissionsList,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) permissionsNeeded.add("Access Fine Location")
        if (addPermission(
                permissionsList,
                Manifest.permission.CALL_PHONE
            )
        ) permissionsNeeded.add("Call Phone")
        if (!addPermission(
                permissionsList,
                Manifest.permission.READ_SMS
            )
        ) permissionsNeeded.add("READ_SMS")
        if (!addPermission(
                permissionsList,
                Manifest.permission.READ_CONTACTS
            )
        ) permissionsNeeded.add("READ_CONTACTS")
        if (permissionsList.size > 0) {
            if (permissionsNeeded.size > 0) {
                try {
                    ActivityCompat.requestPermissions(
                        this@SplashScreenActivity,
                        permissionsList.toTypedArray<String>(),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
                    )
                    return
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                }
                return
            }
            ActivityCompat.requestPermissions(
                this,
                permissionsList.toTypedArray<String>(),
                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
            )
            return
        }
        displaySplash()
    }

    @SuppressLint("Override")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        // Initial
        // Fill with results
        // Check for ACCESS_FINE_LOCATION
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            val perms: MutableMap<String, Int> = HashMap()
            perms[Manifest.permission.READ_CALENDAR] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.WRITE_CALENDAR] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.INTERNET] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.READ_PHONE_STATE] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.PROCESS_OUTGOING_CALLS] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.RECORD_AUDIO] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.ACCESS_NETWORK_STATE] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.ACCESS_COARSE_LOCATION] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.ACCESS_FINE_LOCATION] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.CALL_PHONE] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.READ_SMS] = PackageManager.PERMISSION_GRANTED
            perms[Manifest.permission.READ_CONTACTS] = PackageManager.PERMISSION_GRANTED
            for (i in permissions.indices) perms[permissions[i]] = grantResults[i]
            if (perms[Manifest.permission.READ_CALENDAR] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.WRITE_CALENDAR] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.INTERNET] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.READ_PHONE_STATE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.PROCESS_OUTGOING_CALLS] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.RECORD_AUDIO] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.ACCESS_NETWORK_STATE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.ACCESS_COARSE_LOCATION] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.ACCESS_FINE_LOCATION] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.CALL_PHONE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.READ_SMS] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.READ_CONTACTS] == PackageManager.PERMISSION_GRANTED
            ) {
                displaySplash()
            } else {
                // Permission Denied
                Toast.makeText(this, getString(R.string.msgDeniedPermission), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /**
     * Add-up the permissions and requests them on UI, based on whether it is already allowed or not.
     */
    private fun addPermission(permissionsList: MutableList<String>, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsList.add(permission)
            // Check for Rationale Option
            return !ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
        }
        return false
    }

    companion object {
        var flag: String? = null
    }
}
