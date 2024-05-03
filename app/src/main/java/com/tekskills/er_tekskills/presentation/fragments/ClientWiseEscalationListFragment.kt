package com.tekskills.er_tekskills.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.TaskCategoryInfo
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.databinding.FragmentClientWiseEscalationBinding
import com.tekskills.er_tekskills.presentation.adapter.ClientWiseEscalationAdapter
import com.tekskills.er_tekskills.utils.RestApiStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ClientWiseEscalationListFragment : ParentFragment() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentClientWiseEscalationBinding

    private val args: ClientWiseEscalationListFragmentArgs by navArgs()

    private var clientID: String = ""

    @Inject
    @Named("client_wise_escalation_fragment")
    lateinit var adapter: ClientWiseEscalationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_client_wise_escalation, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        clientID = args.clientID

        adapter.setOnTaskStatusChangedListener {

        }

        adapter.setOnItemClickListener {
            val action =
                ClientWiseEscalationListFragmentDirections.actionClientWiseEscalationDetailsFragmentToOpportunityDetailsFragment(
                    it.projectId.toString()
                )
            binding.root.findNavController().navigate(action)
//            editTaskInformation(it)
        }
        adapter.setOnEditItemClickListener {
//            val action =
//                ViewOpportunityFragmentDirections.actionViewOpportunityFragmentToEditAssignedProjectFragment()
//            binding.root.findNavController().navigate(action)
//            editTaskInformation(it)
        }

        viewModel.getClientEscalationGraphByID(clientID)

        initRecyclerView()
//        viewModel.getCompletedTask()
        viewModel.resEscalationGraphByIDList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        it.data.let { list ->
                            if (list.isEmpty()) binding.avClientWiseEscalation.visibility = View.VISIBLE
                            else binding.avClientWiseEscalation.visibility = View.GONE
                            adapter.differ.submitList(list)
                        }
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
    }

    private fun initRecyclerView() {
        binding.rvClientWiseEscalation.adapter = adapter
        binding.rvClientWiseEscalation.layoutManager = LinearLayoutManager(requireContext())
//        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
//        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun editTaskInformation(taskCategoryInfo: TaskCategoryInfo) {
        val action = CompletedTasksFragmentDirections.actionCompletedTasksFragmentToNewTaskFragment(
            taskCategoryInfo
        )
        findNavController().navigate(action)
    }

//    private val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
//        ItemTouchHelper.SimpleCallback(
//            0,
//            ItemTouchHelper.LEFT
//        ) {
//        override fun onMove(
//            recyclerView: RecyclerView,
//            viewHolder: RecyclerView.ViewHolder,
//            target: RecyclerView.ViewHolder
//        ): Boolean {
//            return false
//        }
//
//        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
//            val position = viewHolder.adapterPosition
//            val taskInfo = adapter.differ.currentList[position]?.taskInfo
//            val categoryInfo = adapter.differ.currentList[position]?.categoryInfo?.get(0)
//            if (taskInfo != null && categoryInfo!= null) {
//                deleteTask(viewModel, taskInfo, categoryInfo)
//                Snackbar.make(binding.root,"Deleted Successfully",Snackbar.LENGTH_LONG)
//                    .apply {
//                        setAction("Undo") {
//                            viewModel.insertTaskAndCategory(taskInfo, categoryInfo)
//                        }
//                        show()
//                    }
//            }
//        }
//    }

}