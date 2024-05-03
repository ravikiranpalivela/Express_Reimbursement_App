package com.tekskills.er_tekskills.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.presentation.adapter.TasksAdapter
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.databinding.FragmentOpportunityBinding
import com.tekskills.er_tekskills.utils.RestApiStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class OpportunityListCountFragment : ParentFragment() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding : FragmentOpportunityBinding

    @Inject
    @Named("completed_task_fragment")
    lateinit var adapter : TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_opportunity, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        viewModel.getPendingActionGraph()
        viewModel.getAssignedProjectList()
        viewModel.getProjectList()

        viewModel.resPendingActionGraphList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        binding.tvClientCount.text = it.data.size.toString()
                    } else {
                        Snackbar.make(
                            binding.root,
                            "No Data Found",
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

        viewModel.resProjectList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        binding.tvProjectsCount.text = it.data.size.toString()
                    } else {
                        Snackbar.make(
                            binding.root,
                            "No Data Found",
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

        viewModel.resAssignedProjectList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        binding.tvAssignProjectsCount.text = it.data.size.toString()
                    } else {
                        Snackbar.make(
                            binding.root,
                            "No Data Found",
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

        binding.apply {
            ivAddProjects.setOnClickListener {
                val action = OpportunityFragmentDirections.actionOpportunityFragmentToNewProjectFragment()
                it.findNavController().navigate(action)
            }

            ivAddClients.setOnClickListener {
                val action = OpportunityFragmentDirections.actionOpportunityFragmentToNewClientFragment()
                it.findNavController().navigate(action)
            }

            ivAddAssignProject.setOnClickListener {
                val action = OpportunityFragmentDirections.actionOpportunityFragmentToNewAssignedProjectFragment()
                it.findNavController().navigate(action)
            }
        }

//        adapter.setOnTaskStatusChangedListener {
//            updateTaskStatus(viewModel, it)
//        }
//        adapter.setOnItemClickListener {
//            editTaskInformation(it)
//        }
//        initRecyclerView()
//        viewModel.getCompletedTask().observe(viewLifecycleOwner, Observer {
//            if(it.isEmpty()) binding.taskAnimationView.visibility = View.VISIBLE
//            else binding.taskAnimationView.visibility = View.GONE
//            adapter.differ.submitList(it)
//        })
    }

//    private fun initRecyclerView() {
//        binding.recyclerView.adapter = adapter
//        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
//        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
//    }
//
//    private fun editTaskInformation(taskCategoryInfo: TaskCategoryInfo) {
//        val action = CompletedTasksFragmentDirections.actionCompletedTasksFragmentToNewTaskFragment(taskCategoryInfo)
//        findNavController().navigate(action)
//    }

    private val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            val position = viewHolder.adapterPosition
            val taskInfo = adapter.differ.currentList[position]?.taskInfo
            val categoryInfo = adapter.differ.currentList[position]?.categoryInfo?.get(0)
            if (taskInfo != null && categoryInfo!= null) {
                deleteTask(viewModel, taskInfo, categoryInfo)
                Snackbar.make(binding.root,"Deleted Successfully",Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo") {
                            viewModel.insertTaskAndCategory(taskInfo, categoryInfo)
                        }
                        show()
                    }
            }
        }
    }

}