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
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.util.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.tekskills.er_tekskills.data.util.DateToString
import com.tekskills.er_tekskills.databinding.FragmentNewActionItemBinding
import com.tekskills.er_tekskills.utils.RestApiStatus
import java.util.*

class EditActionItemFragment : Fragment() {
    private lateinit var binding: FragmentNewActionItemBinding
    private lateinit var navController: NavController
    private val args: NewActionItemFragmentArgs by navArgs()
    private lateinit var viewModel: MainActivityViewModel

    var completionDate: Date = Date(Constants.MAX_TIMESTAMP)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_action_item, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        navController = findNavController()

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

        binding.dateActionItemDate.setOnClickListener { showDateTimePicker() }


        binding.fab.setOnClickListener {
            if (isValidate()) {
                viewModel.addActionItemOpportunity(
                    args.opportunityArg!!.projectId.toString(),
                    args.opportunityArg!!.projectId.toString(),
                    args.opportunityArg!!.AssignId.toString(),
                    binding.edtExpectedInfoFromClients.text.toString(),
                    DateToString.convertDateToString(completionDate),
                    binding.edtExpectedInfoFromTekskills.text.toString(),
                    binding.edtTekskillsActionItems.text.toString(),
                    "Active"
                )
                Toast.makeText(requireActivity(), "validated", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showDateTimePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            completionDate = calendar.time
            binding.dateActionItemDate.text = DateToString.convertDateToString(completionDate)
            timePicker.show(childFragmentManager, "TAG")
        }

        timePicker.addOnPositiveButtonClickListener {
            val cal = Calendar.getInstance()
            cal.time = completionDate
            cal.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            cal.set(Calendar.MINUTE, timePicker.minute)
            cal.set(Calendar.SECOND, 5)
            completionDate = cal.time
            binding.dateActionItemDate.text = DateToString.convertDateToString(completionDate)
        }
        datePicker.show(childFragmentManager, "TAG")
    }

    private fun isValidate(): Boolean =
        validateClientName() && validateClientContact() && validateClientPosition()


    /**
     * field must not be empty
     */
    private fun validateClientName(): Boolean {
        if (binding.edtExpectedInfoFromClients.text.toString().trim().isEmpty()) {
            binding.edtlExpectedInfoFromClients.error = "Required Field!"
            binding.edtExpectedInfoFromClients.requestFocus()
            return false
        } else {
            binding.edtlExpectedInfoFromClients.isErrorEnabled = false
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateClientContact(): Boolean {
        if (binding.edtExpectedInfoFromTekskills.text.toString().trim().isEmpty()) {
            binding.edtlExpectedInfoFromTekskills.error = "Required Field!"
            binding.edtExpectedInfoFromTekskills.requestFocus()
            return false
        } else {
            binding.edtlExpectedInfoFromTekskills.isErrorEnabled = false
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateClientPosition(): Boolean {
        if (binding.edtTekskillsActionItems.text.toString().trim().isEmpty()) {
            binding.edtlTekskillsActionItems.error = "Required Field!"
            binding.edtTekskillsActionItems.requestFocus()
            return false
        } else {
            binding.edtlTekskillsActionItems.isErrorEnabled = false
        }
        return true
    }

}