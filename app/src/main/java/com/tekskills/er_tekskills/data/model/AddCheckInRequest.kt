package com.tekskills.er_tekskills.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Ravi Kiran Palivela on 5/8/2024.
 * Description: $
 */
data class AddCheckInRequest(
    @SerializedName("longitude") val longitude: String,
    @SerializedName("latitude") val latitude: String, )