package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class AddOpportunityRequest(
    @SerializedName("clientId")
    val clientId: String,
    @SerializedName("projectDetailsVO")
    val projectDetailsVO: ProjectDetailsVO,
    @SerializedName("projectsAssignVO")
    val projectsAssignVO: ProjectsAssignVO
)

