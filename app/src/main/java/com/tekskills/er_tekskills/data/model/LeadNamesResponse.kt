package com.tekskills.er_tekskills.data.model

import com.google.gson.annotations.SerializedName

class LeadNamesResponse : ArrayList<LeadNamesResponseItem>()

data class LeadNamesResponseItem(
    @SerializedName("leadEmail")
    val leadEmail: String,
    @SerializedName("leadName")
    val leadName: String,
    @SerializedName("id")
    val leadID: Int,
    @SerializedName("leadContact")
    val leadContact: String,
) {
    override fun toString(): String {
        return leadName
    }
}
