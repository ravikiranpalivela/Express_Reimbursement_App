package com.tekskills.er_tekskills.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.util.DateToString
import com.tekskills.er_tekskills.databinding.FragmentClientEscalationDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientWiseEscalationDetailsFragment : ParentFragment() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentClientEscalationDetailsBinding
    private val args: ClientWiseEscalationDetailsFragmentArgs by navArgs()

    private var opportunityID: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_client_escalation_details, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        opportunityID = args.escalationID

        binding.taskCategoryInfo = args.escalationArg

        args.escalationArg!!.escRaiseDate?.let {
            binding.escalationRaisedDate.text = DateToString.convertDateStringToCustomFormat(
                args.escalationArg!!.escRaiseDate
            )
        }

        binding.ivEditEscalation.setOnClickListener {
//            val action =
//                ClientEscalationDetailsFragmentDirections.actionEscalationDetailsFragmentToEditEscalation(
//                    binding.taskCategoryInfo!!,
//                    opportunityID
//                )
//            binding.root.findNavController().navigate(action)
        }

//        viewModel.resActionItemBYID.observe(
//            viewLifecycleOwner,
//            androidx.lifecycle.Observer {
//                when (it.status) {
//                    RestApiStatus.SUCCESS -> {
//                        binding.progress.visibility = View.GONE
//                        if (it.data != null) {
//                            it.data.let { mom ->
//                                binding.taskCategoryInfo = mom
//                            }
//                        } else {
//                            Snackbar.make(
//                                binding.root,
//                                "No Data Found",
//                                Snackbar.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//
//                    RestApiStatus.LOADING -> {
//                        binding.progress.visibility = View.VISIBLE
//                    }
//
//                    RestApiStatus.ERROR -> {
//                        binding.progress.visibility = View.GONE
//                        Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
//                            .show()
//                    }
//
//                    else -> {
//                        binding.progress.visibility = View.GONE
//                        Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//            })
    }

}