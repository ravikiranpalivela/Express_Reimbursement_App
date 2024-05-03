package com.tekskills.er_tekskills.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.ChartHoverModel
import com.tekskills.er_tekskills.data.util.CommonUtils
import com.tekskills.er_tekskills.databinding.MarkerViewItemBinding


@Suppress("DEPRECATION")
class MarkerViewAdapter(
    chartHoverModelList: ArrayList<ChartHoverModel>,
    selectedType: String,
    where: String
) :
    RecyclerView.Adapter<MarkerViewAdapter.ViewHolder>() {
    private var mCharHoverModelList: ArrayList<ChartHoverModel> = chartHoverModelList
    private val mSelectedType = selectedType
    private val mWhere = where
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: MarkerViewItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.marker_view_item, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mCharHoverModelList[position])
    }

    override fun getItemCount(): Int {
        return mCharHoverModelList.size
    }

    inner class ViewHolder(var itemRowBinding: MarkerViewItemBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(obj: ChartHoverModel) {

            itemRowBinding.executePendingBindings()
            itemRowBinding.tvTitle.text = obj.name

//            itemRowBinding.ivCompany.setColorFilter(
//                obj.color!!,
//                android.graphics.PorterDuff.Mode.SRC_IN
//            )
            // truncateNumber


            if (mWhere == "f") {
                itemRowBinding.tvTitleValue.text =
                    CommonUtils.instance.truncateNumberWithK(obj.value!!.toFloat())
            } else {
                if (mSelectedType == "p") {
                    itemRowBinding.tvTitleValue.text =
                        CommonUtils.instance.makePretty(obj.value!!.toDouble())
                            .removePrefix("$") + "%"
                    if (itemRowBinding.tvTitleValue.text.toString().contains(".M"))
                        itemRowBinding.tvTitleValue.text =
                            itemRowBinding.tvTitleValue.text.toString()
                                .replace(".", "")
                } else {
                    itemRowBinding.tvTitleValue.text =
                        CommonUtils.instance.truncateNumberWithK(obj.value!!.toFloat())
                            .removePrefix("$")
                }
            }


        }

    }
}