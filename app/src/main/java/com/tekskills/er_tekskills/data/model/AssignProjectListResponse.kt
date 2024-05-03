package com.tekskills.er_tekskills.data.model
import com.google.gson.annotations.SerializedName

class AssignProjectListResponse : ArrayList<AssignProjectListResponseItem>()

data class AssignProjectListResponseItem(
    @SerializedName("AccountHead")
    val accountHead: AccountHead,
    @SerializedName("ManagementDetails")
    val managementDetails: List<ManagementDetail>,
    @SerializedName("PracticeHead")
    val practiceHead: PracticeHead,
    @SerializedName("ProjectDetails")
    val projectDetails: ProjectDetails,
    @SerializedName("ProjectManager")
    val projectManager: ProjectManager
)

data class AccountHead(
    @SerializedName("AccountHeadId")
    val accountHeadId: Int,
    @SerializedName("AccountHeadName")
    val accountHeadName: String
)

data class ManagementDetail(
    @SerializedName("ManagementId")
    val managementId: Int,
    @SerializedName("ManagementNameName")
    val managementNameName: String
)

data class PracticeHead(
    @SerializedName("PracticeHeadId")
    val practiceHeadId: Int,
    @SerializedName("PracticeHeadName")
    val practiceHeadName: String
)

data class ProjectDetails(
    @SerializedName("ProjectId")
    val projectId: Int,
    @SerializedName("ProjectName")
    val projectName: String
)

data class ProjectManager(
    @SerializedName("ProjectManagerId")
    val projectManagerId: Int,
    @SerializedName("ProjectManagerName")
    val projectManagerName: String
)