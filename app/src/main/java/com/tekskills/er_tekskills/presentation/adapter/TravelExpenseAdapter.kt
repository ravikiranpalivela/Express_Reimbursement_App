package com.tekskills.er_tekskills.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.UserExpence
import com.tekskills.er_tekskills.databinding.ItemTravelExpensesItemBinding

class TravelExpenseAdapter :
    RecyclerView.Adapter<TravelExpenseAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<UserExpence>() {
        override fun areItemsTheSame(oldItem: UserExpence, newItem: UserExpence): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserExpence, newItem: UserExpence): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemTravelExpensesItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_travel_expenses_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MyViewHolder(private val itemBinding: ItemTravelExpensesItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(foodExpense: UserExpence) {
            itemBinding.taskCategoryInfo = foodExpense
            itemBinding.executePendingBindings()
        }
    }

    fun submitList(foodExpenses: List<UserExpence>) {
        differ.submitList(foodExpenses)
    }
}