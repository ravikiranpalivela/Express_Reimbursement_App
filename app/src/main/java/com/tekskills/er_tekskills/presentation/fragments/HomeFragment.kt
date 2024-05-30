package com.tekskills.er_tekskills.presentation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.AddCheckInRequest
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponse
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.er_tekskills.data.model.MeetingStatusRequest
import com.tekskills.er_tekskills.databinding.FragmentMainBinding
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.adapter.ViewMeetingPurposeAdapter
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.RestApiStatus
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.stream.Collectors
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeFragment : ParentFragment(), OnChartValueSelectedListener {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentMainBinding

    @Inject
    @Named("view_main_meetings")
    lateinit var adapter: ViewMeetingPurposeAdapter

    protected val parties: Array<String> = arrayOf(
        "Approved", "Rejected", "Pending", "Submitted", "Cancelled", "Completed", "Draft",
    )

    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        createPieChart()
        binding.llMeetings.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToViewMeetingPurposeFragment("")
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
        adapter.setOnCheckINItemClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToNewCheckInFragment(it.id.toString())
            binding.root.findNavController().navigate(action)
        }

        adapter.setOnCheckOUTItemClickListener {
            getCurrentLocation(it.id.toString())
        }

        adapter.setOnAddMOMItemClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToNewAddMOMFragment(it.id.toString())
            binding.root.findNavController().navigate(action)
        }


        binding.swiperefresh.setOnRefreshListener {
            binding.swiperefresh.postDelayed({
                viewModel.getMeetingPurpose("")
                viewModel.getMeetingPurposeStatus()
                binding.swiperefresh.isRefreshing = false
            }, 50)
        }

        viewModel.getMeetingPurpose("")

        viewModel.getMeetingPurposeStatus()

        initRecyclerView()

        viewModel.resMeetingPurposeStatusDetails.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        it.data.let { details ->
                            setMeetingStatusData(details)
                        }
                    } else {
                        val meetingStatusRequest = MeetingStatusRequest(
                            approveCount = 0,
                            pendingCount = 0,
                            rejectCount = 0
                        )

                        // Call the method with the static data
                        setMeetingStatusData(meetingStatusRequest)

                        Snackbar.make(
                            binding.root, "No Data Found", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {

                    val meetingStatusRequest = MeetingStatusRequest(
                        approveCount = 0,
                        pendingCount = 0,
                        rejectCount = 0
                    )

                    // Call the method with the static data
                    setMeetingStatusData(meetingStatusRequest)
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

        viewModel.resMeetingPurposeList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {

                        it.data.let { list ->
//                            if (list.content.isEmpty()) binding.avMeetings.visibility = View.VISIBLE
//                            else binding.avMeetings.visibility = View.GONE
//                            adapter.differ.submitList(filterItemsByDate(list.content))
//                            startLocationUpdates()

                            if (list.isEmpty()) binding.avMeetings.visibility = View.VISIBLE
                            else binding.avMeetings.visibility = View.GONE
                            adapter.differ.submitList(filterItemsByDate(list))
                            startLocationUpdates()
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

        viewModel.resUserMeetingCheckIN.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.message != "success") {
                        viewModel.getMeetingPurpose("")
                    } else {
                        Snackbar.make(
                            binding.root, "Something Went Wrong Check-IN", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Something Went Wrong Check-IN",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Something Went Wrong Check-IN",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
        })

        viewModel.resUserMeetingCheckOUT.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.message != "success") {
                        viewModel.getMeetingPurpose("")
                    } else {
                        Snackbar.make(
                            binding.root, "Something Went Wrong Check-OUT", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Something Went Wrong Check-OUT",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Something Went Wrong Check-OUT",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
        })

//        updateWeeklyDistanceChart()
        createPieChart()
//        binding.fab.setOnClickListener {
//            val action =
//                HomeFragmentDirections.actionViewOpportunityFragmentToNewAssignedProjectFragment()
//            it.findNavController().navigate(action)
//        }
    }


    private fun getCurrentLocation(purposeID: String) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful && task.result != null) {
                val location: Location? = task.result
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude

                    val checkin = AddCheckInRequest(
                        latitude = latitude.toString(),
                        longitude = longitude.toString(),
                    )
                    viewModel.putUserMeetingCheckOUT(purposeID, checkin)
                    Log.d("Location", "Lat: $latitude, Lon: $longitude")
                }
            } else {
                Log.w("Location", "Failed to get location.")
            }
        }
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // 10 seconds
            fastestInterval = 5000 // 5 seconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val range = 1000f // Example range in meters
                adapter.observeLocationResult(locationResult, range)
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun filterEventsForTodayAndTomorrow(events: ArrayList<MeetingPurposeResponseData>): MutableList<MeetingPurposeResponseData>? {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Calendar.getInstance().time
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.time

        val todayString = sdf.format(today)
        val tomorrowString = sdf.format(tomorrow)

        return events.stream().filter {
            it.visitDate == todayString || it.visitDate == tomorrowString
        }.collect(Collectors.toList())
    }

//    fun filterItemsByDate(items: ArrayList<MeetingPurposeResponseData>): ArrayList<MeetingPurposeResponseData> {
//        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//        val today = Calendar.getInstance().time
//        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.time
//
//        val todayString = sdf.format(today)
//        val tomorrowString = sdf.format(tomorrow)
//
//        return items.stream().filter {
//            it.visitDate == todayString || it.visitDate == tomorrowString
//        }.sortedBy { LocalDate.parse(it.visitDate) }
//    }

    fun filterItemsByDate(items: MeetingPurposeResponse): ArrayList<MeetingPurposeResponseData> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Calendar.getInstance().time
        val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.time

        val todayString = sdf.format(today)
        val tomorrowString = sdf.format(tomorrow)

        return items.filter {
//            if (it.visitTime != null) {
//                it.visitTime == todayString || it.visitTime == tomorrowString
//            } else {
            it.visitDate == todayString || it.visitDate == tomorrowString
//            }
        }.sortedBy { LocalDate.parse(it.visitDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
            .toCollection(ArrayList())
    }


    private fun createPieChart() {
        binding.pieChart.getDescription().setEnabled(false)
//        val desc = Description()
//        desc.text = "Meetings Status"
//        desc.textSize = 15F

//        binding.pieChart.description = desc
        binding.pieChart.setExtraOffsets(5F, 10F, 5F, 5F)
        binding.pieChart.setDragDecelerationFrictionCoef(0.95f)

//        binding.pieChart.setCenterText(generateCenterSpannableText())

        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.setHoleColor(Color.WHITE)

        binding.pieChart.setTransparentCircleColor(Color.WHITE)
        binding.pieChart.setTransparentCircleAlpha(110)

        binding.pieChart.holeRadius = 60f
        binding.pieChart.transparentCircleRadius = 61f

        binding.pieChart.setDrawCenterText(true)

        binding.pieChart.setRotationAngle(0F)

        // enable rotation of the chart by touch
        binding.pieChart.isRotationEnabled = true
        binding.pieChart.isHighlightPerTapEnabled = true


        // binding.pieChart.setUnit(" €");
        // binding.pieChart.setDrawUnitsInChart(true);

        // add a selection listener
        binding.pieChart.setOnChartValueSelectedListener(this)
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)


        // binding.pieChart.spin(2000, 0, 360);
        val l: Legend = binding.pieChart.legend

        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM;
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT;
        l.orientation = Legend.LegendOrientation.HORIZONTAL;
        l.setDrawInside(false);
        l.form = Legend.LegendForm.SQUARE;
        l.formSize = 9f;
        l.setTextSize(11f);
        l.xEntrySpace = 4f;


//        setData(3, 10F)

        // entry label styling
        binding.pieChart.setEntryLabelColor(Color.BLACK)
        binding.pieChart.setEntryLabelTextSize(12f)

    }

    private fun setMeetingStatusData(meetingStatusRequest: MeetingStatusRequest) {
        val entries = ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the binding.pieChart.

        meetingStatusRequest.let { count ->

            count.rejectCount.let {
                entries.add(
                    PieEntry(
                        it.toFloat(),
                        "Rejected",
                        resources.getDrawable(R.drawable.icon2)
                    )
                )
            }

            count.pendingCount.let {
                entries.add(
                    PieEntry(
                        it.toFloat(),
                        "Pending",
                        resources.getDrawable(R.drawable.icon2)
                    )
                )
            }

            count.approveCount.let {
                entries.add(
                    PieEntry(
                        it.toFloat(),
                        "Approved",
                        resources.getDrawable(R.drawable.icon2)
                    )
                )
            }

        }

        val dataSet = PieDataSet(entries, "")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 10f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE;
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE;

        val colors = ArrayList<Int>()

// Add new RGB colors
        colors.add(Color.rgb(255, 99, 132))   // rgb(255, 99, 132)
        colors.add(Color.rgb(54, 162, 235))   // rgb(54, 162, 235)
        colors.add(Color.rgb(255, 205, 86))   // rgb(255, 205, 86)
        colors.add(Color.rgb(201, 203, 207))  // rgb(201, 203, 207)
        colors.add(Color.rgb(75, 192, 192))   // rgb(75, 192, 192)

        dataSet.colors = colors

//        // add a lot of colors
//        val colors = ArrayList<Int>()
//
//        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
//
//        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
//
//        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
//
//        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
//
//        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
//
//        colors.add(ColorTemplate.getHoloBlue())
//
//        dataSet.colors = colors

        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
//        data.setValueFormatter(PercentFormatter())
        data.setValueFormatter { value, _, _, _ -> value.toInt().toString() }
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)
        binding.pieChart.setData(data)
        binding.pieChart.setUsePercentValues(false)
        // undo all highlights
        binding.pieChart.highlightValues(null)
        binding.pieChart.invalidate()
    }

    private fun initRecyclerView() {
        binding.rvMeetings.adapter = adapter
        binding.rvMeetings.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

        if (e == null)
            return;
        var statusInfo = ""

        h?.let {
            when (h.x) {
                0f -> {
                    statusInfo = "Rejected"
                }

                1f -> {
                    statusInfo = "Pending"
                }

                2f -> {
                    statusInfo = "Approved"
                }

                else ->
                    statusInfo = ""
            }
        }
        val action =
            HomeFragmentDirections.actionHomeFragmentToViewMeetingPurposeFragment(statusInfo)
        binding.root.findNavController().navigate(action)

    }

    override fun onNothingSelected() {

    }
}
