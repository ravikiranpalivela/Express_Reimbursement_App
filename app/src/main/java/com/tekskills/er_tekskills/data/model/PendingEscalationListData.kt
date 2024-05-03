package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class PendingEscalationListData(
    @SerializedName("clientId")
    val clientId: Int,
    @SerializedName("clientName")
    val clientName: String,
    @SerializedName("count")
    val count: Int
)
{
    override fun toString(): String {
        return clientName
    }
}