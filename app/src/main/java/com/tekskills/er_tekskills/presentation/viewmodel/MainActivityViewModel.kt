package com.tekskills.er_tekskills.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
//import com.google.android.libraries.places.api.model.Place
import com.tekskills.er_tekskills.data.model.AccountHeadResponse
import com.tekskills.er_tekskills.data.model.ActionItemProjectIDResponse
import com.tekskills.er_tekskills.data.model.ActionItemProjectIDResponseItem
import com.tekskills.er_tekskills.data.model.AddActionItemOpportunityRequest
import com.tekskills.er_tekskills.data.model.AddCheckInRequest
import com.tekskills.er_tekskills.data.model.AddCommentOpportunity
import com.tekskills.er_tekskills.data.model.AddEscalationRequest
import com.tekskills.er_tekskills.data.model.AddFoodExpenceRequest
import com.tekskills.er_tekskills.data.model.AddHotelExpenceRequest
import com.tekskills.er_tekskills.data.model.AddLocationCoordinates
import com.tekskills.er_tekskills.data.model.AddMOMOpportunityRequest
import com.tekskills.er_tekskills.data.model.AddMOMRequest
import com.tekskills.er_tekskills.data.model.AddMOMResponse
import com.tekskills.er_tekskills.data.model.AddMeetingRequest
import com.tekskills.er_tekskills.data.model.AddOpportunityRequest
import com.tekskills.er_tekskills.data.model.AddPurposeMeetingResponse
import com.tekskills.er_tekskills.data.model.AddReturnTravelExpenceRequest
import com.tekskills.er_tekskills.data.model.AddTravelExpenceRequest
import com.tekskills.er_tekskills.data.model.AddTravelExpenceResponse
import com.tekskills.er_tekskills.data.model.AssignProjectListResponse
import com.tekskills.er_tekskills.data.model.ClientEscalationGraphByIDResponse
import com.tekskills.er_tekskills.data.model.ClientNamesResponse
import com.tekskills.er_tekskills.data.model.ClientsEscalationResponse
import com.tekskills.er_tekskills.data.model.CommentsListResponse
import com.tekskills.er_tekskills.data.model.LeadNamesResponse
import com.tekskills.er_tekskills.data.model.LocationResponse
import com.tekskills.er_tekskills.data.model.LoginResponse
import com.tekskills.er_tekskills.data.model.ManagementResponse
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponse
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.er_tekskills.data.model.MeetingStatusRequest
import com.tekskills.er_tekskills.data.model.MomProjectResponse
import com.tekskills.er_tekskills.data.model.MomProjectResponseItem
import com.tekskills.er_tekskills.data.model.NewClientResponse
import com.tekskills.er_tekskills.data.model.OpportunityByProjectIDResponse
import com.tekskills.er_tekskills.data.model.PendingActionGraphResponse
import com.tekskills.er_tekskills.data.model.PendingActionItemGraphByIDResponse
import com.tekskills.er_tekskills.data.model.PendingEscalationGraphResponse
import com.tekskills.er_tekskills.data.model.PlaceDetails
import com.tekskills.er_tekskills.data.model.PracticeHeadResponse
import com.tekskills.er_tekskills.data.model.ProjectDetailsVO
import com.tekskills.er_tekskills.data.model.ProjectListResponse
import com.tekskills.er_tekskills.data.model.ProjectManagerResponse
import com.tekskills.er_tekskills.data.model.ProjectOpportunityResponse
import com.tekskills.er_tekskills.data.model.ProjectsAssignVO
import com.tekskills.er_tekskills.data.model.TaskInfo
import com.tekskills.er_tekskills.data.model.UserAllowenceResponse
import com.tekskills.er_tekskills.data.model.UserMeResponse
import com.tekskills.er_tekskills.data.repository.MainRepository
import com.tekskills.er_tekskills.domain.TaskCategoryRepository
import com.tekskills.er_tekskills.utils.AppUtil.utlIsNetworkAvailable
import com.tekskills.er_tekskills.utils.Common
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_DEFAULT
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_EMP_ID
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_FIRST_TIME
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_REFRESH_TOKEN
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_TOKEN
import com.tekskills.er_tekskills.utils.SuccessResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val repository: TaskCategoryRepository,
    private val mainRepository: MainRepository
) : ViewModel() {

    val _loading: MutableLiveData<Int> = MutableLiveData<Int>()
    val loading: LiveData<Int> get() = _loading

    val _networkLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val networkLiveData: LiveData<Boolean> get() = _networkLiveData

    private val _resClientNameList = MutableLiveData<SuccessResource<ClientNamesResponse>>()
    val resClientNameList: LiveData<SuccessResource<ClientNamesResponse>>
        get() = _resClientNameList

    private val _resLeadNameList = MutableLiveData<SuccessResource<LeadNamesResponse>>()
    val resLeadNameList: LiveData<SuccessResource<LeadNamesResponse>>
        get() = _resLeadNameList

    private val _resProjectList = MutableLiveData<SuccessResource<ProjectListResponse>>()
    val resProjectList: LiveData<SuccessResource<ProjectListResponse>>
        get() = _resProjectList

    private val _resEmployeeMe = MutableLiveData<SuccessResource<UserMeResponse>>()

    val resEmployeeMe: LiveData<SuccessResource<UserMeResponse>>
        get() = _resEmployeeMe

    private val _resEmployeeAllowence = MutableLiveData<SuccessResource<UserAllowenceResponse>>()

    val resEmployeeAllowence: LiveData<SuccessResource<UserAllowenceResponse>>
        get() = _resEmployeeAllowence

    private val _resEscalationList = MutableLiveData<SuccessResource<ClientsEscalationResponse>>()

    val resEscalationList: LiveData<SuccessResource<ClientsEscalationResponse>>
        get() = _resEscalationList

    private val _resMomActionItemsList = MutableLiveData<SuccessResource<MomProjectResponse>>()

    val resMomActionItemsList: LiveData<SuccessResource<MomProjectResponse>>
        get() = _resMomActionItemsList


    private val _resMomActionItemBYID = MutableLiveData<SuccessResource<MomProjectResponseItem>>()

    val resMomActionItemBYID: LiveData<SuccessResource<MomProjectResponseItem>>
        get() = _resMomActionItemBYID

    private val _resOpportunityByProjectIDItems =
        MutableLiveData<SuccessResource<OpportunityByProjectIDResponse>>()

    val resOpportunityByProjectIDItems: LiveData<SuccessResource<OpportunityByProjectIDResponse>>
        get() = _resOpportunityByProjectIDItems


    private val _resActionItemsList =
        MutableLiveData<SuccessResource<ActionItemProjectIDResponse>>()

    val resActionItemsList: LiveData<SuccessResource<ActionItemProjectIDResponse>>
        get() = _resActionItemsList

    private val _resActionItemBYID =
        MutableLiveData<SuccessResource<ActionItemProjectIDResponseItem>>()

    val resActionItemBYID: LiveData<SuccessResource<ActionItemProjectIDResponseItem>>
        get() = _resActionItemBYID


    private val _resCommentResponse = MutableLiveData<SuccessResource<CommentsListResponse>>()

    val resCommentResponse: LiveData<SuccessResource<CommentsListResponse>>
        get() = _resCommentResponse

    private val _resAssignedProjectList =
        MutableLiveData<SuccessResource<AssignProjectListResponse>>()

    val resAssignedProjectList: LiveData<SuccessResource<AssignProjectListResponse>>
        get() = _resAssignedProjectList

    private val _resManagementList =
        MutableLiveData<SuccessResource<ManagementResponse>>()

    val resManagementList: LiveData<SuccessResource<ManagementResponse>>
        get() = _resManagementList

    private val _resAccountHeadList =
        MutableLiveData<SuccessResource<AccountHeadResponse>>()

    val resAccountHeadList: LiveData<SuccessResource<AccountHeadResponse>>
        get() = _resAccountHeadList

    private val _resProjectManagerList =
        MutableLiveData<SuccessResource<ProjectManagerResponse>>()

    val resProjectManagerList: LiveData<SuccessResource<ProjectManagerResponse>>
        get() = _resProjectManagerList

    private val _resPracticeHeadList =
        MutableLiveData<SuccessResource<PracticeHeadResponse>>()

    val resPracticeHeadList: LiveData<SuccessResource<PracticeHeadResponse>>
        get() = _resPracticeHeadList

    private val _resLoginResponse = MutableLiveData<SuccessResource<LoginResponse>>()

    val resLoginResponse: LiveData<SuccessResource<LoginResponse>>
        get() = _resLoginResponse

    private val _resNewClientResponse = MutableLiveData<SuccessResource<NewClientResponse>>()

    val resNewClientResponse: LiveData<SuccessResource<NewClientResponse>>
        get() = _resNewClientResponse

    private val _resNewCommentResponse = MutableLiveData<SuccessResource<NewClientResponse>>()

    val resNewCommentResponse: LiveData<SuccessResource<NewClientResponse>>
        get() = _resNewCommentResponse

    var _resMeetingPurposeList =
        MutableLiveData<SuccessResource<MeetingPurposeResponse>>()

    val resMeetingPurposeList: LiveData<SuccessResource<MeetingPurposeResponse>>
        get() = _resMeetingPurposeList

    var _resMeetingPurposeIDItems =
        MutableLiveData<SuccessResource<MeetingPurposeResponseData>>()

    val resMeetingPurposeIDItems: LiveData<SuccessResource<MeetingPurposeResponseData>>
        get() = _resMeetingPurposeIDItems

    var _resMeetingPurposeStatusItems =
        MutableLiveData<SuccessResource<MeetingPurposeResponse>>()

    val resMeetingPurposeStatusItems: LiveData<SuccessResource<MeetingPurposeResponse>>
        get() = _resMeetingPurposeStatusItems


    var _resMeetingPurposeStatusDetails =
        MutableLiveData<SuccessResource<MeetingStatusRequest>>()

    val resMeetingPurposeStatusDetails: LiveData<SuccessResource<MeetingStatusRequest>>
        get() = _resMeetingPurposeStatusDetails

    var _resAddTravelExpence =
        MutableLiveData<SuccessResource<AddTravelExpenceResponse>>()

    val resAddTravelExpence: LiveData<SuccessResource<AddTravelExpenceResponse>>
        get() = _resAddTravelExpence

    var _resAddHotelExpence =
        MutableLiveData<SuccessResource<AddTravelExpenceResponse>>()

    val resAddHotelExpence: LiveData<SuccessResource<AddTravelExpenceResponse>>
        get() = _resAddHotelExpence

    var _resAddFoodExpence =
        MutableLiveData<SuccessResource<AddTravelExpenceResponse>>()

    val resAddFoodExpence: LiveData<SuccessResource<AddTravelExpenceResponse>>
        get() = _resAddFoodExpence

    var _resAddMOMToMeetingExpence =
        MutableLiveData<SuccessResource<AddMOMResponse>>()

    val resAddMOMToMeetingExpence: LiveData<SuccessResource<AddMOMResponse>>
        get() = _resAddMOMToMeetingExpence


    private val _dropOffPlaceAddress = MutableLiveData<String?>()
    val dropOffPlaceAddress: LiveData<String?>
        get() = _dropOffPlaceAddress

    private val _pickupPlaceAddress = MutableLiveData<String?>()
    val pickupPlaceAddress: LiveData<String?>
        get() = _pickupPlaceAddress

    private val _priceInVNDString = MutableLiveData<String?>()
    val priceInVNDString: LiveData<String?>
        get() = _priceInVNDString

    private val _distanceInKmString = MutableLiveData<Double?>()
    val distanceInKmString: LiveData<Double?>
        get() = _distanceInKmString

    private val _transportationType = MutableLiveData<String?>()
    val transportationType: LiveData<String?>
        get() = _transportationType

    private val _bookBtnPressed = MutableLiveData<Boolean?>()
    val bookBtnPressed: LiveData<Boolean?>
        get() = _bookBtnPressed

    private val _cancelBookingBtnPressed = MutableLiveData<Boolean?>()
    val cancelBookingBtnPressed: LiveData<Boolean?>
        get() = _cancelBookingBtnPressed

    private val _customerSelectedDropOffPlace = MutableLiveData<PlaceDetails?>()
    val customerSelectedDropOffPlace: LiveData<PlaceDetails?>
        get() = _customerSelectedDropOffPlace

    private val _customerSelectedPickupPlace = MutableLiveData<PlaceDetails?>()
    val customerSelectedPickupPlace: LiveData<PlaceDetails?>
        get() = _customerSelectedPickupPlace

    fun setDistanceInKmString(distanceInKmString: Double?) {
        _distanceInKmString.value = distanceInKmString
    }

    fun setPriceInVNDString(priceInVNDString: String?) {
        _priceInVNDString.value = priceInVNDString
    }

    fun setDropOffPlaceString(dropOffPlaceString: String?) {
        _dropOffPlaceAddress.value = dropOffPlaceString
    }

    fun setPickupPlaceString(pickupPlaceString: String?) {
        _pickupPlaceAddress.value = pickupPlaceString
    }

    fun setTransportationType(transportationType: String?) {
        _transportationType.value = transportationType
    }

    fun setCustomerSelectedDropOffPlace(customerSelectedDropOffPlace: PlaceDetails?) {
        _customerSelectedDropOffPlace.value = customerSelectedDropOffPlace
    }

    fun setCustomerSelectedPickupPlace(customerSelectedPickupPlace: PlaceDetails?) {
        _customerSelectedPickupPlace.value = customerSelectedPickupPlace
    }

    fun setBookBtnPressed(bookBtnPressed: Boolean?) {
        _bookBtnPressed.value = bookBtnPressed
    }

    fun setCancelBookingBtnPressed(cancelBookingBtnPressed: Boolean?) {
        _cancelBookingBtnPressed.value = cancelBookingBtnPressed
    }

    val _resNewMeetingPurpose =
        MutableLiveData<SuccessResource<AddPurposeMeetingResponse>>()

    val resNewMeetingPurpose: LiveData<SuccessResource<AddPurposeMeetingResponse>>
        get() = _resNewMeetingPurpose


    private val _resAddUserCoordinates =
        MutableLiveData<SuccessResource<LocationResponse>>()

    val resAddUserCoordinates: LiveData<SuccessResource<LocationResponse>>
        get() = _resAddUserCoordinates

    private val _resUserMeetingCheckIN =
        MutableLiveData<SuccessResource<LocationResponse>>()

    val resUserMeetingCheckIN: LiveData<SuccessResource<LocationResponse>>
        get() = _resUserMeetingCheckIN

    private val _resUserMeetingCheckOUT =
        MutableLiveData<SuccessResource<LocationResponse>>()

    val resUserMeetingCheckOUT: LiveData<SuccessResource<LocationResponse>>
        get() = _resUserMeetingCheckOUT

    fun insertOrUpdateTaskInfo(taskInfo: TaskInfo) = viewModelScope.launch {
        repository.insertOrUpdateTaskInfo(taskInfo)
    }

    /**
     * Dashboard Graph items
     */

    private val _resPendingActionGraphList =
        MutableLiveData<SuccessResource<PendingActionGraphResponse>>()

    val resPendingActionGraphList: LiveData<SuccessResource<PendingActionGraphResponse>>
        get() = _resPendingActionGraphList

    private val _resEscalationGraphList =
        MutableLiveData<SuccessResource<PendingEscalationGraphResponse>>()

    val resEscalationGraphList: LiveData<SuccessResource<PendingEscalationGraphResponse>>
        get() = _resEscalationGraphList

    private val _resPendingActionGraphByIDList =
        MutableLiveData<SuccessResource<PendingActionItemGraphByIDResponse>>()
    val resPendingActionGraphByIDList: LiveData<SuccessResource<PendingActionItemGraphByIDResponse>>
        get() = _resPendingActionGraphByIDList

    private val _resEscalationGraphByIDList =
        MutableLiveData<SuccessResource<ClientEscalationGraphByIDResponse>>()

    val resEscalationGraphByIDList: LiveData<SuccessResource<ClientEscalationGraphByIDResponse>>
        get() = _resEscalationGraphByIDList

    fun getClientNameList() = viewModelScope.launch {
        try {
            _resClientNameList.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.getClients("Bearer ${checkIfUserLogin()}").let {
                    if (it.isSuccessful) {
                        _resClientNameList.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resClientNameList.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                }
        } catch (e: Exception) {
            Timber.tag("TAG").e("Exception occur at getClients ${e.message}")
            _loading.value = View.GONE
        } finally {
        }
    }

    fun getLeadsNameList(clientName: String) = viewModelScope.launch {
        try {
            _resLeadNameList.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.getLeads("Bearer ${checkIfUserLogin()}", clientName).let {
                    if (it.isSuccessful) {
                        _resLeadNameList.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resLeadNameList.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                }
        } catch (e: Exception) {
            Timber.tag("TAG").e("Exception occur at getClients ${e.message}")
            _loading.value = View.GONE
        } finally {
        }
    }

    fun getMeetingPurpose(authorization: String) =
        viewModelScope.launch {
            try {
                _resMeetingPurposeList.postValue(SuccessResource.loading(null))
                if (utlIsNetworkAvailable())
                    mainRepository.getMeetingPurpose("Bearer ${checkIfUserLogin()}").let {
                        if (it.isSuccessful) {
                            _resMeetingPurposeList.postValue(SuccessResource.success(it.body()))
                        } else {
                            _resMeetingPurposeList.postValue(
                                SuccessResource.error(
                                    it.errorBody().toString(),
                                    null
                                )
                            )
                        }
                    }
            } catch (e: Exception) {
                Timber.tag("TAG").e("Exception occur at getClients ${e.message}")
                _loading.value = View.GONE
            } finally {
            }
        }


    fun getMeetingPurposeByID(opportunityID: String) = viewModelScope.launch {
        try {
            _resMeetingPurposeIDItems.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.getMeetingPurposeByID("Bearer ${checkIfUserLogin()}", opportunityID)
                    .let {
                        if (it.isSuccessful) {
                            _resMeetingPurposeIDItems.postValue(SuccessResource.success(it.body()))
                        } else {
                            _resMeetingPurposeIDItems.postValue(
                                SuccessResource.error(
                                    it.errorBody().toString(), null
                                )
                            )
                        }
                    }
        } catch (e: Exception) {
            Timber.tag("TAG").e("Exception occur at getClients ${e.message}")
            _loading.value = View.GONE
        } finally {
        }
    }

    fun getMeetingPurposeByStatus(opportunityID: String) = viewModelScope.launch {
        try {
            _resMeetingPurposeList.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.getMeetingPurposeByStatus("Bearer ${checkIfUserLogin()}", opportunityID)
                    .let {
                        if (it.isSuccessful) {
                            _resMeetingPurposeList.postValue(SuccessResource.success(it.body()))
                        } else {
                            _resMeetingPurposeList.postValue(
                                SuccessResource.error(
                                    it.errorBody().toString(), null
                                )
                            )
                        }
                    }
        } catch (e: Exception) {
            Timber.tag("TAG").e("Exception occur at getMeetingPurposeByStatus ${e.message}")
            _loading.value = View.GONE
        } finally {
        }
    }

    fun getMeetingPurposeStatus() = viewModelScope.launch {
        try {
            _resMeetingPurposeStatusDetails.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.getMeetingPurposeStatus("Bearer ${checkIfUserLogin()}")
                    .let {
                        if (it.isSuccessful) {
                            _resMeetingPurposeStatusDetails.postValue(SuccessResource.success(it.body()))
                        } else {
                            _resMeetingPurposeStatusDetails.postValue(
                                SuccessResource.error(
                                    it.errorBody().toString(), null
                                )
                            )
                        }
                    }
        } catch (e: Exception) {
            Timber.tag("TAG").e("Exception occur at getMeetingPurposeStatus ${e.message}")
            _loading.value = View.GONE
        } finally {
        }
    }

    fun addTravelExpense(
        travelExpence: AddTravelExpenceRequest,
        returnTravelExpense: AddReturnTravelExpenceRequest?, listImage: MutableList<File>
    ) =
        viewModelScope.launch {
            try {
                _loading.value = View.VISIBLE
                Timber.tag("TAG").d("uploadGenresFile: " + travelExpence.toString())

                val requestBody: MutableMap<String, RequestBody> = HashMap()
                requestBody["travelFrom"] =
                    travelExpence.travelFrom.toRequestBody("text/plain".toMediaTypeOrNull())
                requestBody["travelTo"] =
                    travelExpence.travelTo.toRequestBody("text/plain".toMediaTypeOrNull())
                requestBody["travelDate"] =
                    travelExpence.travelDate.toRequestBody("text/plain".toMediaTypeOrNull())
                requestBody["modeOfTravel"] =
                    travelExpence.modeOfTravel.toRequestBody("text/plain".toMediaTypeOrNull())
//                requestBody["amount"] =
//                    travelExpence.amount.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                requestBody["expensesUser"] =
                    travelExpence.expensesUser.toRequestBody("text/plain".toMediaTypeOrNull())

                if (returnTravelExpense != null) {
                    requestBody["returnFrom"] =
                        returnTravelExpense.returnFrom.toRequestBody("text/plain".toMediaTypeOrNull())
                    requestBody["returnTo"] =
                        returnTravelExpense.returnTo.toRequestBody("text/plain".toMediaTypeOrNull())
                    requestBody["returnTravelDate"] =
                        returnTravelExpense.returnTravelDate.toRequestBody("text/plain".toMediaTypeOrNull())
                    requestBody["returnModeOfTravel"] =
                        returnTravelExpense.returnModeOfTravel.toRequestBody("text/plain".toMediaTypeOrNull())
                }


                val listMultipartImage: MutableList<MultipartBody.Part> = ArrayList()
                for (i in 0 until listImage.size) {
                    listMultipartImage.add(
                        MultipartBody.Part.createFormData(
                            "files",
                            listImage[i].name,
//                        RequestBody.create(MediaType.parse("image/*")!!,
                            listImage[i].readBytes().toRequestBody(
                                "multipart/form-data".toMediaTypeOrNull(), 0,
                            )
                        )
//                    )
                    )
                }

//                val amountPart =
//                    MultipartBody.Part.createFormData("amount", travelExpence.amount)

                _resAddTravelExpence.postValue(SuccessResource.loading(null))
                if (utlIsNetworkAvailable()) {
                    Timber.tag("TAG").d("addTravelExpense: " + travelExpence.toString())

                    mainRepository.addTravelExpense(
                        "Bearer ${checkIfUserLogin()}",
                        travelExpence.purposeId.toString()
                            .toRequestBody("text/plain".toMediaTypeOrNull()),
                        travelExpence.amount.toLong(),
//                    RequestBody.create(MediaType.parse("text/plain"), travelExpence.purposeId.toString()),
                        listMultipartImage, requestBody
                    )
                        .let {
                            if (it.isSuccessful) {
                                _resAddTravelExpence.postValue(SuccessResource.success(it.body()))
                            } else {
                                _resAddTravelExpence.postValue(
                                    SuccessResource.error(
                                        it.errorBody().toString(), null
                                    )
                                )
                            }
                        }
                }
            } catch (e: Exception) {
                Timber.tag("TAG").e("Exception occur at add travel expenses ${e.message}")
                _loading.value = View.GONE
            } finally {
            }
        }


    fun addMOMExpense(
        travelExpence: AddMOMRequest,
        listImage: MutableList<File>
    ) =
        viewModelScope.launch {
            try {
                _loading.value = View.VISIBLE
                Timber.tag("TAG").d("uploadGenresFile: " + travelExpence.toString())

                val requestBody: MutableMap<String, RequestBody> = HashMap()
                requestBody["meetingNotes"] =
                    travelExpence.meetingNotes.toRequestBody("text/plain".toMediaTypeOrNull())
                requestBody["infoFromClient"] =
                    travelExpence.infoFromClient.toRequestBody("text/plain".toMediaTypeOrNull())
                requestBody["targetDate"] =
                    travelExpence.targetDate.toRequestBody("text/plain".toMediaTypeOrNull())

                val listMultipartImage: MutableList<MultipartBody.Part> = ArrayList()
                for (i in 0 until listImage.size) {
                    listMultipartImage.add(
                        MultipartBody.Part.createFormData(
                            "files",
                            listImage[i].name,
//                        RequestBody.create(MediaType.parse("image/*")!!,
                            listImage[i].readBytes().toRequestBody(
                                "multipart/form-data".toMediaTypeOrNull(), 0,
                            )
                        )
//                    )
                    )
                }

//                val amountPart = MultipartBody.Part.createFormData("amount", travelExpence.hotelAmount.toString())

                _resAddMOMToMeetingExpence.postValue(SuccessResource.loading(null))
                if (utlIsNetworkAvailable()) {
                    Timber.tag("TAG").d("addFoodExpense: " + travelExpence.toString())

                    mainRepository.addMOMToMeeting(
                        "Bearer ${checkIfUserLogin()}",
                        travelExpence.purposeId.toString()
                            .toRequestBody("text/plain".toMediaTypeOrNull()),
//                    RequestBody.create(MediaType.parse("text/plain"), travelExpence.purposeId.toString()),
                        listMultipartImage, requestBody
                    )
                        .let {
                            if (it.isSuccessful) {
                                _resAddMOMToMeetingExpence.postValue(SuccessResource.success(it.body()))
                            } else {
                                _resAddMOMToMeetingExpence.postValue(
                                    SuccessResource.error(
                                        it.errorBody().toString(), null
                                    )
                                )
                            }
                        }
                }
            } catch (e: Exception) {
                Timber.tag("TAG").e("Exception occur at add travel expenses ${e.message}")
                _loading.value = View.GONE
            } finally {
            }
        }

    fun putUserMeetingCheckIN(
        clientName: String,
        checkin: AddCheckInRequest,
        listImage: MutableList<File>
    ) = viewModelScope.launch {

        try {
            _loading.value = View.VISIBLE
            Timber.tag("TAG").d("uploadGenresFile: " + checkin.toString())
            val requestBody: MutableMap<String, RequestBody> = HashMap()
            requestBody["latitude"] =
                checkin.latitude.toRequestBody("text/plain".toMediaTypeOrNull())
            requestBody["longitude"] =
                checkin.longitude.toRequestBody("text/plain".toMediaTypeOrNull())

            val listMultipartImage: MultipartBody.Part =
                MultipartBody.Part.createFormData(
                    "file",
                    listImage[0].name,
//                        RequestBody.create(MediaType.parse("image/*")!!,
                    listImage[0].readBytes().toRequestBody(
                        "multipart/form-data".toMediaTypeOrNull(), 0,
                    )
                )

            _resUserMeetingCheckIN.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.putUserMeetingCheckIN(
                    "Bearer ${checkIfUserLogin()}",
                    clientName,
                    requestBody,
                    listMultipartImage
                ).let {
                    if (it.isSuccessful) {
                        _resUserMeetingCheckIN.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resUserMeetingCheckIN.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                }
        } catch (e: Exception) {
            Timber.tag("TAG").e("Exception occur at add travel expenses ${e.message}")
            _loading.value = View.GONE
        } finally {
        }
    }

    fun putUserMeetingCheckOUT(
        clientName: String, checkin: AddCheckInRequest
    ) = viewModelScope.launch {

        try {
            _loading.value = View.VISIBLE
            Timber.tag("TAG").d("uploadGenresFile: " + checkin.toString())


            val requestBody: MutableMap<String, RequestBody> = HashMap()
            requestBody["latitude"] =
                checkin.latitude.toRequestBody("text/plain".toMediaTypeOrNull())
            requestBody["longitude"] =
                checkin.longitude.toRequestBody("text/plain".toMediaTypeOrNull())

            _resUserMeetingCheckOUT.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.putUserMeetingCheckOUT(
                    "Bearer ${checkIfUserLogin()}",
                    clientName,
                    requestBody
                )
                    .let {
                        if (it.isSuccessful) {
                            _resUserMeetingCheckOUT.postValue(SuccessResource.success(it.body()))
                        } else {
                            _resUserMeetingCheckOUT.postValue(
                                SuccessResource.error(
                                    it.errorBody().toString(),
                                    null
                                )
                            )
                        }
                    }
        } catch (e: Exception) {
            Timber.tag("TAG").e("Exception occur at add travel expenses ${e.message}")
            _loading.value = View.GONE
        } finally {

        }
    }

    fun addFoodExpense(
        travelExpence: AddFoodExpenceRequest,
        listImage: MutableList<File>
    ) =
        viewModelScope.launch {
            try {
                _loading.value = View.VISIBLE
                Timber.tag("TAG").d("uploadGenresFile: " + travelExpence.toString())

                val requestBody: MutableMap<String, RequestBody> = HashMap()
                requestBody["foodComments"] =
                    travelExpence.foodComments.toRequestBody("text/plain".toMediaTypeOrNull())
//                requestBody["hotelFromDate"] =
//                    travelExpence..toRequestBody("text/plain".toMediaTypeOrNull())
//                requestBody["hotelToDate"] =
//                    travelExpence.hotelToDate.toRequestBody("text/plain".toMediaTypeOrNull())
//                requestBody["noOfDays"] =
//                    travelExpence.noOfDays.toRequestBody("text/plain".toMediaTypeOrNull())
//                requestBody["amount"] =
//                    travelExpence.hotelAmount.toString()
//                        .toRequestBody("text/plain".toMediaTypeOrNull())
                requestBody["expensesUser"] =
                    travelExpence.expensesUser.toRequestBody("text/plain".toMediaTypeOrNull())

                val listMultipartImage: MutableList<MultipartBody.Part> = ArrayList()
                for (i in 0 until listImage.size) {
                    listMultipartImage.add(
                        MultipartBody.Part.createFormData(
                            "files",
                            listImage[i].name,
//                        RequestBody.create(MediaType.parse("image/*")!!,
                            listImage[i].readBytes().toRequestBody(
                                "multipart/form-data".toMediaTypeOrNull(), 0,
                            )
                        )
                    )
                }

                _resAddFoodExpence.postValue(SuccessResource.loading(null))
                if (utlIsNetworkAvailable()) {
                    Timber.tag("TAG").d("addFoodExpense: $travelExpence")

                    mainRepository.addTravelExpense(
                        "Bearer ${checkIfUserLogin()}",
                        travelExpence.purposeId.toString()
                            .toRequestBody("text/plain".toMediaTypeOrNull()),
                        travelExpence.foodAmount.toLong(),
//                    RequestBody.create(MediaType.parse("text/plain"), travelExpence.purposeId.toString()),
                        listMultipartImage, requestBody
                    )
                        .let {
                            if (it.isSuccessful) {
                                _resAddFoodExpence.postValue(SuccessResource.success(it.body()))
                            } else {
                                _resAddFoodExpence.postValue(
                                    SuccessResource.error(
                                        it.errorBody().toString(), null
                                    )
                                )
                            }
                        }
                }
            } catch (e: Exception) {
                Timber.tag("TAG").e("Exception occur at add travel expenses ${e.message}")
                _loading.value = View.GONE
            } finally {
            }
        }

    fun addHotelExpense(
        travelExpence: AddHotelExpenceRequest,
        listImage: MutableList<File>
    ) =
        viewModelScope.launch {
            try {
                _loading.value = View.VISIBLE
                Timber.tag("TAG").d("uploadGenresFile: " + travelExpence.toString())

                val requestBody: MutableMap<String, RequestBody> = HashMap()
                requestBody["location"] =
                    travelExpence.location.toRequestBody("text/plain".toMediaTypeOrNull())
                requestBody["hotelFromDate"] =
                    travelExpence.hotelFromDate.toRequestBody("text/plain".toMediaTypeOrNull())
                requestBody["hotelToDate"] =
                    travelExpence.hotelToDate.toRequestBody("text/plain".toMediaTypeOrNull())
                requestBody["noOfDays"] =
                    travelExpence.noOfDays.toRequestBody("text/plain".toMediaTypeOrNull())
//                requestBody["amount"] =
//                    travelExpence.hotelAmount.toString()
//                        .toRequestBody("text/plain".toMediaTypeOrNull())
                requestBody["expensesUser"] =
                    travelExpence.expensesUser.toRequestBody("text/plain".toMediaTypeOrNull())

                val listMultipartImage: MutableList<MultipartBody.Part> = ArrayList()
                for (i in 0 until listImage.size) {
                    listMultipartImage.add(
                        MultipartBody.Part.createFormData(
                            "files",
                            listImage[i].name,
//                        RequestBody.create(MediaType.parse("image/*")!!,
                            listImage[i].readBytes().toRequestBody(
                                "multipart/form-data".toMediaTypeOrNull(), 0,
                            )
                        )
//                    )
                    )
                }

                val amountPart = MultipartBody.Part.createFormData(
                    "amount",
                    travelExpence.hotelAmount.toString()
                )

                _resAddHotelExpence.postValue(SuccessResource.loading(null))
                if (utlIsNetworkAvailable()) {
                    Log.d("TAG", "addTravelExpense: ${travelExpence.toString()}")

                    mainRepository.addTravelExpense(
                        "Bearer ${checkIfUserLogin()}",
                        travelExpence.purposeId.toString()
                            .toRequestBody("text/plain".toMediaTypeOrNull()),
                        travelExpence.hotelAmount.toLong(),
//                    RequestBody.create(MediaType.parse("text/plain"), travelExpence.purposeId.toString()),
                        listMultipartImage, requestBody
                    )
                        .let {
                            if (it.isSuccessful) {
                                _resAddHotelExpence.postValue(SuccessResource.success(it.body()))
                            } else {
                                _resAddHotelExpence.postValue(
                                    SuccessResource.error(
                                        it.errorBody().toString(), null
                                    )
                                )
                            }
                        }
                }
            } catch (e: Exception) {
                Timber.tag("TAG").e("Exception occur at add travel expenses ${e.message}")
                _loading.value = View.GONE
            } finally {
            }
        }

    @SuppressLint("SuspiciousIndentation")
    fun getEmployeeMe() = viewModelScope.launch {
        try {
            _resEmployeeMe.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.getEmployeeMe("Bearer ${checkIfUserLogin()}").let {
                    if (it.isSuccessful) {
                        _resEmployeeMe.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resEmployeeMe.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                }
        } catch (e: Exception) {
            Timber.tag("TAG").e("Exception occur at getClients ${e.message}")
            _loading.value = View.GONE
        } finally {
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun getEmployeeAllowences() = viewModelScope.launch {
        try {
            _resEmployeeAllowence.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.getEmployeeAllowences(
                    "Bearer ${checkIfUserLogin()}",
                    getUserEmployeeID()
                ).let {
                    if (it.isSuccessful) {
                        _resEmployeeAllowence.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resEmployeeAllowence.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                }
        } catch (e: Exception) {
            Timber.tag("TAG").e("Exception occur at getClients ${e.message}")
            _loading.value = View.GONE
        } finally {
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun addUserCoordinates(
        visitDate: AddLocationCoordinates
    ) = viewModelScope.launch {
        try {
            _resAddUserCoordinates.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.addUserCoordinates("Bearer ${checkIfUserLogin()}", visitDate).let {
                    if (it.isSuccessful) {
                        _resAddUserCoordinates.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resAddUserCoordinates.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                }
        } catch (e: Exception) {
            Timber.tag("TAG").e("Exception occur at getClients ${e.message}")
            _loading.value = View.GONE
        } finally {
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun addMeetingPurpose(
        visitDate: AddMeetingRequest
    ) = viewModelScope.launch {

        try {
            _resNewMeetingPurpose.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.addMeetingPurpose("Bearer ${checkIfUserLogin()}", visitDate).let {
                    if (it.isSuccessful) {
                        _resNewMeetingPurpose.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resNewMeetingPurpose.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                }
        } catch (e: Exception) {
            Timber.tag("TAG").e("Exception occur at getClients ${e.message}")
            _loading.value = View.GONE
        } finally {
        }
    }

    fun getCommentProjectID(opportunityID: String) = viewModelScope.launch {
        try {
            _resCommentResponse.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.getCommentProjectID("Bearer ${checkIfUserLogin()}", opportunityID)
                    .let {
                        if (it.isSuccessful) {
                            _resCommentResponse.postValue(SuccessResource.success(it.body()))
                        } else {
                            _resCommentResponse.postValue(
                                SuccessResource.error(
                                    it.errorBody().toString(),
                                    null
                                )
                            )
                        }
                    }
        } catch (e: Exception) {
            Timber.tag("TAG").e("Exception occur at getClients ${e.message}")
            _loading.value = View.GONE
        } finally {
        }
    }

    fun userLogin(userId: String, password: String) = viewModelScope.launch {
        try {
            val requestBody: MutableMap<String, String> = HashMap()
            requestBody[Common.User_Id] = userId
            requestBody[Common.Password] = password
            _resLoginResponse.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.userLogin(requestBody).let {
                    if (it.isSuccessful) {
                        _resLoginResponse.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resLoginResponse.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                }
        } catch (e: Exception) {
            Timber.tag("TAG").e("Exception occur at getClients ${e.message}")
            _loading.value = View.GONE
        } finally {
        }
    }

    fun addClient(clientName: String, clientContactName: String, clientContactPos: String) =
        viewModelScope.launch {
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody[Common.CLIENT_NAME] = clientName
                requestBody[Common.CLIENT_CONTACT_NAME] = clientContactName
                requestBody[Common.CLIENT_CONTACT_POS] = clientContactPos

                _resNewClientResponse.postValue(SuccessResource.loading(null))
                if (utlIsNetworkAvailable())
                    mainRepository.addClient("Bearer ${checkIfUserLogin()}", requestBody).let {
                        if (it.isSuccessful) {
                            _resNewClientResponse.postValue(SuccessResource.success(it.body()))
                        } else {
                            _resNewClientResponse.postValue(
                                SuccessResource.error(
                                    it.errorBody().toString(),
                                    null
                                )
                            )
                        }
                    }
            } catch (e: Exception) {
                Timber.tag("TAG").e("Exception occur at getClients ${e.message}")
                _loading.value = View.GONE
            } finally {
            }
        }

    fun addCommentOpportunity(
        projectId: String, assignedId: String,
        comment: String,
    ) =
        viewModelScope.launch {
            try {
                val requestBody = AddCommentOpportunity(
                    comment = comment,
                    purposeId = projectId,
                    empType = assignedId,
                )

                _resNewCommentResponse.postValue(SuccessResource.loading(null))
                if (utlIsNetworkAvailable())
                    mainRepository.addCommentOpportunity(
                        "Bearer ${checkIfUserLogin()}",
                        requestBody
                    )
                        .let {
                            if (it.isSuccessful) {
                                _resNewCommentResponse.postValue(SuccessResource.success(it.body()))
                            } else {
                                _resNewCommentResponse.postValue(
                                    SuccessResource.error(
                                        it.errorBody().toString(),
                                        null
                                    )
                                )
                            }
                        }
            } catch (e: Exception) {
                Timber.tag("TAG").e("Exception occur at getClients ${e.message}")
                _loading.value = View.GONE
            } finally {
            }
        }


    fun addProject(
        projectName: String,
        clientId: String,
        opportunityType: String,
        opportunityDesc: String,
        status: String
    ) = viewModelScope.launch {
       try{ val requestBody: MutableMap<String, String> = HashMap()
        requestBody[Common.PROJECT_NAME] = projectName
        requestBody[Common.CLIENT_ID] = clientId
        requestBody[Common.OPPORTUNITY_TYPE] = opportunityType
        requestBody[Common.OPPORTUNITY_DESC] = opportunityDesc
        requestBody[Common.STATUS] = status

        _resNewClientResponse.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.addProject("Bearer ${checkIfUserLogin()}", requestBody).let {
                if (it.isSuccessful) {
                    _resNewClientResponse.postValue(SuccessResource.success(it.body()))
                } else {
                    _resNewClientResponse.postValue(
                        SuccessResource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            }} catch (e: Exception) {
           Timber.tag("TAG").e("Exception occur at getClients ${e.message}")
           _loading.value = View.GONE
       } finally {
       }
    }

    fun checkIfUserLogin(): String {
        return getAuthToken()
    }

    fun saveAuthToken(auth_token: String, refresh_token: String): Boolean {
        val editor = sharedPreferences.edit()
        editor.putString(PREF_TOKEN, auth_token)
        editor.putString(PREF_REFRESH_TOKEN, refresh_token)
        editor.putBoolean(PREF_FIRST_TIME, false)
        editor.apply()
        return true
    }

    fun getAuthToken(): String {
        return sharedPreferences.getString(PREF_TOKEN, PREF_DEFAULT)!!
    }

    fun saveUserEmployeeID(roleID: String) {
        val editor = sharedPreferences.edit()
        editor.putString(PREF_EMP_ID, roleID)
        editor.apply()
    }

    fun getUserEmployeeID(): String {
        return sharedPreferences.getString(PREF_EMP_ID, PREF_DEFAULT)!!
    }

    fun clearSharedPreference(): Boolean {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        return true
    }
}