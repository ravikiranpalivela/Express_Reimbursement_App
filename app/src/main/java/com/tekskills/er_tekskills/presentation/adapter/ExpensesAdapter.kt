package com.tekskills.er_tekskills.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.ExpenseType
import com.tekskills.er_tekskills.data.model.UserExpence
import com.tekskills.er_tekskills.databinding.ItemFoodExpensesBinding
import com.tekskills.er_tekskills.databinding.ItemHotelExpensesBinding
import com.tekskills.er_tekskills.databinding.ItemTravelExpensesItemBinding

class ExpensesAdapter :
    RecyclerView.Adapter<ExpensesAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<UserExpence>() {
        override fun areItemsTheSame(oldItem: UserExpence, newItem: UserExpence): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserExpence, newItem: UserExpence): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, callback)

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].expensesUser.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = when (ExpenseType.values()[viewType]) {
            ExpenseType.Travel -> DataBindingUtil.inflate(inflater, R.layout.item_travel_expenses_item, parent, false)
            ExpenseType.Hotel -> DataBindingUtil.inflate(inflater, R.layout.item_hotel_expenses, parent, false)
            ExpenseType.foodexpence -> DataBindingUtil.inflate(inflater, R.layout.item_food_expenses, parent, false)
        }
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MyViewHolder(private val itemBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(expense: UserExpence) {
            when (expense.expensesUser) {
                ExpenseType.Travel -> (itemBinding as ItemTravelExpensesItemBinding).taskCategoryInfo = expense
                ExpenseType.Hotel -> (itemBinding as ItemHotelExpensesBinding).foodExpense = expense
                ExpenseType.foodexpence -> (itemBinding as ItemFoodExpensesBinding).foodExpense = expense
            }
            itemBinding.executePendingBindings()
        }
    }

    fun submitList(expenses: List<UserExpence>) {
        differ.submitList(expenses)
    }
}
