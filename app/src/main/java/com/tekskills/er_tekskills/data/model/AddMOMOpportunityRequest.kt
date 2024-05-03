package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class AddMOMOpportunityRequest(
    @SerializedName("assignedId")
    var assignedId: String,
    @SerializedName("clientId")
    var clientId: String,
    @SerializedName("meetingDate")
    var meetingDate: String,
    @SerializedName("meetingTime")
    var meetingTime: String,
    @SerializedName("meetingNotes")
    var meetingNotes: String,
    @SerializedName("meetingTitle")
    var meetingTitle: String,
    @SerializedName("projectId")
    var projectId: String
)