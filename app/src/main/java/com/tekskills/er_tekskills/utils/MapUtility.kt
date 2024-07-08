package com.tekskills.er_tekskills.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import com.tekskills.er_tekskills.R

object MapUtility {
    const val MAP_URL: String = "https://maps.googleapis.com"

    var currentLocation: Location? = null
    var popupWindow: Dialog? = null
    var ADDRESS: String = "address"
    var LATITUDE: String = "lat"
    var LONGITUDE: String = "long"
    var PLACE_ID: String = "id"
    var NAME: String = "name"

    /**
     * Two Letters county ISO code like PK, US, AU, AE etc
     */
    var COUNTRY_ISO_CODE: String = ""

    fun isNetworkAvailable(context: Context): Boolean {
        var activeNetworkInfo: NetworkInfo? = null
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            activeNetworkInfo = connectivityManager.activeNetworkInfo
        } catch (ex: Exception) {
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun showToast(context: Context?, message: String?) {
        try {
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
            toast.show()
        } catch (ex: Exception) {
        }
    }

    fun showProgress(context: Context) {
        try {
            if (!(context as Activity).isFinishing) {
                val layout = LayoutInflater.from(context).inflate(R.layout.popup_loading, null)
                popupWindow = Dialog(context, android.R.style.Theme_Translucent)
                popupWindow!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                popupWindow!!.setContentView(layout)
                popupWindow!!.setCancelable(false)
                if (!context.isFinishing) {
                    popupWindow!!.show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideProgress() {
        try {
            if (popupWindow != null && popupWindow!!.isShowing) {
                popupWindow!!.dismiss()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}