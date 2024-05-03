package com.tekskills.er_tekskills.data.model

import com.google.gson.annotations.SerializedName

data class PendingActionGraphListData(
    @SerializedName("count")
    val count: Int,
    @SerializedName("projectId")
    val projectId: Int,
    @SerializedName("projectName")
    val projectName: String?=""
)
{
    override fun toString(): String {
        return projectName.toString()
    }
}