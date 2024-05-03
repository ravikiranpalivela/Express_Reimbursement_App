package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ClientsEscalationResponseItem(
    @SerializedName("ClientName")
    val clientName: String,
    @SerializedName("EscalationDetails")
    val escalationDetails: EscalationDetails,
    @SerializedName("OppertunityType")
    val oppertunityType: String,
    @SerializedName("ProjectName")
    val projectName: String
) : Serializable