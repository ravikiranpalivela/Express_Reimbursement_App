package com.tekskills.er_tekskills.presentation.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.databinding.ActivityLocationPickerBinding
import com.tekskills.er_tekskills.utils.MapUtility
import timber.log.Timber
import java.io.IOException
import java.util.Locale
import java.util.regex.Pattern

class LocationPickerActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var binding: ActivityLocationPickerBinding
    private val TAG: String = LocationPickerActivity::class.java.simpleName
    private var userAddress: String? = ""
    private var userAddressName: String? = ""
    private var userState: String? = ""
    private var userCity: String? = ""
    private var userPostalCode: String? = ""
    private var userCountry: String? = ""
    private var userAddressline2: String? = ""
    private var addressBundle: Bundle? = null
    private var mLatitude = 0.0
    private var mLongitude = 0.0
    private var userCountryISOCode: String? = null
    private var place_id = ""
    private var place_url = " "
    private var mMap: GoogleMap? = null
    var mapPlace: Place? = null
    private var mLocationPermissionGranted = false
    var PLACE_AUTOCOMPLETE_REQUEST_CODE: Int = 1

    //initial zoom
    private val previousZoomLevel = -1.0f
    private var isZooming = false

    //Declaration of FusedLocationProviderClient
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val filterTaskList: MutableList<AsyncTask<*, *, *>> = ArrayList()
    var regex: String = "^(-?\\d+(\\.\\d+)?),\\s*(-?\\d+(\\.\\d+)?)$"
    var latLongPattern: Pattern = Pattern.compile(regex)
    private var doAfterPermissionProvided = 0
    private var doAfterLocationSwitchedOn = 1
    private var currentLatitude = 0.0
    private var currentLongitude = 0.0
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_location_picker)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        if (supportActionBar != null) supportActionBar!!.hide()
        // Initialize bundle
        addressBundle = Bundle()

        //initialization of FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            this
        )

        //Prepare for Request for current location
        getLocationRequest()

        //define callback of location request
        locationCallback = object : LocationCallback() {
            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                Timber.tag(TAG)
                    .d(
                        "onLocationAvailability: isLocationAvailable =  %s",
                        locationAvailability.isLocationAvailable
                    )
            }

            override fun onLocationResult(locationResult: LocationResult) {
                Timber.tag(TAG).d("onLocationResult: " + locationResult)
                if (locationResult == null) {
                    return
                }

                when (doAfterLocationSwitchedOn) {
                    1 -> startParsingAddressToShow()
                    2 ->                         //on click of imgCurrent
                        showCurrentLocationOnMap(false)
                    3 ->                         //on Click of Direction Tool
                        showCurrentLocationOnMap(true)
                }
                //Location fetched, update listener can be removed
                fusedLocationProviderClient!!.removeLocationUpdates(locationCallback!!)
            }
        }

        // Try to obtain the map from the SupportMapFragment.
        val mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)
        //if you want to open the location on the LocationPickerActivity through intent
        val i = intent
        if (i != null) {
            val extras = i.extras
            if (extras != null) {
                userAddress = extras.getString(MapUtility.ADDRESS)
                userAddressName = extras.getString(MapUtility.NAME)
//                userAddressline2 = extras.getString(MapUtility.NAME)
                //temp -> get lat , log from db
                mLatitude = intent.getDoubleExtra(MapUtility.LATITUDE, 0.0)
                mLongitude = intent.getDoubleExtra(MapUtility.LONGITUDE, 0.0)
                userCountryISOCode = extras.getString(MapUtility.COUNTRY_ISO_CODE, null)
            }
        }

        if (savedInstanceState != null) {
            mLatitude = savedInstanceState.getDouble("latitude")
            mLongitude = savedInstanceState.getDouble("longitude")
            userAddress = savedInstanceState.getString("userAddress")
            userAddressName = savedInstanceState.getString("userAddressName")
            currentLatitude = savedInstanceState.getDouble("currentLatitude")
            currentLongitude = savedInstanceState.getDouble("currentLongitude")
            userCountryISOCode = savedInstanceState.getString("userCountryISOCode", null)
        }

        if (!MapUtility.isNetworkAvailable(this)) {
            MapUtility.showToast(this, "Please Connect to Internet")
        }

        binding.imgSearch.setOnClickListener(View.OnClickListener {
            if (!Places.isInitialized()) {
                Places.initialize(
                    this@LocationPickerActivity.applicationContext,
                    getString(R.string.google_maps_key)
                )
            }
            // Set the fields to specify which types of place data to return.
            val fields = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.PLUS_CODE
            )

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            )
//                .setCountry(userCountryISOCode)
                .build(this@LocationPickerActivity)
            this@LocationPickerActivity.startActivityForResult(
                intent,
                PLACE_AUTOCOMPLETE_REQUEST_CODE
            )
        })

        binding.fabSelectLocation.setOnClickListener {
            val intent = Intent()
            // add data into intent and send back to Parent Activity
            intent.putExtra(
                MapUtility.ADDRESS,
//                binding.imgSearch.getText().toString()
                userAddress!!.trim { it <= ' ' })
            intent.putExtra(MapUtility.NAME, userAddressName)
            intent.putExtra(MapUtility.LATITUDE, mLatitude)
            intent.putExtra(MapUtility.LONGITUDE, mLongitude)
            intent.putExtra("fullAddress", addressBundle)
//            intent.putExtra("id", place_id) //if you want place id
//            intent.putExtra("url", place_url) //if you want place url
            this@LocationPickerActivity.setResult(RESULT_OK, intent)
            this@LocationPickerActivity.finish()
        }

        binding.imgCurrentloc.setOnClickListener {
            this@LocationPickerActivity.showCurrentLocationOnMap(false)
            doAfterPermissionProvided = 2
            doAfterLocationSwitchedOn = 2
        }

        // google maps tools
        binding.directionTool.setOnClickListener {
            this@LocationPickerActivity.showCurrentLocationOnMap(true)
            doAfterPermissionProvided = 3
            doAfterLocationSwitchedOn = 3
        }

        binding.googleMapsTool.setOnClickListener { // Default google map
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "http://maps.google.com/maps?q=loc:$mLatitude, $mLongitude"
                )
            )
            this@LocationPickerActivity.startActivity(intent)
        }

//        try {
//            Toast.makeText(
//                applicationContext,
//                this.resources.getString(R.string.edittext_hint),
//                Toast.LENGTH_SHORT
//            ).show()
//        } catch (e: Exception) {
//            Toast.makeText(
//                this,
//                this.resources.getString(R.string.edittext_hint),
//                Toast.LENGTH_SHORT
//            ).show()
//        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient!!.removeLocationUpdates(locationCallback!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                val place = Autocomplete.getPlaceFromIntent(data)
                binding.imgSearch.text = place.name
                mLatitude = place.latLng?.latitude ?: 0.0
                mLongitude = place.latLng?.longitude ?: 0.0
//                place_id = place.id ?: ""
//                place_url = place.websiteUri?.toString() ?: ""

                if (mLatitude != 0.0 && mLongitude != 0.0) {
                    if (MapUtility.popupWindow != null && MapUtility.popupWindow!!.isShowing) {
                        MapUtility.hideProgress()
                    }

                    Timber.tag(TAG).d("getAddressByGeoCodingLatLng: START")
                    //Cancel previous tasks and launch this one
                    for (prevTask in filterTaskList) {
                        prevTask.cancel(true)
                    }

                    filterTaskList.clear()
                    val asyncTask = GetAddressFromLatLng()
                    filterTaskList.add(asyncTask)
                    asyncTask.executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR,
                        mLatitude,
                        mLongitude
                    )
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.i(TAG, status.statusMessage ?: "Error")
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Location Not Available..", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Fetching Location...", Toast.LENGTH_LONG).show()
                startLocationUpdates()
            }
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val locationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarsePermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val listPermissionsNeeded: MutableList<String> = ArrayList()

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (coarsePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray<String>(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }

        //getSettingsLocation();
        return true
    }

    private fun showCurrentLocationOnMap(isDirectionClicked: Boolean) {
        if (checkAndRequestPermissions()) {
            @SuppressLint("MissingPermission") val lastLocation =
                fusedLocationProviderClient!!.lastLocation
            lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    mMap!!.clear()
                    if (isDirectionClicked) {
                        currentLatitude = location.latitude
                        currentLongitude = location.longitude
                        //Go to Map for Directions
                        val intent = Intent(
                            Intent.ACTION_VIEW, Uri.parse(
                                "http://maps.google.com/maps?saddr=$currentLatitude, $currentLongitude&daddr=$mLatitude, $mLongitude"
                            )
                        )
                        this@LocationPickerActivity.startActivity(intent)
                    } else {
                        //Go to Current Location
                        mLatitude = location.latitude
                        mLongitude = location.longitude
                        this@LocationPickerActivity.addressByGeoCodingLatLng
                    }
                } else {
                    //Gps not enabled if loc is null
                    this@LocationPickerActivity.settingsLocation
                    Toast.makeText(this@LocationPickerActivity,
                        "Location not Available", Toast.LENGTH_SHORT).show()
                }
            }
            lastLocation.addOnFailureListener { //If perm provided then gps not enabled
//                getSettingsLocation();
                Toast.makeText(this@LocationPickerActivity,
                    "Location Not Availabe", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun resizeMapIcons(iconName: String?, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(
            resources,
            resources.getIdentifier(iconName, "drawable", packageName)
        )
        val resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false)
        return resizedBitmap
    }

    private fun addMarker() {
        val cameraUpdate: CameraUpdate
        val coordinate = LatLng(mLatitude, mLongitude)
        if (mMap != null) {
            val markerOptions: MarkerOptions
            try {
                mMap!!.clear()
                binding.imgSearch.text = userAddressName
//                + userAddress
                markerOptions = MarkerOptions().position(coordinate).title(userAddress).icon(
                    BitmapDescriptorFactory.fromBitmap(
                        resizeMapIcons("ic_pointer", 100, 100)
                    )
                )
                cameraUpdate = if (isZooming) {
                    CameraUpdateFactory.newLatLngZoom(coordinate, mMap!!.cameraPosition.zoom)
                } else {
                    CameraUpdateFactory.newLatLngZoom(coordinate, 18f)
                }

                mMap!!.animateCamera(cameraUpdate)
                mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL

                val marker = mMap!!.addMarker(markerOptions)
                //marker.showInfoWindow();
            } catch (ex: Exception) {
                Timber.tag(TAG).d("address error init $ex")
            }
        }
        try {
            userAddressline2 =
                userAddressline2!!.substring(0, userAddressline2!!.indexOf(userCity!!))
            // userAddressline.replace(userCity,"");
            //  userAddressline.replace(userPostalCode,"");
            //   userAddressline.replace(userState,"");
            //  userAddressline.replace(userCountry,"");
        } catch (ex: Exception) {
            Timber.tag(TAG).d("address error $ex")
        }

        try {
            binding.addressline2.text = userAddress
//            binding.citydetails.text =
//                userCity + SPACE + userPostalCode + SPACE + userState + SPACE + userCountry
        } catch (ex: Exception) {
            Timber.tag(TAG).d("address error text $ex")
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.clear()
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap!!.uiSettings.isMapToolbarEnabled = false
        if (mMap!!.isIndoorEnabled) {
            mMap!!.setIndoorEnabled(false)
        }

        mMap!!.setInfoWindowAdapter(object : InfoWindowAdapter {
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(arg0: Marker): View? {
                val v = layoutInflater.inflate(R.layout.info_window_layout, null)

                // Getting the position from the marker
                val latLng = arg0.position
                mLatitude = latLng.latitude
                mLongitude = latLng.longitude
                val tvLat = v.findViewById<TextView>(R.id.address)
                tvLat.text = userAddress
                return v
            }
        })

        mMap!!.uiSettings.isZoomControlsEnabled = true

        // Setting a click event handler for the map
        mMap!!.setOnMapClickListener { latLng ->
            mMap!!.clear()
            mLatitude = latLng.latitude
            mLongitude = latLng.longitude
            Timber.tag("latlng").e("%s", latLng.toString())
            isZooming = true
            this@LocationPickerActivity.addMarker()
            if (!MapUtility.isNetworkAvailable(this@LocationPickerActivity)) {
                MapUtility.showToast(this@LocationPickerActivity, "Please Connect to Internet")
            }
            this@LocationPickerActivity.addressByGeoCodingLatLng
        }

        if (checkAndRequestPermissions()) {
            startParsingAddressToShow()
        } else {
            doAfterPermissionProvided = 1
        }
    }

    private val settingsLocation: Unit
        get() {
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest!!)

            val result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

            result.addOnCompleteListener { task ->
                try {
                    val response = task.getResult(
                        ApiException::class.java
                    )
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    //...
                    if (response != null) {
                        val locationSettingsStates = response.locationSettingsStates
                        Timber.tag(TAG).d("getSettingsLocation: %s", locationSettingsStates)
                        this@LocationPickerActivity.startLocationUpdates()
                    }
                } catch (exception: ApiException) {
                    Timber.tag(TAG).e("getSettingsLocation: $exception")
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                             // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                val resolvable = exception as ResolvableApiException
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                    this@LocationPickerActivity,
                                    REQUEST_CHECK_SETTINGS
                                )
                            } catch (e: SendIntentException) {
                                Timber.tag(TAG).e("SendIntentException: $exception")

                            } catch (e: ClassCastException) {
                                Timber.tag(TAG).e("ClassCastException: $exception")
                            }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                    }
                }
            }
        }

    /**
     * Show location from intent
     */
    private fun startParsingAddressToShow() {
        //get address from intent to show on map
        if (userAddress == null || userAddress!!.isEmpty()) {
            //if intent does not have address,
            //cell is blank

            showCurrentLocationOnMap(false)
        } else  //check if address contains lat long, then extract
        //format will be lat,lng i.e 19.23234,72.65465
            if (latLongPattern.matcher(userAddress).matches()) {
                val p = Pattern.compile("(-?\\d+(\\.\\d+)?)") // the pattern to search for
                val m = p.matcher(userAddress)

                // if we find a match, get the group
                var i = 0
                while (m.find()) {
                    // we're only looking for 2s group, so get it
                    if (i == 0) mLatitude = m.group().toDouble()
                    if (i == 1) mLongitude = m.group().toDouble()
                    i++
                }
                //show on map
                addressByGeoCodingLatLng
                addMarker()
            } else {
                //get  latlong from String address via reverse geo coding
                //Since lat long not present in db
                if (mLatitude == 0.0 && mLongitude == 0.0) {
                    latLngByRevGeoCodeFromAdd
                } else {
                    // Latlong is more accurate to get exact point on map ,
                    // String address might not be sufficient (i.e Mumbai, Mah..etc)
                    addMarker()
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("latitude", mLatitude)
        outState.putDouble("longitude", mLongitude)
        outState.putString("userAddress", userAddress)
        outState.putString("userAddressName", userAddressName)
        outState.putBundle("addressBundle", addressBundle)
        outState.putDouble("currentLatitude", currentLatitude)
        outState.putDouble("currentLongitude", currentLongitude)
        outState.putString("userCountryISOCode", userCountryISOCode)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // app icon in action bar clicked; goto parent activity.
                this.finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private val addressByGeoCodingLatLng: Unit
        get() {
            //Get string address by geo coding from lat long

            if (mLatitude != 0.0 && mLongitude != 0.0) {
                if (MapUtility.popupWindow != null && MapUtility.popupWindow!!.isShowing) {
                    MapUtility.hideProgress()
                }

                Timber.tag(TAG).d("getAddressByGeoCodingLatLng: START")
                //Cancel previous tasks and launch this one
                for (prevTask in filterTaskList) {
                    prevTask.cancel(true)
                }

                filterTaskList.clear()
                val asyncTask = GetAddressFromLatLng()
                filterTaskList.add(asyncTask)
                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mLatitude, mLongitude)
            }
        }

    private val latLngByRevGeoCodeFromAdd: Unit
        get() {
            //Get string address by geo coding from lat long

            if (mLatitude == 0.0 && mLongitude == 0.0) {
                if (MapUtility.popupWindow != null && MapUtility.popupWindow!!.isShowing) {
                    MapUtility.hideProgress()
                }

                Timber.tag(TAG).d("getLatLngByRevGeoCodeFromAdd: START")
                //Cancel previous tasks and launch this one
                for (prevTask in filterTaskList) {
                    prevTask.cancel(true)
                }

                filterTaskList.clear()
                val asyncTask = GetLatLngFromAddress()
                filterTaskList.add(asyncTask)
                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, userAddress)
            }
        }

    @SuppressLint("StaticFieldLeak")
    private inner class GetAddressFromLatLng : AsyncTask<Double?, Void?, Bundle?>() {
        var latitude: Double? = null
        var longitude: Double? = null
        override fun doInBackground(vararg params: Double?): Bundle? {
            try {
                latitude = params[0]
                longitude = params[1]




                val addresses: List<Address>
                val geocoder = Geocoder(this@LocationPickerActivity, Locale.getDefault())

                val sb = StringBuilder()

                //get location from lat long if address string is null
                addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)!!

                if (addresses != null && addresses.size > 0) {
                    val name = addresses[0].featureName
                    if (name != null) addressBundle!!.putString("name", name)
                    sb.append(name).append(" ")

                    val address = addresses[0].getAddressLine(0)
                    if (address != null) addressBundle!!.putString("addressline2", address)
                    sb.append(address).append(" ")

                    val city = addresses[0].locality
                    if (city != null) addressBundle!!.putString("city", city)
                    sb.append(city).append(" ")

                    val state = addresses[0].adminArea
                    if (state != null) addressBundle!!.putString("state", state)
                    sb.append(state).append(" ")

                    val country = addresses[0].countryName
                    if (country != null) addressBundle!!.putString("country", country)
                    sb.append(country).append(" ")

                    val postalCode = addresses[0].postalCode
                    if (postalCode != null) addressBundle!!.putString("postalcode", postalCode)
                    sb.append(postalCode).append(" ")

                    // return sb.toString();
                    addressBundle!!.putString("fulladdress", sb.toString())

                    return addressBundle
                } else {
                    return null
                }
            } catch (e: IOException) {
                e.printStackTrace()
                addressBundle!!.putBoolean("error", true)
                return addressBundle

                //return roundAvoid(latitude) + "," + roundAvoid(longitude);
            }

            // return bu;
        }

        override fun onPreExecute() {
            super.onPreExecute()
            MapUtility.showProgress(this@LocationPickerActivity)
        }

        // setting address into different components
        override fun onPostExecute(userAddress: Bundle?) {
            super.onPostExecute(userAddress)
            if (userAddress != null) {
                this@LocationPickerActivity.userAddress = userAddress.getString("fulladdress")
                this@LocationPickerActivity.userAddressName = userAddress.getString("name")
                this@LocationPickerActivity.userCity = userAddress.getString("city")
                this@LocationPickerActivity.userState = userAddress.getString("state")
                this@LocationPickerActivity.userPostalCode = userAddress.getString("postalcode")
                this@LocationPickerActivity.userCountry = userAddress.getString("country")
                this@LocationPickerActivity.userAddressline2 = userAddress.getString("addressline2")
            }
            MapUtility.hideProgress()
            addMarker()
        }
    }

    private inner class GetLatLngFromAddress : AsyncTask<String?, Void?, LatLng>() {
        override fun onPreExecute() {
            super.onPreExecute()
            MapUtility.showProgress(this@LocationPickerActivity)
        }

        protected override fun doInBackground(vararg params: String?): LatLng? {
            var latLng = LatLng(0.0, 0.0)

            try {
                val addresses: List<Address>
                val geocoder = Geocoder(this@LocationPickerActivity, Locale.getDefault())

                //get location from lat long if address string is null
                addresses = geocoder.getFromLocationName(params[0]!!, 1)!!

                if (addresses != null && addresses.size > 0) {
                    latLng = LatLng(addresses[0].latitude, addresses[0].longitude)
                }
            } catch (ignored: Exception) {
            }
            return latLng
        }

        override fun onPostExecute(latLng: LatLng) {
            super.onPostExecute(latLng)
            this@LocationPickerActivity.mLatitude = latLng.latitude
            this@LocationPickerActivity.mLongitude = latLng.longitude
            MapUtility.hideProgress()
            addMarker()
        }
    }


//    fun roundAvoid(value: Double): Double {
//        val scale: Double = 10.pow(6.0)
//        return Math.round(value * scale) / scale
//    }

    override fun onDestroy() {
        super.onDestroy()
        for (task in filterTaskList) {
            task.cancel(true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mLocationPermissionGranted = false
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    mLocationPermissionGranted = true
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        //Do tasks for which permission was granted by user in onRequestPermission()
        if (!isFinishing && mLocationPermissionGranted) {
            // perform action required b4 asking permission
            mLocationPermissionGranted = false
            when (doAfterPermissionProvided) {
                1 -> startParsingAddressToShow()
                2 -> showCurrentLocationOnMap(false)
                3 -> showCurrentLocationOnMap(true)
            }
        }
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                this@LocationPickerActivity,
                "Location not Available",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest!!,
            locationCallback!!,
            null /* Looper */
        )
            .addOnSuccessListener { Timber.tag(TAG).d("startLocationUpdates: onSuccess: ") }
            .addOnFailureListener { e ->
                if (e is ApiException) {
                    Timber.tag(TAG).d("startLocationUpdates: %s", e.message)
                } else {
                    Timber.tag(TAG).d("startLocationUpdates: %s", e.message)
                }
            }
    }

    private fun getLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest!!.setInterval(10000)
        locationRequest!!.setFastestInterval(3000)
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 2
        private const val REQUEST_ID_MULTIPLE_PERMISSIONS = 2
    }
}

