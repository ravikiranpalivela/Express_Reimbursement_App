package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName
import java.util.Date

data class AddActionItemOpportunityRequest(
    @SerializedName("assignedId")
    var assignedId: String,
    @SerializedName("momId")
    var momId: String,
    @SerializedName("actionItemCompletionDate")
    var actionItemCompletionDate: String,
    @SerializedName("tekskillsActionItesm")
    var tekskillsActionItesm: String,
    @SerializedName("expectedInfoFromClient")
    var expectedInfoFromClient: String,
    @SerializedName("expectedInfoFromTekskills")
    var expectedInfoFromTekskills: String,
    @SerializedName("projectId")
    var projectId: String,
    @SerializedName("actionStatus")
    var actionStatus: String,
    @SerializedName("date")
    var date: Date,
    )