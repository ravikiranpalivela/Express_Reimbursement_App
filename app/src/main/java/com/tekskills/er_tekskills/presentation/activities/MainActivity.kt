package com.tekskills.er_tekskills.presentation.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
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
                            viewModel.saveUserEmployeeID(res.employeeMaster.id.toString())

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

        val navController = findNavController(R.id.nav_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_fragment,
                R.id.base_fragment,
                R.id.opportunity,
                R.id.view_opportunity_fragment,
                R.id.new_purpose_meeting,
                R.id.view_meetings,
                R.id.new_client,
                R.id.settings_fragment,
                R.id.completed_tasks_fragment,
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}