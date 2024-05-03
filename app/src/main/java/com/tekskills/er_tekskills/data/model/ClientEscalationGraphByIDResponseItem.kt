package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ClientEscalationGraphByIDResponseItem(
    @SerializedName("clientId")
    val clientId: Int,
    @SerializedName("clientName")
    val clientName: String,
    @SerializedName("escDesc")
    val escDesc: String,
    @SerializedName("escRaiseDate")
    val escRaiseDate: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("projectName")
    val projectName: String
): Serializable