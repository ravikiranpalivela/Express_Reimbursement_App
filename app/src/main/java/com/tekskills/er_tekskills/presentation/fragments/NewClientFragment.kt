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
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.R
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.databinding.FragmentNewClientBinding
import com.tekskills.er_tekskills.utils.RestApiStatus

class NewClientFragment : Fragment() {
    private lateinit var binding: FragmentNewClientBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_client, container, false)
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
                        it.data.let {
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
                viewModel.addClient(
                    binding.edtClientName.text.toString(),
                    binding.edtClientContactName.text.toString(),
                    binding.edtClientContactPosition.text.toString()
                )
//                Toast.makeText(requireActivity(), "validated", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun isValidate(): Boolean =
        validateClientName() && validateClientContact() && validateClientPosition()

    /**
     * field must not be empty
     */
    private fun validateClientName(): Boolean {
        if (binding.edtClientName.text.toString().trim().isEmpty()) {
            binding.edtlClientName.error = "Required Field!"
            binding.edtClientName.requestFocus()
            return false
        } else {
            binding.edtlClientName.isErrorEnabled = false
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateClientContact(): Boolean {
        if (binding.edtClientContactName.text.toString().trim().isEmpty()) {
            binding.edtlClientContactName.error = "Required Field!"
            binding.edtClientContactName.requestFocus()
            return false
        } else {
            binding.edtlClientContactName.isErrorEnabled = false
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateClientPosition(): Boolean {
        if (binding.edtClientContactPosition.text.toString().trim().isEmpty()) {
            binding.edtlClientContactPosition.error = "Required Field!"
            binding.edtClientContactPosition.requestFocus()
            return false
        } else {
            binding.edtlClientContactPosition.isErrorEnabled = false
        }
        return true
    }
}