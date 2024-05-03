package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class ProjectsAssignVO(
    @SerializedName("accountHeadId")
    val accountHeadId: String,
    @SerializedName("managementId")
    val managementId: String,
    @SerializedName("practiceHeadId")
    val practiceHeadId: String,
    @SerializedName("projectManagerId")
    val projectManagerId: String
)