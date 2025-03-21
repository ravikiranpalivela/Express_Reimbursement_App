package com.tekskills.er_tekskills.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
        @SerializedName("access_token")
        val accessToken: String,
        @SerializedName("refresh_token")
        val refreshToken: String
)
