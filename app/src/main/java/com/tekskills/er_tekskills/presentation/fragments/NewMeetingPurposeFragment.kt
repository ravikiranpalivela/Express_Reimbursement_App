package com.tekskills.er_tekskills.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.AddMeetingRequest
import com.tekskills.er_tekskills.data.model.ClientNamesResponseItem
import com.tekskills.er_tekskills.data.model.LeadNamesResponseItem
import com.tekskills.er_tekskills.data.model.UserCoordinates
import com.tekskills.er_tekskills.data.util.Constants
import com.tekskills.er_tekskills.data.util.DateToString
import com.tekskills.er_tekskills.data.util.DateToString.Companion.convertDateTimeToString
import com.tekskills.er_tekskills.data.util.DateToString.Companion.convertDateToString
import com.tekskills.er_tekskills.databinding.FragmentPurposeMeetingBinding
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.view.spinner.SearchableSpinner
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.RestApiStatus
import com.tekskills.er_tekskills.utils.UtilsConstants
import java.util.Calendar
import java.util.Date

class NewMeetingPurposeFragment : Fragment()
//    , OnMapReadyCallback 
{
    private lateinit var binding: FragmentPurposeMeetingBinding
    private lateinit var navController: NavController
    private val args: NewMeetingPurposeFragmentArgs by navArgs()

    private lateinit var viewModel: MainActivityViewModel
    var meetingDate: Date = Date(Constants.MAX_TIMESTAMP)

    private var mClientNames = ArrayList<ClientNamesResponseItem>()
    private var selectClientPos: Int = 0

    private var mLeadNames = ArrayList<LeadNamesResponseItem>()
    private var selectLeadPos: Int = 0

    //Booking info
    var customerDropOffPlace: Place? = null
    var customerPickupPlace: Place? = null
    var transportationType: String? = null
    var distanceInKm: Double? = null

    //    var distanceInKmString: String? = null
    var priceInVNDString: String? = null
    private var sourceAutocompleteFragment: AutocompleteSupportFragment? = null

    private var destinationAutocompleteFragment: AutocompleteSupportFragment? = null

    var userCoordinationData: UserCoordinates? = null

    private var selectMOTPos = ""

    private var mModeType: ArrayList<String> = arrayListOf(
        "Bike", "Car", "Bus", "Train", "Flight"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_purpose_meeting,
            container,
            false
        )
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        navController = findNavController()

        viewModel.getClientNameList()
        viewModel.getEmployeeAllowences()

        observeDate()
        setOnClickListenerInit()
        initSourceGooglePlacesAutocomplete()
        setPlaceSourceSelectedActionHandler()
        initDestinationGooglePlacesAutocomplete()
        setPlaceDestinationSelectedActionHandler()
        loadBookingFragment()
    }

    private fun observeDate() {
        viewModel.resNewMeetingPurpose.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = GONE
                    if (it.data != null)
                        it.data.let { res ->
                            requireActivity().onBackPressed()
                        }
                    else {
                        Snackbar.make(
                            binding.root,
                            "Login Failed",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    binding.progress.visibility = GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

        viewModel.resEmployeeAllowence.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = GONE
                    if (it.data != null)
                        it.data.let { res ->
                            mModeType = ArrayList(res.travelType.split(",").toMutableList())
                            binding.sModeOfTravel.setItems(mModeType)
                        }
                    else {
                        Snackbar.make(
                            binding.root,
                            "Login Failed",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    binding.progress.visibility = GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

        viewModel.resLeadNameList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = GONE
                    if (it.data != null)
                        it.data.let { res ->
                            mLeadNames = res
                            binding.sLeads.setItems(res)
                        }
                    else {
                        Snackbar.make(
                            binding.root,
                            "Login Failed",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    binding.progress.visibility = GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

        viewModel.resClientNameList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = GONE
                    if (it.data != null)
                        it.data.let { res ->
                            mClientNames = res
                            binding.sClients.setItems(res)
                        }
                    else {
                        Snackbar.make(
                            binding.root,
                            "Login Failed",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                RestApiStatus.LOADING -> {
                    binding.progress.visibility = VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    binding.progress.visibility = GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

    }

    private fun setOnClickListenerInit() {
        binding.sModeOfTravel.setItems(mModeType)

        binding.sModeOfTravel.setOnItemSelectListener(object :
            SearchableSpinner.SearchableItemListener {
            override fun onItemSelected(view: View?, position: Int) {
                selectMOTPos = mModeType[position]

                if (!(selectMOTPos == "Train" || selectMOTPos == "Flight")) {
                    binding.llDistance.visibility = VISIBLE
                } else {
                    binding.llDistance.visibility = GONE
                }
            }

            override fun onSelectionClear() {
                binding.llDistance.visibility = VISIBLE
            }
        })

        binding.sLeads.setOnItemSelectListener(object :
            SearchableSpinner.SearchableItemListener {
            override fun onItemSelected(view: View?, position: Int) {
                selectLeadPos = mLeadNames[position].leadID
                binding.edtContactName.setText(mLeadNames[position].leadName)
                binding.edtContactNo.setText(mLeadNames[position].leadContact)
                binding.edtEmailId.setText(mLeadNames[position].leadEmail)
            }

            override fun onSelectionClear() {
                selectLeadPos = 0
                binding.edtContactName.setText("")
                binding.edtContactNo.setText("")
                binding.edtEmailId.setText("")
            }
        })

        binding.sClients.setOnItemSelectListener(object :
            SearchableSpinner.SearchableItemListener {
            override fun onItemSelected(view: View?, position: Int) {
                selectClientPos = mClientNames[position].clientId
                binding.edtClientName.setText(mClientNames[position].clientName)
                viewModel.getLeadsNameList(selectClientPos.toString())
            }

            override fun onSelectionClear() {
                selectClientPos = 0
                binding.edtClientName.setText("")
            }
        })

        binding.ivCloseAddClient.setOnClickListener {
            binding.llAddingNewClient.visibility = GONE
            binding.edtClientName.setText("")
            binding.edtContactName.setText("")
            binding.edtContactNo.setText("")
            binding.edtEmailId.setText("")
        }

        binding.ivCloseAddClient.setOnClickListener {
            binding.llAddingNewClient.visibility = GONE
        }

        binding.ivAddNewClient.setOnClickListener {
            val isClientNameVisible = binding.edtClientName.isVisible

            if (!isClientNameVisible) {
                binding.edtClientName.setText("")
                binding.edtContactName.setText("")
                binding.edtContactNo.setText("")
                binding.edtEmailId.setText("")
            }

//            binding.sClients.isSelected = false
            binding.sClients.setSelection(null)
            selectClientPos = 0

            binding.edtClientName.visibility = if (isClientNameVisible) GONE else VISIBLE
            binding.llContactDetails.visibility =
                if (isClientNameVisible) VISIBLE else GONE
            binding.llAddContactDetails.visibility =
                if (isClientNameVisible) GONE else VISIBLE

            binding.ivAddNewClient.background = if (isClientNameVisible) {
                resources.getDrawable(R.drawable.ic_baseline_add_24, null)
            } else {
                resources.getDrawable(R.drawable.ic_delete, null)
            }
        }

        binding.ivAddNewContactPerson.setOnClickListener {

            binding.sLeads.setSelection(null)
            selectLeadPos = 0

            binding.llAddContactDetails.visibility =
                if (binding.llAddContactDetails.isVisible) GONE
                else
                    VISIBLE

            binding.ivAddNewContactPerson.background =
                if (binding.llAddContactDetails.isVisible) resources.getDrawable(R.drawable.ic_delete)
                else
                    resources.getDrawable(R.drawable.ic_baseline_add_24)
        }


        binding.edtDate.setOnClickListener {
            showDateTimePicker()
        }

        binding.btnSave.setOnClickListener {
            if (isValidate()) {
                saveMeeting()
            }
        }

        binding.fab.setOnClickListener {
            if (isValidate()) {
                saveMeeting()
            }
        }

        binding.btnCancel.setOnClickListener {
            binding.edtClientName.setText("")
            binding.edtVisitPurpose.setText("")
            meetingDate = Date(Constants.MAX_TIMESTAMP)
            binding.edtContactNo.setText("")
            binding.edtEmailId.setText("")
            binding.edtContactName.setText("")
            userCoordinationData = null

            val pickupFragment = BookingFragment()
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.booking_info_data, pickupFragment).commit()
        }
    }

    private fun saveMeeting() {
        try {

            val visitData = AddMeetingRequest(
                modeOfTravel = selectMOTPos,
                visitPurpose = binding.edtVisitPurpose.text.toString(),
                visitDate = convertDateToString(meetingDate),
                visitTime = convertDateTimeToString(meetingDate),
                leadId = selectLeadPos,
                clientId = selectClientPos,
//                    noOfDays = binding.edtNoOfDays.text.toString(),
                customerName = if (selectLeadPos > 0) "" else binding.edtClientName.text.toString(),
                customerPhone = if (selectLeadPos > 0) "" else binding.edtContactNo.text.toString(),
                customerEmail = if (selectLeadPos > 0) "" else binding.edtEmailId.text.toString(),
                customerContactName = if (selectClientPos > 0) "" else binding.edtContactName.text.toString(),

                opportunity = binding.edtOpportunity.text.toString(),
                employeeId = viewModel.getUserEmployeeID().toInt(),
                userCoordinates = userCoordinationData!!
            )

            Log.d("TAG", "saveMeeting: request ${visitData.toString()}")

            viewModel.addMeetingPurpose(visitData)
        } catch (e: Exception) {
            Log.d("TAG", "saveMeeting: request ${e.message.toString()}")

        }
    }

    /**
     * Init Source GooglePlacesAutocomplete search bar
     */
    private fun initSourceGooglePlacesAutocomplete() {
        //Init the SDK
        val apiKey = getString(R.string.google_maps_key)
        if (!Places.isInitialized()) {
            Places.initialize(requireActivity().applicationContext, apiKey)
        }
//        placesClient = Places.createClient(requireActivity().applicationContext)

        // Initialize the AutocompleteSupportFragment.
        sourceAutocompleteFragment =
            childFragmentManager.findFragmentById(R.id.source_maps_place_autocomplete_fragment) as AutocompleteSupportFragment?

        // Specify the types of place data to return.
        sourceAutocompleteFragment!!.setPlaceFields(
            listOf<Place.Field>(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.PLUS_CODE
            )
        )
        sourceAutocompleteFragment!!.setHint("Add source location")
    }

    private fun setPlaceSourceSelectedActionHandler() {
        sourceAutocompleteFragment!!.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {

                if (place == null) return

                viewModel.setCustomerSelectedPickupPlace(place)
            }

            override fun onError(status: Status) {
                Toast.makeText(
                    activity!!.applicationContext,
                    UtilsConstants.ToastMessage.placeAutocompleteError + status, Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    /**
     * Init Source GooglePlacesAutocomplete search bar
     */
    private fun initDestinationGooglePlacesAutocomplete() {
        //Init the SDK
        val apiKey = getString(R.string.google_maps_key)
        if (!Places.isInitialized()) {
            Places.initialize(requireActivity().applicationContext, apiKey)
        }

        // Initialize the AutocompleteSupportFragment.
        destinationAutocompleteFragment =
            childFragmentManager.findFragmentById(R.id.destination_maps_place_autocomplete_fragment) as AutocompleteSupportFragment?

        // Specify the types of place data to return.
        destinationAutocompleteFragment!!.setPlaceFields(
            listOf<Place.Field>(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.PLUS_CODE
            )
        )

        destinationAutocompleteFragment!!.setHint("Add Destination location")

    }

    private fun setPlaceDestinationSelectedActionHandler() {
        destinationAutocompleteFragment!!.setOnPlaceSelectedListener(object :
            PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                if (place == null) return
                viewModel.setCustomerSelectedDropOffPlace(place)
            }

            override fun onError(status: Status) {
                Toast.makeText(
                    activity!!.applicationContext,
                    UtilsConstants.ToastMessage.placeAutocompleteError + status, Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun loadBookingFragment() {
        val pickupFragment = BookingFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.booking_info_data, pickupFragment).commit()
    }


    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            viewModel.getDistanceInKmString()!!
                .observe(viewLifecycleOwner, Observer<Double?> { s ->
                    if (s == null) return@Observer
                    binding.distanceLocation.text = "${s}Km"
                    distanceInKm = s
                    addingUserCoordinates()
//                setCheckoutInfo()
                })
            viewModel.getDropOffPlaceString()!!.observe(
                viewLifecycleOwner
            ) { s -> binding.addDestinationLocation!!.text = s }

            viewModel.getPickupPlaceString()!!.observe(
                viewLifecycleOwner
            ) { s -> binding.addSourceLocation!!.text = s }

            viewModel.getCustomerSelectedDropOffPlace()
                .observe(viewLifecycleOwner, Observer<Any?> { place ->
                    if (place == null) return@Observer
                    customerDropOffPlace = place as Place?
                })

            viewModel!!.getCustomerSelectedPickupPlace()
                .observe(viewLifecycleOwner, Observer<Any?> { place ->
                    if (place == null) return@Observer
                    customerPickupPlace = place as Place?

                    if (customerDropOffPlace != null && customerPickupPlace != null) {
//                        addingUserCoordinates()
                    }
                })

        } catch (e: NullPointerException) {
            Log.e("TAG", "onActivityCreated: ${e.message}", e)
        }
    }

    /**
     * Adding User Coordinates
     */
    private fun addingUserCoordinates() {
        userCoordinationData = UserCoordinates(
            sourceLatitude = customerPickupPlace!!.latLng!!.latitude.toString(),
            sourceLongitude = customerPickupPlace!!.latLng!!.longitude.toString(),
            source = "${customerPickupPlace!!.name!!}\n${customerPickupPlace!!.address!!}",
            destinationLatitude = customerDropOffPlace!!.latLng!!.latitude.toString(),
            destinationLongitude = customerDropOffPlace!!.latLng!!.longitude.toString(),
            destination = "${customerDropOffPlace!!.name!!}\n${customerDropOffPlace!!.address!!}",
            totalDistance = distanceInKm!!,
            checkInTime = "",
            checkOutTime = "",
            checkInCordinates = "",
            checkOutCordinates = "",
            mapTime = distanceInKm.toString()
        )
    }


    private fun showDateTimePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a Visit Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) // Default selection to today
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now()) // Prevents selecting past dates
                    .build()
            )
            .build()

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setTitleText("Select a Visit Time")
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            meetingDate = calendar.time
            binding.edtDate.setText(DateToString.convertDateToStringDateWise(meetingDate))
            timePicker.show(childFragmentManager, "TAG")
        }

        timePicker.addOnPositiveButtonClickListener {
            val cal = Calendar.getInstance()
            cal.time = meetingDate
            cal.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            cal.set(Calendar.MINUTE, timePicker.minute)
            cal.set(Calendar.SECOND, 5)
            meetingDate = cal.time
            binding.edtDate.setText(DateToString.convertDateToStringDateTimeWise(meetingDate))
        }

        datePicker.show(childFragmentManager, "TAG")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sourceAutocompleteFragment!!.setOnPlaceSelectedListener(null)
        destinationAutocompleteFragment!!.setOnPlaceSelectedListener(null)
    }

    private fun isValidate(): Boolean {
        var isValid = true

        if (!validateVisitPurpose()) isValid = false
        if (!validateMeetingDate()) isValid = false
        if (!validateOpportunity()) isValid = false
        // if (!validateNoOfDays()) isValid = false
        if (!validateClientName()) isValid = false
        if (!validateLeadInfo()) isValid = false
        if (!validateContactEmail()) isValid = false
        if (!validateContact()) isValid = false
        if (!validateContactName()) isValid = false
        if (!validateUserCordinates()) isValid = false

        return isValid
    }

    private fun validateUserCordinates(): Boolean {
        if (userCoordinationData == null) {
            binding.distanceLocation.error = "Required Field!"
            Snackbar.make(binding.root, "Source And Destination Missing", Snackbar.LENGTH_SHORT)
                .show()
            binding.distanceLocation.requestFocus()
            return false
        } else {
            binding.distanceLocation.error = null
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateVisitPurpose(): Boolean {
        if (binding.edtVisitPurpose.text.toString().trim().isEmpty()) {
            binding.edtVisitPurpose.setError("Required Field!")
            binding.edtVisitPurpose.requestFocus()
            return false
        } else {
            binding.edtVisitPurpose.error = null
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateOpportunity(): Boolean {
        if (binding.edtOpportunity.text.toString().trim().isEmpty()) {
            binding.edtOpportunity.setError("Required Field!")
            binding.edtOpportunity.requestFocus()
            return false
        } else {
            binding.edtOpportunity.error = null
        }
        return true
    }

    /**
     * field must not be empty
     */
    private fun validateNoOfDays(): Boolean {
        if (binding.edtNoOfDays.text.toString().trim().isEmpty()) {
            binding.edtNoOfDays.setError("Required Field!")
            binding.edtNoOfDays.requestFocus()
            return false
        } else {
            binding.edtNoOfDays.error = null
        }
        return true
    }

    private fun validateMeetingDate(): Boolean {
        if (binding.edtDate.text.toString().trim().isEmpty()) {
            binding.edtDate.setError("Required Field!")
            binding.edtDate.requestFocus()
            return false
        } else {
            binding.edtDate.error = null
        }
        return true
    }

    private fun validateClientName(): Boolean {

        if (selectClientPos == 0) {
            if (binding.edtClientName.text.toString().trim().isEmpty()) {
                binding.edtClientName.setError("Required Field!")
                binding.sClients.setError("Please Add Client!")
                binding.edtClientName.requestFocus()
                return false
            } else {
                binding.edtClientName.error = null
                binding.sClients.setError(null)
            }
        } else {
            binding.edtClientName.error = null
            binding.sClients.setError(null)
        }
        return true
    }

    private fun validateSourceLoc(): Boolean {
        if (binding.addSourceLocation.text.toString().trim().isEmpty()) {
            binding.addSourceLocation.setError("Required Field!")
            binding.addSourceLocation.requestFocus()
            return false
        } else {
            binding.addSourceLocation.error = null
        }
        return true
    }

    private fun validateDestinationLoc(): Boolean {
        if (binding.addDestinationLocation.text.toString().trim().isEmpty()) {
            binding.addDestinationLocation.setError("Required Field!")
            binding.addDestinationLocation.requestFocus()
            return false
        } else {
            binding.addDestinationLocation.error = null
        }
        return true
    }

    private fun validateLeadInfo(): Boolean {
        val mobile = binding.edtContactNo.text.toString().trim()
        val email = binding.edtEmailId.text.toString().trim()
        val name = binding.edtContactName.text.toString().trim()

        if (mobile.isEmpty() || email.isEmpty() || name.isEmpty()) {
            binding.sLeads.setError("Complete Lead Details")
            return false
        }
        binding.sLeads.setError(null)
        return true
    }

    private fun validateContact(): Boolean {
        val mobile = binding.edtContactNo.text.toString().trim()
        if (mobile.isEmpty()) {
            binding.edtContactNo.setError("Empty Contact Number")
            binding.edtContactNo.requestFocus()
            return false
        } else if (!mobile.matches(Regex("^[0-9]{10}$"))) {
            binding.edtContactNo.setError("Invalid Contact Number")
            binding.edtContactNo.requestFocus()
            return false
        }
        binding.edtContactNo.error = null
        return true
    }

    private fun validateContactEmail(): Boolean {
        val mobile = binding.edtEmailId.text.toString().trim()
        if (mobile.isEmpty()) {
            binding.edtEmailId.setError("Empty Email ID")
            binding.edtEmailId.requestFocus()
            return false
        } else if (!mobile.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) {
            binding.edtEmailId.setError("Invalid Email ID")
            binding.edtEmailId.requestFocus()
            return false
        }
        binding.edtEmailId.error = null
        return true
    }

    private fun validateContactName(): Boolean {
        if (binding.edtContactName.text.toString().trim().isEmpty()) {
            binding.edtContactName.setError("Required Field!")
            binding.edtContactName.requestFocus()
            return false
        } else {
            binding.edtContactName.error = null
        }
        return true
    }
}