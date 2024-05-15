package com.tekskills.er_tekskills.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.tekskills.er_tekskills.data.model.AccountHeadResponse
import com.tekskills.er_tekskills.data.model.ActionItemProjectIDResponse
import com.tekskills.er_tekskills.data.model.ActionItemProjectIDResponseItem
import com.tekskills.er_tekskills.data.model.AddActionItemOpportunityRequest
import com.tekskills.er_tekskills.data.model.AddCommentOpportunity
import com.tekskills.er_tekskills.data.model.AddEscalationRequest
import com.tekskills.er_tekskills.data.model.AddFoodExpenceRequest
import com.tekskills.er_tekskills.data.model.AddHotelExpenceRequest
import com.tekskills.er_tekskills.data.model.AddMOMOpportunityRequest
import com.tekskills.er_tekskills.data.model.AddMeetingRequest
import com.tekskills.er_tekskills.data.model.AddOpportunityRequest
import com.tekskills.er_tekskills.data.model.AddPurposeMeetingResponse
import com.tekskills.er_tekskills.data.model.AddReturnTravelExpenceRequest
import com.tekskills.er_tekskills.data.model.AddTravelExpenceRequest
import com.tekskills.er_tekskills.data.model.AddTravelExpenceResponse
import com.tekskills.er_tekskills.data.model.AssignProjectListResponse
import com.tekskills.er_tekskills.data.model.CategoryInfo
import com.tekskills.er_tekskills.data.model.ClientEscalationGraphByIDResponse
import com.tekskills.er_tekskills.data.model.ClientNamesResponse
import com.tekskills.er_tekskills.data.model.ClientsEscalationResponse
import com.tekskills.er_tekskills.data.model.CommentsListResponse
import com.tekskills.er_tekskills.data.model.LoginResponse
import com.tekskills.er_tekskills.data.model.ManagementResponse
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponse
import com.tekskills.er_tekskills.data.model.MeetingPurposeResponseData
import com.tekskills.er_tekskills.data.model.MomProjectResponse
import com.tekskills.er_tekskills.data.model.MomProjectResponseItem
import com.tekskills.er_tekskills.data.model.NewClientResponse
import com.tekskills.er_tekskills.data.model.NoOfTaskForEachCategory
import com.tekskills.er_tekskills.data.model.OpportunityByProjectIDResponse
import com.tekskills.er_tekskills.data.model.PendingActionGraphResponse
import com.tekskills.er_tekskills.data.model.PendingActionItemGraphByIDResponse
import com.tekskills.er_tekskills.data.model.PendingEscalationGraphResponse
import com.tekskills.er_tekskills.data.model.PracticeHeadResponse
import com.tekskills.er_tekskills.data.model.ProjectDetailsVO
import com.tekskills.er_tekskills.data.model.ProjectListResponse
import com.tekskills.er_tekskills.data.model.ProjectManagerResponse
import com.tekskills.er_tekskills.data.model.ProjectOpportunityResponse
import com.tekskills.er_tekskills.data.model.ProjectsAssignVO
import com.tekskills.er_tekskills.data.model.TaskCategoryInfo
import com.tekskills.er_tekskills.data.model.TaskInfo
import com.tekskills.er_tekskills.data.model.UserAllowenceResponse
import com.tekskills.er_tekskills.data.model.UserMeResponse
import com.tekskills.er_tekskills.data.repository.MainRepository
import com.tekskills.er_tekskills.data.util.Constants
import com.tekskills.er_tekskills.domain.TaskCategoryRepository
import com.tekskills.er_tekskills.utils.AppUtil.utlIsNetworkAvailable
import com.tekskills.er_tekskills.utils.Common
import com.tekskills.er_tekskills.utils.Common.Companion.MANAGER
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_DEFAULT
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_EMP_ID
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_FIRST_TIME
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_REFRESH_TOKEN
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_ROLE_TYPE
import com.tekskills.er_tekskills.utils.Common.Companion.PREF_TOKEN
import com.tekskills.er_tekskills.utils.SuccessResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    private val _eventChannel = Channel<MainEvent>()
    private val _hotelEventChannel = Channel<MainEvent>()

//    val cartoonListData = Pager(PagingConfig(pageSize = 1)) {
//        repository
//    }.flow.cachedIn(viewModelScope)

    val mainEvent = _eventChannel.receiveAsFlow()
    val hotelMainEvent = _hotelEventChannel.receiveAsFlow()

    val _networkLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val networkLiveData: LiveData<Boolean> get() = _networkLiveData

    private val _resClientNameList = MutableLiveData<SuccessResource<ClientNamesResponse>>()

    val resClientNameList: LiveData<SuccessResource<ClientNamesResponse>>
        get() = _resClientNameList

    private val _resProjectList = MutableLiveData<SuccessResource<ProjectListResponse>>()

    val resProjectList: LiveData<SuccessResource<ProjectListResponse>>
        get() = _resProjectList

    private val _resProjectOpportunityList =
        MutableLiveData<SuccessResource<ProjectOpportunityResponse>>()

    val resProjectOpportunityList: LiveData<SuccessResource<ProjectOpportunityResponse>>
        get() = _resProjectOpportunityList

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


    private val _resCommentsList = MutableLiveData<SuccessResource<CommentsListResponse>>()

    val resCommentsList: LiveData<SuccessResource<CommentsListResponse>>
        get() = _resCommentsList

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

    private var dropOffPlaceAddress: MutableLiveData<String>? = MutableLiveData()
    private var pickupPlaceAddress: MutableLiveData<String>? = MutableLiveData()
    private var priceInVNDString: MutableLiveData<String>? = MutableLiveData()
    private val distanceInKmString: MutableLiveData<Double>? = MutableLiveData()
    private var transportationType: MutableLiveData<String>? = MutableLiveData()
    private var bookBtnPressed: MutableLiveData<Boolean>? = MutableLiveData()
    private var cancelBookingBtnPressed: MutableLiveData<Boolean>? = MutableLiveData()
    private val customerSelectedDropOffPlace: MutableLiveData<Place?> = MutableLiveData<Place?>();
    private val customerSelectedPickupPlace: MutableLiveData<Place?> = MutableLiveData<Place?>();

    fun setDistanceInKmString(distanceInKmString: Double?) {
        this.distanceInKmString!!.value = distanceInKmString
    }

    fun getDistanceInKmString(): MutableLiveData<Double>? {
        return distanceInKmString
    }


    fun setPriceInVNDString(priceInVNDString: String) {
        this.priceInVNDString!!.value = priceInVNDString
    }

    fun setDropOffPlaceString(dropOffPlaceString: String) {
        dropOffPlaceAddress!!.value = dropOffPlaceString
    }

    fun setPickupPlaceString(pickupPlaceString: String) {
        pickupPlaceAddress!!.value = pickupPlaceString
    }

    fun getPriceInVNDString(): MutableLiveData<String>? {
        return priceInVNDString
    }

    fun getDropOffPlaceString(): MutableLiveData<String>? {
        return dropOffPlaceAddress
    }

    fun getPickupPlaceString(): MutableLiveData<String>? {
        return pickupPlaceAddress
    }

    fun getTransportationType(): MutableLiveData<String>? {
        return transportationType
    }

    fun setTransportationType(transportationType: String?) {
        this.transportationType!!.value = transportationType
    }

    fun setCustomerSelectedDropOffPlace(customerSelectedDropOffPlace: Place?) {
        this.customerSelectedDropOffPlace!!.value = customerSelectedDropOffPlace
    }

    fun setCustomerSelectedPickupPlace(customerSelectedPickupPlace: Place?) {
        this.customerSelectedPickupPlace!!.value = customerSelectedPickupPlace
    }

    fun setBookBtnPressed(bookBtnPressed: Boolean?) {
        this.bookBtnPressed!!.value = bookBtnPressed
    }

    fun setCancelBookingBtnPressed(cancelBookingBtnPressed: Boolean?) {
        this.cancelBookingBtnPressed!!.value = cancelBookingBtnPressed
    }

    fun getCustomerSelectedPickupPlace(): MutableLiveData<Place?> {
        return customerSelectedPickupPlace
    }

    fun getCustomerSelectedDropOffPlace(): MutableLiveData<Place?> {
        return customerSelectedDropOffPlace
    }

    fun getBookBtnPressed(): MutableLiveData<Boolean>? {
        return bookBtnPressed
    }

    fun getCancelBookingBtnPressed(): MutableLiveData<Boolean>? {
        return cancelBookingBtnPressed
    }

    private val _resNewMeetingPurpose =
        MutableLiveData<SuccessResource<AddPurposeMeetingResponse>>()

    val resNewMeetingPurpose: LiveData<SuccessResource<AddPurposeMeetingResponse>>
        get() = _resNewMeetingPurpose


//    fun setCustomerSelectedDropOffPlace(customerSelectedDropOffPlace: Place?) {
//        this.customerSelectedDropOffPlace.value = customerSelectedDropOffPlace
//    }
//
//    fun setCustomerSelectedPickupPlace(customerSelectedPickupPlace: Place?) {
//        this.customerSelectedPickupPlace.value = customerSelectedPickupPlace
//    }

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

    fun getPendingActionGraph() = viewModelScope.launch {
        _resPendingActionGraphList.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.getPendingActionGraph("Bearer ${checkIfUserLogin()}").let {
                if (it.isSuccessful) {
                    _resPendingActionGraphList.postValue(SuccessResource.success(it.body()))
                } else {
                    _resPendingActionGraphList.postValue(
                        SuccessResource.error(
                            it.errorBody().toString(), null
                        )
                    )
                }
            }
    }

    fun getPendingActionGraphByID(projectId: String) = viewModelScope.launch {
        _resPendingActionGraphByIDList.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.getPendingActionGraphByID("Bearer ${checkIfUserLogin()}", projectId)
                .let {
                    if (it.isSuccessful) {
                        _resPendingActionGraphByIDList.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resPendingActionGraphByIDList.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(), null
                            )
                        )
                    }
                }
    }

    fun getClientEscalationGraphByID(clientId: String) = viewModelScope.launch {
        _resEscalationGraphByIDList.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.getEscalationGraphByID("Bearer ${checkIfUserLogin()}", clientId).let {
                if (it.isSuccessful) {
                    _resEscalationGraphByIDList.postValue(SuccessResource.success(it.body()))
                } else {
                    _resEscalationGraphByIDList.postValue(
                        SuccessResource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            }
    }


    @SuppressLint("SuspiciousIndentation")
    fun getEscalationGraph() = viewModelScope.launch {
        _resEscalationGraphList.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.getEscalationGraph("Bearer ${checkIfUserLogin()}").let {
                if (it.isSuccessful) {
                    _resEscalationGraphList.postValue(SuccessResource.success(it.body()))
                } else {
                    _resEscalationGraphList.postValue(
                        SuccessResource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            }
    }

    fun getClientNameList() = viewModelScope.launch {
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
    }

    fun getProjectList() = viewModelScope.launch {
        _resProjectList.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.getProjects("Bearer ${checkIfUserLogin()}").let {
                if (it.isSuccessful) {
                    _resProjectList.postValue(SuccessResource.success(it.body()))
                } else {
                    _resProjectList.postValue(
                        SuccessResource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            }
    }

    fun getAssignedProjectList() = viewModelScope.launch {
        _resAssignedProjectList.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.getAssignProjects("Bearer ${checkIfUserLogin()}").let {
                if (it.isSuccessful) {
                    _resAssignedProjectList.postValue(SuccessResource.success(it.body()))
                } else {
                    _resAssignedProjectList.postValue(
                        SuccessResource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            }
    }


    fun getManagementList(authorization: String) =
        viewModelScope.launch {
            _resManagementList.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.getManagementList("Bearer ${checkIfUserLogin()}").let {
                    if (it.isSuccessful) {
                        _resManagementList.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resManagementList.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                }
        }

    fun getAccountManagerList(authorization: String) =
        viewModelScope.launch {
            _resAccountHeadList.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.getAccountManagerList("Bearer ${checkIfUserLogin()}").let {
                    if (it.isSuccessful) {
                        _resAccountHeadList.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resAccountHeadList.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                }
        }


    fun getProjectManagerList(authorization: String) =
        viewModelScope.launch {
            _resProjectManagerList.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.getProjectManagerList("Bearer ${checkIfUserLogin()}").let {
                    if (it.isSuccessful) {
                        _resProjectManagerList.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resProjectManagerList.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                }
        }

    fun getPracticeHeadList(authorization: String) =
        viewModelScope.launch {
            _resPracticeHeadList.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.getPracticeHeadList("Bearer ${checkIfUserLogin()}").let {
                    if (it.isSuccessful) {
                        _resPracticeHeadList.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resPracticeHeadList.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                }
        }


    fun getMeetingPurpose(authorization: String) =
        viewModelScope.launch {
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
        }


    fun getMeetingPurposeByID(opportunityID: String) = viewModelScope.launch {
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
    }


    fun addTravelExpense(
        travelExpence: AddTravelExpenceRequest,
        returnTravelExpense: AddReturnTravelExpenceRequest?, listImage: MutableList<File>
    ) =
        viewModelScope.launch {
            try {
                _loading.value = View.VISIBLE
                Log.d("TAG", "uploadGenresFile: ${travelExpence.toString()}")

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
                    Log.d("TAG", "addTravelExpense: ${travelExpence.toString()}")

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
                Log.e("TAG", "Exception occur at add travel expenses ${e.message}")
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
                Log.d("TAG", "uploadGenresFile: ${travelExpence.toString()}")
//                if (file == null) {
//                    _eventChannel.send(MainEvent.Error("File is empty"))
//                    return@launch
//                }


//                val reqBody =
//                    file!!.toRequestBody(
//                        "multipart/form-data".toMediaTypeOrNull(),
//                        0,
//                    )
//                val formData = MultipartBody.Part.createFormData(
//                    "file",
//                    "expensesFile-${Random.nextInt(1, 10)}",
//                    reqBody
//                )

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
//                    )
                    )
                }

//                val amountPart = MultipartBody.Part.createFormData("amount", travelExpence.hotelAmount.toString())

                _resAddFoodExpence.postValue(SuccessResource.loading(null))
                if (utlIsNetworkAvailable()) {
                    Log.d("TAG", "addFoodExpense: ${travelExpence.toString()}")

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
                Log.e("TAG", "Exception occur at add travel expenses ${e.message}")
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
                Log.d("TAG", "uploadGenresFile: ${travelExpence.toString()}")
//                if (file == null) {
//                    _eventChannel.send(MainEvent.Error("File is empty"))
//                    return@launch
//                }


//                val reqBody =
//                    file!!.toRequestBody(
//                        "multipart/form-data".toMediaTypeOrNull(),
//                        0,
//                    )
//                val formData = MultipartBody.Part.createFormData(
//                    "file",
//                    "expensesFile-${Random.nextInt(1, 10)}",
//                    reqBody
//                )

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

                val amountPart = MultipartBody.Part.createFormData("amount", travelExpence.hotelAmount.toString())

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
                Log.e("TAG", "Exception occur at add travel expenses ${e.message}")
                _loading.value = View.GONE
            } finally {
            }
        }


//    fun addMultipleImages(name: String, listImage: MutableList<File>) {
//        disposables.add(
//            repo.addMultipleImages(name, listImage)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe {}
//                .doOnComplete {}
//                .doOnError {
//                    ldMsg.value = it.message
//                }
//                .subscribe {
//                    val response = Gson().fromJson(it.string(), RespAddMultipleImagesModel::class.java)
//                    ldRespAddMultipleImages.value = response
//                }
//        )
//    }

//    fun addHotelExpense(travelExpence: AddHotelExpenceRequest) = viewModelScope.launch {
//
//        try {
//            _loading.value = View.VISIBLE
//            Log.d("TAG", "uploadGenresFile: ${travelExpence.toString()}")
//            if (file == null) {
//                _eventChannel.send(MainEvent.Error("File is empty"))
//                return@launch
//            }
//            val reqBody =
//                file!!.toRequestBody(
//                    "multipart/form-data".toMediaTypeOrNull(),
//                    0,
//                )
//            val formData = MultipartBody.Part.createFormData(
//                "file",
//                "expensesFile-${Random.nextInt(1, 10)}",
//                reqBody
//            )
//
//            val requestBody: MutableMap<String, RequestBody> = HashMap()
////            requestBody["purposeId"] = travelExpence.purposeId.toString()
//            requestBody["hotelFromDate"] =
//                travelExpence.hotelFromDate.toRequestBody("text/plain".toMediaTypeOrNull())
//            requestBody["hotelToDate"] =
//                travelExpence.hotelToDate.toRequestBody("text/plain".toMediaTypeOrNull())
//            requestBody["location"] =
//                travelExpence.location.toRequestBody("text/plain".toMediaTypeOrNull())
//            requestBody["noOfDays"] =
//                travelExpence.noOfDays.toRequestBody("text/plain".toMediaTypeOrNull())
//            requestBody["hotelAmount"] =
//                travelExpence.hotelAmount.toRequestBody("text/plain".toMediaTypeOrNull())
//            requestBody["expensesUser"] =
//                travelExpence.expensesUser.toRequestBody("text/plain".toMediaTypeOrNull())
//
////            val fields: MutableMap<String, RequestBody> = HashMap()
//////            fields["purposeId"] =
//////                RequestBody.create(MediaType.parse("text/plain"), travelExpence.purposeId.toString())
////            fields["travelFrom"] =
////                RequestBody.create(MediaType.parse("text/plain"), travelExpence.travelFrom)
////            fields["travelTo"] =
////                RequestBody.create(MediaType.parse("text/plain"), travelExpence.travelTo)
////            fields["travelDate"] =
////                RequestBody.create(MediaType.parse("text/plain"), travelExpence.travelDate)
////            fields["modeOfTravel"] =
////                RequestBody.create(MediaType.parse("text/plain"), travelExpence.modeOfTravel)
////            fields["expensesUser"] =
////                RequestBody.create(MediaType.parse("text/plain"),  travelExpence.expensesUser)
//
//            _resMeetingPurposeIDItems.postValue(SuccessResource.loading(null))
//            if (utlIsNetworkAvailable()) {
//                Log.d("TAG", "addTravelExpense: ${travelExpence.toString()}")
//
//                mainRepository.addHotelExpense(
//                    "Bearer ${checkIfUserLogin()}",
//                    travelExpence.purposeId.toString()
//                        .toRequestBody("text/plain".toMediaTypeOrNull()),
////                    RequestBody.create(MediaType.parse("text/plain"), travelExpence.purposeId.toString()),
//                    formData, requestBody
//                )
//                    .let {
//                        if (it.isSuccessful) {
//                            _resAddTravelExpence.postValue(SuccessResource.success(it.body()))
//                        } else {
//                            _resAddTravelExpence.postValue(
//                                SuccessResource.error(
//                                    it.errorBody().toString(), null
//                                )
//                            )
//                        }
//                    }
//            }
//        } catch (e: Exception) {
//            Log.e("TAG", "Exception occur at add travel expenses ${e.message}")
//            _loading.value = View.GONE
//        } finally {
//        }
//    }

//    fun uploadGenresFile(genresName: String, genresDesc: String) =
//        viewModelScope.launch {
//            if (utlIsNetworkAvailable())
//                try {
//                    _loading.value = View.VISIBLE
//
//                    if (file == null) {
//                        _eventChannel.send(MainEvent.Error("File is empty"))
//                        return@launch
//                    }
//                    val reqBody =
//                        file!!.toRequestBody(
//                            "multipart/form-data".toMediaTypeOrNull(),
//                            0,
//                        )
//                    val formData = MultipartBody.Part.createFormData(
//                        "",
//                        "pexcelfile-${Random.nextInt(1, 10)}",
//                        reqBody
//                    )
//                    val requestBody: MutableMap<String, String> = HashMap()
//                    requestBody["AppConstant.NAME"] = genresName
//                    requestBody["AppConstant.DESCRIPTION"] = genresDesc
//
////                    homeRepo.uploadGenresFile(formData, requestBody, object : ApiCallback {
////                        @SuppressLint("SuspiciousIndentation")
////                        override fun onSuccess(response: Any) {
////                            response.let { advertisement ->
////                                uploadedSuccessful()
////                            }
////                        }
////
////                        override fun onFailure(errorMsg: String) {
////                            message.value =
////                                Event("getUploadGenres Exception $errorMsg")
////                            _errorMessage.value = Event(errorMsg)
////                            uploadError(errorMsg)
////                            _loading.value = View.GONE
////                        }
////
////                        override fun onSubscribe(disposable: Disposable) {
////                            getCompositeDisposable()!!.add(disposable)
////                        }
////
////                        override fun showLoading() {
////                            getLogDebugData(
////                                "FullScreenViewModel",
////                                "uploadGenresFile loading visible"
////                            )
////                        }
////
////                        override fun hideLoading() {
////                            getLogDebugData(
////                                "FullScreenViewModel",
////                                "uploadGenresFile loading hide"
////                            )
////                        }
////                    })
//
//                } catch (e: Exception) {
//                    _loading.value = View.GONE
//                } finally {
//                }
//        }


    fun getProjectOpportunity(authorization: String) =
        viewModelScope.launch {
            _resProjectOpportunityList.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.getProjectOpportunity("Bearer ${checkIfUserLogin()}").let {
                    if (it.isSuccessful) {
                        _resProjectOpportunityList.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resProjectOpportunityList.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                }
        }

    @SuppressLint("SuspiciousIndentation")
    fun getEmployeeMe() = viewModelScope.launch {
        _resEmployeeMe.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.getEmployeeMe("Bearer ${checkIfUserLogin()}").let {
                if (it.isSuccessful) {
                    _resEmployeeMe.postValue(SuccessResource.success(it.body()))
                } else {
                    _resEmployeeMe.postValue(SuccessResource.error(it.errorBody().toString(), null))
                }
            }
    }

    @SuppressLint("SuspiciousIndentation")
    fun getEmployeeAllowences() = viewModelScope.launch {
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
    }

    @SuppressLint("SuspiciousIndentation")
    fun addMeetingPurpose(
        visitDate: AddMeetingRequest
//        employeeId: String,
//        noOfDays: Int,
//        userCordinates: UserCoordinates,
//        visitDate: String,
//        visitPurpose: String
    ) = viewModelScope.launch {

//        val requestBody = AddMeetingRequest(
//            employeeId = employeeId,
//            noOfDays = noOfDays,
//            userCordinates = userCordinates,
//            visitDate = visitDate,
//            visitPurpose = visitPurpose
//        )

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
    }

    fun getEscalationItemProjectID(itemID: String) = viewModelScope.launch {
        _resEscalationList.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.getEscalationItemProjectID("Bearer ${checkIfUserLogin()}", itemID).let {
                if (it.isSuccessful) {
                    _resEscalationList.postValue(SuccessResource.success(it.body()))
                } else {
                    _resEscalationList.postValue(
                        SuccessResource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            }
    }

    fun getMOMProjects(opportunityID: String) = viewModelScope.launch {
        _resMomActionItemsList.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.getMOMProjects("Bearer ${checkIfUserLogin()}", opportunityID).let {
                if (it.isSuccessful) {
                    _resMomActionItemsList.postValue(SuccessResource.success(it.body()))
                } else {
                    _resMomActionItemsList.postValue(
                        SuccessResource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            }
    }

    fun getMOMActionItemDetailsBYId(opportunityID: String) = viewModelScope.launch {
        _resMomActionItemBYID.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.getMOMActionItemDetailsBYId(
                "Bearer ${checkIfUserLogin()}",
                opportunityID
            ).let {
                if (it.isSuccessful) {
                    _resMomActionItemBYID.postValue(SuccessResource.success(it.body()))
                } else {
                    _resMomActionItemBYID.postValue(
                        SuccessResource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            }
    }

    fun getOpportunityByProductID(opportunityID: String) = viewModelScope.launch {
        _resOpportunityByProjectIDItems.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.getOpportunityByProductID("Bearer ${checkIfUserLogin()}", opportunityID)
                .let {
                    if (it.isSuccessful) {
                        _resOpportunityByProjectIDItems.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resOpportunityByProjectIDItems.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(), null
                            )
                        )
                    }
                }
    }

    fun getActionItemProjectID(opportunityID: String) = viewModelScope.launch {
        _resActionItemsList.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.getActionItemProjectID("Bearer ${checkIfUserLogin()}", opportunityID)
                .let {
                    if (it.isSuccessful) {
                        _resActionItemsList.postValue(SuccessResource.success(it.body()))
                    } else {
                        _resActionItemsList.postValue(
                            SuccessResource.error(
                                it.errorBody().toString(),
                                null
                            )
                        )
                    }
                }
    }

    fun getActionItemBYID(opportunityID: String) = viewModelScope.launch {
        _resActionItemBYID.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.getActionItemBYID("Bearer ${checkIfUserLogin()}", opportunityID).let {
                if (it.isSuccessful) {
                    _resActionItemBYID.postValue(SuccessResource.success(it.body()))
                } else {
                    _resActionItemBYID.postValue(
                        SuccessResource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            }
    }

    fun getCommentProjectID(opportunityID: String) = viewModelScope.launch {
        _resCommentsList.postValue(SuccessResource.loading(null))
        if (utlIsNetworkAvailable())
            mainRepository.getCommentProjectID("Bearer ${checkIfUserLogin()}", opportunityID).let {
                if (it.isSuccessful) {
                    _resCommentsList.postValue(SuccessResource.success(it.body()))
                } else {
                    _resCommentsList.postValue(
                        SuccessResource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            }
    }


    fun userLogin(userId: String, password: String) = viewModelScope.launch {
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
    }

    fun addClient(clientName: String, clientContactName: String, clientContactPos: String) =
        viewModelScope.launch {
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
        }


//    fun addComment(itemId: String, commentText: String) =
//        viewModelScope.launch {
//            val requestBody: MutableMap<String, String> = HashMap()
//            requestBody[Common.CLIENT_NAME] = clientName
//            requestBody[Common.CLIENT_CONTACT_NAME] = clientContactName
//            requestBody[Common.CLIENT_CONTACT_POS] = clientContactPos
//
//            _resNewClientResponse.postValue(SuccessResource.loading(null))
//            mainRepository.addClient("Bearer ${checkIfUserLogin()}", requestBody).let {
//                if (it.isSuccessful) {
//                    _resNewClientResponse.postValue(SuccessResource.success(it.body()))
//                } else {
//                    _resNewClientResponse.postValue(
//                        SuccessResource.error(
//                            it.errorBody().toString(),
//                            null
//                        )
//                    )
//                }
//            }
//        }

    fun addEmployee(
        userName: String, pwd: String, employeeNumber: String,
        roleId: String, departmentId: String,
        reportingManagerId: String, contactNoOne: String, email: String, status: String
    ) =
        viewModelScope.launch {
            val requestBody: MutableMap<String, String> = HashMap()
            requestBody[Common.USER_NAME] = userName
            requestBody[Common.PASSWORD] = pwd
            requestBody[Common.EMPLOYEE_NUM] = employeeNumber
            requestBody[Common.ROLE_ID] = roleId
            requestBody[Common.DEPT_ID] = departmentId
            requestBody[Common.REPORTMANAGER_ID] = reportingManagerId
            requestBody[Common.CONTACT_NUM] = contactNoOne
            requestBody[Common.EMAIL_ID] = email
            requestBody[Common.STATUS] = status

            _resNewClientResponse.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.addEmployee("Bearer ${checkIfUserLogin()}", requestBody).let {
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
        }

    fun addClientEscalation(
        projectId: String, clientId: String, clientEscalations: String,
        escalationDescription: String, escalationRaisedDate: String,
        escalationResolvedDate: String, escalationStatus: String
    ) =
        viewModelScope.launch {

            val requestBody = AddEscalationRequest(
                clientId = clientId,
                projectId = projectId,
                escalationRaisedDate = escalationRaisedDate,
                escalationResolvedDate = escalationResolvedDate,
                escalationDescription = escalationDescription,
                clientEscalations = clientEscalations == "NO", escalationStatus = escalationStatus

            )
//            val requestBody: MutableMap<String, String> = HashMap()
//            requestBody[Common.PROJECT_ID] = projectId
//            requestBody[Common.CLIENT_ID] = clientId
//            requestBody[Common.CLIENT_ESCALATION] = clientEscalations
//            requestBody[Common.ESCALATION_DESC] = escalationDescription
//            requestBody[Common.ESCALATION_RAISED_DATE] = escalationRaisedDate
//            requestBody[Common.ESCALATION_RESOLVE_DATE] = escalationResolvedDate
//            requestBody[Common.ESCALATION_STATUS] = escalationStatus

            _resNewClientResponse.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.addClientEscalation("Bearer ${checkIfUserLogin()}", requestBody)
                    .let {
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
        }

    fun addMOMActionItem(
        projectId: String, clientId: String, assignedId: String,
        meetingTitle: String, meetingDate: String,
        meetingTime: String, meetingNotes: String
    ) =
        viewModelScope.launch {

            val requestBody = AddMOMOpportunityRequest(
                meetingNotes = meetingNotes,
                meetingTitle = meetingTitle,
                clientId = clientId,
                projectId = projectId,
                assignedId = assignedId,
                meetingDate = meetingDate,
                meetingTime = meetingTime,
            )

            _resNewClientResponse.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.addMOMActionItem("Bearer ${checkIfUserLogin()}", requestBody).let {
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
        }

    fun addCommentOpportunity(
        projectId: String, assignedId: String,
        comment: String,
    ) =
        viewModelScope.launch {

            val requestBody = AddCommentOpportunity(
                comments = comment,
                projectId = projectId,
                assignedId = assignedId,
                employeeId = getUserEmployeeID(),
            )

            _resNewCommentResponse.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.addCommentOpportunity("Bearer ${checkIfUserLogin()}", requestBody)
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
        }

    fun addActionItemOpportunity(
        projectId: String, momId: String, assignedId: String,
        expectedInfoFromClient: String, actionItemCompletionDate: String,
        expectedInfoFromTekskills: String, tekskillsActionItesm: String, actionStatus: String
    ) =
        viewModelScope.launch {

            val requestBody = AddActionItemOpportunityRequest(
                projectId = projectId,
                assignedId = assignedId,
                momId = momId,
                expectedInfoFromClient = expectedInfoFromClient,
                expectedInfoFromTekskills = expectedInfoFromTekskills,
                tekskillsActionItesm = tekskillsActionItesm,
                actionItemCompletionDate = actionItemCompletionDate,
                actionStatus = actionStatus,
                date = Date(Constants.MAX_TIMESTAMP)
            )

            _resNewClientResponse.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.addActionItemOpportunity("Bearer ${checkIfUserLogin()}", requestBody)
                    .let {
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
        }

    /**
     * ADD Opportunity
     */
    private val _resAddOpportunity =
        MutableLiveData<SuccessResource<NewClientResponse>>()

    val resAddOpportunity: LiveData<SuccessResource<NewClientResponse>>
        get() = _resAddOpportunity

    fun addOpportunity(
        managementId: String, accountHeadId: String,
        practiceHeadId: String, projectManagerId: String, projectName: String,
        clientId: String,
        opportunityType: String,
        opportunityDesc: String,
        status: String
    ) = viewModelScope.launch {
        _resAddOpportunity.postValue(SuccessResource.loading(null))
        val requestBody = AddOpportunityRequest(
            clientId = clientId,
            projectDetailsVO = ProjectDetailsVO(
                projectName = projectName,
                oppotunityType = opportunityType,
                opportunityDesc = opportunityDesc,
                status = status
            ),
            projectsAssignVO = ProjectsAssignVO(
                managementId = managementId,
                accountHeadId = accountHeadId,
                practiceHeadId = practiceHeadId,
                projectManagerId = projectManagerId
            )
        )
        if (utlIsNetworkAvailable())
            mainRepository.addOpportunity("Bearer ${checkIfUserLogin()}", requestBody).let {
                if (it.isSuccessful) {
                    _resAddOpportunity.postValue(SuccessResource.success(it.body()))
                } else {
                    _resAddOpportunity.postValue(
                        SuccessResource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            }
    }


    fun addAssignProject(
        projectId: String, managementId: String, accountHeadId: String,
        practiceHeadId: String, projectManagerId: String
    ) =
        viewModelScope.launch {
            val requestBody: MutableMap<String, String> = HashMap()
            requestBody[Common.PROJECT_ID] = projectId
            requestBody[Common.MANAGEMENT_ID] = managementId
            requestBody[Common.ACCOUNTHEAD_ID] = accountHeadId
            requestBody[Common.PRACTICEHEAD_ID] = practiceHeadId
            requestBody[Common.PROJECTMANAGER_ID] = projectManagerId

            _resNewClientResponse.postValue(SuccessResource.loading(null))
            if (utlIsNetworkAvailable())
                mainRepository.assignProject("Bearer ${checkIfUserLogin()}", requestBody).let {
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
        }


    fun addProject(
        projectName: String,
        clientId: String,
        opportunityType: String,
        opportunityDesc: String,
        status: String
    ) = viewModelScope.launch {
        val requestBody: MutableMap<String, String> = HashMap()
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
            }
    }

    var file: ByteArray? = null
        set(value) {
            field = value
            selectedFile()
        }

    private fun selectedFile() = viewModelScope.launch {
        if (file != null)
            _eventChannel.send(MainEvent.FileSelected)
    }

    var returnFile: ByteArray? = null
        set(value) {
            field = value
            selectedReturnFile()
        }

    private fun selectedReturnFile() = viewModelScope.launch {
        if (returnFile != null)
            _eventChannel.send(MainEvent.FileSelected)
    }

    var hotelFile: ByteArray? = null
        set(value) {
            field = value
            selectedHotelFile()
        }

    private fun selectedHotelFile() = viewModelScope.launch {
        if (hotelFile != null)
            _hotelEventChannel.send(MainEvent.FileSelected)
    }

    fun uploadGenresFile(genresName: String, genresDesc: String) =
        viewModelScope.launch {
            if (utlIsNetworkAvailable())
                try {
                    _loading.value = View.VISIBLE

                    if (file == null) {
                        _hotelEventChannel.send(MainEvent.Error("File is empty"))
                        return@launch
                    }
                    val reqBody =
                        file!!.toRequestBody(
                            "multipart/form-data".toMediaTypeOrNull(),
                            0,
                        )
                    val formData = MultipartBody.Part.createFormData(
                        "",
                        "pexcelfile-${Random.nextInt(1, 10)}",
                        reqBody
                    )
                    val requestBody: MutableMap<String, String> = HashMap()
                    requestBody["AppConstant.NAME"] = genresName
                    requestBody["AppConstant.DESCRIPTION"] = genresDesc

//                    homeRepo.uploadGenresFile(formData, requestBody, object : ApiCallback {
//                        @SuppressLint("SuspiciousIndentation")
//                        override fun onSuccess(response: Any) {
//                            response.let { advertisement ->
//                                uploadedSuccessful()
//                            }
//                        }
//
//                        override fun onFailure(errorMsg: String) {
//                            message.value =
//                                Event("getUploadGenres Exception $errorMsg")
//                            _errorMessage.value = Event(errorMsg)
//                            uploadError(errorMsg)
//                            _loading.value = View.GONE
//                        }
//
//                        override fun onSubscribe(disposable: Disposable) {
//                            getCompositeDisposable()!!.add(disposable)
//                        }
//
//                        override fun showLoading() {
//                            getLogDebugData(
//                                "FullScreenViewModel",
//                                "uploadGenresFile loading visible"
//                            )
//                        }
//
//                        override fun hideLoading() {
//                            getLogDebugData(
//                                "FullScreenViewModel",
//                                "uploadGenresFile loading hide"
//                            )
//                        }
//                    })

                } catch (e: Exception) {
                    _loading.value = View.GONE
                } finally {
                }
        }


    fun updateTaskStatus(task: TaskInfo) {
        viewModelScope.launch(IO) {
            repository.updateTaskStatus(task)
        }
    }

    fun deleteTask(task: TaskInfo) {
        viewModelScope.launch(IO) {
            repository.deleteTask(task)
        }
    }

    fun insertTaskAndCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo) {
        viewModelScope.launch(IO) {
            repository.insertTaskAndCategory(taskInfo, categoryInfo)
        }
    }

    fun updateTaskAndAddCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo) {
        viewModelScope.launch(IO) {
            repository.updateTaskAndAddCategory(taskInfo, categoryInfo)
        }
    }

    fun updateTaskAndAddDeleteCategory(
        taskInfo: TaskInfo,
        categoryInfoAdd: CategoryInfo,
        categoryInfoDelete: CategoryInfo
    ) {
        viewModelScope.launch(IO) {
            repository.updateTaskAndAddDeleteCategory(taskInfo, categoryInfoAdd, categoryInfoDelete)
        }
    }

    fun deleteTaskAndCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo) {
        viewModelScope.launch(IO) {
            repository.deleteTaskAndCategory(taskInfo, categoryInfo)
        }
    }

    fun getUncompletedTask(): LiveData<List<TaskCategoryInfo>> {
        return repository.getUncompletedTask()
    }

    fun getCompletedTask(): LiveData<List<TaskCategoryInfo>> {
        return repository.getCompletedTask()
    }

    fun getUncompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>> {
        return repository.getUncompletedTaskOfCategory(category)
    }

    fun getCompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>> {
        return repository.getCompletedTaskOfCategory(category)
    }

    fun getNoOfTaskForEachCategory(): LiveData<List<NoOfTaskForEachCategory>> {
        return repository.getNoOfTaskForEachCategory()
    }

    fun getCategories(): LiveData<List<CategoryInfo>> {
        return repository.getCategories()
    }

    suspend fun getCountOfCategory(category: String): Int {
        var count: Int
        coroutineScope() {
            count = withContext(IO) { repository.getCountOfCategory(category) }
        }
        return count
    }

    fun getAlarms(currentTime: Date) {
        CoroutineScope(Dispatchers.Main).launch {
            val list = repository.getActiveAlarms(currentTime)
            Log.d("DATA", list.toString())
        }
    }


    fun checkIfUserLogin(): String {
        return getAuthToken()
    }

    fun checkFirstTimeLogin(): Boolean {
        return getFirstTime()
    }

    fun checkIfUserSubscription(): String {
        return getUserSubscription()
    }

//    fun writeTokenToSharedPref(auth: String) {
//        saveAuthToken(auth)
//    }

    fun saveAuthToken(auth_token: String, refresh_token: String): Boolean {
        val editor = sharedPreferences.edit()
        editor.putString(PREF_TOKEN, auth_token)
        editor.putString(PREF_REFRESH_TOKEN, refresh_token)
        editor.putBoolean(PREF_FIRST_TIME, false)
        editor.apply()
        return true
    }

    fun updateFirstTime(statue: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("First_time", statue)
        editor.apply()
    }

    fun getAuthToken(): String {
        return sharedPreferences.getString(PREF_TOKEN, PREF_DEFAULT)!!
    }

    fun getFirstTime(): Boolean {
        return sharedPreferences.getBoolean("First_time", false)
    }

    fun saveUserSubscription(subscription: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putString(PREF_ROLE_TYPE, if (subscription) MANAGER else PREF_DEFAULT)
        editor.apply()
    }

    fun getUserSubscription(): String {
        return sharedPreferences.getString(PREF_ROLE_TYPE, PREF_DEFAULT)!!
    }


    fun saveUserEmployeeID(roleID: String) {
        val editor = sharedPreferences.edit()
        editor.putString(PREF_EMP_ID, roleID)
        editor.apply()
    }

    fun getUserEmployeeID(): String {
        return sharedPreferences.getString(PREF_EMP_ID, PREF_DEFAULT)!!
    }

    fun clearSharedPrefrence(): Boolean {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        return true
    }

    sealed class MainEvent {
        object FileSelected : MainEvent()
        data class Error(val error: String) : MainEvent()
        data class Uploading(val progress: Int) : MainEvent()
        object UploadSuccess : MainEvent()
    }

}