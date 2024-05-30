package com.tekskills.er_tekskills.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Ravi Kiran Palivela on 5/8/2024.
 * Description: $
 */
data class AddMOMRequest(
    @SerializedName("purposeId") val purposeId: Int,
    @SerializedName("meetingNotes") val meetingNotes: String,
    @SerializedName("infoFromClient") val infoFromClient: String,
    @SerializedName("targetDate") val targetDate: String,
    )