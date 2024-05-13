package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class AddTravelExpenceResponse(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("expensesUser")
    val expensesUser: String,
    @SerializedName("file")
    val `file`: Any,
    @SerializedName("foodAmount")
    val foodAmount: Int,
    @SerializedName("foodComments")
    val foodComments: Any,
    @SerializedName("hotelAmount")
    val hotelAmount: Int,
    @SerializedName("hotelFromDate")
    val hotelFromDate: Any,
    @SerializedName("hotelToDate")
    val hotelToDate: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invoiceFile")
    val invoiceFile: String,
    @SerializedName("location")
    val location: Any,
    @SerializedName("modeOfTravel")
    val modeOfTravel: String,
    @SerializedName("noOfDays")
    val noOfDays: Int,
    @SerializedName("purposeId")
    val purposeId: Int,
    @SerializedName("returnFrom")
    val returnFrom: Any,
    @SerializedName("returnModeOfTravel")
    val returnModeOfTravel: Any,
    @SerializedName("returnTo")
    val returnTo: Any,
    @SerializedName("returnTravelDate")
    val returnTravelDate: Any,
    @SerializedName("tenantId")
    val tenantId: Int,
    @SerializedName("travelDate")
    val travelDate: String,
    @SerializedName("travelFrom")
    val travelFrom: String,
    @SerializedName("travelTo")
    val travelTo: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int
)