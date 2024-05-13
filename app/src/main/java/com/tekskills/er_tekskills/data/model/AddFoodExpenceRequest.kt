package com.tekskills.er_tekskills.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Ravi Kiran Palivela on 5/8/2024.
 * Description: $
 */
data class AddFoodExpenceRequest (
    @SerializedName("purposeId") val purposeId: Int,
    @SerializedName("foodAmount") val foodAmount: String,
    @SerializedName("foodComments") val foodComments: String,
    @SerializedName("expensesUser") val expensesUser: String
)