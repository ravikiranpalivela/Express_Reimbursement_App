package com.tekskills.er_tekskills.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.ClientEscalationGraphByIDResponseItem
import com.tekskills.er_tekskills.data.util.DateToString
import com.tekskills.er_tekskills.databinding.ItemClientWiseEscalationItemsBinding

class ClientWiseEscalationAdapter :
    RecyclerView.Adapter<ClientWiseEscalationAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<ClientEscalationGraphByIDResponseItem>() {
        override fun areItemsTheSame(
            oldItem: ClientEscalationGraphByIDResponseItem,
            newItem: ClientEscalationGraphByIDResponseItem
        ): Boolean {
            return oldItem.projectName == newItem.projectName
        }

        override fun areContentsTheSame(
            oldItem: ClientEscalationGraphByIDResponseItem,
            newItem: ClientEscalationGraphByIDResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_client_wise_escalation_items, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MyViewHolder(private val itemTaskBinding: ItemClientWiseEscalationItemsBinding) :
        RecyclerView.ViewHolder(itemTaskBinding.root) {
        fun bind(taskCategoryInfo: ClientEscalationGraphByIDResponseItem) {
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

            taskCategoryInfo.escRaiseDate?.let {
                itemTaskBinding.clientWiseEscalationRaisedDate.text = DateToString.convertDateStringToCustomFormat(
                    taskCategoryInfo.escRaiseDate
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

    private var onItemClickListener: ((ClientEscalationGraphByIDResponseItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (ClientEscalationGraphByIDResponseItem) -> Unit) {
        onItemClickListener = listener
    }

    private var onEditItemClickListener: ((ClientEscalationGraphByIDResponseItem) -> Unit)? = null
    fun setOnEditItemClickListener(listener: (ClientEscalationGraphByIDResponseItem) -> Unit) {
        onEditItemClickListener = listener
    }

    private var onTaskStatusChangedListener: ((ClientEscalationGraphByIDResponseItem) -> Unit)? = null
    fun setOnTaskStatusChangedListener(listener: (ClientEscalationGraphByIDResponseItem) -> Unit) {
        onTaskStatusChangedListener = listener
    }

}