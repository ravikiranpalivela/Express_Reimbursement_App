package com.tekskills.er_tekskills.presentation.adapter

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.er_tekskills.databinding.ItemViewMeetingPurposeBinding
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

class ViewMeetingPurposeAdapter() :
    RecyclerView.Adapter<ViewMeetingPurposeAdapter.MyViewHolder>() {

    private var onLocationVerificationListener: ((MeetingPurposeResponseData, Boolean) -> Unit)? = null

    fun setOnLocationVerificationListener(listener: (MeetingPurposeResponseData, Boolean) -> Unit) {
        onLocationVerificationListener = listener
    }

    fun observeLocationResult(locationResult: LocationResult, range: Float) {
        val currentLocation = locationResult.lastLocation
        differ.currentList.forEach { meetingPurpose ->
//            meetingPurpose.userCordinates.sourceLatitude?.let { lat->
//                meetingPurpose.userCordinates.sourceLongitude?.let { long->
//                    val meetingLocation = Location("").apply {
//                        latitude = lat.toDouble()
//                        longitude = long.toDouble()
//                    }
//                    val distance = currentLocation!!.distanceTo(meetingLocation)
//                    val isWithinRange = distance <= range
//                    onLocationVerificationListener?.let {
//                        it(meetingPurpose, isWithinRange)
//                    }
//                }
//            }
        }
    }

    private val callback = object : DiffUtil.ItemCallback<MeetingPurposeResponseData>() {
        override fun areItemsTheSame(
            oldItem: MeetingPurposeResponseData,
            newItem: MeetingPurposeResponseData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MeetingPurposeResponseData,
            newItem: MeetingPurposeResponseData
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_view_meeting_purpose, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MyViewHolder(
        private val itemTaskBinding: ItemViewMeetingPurposeBinding
    ) :
        RecyclerView.ViewHolder(itemTaskBinding.root) {
        fun bind(taskCategoryInfo: MeetingPurposeResponseData) {

            itemTaskBinding.taskCategoryInfo = taskCategoryInfo
            itemTaskBinding.executePendingBindings()

            itemTaskBinding.isCompleted.setOnCheckedChangeListener { _, it ->
                taskCategoryInfo.status = if (it) "Active" else "InActive"
                onTaskStatusChangedListener?.let {
                    it(taskCategoryInfo)
                }
            }

            itemTaskBinding.ivViewOpportunity.setOnClickListener {
                onItemClickListener?.let {
                    it(taskCategoryInfo)
                }
            }

            itemTaskBinding.ivEditOpportunity.setOnClickListener {
                onEditItemClickListener?.let {
                    it(taskCategoryInfo)
                }
            }

            itemTaskBinding.tvCheckIn.setOnClickListener {
                onCheckINItemClickListener?.let {
                    it(taskCategoryInfo)
                }
            }

            itemTaskBinding.tvCheckOut.setOnClickListener {
                onCheckOUTItemClickListener?.let {
                    it(taskCategoryInfo)
                }
            }

            itemTaskBinding.tvAddMom.setOnClickListener {
                onAddMOMItemClickListener?.let {
                    it(taskCategoryInfo)
                }
            }
        }
    }

    private var onItemClickListener: ((MeetingPurposeResponseData) -> Unit)? = null
    private var onEditItemClickListener: ((MeetingPurposeResponseData) -> Unit)? = null
    private var onCheckINItemClickListener: ((MeetingPurposeResponseData) -> Unit)? = null
    private var onCheckOUTItemClickListener: ((MeetingPurposeResponseData) -> Unit)? = null
    private var onAddMOMItemClickListener: ((MeetingPurposeResponseData) -> Unit)? = null

    fun setOnItemClickListener(listener: (MeetingPurposeResponseData) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnEditItemClickListener(listener: (MeetingPurposeResponseData) -> Unit) {
        onEditItemClickListener = listener
    }

    fun setOnCheckINItemClickListener(listener: (MeetingPurposeResponseData) -> Unit) {
        onCheckINItemClickListener = listener
    }

    fun setOnCheckOUTItemClickListener(listener: (MeetingPurposeResponseData) -> Unit) {
        onCheckOUTItemClickListener = listener
    }

    fun setOnAddMOMItemClickListener(listener: (MeetingPurposeResponseData) -> Unit) {
        onAddMOMItemClickListener = listener
    }

    private var onTaskStatusChangedListener: ((MeetingPurposeResponseData) -> Unit)? = null
    fun setOnTaskStatusChangedListener(listener: (MeetingPurposeResponseData) -> Unit) {
        onTaskStatusChangedListener = listener
    }

}