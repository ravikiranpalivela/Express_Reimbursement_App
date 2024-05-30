package com.tekskills.er_tekskills.data.model
import com.google.gson.annotations.SerializedName


data class NewClientResponse(
    @SerializedName("MESSAGE")
    val mESSAGE: String,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("empType")
    val empType: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("purchaseId")
    val purchaseId: Int,
    @SerializedName("tenantId")
    val tenantId: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int
)

