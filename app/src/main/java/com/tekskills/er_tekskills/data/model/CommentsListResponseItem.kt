package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class CommentsListResponseItem(
    @SerializedName("comment")
    val comment: String,
    @SerializedName("commentDate")
    val commentDate: String,
    @SerializedName("empRoleName")
    val empRoleName: String,
    @SerializedName("employeeId")
    val employeeId: Int,
    @SerializedName("employeeName")
    val employeeName: String,
    @SerializedName("projectId")
    val projectId: Int
)