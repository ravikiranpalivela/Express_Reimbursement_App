package com.tekskills.er_tekskills.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.CommentsListResponseItem
import com.tekskills.er_tekskills.data.util.DateToString
import com.tekskills.er_tekskills.databinding.ItemViewCommentBinding

class CommentsOpportunitysAdapter :
    RecyclerView.Adapter<CommentsOpportunitysAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<CommentsListResponseItem>() {
        override fun areItemsTheSame(
            oldItem: CommentsListResponseItem,
            newItem: CommentsListResponseItem
        ): Boolean {
            return oldItem.commentDate == newItem.commentDate
        }

        override fun areContentsTheSame(
            oldItem: CommentsListResponseItem,
            newItem: CommentsListResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_view_comment, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MyViewHolder(private val itemTaskBinding: ItemViewCommentBinding) :
        RecyclerView.ViewHolder(itemTaskBinding.root) {
        fun bind(taskCategoryInfo: CommentsListResponseItem) {
            itemTaskBinding.taskCategoryInfo = taskCategoryInfo
            itemTaskBinding.executePendingBindings()

            taskCategoryInfo.commentDate.let {
                itemTaskBinding.tvCommentDate.text = DateToString.convertDateStringToCustomFormat(taskCategoryInfo.commentDate)
            }

//            itemTaskBinding.isCompleted.setOnCheckedChangeListener { _, it ->
//                taskCategoryInfo.escalationDetails.escalationStatus = if (it) "Active" else "InActive"
//                onTaskStatusChangedListener?.let {
//                    it(taskCategoryInfo)
//                }
//            }

            itemTaskBinding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(taskCategoryInfo)
                }
            }

        }
    }

    private var onItemClickListener: ((CommentsListResponseItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (CommentsListResponseItem) -> Unit) {
        onItemClickListener = listener
    }

    private var onTaskStatusChangedListener: ((CommentsListResponseItem) -> Unit)? = null
    fun setOnTaskStatusChangedListener(listener: (CommentsListResponseItem) -> Unit) {
        onTaskStatusChangedListener = listener
    }

}