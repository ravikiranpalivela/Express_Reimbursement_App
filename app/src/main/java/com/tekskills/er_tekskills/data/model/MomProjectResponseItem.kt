package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MomProjectResponseItem(
    @SerializedName("assignedId")
    val assignedId: Int,
    @SerializedName("clientId")
    val clientId: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("meetingDate")
    val meetingDate: String,
    @SerializedName("meetingNotes")
    val meetingNotes: String,
    @SerializedName("meetingTime")
    val meetingTime: String,
    @SerializedName("meetingTitle")
    val meetingTitle: String,
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int
): Serializable