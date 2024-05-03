package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class UserExpence(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("expensesUser")
    val expensesUser: String,
    @SerializedName("foodAmount")
    val foodAmount: Int,
    @SerializedName("foodComments")
    val foodComments: String,
    @SerializedName("hotelAmount")
    val hotelAmount: Int,
    @SerializedName("hotelFromDate")
    val hotelFromDate: String,
    @SerializedName("hotelToDate")
    val hotelToDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("invoiceFile")
    val invoiceFile: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("modeOfTravel")
    val modeOfTravel: String,
    @SerializedName("noOfDays")
    val noOfDays: Int,
    @SerializedName("purposeId")
    val purposeId: Int,
    @SerializedName("returnFrom")
    val returnFrom: String,
    @SerializedName("returnModeOfTravel")
    val returnModeOfTravel: String,
    @SerializedName("returnTo")
    val returnTo: String,
    @SerializedName("returnTravelDate")
    val returnTravelDate: String,
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