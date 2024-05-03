package com.tekskills.er_tekskills.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.ActionItemProjectIDResponseItem
import com.tekskills.er_tekskills.data.util.DateToString
import com.tekskills.er_tekskills.databinding.ItemActionItemsBinding

class ActionItemsOpportunitysAdapter :
    RecyclerView.Adapter<ActionItemsOpportunitysAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<ActionItemProjectIDResponseItem>() {
        override fun areItemsTheSame(
            oldItem: ActionItemProjectIDResponseItem,
            newItem: ActionItemProjectIDResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ActionItemProjectIDResponseItem,
            newItem: ActionItemProjectIDResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_action_items, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MyViewHolder(private val itemTaskBinding: ItemActionItemsBinding) :
        RecyclerView.ViewHolder(itemTaskBinding.root) {
        fun bind(taskCategoryInfo: ActionItemProjectIDResponseItem) {
            itemTaskBinding.taskCategoryInfo = taskCategoryInfo
            itemTaskBinding.executePendingBindings()

            taskCategoryInfo.actionItemCompletionDate.let {
                itemTaskBinding.actionItemCompletionDate.text = DateToString.convertDateStringToCustomFormat(taskCategoryInfo.actionItemCompletionDate)
            }

//            itemTaskBinding.isCompleted.setOnCheckedChangeListener { _, it ->
//                taskCategoryInfo.escalationDetails.escalationStatus = if (it) "Active" else "InActive"
//                onTaskStatusChangedListener?.let {
//                    it(taskCategoryInfo)
//                }
//            }

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

    private var onItemClickListener: ((ActionItemProjectIDResponseItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (ActionItemProjectIDResponseItem) -> Unit) {
        onItemClickListener = listener
    }

    private var onEditItemClickListener: ((ActionItemProjectIDResponseItem) -> Unit)? = null
    fun setOnEditItemClickListener(listener: (ActionItemProjectIDResponseItem) -> Unit) {
        onEditItemClickListener = listener
    }

    private var onTaskStatusChangedListener: ((ActionItemProjectIDResponseItem) -> Unit)? = null
    fun setOnTaskStatusChangedListener(listener: (ActionItemProjectIDResponseItem) -> Unit) {
        onTaskStatusChangedListener = listener
    }

}