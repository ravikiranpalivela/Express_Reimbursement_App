package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class SRManagementResponseItem(
    @SerializedName("SrManagerId")
    val srManagerId: Int,
    @SerializedName("SrManagerName")
    val srManagerName: String
)
{
    override fun toString(): String {
        return srManagerName
    }
}