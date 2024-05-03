package com.tekskills.er_tekskills.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.databinding.ItemEscalationItemsBinding
import com.tekskills.er_tekskills.data.model.ClientsEscalationResponseItem
import com.tekskills.er_tekskills.data.util.DateToString

class ClientEscalationOpportunitysAdapter :
    RecyclerView.Adapter<ClientEscalationOpportunitysAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<ClientsEscalationResponseItem>() {
        override fun areItemsTheSame(
            oldItem: ClientsEscalationResponseItem,
            newItem: ClientsEscalationResponseItem
        ): Boolean {
            return oldItem.projectName == newItem.projectName
        }

        override fun areContentsTheSame(
            oldItem: ClientsEscalationResponseItem,
            newItem: ClientsEscalationResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_escalation_items, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MyViewHolder(private val itemTaskBinding: ItemEscalationItemsBinding) :
        RecyclerView.ViewHolder(itemTaskBinding.root) {
        fun bind(taskCategoryInfo: ClientsEscalationResponseItem) {
            itemTaskBinding.taskCategoryInfo = taskCategoryInfo
            itemTaskBinding.executePendingBindings()

//            taskCategoryInfo.escalationDetails.escalationResolvedDate?.let {
//                itemTaskBinding.escalationResolvedDate.text = DateToString.getTimeAgo(itemTaskBinding.root.context, taskCategoryInfo.escalationDetails.escalationResolvedDate)
//
//            }
//            taskCategoryInfo.escalationDetails.escalationRaisedDate?.let {
//                itemTaskBinding.escalationRaisedDate.text = DateToString.getTimeAgo(
//                    itemTaskBinding.root.context,
//                    taskCategoryInfo.escalationDetails.escalationRaisedDate
//                )
//            }

            taskCategoryInfo.escalationDetails.escalationResolvedDate?.let {
                itemTaskBinding.escalationResolvedDate.text = DateToString.convertDateStringToCustomFormat(taskCategoryInfo.escalationDetails.escalationResolvedDate)

            }
            taskCategoryInfo.escalationDetails.escalationRaisedDate?.let {
                itemTaskBinding.escalationRaisedDate.text = DateToString.convertDateStringToCustomFormat(
                    taskCategoryInfo.escalationDetails.escalationRaisedDate
                )
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

    private var onItemClickListener: ((ClientsEscalationResponseItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (ClientsEscalationResponseItem) -> Unit) {
        onItemClickListener = listener
    }

    private var onEditItemClickListener: ((ClientsEscalationResponseItem) -> Unit)? = null
    fun setOnEditItemClickListener(listener: (ClientsEscalationResponseItem) -> Unit) {
        onEditItemClickListener = listener
    }

    private var onTaskStatusChangedListener: ((ClientsEscalationResponseItem) -> Unit)? = null
    fun setOnTaskStatusChangedListener(listener: (ClientsEscalationResponseItem) -> Unit) {
        onTaskStatusChangedListener = listener
    }

}