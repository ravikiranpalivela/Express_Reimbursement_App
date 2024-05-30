package com.tekskills.er_tekskills.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.MomResponse
import com.tekskills.er_tekskills.data.model.MomResponseItem
import com.tekskills.er_tekskills.databinding.ItemMomActionItemsBinding

class MomActionItemsAdapter :
    RecyclerView.Adapter<MomActionItemsAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<MomResponseItem>() {
        override fun areItemsTheSame(oldItem: MomResponseItem, newItem: MomResponseItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MomResponseItem, newItem: MomResponseItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemMomActionItemsBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_mom_action_items, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MyViewHolder(private val itemBinding: ItemMomActionItemsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(foodExpense: MomResponseItem) {
            itemBinding.taskCategoryInfo = foodExpense
            itemBinding.executePendingBindings()
        }
    }

    fun submitList(foodExpenses: MomResponse) {
        differ.submitList(foodExpenses)
    }
}