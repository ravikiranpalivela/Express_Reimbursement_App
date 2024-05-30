package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class CommentsListResponseItem(
    @SerializedName("comment")
    val comment: String,
    @SerializedName("createdAt")
    val commentDate: String,
    @SerializedName("empType")
    val empRoleName: String,
    @SerializedName("employeeId")
    val employeeId: Int,
    @SerializedName("empName")
    val employeeName: String,
    @SerializedName("purposeId")
    val projectId: Int
)

//{
//    "createdAt": "2024-05-29T15:15:14.767",
//    "empType": "User",
//    "empName": "prasanth",
//    "purposeId": 1,
//    "comment": "hello sir Reached Destination"
//}