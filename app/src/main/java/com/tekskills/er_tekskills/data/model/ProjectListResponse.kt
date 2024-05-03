package com.tekskills.er_tekskills.data.model

class ProjectListResponse : ArrayList<ProjectListResponseItem>()

data class ProjectListResponseItem(
    val ClientDetailsItem: ClientDetailsItem,
    val opportunityDesc: String,
    val oppotunityType: String,
    val projectId: Int,
    val projectName: String,
    val status: String
)

data class ClientDetails(
    val ClientId: Int,
    val ClientName: String
)