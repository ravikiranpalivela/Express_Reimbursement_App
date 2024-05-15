package com.tekskills.er_tekskills.presentation.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.databinding.ActivityMainBinding
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.NetworkObserver
import com.tekskills.er_tekskills.utils.RestApiStatus
import com.tekskills.er_tekskills.utils.SmartDialog
import com.tekskills.er_tekskills.utils.SmartDialogBuilder
import com.tekskills.er_tekskills.utils.SmartDialogClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    val viewModel: MainActivityViewModel by viewModels()


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.appBarMain.toolbar)
        viewModel.getEmployeeMe()

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
                                    val intent = Intent(this@MainActivity, SplashActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                smartDialog!!.dismiss()
                            }
                        })
                        .setNegativeButton("Cancel", object : SmartDialogClickListener {
                            override fun onClick(smartDialog: SmartDialog?) {
                                if (viewModel.clearSharedPrefrence()) {
                                    val intent = Intent(this@MainActivity, SplashActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                smartDialog!!.dismiss()
                            }
                        })
                        .setNeutralButton("Cancel", object : SmartDialogClickListener {
                            override fun onClick(smartDialog: SmartDialog?) {
                                if (viewModel.clearSharedPrefrence()) {
                                    val intent = Intent(this@MainActivity, SplashActivity::class.java)
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
                R.id.view_opportunity_fragment,
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
}