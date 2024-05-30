package com.tekskills.er_tekskills.data.model.meeting_purpose


import com.google.gson.annotations.SerializedName

data class UserCordinates(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("destinantion")
    val destinantion: String,
    @SerializedName("destinantionLatitude")
    val destinantionLatitude: String,
    @SerializedName("destinantionLongitude")
    val destinantionLongitude: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("mapTime")
    val mapTime: String,
    @SerializedName("purposeId")
    val purposeId: Int,
    @SerializedName("source")
    val source: String,
    @SerializedName("sourceLatitude")
    val sourceLatitude: String,
    @SerializedName("sourceLongitude")
    val sourceLongitude: String,
    @SerializedName("tenantId")
    val tenantId: Int,
    @SerializedName("totalDistance")
    val totalDistance: Double,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int
)