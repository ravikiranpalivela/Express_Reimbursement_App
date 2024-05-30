package com.tekskills.er_tekskills.data.repository

import com.tekskills.er_tekskills.data.model.AddLocationCoordinates
import com.tekskills.er_tekskills.data.remote.ApiHelper
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {

    suspend fun addUserCoordinates(
        authorization: String,
        user: AddLocationCoordinates
    ) =apiHelper.addUserCoordinates(authorization, user)

}