package com.tekskills.er_tekskills.presentation.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
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
import com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER
import com.google.android.gms.location.LocationResult
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.AddLocationCoordinates
import com.tekskills.er_tekskills.data.model.LocationResponse
import com.tekskills.er_tekskills.databinding.ActivityMainBinding
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.NetworkObserver
import com.tekskills.er_tekskills.utils.RestApiStatus
import com.tekskills.er_tekskills.utils.SmartDialog
import com.tekskills.er_tekskills.utils.SmartDialogBuilder
import com.tekskills.er_tekskills.utils.SmartDialogClickListener
import com.tekskills.er_tekskills.utils.SuccessResource
import com.tekskills.er_tekskills.utils.geoLocation.NotificationWorker
import com.tekskills.er_tekskills.utils.location.executeUnderLocationPermission
import com.tekskills.er_tekskills.utils.location.workers.LocationWorker
import com.tekskills.geolocator.geofencer.Geofencer
import com.tekskills.geolocator.geofencer.models.Geofence
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    val viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navViewInit()
        Geofencer(this)
            .removeAll(){

            }
        viewModel.getEmployeeMe()
        observerData()
        updateWorkManager()
    }

    private fun updateWorkManager() {
        executeUnderLocationPermission(
            this,
        ) {
            createWorkManager(context = this)
        }
    }

    private fun observerData() {
        LocationLiveData.locationData.observe(this, Observer { locationResult ->
            // Handle the location data here
            locationResult?.let { data ->

                val userCoordinates = AddLocationCoordinates(
                    longitude = data.lastLocation?.longitude.toString(),
                    lattitude = data.lastLocation?.latitude.toString()
                )

                Timber.d("onViewCreated: print response " + userCoordinates.toString())

                viewModel.addUserCoordinates(userCoordinates)
            }
        })

        viewModel.resEmployeeMe.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    if (it.data != null)
                        it.data.let { res ->
                            viewModel.saveUserEmployeeID(res.securityUser.employeeMaster.roleId.toString())
                            var geofence = Geofence()

                            geofence.title = "Reached Home"
//                            geofence.message = res.userAddress.lineOne
                            res.userAddress?.let { address->
                                geofence.message = with(address) {
                                    "$lineOne, $lineTwo, $city, $state, $country, $zpiCode"
                                }
                                val coordinates = address.coOrdinates.split(",")
                                geofence.latitude = coordinates[0].toDouble()
                                geofence.longitude = coordinates[1].toDouble()
                                geofence.radius = 30.0
                                geofence.transitionType =
                                    GEOFENCE_TRANSITION_ENTER
                                geofence.locType = "User"
//                                requestLocationPermission {
//                                    if (it.granted) {
                                        Geofencer(this)
                                            .addGeofenceWorker(geofence, NotificationWorker::class.java) {
//                                            binding?.container?.isGone = true
//                                            showGeofences()
                                            }
//                                    }
//                                }
                            }
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

        viewModel.networkLiveData.observe(this, Observer {
            observeUserNetworkConnection(it)
        })

        NetworkObserver.getNetworkLiveData(applicationContext)
            .observe(this, Observer { isConnected ->
                viewModel._networkLiveData.value = isConnected
            })
    }

    private fun navViewInit() {
        setSupportActionBar(binding.appBarMain.toolbar)

        val navController = findNavController(R.id.nav_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_fragment,
//                R.id.view_opportunity_fragment,
                R.id.new_purpose_meeting,
                R.id.view_meetings,
                R.id.settings_fragment,
                R.id.map_fragment,
                R.id.logout
            ), binding.drawerLayout
        )

        binding.navView.menu.apply {
            findItem(R.id.logout)
                .setOnMenuItemClickListener { menuItem: MenuItem? ->
                    if (viewModel.clearSharedPrefrence()) {
                        val intent = Intent(this@MainActivity, SplashActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    true
                }

            findItem(R.id.home_fragment)
                .setOnMenuItemClickListener { menuItem: MenuItem? ->
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

            findItem(R.id.view_meetings)
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

            findItem(R.id.new_purpose_meeting)
                .setOnMenuItemClickListener { menuItem: MenuItem? ->
                    findNavController(R.id.nav_fragment).navigate(R.id.new_purpose_meeting)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
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