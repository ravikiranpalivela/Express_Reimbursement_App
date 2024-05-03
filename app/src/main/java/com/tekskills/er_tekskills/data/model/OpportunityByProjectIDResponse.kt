package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OpportunityByProjectIDResponse(
    @SerializedName("clientDetails")
    val clientDetails: ClientsDetails,
    @SerializedName("clientEscalations")
    val clientEscalations: String,
    @SerializedName("opportunityStatus")
    val opportunityStatus: String,
    @SerializedName("opportunityType")
    val opportunityType: String,
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("AssignId")
    val AssignId: Int,
    @SerializedName("projectName")
    val projectName: String
): Serializable

