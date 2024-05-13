package com.tekskills.er_tekskills.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Ravi Kiran Palivela on 5/8/2024.
 * Description: $
 */
data class AddTravelExpenceRequest (
    @SerializedName("purposeId") val purposeId: Int,
    @SerializedName("travelFrom") val travelFrom: String,
    @SerializedName("travelTo") val travelTo: String,
    @SerializedName("travelDate") val travelDate: String,
    @SerializedName("modeOfTravel") val modeOfTravel: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("expensesUser") val expensesUser: String
)

data class AddReturnTravelExpenceRequest (
    @SerializedName("returnFrom") val returnFrom: String,
    @SerializedName("returnTo") val returnTo: String,
    @SerializedName("returnTravelDate") val returnTravelDate: String,
    @SerializedName("returnModeOfTravel") val returnModeOfTravel: String,
)
