package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class UserCordinates(
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