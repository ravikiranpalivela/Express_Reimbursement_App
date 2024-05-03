package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class ProjectDetailsVO(
    @SerializedName("opportunityDesc")
    val opportunityDesc: String,
    @SerializedName("oppotunityType")
    val oppotunityType: String,
    @SerializedName("projectName")
    val projectName: String,
    @SerializedName("status")
    val status: String
)