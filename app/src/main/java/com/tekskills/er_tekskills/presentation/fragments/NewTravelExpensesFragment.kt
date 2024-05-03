package com.tekskills.er_tekskills.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.ClientNamesResponseItem
import com.tekskills.er_tekskills.databinding.FragmentNewTravelExpensesBinding
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.view.spinner.SearchableSpinner
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.RestApiStatus
import kotlin.collections.ArrayList

class NewTravelExpensesFragment : Fragment() {
    private lateinit var binding: FragmentNewTravelExpensesBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: MainActivityViewModel

    private var mClientNames = ArrayList<ClientNamesResponseItem>()
    private var selectClientPos = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_travel_expenses, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        navController = findNavController()

        viewModel.getClientNameList()

////        val spinnerData: ArrayList<String> = arrayListOf(mClientNames)
//        val spinnerData = arrayListOf("Kotlin", "Java", "Python", "Php", "Swift")
//        binding.sJourneyTypes.buildCheckedSpinner(spinnerData) { selectedPositionList, displayString ->
////            tvSelectedPosition.text = "Selected position:  $selectedPositionList"
////            tvDispString.text = "Display String:  $displayString"
//        }

        binding.sJourneyTypes.setOnItemSelectListener(object : SearchableSpinner.SearchableItemListener {
            override fun onItemSelected(view: View?, position: Int) {
                selectClientPos = mClientNames[position].clientId
            }

            override fun onSelectionClear() {

            }
        })

        viewModel.resClientNameList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null)
                        it.data.let { res ->
                            mClientNames = res
                            binding.sJourneyTypes.setItems(res)
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
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

        viewModel.resNewClientResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null)
                        it.data.let { res ->
                            val intent = Intent(requireActivity(), MainActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
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
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

        binding.fab.setOnClickListener {
            if (isValidate()) {
                viewModel.addProject(
                    binding.edtFromLoc.text.toString(),
                    selectClientPos.toString(),
                    binding.edtToLoc.text.toString(),
                    binding.dateTravelDate.text.toString(),
                    if(binding.chkRoundTrip.isChecked) "Active" else "InActive"
                )
                Toast.makeText(requireActivity(), "Added", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun isValidate(): Boolean =
        validateProjectName() && validateOpportunityType() && validateOpportunityDesc() && validateClientName()

    /**
     * field must not be empty
     */
    private fun validateProjectName(): Boolean {
        if (binding.edtFromLoc.text.toString().trim().isEmpty()) {
            binding.edtlFromLoc.error = "Required Field!"
            binding.edtFromLoc.requestFocus()
            return false
        } else {
            binding.edtlFromLoc.isErrorEnabled = false
        }
        return true
    }

    private fun validateClientName(): Boolean {
        if (selectClientPos==0) {

            return false
        } else {

        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateOpportunityType(): Boolean {
        if (binding.edtToLoc.text.toString().trim().isEmpty()) {
            binding.edtlToLoc.error = "Required Field!"
            binding.edtToLoc.requestFocus()
            return false
        } else {
            binding.edtlToLoc.isErrorEnabled = false
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateOpportunityDesc(): Boolean {
        if (binding.dateTravelDate.text.toString().trim().isEmpty()) {
            binding.dateTravelDate.error = "Required Field!"
            binding.dateTravelDate.requestFocus()
            return false
        } else {
            binding.dateTravelDate.error = ""
        }
        return true
    }

}