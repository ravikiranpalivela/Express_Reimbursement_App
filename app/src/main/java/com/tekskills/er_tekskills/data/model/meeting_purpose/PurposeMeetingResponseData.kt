package com.tekskills.er_tekskills.data.model.meeting_purpose

import com.google.gson.annotations.SerializedName


data class PurposeMeetingResponseData(
    @SerializedName("advanceAmountRupees")
    val advanceAmountRupees: Int,
    @SerializedName("checkIn")
    val checkIn: String,
    @SerializedName("checkOut")
    val checkOut: String,
    @SerializedName("clientId")
    val clientId: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("custmerEmail")
    val custmerEmail: String,
    @SerializedName("customerContactName")
    val customerContactName: String,
    @SerializedName("customerName")
    val customerName: String,
    @SerializedName("customerPhone")
    val customerPhone: Long,
    @SerializedName("description")
    val description: String,
    @SerializedName("employeeId")
    val employeeId: Int,
    @SerializedName("employeeName")
    val employeeName: String,
    @SerializedName("expenceType")
    val expenceType: String,
    @SerializedName("financeStatus")
    val financeStatus: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("managerStatus")
    val managerStatus: String,
    @SerializedName("modeOfTravel")
    val modeOfTravel: String,
    @SerializedName("opportunity")
    val opportunity: String,
    @SerializedName("tenantId")
    val tenantId: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int,
    @SerializedName("visitDate")
    val visitDate: String,
    @SerializedName("visitPurpose")
    val visitPurpose: String,
    @SerializedName("userCordinates")
    val userCordinates: UserCordinates,
    @SerializedName("userExpences")
    val userExpences: List<UserExpence>,
    @SerializedName("allowncesLimit")
    val allowncesLimit: AllowncesLimit,
)