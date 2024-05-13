package com.tekskills.er_tekskills.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.databinding.FragmentMainBinding
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.adapter.ViewMeetingPurposeAdapter
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.RestApiStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeFragment : ParentFragment() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentMainBinding

    @Inject
    @Named("view_main_meetings")
    lateinit var adapter: ViewMeetingPurposeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        binding.llMeetings.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToViewMeetingPurposeFragment()
            binding.root.findNavController().navigate(action)
        }
        adapter.setOnTaskStatusChangedListener {

        }
        adapter.setOnItemClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToViewMeetingDetailsFragment(
                    it.id.toString()
                )
            binding.root.findNavController().navigate(action)
//            editTaskInformation(it)
        }
        adapter.setOnEditItemClickListener {
//            val action =
//                HomeFragmentDirections.actionViewOpportunityFragmentToEditAssignedProjectFragment()
//            binding.root.findNavController().navigate(action)
//            editTaskInformation(it)
        }

        viewModel.getMeetingPurpose("")

        initRecyclerView()

        viewModel.resMeetingPurposeList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {

                        it.data.let { list ->
                            if (list.content.isEmpty()) binding.avMeetings.visibility = View.VISIBLE
                            else binding.avMeetings.visibility = View.GONE
                            adapter.differ.submitList(list.content)
                        }

                    } else {
                        Snackbar.make(
                            binding.root, "No Data Found", Snackbar.LENGTH_SHORT
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

//        binding.fab.setOnClickListener {
//            val action =
//                HomeFragmentDirections.actionViewOpportunityFragmentToNewAssignedProjectFragment()
//            it.findNavController().navigate(action)
//        }
    }

    private fun initRecyclerView() {
        binding.rvMeetings.adapter = adapter
        binding.rvMeetings.layoutManager = LinearLayoutManager(requireContext())
    }
}
