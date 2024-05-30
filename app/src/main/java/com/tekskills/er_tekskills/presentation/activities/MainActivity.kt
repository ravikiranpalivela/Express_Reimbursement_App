package com.tekskills.er_tekskills.presentation.activities

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.location.LocationResult
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.local.SharedPrefManager
import com.tekskills.er_tekskills.data.model.AddLocationCoordinates
import com.tekskills.er_tekskills.data.model.AddMeetingRequest
import com.tekskills.er_tekskills.data.model.LocationResponse
import com.tekskills.er_tekskills.data.util.DateToString.Companion.convertDateToString
import com.tekskills.er_tekskills.databinding.ActivityMainBinding
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.NetworkObserver
import com.tekskills.er_tekskills.utils.RestApiStatus
import com.tekskills.er_tekskills.utils.SmartDialog
import com.tekskills.er_tekskills.utils.SmartDialogBuilder
import com.tekskills.er_tekskills.utils.SmartDialogClickListener
import com.tekskills.er_tekskills.utils.SuccessResource
import com.tekskills.er_tekskills.utils.location.executeUnderLocationPermission
import com.tekskills.er_tekskills.utils.location.helper.ActivityServiceHelper.isAppInForeground
import com.tekskills.er_tekskills.utils.location.receiver.LocationUpdateReceiver
import com.tekskills.er_tekskills.utils.location.workers.LocationWorker
import com.tekskills.er_tekskills.utils.location.workers.LocationWorker.Companion.MIN_DISTANCE
import com.tekskills.er_tekskills.utils.location.workers.LocationWorker.Companion.UPDATE_INTERVAL
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    val viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var locationArray: JSONArray

//    private var selectedAddress: AddressData? = null
//
//    private val activityResultLauncher = registerForActivityResult(
//        ActivityResultContracts.StartIntentSenderForResult()
//    ) { result ->
//        viewModel.isRequestGPSDialogOn(false)
//        when (result.resultCode) {
//            Activity.RESULT_OK ->
//                viewModel.startLocationUpdates()
//            else -> {
//                //keep asking if imp or do whatever
//                viewModel.startLocationUpdates()
//            }
//        }
//    }
//
//    private val locationType = LocationType.FINE_LOCATION
//    private val intervalInSecond: Long = 6
//    private val fastIntervalInSecond: Long = 3
//
//    // You can customize the resources of [permission dialog] if you wish
//    private val foregroundUiData = PermissionUIData.Foreground(
//        hideUi = false,
//        btn_approve = R.string.approve_location_access,
//        btn_cancel = android.R.string.cancel,
//    )
//
//    // You can customize the resources of [permission dialog] if you wish
//    private val backgroundUiData = PermissionUIData.Background(
//        hideUi = false,
//        btn_approve = R.string.approve_background_location_access,
//        btn_cancel = android.R.string.cancel,
//    )
//
//    private val mViewModel: GPSLocationViewModel by viewModels {
//        @Suppress("UNCHECKED_CAST")
//        GPSLocationViewModelFactory(application, LocationRepository.getInstance(
//            this, locationType, intervalInSecond, fastIntervalInSecond, activityResultLauncher,
//            PermissionUIData(foreground = foregroundUiData, background = backgroundUiData),
//            // Don't forget to pass broadcast receiver class here
//            LocationBroadcastReceiver::class.java as Class<BroadcastReceiver>
//        ))
//    }

    val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                permissions[Manifest.permission.ACCESS_BACKGROUND_LOCATION] == true
            ) {
                createWorkManager(applicationContext)
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    // Call this function when you want to request location permissions
    private fun requestLocationPermissions() {
        val permissionsToRequest = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        locationPermissionLauncher.launch(permissionsToRequest)
    }

    // Use this method to check if permissions are granted
    private fun checkLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun updateWorkManager() {
        executeUnderLocationPermission(
            this,
        ) {
            createWorkManager(context = this)
        }

//        if (checkLocationPermissions()) {
//            createWorkManager(applicationContext)
//        } else {
//            requestLocationPermissions()
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.appBarMain.toolbar)
        viewModel.getEmployeeMe()

        val jsonArray = getLocations().getJSONArray("last_location")

        locationArray = jsonArray

        updateWorkManager()


        LocationLiveData.locationData.observe(this, Observer { locationResult ->
            // Handle the location data here
            locationResult?.let { data ->

                val userCoordinates = AddLocationCoordinates(
                    longitude = data?.lastLocation?.longitude.toString(),
                    lattitude = data?.lastLocation?.latitude.toString()
                )

                Log.d("TAG", "onViewCreated: print response ${userCoordinates.toString()}")

                viewModel.addUserCoordinates(userCoordinates)
            }
        })

        viewModel.resEmployeeMe.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
//                        binding.progress.visibility = View.GONE
                    if (it.data != null)
                        it.data.let { res ->
                            viewModel.saveUserEmployeeID(res.employeeMaster.roleId.toString())

//                                viewModel.saveUserSubscription(res.employeeMaster.roleId.r)
//                                val intent = Intent(requireActivity(), MainActivity::class.java)
//                                startActivity(intent)
//                                requireActivity().finish()
                        }
                    else {
                        Snackbar.make(
                            binding.root,
                            "Login Failed",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
//                        binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
//                        binding.progress.visibility = View.GONE
                    SmartDialogBuilder(this@MainActivity)
                        .setTitle("Log Out")
                        .setSubTitle("Session Expired")
                        .setCancalable(false)
                        .setCustomIcon(R.drawable.icon2)
                        .setTitleColor(resources.getColor(R.color.black))
                        .setSubTitleColor(resources.getColor(R.color.black))
                        .setNegativeButtonHide(true)
                        .useNeutralButton(true)
                        .setPositiveButton("Okay", object : SmartDialogClickListener {
                            override fun onClick(smartDialog: SmartDialog?) {
                                if (viewModel.clearSharedPrefrence()) {
                                    val intent =
                                        Intent(this@MainActivity, SplashActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                smartDialog!!.dismiss()
                            }
                        })
                        .setNegativeButton("Cancel", object : SmartDialogClickListener {
                            override fun onClick(smartDialog: SmartDialog?) {
                                if (viewModel.clearSharedPrefrence()) {
                                    val intent =
                                        Intent(this@MainActivity, SplashActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                smartDialog!!.dismiss()
                            }
                        })
                        .setNeutralButton("Cancel", object : SmartDialogClickListener {
                            override fun onClick(smartDialog: SmartDialog?) {
                                if (viewModel.clearSharedPrefrence()) {
                                    val intent =
                                        Intent(this@MainActivity, SplashActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                smartDialog!!.dismiss()
                            }
                        })
                        .build().show()

                    Snackbar.make(binding.root, "Log Out", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
//                        binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Log Out", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

//        mViewModel.setLocationType(LocationType.BACKGROUND_LOCATION)
//        updateStartOrStopButtonState(mViewModel.isForegroundOn || mViewModel.isBackgroundOn)
//
//
//        // Receiving location updates if it's starting or not
//        mViewModel.receivingLocationUpdates.observe(this) {
//            updateStartOrStopButtonState(it)
//        }
//
//        // Receiving status of whether the app permission is granted or not.
//        mViewModel.isPermissionGranted.observe(this) {
//            // keep asking if imp or do whatever
//            Log.e("Permission","isPermissionGranted: $it")
//            // viewModel.startLocationUpdates() // keep asking
//        }
//
//        // Receiving location data from [FusedLocationProviderClient]
//        mViewModel.receivingLocation.observe(this) { location ->
//            setToText(location,"Current Location:  ${location.latitude} - ${location.longitude}")
//
//            // Retrieve address data from [Geocoder] with the retrieved location
//            mViewModel.retrieveAddressDataFromGeocoder {
//                setToText(location, "Current Address: ${it.name} - ${it.address}")
//            }
//
//            // Retrieve address data from [Geocoder] using custom location
//            mViewModel.retrieveAddressDataFromGeocoder(location.latitude, location.longitude) {
//                setToText(location, "Current Address: ${it.name} - ${it.address}")
//            }
//        }

        val navController = findNavController(R.id.nav_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_fragment,
//                R.id.view_opportunity_fragment,
                R.id.new_purpose_meeting,
                R.id.view_meetings,
                R.id.settings_fragment,
                R.id.logout
            ), binding.drawerLayout
        )
        binding.navView.menu.findItem(R.id.logout)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                if (viewModel.clearSharedPrefrence()) {
                    val intent = Intent(this, SplashActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                true
            }

        binding.navView.menu.findItem(R.id.home_fragment)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

        binding.navView.menu.findItem(R.id.view_meetings)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                val bundle = Bundle().apply {
                    putString(
                        "opportunityID",
                        ""
                    )
                }
                findNavController(R.id.nav_fragment).navigate(R.id.view_meetings, bundle)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                true
            }

        binding.navView.menu.findItem(R.id.new_purpose_meeting)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                findNavController(R.id.nav_fragment).navigate(R.id.new_purpose_meeting)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                true
            }



        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        viewModel.networkLiveData.observe(this, Observer {
            observeUserNetworkConnection(it)
        })

        NetworkObserver.getNetworkLiveData(applicationContext)
            .observe(this, Observer { isConnected ->
                viewModel._networkLiveData.value = isConnected
//                observeUserNetworkConnection(isConnected)
            })
    }

    private fun observeUserNetworkConnection(isConnected: Boolean) {

        if (!isConnected) {
            binding.appBarMain.includeContainMain.textViewNetworkStatus.text =
                "No internet connection"
            binding.appBarMain.includeContainMain.networkStatusLayout.apply {
                binding.appBarMain.includeContainMain.networkStatusLayout.visibility = View.VISIBLE
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        android.R.color.holo_red_light
                    )
                )
            }
        } else {
            binding.appBarMain.includeContainMain.textViewNetworkStatus.text = "Back Online"

            binding.appBarMain.includeContainMain.networkStatusLayout.apply {
                animate()
                    .alpha(1f)
                    .setStartDelay(1000)
                    .setDuration(1000)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            binding.appBarMain.includeContainMain.networkStatusLayout.visibility =
                                View.GONE
                        }
                    }).start()
                setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        android.R.color.holo_green_dark
                    )
                )
            }
        }
    }


//    private fun updateStartOrStopButtonState(receivingLocation: Boolean) {
//        if (receivingLocation) {
//            findViewById<Button>(R.id.btn_refresh).apply {
//                text = getString(R.string.stop_receiving_location)
//                setOnClickListener { viewModel.stopLocationUpdates() }
//            }
//        } else {
//            findViewById<Button>(R.id.btn_refresh).apply {
//                text = getString(R.string.start_receiving_location)
//                setOnClickListener { viewModel.startLocationUpdates() }
//            }
//        }
//    }
//
//    private var locationString: String = ""
//    private fun setToText(location: AddressData, locationString: String) {
//        this.selectedAddress = location
//        this.locationString += "\n\n$locationString"
//
//        Log.d("TAG", locationString)
//        findViewById<TextView>(R.id.textView).text = this.locationString
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun createWorkManager(context: Context) {
        val updateInterval = 15L

        val locationRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(
            LocationWorker::class.java,
            updateInterval,
            TimeUnit.MINUTES
        ).setInitialDelay(0, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "LocationUpdates",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            locationRequest
        )

//        Toast.makeText(
//            context,
//            "Min distance set: ${MIN_DISTANCE.toString().removeSuffix("f")} meters",
//            Toast.LENGTH_LONG
//        ).show()

//        Toast.makeText(
//            context,
//            "Update interval: ${(UPDATE_INTERVAL / (1000 * 60) % 60)} minutes",
//            Toast.LENGTH_LONG
//        ).show()
    }

    fun getLocations(): JSONObject {

        val oldLocation = sharedPreferences.getString("last_location", "{}") ?: "{}"
        return JSONObject(oldLocation).apply {
            if (!this.has("last_location")) {

                // Add the "last_location" key and value.
                this.put("last_location", JSONArray())
            }
        }
    }

    fun clearLocations(preferences: SharedPreferences) {
        preferences.edit().clear().apply()
    }

}

object LocationLiveData {
    private val _locationData = MutableLiveData<LocationResult>()
    val locationData: LiveData<LocationResult>
        get() = _locationData

    fun setLocationData(locationResult: LocationResult) {
        _locationData.value = locationResult
    }

    val _resAddUserCoordinates =
        MutableLiveData<SuccessResource<LocationResponse>>()

    val resAddUserCoordinates: LiveData<SuccessResource<LocationResponse>>
        get() = _resAddUserCoordinates


}