package com.tekskills.er_tekskills.data.model.meeting_purpose


import com.google.gson.annotations.SerializedName

data class UserExpence(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("expensesUser")
    val expensesUser: String,
    @SerializedName("fileNames")
    val fileNames: List<Any>,
    @SerializedName("files")
    val files: Any,
    @SerializedName("financeComments")
    val financeComments: String,
    @SerializedName("financeId")
    val financeId: Int,
    @SerializedName("financeStatus")
    val financeStatus: String,
    @SerializedName("foodComments")
    val foodComments: String,
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
    @SerializedName("managerComments")
    val managerComments: String,
    @SerializedName("managerId")
    val managerId: Int,
    @SerializedName("managerStatus")
    val managerStatus: String,
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
    @SerializedName("returnTravelAmount")
    val returnTravelAmount: Int,
    @SerializedName("returnTravelDate")
    val returnTravelDate: Any,
    @SerializedName("tenantId")
    val tenantId: Int,
    @SerializedName("travelAmount")
    val travelAmount: Int,
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