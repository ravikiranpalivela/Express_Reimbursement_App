package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class ClientDetailsItem(
    @SerializedName("ClientId")
    val clientId: Int,
    @SerializedName("ClientName")
    val clientName: String
)