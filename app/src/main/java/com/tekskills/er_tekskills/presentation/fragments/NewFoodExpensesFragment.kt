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
import com.tekskills.er_tekskills.databinding.FragmentNewFoodExpensesBinding
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.view.spinner.SearchableSpinner
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.RestApiStatus
import kotlin.collections.ArrayList

class NewFoodExpensesFragment : Fragment() {
    private lateinit var binding: FragmentNewFoodExpensesBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_food_expenses, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        navController = findNavController()

//        viewModel.getClientNameList()

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

        binding.btnSave.setOnClickListener {
            if (isValidate()) {
                viewModel.addProject(
                    binding.edtFoodAmt.text.toString(),
                    binding.edtFoodComment.text.toString(),
                    "","",""
                )
                Toast.makeText(requireActivity(), "Added", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun isValidate(): Boolean =
        validateFoodAmount() && validateFoodComment()

    /**
     * field must not be empty
     */
    private fun validateFoodAmount(): Boolean {
        if (binding.edtFoodAmt.text.toString().trim().isEmpty()) {
            binding.edtFoodAmt.error = "Required Field!"
            binding.edtFoodAmt.requestFocus()
            return false
        } else {
            binding.edtFoodAmt.error = null
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateFoodComment(): Boolean {
        if (binding.edtFoodComment.text.toString().trim().isEmpty()) {
            binding.edtFoodComment.error = "Required Field!"
            binding.edtFoodComment.requestFocus()
            return false
        } else {
            binding.edtFoodComment.error = null
        }
        return true
    }

}