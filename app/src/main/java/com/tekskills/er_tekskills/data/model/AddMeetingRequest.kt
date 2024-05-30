package com.tekskills.er_tekskills.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Ravi Kiran Palivela on 5/6/2024.
 * Description: $
 */
//data class AddMeetingRequest(
//    @SerializedName("client_name") val clientName: String,
//    @SerializedName("visitPurpose") val visitPurpose: String,
//    @SerializedName("visitDate") val visitDate: String,
//    @SerializedName("noOfDays") val noOfDays: String,
//    @SerializedName("employeeId") val employeeId: Int,
//    @SerializedName("contact") val contact: String,
//    @SerializedName("email_id") val emailID: String,
//    @SerializedName("description") val description: String,
//    @SerializedName("userCordinates") val userCoordinates: UserCoordinates
//)
//
//data class UserCoordinates(
//    @SerializedName("sourceLatitude") val sourceLatitude: String,
//    @SerializedName("sourceLongitude") val sourceLongitude: String,
//    @SerializedName("sourceAddress") val sourceAddress: String,
//    @SerializedName("destinationLatitude") val destinationLatitude: String,
//    @SerializedName("destinationLongitude") val destinationLongitude: String,
//    @SerializedName("destinationAddress") val destinationAddress: String,
//    @SerializedName("totalDistance") val totalDistance: String
//)

data class AddLocationCoordinates(
    @SerializedName("lattitude") val lattitude: String,
    @SerializedName("longitude") val longitude: String,
)

data class AddMeetingRequest(
    @SerializedName("modeOfTravel") val modeOfTravel: String,
    @SerializedName("visitPurpose") val visitPurpose: String,
    @SerializedName("visitDate") val visitDate: String,
    @SerializedName("visitTime") val visitTime: String,
    @SerializedName("employeeId") val employeeId: Int,
    @SerializedName("clientId") val clientId: Int,
    @SerializedName("leadId") val leadId: Int,
    @SerializedName("customerContactName") val customerContactName: String,
    @SerializedName("customerName") val customerName: String,
    @SerializedName("custmerEmail") val customerEmail: String,
    @SerializedName("customerPhone") val customerPhone: String,
//    @SerializedName("description") val description: String,
    @SerializedName("opportunity") val opportunity: String,
    @SerializedName("userCordinates") val userCoordinates: UserCoordinates
)


data class UserCoordinatesRequest(
    @SerializedName("source") val source: String,
    @SerializedName("sourceLatitude") val sourceLatitude: String,
    @SerializedName("sourceLongitude") val sourceLongitude: String,
    @SerializedName("destinantion") val destination: String,
    @SerializedName("destinantionLatitude") val destinationLatitude: String,
    @SerializedName("destinantionLongitude") val destinationLongitude: String,
    @SerializedName("totalDistance") val totalDistance: Double,
    @SerializedName("mapTime") val mapTime: String
)

data class UserCoordinates(
    @SerializedName("source") val source: String,
    @SerializedName("sourceLatitude") val sourceLatitude: String,
    @SerializedName("sourceLongitude") val sourceLongitude: String,
    @SerializedName("destinantion") val destination: String,
    @SerializedName("destinantionLatitude") val destinationLatitude: String,
    @SerializedName("destinantionLongitude") val destinationLongitude: String,
    @SerializedName("totalDistance") val totalDistance: Double,
    @SerializedName("checkInTime") val checkInTime: String,
    @SerializedName("checkInCordinates") val checkInCordinates: String,
    @SerializedName("checkOutTime") val checkOutTime: String,
    @SerializedName("checkOutCordinates") val checkOutCordinates: String,
    @SerializedName("mapTime") val mapTime: String
)
