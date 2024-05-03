package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PendingActionItemGraphByIDResponseItem(
    @SerializedName("complitionDate")
    val complitionDate: String,
    @SerializedName("expFromClient")
    val expFromClient: String,
    @SerializedName("expFromTek")
    val expFromTek: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("tekActionItem")
    val tekActionItem: String
): Serializable