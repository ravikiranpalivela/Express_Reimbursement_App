package com.tekskills.er_tekskills.presentation.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.databinding.FragmentHomeBinding
import com.tekskills.er_tekskills.data.model.PendingActionGraphListData
import com.tekskills.er_tekskills.data.model.PendingActionGraphResponse
import com.tekskills.er_tekskills.data.model.PendingEscalationGraphResponse
import com.tekskills.er_tekskills.data.model.PendingEscalationListData
import com.tekskills.er_tekskills.utils.RestApiStatus
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainFragment : ParentFragment() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentHomeBinding

    private var xAxisValues = ArrayList<String>()
    private var xPendingAxisValues = ArrayList<String>()

    private var mChartDataArray = ArrayList<PendingActionGraphListData>()
    private var mPendingChartDataArray = ArrayList<PendingEscalationListData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        viewModel.getPendingActionGraph()
        viewModel.getEscalationGraph()


        viewModel.resPendingActionGraphList.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it.status) {
                    RestApiStatus.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        if (it.data != null) {
                            setPendingBarChart(it.data)
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
                        Snackbar.make(binding.root, "Something went wrong ${it.message}", Snackbar.LENGTH_SHORT)
                            .show()
                    }

                    else -> {
                        binding.progress.visibility = View.GONE
                        Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            })


        viewModel.resEscalationGraphList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null) {
                        setEscalationBarChart(it.data)
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

    private fun getDataSet(clientNames: PendingActionGraphResponse): ArrayList<BarDataSet> {

        val dataSets: ArrayList<BarDataSet> = ArrayList()
        xAxisValues.clear()
        mChartDataArray.clear()
        mChartDataArray.addAll(clientNames)

        val barEntryList1: ArrayList<BarEntry> = ArrayList()
        for (j in 0 until mChartDataArray.size) {
            val barEntry1 = BarEntry(j.toFloat(), mChartDataArray[j].count.toFloat() ?: 0f)
            barEntryList1.add(barEntry1)
            xAxisValues.add("${mChartDataArray[j].projectName}")
        }
        val barDataSet1 = BarDataSet(
            barEntryList1,
            "Clients"
        )

        barDataSet1.highLightAlpha = 0

        barDataSet1.colors = (clientNames.indices).map {
            Color.rgb(
                (0..255).random(),
                (0..255).random(),
                (0..255).random()
            )
        }

        for (i in 0 until dataSets.size) {
            Log.d("TAG", "dataset ${dataSets[i].label}")
        }

        return dataSets
    }

    private fun setEscalationBarChart(clientNames: PendingEscalationGraphResponse) {

        if(clientNames.size > 0) {
            // Generate random stock values less than 20 for each company
            xPendingAxisValues.clear()
            mPendingChartDataArray.clear()
            mPendingChartDataArray.addAll(clientNames)

            val barEntryList1: ArrayList<BarEntry> = ArrayList()
            for (j in 0 until mPendingChartDataArray.size) {
                val barEntry1 =
                    BarEntry(j.toFloat(), mPendingChartDataArray[j].count.toFloat() ?: 0f,mPendingChartDataArray)
                barEntryList1.add(barEntry1)
                xPendingAxisValues.add("${mPendingChartDataArray[j]}")
            }
//        val barDataSet1 = BarDataSet(
//            barEntryList1,
//            "Clients"
//        )
//
//        barDataSet1.highLightAlpha = 0
//
//        barDataSet1.colors = (clientNames.indices).map {
//            Color.rgb(
//                (0..255).random(),
//                (0..255).random(),
//                (0..255).random()
//            )
//        }

            val barDataSet = BarDataSet(barEntryList1, "Client Escalation")
//        barDataSet.colors = (0 until companyNames.size).map { Color.rgb(0, 0, 255) }
            barDataSet.colors = (0 until clientNames.size).map {
                Color.rgb(
                    (0..255).random(),
                    (0..255).random(),
                    (0..255).random()
                )
            }
            val data = BarData(barDataSet)

            binding.pendingChart.data = data
            binding.pendingChart.setFitBars(true)
            binding.pendingChart.invalidate()
            binding.pendingChart.setDrawBorders(false)
            binding.pendingChart.setBackgroundColor(Color.TRANSPARENT)
            binding.pendingChart.setDrawGridBackground(false)
            binding.pendingChart.setPinchZoom(false)
            binding.pendingChart.isDoubleTapToZoomEnabled = false
            binding.pendingChart.description.isEnabled = false
            binding.pendingChart.animateY(1000)
            // Customize the chart appearance
            binding.pendingChart.description.isEnabled = false
//        binding.pendingChart.setDrawGridBackground(false)
//        binding.pendingChart.setDrawBarShadow(false)
//        binding.pendingChart.axisLeft.axisMinimum = 0.5f
//        binding.pendingChart.axisRight.axisMinimum = 0.5f
            val xAxis = binding.pendingChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            if (clientNames.size > 10)
                xAxis.labelRotationAngle = 45F

            xAxis.valueFormatter = IndexAxisValueFormatter(xPendingAxisValues)
            xAxis.labelCount = clientNames.size
            xAxis.granularity = 1f // Adjust the granularity as needed
            binding.pendingChart.setVisibleXRangeMaximum(15f)

            binding.pendingChart.setOnChartValueSelectedListener(object :
                OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
//                    val barSets = binding.pendingChart.barData?.dataSets as ArrayList<BarDataSet>

                    val action =
                        HomeFragmentDirections.actionHomeFragmentToClientWiseEscalationDetailsFragment(
                            clientNames[e?.x!!.toInt()].clientId.toString()
                        )
                    binding.root.findNavController().navigate(action)

//                    val data = ArrayList<ChartHoverModel>()
//                    for (i in 0 until barSets.size) {
//                        val chartHoverModel = ChartHoverModel()
//                        chartHoverModel.value =
//                            DecimalFormat("#0.00").format(barSets[i].getEntryForIndex(e?.x!!.toInt()).y)
//
//                        chartHoverModel.name = barSets[i].label
//                        chartHoverModel.yAxisLabel = xPendingAxisValues[e.x.toInt()]
//
//                        data.add(chartHoverModel)
//                    }
//
//                    binding.pendingChart.invalidate()
//
//                    val mv =
//                        CustomMarkerView(
//                            requireContext(),
//                            R.layout.marker_view,
//                            data,
//                            "bar",
//                            "n",
//                            "f"
//                        )
//                    mv.chartView = binding.pendingChart
//                    binding.pendingChart.markerView = mv
                }

                override fun onNothingSelected() {
                    val barSets = binding.pendingChart.barData?.dataSets as ArrayList<BarDataSet>
                    for (i in 0 until barSets.size) {
                    }
                    binding.pendingChart.invalidate()
                }
            })
            binding.tvClientEscalation.visibility = View.VISIBLE
            binding.pendingChart.visibility = View.VISIBLE
        }
        else
        {
            binding.tvClientEscalation.visibility = View.GONE
            binding.pendingChart.visibility = View.GONE
        }

    }

    private fun setPendingBarChart(clientNames: PendingActionGraphResponse) {
        if(clientNames.size > 0) {
        // Generate random stock values less than 20 for each company
        xAxisValues.clear()
        mChartDataArray.clear()
        mChartDataArray.addAll(clientNames)

        val barEntryList1: ArrayList<BarEntry> = ArrayList()
        for (j in 0 until mChartDataArray.size) {
            val barEntry1 =
            BarEntry(j.toFloat(), mChartDataArray[j].count.toFloat() ?: 0f,mChartDataArray)

            barEntryList1.add(barEntry1)
            xAxisValues.add("${mChartDataArray[j].projectName}")
        }
//        val barDataSet1 = BarDataSet(
//            barEntryList1,
//            "Clients"
//        )
//
//        barDataSet1.highLightAlpha = 0
//
//        barDataSet1.colors = (clientNames.indices).map {
//            Color.rgb(
//                (0..255).random(),
//                (0..255).random(),
//                (0..255).random()
//            )
//        }

        val barDataSet = BarDataSet(barEntryList1, "Pending Action Items")
//        barDataSet.colors = (0 until companyNames.size).map { Color.rgb(0, 0, 255) }
        barDataSet.colors = (0 until clientNames.size).map {
            Color.rgb(
                (0..255).random(),
                (0..255).random(),
                (0..255).random()
            )
        }
        val data = BarData(barDataSet)

        binding.chart.data = data
        binding.chart.setFitBars(true)
        binding.chart.invalidate()
        binding.chart.setDrawBorders(false)
        binding.chart.setBackgroundColor(Color.TRANSPARENT)
        binding.chart.setDrawGridBackground(false)
        binding.chart.setPinchZoom(false)
        binding.chart.isDoubleTapToZoomEnabled = false
        binding.chart.description.isEnabled = false
        binding.chart.animateY(1500)
        // Customize the chart appearance
        binding.chart.description.isEnabled = false
//        binding.chart.setDrawGridBackground(false)
//        binding.chart.setDrawBarShadow(false)
//        binding.chart.axisLeft.axisMinimum = 0.5f
//        binding.chart.axisRight.axisMinimum = 0.5f
        val xAxis = binding.chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        if (clientNames.size > 10)
            xAxis.labelRotationAngle = 45F

        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)
        xAxis.labelCount = clientNames.size
        xAxis.granularity = 1f // Adjust the granularity as needed
        binding.chart.setVisibleXRangeMaximum(15f)

        binding.chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToProjectWisePendingActionItemDetailsFragment(
                        clientNames[e?.x!!.toInt()].projectId.toString()
                    )
                binding.root.findNavController().navigate(action)

//                val barSets = binding.chart.barData?.dataSets as ArrayList<BarDataSet>
//
//                val data = ArrayList<ChartHoverModel>()
//                for (i in 0 until barSets.size) {
//                    val chartHoverModel = ChartHoverModel()
//                    chartHoverModel.value =
//                        DecimalFormat("#0.00").format(barSets[i].getEntryForIndex(e?.x!!.toInt()).y)
//                    chartHoverModel.name = barSets[i].label
//                    chartHoverModel.yAxisLabel = xAxisValues[e.x.toInt()]
//                    data.add(chartHoverModel)
//                }
//
//                binding.chart.invalidate()
//
//                val mv =
//                    CustomMarkerView(requireContext(), R.layout.marker_view, data, "bar", "n", "f")
//                mv.chartView = binding.chart
//                binding.chart.markerView = mv
            }

            override fun onNothingSelected() {
                val barSets = binding.chart.barData?.dataSets as ArrayList<BarDataSet>
                for (i in 0 until barSets.size) {

                }

                binding.chart.invalidate()
            }

        })

        binding.tvPendingAction.visibility = View.VISIBLE
        binding.chart.visibility = View.VISIBLE
    }
    else
    {
        binding.tvPendingAction.visibility = View.GONE
        binding.chart.visibility = View.GONE
    }

    }
}
