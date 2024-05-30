//package com.tekskills.er_tekskills.presentation.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.recyclerview.widget.AsyncListDiffer
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.tekskills.er_tekskills.R
//import com.tekskills.er_tekskills.data.model.MomProjectResponseItem
//import com.tekskills.er_tekskills.data.util.DateToString
//import com.tekskills.er_tekskills.databinding.ItemMomActionItemsBinding
//import java.lang.Exception
//
//class MomActionItemsOpportunitysAdapter :
//    RecyclerView.Adapter<MomActionItemsOpportunitysAdapter.MyViewHolder>() {
//
//    private val callback = object : DiffUtil.ItemCallback<MomProjectResponseItem>() {
//        override fun areItemsTheSame(
//            oldItem: MomProjectResponseItem,
//            newItem: MomProjectResponseItem
//        ): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(
//            oldItem: MomProjectResponseItem,
//            newItem: MomProjectResponseItem
//        ): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//    val differ = AsyncListDiffer(this, callback)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        return MyViewHolder(
//            DataBindingUtil.inflate(
//                LayoutInflater.from(parent.context), R.layout.item_mom_action_items, parent, false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.bind(differ.currentList[position])
//    }
//
//    override fun getItemCount(): Int {
//        return differ.currentList.size
//    }
//
//    inner class MyViewHolder(private val itemTaskBinding: ItemMomActionItemsBinding) :
//        RecyclerView.ViewHolder(itemTaskBinding.root) {
//        fun bind(taskCategoryInfo: MomProjectResponseItem) {
//            itemTaskBinding.taskCategoryInfo = taskCategoryInfo
//            itemTaskBinding.executePendingBindings()
//
//            try {
//                taskCategoryInfo.meetingDate.let {
//                    itemTaskBinding.meetingNotes.text =
//                        DateToString.convertDateStringToCustomFormat(taskCategoryInfo.meetingDate)
//                }
//                taskCategoryInfo.meetingTime.let {
//                    itemTaskBinding.meetingNotes.text = DateToString.convertDateStringToCustomFormat(
//                        taskCategoryInfo.meetingTime
//                    )
//                }
//            }catch (e:Exception)
//            {
//
//            }
//
////            itemTaskBinding.isCompleted.setOnCheckedChangeListener { _, it ->
////                taskCategoryInfo.escalationDetails.escalationStatus = if (it) "Active" else "InActive"
////                onTaskStatusChangedListener?.let {
////                    it(taskCategoryInfo)
////                }
////            }
//
//            itemTaskBinding.ivViewMom.setOnClickListener {
//                onItemClickListener?.let {
//                    it(taskCategoryInfo)
//                }
//            }
//
//            itemTaskBinding.ivEditMom.setOnClickListener {
//                onEditItemClickListener?.let {
//                    it(taskCategoryInfo)
//                }
//            }
//
//        }
//    }
//
//    private var onItemClickListener: ((MomProjectResponseItem) -> Unit)? = null
//
//    fun setOnItemClickListener(listener: (MomProjectResponseItem) -> Unit) {
//        onItemClickListener = listener
//    }
//
//    private var onEditItemClickListener: ((MomProjectResponseItem) -> Unit)? = null
//
//    fun setOnEditItemClickListener(listener: (MomProjectResponseItem) -> Unit) {
//        onEditItemClickListener = listener
//    }
//
//    private var onTaskStatusChangedListener: ((MomProjectResponseItem) -> Unit)? = null
//    fun setOnTaskStatusChangedListener(listener: (MomProjectResponseItem) -> Unit) {
//        onTaskStatusChangedListener = listener
//    }
//
//}