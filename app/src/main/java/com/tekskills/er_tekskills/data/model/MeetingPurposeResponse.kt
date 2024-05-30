package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName


class MeetingPurposeResponse : ArrayList<MeetingPurposeResponseData>()

//data class MeetingPurposeResponse(
//    @SerializedName("content")
//    val content: ArrayList<MeetingPurposeResponseData>,
//    @SerializedName("empty")
//    val empty: Boolean,
//    @SerializedName("first")
//    val first: Boolean,
//    @SerializedName("last")
//    val last: Boolean,
//    @SerializedName("number")
//    val number: Int,
//    @SerializedName("numberOfElements")
//    val numberOfElements: Int,
//    @SerializedName("pageable")
//    val pageable: Pageable,
//    @SerializedName("size")
//    val size: Int,
//    @SerializedName("sort")
//    val sort: SortX,
//    @SerializedName("totalElements")
//    val totalElements: Int,
//    @SerializedName("totalPages")
//    val totalPages: Int
//)


data class MeetingPurposeData(
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
    val noOfDays: String,
    @SerializedName("status")
    var status: String,
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
    val userCordinates: UserCoordinates,
    @SerializedName("userExpences")
    val userExpences: Any,
    @SerializedName("visitDate")
    val visitDate: String,
    @SerializedName("visitPurpose")
    val visitPurpose: String
)

data class SortX(
    @SerializedName("empty")
    val empty: Boolean,
    @SerializedName("sorted")
    val sorted: Boolean,
    @SerializedName("unsorted")
    val unsorted: Boolean
)