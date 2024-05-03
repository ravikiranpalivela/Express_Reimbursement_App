package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class EscalationDetails(
    @SerializedName("clientEscalations")
    val clientEscalations: Boolean,
    @SerializedName("clientId")
    val clientId: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("escalationDescription")
    val escalationDescription: String,
    @SerializedName("escalationRaisedDate")
    val escalationRaisedDate: String,
    @SerializedName("escalationResolvedDate")
    val escalationResolvedDate: String,
    @SerializedName("escalationStatus")
    val escalationStatus: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int
)