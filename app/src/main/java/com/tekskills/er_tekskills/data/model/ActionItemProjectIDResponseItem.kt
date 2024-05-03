package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ActionItemProjectIDResponseItem(
    @SerializedName("actionItemCompletionDate")
    val actionItemCompletionDate: String,
    @SerializedName("actionStatus")
    val actionStatus: String,
    @SerializedName("assignedId")
    val assignedId: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("expectedInfoFromClient")
    val expectedInfoFromClient: String,
    @SerializedName("expectedInfoFromTekskills")
    val expectedInfoFromTekskills: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("momId")
    val momId: Int,
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("tekskillsActionItesm")
    val tekskillsActionItesm: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int
): Serializable