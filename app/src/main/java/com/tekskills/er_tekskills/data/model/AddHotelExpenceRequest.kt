package com.tekskills.er_tekskills.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Ravi Kiran Palivela on 5/8/2024.
 * Description: $
 */
data class AddHotelExpenceRequest (
    @SerializedName("purposeId") val purposeId: Int,
    @SerializedName("hotelFromDate") val hotelFromDate: String,
    @SerializedName("hotelToDate") val hotelToDate: String,
    @SerializedName("location") val location: String,
    @SerializedName("noOfDays") val noOfDays: String,
    @SerializedName("amount") val hotelAmount: Double,
    @SerializedName("expensesUser") val expensesUser: String
)