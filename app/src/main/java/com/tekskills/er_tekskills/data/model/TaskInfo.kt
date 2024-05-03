package com.tekskills.er_tekskills.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "taskInfo")
data class TaskInfo(
    @PrimaryKey(autoGenerate = false)
    var id : Int,
    var description : String,
    var date : Date,
    var priority : Int,
    var status : Boolean,
    var category: String,
    var clientContactPerson: String,
    var clientContactPos: String,
    var opportunityDesc: String,
) : Serializable

