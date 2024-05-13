package com.tekskills.er_tekskills.data.repository

import com.tekskills.er_tekskills.data.model.ActionItemProjectIDResponse
import com.tekskills.er_tekskills.data.model.ActionItemProjectIDResponseItem
import com.tekskills.er_tekskills.data.model.AddActionItemOpportunityRequest
import com.tekskills.er_tekskills.data.model.AddCommentOpportunity
import com.tekskills.er_tekskills.data.model.AddEscalationRequest
import com.tekskills.er_tekskills.data.model.AddMOMOpportunityRequest
import com.tekskills.er_tekskills.data.model.AddOpportunityRequest
import com.tekskills.er_tekskills.data.model.AddMeetingRequest
import com.tekskills.er_tekskills.data.model.AddTravelExpenceResponse
import com.tekskills.er_tekskills.data.model.ClientsEscalationResponse
import com.tekskills.er_tekskills.data.model.CommentsListResponse
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponse
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.er_tekskills.data.model.MomProjectResponse
import com.tekskills.er_tekskills.data.model.MomProjectResponseItem
import com.tekskills.er_tekskills.data.model.NewClientResponse
import com.tekskills.er_tekskills.data.model.OpportunityByProjectIDResponse
import com.tekskills.er_tekskills.data.model.ProjectListResponse
import com.tekskills.er_tekskills.data.model.ProjectOpportunityResponse
import com.tekskills.er_tekskills.data.model.UserAllowenceResponse
import com.tekskills.er_tekskills.data.remote.ApiHelper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {

    suspend fun userLogin(user: Map<String, String>) = apiHelper.userLogin(user)

    suspend fun getEmployeeMe(authorization: String) = apiHelper.getEmployeeMe(authorization)

    suspend fun getEmployeeAllowences(authorization: String, itemID:String)
    = apiHelper.getEmployeeAllowences(authorization, itemID)

    suspend fun addMeetingPurpose(
        authorization: String, user: AddMeetingRequest
    ) = apiHelper.addMeetingPurpose(authorization, user)


    suspend fun getMeetingPurpose(authorization: String): Response<MeetingPurposeResponse> =
        apiHelper.getMeetingPurpose(authorization)

    suspend fun getMeetingPurposeByID(authorization: String, itemID:String): Response<MeetingPurposeResponseData>
    = apiHelper.getMeetingPurposeByID(authorization, itemID)


    suspend fun addTravelExpense(authorization: String,purposeID: RequestBody, amount: Long,
                                 file: MutableList<MultipartBody.Part>, user: Map<String, RequestBody>
    ): Response<AddTravelExpenceResponse>
    = apiHelper.addTravelExpense(authorization, purposeID, amount,file, user)

    suspend fun addHotelExpense(authorization: String,purposeID: RequestBody,
                                 file: MultipartBody.Part, user: Map<String, RequestBody>
    ): Response<AddTravelExpenceResponse>
            = apiHelper.addHotelExpense(authorization, purposeID,file, user)

    suspend fun addFoodExpense(authorization: String,purposeID: RequestBody,
                               user: Map<String, RequestBody>
    ): Response<AddTravelExpenceResponse>
            = apiHelper.addFoodExpense(authorization, purposeID, user)


    /**
     * Dashboard Graph items
     */
    suspend fun getPendingActionGraph(authorization: String) =
        apiHelper.getPendingActionGraph(authorization)

    suspend fun getEscalationGraph(authorization: String) =
        apiHelper.getEscalationGraph(authorization)

    suspend fun getPendingActionGraphByID(authorization: String, itemID: String) =
        apiHelper.getPendingActionGraphByID(authorization, itemID)

    suspend fun getEscalationGraphByID(authorization: String, itemID: String) =
        apiHelper.getEscalationGraphByID(authorization, itemID)

    /**
     * ADD Opportunity
     */
    suspend fun addOpportunity(authorization: String, requestBody: AddOpportunityRequest) =
        apiHelper.addOpportunity(authorization, requestBody)


    suspend fun getClients(authorization: String) = apiHelper.getClients(authorization)

    suspend fun getProjects(authorization: String) = apiHelper.getProjects(authorization)

    suspend fun getAssignProjects(authorization: String) =
        apiHelper.getAssignProjects(authorization)


    suspend fun getManagementList(authorization: String) =
        apiHelper.getManagementList(authorization)

    suspend fun getAccountManagerList(authorization: String) =
        apiHelper.getAccountManagerList(authorization)

    suspend fun getProjectManagerList(authorization: String) =
        apiHelper.getProjectManagerList(authorization)

    suspend fun getPracticeHeadList(authorization: String) =
        apiHelper.getPracticeHeadList(authorization)

    suspend fun getProjectOpportunity(authorization: String): Response<ProjectOpportunityResponse> =
        apiHelper.getProjectOpportunity(authorization)

    suspend fun getMOMProjects(
        authorization: String,
        itemID: String
    ): Response<MomProjectResponse> =
        apiHelper.getMOMProjects(authorization, itemID)

    suspend fun getMOMActionItemDetailsBYId(
        authorization: String,
        itemID: String
    ): Response<MomProjectResponseItem> =
        apiHelper.getMOMActionItemDetailsBYId(authorization, itemID)

    suspend fun getOpportunityByProductID(
        authorization: String,
        itemID: String
    ): Response<OpportunityByProjectIDResponse> =
        apiHelper.getOpportunityByProductID(authorization, itemID)

    suspend fun getClientExist(authorization: String): Response<NewClientResponse> =
        apiHelper.getClientExist(authorization)

    suspend fun getActionItemProjects(authorization: String): Response<NewClientResponse> =
        apiHelper.getActionItemProjects(authorization)

    suspend fun getActionItemProjectID(
        authorization: String,
        itemID: String
    ): Response<ActionItemProjectIDResponse> =
        apiHelper.getActionItemProjectID(authorization, itemID)

    suspend fun getActionItemBYID(
        authorization: String,
        itemID: String
    ): Response<ActionItemProjectIDResponseItem> =
        apiHelper.getActionItemBYID(authorization, itemID)

    suspend fun getProjectID(authorization: String): Response<ProjectListResponse> =
        apiHelper.getProjectID(authorization)

    suspend fun getEscalationItemProjectID(
        authorization: String,
        itemID: String
    ): Response<ClientsEscalationResponse> =
        apiHelper.getEscalationItemProjectID(authorization, itemID)

    suspend fun getCommentProjectID(
        authorization: String,
        itemID: String
    ): Response<CommentsListResponse> =
        apiHelper.getCommentProjectID(authorization, itemID)

    suspend fun addClient(
        authorization: String,
        user: Map<String, String>
    ) = apiHelper.addClient(authorization, user)

    suspend fun addProject(authorization: String, user: Map<String, String>) =
        apiHelper.addProject(authorization, user)

    suspend fun addEmployee(authorization: String, user: Map<String, String>) =
        apiHelper.addEmployee(authorization, user)

    suspend fun assignProject(authorization: String, user: Map<String, String>) =
        apiHelper.assignProject(authorization, user)

    suspend fun addClientEscalation(authorization: String, user: AddEscalationRequest) =
        apiHelper.addClientEscalation(authorization, user)

    suspend fun addMOMActionItem(authorization: String, user: AddMOMOpportunityRequest) =
        apiHelper.addMOMActionItem(authorization, user)

    suspend fun addActionItemOpportunity(
        authorization: String,
        user: AddActionItemOpportunityRequest
    ) = apiHelper.addActionItemOpportunity(authorization, user)


    suspend fun addCommentOpportunity(
        authorization: String,
        user: AddCommentOpportunity
    ) = apiHelper.addCommentOpportunity(authorization, user)
}