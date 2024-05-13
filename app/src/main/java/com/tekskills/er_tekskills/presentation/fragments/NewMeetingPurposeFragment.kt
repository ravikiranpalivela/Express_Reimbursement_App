package com.tekskills.er_tekskills.presentation.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.snackbar.Snackbar
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.data.model.AddMeetingRequest
import com.tekskills.er_tekskills.data.model.UserCoordinates
import com.tekskills.er_tekskills.data.util.Constants
import com.tekskills.er_tekskills.data.util.DateToString
import com.tekskills.er_tekskills.data.util.DateToString.Companion.convertDateToString
import com.tekskills.er_tekskills.databinding.FragmentPurposeMeetingBinding
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.view.spinner.SearchableSpinner
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.RestApiStatus
import com.tekskills.er_tekskills.utils.UtilsConstants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class NewMeetingPurposeFragment : Fragment()
//    , OnMapReadyCallback 
{
    private lateinit var binding: FragmentPurposeMeetingBinding
    private lateinit var navController: NavController
    private val args: NewMeetingPurposeFragmentArgs by navArgs()

    private lateinit var viewModel: MainActivityViewModel
    var meetingDate: Date = Date(Constants.MAX_TIMESTAMP)

    private var mMap: GoogleMap? = null
    private var locationClient: FusedLocationProviderClient? = null
    private var currentUserLocationMarker: Marker? = null
    private var currentUserLocation: Location? = null
    private val currentRoute: ArrayList<Polyline> = ArrayList<Polyline>()

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

    private var mClientNames: ArrayList<String> = arrayListOf(
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        navController = findNavController()

        viewModel.getEmployeeAllowences()

        initSourceGooglePlacesAutocomplete()
        setPlaceSourceSelectedActionHandler()
        initDestinationGooglePlacesAutocomplete()
        setPlaceDestinationSelectedActionHandler()

        binding.sModeOfTravel.setItems(mClientNames)

        binding.sModeOfTravel.setOnItemSelectListener(object :
            SearchableSpinner.SearchableItemListener {
            override fun onItemSelected(view: View?, position: Int) {
                selectMOTPos = mClientNames[position]

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

        viewModel.resEmployeeAllowence.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null)
                        it.data.let { res ->
                            mClientNames = ArrayList(res.travelType.split(",").toMutableList())
                            binding.sModeOfTravel.setItems(mClientNames)
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
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })


        viewModel.resNewMeetingPurpose.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it.status) {
                RestApiStatus.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    if (it.data != null)
                        it.data.let { res ->
                            requireActivity().onBackPressed()
//                            val intent = Intent(requireActivity(), MainActivity::class.java)
//                            startActivity(intent)
//                            requireActivity().finish()
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
                    binding.progress.visibility = View.VISIBLE
                }

                RestApiStatus.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(binding.root, "Something went wrong", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        })

//        binding.addSourceLocation.setOnClickListener {
//            loadBookingFragment()
//        }
//        binding.addSourceLocationMap.setOnClickListener {
//            loadBookingFragment()
//        }
//
//        binding.addDestinationLocation.setOnClickListener {
//            loadBookingFragment()
//        }
//        binding.addDestinationLocationMap.setOnClickListener {
//            loadBookingFragment()
//        }

        binding.edtDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnSave.setOnClickListener {
            if (isValidate()) {

                val visitData = AddMeetingRequest(
                    modeOfTravel = selectMOTPos,
                    customerName = binding.edtClientName.text.toString(),
                    visitPurpose = binding.edtVisitPurpose.text.toString(),
                    visitDate = convertDateToString(meetingDate),
//                    noOfDays = binding.edtNoOfDays.text.toString(),
                    customerPhone = binding.edtContactNo.text.toString(),
                    customerEmail = binding.edtEmailId.text.toString(),
                    description = binding.edtDescription.text.toString(),
                    employeeId = viewModel.getUserEmployeeID().toInt(),
                    userCoordinates = userCoordinationData!!
                )

                Log.d("TAG", "onViewCreated: print response ${visitData.toString()}")

                viewModel.addMeetingPurpose(visitData)
                Toast.makeText(requireActivity(), "validated", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCancel.setOnClickListener {
            binding.edtClientName.setText("")
            binding.edtVisitPurpose.setText("")
            meetingDate = Date(Constants.MAX_TIMESTAMP)
            binding.edtContactNo.setText("")
            binding.edtEmailId.setText("")
            binding.edtDescription.setText("")
            userCoordinationData = null

            val pickupFragment = BookingFragment()
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.booking_info_data, pickupFragment).commit()
        }



        loadBookingFragment()
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
//                smoothlyMoveCameraToPosition(place.getLatLng(), Constants.GoogleMaps.CameraZoomLevel.betweenCityAndStreets);
                //Send customer selected drop off place to booking fragment
                if (place == null) return
                //                smoothlyMoveCameraToPosition(place.getLatLng(), Constants.GoogleMaps.CameraZoomLevel.betweenCityAndStreets);
                //Send customer selected drop off place to booking fragment
//                val bookingViewModel: MainActivityViewModel = ViewModelProvider(requireActivity()).get(
//                    MainActivityViewModel::class.java
//                )
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
//        placesClient = Places.createClient(requireActivity().applicationContext)

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
//                smoothlyMoveCameraToPosition(place.getLatLng(), Constants.GoogleMaps.CameraZoomLevel.betweenCityAndStreets);
                //Send customer selected drop off place to booking fragment
                if (place == null) return
                //                smoothlyMoveCameraToPosition(place.getLatLng(), Constants.GoogleMaps.CameraZoomLevel.betweenCityAndStreets);
                //Send customer selected drop off place to booking fragment
//                val bookingViewModel: MainActivityViewModel = ViewModelProvider(requireActivity()).get(
//                    MainActivityViewModel::class.java
//                )
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
                    createNewBookingInDB()
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
//                binding.fragmentMapsBackBtn.visibility = View.VISIBLE //Show back button
//                binding.edtDestination.text = (place?.toString() ?: "") as Editable?

//                    //TODO Move to customerPickUpPlace fragment
//                    if (customerPickupPlace != null)
//                        loadPickupPlacePickerFragment()

//                    if (currentUserLocation != null) {
//                        smoothlyMoveCameraToPosition(
//                            LatLng(currentUserLocation!!.latitude, currentUserLocation!!.longitude),
//                            UtilsConstants.GoogleMaps.CameraZoomLevel.betweenStreetsAndBuildings
//                        )
//                    }
                    //TODO Draw 2 pickup/drop-off markers

                    if (customerDropOffPlace != null && customerPickupPlace != null) {
                        //TODO load checkout fragment
//                        loadCheckoutFragment()

//                        drawDropOffAndPickupMarkers()

                        //TODO Draw route from pickup place to drop-off place
//                        drawRouteFromPickupToDropOff()

//                        createNewBookingInDB()
                    }
                })

            viewModel!!.getCustomerSelectedPickupPlace()
                .observe(viewLifecycleOwner, Observer<Any?> { place ->
                    if (place == null) return@Observer
                    customerPickupPlace = place as Place?

                    if (customerDropOffPlace != null && customerPickupPlace != null) {
//                        createNewBookingInDB()
                    }
                })

        } catch (e: NullPointerException) {
            Log.e("TAG", "onActivityCreated: ${e.message}", e)
        }
    }

    /**
     * Create booking in db
     */
    private fun createNewBookingInDB() {

        userCoordinationData = UserCoordinates(
            sourceLatitude = customerPickupPlace!!.latLng!!.latitude.toString(),
            sourceLongitude = customerPickupPlace!!.latLng!!.longitude.toString(),
            source = "${customerPickupPlace!!.name!!}\n${customerPickupPlace!!.address!!}",
            destinationLatitude = customerDropOffPlace!!.latLng!!.latitude.toString(),
            destinationLongitude = customerDropOffPlace!!.latLng!!.longitude.toString(),
            destination = "${customerDropOffPlace!!.name!!}\n${customerDropOffPlace!!.address!!}",
            totalDistance = distanceInKm!!
        )

//       userCoordinationData["userCordinates"] = mapOf(
//            "sourceLatitude" to customerPickupPlace!!.getLatLng().latitude,
//            "sourceLongitude" to customerPickupPlace!!.getLatLng().longitude,
//            "sourceAddress" to customerPickupPlace!!.getAddress(),
//            "destinantionLatitude" to customerDropOffPlace!!.getLatLng().latitude,
//            "destinantionLongitude" to customerDropOffPlace!!.getLatLng().longitude,
//            "destinantionAddress" to customerDropOffPlace!!.getAddress(),
//            "totalDistance" to distanceInKmString.toDouble()
//        )

//        val data: MutableMap<String, Any?> = HashMap()
//        data[UtilsConstants.FSBooking.pickupPlaceAddress] = customerPickupPlace!!.getAddress()
//        data[UtilsConstants.FSBooking.pickUpPlaceLatitude] =
//            customerPickupPlace!!.getLatLng().latitude
//        data[UtilsConstants.FSBooking.pickUpPlaceLongitude] =
//            customerPickupPlace!!.getLatLng().longitude
//        data[UtilsConstants.FSBooking.dropOffPlaceAddress] = customerDropOffPlace!!.getAddress()
//        data[UtilsConstants.FSBooking.dropOffPlaceLatitude] =
//            customerDropOffPlace!!.getLatLng().latitude
//        data[UtilsConstants.FSBooking.dropOffPlaceLongitude] =
//            customerDropOffPlace!!.getLatLng().longitude
//        data[UtilsConstants.FSBooking.distanceInKm] = distanceInKmString
//        db.collection(UtilsConstants.FSBooking.bookingCollection)
//            .add(data)
//            .addOnSuccessListener(object : OnSuccessListener<DocumentReference?> {
//                override fun onSuccess(documentReference: DocumentReference) {
//                    currentBookingDocRef = documentReference
//                    setDetectAcceptedDriver()
//                }
//            })
//            .addOnFailureListener(object : OnFailureListener {
//                override fun onFailure(e: Exception) {
//                    Toast.makeText(
//                        requireActivity(),
//                        UtilsConstants.ToastMessage.addNewBookingToDbFail,
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    resetBookingFlow()
//                }
//            })
    }

    private fun showDatePicker() {
        val date = System.currentTimeMillis()
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val dateString = sdf.format(date)
        val myCal: Calendar = GregorianCalendar()
        try {
            val theDate = sdf.parse(dateString)
            myCal.time = theDate
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        val _date: DatePickerDialog = object : DatePickerDialog(
            requireContext(), mDateSetListener,
            myCal[Calendar.YEAR], myCal[Calendar.MONTH],
            myCal[Calendar.DAY_OF_MONTH]
        ) {
            override fun onDateChanged(
                view: DatePicker,
                year: Int,
                monthOfYear: Int,
                dayOfMonth: Int
            ) {
                if (year < myCal[Calendar.YEAR]) view.updateDate(
                    myCal[Calendar.YEAR],
                    myCal[Calendar.MONTH], myCal[Calendar.DAY_OF_MONTH]
                )
                if (monthOfYear < myCal[Calendar.MONTH] && year == myCal[Calendar.YEAR]) view.updateDate(
                    myCal[Calendar.YEAR], myCal[Calendar.MONTH], myCal[Calendar.DAY_OF_MONTH]
                )
                if (dayOfMonth < myCal[Calendar.DAY_OF_MONTH] && year == myCal[Calendar.YEAR] && monthOfYear == myCal[Calendar.MONTH]) view.updateDate(
                    myCal[Calendar.YEAR], myCal[Calendar.MONTH], myCal[Calendar.DAY_OF_MONTH]
                )
            }
        }
        _date.show()
    }

    private val mDateSetListener =
        OnDateSetListener { view, yearSelected, monthOfYear, dayOfMonth ->

            val calendar = Calendar.getInstance()
//            calendar.timeInMillis = it
            calendar.set(Calendar.YEAR, yearSelected)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            meetingDate = calendar.time
            binding.edtDate.setText(DateToString.convertDateToStringDateWise(meetingDate))

//            year = yearSelected
//            month = monthOfYear + 1
//            day = dayOfMonth
//
//
//            binding.edtDate.setText("" + year + "/" + month + "/" + day)
//            if (day < 10) {
//                StartDate =
//                    "0" + year + "/" + month + "/" + day
//            } else {
//                StartDate =
//                    "" + year + "/" + month + "/" + day
//            }
        }


    override fun onDestroyView() {
        super.onDestroyView()
        sourceAutocompleteFragment!!.setOnPlaceSelectedListener(null)
        destinationAutocompleteFragment!!.setOnPlaceSelectedListener(null)

    }

    private fun isValidate(): Boolean =
        validateClientName() && validateMeetingTitle() && validateMeetingDate()
//                && validateNoOfDays()
                && validateContactEmail() && validateContact() && validateDescription()
                && validateUserCordinates()

    private fun validateUserCordinates(): Boolean {
        if (userCoordinationData == null) {
            binding.distanceLocation.error = "Required Field!"
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
    private fun validateMeetingTitle(): Boolean {
        if (binding.edtVisitPurpose.text.toString().trim().isEmpty()) {
            binding.edtVisitPurpose.error = "Required Field!"
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
    private fun validateNoOfDays(): Boolean {
        if (binding.edtNoOfDays.text.toString().trim().isEmpty()) {
            binding.edtNoOfDays.error = "Required Field!"
            binding.edtNoOfDays.requestFocus()
            return false
        } else {
            binding.edtNoOfDays.error = null
        }
        return true
    }

    private fun validateMeetingDate(): Boolean {
        if (binding.edtDate.text.toString().trim().isEmpty()) {
            binding.edtDate.error = "Required Field!"
            binding.edtDate.requestFocus()
            return false
        } else {
            binding.edtDate.error = null
//            binding.dateEscalatedMeetingDate.isErrorEnabled = false
        }
        return true
    }

    private fun validateClientName(): Boolean {
        if (binding.edtClientName.text.toString().trim().isEmpty()) {
            binding.edtClientName.error = "Required Field!"
            binding.edtClientName.requestFocus()
            return false
        } else {
            binding.edtClientName.error = null
//            binding.dateEscalatedMeetingDate.isErrorEnabled = false
        }
        return true
    }

    private fun validateSourceLoc(): Boolean {
        if (binding.addSourceLocation.text.toString().trim().isEmpty()) {
            binding.addSourceLocation.error = "Required Field!"
            binding.addSourceLocation.requestFocus()
            return false
        } else {
            binding.addSourceLocation.error = null
//            binding.dateEscalatedMeetingDate.isErrorEnabled = false
        }
        return true
    }

    private fun validateDestinationLoc(): Boolean {
        if (binding.addDestinationLocation.text.toString().trim().isEmpty()) {
            binding.addDestinationLocation.error = "Required Field!"
            binding.addDestinationLocation.requestFocus()
            return false
        } else {
            binding.addDestinationLocation.error = null
//            binding.dateEscalatedMeetingDate.isErrorEnabled = false
        }
        return true
    }

    private fun validateContact(): Boolean {
        val mobile = binding.edtContactNo.text.toString().trim()
        if (mobile.isEmpty()) {
            binding.edtContactNo.error = "Empty Contact Number"
            binding.edtContactNo.requestFocus()
            return false
        } else if (!mobile.matches(Regex("^[0-9]{10}$"))) {
            binding.edtContactNo.error = "Invalid Contact Number"
            binding.edtContactNo.requestFocus()
            return false
        }
        binding.edtContactNo.error = null
        return true
    }

    private fun validateContactEmail(): Boolean {
        val mobile = binding.edtEmailId.text.toString().trim()
        if (mobile.isEmpty()) {
            binding.edtEmailId.error = "Empty Email ID"
            binding.edtEmailId.requestFocus()
            return false
        } else if (!mobile.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))) {
            binding.edtEmailId.error = "Invalid Email ID"
            binding.edtEmailId.requestFocus()
            return false
        }
        binding.edtEmailId.error = null
        return true
    }

    private fun validateDescription(): Boolean {
        if (binding.edtDescription.text.toString().trim().isEmpty()) {
            binding.edtDescription.error = "Required Field!"
            binding.edtDescription.requestFocus()
            return false
        } else {
            binding.edtDescription.error = null
//            binding.dateEscalatedMeetingDate.isErrorEnabled = false
        }
        return true
    }

//    override fun onMapReady(p0: GoogleMap) {
//        val apiKey = getString(R.string.google_maps_key)
//        if (!Places.isInitialized()) { //Init GooglePlaceAutocomplete if not existed
//            Places.initialize(requireActivity().applicationContext, apiKey)
//        }
//        placesClient = Places.createClient(requireActivity().applicationContext)
//        mMap = p0
//        requestPermission() //Request user for location permission
//        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//        mMap!!.uiSettings.isZoomControlsEnabled = true
//        startLocationUpdate() //Start location update listener
//        //        setUpCluster(); //Set up cluster on Google Map
//        onGetPositionClick() // Position the map.
//    }

//    /**
//     * //Start location update listener
//     */
//    @SuppressLint("MissingPermission", "RestrictedApi")
//    private fun startLocationUpdate() {
//        locationRequest = LocationRequest()
//        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//        locationRequest!!.setInterval((5 * 1000).toLong()) //5s
//        locationRequest!!.setFastestInterval((5 * 1000).toLong()) //5s
//        locationClient!!.requestLocationUpdates(
//            locationRequest!!,
//            object : LocationCallback() {
//                override fun onLocationResult(locationResult: LocationResult) {
//                    super.onLocationResult(locationResult)
//                    val location: Location = locationResult.lastLocation!!
//                    val latLng = LatLng(
//                        location.latitude,
//                        location.longitude
//                    )
//                    updateCurrentUserLocationMarker(latLng)
//                    //                        updateCurrentRoute();
//                }
//            }, null
//        )
//    }
//
//    /**
//     * Request user for location permission
//     */
//    private fun requestPermission() {
//        ActivityCompat.requestPermissions(
//            requireActivity(), arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
//            MY_LOCATION_REQUEST
//        )
//    }

}