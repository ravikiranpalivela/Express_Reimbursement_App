package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class MeetingPurposeResponseData(
    @SerializedName("advanceAmountRupees")
    val advanceAmountRupees: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("employeeId")
    val employeeId: Int,
    @SerializedName("expenceType")
    val expenceType: String?,
    @SerializedName("financeComments")
    val financeComments: String?,
    @SerializedName("financeId")
    val financeId: Int,
    @SerializedName("financeStatus")
    val financeStatus: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("customerName")
    val customerName: String,
    @SerializedName("custmerEmail")
    val custmerEmail: String,
    @SerializedName("modeOfTravel")
    val modeOfTravel: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("customerContactName")
    val customerContactName: String,
    @SerializedName("customerPhone")
    val customerPhone: String,
    @SerializedName("managerComments")
    val managerComments: String,
    @SerializedName("managerId")
    val managerId: Int,
    @SerializedName("managerStatus")
    val managerStatus: String,
    @SerializedName("noOfDays")
    val noOfDays: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("tenantId")
    val tenantId: Int,
    @SerializedName("travelDeskComments")
    val travelDeskComments: String?,
    @SerializedName("travelDeskId")
    val travelDeskId: Int,
    @SerializedName("travelDeskStatus")
    val travelDeskStatus: String?,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int,
//    @SerializedName("checkIn")
//    val checkIn: String,
//    @SerializedName("checkOut")
//    val checkOut: String,
    @SerializedName("momDetails")
    val momDetails: MomResponse,
    @SerializedName("userCordinates")
    val userCordinates: UserCoordinates,
    @SerializedName("userExpences")
    val userExpences: List<UserExpence>,
    @SerializedName("allowncesLimit")
    val allowncesLimit: UserAllowenceResponse,
    @SerializedName("visitDate")
    val visitDate: String,
    @SerializedName("visitTime")
    val visitTime: String,
    @SerializedName("visitPurpose")
    val visitPurpose: String
)