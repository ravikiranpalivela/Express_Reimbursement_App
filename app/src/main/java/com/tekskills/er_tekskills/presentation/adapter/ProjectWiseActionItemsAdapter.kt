package com.tekskills.er_tekskills.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.PendingActionItemGraphByIDResponseItem
import com.tekskills.er_tekskills.data.util.DateToString
import com.tekskills.er_tekskills.databinding.ItemProjectWisePendingActionItemBinding

class ProjectWiseActionItemsAdapter :
    RecyclerView.Adapter<ProjectWiseActionItemsAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<PendingActionItemGraphByIDResponseItem>() {
        override fun areItemsTheSame(
            oldItem: PendingActionItemGraphByIDResponseItem,
            newItem: PendingActionItemGraphByIDResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PendingActionItemGraphByIDResponseItem,
            newItem: PendingActionItemGraphByIDResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_project_wise_pending_action_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MyViewHolder(private val itemTaskBinding: ItemProjectWisePendingActionItemBinding) :
        RecyclerView.ViewHolder(itemTaskBinding.root) {
        fun bind(taskCategoryInfo: PendingActionItemGraphByIDResponseItem) {
            itemTaskBinding.taskCategoryInfo = taskCategoryInfo
            itemTaskBinding.executePendingBindings()

            taskCategoryInfo.complitionDate?.let {
                itemTaskBinding.actionItemCompletionDate.text = DateToString.convertDateStringToCustomFormat(
                    taskCategoryInfo.complitionDate
                )
            }

            itemTaskBinding.ivViewActionItem.setOnClickListener {
                onItemClickListener?.let {
                    it(taskCategoryInfo)
                }
            }

            itemTaskBinding.ivEditActionItem.setOnClickListener {
                onEditItemClickListener?.let {
                    it(taskCategoryInfo)
                }
            }
        }
    }

    private var onItemClickListener: ((PendingActionItemGraphByIDResponseItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (PendingActionItemGraphByIDResponseItem) -> Unit) {
        onItemClickListener = listener
    }

    private var onEditItemClickListener: ((PendingActionItemGraphByIDResponseItem) -> Unit)? = null
    fun setOnEditItemClickListener(listener: (PendingActionItemGraphByIDResponseItem) -> Unit) {
        onEditItemClickListener = listener
    }

    private var onTaskStatusChangedListener: ((PendingActionItemGraphByIDResponseItem) -> Unit)? = null
    fun setOnTaskStatusChangedListener(listener: (PendingActionItemGraphByIDResponseItem) -> Unit) {
        onTaskStatusChangedListener = listener
    }

}