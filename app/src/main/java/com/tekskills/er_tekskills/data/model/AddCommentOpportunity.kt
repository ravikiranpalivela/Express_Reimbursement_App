package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class AddCommentOpportunity(
    @SerializedName("assignedId")
    val assignedId: String,
    @SerializedName("comments")
    val comments: String,
    @SerializedName("employeeId")
    val employeeId: String,
    @SerializedName("projectId")
    val projectId: String
)