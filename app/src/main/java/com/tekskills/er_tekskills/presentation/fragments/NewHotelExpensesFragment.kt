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
import com.tekskills.er_tekskills.databinding.FragmentNewHotelExpensesBinding
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.view.spinner.SearchableSpinner
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.RestApiStatus
import kotlin.collections.ArrayList

class NewHotelExpensesFragment : Fragment() {
    private lateinit var binding: FragmentNewHotelExpensesBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_hotel_expenses, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        navController = findNavController()

        viewModel.getClientNameList()
        

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
                    binding.edtHotelExpenses.text.toString(),
                    binding.edtHotelLoc.text.toString(),
                    binding.dateBookDate.text.toString(),
                    "",""
                )
                Toast.makeText(requireActivity(), "Added", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun isValidate(): Boolean =
        validateProjectName() && validateOpportunityType() && validateOpportunityDesc()

    /**
     * field must not be empty
     */
    private fun validateProjectName(): Boolean {
        if (binding.edtHotelExpenses.text.toString().trim().isEmpty()) {
            binding.edtlHotelExpenses.error = "Required Field!"
            binding.edtHotelExpenses.requestFocus()
            return false
        } else {
            binding.edtlHotelExpenses.isErrorEnabled = false
        }
        return true
    }


    /**
     * field must not be empty
     */
    private fun validateOpportunityType(): Boolean {
        if (binding.edtHotelLoc.text.toString().trim().isEmpty()) {
            binding.edtlHotelLoc.error = "Required Field!"
            binding.edtHotelLoc.requestFocus()
            return false
        } else {
            binding.edtlHotelLoc.isErrorEnabled = false
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateOpportunityDesc(): Boolean {
        if (binding.dateBookDate.text.toString().trim().isEmpty()) {
            binding.dateBookDate.error = "Required Field!"
            binding.dateBookDate.requestFocus()
            return false
        } else {
            binding.dateBookDate.error = ""
        }
        return true
    }

}