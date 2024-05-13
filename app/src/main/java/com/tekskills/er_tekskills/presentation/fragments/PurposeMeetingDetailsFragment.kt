package com.tekskills.er_tekskills.presentation.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.UserExpence
import com.tekskills.er_tekskills.databinding.FragmentMeetingPurposeDetailsBinding
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.adapter.AdvanceAmountAdapter
import com.tekskills.er_tekskills.presentation.adapter.FoodExpenseAdapter
import com.tekskills.er_tekskills.presentation.adapter.HotelExpenseAdapter
import com.tekskills.er_tekskills.presentation.adapter.TravelExpenseAdapter
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.RestApiStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class PurposeMeetingDetailsFragment : ParentFragment() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentMeetingPurposeDetailsBinding
    private val args: PurposeMeetingDetailsFragmentArgs by navArgs()

    private var opportunityID: String = ""

    @Inject
    @Named("advance_meeting_purpose_details_fragment")
    lateinit var adapter: AdvanceAmountAdapter

    @Inject
    @Named("food_meeting_purpose_details_fragment")
    lateinit var foodExpenseAdapter: FoodExpenseAdapter


    @Inject
    @Named("travel_meeting_purpose_details_fragment")
    lateinit var travelExpenseAdapter: TravelExpenseAdapter

    @Inject
    @Named("hotel_meeting_purpose_details_fragment")
    lateinit var hotelExpenseAdapter: HotelExpenseAdapter

    private var recyclableTextView: TextView? = null
    val fixedColumnWidths = intArrayOf(5, 20, 20, 20, 20, 20, 20, 20)
    val scrollableColumnWidths = intArrayOf(20, 20, 20, 20, 20, 30, 30)
    val fixedRowHeight = 50
    val fixedHeaderHeight = 60

    private lateinit var row :TableRow

    val wrapWrapTableRowParams: TableRow.LayoutParams =
        TableRow.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )

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

        row = TableRow(requireContext())

        createTableData()

//        viewModel.getEscalationItemProjectID(opportunityID)
//        viewModel.getCommentProjectID(opportunityID)
//        viewModel.getMOMProjects(opportunityID)
//        viewModel.getActionItemProjectID(opportunityID)
        viewModel.getMeetingPurposeByID(opportunityID)
//        viewModel._resMeetingPurposeIDItems.value = DummyData().createDummyDataBYID()


        binding.ivAddAdvanceAmount.setOnClickListener {
            if (binding.llAddAdvanceAmount.isVisible)
                binding.llAddAdvanceAmount.visibility = GONE
            else
                binding.llAddAdvanceAmount.visibility = VISIBLE
        }

        binding.btnAddAdvanceAmount.setOnClickListener {
//            if (isValidate())
//                viewModel.addCommentOpportunity(
//                    binding.taskCategoryInfo!!.id.toString(),
//                    binding.taskCategoryInfo!!.AssignId.toString(),
//                    binding.edtComment.text.toString()
//                )
        }



//
//        adapter.setOnTaskStatusChangedListener {
//
//        }
//        adapter.setOnItemClickListener {
////            val action =
////                ViewMeetingPurposeFragmentDirections.actionOpportunityDetailsFragmentToEscalationDetails(
////                    it,
////                    it.escalationDetails.id.toString()
////                )
////            binding.root.findNavController().navigate(action)
//        }
//
//        adapter.setOnEditItemClickListener {
////            val action =
////                ViewMeetingPurposeFragmentDirections.actionOpportunityDetailsFragmentToEditEscalation(
////                    it,
////                    it.escalationDetails.id.toString()
////                )
////            binding.root.findNavController().navigate(action)
//        }
//
//        foodExpenseAdapter.setOnTaskStatusChangedListener {
////            updateTaskStatus(viewModel, it)
//        }
//        foodExpenseAdapter.setOnItemClickListener {
////            val action =
////                ViewMeetingPurposeFragmentDirections.actionOpportunityDetailsFragmentToActionItemDetails(
////                    it,
////                    it.id.toString()
////                )
////            binding.root.findNavController().navigate(action)
//        }
//
//        foodExpenseAdapter.setOnEditItemClickListener {
////            val action =
////                ViewMeetingPurposeFragmentDirections.actionOpportunityDetailsFragmentToEditActionItem(
////                    it,
////                    it.id.toString()
////                )
////            binding.root.findNavController().navigate(action)
//        }
//
//        travelExpenseAdapter.setOnTaskStatusChangedListener {
////            updateTaskStatus(viewModel, it)
//        }
//        travelExpenseAdapter.setOnItemClickListener {
////            val action =
////                ViewMeetingPurposeFragmentDirections.actionOpportunityDetailsFragmentToMomActionItemDetails(
////                    it,
////                    it.id.toString()
////                )
////            binding.root.findNavController().navigate(action)
//        }
//
//        travelExpenseAdapter.setOnEditItemClickListener {
////            val action =
////                ViewMeetingPurposeFragmentDirections.actionOpportunityDetailsFragmentToEditMomActionItem(
////                    it,
////                    it.id.toString()
////                )
////            binding.root.findNavController().navigate(action)
//        }

        binding.ivEditPurposeVisit.setOnClickListener {
//            val action =
//                ViewMeetingPurposeFragmentDirections.actionOpportunityDetailsFragmentToNewAssignedProjectFragment()
//            binding.root.findNavController().navigate(action)
        }

        binding.ivAddTravelExpenses.setOnClickListener {
            val action =
                PurposeMeetingDetailsFragmentDirections.actionMeetingDetailsFragmentToNewTravelExpenses(
                    opportunityID
                )
            binding.root.findNavController().navigate(action)
        }

        binding.ivAddHotelExpenses.setOnClickListener {
            val action =
                PurposeMeetingDetailsFragmentDirections.actionMeetingDetailsFragmentToNewHotelExpensesFragment(
                    opportunityID
                )
            binding.root.findNavController().navigate(action)
        }

        binding.ivAddFoodExpenses.setOnClickListener {
            val action =
                PurposeMeetingDetailsFragmentDirections.actionMeetingDetailsFragmentToNewFoodExpensesFragment(
                    opportunityID
                )
            binding.root.findNavController().navigate(action)
        }

        binding.apply {
            llPurposeMeeting.setOnClickListener(View.OnClickListener {
                if (clVistPurpose.isVisible) {
                    clVistPurpose.visibility = GONE
                    ivViewPurposeMeeting.background = resources.getDrawable(R.drawable.ic_down_icon)
                } else {
                    clVistPurpose.visibility = VISIBLE
                    ivViewPurposeMeeting.background = resources.getDrawable(R.drawable.ic_up_icon)
                }
            })
            ivViewPurposeMeeting.setOnClickListener(View.OnClickListener {
                if (clVistPurpose.isVisible) {
                    clVistPurpose.visibility = GONE
                    ivViewPurposeMeeting.background = resources.getDrawable(R.drawable.ic_down_icon)
                } else {
                    clVistPurpose.visibility = VISIBLE
                    ivViewPurposeMeeting.background = resources.getDrawable(R.drawable.ic_up_icon)
                }
            })

            llTravelExpenses.setOnClickListener(View.OnClickListener {
                if (clTravelExpenses.isVisible) {
                    clTravelExpenses.visibility = GONE
                    ivTravelExpenses.background = resources.getDrawable(R.drawable.ic_down_icon)
                } else {
                    clTravelExpenses.visibility = VISIBLE
                    ivTravelExpenses.background = resources.getDrawable(R.drawable.ic_up_icon)
                }
            })
            ivTravelExpenses.setOnClickListener(View.OnClickListener {
                if (clTravelExpenses.isVisible) {
                    clTravelExpenses.visibility = GONE
                    ivTravelExpenses.background = resources.getDrawable(R.drawable.ic_down_icon)
                } else {
                    clTravelExpenses.visibility = VISIBLE
                    ivTravelExpenses.background = resources.getDrawable(R.drawable.ic_up_icon)
                }
            })
            llHotelExpenses.setOnClickListener(View.OnClickListener {
                if (clHotelExpenses.isVisible) {
                    clHotelExpenses.visibility = GONE
                    ivHotelExpenses.background = resources.getDrawable(R.drawable.ic_down_icon)
                } else {
                    clHotelExpenses.visibility = VISIBLE
                    ivHotelExpenses.background = resources.getDrawable(R.drawable.ic_up_icon)
                }
            })
            ivHotelExpenses.setOnClickListener(View.OnClickListener {
                if (clHotelExpenses.isVisible) {
                    clHotelExpenses.visibility = GONE
                    ivHotelExpenses.background = resources.getDrawable(R.drawable.ic_down_icon)
                } else {
                    clHotelExpenses.visibility = VISIBLE
                    ivHotelExpenses.background = resources.getDrawable(R.drawable.ic_up_icon)
                }
            })
            llFoodExpenses.setOnClickListener(View.OnClickListener {
                if (clFoodExpenses.isVisible) {
                    clFoodExpenses.visibility = GONE
                    ivFoodExpenses.background = resources.getDrawable(R.drawable.ic_down_icon)
                } else {
                    clFoodExpenses.visibility = VISIBLE
                    ivFoodExpenses.background = resources.getDrawable(R.drawable.ic_up_icon)
                }
            })
            ivFoodExpenses.setOnClickListener(View.OnClickListener {
                if (clFoodExpenses.isVisible) {
                    clFoodExpenses.visibility = GONE
                    ivFoodExpenses.background = resources.getDrawable(R.drawable.ic_down_icon)
                } else {
                    clFoodExpenses.visibility = VISIBLE
                    ivFoodExpenses.background = resources.getDrawable(R.drawable.ic_up_icon)
                }
            })

            ivAllExpenses.setOnClickListener(View.OnClickListener {
                if (clAllExpenses.isVisible) {
                    clAllExpenses.visibility = GONE
                    ivAllExpenses.background = resources.getDrawable(R.drawable.ic_down_icon)
                } else {
                    clAllExpenses.visibility = VISIBLE
                    ivAllExpenses.background = resources.getDrawable(R.drawable.ic_up_icon)
                }
            })

//            ivTravelExpenses.setOnClickListener(View.OnClickListener {
//                if (clTravelExpenses.isVisible) {
//                    clTravelExpenses.visibility = GONE
//                    ivViewPurposeMeeting.background = resources.getDrawable(R.drawable.ic_down_icon)
//                } else {
//                    clTravelExpenses.visibility = VISIBLE
//                    ivTravelExpenses.background = resources.getDrawable(R.drawable.ic_up_icon)
//                }
//            })

        }

        initRecyclerView()
//        hotelExpenseAdapter.differ.submitList(createDummyData())
//        createDummyData()

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

                                expensesData(list.userExpences)

                                val travelExpenses = filterByExpenseUser(
                                    list.userExpences,
                                    "Travel"
                                )
                                val hotelExpenses = filterByExpenseUser(
                                    list.userExpences,
                                    "Hotel"
                                )
                                val foodExpenses = filterByExpenseUser(
                                    list.userExpences,
                                    "foodexpence"
                                )

                                foodExpenseAdapter.submitList(foodExpenses)
                                foodExpenseAdapter.notifyDataSetChanged()
//                                if (foodExpenses.isEmpty()) binding.avFoodExpenses.visibility =
//                                    View.VISIBLE
//                                else binding.avFoodExpenses.visibility = View.GONE
                                travelExpenseAdapter.submitList(travelExpenses)
                                travelExpenseAdapter.notifyDataSetChanged()
//                                if (travelExpenses.isEmpty()) binding.avTravelExpenses.visibility =
//                                    View.VISIBLE
//                                else binding.avTravelExpenses.visibility = View.GONE
                                hotelExpenseAdapter.submitList(hotelExpenses)
                                hotelExpenseAdapter.notifyDataSetChanged()
//                                if (hotelExpenses.isEmpty()) binding.avHotelExpenses.visibility =
//                                    View.VISIBLE
//                                else binding.avHotelExpenses.visibility = View.GONE

                                binding.priority.text = if (list.status == "Active")
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

//        viewModel.resEscalationList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            when (it.status) {
//                RestApiStatus.SUCCESS -> {
//                    binding.progress.visibility = View.GONE
//                    if (it.data != null) {
//                        it.data.let { list ->
////                            if (list.isEmpty()) binding.avEscalationItems.visibility = View.VISIBLE
////                            else binding.avEscalationItems.visibility = View.GONE
//                            adapter.differ.submitList(list)
//                        }
//                    } else {
//                        Snackbar.make(
//                            binding.root,
//                            "No Data Found",
//                            Snackbar.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//                RestApiStatus.LOADING -> {
//                    binding.progress.visibility = View.VISIBLE
//                }
//
//                RestApiStatus.ERROR -> {
//                    binding.progress.visibility = View.GONE
//                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
//                        .show()
//                }
//
//                else -> {
//                    binding.progress.visibility = View.GONE
//                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        })

//        viewModel.resActionItemsList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            when (it.status) {
//                RestApiStatus.SUCCESS -> {
//                    binding.progress.visibility = View.GONE
//                    if (it.data != null) {
//                        it.data.let { list ->
////                            if (list.isEmpty()) binding.avActionItems.visibility = View.VISIBLE
////                            else binding.avActionItems.visibility = View.GONE
//                            foodExpenseAdapter.differ.submitList(list)
//                        }
//                    } else {
//                        Snackbar.make(
//                            binding.root,
//                            "No Data Found",
//                            Snackbar.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//                RestApiStatus.LOADING -> {
//                    binding.progress.visibility = View.VISIBLE
//                }
//
//                RestApiStatus.ERROR -> {
//                    binding.progress.visibility = View.GONE
//                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
//                        .show()
//                }
//
//                else -> {
//                    binding.progress.visibility = View.GONE
//                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        })

//        viewModel.resMomActionItemsList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            when (it.status) {
//                RestApiStatus.SUCCESS -> {
//                    binding.progress.visibility = View.GONE
//                    if (it.data != null) {
//                        it.data.let { list ->
////                            if (list.isEmpty()) binding.avMomActionItems.visibility = View.VISIBLE
////                            else binding.avMomActionItems.visibility = View.GONE
//                            travelExpenseAdapter.differ.submitList(list)
//                        }
//                    } else {
//                        Snackbar.make(
//                            binding.root,
//                            "No Data Found",
//                            Snackbar.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//                RestApiStatus.LOADING -> {
//                    binding.progress.visibility = View.VISIBLE
//                }
//
//                RestApiStatus.ERROR -> {
//                    binding.progress.visibility = View.GONE
//                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
//                        .show()
//                }
//
//                else -> {
//                    binding.progress.visibility = View.GONE
//                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        })

//        viewModel.resCommentsList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            when (it.status) {
//                RestApiStatus.SUCCESS -> {
//                    binding.progress.visibility = View.GONE
//                    if (it.data != null) {
//                        it.data.let { list ->
////                            if (list.isEmpty()) binding.avComments.visibility = View.VISIBLE
////                            else binding.avComments.visibility = View.GONE
//                            hotelExpenseAdapter.differ.submitList(list)
//                        }
//                    } else {
//                        Snackbar.make(
//                            binding.root,
//                            "No Data Found",
//                            Snackbar.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//
//                RestApiStatus.LOADING -> {
//                    binding.progress.visibility = View.VISIBLE
//                }
//
//                RestApiStatus.ERROR -> {
//                    binding.progress.visibility = View.GONE
//                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
//                        .show()
//                }
//
//                else -> {
//                    binding.progress.visibility = View.GONE
//                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        })

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


    fun createTableData() {
        //header (fixed vertically)
        //header (fixed vertically)
        row.layoutParams = wrapWrapTableRowParams
        row.gravity = Gravity.CENTER
        row.setBackgroundColor(Color.BLUE)
        row.addView(makeTableRowWithText("Sno", fixedColumnWidths[0], fixedHeaderHeight))
        row.addView(makeTableRowWithText("Type", fixedColumnWidths[1], fixedHeaderHeight))
        row.addView(makeTableRowWithText("Actual", fixedColumnWidths[2], fixedHeaderHeight))
        row.addView(makeTableRowWithText("Allocated", fixedColumnWidths[3], fixedHeaderHeight))
        row.addView(makeTableRowWithText("Overdue", fixedColumnWidths[4], fixedHeaderHeight))
        row.addView(makeTableRowWithText("Status", fixedColumnWidths[4], fixedHeaderHeight))
        row.addView(makeTableRowWithText("Action", fixedColumnWidths[4], fixedHeaderHeight))
        binding.tableHeader.addView(row)
    }

    fun expensesData(userExpences: List<UserExpence>) {
        for (i in userExpences.indices) {
            val fixedView: TextView = makeTableRowWithText(
                userExpences[i].id.toString(),
                scrollableColumnWidths[0], fixedRowHeight
            )!!
            fixedView.setBackgroundColor(Color.BLUE)
            binding.fixedColumn.addView(fixedView)
            row = TableRow(requireContext())
            row.layoutParams = wrapWrapTableRowParams
            row.gravity = Gravity.CENTER
            row.setBackgroundColor(Color.WHITE)
            row.addView(
                makeTableRowWithText(
                    userExpences[i].expensesUser,
                    scrollableColumnWidths[1],
                    fixedRowHeight
                )
            )
            row.addView(
                makeTableRowWithText(
                    userExpences[i].amount.toString(),
                    scrollableColumnWidths[2],
                    fixedRowHeight
                )
            )
            row.addView(
                makeTableRowWithText(
                    userExpences[i].amount.toString(),
                    scrollableColumnWidths[3],
                    fixedRowHeight
                )
            )
            row.addView(
                makeTableRowWithText(
                    userExpences[i].amount.toString(),
                    scrollableColumnWidths[4],
                    fixedRowHeight
                )
            )

            row.addView(
                makeTableRowWithText(
                    userExpences[i].expensesUser,
                    scrollableColumnWidths[4],
                    fixedRowHeight
                )
            )

            row.addView(
                makeTableRowWithText(
                    userExpences[i].expensesUser,
                    scrollableColumnWidths[4],
                    fixedRowHeight
                )
            )

            row.addView(
                makeTableRowWithText(
                    userExpences[i].expensesUser,
                    scrollableColumnWidths[4],
                    fixedRowHeight
                )
            )
            binding.scrollablePart.addView(row)
        }
    }

    fun makeTableRowWithText(
        text: String?,
        widthInPercentOfScreenWidth: Int,
        fixedHeightInPixels: Int
    ): TextView? {
        val screenWidth = resources.displayMetrics.widthPixels
        recyclableTextView = TextView(requireContext())
        recyclableTextView!!.text = text
        recyclableTextView!!.setTextColor(Color.BLACK)
        recyclableTextView!!.textSize = 20F
        recyclableTextView!!.width = widthInPercentOfScreenWidth * screenWidth / 100
        recyclableTextView!!.height = fixedHeightInPixels
        return recyclableTextView
    }

    fun filterByExpenseUser(
        expenseList: List<UserExpence>,
        expenseUser: String
    ): List<UserExpence> {
        return expenseList.filter { it.expensesUser == expenseUser }
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
        binding.rvAdvanceAmount.adapter = adapter
        binding.rvAdvanceAmount.layoutManager = LinearLayoutManager(requireContext())
//
        binding.rvHotelExpenses.adapter = hotelExpenseAdapter
        binding.rvHotelExpenses.layoutManager = LinearLayoutManager(requireContext())

        binding.rvFoodExpenses.adapter = foodExpenseAdapter
        binding.rvFoodExpenses.layoutManager = LinearLayoutManager(requireContext())

        binding.rvTravelExpenses.adapter = travelExpenseAdapter
        binding.rvTravelExpenses.layoutManager = LinearLayoutManager(requireContext())
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