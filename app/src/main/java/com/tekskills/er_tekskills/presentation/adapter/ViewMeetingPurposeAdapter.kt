package com.tekskills.er_tekskills.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.er_tekskills.databinding.ItemViewMeetingPurposeBinding

class ViewMeetingPurposeAdapter :
    RecyclerView.Adapter<ViewMeetingPurposeAdapter.MyViewHolder>() {

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
        return MyViewHolder(
            DataBindingUtil.inflate(
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

    inner class MyViewHolder(private val itemTaskBinding: ItemViewMeetingPurposeBinding) :
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

        }
    }

    private var onItemClickListener: ((MeetingPurposeResponseData) -> Unit)? = null
    private var onEditItemClickListener: ((MeetingPurposeResponseData) -> Unit)? = null

    fun setOnItemClickListener(listener: (MeetingPurposeResponseData) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnEditItemClickListener(listener: (MeetingPurposeResponseData) -> Unit) {
        onEditItemClickListener = listener
    }

    private var onTaskStatusChangedListener: ((MeetingPurposeResponseData) -> Unit)? = null
    fun setOnTaskStatusChangedListener(listener: (MeetingPurposeResponseData) -> Unit) {
        onTaskStatusChangedListener = listener
    }

}