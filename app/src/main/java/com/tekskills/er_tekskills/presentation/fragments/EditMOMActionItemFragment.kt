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
import com.tekskills.er_tekskills.databinding.FragmentNewMomActionItemBinding
import com.tekskills.er_tekskills.utils.RestApiStatus
import java.util.*

class EditMOMActionItemFragment : Fragment() {
    private lateinit var binding: FragmentNewMomActionItemBinding
    private lateinit var navController: NavController
    private val args: EditMOMActionItemFragmentArgs by navArgs()

    private lateinit var viewModel: MainActivityViewModel

    var meetingDate: Date = Date(Constants.MAX_TIMESTAMP)
    var meetingTime: Date = Date(Constants.MAX_TIMESTAMP)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_new_mom_action_item,
            container,
            false
        )
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

        binding.dateMeetingDate.setOnClickListener {
            showMeetingDatePicker()
        }

        binding.dateMeetingTime.setOnClickListener {
            showMeetingDateTimePicker()
        }

        binding.fab.setOnClickListener {
            if (isValidate()) {
                viewModel.addMOMActionItem(
                    args.momActionItemArg!!.projectId.toString(),args.momActionItemArg!!.clientId.toString(),
                    args.momActionItemArg!!.assignedId.toString(),
                    binding.edtMeetingTitle.text.toString(),DateToString.convertDateToString(meetingDate),
                    DateToString.convertDateToString(meetingTime),
                    binding.edtMeetingNotes.text.toString()
                )
                Toast.makeText(requireActivity(), "validated", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidate(): Boolean =
        validateMeetingTitle() && validateMeetingNotes() && validateMeetingTime() && validateMeetingDate()
//                && validateClientPosition()

    /**
     * field must not be empty
     */
    private fun validateMeetingTitle(): Boolean {
        if (binding.edtMeetingTitle.text.toString().trim().isEmpty()) {
            binding.edtlMeetingTitle.error = "Required Field!"
            binding.edtMeetingTitle.requestFocus()
            return false
        } else {
            binding.edtlMeetingTitle.isErrorEnabled = false
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateMeetingNotes(): Boolean {
        if (binding.edtMeetingNotes.text.toString().trim().isEmpty()) {
            binding.edtlMeetingNotes.error = "Required Field!"
            binding.edtMeetingNotes.requestFocus()
            return false
        } else {
            binding.edtlMeetingNotes.isErrorEnabled = false
        }
        return true
    }

    private fun validateMeetingDate(): Boolean {
        if (binding.dateMeetingDate.text.toString() == getString(R.string.meeting_date)) {
            binding.dateMeetingDate.error = "Required Field!"
            binding.dateMeetingDate.requestFocus()
            return false
        } else {
//            binding.dateEscalatedMeetingDate.isErrorEnabled = false
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateMeetingTime(): Boolean {
        if (binding.dateMeetingTime.text.toString() == getString(R.string.meeting_time)) {
            binding.dateMeetingTime.error = "Required Field!"
            binding.dateMeetingTime.requestFocus()
            return false
        } else {
//            binding.dateEscalatedMeetingTime.isErrorEnabled = false
        }
        return true
    }

    private fun showMeetingDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            meetingDate = calendar.time
            binding.dateMeetingDate.text = DateToString.convertDateToString(meetingDate)
        }
        datePicker.show(childFragmentManager, "TAG")
    }

    private fun showMeetingDateTimePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            meetingTime = calendar.time
            binding.dateMeetingTime.text = DateToString.convertDateToString(meetingTime)
            timePicker.show(childFragmentManager, "TAG")
        }

        timePicker.addOnPositiveButtonClickListener {
            val cal = Calendar.getInstance()
            cal.time = meetingTime
            cal.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            cal.set(Calendar.MINUTE, timePicker.minute)
            cal.set(Calendar.SECOND, 5)
            meetingTime = cal.time
            binding.dateMeetingTime.text = DateToString.convertDateToString(meetingTime)
        }
        datePicker.show(childFragmentManager, "TAG")
    }

    /**
     * field must not be empty
     */
//    private fun validateClientPosition(): Boolean {
//        if (binding.edtClientContactPosition.text.toString().trim().isEmpty()) {
//            binding.edtlClientContactPosition.error = "Required Field!"
//            binding.edtClientContactPosition.requestFocus()
//            return false
//        } else {
//            binding.edtlClientContactPosition.isErrorEnabled = false
//        }
//        return true
//    }

}