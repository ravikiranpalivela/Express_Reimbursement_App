package com.tekskills.er_tekskills.data.model

import com.google.gson.annotations.SerializedName

class ClientNamesResponse : ArrayList<ClientNamesResponseItem>()

data class ClientNamesResponseItem(
    @SerializedName("clientId")
    val clientId: Int,
    @SerializedName("clientName")
    val clientName: String
) {
    override fun toString(): String {
        return clientName
    }
}
