package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class AddEscalationRequest(
    @SerializedName("clientEscalations")
    var clientEscalations: Boolean,
    @SerializedName("clientId")
    var clientId: String,
    @SerializedName("escalationDescription")
    var escalationDescription: String,
    @SerializedName("escalationRaisedDate")
    var escalationRaisedDate: String,
    @SerializedName("escalationResolvedDate")
    var escalationResolvedDate: String,
    @SerializedName("escalationStatus")
    var escalationStatus: String,
    @SerializedName("projectId")
    var projectId: String
)