package com.tekskills.er_tekskills.utils

class UtilsConstants {
    class Transportation {
        object Type {
            const val carType = "car"
            const val bikeType = "bike"
        }

        object UnitPrice {
            const val carType = 5000.0
            const val bikeType = 10.0
        }
    }

    //Fields of FireStore 'users' collection
    object FSUser {
        const val userCollection = "users"
        const val usernameField = "username"
        const val phoneField = "phone"
        const val birthDateField = "birthDate"
        const val genderField = "gender"
        const val emailField = "email"
        const val roleField = "role"
        const val transportationType = "transportationType"
        const val vehiclePlateNumber = "vehiclePlateNumber"
        const val rating = "rating"
        const val currentPositionLatitude = "currentPositionLatitude"
        const val currentPositionLongitude = "currentPositionLongitude"
        const val roleCustomerVal = "Customer"
        const val roleDriverVal = "Driver"
    }

    object FSBooking {
        const val bookingCollection = "bookings"
        const val pickupPlaceAddress = "pickupPlaceAddress"
        const val dropOffPlaceAddress = "dropOffPlaceAddress"
        const val pickUpPlaceLatitude = "pickUpPlaceLatitude"
        const val pickUpPlaceLongitude = "pickUpPlaceLongitude"
        const val dropOffPlaceLatitude = "dropOffPlaceLatitude"
        const val dropOffPlaceLongitude = "dropOffPlaceLongitude"
        const val driver = "driver"
        const val distanceInKm = "distanceInKm"
        const val priceInVND = "priceInVND"
        const val transportationType = "transportationType"
        const val available = "available"
        const val arrived = "arrived"
        const val finished = "finished"
    }

    object FSDriverLocation {
        const val driverLocationCollection = "driverLocations"
        const val currentPositionLatitude = "currentPositionLatitude"
        const val currentPositionLongitude = "currentPositionLongitude"
    }

    //All Toast messages being used
    object ToastMessage {
        const val emptyInputError = "Please fill in your account authentication."
        const val signInSuccess = "Sign in successfully!"
        const val signInFailure = "Invalid email/password!"
        const val registerSuccess = "Successfully registered"
        const val registerFailure =
            "Authentication failed, email must be unique and has correct form!"
        const val retrieveUsersInfoFailure = "Error querying for all users' information!"
        const val emptyMessageInputError = "Please type your message to send!"

        //Create site validation message
        const val placeAutocompleteError = "Google PlaceAutocomplete error with code: "

        //Maps Error Handling
        const val currentLocationNotUpdatedYet =
            "Please wait for a few seconds for current location to be updated!"
        const val routeRenderingInProgress = "Please wait, the route is being rendered!"

        //Edit site Message
        const val editSiteSuccess = "Edit site successfully!"

        //Booking error
        const val addNewBookingToDbFail = "Fail to create new booking"
    }

    object PlaceAddressComponentTypes {
        const val premise = "premise"
        const val streetNumber = "street_number"
        const val route = "route"
        const val adminAreaLv1 = "administrative_area_level_1"
        const val adminAreaLv2 = "administrative_area_level_2"
        const val country = "country"
    }

    object MenuItemsIndex {
        const val myCreatedSitesItemIndex = 0
        const val joinSitesItemIndex = 1
        const val createSiteItemIndex = 2
    }

    class GoogleMaps {
        object CameraZoomLevel {
            const val city = 10
            const val streets = 15
            const val buildings = 20
            const val betweenCityAndStreets = 12.5.toFloat()
            const val betweenStreetsAndBuildings = 17.5.toFloat()
        }

        object DirectionApi {
            const val baseUrl = "https://maps.googleapis.com/maps/api/directions/"
            const val originParam = "origin"
            const val destinationParam = "destination"
            const val modeParam = "mode"
            const val outputParam = "json"
        }
    }

    object Notification {
        var CHANNEL_ID = "GAC"
        var CHANNEL_NAME = "GreenAndClean notification"
        var CHANNEL_DES = "GreenAndClean app notification"
        var title = "Green&Clean notification"
        var onSiteChangeTextContent =
            "There has been some changes made to one of your participating site, click to see..."
    }
}
