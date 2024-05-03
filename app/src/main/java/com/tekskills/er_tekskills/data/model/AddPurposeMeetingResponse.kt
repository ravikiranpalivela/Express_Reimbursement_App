package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class AddPurposeMeetingResponse(
    @SerializedName("advanceAmountRupees")
    val advanceAmountRupees: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("employeeId")
    val employeeId: Int,
    @SerializedName("expenceType")
    val expenceType: Any,
    @SerializedName("financeComments")
    val financeComments: Any,
    @SerializedName("financeId")
    val financeId: Int,
    @SerializedName("financeStatus")
    val financeStatus: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("managerComments")
    val managerComments: Any,
    @SerializedName("managerId")
    val managerId: Int,
    @SerializedName("managerStatus")
    val managerStatus: Any,
    @SerializedName("noOfDays")
    val noOfDays: Int,
    @SerializedName("status")
    val status: Any,
    @SerializedName("tenantId")
    val tenantId: Int,
    @SerializedName("travelDeskComments")
    val travelDeskComments: Any,
    @SerializedName("travelDeskId")
    val travelDeskId: Int,
    @SerializedName("travelDeskStatus")
    val travelDeskStatus: Any,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int,
    @SerializedName("userCordinates")
    val userCordinates: UserCordinatesResponse,
    @SerializedName("userExpences")
    val userExpences: Any,
    @SerializedName("visitDate")
    val visitDate: String,
    @SerializedName("visitPurpose")
    val visitPurpose: String
)

data class UserCordinatesResponse(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("destinantionLatitude")
    val destinantionLatitude: String,
    @SerializedName("destinantionLongitude")
    val destinantionLongitude: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("purposeId")
    val purposeId: Int,
    @SerializedName("sourceLatitude")
    val sourceLatitude: String,
    @SerializedName("sourceLongitude")
    val sourceLongitude: String,
    @SerializedName("tenantId")
    val tenantId: Int,
    @SerializedName("totalDistance")
    val totalDistance: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int
)