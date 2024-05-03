package com.tekskills.er_tekskills.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.databinding.ItemViewOpportunityBinding
import com.tekskills.er_tekskills.data.model.ProjectOpportunityResponseItem

class ViewOpportunitysAdapter :
    RecyclerView.Adapter<ViewOpportunitysAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<ProjectOpportunityResponseItem>() {
        override fun areItemsTheSame(
            oldItem: ProjectOpportunityResponseItem,
            newItem: ProjectOpportunityResponseItem
        ): Boolean {
            return oldItem.projectId == newItem.projectId
        }

        override fun areContentsTheSame(
            oldItem: ProjectOpportunityResponseItem,
            newItem: ProjectOpportunityResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_view_opportunity, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MyViewHolder(private val itemTaskBinding: ItemViewOpportunityBinding) :
        RecyclerView.ViewHolder(itemTaskBinding.root) {
        fun bind(taskCategoryInfo: ProjectOpportunityResponseItem) {
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

    private var onItemClickListener: ((ProjectOpportunityResponseItem) -> Unit)? = null
    private var onEditItemClickListener: ((ProjectOpportunityResponseItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (ProjectOpportunityResponseItem) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnEditItemClickListener(listener: (ProjectOpportunityResponseItem) -> Unit) {
        onEditItemClickListener = listener
    }

    private var onTaskStatusChangedListener: ((ProjectOpportunityResponseItem) -> Unit)? = null
    fun setOnTaskStatusChangedListener(listener: (ProjectOpportunityResponseItem) -> Unit) {
        onTaskStatusChangedListener = listener
    }

}