package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

class PendingActionItemData : ArrayList<PendingActionItemGraphByIDResponseItem>()

data class PendingActionItemGraphByIDResponse(
    @SerializedName("pendingActionItem")
    val pendingActionItemGraphByIDResponseItem: PendingActionItemData,
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("projectName")
    val projectName: String,
    @SerializedName("projectStatus")
    val projectStatus: String
)