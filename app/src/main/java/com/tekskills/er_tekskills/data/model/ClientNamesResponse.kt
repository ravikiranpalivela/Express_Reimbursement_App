package com.tekskills.er_tekskills.data.model

import com.google.gson.annotations.SerializedName

class ClientNamesResponse : ArrayList<ClientNamesResponseItem>()

data class ClientNamesResponseItem(
    @SerializedName("id")
    val clientId: Int,
    @SerializedName("name")
    val clientName: String
) {
    override fun toString(): String {
        return clientName
    }
}
