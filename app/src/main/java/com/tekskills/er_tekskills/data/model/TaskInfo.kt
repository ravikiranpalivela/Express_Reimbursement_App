package com.tekskills.er_tekskills.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

@Entity(tableName = "taskInfo")
data class TaskInfo(
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var purposeOfVisit: String,
    var date: Date,
    var priority: Int,
    var status: Boolean,
    var category: String,
    var clientContactPerson: String,
    var clientContactPos: String,
    var opportunityDesc: String,
    val TaskID: Int,
    val customerName: String,
    val custmerEmail: String,
    val modeOfTravel: String,
    val customerContactName: String,
    val customerPhone: String,
    val visitDate: String,
    val visitTime: String,
    val visitPurpose: String,
    val source: String,
    val sourceLatitude: String,
    val sourceLongitude: String,
    val destination: String,
    val destinationLatitude: String,
    val destinationLongitude: String,
    val totalDistance: Double,
    val checkInTime: String,
    val checkInCordinates: String,
    val checkOutTime: String,
    val checkOutCordinates: String,
    val mapTime: String,
) : Serializable

