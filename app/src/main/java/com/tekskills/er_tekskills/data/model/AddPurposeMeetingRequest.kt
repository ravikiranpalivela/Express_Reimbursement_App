package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class AddPurposeMeetingRequest(
    @SerializedName("employeeId")
    val employeeId: String,
    @SerializedName("noOfDays")
    val noOfDays: Int,
    @SerializedName("userCordinates")
    val userCordinates: UserCordinatesData,
    @SerializedName("visitDate")
    val visitDate: String,
    @SerializedName("visitPurpose")
    val visitPurpose: String
)

data class UserCordinatesData(
    @SerializedName("destinantionLatitude")
    val destinantionLatitude: String,
    @SerializedName("destinantionLongitude")
    val destinantionLongitude: String,
    @SerializedName("sourceLatitude")
    val sourceLatitude: String,
    @SerializedName("sourceLongitude")
    val sourceLongitude: String,
    @SerializedName("totalDistance")
    val totalDistance: Double
)