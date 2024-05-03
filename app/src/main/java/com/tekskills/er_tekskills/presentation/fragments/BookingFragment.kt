package com.tekskills.er_tekskills.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.clustering.ClusterManager
import com.tekskills.er_tekskills.R
import com.tekskills.er_tekskills.databinding.FragmentCustomerBookingBinding
import com.tekskills.er_tekskills.presentation.activities.MainActivity
import com.tekskills.er_tekskills.presentation.viewmodel.MainActivityViewModel
import com.tekskills.er_tekskills.utils.GoogleMaps.MyClusterItem
import com.tekskills.er_tekskills.utils.GoogleMaps.utilities.DirectionsJSONParser
import com.tekskills.er_tekskills.utils.UtilsConstants
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Objects

class BookingFragment : Fragment(),
    OnMapReadyCallback {
    private var mViewModel: MainActivityViewModel? = null
    private lateinit var binding: FragmentCustomerBookingBinding

    //Maps marker clustering
    private val clusterManager: ClusterManager<MyClusterItem>? = null
    private var supportMapFragment: SupportMapFragment? = null //maps view
    private var mMap: GoogleMap? = null
    private var locationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var currentPickupLocationMarker: Marker? = null
    private var currentDropOffLocationMarker: Marker? = null
    private var currentUserLocationMarker: Marker? = null
    private var currentDriverLocationMarker: Marker? = null
    private var currentUserLocation: Location? = null
    private val prevUserLocation: LatLng? = null
    private val currentTargetLocationClusterItem: MyClusterItem? = null
    private val prevTargetLocation: LatLng? = null
    private val currentRoute: ArrayList<Polyline> = ArrayList<Polyline>()
    private var placesClient: PlacesClient? = null


    //Booking info
    var customerDropOffPlace: Place? = null
    var customerPickupPlace: Place? = null
    var transportationType: String? = null
    var distanceInKm: Double? = null
    var distanceInKmString: String? = null
    var priceInVNDString: String? = null

    //Booking flow
    var bookBtnPressed: Boolean? = null
    var cancelBookingBtnPressed: Boolean? = null

    /**
     * Init Google MapsFragment
     */
    private fun initMapsFragment() {
        supportMapFragment =
            childFragmentManager.findFragmentById(R.id.fragment_maps) as SupportMapFragment?
        supportMapFragment!!.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_customer_booking,
            container, false
        )
        return binding.root
    }


    /**
     * Set Action Handlers
     */
    private fun setActionHandlers() {
        setGetMyLocationBtnHandler() //Find My location Button listener
        setRestartBtnHandler()
    }

    /**
     * Set event listener for restart btn
     */
    private fun setRestartBtnHandler() {
        binding.fragmentMapsBackBtn.setOnClickListener(View.OnClickListener { resetBookingFlow() })
    }

    /**
     * Reset booking
     */
    private fun resetBookingFlow() {
        //Remove all markers if existed
        removeAllMarkers()
        //Remove current route
        removeCurrentRoute()
        //Go back to the picking drop-off place step
        loadPickupPlacePickerFragment()
        //Hide back btn
        binding.fragmentMapsBackBtn.setVisibility(View.GONE)
    }

    /**
     * Remove all the marker existing in the map fragment
     */
    private fun removeAllMarkers() {
        //Clear pickup/drop-off markers if exists
        if (currentPickupLocationMarker != null) {
            currentPickupLocationMarker!!.remove()
            currentPickupLocationMarker = null
        }

        //Clear drop-off markers if exists
        if (currentDropOffLocationMarker != null) {
            currentDropOffLocationMarker!!.remove()
            currentDropOffLocationMarker = null
        }
        if (currentDriverLocationMarker != null) {
            currentDriverLocationMarker!!.remove()
            currentDriverLocationMarker = null
        }
    }

    /**
     * Load drop-off picker fragment
     */
    private fun loadDropOffPlacePickerFragment() {
        //Load drop-off picker fragment
        val dropoffFragment = DropoffFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.booking_info, dropoffFragment).commit()
    }

    /**
     * Load pick up picker fragment
     */
    private fun loadPickupPlacePickerFragment() {
        val pickupFragment = PickupFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.booking_info, pickupFragment).commit()
    }

    /**
     * Load checkout fragment
     */
    private fun loadCheckoutFragment() {
        val checkoutFragment = CheckoutFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.booking_info, checkoutFragment).commit()
    }

    /**
     * Draw marker on dropoff and pickup map fragment
     */
    private fun drawDropOffAndPickupMarkers() {
        currentPickupLocationMarker = mMap!!.addMarker(
            MarkerOptions()
                .position(Objects.requireNonNull<LatLng>(customerPickupPlace!!.latLng))
                .icon(
                    bitmapDescriptorFromVector(
                        activity,
                        R.drawable.ic_location_blue, Color.BLUE
                    )
                )
                .title(customerPickupPlace!!.address)
        )
        currentDropOffLocationMarker = mMap!!.addMarker(
            MarkerOptions()
                .position(customerDropOffPlace!!.latLng)
                .icon(
                    bitmapDescriptorFromVector(
                        activity,
                        R.drawable.ic_location_red, Color.RED
                    )
                )
                .title(customerDropOffPlace!!.getAddress())
        )
        currentPickupLocationMarker!!.showInfoWindow()
        currentDropOffLocationMarker!!.showInfoWindow()

        //Smoothly move camera to include 2 points in the map
        val latLngBounds: LatLngBounds.Builder = LatLngBounds.Builder()
        customerDropOffPlace!!.latLng?.let { latLngBounds.include(it) }
        customerPickupPlace!!.latLng?.let { latLngBounds.include(it) }
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 200))
    }

    /**
     * Draw route from pickup location to drop off location on the map fragment
     */
    private fun drawRouteFromPickupToDropOff() {
        // Checks, whether start and end locations are captured
        // Getting URL to the Google Directions API
        val url = customerPickupPlace!!.latLng?.let {
            customerDropOffPlace!!.latLng?.let { it1 ->
                getRouteUrl(
                    it,
                    it1,
                    "driving"
                )
            }
        }
        val fetchRouteDataTask = FetchRouteDataTask()

        // Start fetching json data from Google Directions API
        fetchRouteDataTask.execute(url)
    }


//    /**
//     * Draw route from pickup location to drop off location on the map fragment
//     */
//    private fun drawRouteFromPickupToDropOff() {
//        // Checks, whether start and end locations are captured
//        // Getting URL to the Google Directions API
//        val url = customerPickupPlace?.let {
//            customerDropOffPlace?.latLng?.let { it1 ->
//                it.latLng?.let { it2 ->
//                    getRouteUrl(
//                        it2,
//                        it1,
//                        "driving"
//                    )
//                }
//            }
//        }
//        val fetchRouteDataTask = FetchRouteDataTask()
//
//        // Start fetching json data from Google Directions API
//        fetchRouteDataTask.execute(url)
//    }

    /**
     * //Find My location Button listener
     */
    private fun setGetMyLocationBtnHandler() {
        binding.fragmentMapsFindMyLocationBtn.setOnClickListener(View.OnClickListener { onGetPositionClick() })
    }

    /**
     * Smoothly change camera position with zoom level
     *
     * @param latLng
     * @param zoomLevel
     */
    private fun smoothlyMoveCameraToPosition(latLng: LatLng, zoomLevel: Float) {
        val cameraPosition: CameraPosition = CameraPosition.Builder()
            .target(latLng)
            .build()
        val cameraUpdate: CameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        mMap!!.animateCamera(cameraUpdate)
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
    }

    /**
     * Find my position action handler
     */
    @SuppressLint("MissingPermission")
    fun onGetPositionClick() {
        locationClient!!.getLastLocation()
            .addOnSuccessListener(object : OnSuccessListener<Location?> {
                override fun onSuccess(p0: Location?) {
                    if (p0 == null) {
                        Toast.makeText(
                            activity,
                            UtilsConstants.ToastMessage.currentLocationNotUpdatedYet,
                            Toast.LENGTH_LONG
                        ).show()
                        return
                    }
                    val latLng = LatLng(p0.latitude, p0.longitude)
                    currentUserLocation = p0
                    if (currentUserLocationMarker == null) {
                        updateCurrentUserLocationMarker(latLng)
                    }
                    smoothlyMoveCameraToPosition(
                        latLng,
                        UtilsConstants.GoogleMaps.CameraZoomLevel.streets.toFloat()
                    )
                }
            })
    }

    /**
     * Update current user location marker
     *
     * @param newLatLng
     */
    private fun updateCurrentUserLocationMarker(newLatLng: LatLng) {
        if (currentUserLocationMarker != null) {
            currentUserLocationMarker!!.remove()
        }
        currentUserLocationMarker = mMap!!.addMarker(
            MarkerOptions()
                .position(newLatLng)
                .icon(
                    bitmapDescriptorFromVector(
                        activity,
                        R.drawable.ic_current_location_marker, Color.BLUE
                    )
                )
                .title("You are here!")
        )
    }

    /**
     * Get BitmapDescriptor from drawable vector asset, for custom cluster marker
     *
     * @param context
     * @param vectorResId
     * @param color
     * @return
     */
    private fun bitmapDescriptorFromVector(
        context: Context?,
        vectorResId: Int,
        color: Int
    ): BitmapDescriptor? {
        if (context == null) {
            return null
        }
        val vectorDrawable: Drawable = ContextCompat.getDrawable(context, vectorResId)!!
        DrawableCompat.setTint(vectorDrawable, color)
        DrawableCompat.setTintMode(vectorDrawable, PorterDuff.Mode.SRC_IN)
        vectorDrawable.setBounds(
            0,
            0,
            vectorDrawable.getIntrinsicWidth(),
            vectorDrawable.getIntrinsicHeight()
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.getIntrinsicWidth(),
            vectorDrawable.getIntrinsicHeight(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        loadDropOffPlacePickerFragment();
        mViewModel = (activity as MainActivity).viewModel
        initMapsFragment()
        setActionHandlers()
        resetBookingFlow()
    }

    /**
     * Request user for location permission
     */
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
            MY_LOCATION_REQUEST
        )
    }

    /**
     * //Start location update listener
     */
    @SuppressLint("MissingPermission", "RestrictedApi")
    private fun startLocationUpdate() {
        locationRequest = LocationRequest()
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest!!.setInterval((5 * 1000).toLong()) //5s
        locationRequest!!.setFastestInterval((5 * 1000).toLong()) //5s
        locationClient!!.requestLocationUpdates(
            locationRequest!!,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val location: Location = locationResult.lastLocation!!
                    val latLng = LatLng(
                        location.latitude,
                        location.longitude
                    )
                    updateCurrentUserLocationMarker(latLng)
                    //                        updateCurrentRoute();
                }
            }, null
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val apiKey = getString(R.string.google_maps_key)
        if (!Places.isInitialized()) { //Init GooglePlaceAutocomplete if not existed
            Places.initialize(requireActivity().applicationContext, apiKey)
        }
        placesClient = Places.createClient(requireActivity().applicationContext)
        mMap = googleMap
        requestPermission() //Request user for location permission
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mMap!!.uiSettings.isZoomControlsEnabled = true
        startLocationUpdate() //Start location update listener
        //        setUpCluster(); //Set up cluster on Google Map
        onGetPositionClick() // Position the map.
    }

    /**
     * Clear the route in the map
     */
    private fun removeCurrentRoute() {
        //Clear current route
        if (currentRoute.isEmpty()) return
        for (polyline in currentRoute) {
            polyline.remove()
        }
        currentRoute.clear()
    }

    /**
     * Sent the required data to checkout fragment
     */
    @SuppressLint("DefaultLocale")
    private fun sendCheckoutInfoToCheckoutFragment() {
        distanceInKmString = String.format("%.1fkm", distanceInKm)
        mViewModel!!.setDistanceInKmString(distanceInKmString)
//        val price: Int = (distanceInKm!! * UtilsConstants.Transportation.UnitPrice.bikeType) as Int

//        priceInVNDString = Integer.toString(price) + " VND"
    }

    /**
     * Reset all BookingViewModel data to null to prevent caching
     */
    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel!!.setCustomerSelectedDropOffPlace(null)
        mViewModel!!.setCustomerSelectedPickupPlace(null)
        mViewModel!!.setTransportationType(null)
        mViewModel!!.setBookBtnPressed(null)
        mViewModel!!.setCancelBookingBtnPressed(null)
    }

    /**
     * Send data to ProcessBookingViewModel
     */
    private fun sendDataToProcessBookingViewModel() {
        mViewModel!!.setDropOffPlaceString(customerDropOffPlace!!.name)
        mViewModel!!.setPickupPlaceString(customerPickupPlace!!.name)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Action handler when customer's chosen drop off place is selected


        //Action handler when customer's chosen drop off place is selected
        mViewModel!!.getCustomerSelectedDropOffPlace()
            .observe(viewLifecycleOwner, Observer<Any?> { place ->
                if (place == null) return@Observer
                customerDropOffPlace = place as Place?
                binding.fragmentMapsBackBtn.visibility = View.VISIBLE //Show back button
//                binding.edtDestination.text = (place?.toString() ?: "") as Editable?

                //TODO Move to customerPickUpPlace fragment
                if (customerPickupPlace != null)
                    loadPickupPlacePickerFragment()

                if (currentUserLocation != null) {
                    smoothlyMoveCameraToPosition(
                        LatLng(currentUserLocation!!.latitude, currentUserLocation!!.longitude),
                        UtilsConstants.GoogleMaps.CameraZoomLevel.betweenStreetsAndBuildings
                    )
                }
                //TODO Draw 2 pickup/drop-off markers

                if (customerDropOffPlace != null && customerPickupPlace != null) {
                    //TODO load checkout fragment
                    loadCheckoutFragment()

                    drawDropOffAndPickupMarkers()

                    //TODO Draw route from pickup place to drop-off place
                    drawRouteFromPickupToDropOff()
                }
            })


//                mViewModel!!.getCustomerSelectedDropOffPlace()
//                    .observe(viewLifecycleOwner, Observer<Any?> { place ->
//                        if (place == null) return@Observer
//                        customerDropOffPlace = place as Place?
//                        binding.fragmentMapsBackBtn.setVisibility(View.VISIBLE) //Show back button
//
//                        //TODO Move to customerPickUpPlace fragment
//                        loadPickupPlacePickerFragment()
//                        smoothlyMoveCameraToPosition(
//                            LatLng(currentUserLocation!!.latitude, currentUserLocation!!.longitude),
//                            UtilsConstants.GoogleMaps.CameraZoomLevel.betweenStreetsAndBuildings
//                        )
//                    })

        mViewModel!!.getCustomerSelectedPickupPlace()
            .observe(viewLifecycleOwner, Observer<Any?> { place ->
                if (place == null) return@Observer
                customerPickupPlace = place as Place?

//                binding.edtSource.text = (place?.toString() ?: "") as Editable?

                loadDropOffPlacePickerFragment()

                //TODO Draw 2 pickup/drop-off markers

                if (customerDropOffPlace != null && customerPickupPlace != null) {
                    //TODO load checkout fragment
                    loadCheckoutFragment()

                    drawDropOffAndPickupMarkers()

                    //TODO Draw route from pickup place to drop-off place
                    drawRouteFromPickupToDropOff()
                }
            })

//                //Action handler when customer's chosen pickup place is selected
//                mViewModel!!.getCustomerSelectedPickupPlace()
//                    .observe(viewLifecycleOwner, Observer<Any?> { place ->
//                        if (place == null) return@Observer
//                        customerPickupPlace = place as Place?
//
//                        //TODO load checkout fragment
//                        loadCheckoutFragment()
//
//                        //TODO Draw 2 pickup/drop-off markers
//                        drawDropOffAndPickupMarkers()
//
//                        //TODO Draw route from pickup place to drop-off place
//                        drawRouteFromPickupToDropOff()
//                    })


        mViewModel!!.getTransportationType()!!
            .observe(viewLifecycleOwner, Observer<String?> { s ->
                if (s == null) return@Observer
                transportationType = s
            })

        //*********************** For booking synchronization between user and driver flow *********************** //

        //Book btn pressed
        mViewModel!!.getBookBtnPressed()!!
            .observe(viewLifecycleOwner, Observer<Boolean?> { aBoolean ->
                if (aBoolean == null) return@Observer
                binding.fragmentMapsBackBtn.setVisibility(View.GONE)
                removeCurrentRoute() //Remove drawn route
                createNewBookingInDB() //Create new booking in DB, set listener to update for driver accepting this booking
                sendDataToProcessBookingViewModel()
                loadProcessingBookingFragment() //Load processing booking fragment
            })

        //Cancel booking btn pressed
        mViewModel!!.getCancelBookingBtnPressed()
            ?.observe(viewLifecycleOwner, Observer<Boolean?> { aBoolean ->
                if (aBoolean == null) return@Observer
//                resetBookingFlow()
//                cancelBooking()
            })
    }
    /*************************************************** For booking synchronization  */
    /**
     * Load process booking fragment
     */
    private fun loadProcessingBookingFragment() {
        //Load drop-off picker fragment
        val processingBookingFragment = CheckoutFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.booking_info, processingBookingFragment).commit()
    }

    /**
     * Create booking in db
     */
    private fun createNewBookingInDB() {
        val data: MutableMap<String, Any?> = HashMap()
        data[UtilsConstants.FSBooking.pickupPlaceAddress] = customerPickupPlace!!.address
        data[UtilsConstants.FSBooking.pickUpPlaceLatitude] =
            customerPickupPlace!!.latLng.latitude
        data[UtilsConstants.FSBooking.pickUpPlaceLongitude] =
            customerPickupPlace!!.latLng.longitude
        data[UtilsConstants.FSBooking.dropOffPlaceAddress] = customerDropOffPlace!!.address
        data[UtilsConstants.FSBooking.dropOffPlaceLatitude] =
            customerDropOffPlace!!.latLng.latitude
        data[UtilsConstants.FSBooking.dropOffPlaceLongitude] =
            customerDropOffPlace!!.latLng.longitude
        data[UtilsConstants.FSBooking.distanceInKm] = distanceInKmString
        data[UtilsConstants.FSBooking.priceInVND] = priceInVNDString
        data[UtilsConstants.FSBooking.transportationType] = transportationType
        data[UtilsConstants.FSBooking.available] = true
        data[UtilsConstants.FSBooking.finished] = false
        data[UtilsConstants.FSBooking.arrived] = false
        data[UtilsConstants.FSBooking.driver] = null
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

    /*************************************************** For booking synchronization  */
    private fun drawRoute(result: List<List<HashMap<String?, String?>?>?>?) {
        //Clear current route
        for (polyline in currentRoute) {
            polyline.remove()
        }
        currentRoute.clear()
        var points: ArrayList<LatLng?>? = null
        var lineOptions: PolylineOptions? = null
        for (i in result!!.indices) {
            points = ArrayList<LatLng?>()
            lineOptions = PolylineOptions()
            val route = result[i]
            for (j in route!!.indices) {
                val point = route[j]
                val lat = point!!["lat"]!!.toDouble()
                val lng = point["lng"]!!.toDouble()
                val position = LatLng(lat, lng)
                points.add(position)
            }
            lineOptions.addAll(points)
            lineOptions.width(12f)
            lineOptions.color(Color.RED)
            lineOptions.geodesic(true)
        }

        // Drawing polyline in the Google Map for the i-th route
        currentRoute.add(mMap!!.addPolyline(lineOptions!!))
    }

    /**
     * A Class to call Google Directions API with callback
     */
    private inner class FetchRouteDataTask : AsyncTask<String?, Void?, String?>() {

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val routeParserTask = RouteParserTask()
            routeParserTask.execute(result)
        }

        override fun doInBackground(vararg p0: String?): String? {
            var data = ""
            try {
                data = fetchDataFromURL(p0[0]!!)
            } catch (ignored: Exception) {
            }
            return data
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private inner class RouteParserTask :
        AsyncTask<String?, Int?, List<List<HashMap<String?, String?>?>?>?>() {

        override fun onPostExecute(result: List<List<HashMap<String?, String?>?>?>?) {
            sendCheckoutInfoToCheckoutFragment() //Send calculated checkout info to checkout fragment
            drawRoute(result) //Draw new route
        }

        override fun doInBackground(vararg p0: String?): List<List<HashMap<String?, String?>?>?>? {
            val jObject: JSONObject
            var routes: List<List<HashMap<String?, String?>>>? = null
            try {
                jObject = JSONObject(p0[0]!!)
                val parser = DirectionsJSONParser(jObject)
                routes = parser.routes
                distanceInKm = parser.totalDistanceInKm
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return routes!!
        }
    }

    /**
     * Method to get URL for fetching data from Google Directions API (finding direction from origin to destination)
     *
     * @param origin
     * @param destination
     * @param directionMode
     * @return
     */
    private fun getRouteUrl(
        origin: LatLng,
        destination: LatLng,
        directionMode: String
    ): String {
        val originParam: String = UtilsConstants.GoogleMaps.DirectionApi.originParam +
                "=" + origin.latitude + "," + origin.longitude
        val destinationParam: String = UtilsConstants.GoogleMaps.DirectionApi.destinationParam +
                "=" + destination.latitude + "," + destination.longitude
        val modeParam: String =
            UtilsConstants.GoogleMaps.DirectionApi.modeParam + "=" + directionMode
        val params = "$originParam&$destinationParam&$modeParam"
        val output: String = UtilsConstants.GoogleMaps.DirectionApi.outputParam
        return (UtilsConstants.GoogleMaps.DirectionApi.baseUrl + output + "?" + params
                + "&key=" + getString(R.string.google_maps_key))
    }

    /**
     * A method to fetch json data from url
     */
    @Throws(IOException::class)
    private fun fetchDataFromURL(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            iStream = urlConnection!!.inputStream
            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line: String? = ""
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            data = sb.toString()
            br.close()
        } catch (e: Exception) {
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    companion object {
        //Google maps variables
        const val MY_LOCATION_REQUEST = 99
        fun newInstance(): BookingFragment {
            return BookingFragment()
        }
    }
}