package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class PlaceDetails(
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("long")
    val long: Double,
)