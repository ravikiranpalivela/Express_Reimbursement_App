package com.tekskills.er_tekskills.presentation.fragments

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
import com.tekskills.er_tekskills.data.util.DateToString
import com.tekskills.er_tekskills.databinding.FragmentNewEscalationBinding
import com.tekskills.er_tekskills.utils.RestApiStatus
import java.util.*

class NewEscalationFragment : Fragment() {
    private lateinit var binding: FragmentNewEscalationBinding
    private lateinit var navController: NavController
    private val args: NewEscalationFragmentArgs by navArgs()
    private lateinit var viewModel: MainActivityViewModel

    var raisedDate: Date = Date(Constants.MAX_TIMESTAMP)
    var resolvedDate: Date = Date(Constants.MAX_TIMESTAMP)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_escalation, container, false)
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
                            requireActivity().onBackPressed()
//                            val intent = Intent(requireActivity(), MainActivity::class.java)
//                            startActivity(intent)
//                            requireActivity().finish()
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

        binding.dateEscalatedRaisedDate.setOnClickListener { showDateTimePickerRaisedDate() }

        binding.dateEscalatedResolvedDate.setOnClickListener { showDateTimePickerResolvedDate() }

        binding.fab.setOnClickListener {
            if (isValidate()) {
                viewModel.addClientEscalation(
                    args.opportunityArg!!.projectId.toString(),
                    args.opportunityArg!!.clientDetails.clientId.toString(),
                    args.opportunityArg!!.clientEscalations,
                    binding.edtEscalatedDesc.text.toString(),
                    DateToString.convertDateToString(raisedDate),
                    DateToString.convertDateToString(resolvedDate),
                    "Active"
                )
                Toast.makeText(requireActivity(), "validated", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showDateTimePickerRaisedDate() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
//        val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            raisedDate = calendar.time
            binding.dateEscalatedRaisedDate.text = DateToString.convertDateToStringDateWise(raisedDate)
//            timePicker.show(childFragmentManager, "TAG")
        }

//        timePicker.addOnPositiveButtonClickListener {
//            val cal = Calendar.getInstance()
//            cal.time = raisedDate
//            cal.set(Calendar.HOUR_OF_DAY, timePicker.hour)
//            cal.set(Calendar.MINUTE, timePicker.minute)
//            cal.set(Calendar.SECOND, 5)
//            raisedDate = cal.time
//            binding.dateEscalatedRaisedDate.text = DateToString.convertDateToStringDateWise(raisedDate)
//        }
        datePicker.show(childFragmentManager, "TAG")
    }


    private fun showDateTimePickerResolvedDate() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
//        val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            resolvedDate = calendar.time
            binding.dateEscalatedResolvedDate.text = DateToString.convertDateToStringDateWise(resolvedDate)
//            timePicker.show(childFragmentManager, "TAG")
        }

//        timePicker.addOnPositiveButtonClickListener {
//            val cal = Calendar.getInstance()
//            cal.time = resolvedDate
//            cal.set(Calendar.HOUR_OF_DAY, timePicker.hour)
//            cal.set(Calendar.MINUTE, timePicker.minute)
//            cal.set(Calendar.SECOND, 5)
//            resolvedDate = cal.time
//            binding.dateEscalatedResolvedDate.text = DateToString.convertDateToStringDateWise(resolvedDate)
//        }
        datePicker.show(childFragmentManager, "TAG")
    }

    private fun isValidate(): Boolean =
        validateRaisedDate() && validateResolvedDate() && validateEscalatedDesc()


    /**
     * field must not be empty
     */
    private fun validateRaisedDate(): Boolean {
        if (binding.dateEscalatedRaisedDate.text.toString() == getString(R.string.edt_escalated_raised_date)) {
            binding.dateEscalatedRaisedDate.error = "Required Field!"
            binding.dateEscalatedRaisedDate.requestFocus()
            return false
        } else {
//            binding.dateEscalatedRaisedDate.isErrorEnabled = false
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateResolvedDate(): Boolean {
        if (binding.dateEscalatedResolvedDate.text.toString() == getString(R.string.edt_escalated_resolved_date)) {
            binding.dateEscalatedResolvedDate.error = "Required Field!"
            binding.dateEscalatedResolvedDate.requestFocus()
            return false
        } else {
//            binding.dateEscalatedResolvedDate.isErrorEnabled = false
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateEscalatedDesc(): Boolean {
        if (binding.edtEscalatedDesc.text.toString().trim().isEmpty()) {
            binding.edtlEscalatedDesc.error = "Required Field!"
            binding.edtEscalatedDesc.requestFocus()
            return false
        } else {
            binding.edtlEscalatedDesc.isErrorEnabled = false
        }
        return true
    }

}