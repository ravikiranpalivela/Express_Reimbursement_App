package com.tekskills.er_tekskills.presentation.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.databinding.FragmentSettingsBinding
import com.tekskills.er_tekskills.presentation.activities.LocationPickerActivity
import com.tekskills.er_tekskills.utils.Common.Companion.THEME
import com.tekskills.er_tekskills.utils.MapUtility
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    val ADDRESS_PICKER_REQUEST: Int = 1020

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        binding.apply {
            lowTasks.isChecked = sharedPreferences.getBoolean("0", true)
            midTasks.isChecked = sharedPreferences.getBoolean("1", true)
            highTasks.isChecked = sharedPreferences.getBoolean("2", true)
            darkTheme.isChecked = sharedPreferences.getBoolean(THEME, false)
        }

//        binding.rateApp.setOnClickListener {
//            val manager = ReviewManagerFactory.create(requireContext())
//            val request = manager.requestReviewFlow()
//            request.addOnCompleteListener {
//                if(it.isSuccessful){
//                    val reviewInfo = it.result
//                    val flow = manager.launchReviewFlow(activity!!, reviewInfo!!)
//                    flow.addOnCompleteListener {  }
//                }else {
//                    Snackbar.make(binding.root, "Some error occurred!", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
//                }
//            }
//        }

        binding.darkTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            editor.putBoolean(THEME, isChecked)
            editor.apply()
        }

        binding.lowTasks.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("0", isChecked)
            editor.apply()
        }

        binding.midTasks.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("1", isChecked)
            editor.apply()
        }

        binding.highTasks.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("2", isChecked)
            editor.apply()
        }


        binding.btnAddressPicker.setOnClickListener {
            //                            val intent = Intent(requireActivity(), MainActivity::class.java)
//                            startActivity(intent)
//                            requireActivity().finish()
            val intent = Intent(requireActivity(), LocationPickerActivity::class.java)
            startActivityForResult(
                intent, ADDRESS_PICKER_REQUEST
            )
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data?.getStringExtra(MapUtility.ADDRESS) != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    val currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0)
                    val currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0)
                    val completeAddress = data.getBundleExtra("fullAddress")

                    /* data in completeAddress bundle
                    "fulladdress"
                    "city"
                    "state"
                    "postalcode"
                    "country"
                    "addressline1"
                    "addressline2"
                     */
                    binding.txtAddress.setText(
                        StringBuilder().append("addressline2: ").append(
                            completeAddress!!.getString("addressline2")
                        ).append("\ncity: ").append(
                            completeAddress.getString("city")
                        ).append("\npostalcode: ").append(completeAddress.getString("postalcode"))
                            .append("\nstate: ").append(
                                completeAddress.getString("state")
                            ).toString()
                    )

                    binding.txtLatLong.setText(
                        StringBuilder().append("Lat:").append(currentLatitude).append("  Long:")
                            .append(currentLongitude).toString()
                    )
                }
            } catch (ex: Exception) {
                Log.e("TAG","exception at activity Result ${ex}")
            }
        }
    }
}