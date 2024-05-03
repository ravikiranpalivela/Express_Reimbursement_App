package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class ProjectOpportunityResponseItem(
    @SerializedName("ClientDetails")
    val clientDetailsItem: ClientDetailsItem,
    @SerializedName("opportunityDesc")
    val opportunityDesc: String,
    @SerializedName("oppotunityType")
    val oppotunityType: String,
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("projectName")
    val projectName: String,
    @SerializedName("status")
    var status: String
)