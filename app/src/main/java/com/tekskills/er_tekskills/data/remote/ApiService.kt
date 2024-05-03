package com.tekskills.er_tekskills.data.remote

import com.tekskills.er_tekskills.data.model.AccountHeadResponse
import com.tekskills.er_tekskills.data.model.ActionItemProjectIDResponse
import com.tekskills.er_tekskills.data.model.ActionItemProjectIDResponseItem
import com.tekskills.er_tekskills.data.model.AddActionItemOpportunityRequest
import com.tekskills.er_tekskills.data.model.AddCommentOpportunity
import com.tekskills.er_tekskills.data.model.AddEscalationRequest
import com.tekskills.er_tekskills.data.model.AddMOMOpportunityRequest
import com.tekskills.er_tekskills.data.model.AddOpportunityRequest
import com.tekskills.er_tekskills.data.model.AddPurposeMeetingRequest
import com.tekskills.er_tekskills.data.model.AddPurposeMeetingResponse
import com.tekskills.er_tekskills.data.model.AssignProjectListResponse
import com.tekskills.er_tekskills.data.model.ClientEscalationGraphByIDResponse
import com.tekskills.er_tekskills.data.model.PendingActionGraphResponse
import com.tekskills.er_tekskills.data.model.ClientNamesResponse
import com.tekskills.er_tekskills.data.model.ClientsEscalationResponse
import com.tekskills.er_tekskills.data.model.CommentsListResponse
import com.tekskills.er_tekskills.data.model.LoginResponse
import com.tekskills.er_tekskills.data.model.MomProjectResponse
import com.tekskills.er_tekskills.data.model.NewClientResponse
import com.tekskills.er_tekskills.data.model.ProjectListResponse
import com.tekskills.er_tekskills.data.model.ManagementResponse
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponse
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.er_tekskills.data.model.MomProjectResponseItem
import com.tekskills.er_tekskills.data.model.OpportunityByProjectIDResponse
import com.tekskills.er_tekskills.data.model.PendingActionItemGraphByIDResponse
import com.tekskills.er_tekskills.data.model.PendingEscalationGraphResponse
import com.tekskills.er_tekskills.data.model.PracticeHeadResponse
import com.tekskills.er_tekskills.data.model.ProjectManagerResponse
import com.tekskills.er_tekskills.data.model.ProjectOpportunityResponse
import com.tekskills.er_tekskills.data.model.UserMeResponse
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_ACCOUNT_MANAGER
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_ACTION_ITEM_BY_ID
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_ACTION_ITEM_PROJECT
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_ACTION_ITEM_PROJECT_ID
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_ASSIGN_PROJECTS
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_CLIENTS
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_CLIENT_ESCALATION_BY_ID
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_CLIENT_ESCALATION_GRAPH_BY_ID
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_CLIENT_EXIST
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_COMMENT_BY_ID
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_ME
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_MEETING_PURPOSE
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_MOM_ACTION_ITEM_BY_ID
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_PENDING_ACTION_GRAPH
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_MOM_PROJECT
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_OPPORTUNITY_BY_PROJECT_ID
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_PENDING_ACTION_GRAPH_BY_ID
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_PENDING_ESCALATION_GRAPH
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_PRACTICE_MANAGER
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_PROJECTS
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_PROJECT_ID
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_PROJECT_MANAGER
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.GET_SRMANAGEMENT
import com.tekskills.er_tekskills.data.remote.APIEndPoint.Companion.POST_ADD_OPPORTUNITY
import com.tekskills.er_tekskills.utils.Common.Companion.APP_JSON
import com.tekskills.er_tekskills.utils.Common.Companion.AUTHORIZATION
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {


    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_USER_LOGIN)
    suspend fun userLogin(
        @Body user: Map<String, String>
    ): Response<LoginResponse>

    @GET(GET_ME)
    suspend fun getEmployeeMe(@Header(AUTHORIZATION) authorization: String): Response<UserMeResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_MEETING_PURPOSE)
    suspend fun addMeetingPurpose(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: AddPurposeMeetingRequest
    ): Response<AddPurposeMeetingResponse>

    @GET(GET_MEETING_PURPOSE)
    suspend fun getMeetingPurpose(@Header(AUTHORIZATION) authorization: String): Response<MeetingPurposeResponse>

    @GET("${GET_MEETING_PURPOSE}/{id}")
    suspend fun getMeetingPurposeByID(@Header(AUTHORIZATION) authorization: String,@Path("id") itemID:String): Response<MeetingPurposeResponseData>

    /**
     * Dashboard Graph items
     */
    @GET(GET_PENDING_ACTION_GRAPH)
    suspend fun getPendingActionGraph(@Header(AUTHORIZATION) authorization: String): Response<PendingActionGraphResponse>

    @GET(GET_PENDING_ESCALATION_GRAPH)
    suspend fun getEscalationGraph(@Header(AUTHORIZATION) authorization: String): Response<PendingEscalationGraphResponse>

    @GET("${GET_PENDING_ACTION_GRAPH_BY_ID}/{id}")
    suspend fun getPendingActionGraphByID(@Header(AUTHORIZATION) authorization: String,@Path("id") itemID:String): Response<PendingActionItemGraphByIDResponse>

    @GET("${GET_CLIENT_ESCALATION_GRAPH_BY_ID}/{id}")
    suspend fun getEscalationGraphByID(@Header(AUTHORIZATION) authorization: String,@Path("id") itemID:String): Response<ClientEscalationGraphByIDResponse>

    /**
     * ADD Opportunity
     */
    @POST(POST_ADD_OPPORTUNITY)
    suspend fun addOpportunity(@Header(AUTHORIZATION) authorization: String,@Body requestBody: AddOpportunityRequest): Response<NewClientResponse>

    @GET(GET_CLIENTS)
    suspend fun getClients(@Header(AUTHORIZATION) authorization: String): Response<ClientNamesResponse>

    @GET(GET_PROJECTS)
    suspend fun getProjects(@Header(AUTHORIZATION) authorization: String): Response<ProjectListResponse>

    @GET(GET_ASSIGN_PROJECTS)
    suspend fun getAssignProjects(@Header(AUTHORIZATION) authorization: String): Response<AssignProjectListResponse>

    @GET(GET_SRMANAGEMENT)
    suspend fun getManagementList(@Header(AUTHORIZATION) authorization: String): Response<ManagementResponse>

    @GET(GET_ACCOUNT_MANAGER)
    suspend fun getAccountManagerList(@Header(AUTHORIZATION) authorization: String): Response<AccountHeadResponse>

    @GET(GET_PROJECT_MANAGER)
    suspend fun getProjectManagerList(@Header(AUTHORIZATION) authorization: String): Response<ProjectManagerResponse>

    @GET(GET_PRACTICE_MANAGER)
    suspend fun getPracticeHeadList(@Header(AUTHORIZATION) authorization: String): Response<PracticeHeadResponse>

    @GET("${GET_MOM_PROJECT}/{id}")
    suspend fun getMOMProjects(@Header(AUTHORIZATION) authorization: String,@Path("id") itemID:String): Response<MomProjectResponse>

    @GET("${GET_MOM_ACTION_ITEM_BY_ID}/{id}")
    suspend fun getMOMActionItemDetailsBYId(@Header(AUTHORIZATION) authorization: String,@Path("id") itemID:String): Response<MomProjectResponseItem>

    @GET(GET_CLIENT_EXIST)
    suspend fun getClientExist(@Header(AUTHORIZATION) authorization: String): Response<NewClientResponse>

    @GET(GET_ACTION_ITEM_PROJECT)
    suspend fun getActionItemProjects(@Header(AUTHORIZATION) authorization: String): Response<NewClientResponse>

    @GET("${GET_ACTION_ITEM_PROJECT_ID}/{id}")
    suspend fun getActionItemProjectID(@Header(AUTHORIZATION) authorization: String,@Path("id") itemID:String): Response<ActionItemProjectIDResponse>

    @GET("${GET_ACTION_ITEM_BY_ID}/{id}")
    suspend fun getActionItemBYID(@Header(AUTHORIZATION) authorization: String,@Path("id") itemID:String): Response<ActionItemProjectIDResponseItem>

    @GET("${GET_CLIENT_ESCALATION_BY_ID}/{id}")
    suspend fun getEscalationItemProjectID(@Header(AUTHORIZATION) authorization: String,@Path("id") itemID:String): Response<ClientsEscalationResponse>

    @GET("${GET_COMMENT_BY_ID}/{id}")
    suspend fun getCommentProjectID(@Header(AUTHORIZATION) authorization: String,@Path("id") itemID:String): Response<CommentsListResponse>

    @GET("${GET_OPPORTUNITY_BY_PROJECT_ID}/{id}")
    suspend fun getOpportunityByProductID(@Header(AUTHORIZATION) authorization: String,@Path("id") itemID:String): Response<OpportunityByProjectIDResponse>

    @GET(GET_PROJECT_ID)
    suspend fun getProjectID(@Header(AUTHORIZATION) authorization: String): Response<ProjectListResponse>

    @GET(GET_PROJECT_ID)
    suspend fun getProjectOpportunity(@Header(AUTHORIZATION) authorization: String): Response<ProjectOpportunityResponse>



    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_CLIENT)
    suspend fun addClient(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: Map<String, String>
    ): Response<NewClientResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_PROJECT)
    suspend fun addProject(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: Map<String, String>
    ): Response<NewClientResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_EMPLOYEE)
    suspend fun addEmployee(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: Map<String, String>
    ): Response<NewClientResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ASSIGN_PROJECT)
    suspend fun assignProject(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: Map<String, String>
    ): Response<NewClientResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_CLIENT_ESCALATION)
    suspend fun addClientEscalation(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: AddEscalationRequest
    ): Response<NewClientResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_MOM)
    suspend fun addMOMActionItem(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: AddMOMOpportunityRequest
    ): Response<NewClientResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_Action_Item)
    suspend fun addActionItemOpportunity(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: AddActionItemOpportunityRequest
    ): Response<NewClientResponse>

    @Headers(APP_JSON)
    @POST(APIEndPoint.POST_ADD_COMMENT)
    suspend fun addCommentOpportunity(
        @Header(AUTHORIZATION) authorization: String,
        @Body user: AddCommentOpportunity
    ): Response<NewClientResponse>

}