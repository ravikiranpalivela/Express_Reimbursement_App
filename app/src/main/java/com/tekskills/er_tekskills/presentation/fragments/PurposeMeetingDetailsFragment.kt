package com.tekskills.er_tekskills.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.R
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.databinding.FragmentMeetingPurposeDetailsBinding
import com.tekskills.er_tekskills.presentation.adapter.ActionItemsOpportunitysAdapter
import com.tekskills.er_tekskills.presentation.adapter.ClientEscalationOpportunitysAdapter
import com.tekskills.er_tekskills.presentation.adapter.CommentsOpportunitysAdapter
import com.tekskills.er_tekskills.presentation.adapter.MomActionItemsOpportunitysAdapter
import com.tekskills.er_tekskills.utils.RestApiStatus
import com.tekskills.er_tekskills.utils.createDummyData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class PurposeMeetingDetailsFragment : ParentFragment() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentMeetingPurposeDetailsBinding
    private val args: OpportunityDetailsFragmentArgs by navArgs()

    private var opportunityID: String = ""

    @Inject
    @Named("opportunity_details_fragment")
    lateinit var adapter: ClientEscalationOpportunitysAdapter

    @Inject
    @Named("action_item_opportunity_details_fragment")
    lateinit var actionItemListAdapter: ActionItemsOpportunitysAdapter


    @Inject
    @Named("mom_action_item_opportunity_details_fragment")
    lateinit var momActionItemListAdapter: MomActionItemsOpportunitysAdapter

    @Inject
    @Named("comments_opportunity_details_fragment")
    lateinit var commentsItemListAdapter: CommentsOpportunitysAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_meeting_purpose_details, container, false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        opportunityID = args.opportunityID

//        viewModel.getEscalationItemProjectID(opportunityID)
//        viewModel.getCommentProjectID(opportunityID)
//        viewModel.getMOMProjects(opportunityID)
//        viewModel.getActionItemProjectID(opportunityID)
        viewModel.getMeetingPurposeByID(opportunityID)

        binding.ivAddAdvanceAmount.setOnClickListener {
            if (binding.llAddAdvanceAmount.isVisible)
                binding.llAddAdvanceAmount.visibility = GONE
            else
                binding.llAddAdvanceAmount.visibility = VISIBLE
        }
//
        binding.btnAddAdvanceAmount.setOnClickListener {
//            if (isValidate())
//                viewModel.addCommentOpportunity(
//                    binding.taskCategoryInfo!!.id.toString(),
//                    binding.taskCategoryInfo!!.AssignId.toString(),
//                    binding.edtComment.text.toString()
//                )
        }

        adapter.setOnTaskStatusChangedListener {

        }
        adapter.setOnItemClickListener {
//            val action =
//                ViewMeetingPurposeFragmentDirections.actionOpportunityDetailsFragmentToEscalationDetails(
//                    it,
//                    it.escalationDetails.id.toString()
//                )
//            binding.root.findNavController().navigate(action)
        }

        adapter.setOnEditItemClickListener {
//            val action =
//                ViewMeetingPurposeFragmentDirections.actionOpportunityDetailsFragmentToEditEscalation(
//                    it,
//                    it.escalationDetails.id.toString()
//                )
//            binding.root.findNavController().navigate(action)
        }

        actionItemListAdapter.setOnTaskStatusChangedListener {
//            updateTaskStatus(viewModel, it)
        }
        actionItemListAdapter.setOnItemClickListener {
//            val action =
//                ViewMeetingPurposeFragmentDirections.actionOpportunityDetailsFragmentToActionItemDetails(
//                    it,
//                    it.id.toString()
//                )
//            binding.root.findNavController().navigate(action)
        }

        actionItemListAdapter.setOnEditItemClickListener {
//            val action =
//                ViewMeetingPurposeFragmentDirections.actionOpportunityDetailsFragmentToEditActionItem(
//                    it,
//                    it.id.toString()
//                )
//            binding.root.findNavController().navigate(action)
        }

        momActionItemListAdapter.setOnTaskStatusChangedListener {
//            updateTaskStatus(viewModel, it)
        }
        momActionItemListAdapter.setOnItemClickListener {
//            val action =
//                ViewMeetingPurposeFragmentDirections.actionOpportunityDetailsFragmentToMomActionItemDetails(
//                    it,
//                    it.id.toString()
//                )
//            binding.root.findNavController().navigate(action)
        }

        momActionItemListAdapter.setOnEditItemClickListener {
//            val action =
//                ViewMeetingPurposeFragmentDirections.actionOpportunityDetailsFragmentToEditMomActionItem(
//                    it,
//                    it.id.toString()
//                )
//            binding.root.findNavController().navigate(action)
        }

        binding.ivEditPurposeVisit.setOnClickListener {
//            val action =
//                ViewMeetingPurposeFragmentDirections.actionOpportunityDetailsFragmentToNewAssignedProjectFragment()
//            binding.root.findNavController().navigate(action)
        }

        binding.ivAddTravelExpenses.setOnClickListener {
            val action =
                PurposeMeetingDetailsFragmentDirections.actionMeetingDetailsFragmentToNewTravelExpenses(
                )
            binding.root.findNavController().navigate(action)
        }

        binding.ivAddHotelExpenses.setOnClickListener {
            val action =
                PurposeMeetingDetailsFragmentDirections.actionMeetingDetailsFragmentToNewHotelExpensesFragment(
                )
            binding.root.findNavController().navigate(action)
        }

        binding.ivAddFoodExpenses.setOnClickListener {
            val action =
                PurposeMeetingDetailsFragmentDirections.actionMeetingDetailsFragmentToNewFoodExpensesFragment(
                )
            binding.root.findNavController().navigate(action)
        }

        initRecyclerView()
        commentsItemListAdapter.differ.submitList(createDummyData())
        createDummyData()

        viewModel.resNewCommentResponse.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it.status) {
                    RestApiStatus.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        if (it.data != null) {
                            it.data.let { list ->
                                binding.llAdvanceAmount.visibility = GONE
                                viewModel.getCommentProjectID(opportunityID)
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

        viewModel.resMeetingPurposeIDItems.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it.status) {
                    RestApiStatus.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        if (it.data != null) {
                            it.data.let { list ->
                                binding.taskCategoryInfo = list

                                binding.priority.text = if(list.status == "Active")
                                      "Active" else "InActive"
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

        viewModel.resEscalationList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        it.data.let { list ->
//                            if (list.isEmpty()) binding.avEscalationItems.visibility = View.VISIBLE
//                            else binding.avEscalationItems.visibility = View.GONE
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

        viewModel.resActionItemsList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        it.data.let { list ->
//                            if (list.isEmpty()) binding.avActionItems.visibility = View.VISIBLE
//                            else binding.avActionItems.visibility = View.GONE
                            actionItemListAdapter.differ.submitList(list)
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

        viewModel.resMomActionItemsList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        it.data.let { list ->
//                            if (list.isEmpty()) binding.avMomActionItems.visibility = View.VISIBLE
//                            else binding.avMomActionItems.visibility = View.GONE
                            momActionItemListAdapter.differ.submitList(list)
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

        viewModel.resCommentsList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        it.data.let { list ->
//                            if (list.isEmpty()) binding.avComments.visibility = View.VISIBLE
//                            else binding.avComments.visibility = View.GONE
                            commentsItemListAdapter.differ.submitList(list)
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

        viewModel.resPendingActionGraphList.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it.status) {
                    RestApiStatus.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        if (it.data != null) {

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

    private fun isValidate(): Boolean =
        validateComments()

    /**
     * field must not be empty
     */
    private fun validateComments(): Boolean {
//        if (binding.edtComment.text.toString().trim().isEmpty()) {
//            binding.edtlComment.error = "Required Field!"
//            binding.edtComment.requestFocus()
//            return false
//        } else {
//            binding.edtlComment.isErrorEnabled = false
//        }
        return true
    }

    private fun initRecyclerView() {
//        binding.rvEscalationItems.adapter = adapter
//        binding.rvEscalationItems.layoutManager = LinearLayoutManager(requireContext())
//
//        binding.rvComments.adapter = commentsItemListAdapter
//        binding.rvComments.layoutManager = LinearLayoutManager(requireContext())
//
//        binding.rvActionItems.adapter = actionItemListAdapter
//        binding.rvActionItems.layoutManager = LinearLayoutManager(requireContext())
//
//        binding.rvMomActionItems.adapter = momActionItemListAdapter
//        binding.rvMomActionItems.layoutManager = LinearLayoutManager(requireContext())
//        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
//        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
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
//            val taskInfo = adapter.differ.currentList[position]
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