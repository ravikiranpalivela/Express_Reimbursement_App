package com.tekskills.er_tekskills.Configration

import android.Manifest
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.ActivityCompat

/**
 * Created by Ravi Kiran p
 */
class GPSService(// saving the context for later use
    private val mContext: Context
) : Service(), LocationListener {
    // if GPS is enabled
    var isGPSEnabled = false

    // if Network is enabled
    var isNetworkEnabled = false

    // if Location co-ordinates are available using GPS or Network
	@JvmField
	var isLocationAvailable = false

    // Location and co-ordinates coordinates
    var mLocation: Location? = null
    var mLatitude = 0.0
    var mLongitude = 0.0

    // Declaring a Location Manager
    protected var mLocationManager: LocationManager?

    init {
        mLocationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
    }

    val location: Location?
        get() {
            try {
                isGPSEnabled = mLocationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    /*if (ContextCompat.checkSelfPermission( this,
						android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {*/
                    //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return mLocation
                    }
                    mLocationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        TIME,
                        DISTANCE.toFloat(),
                        this
                    )
                    if (mLocationManager != null) {
                        mLocation =
                            mLocationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        if (mLocation != null) {
                            mLatitude = mLocation!!.latitude
                            mLongitude = mLocation!!.longitude
                            isLocationAvailable = true // setting a flag that
                            return mLocation
                        }
                    }
                    isGPSEnabled = true
                    //}
                }
                isNetworkEnabled =
                    mLocationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                if (isNetworkEnabled) {
                    mLocationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        TIME,
                        DISTANCE.toFloat(),
                        this
                    )
                    if (mLocationManager != null) {
                        mLocation =
                            mLocationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (mLocation != null) {
                            mLatitude = mLocation!!.latitude
                            mLongitude = mLocation!!.longitude
                            isLocationAvailable = true // setting a flag that
                            return mLocation
                        }
                    }
                }
                if (!isGPSEnabled) {
                    askUserToOpenGPS()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isLocationAvailable = false
            return null
        }
    val latitude: Double
        get() {
            if (mLocation != null) {
                mLatitude = mLocation!!.latitude
            }
            return mLatitude
        }
    val longitude: Double
        /**
         * get longitude
         *
         * @return longitude in double
         */
        get() {
            if (mLocation != null) {
                mLongitude = mLocation!!.longitude
            }
            return mLongitude
        }

    /**
     * show settings to open GPS
     */
    fun askUserToOpenGPS() {
        val mAlertDialog = AlertDialog.Builder(mContext)
        // Setting Dialog Title
        mAlertDialog.setTitle("Location not available, Open GPS?")
            .setMessage("Activate GPS to use use location services?")
            .setPositiveButton("Open Settings") { dialog, which ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                mContext.startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }.show()
    }

    /**
     * Updating the location when location changes
     */
    override fun onLocationChanged(location: Location) {
        mLatitude = location.latitude
        mLongitude = location.longitude
    }

    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    companion object {
        // Minimum time fluctuation for next update (in milliseconds)
        private const val TIME: Long = 30000

        // Minimum distance fluctuation for next update (in meters)
        private const val DISTANCE: Long = 20
    }
}
