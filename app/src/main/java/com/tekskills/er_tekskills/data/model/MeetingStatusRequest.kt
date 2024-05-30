package com.tekskills.er_tekskills.data.model

import com.google.gson.annotations.SerializedName

data class MeetingStatusRequest(
    @SerializedName("approveCount")
    val approveCount: Int,
    @SerializedName("pendingCount")
    val pendingCount: Int,
    @SerializedName("rejectCount")
    val rejectCount: Int
)