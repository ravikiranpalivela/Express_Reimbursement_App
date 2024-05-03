package com.tekskills.er_tekskills.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class Common {
    val time: String?
        get() {
            var dte: String? = null
            val date = Date()
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            dte = formatter.format(date)
            Log.v("## SignUp Date", "## SignUp Date : $dte")
            return dte
        }

    companion object {


        const val CLIENT_NAME = "clientName"
        const val CLIENT_CONTACT_NAME = "clientContactName"
        const val CLIENT_CONTACT_POS = "clientContactPosition"

        const val PROJECT_NAME = "projectName"
        const val CLIENT_ID = "clientId"
        const val OPPORTUNITY_TYPE = "oppotunityType"
        const val OPPORTUNITY_DESC = "opportunityDesc"
        const val STATUS = "status"

        const val PROJECT_ID ="projectId"
        const val MANAGEMENT_ID ="managementId"
        const val ACCOUNTHEAD_ID ="accountHeadId"
        const val PRACTICEHEAD_ID = "practiceHeadId"
        const val PROJECTMANAGER_ID ="projectManagerId"


        const val USER_NAME = "userName"
        const val PASSWORD = "pwd"
        const val EMPLOYEE_NUM = "employeeNumber"
        const val ROLE_ID = "roleId"
        const val DEPT_ID = "departmentId"
        const val REPORTMANAGER_ID = "reportingManagerId"
        const val CONTACT_NUM = "contactNoOne"
        const val EMAIL_ID = "email"

        const val CLIENT_ESCALATION = "clientEscalations"
        const val ESCALATION_DESC = "escalationDescription"
        const val ESCALATION_RAISED_DATE = "escalationRaisedDate"
        const val ESCALATION_RESOLVE_DATE = "escalationResolvedDate"
        const val ESCALATION_STATUS = "escalationStatus"
        const val INITIALIZE_CONFIG = "You must initialize the configuration"

//        const val BASE_URL = "http://192.168.0.179:8070/"
        const val  BASE_URL = "http://192.168.0.219:8099/"
        const val DUMMY_BASE_URL = "http://dummy.restapiexample.com/api/v1/"
        const val APP_JSON = "Content-Type: application/json"
        const val First_Name = "FirstName"
        const val Last_Name = "LastName"
        const val User_Id = "username"
        const val Email_Id = "EmailId"
        const val Password = "password"
        const val PREF_DEFAULT = "Default"
        const val PREF_TOKEN = "auth_token"
        const val PREF_REFRESH_TOKEN = "auth_refresh_token"
        const val PREF_ROLE_TYPE = "role_type"
        const val PREF_EMP_ID = "employee_id"
        const val PREF_TOKEN_DATA = "auth_token_data"
        const val PREF_FIRST_TIME = "First_time"
        const val MANAGER= "manager"
        const val Mobile_Number = "MobileNumber"
        const val Age_Group = "AgeGroup"
        const val Name = "Name"
        const val EMPTY = ""
        const val USERNAME = "customer_name"
        const val MANAGERNAME = "managername"
        const val THEME = "dark_theme"
        const val AUTHORIZATION = "Authorization"

        //public static final String DEVICE_ID = "device_id";
        const val CUSTOMER_ID = "customer_id"
        const val MOBILE_NUMBER = "mobile_number"
        const val EMAIL = "email"
        const val UID = "UID"
        const val TIME = "0:00"
        var CheckinStatus = false
        private val TYPE_FACES: MutableMap<String, Typeface?> = HashMap<String, Typeface?>()
        const val PLACESAPI = "https://maps.googleapis.com/maps/api/geocode/json?address="
        const val PLACESAPIKEY = "&key=AIzaSyAmtCl_1MQ9Uw0XpKPE54-oQCCv9rWWVPo"

        // * font style of sourceScansPro  *//
        const val FONT_SOURCESANSPRO_LIGHT = "fonts/SourceSansPro-Light.ttf"
        const val FONT_SOURCESANSPRO_SEMIBOLD = "fonts/SourceSansPro-Semibold.ttf"
        const val FONT_SOURCESANSPRO_REGULAR = "fonts/SourceSansPro-Regular.ttf"

        // * font style of oswald and Sharp sans*//
        const val FONT_OSWALD_BOLD = "fonts/Oswald-Bold.ttf"
        const val FONT_SHARP_SANS = "fonts/Oswald-Bold.ttf"

        // * font style of AVENIR font  *//
        const val FONT_AVENIR_BOOK = "fonts/AvenirLTStd-Book.otf"
        const val FONT_AVENIR_BOLD = "fonts/AvenirNextLTPro-Bold.otf"
        const val FONT_AVENIR_MEDIUM = "fonts/AvenirLTStd-Medium.otf"
        const val FONT_AVENIR_HEAVY = "fonts/AvenirLTStd-Heavy.otf"
        const val FONT_AVENIR_LIGHT = "fonts/AvenirLTStd-Light.otf"

        // * font style of Lato  *//
        const val FONT_LATO_LIGHT = "fonts/Lato-Light.ttf"
        const val FONT_LATO_BOLD = "fonts/Lato-Bold.ttf"
        const val FONT_LATO_BLACK = "fonts/Lato-Black.ttf"
        const val FONT_LATO_REGULAR = "fonts/Lato-Regular.ttf"

        // * font style of Roboto  *//
        const val FONT_ROBOTO_LIGHT = "fonts/Roboto-Light.ttf"
        const val FONT_ROBOTO_BOLD = "fonts/Roboto-Bold.ttf"
        const val FONT_ROBOTO_MEDIUM = "fonts/Roboto-Medium.ttf"
        const val FONT_ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf"

        /////GET API LINKS
        const val OPPORTUNITY_DETAILS = "oppetunitydetails?oppertunityid="
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo: NetworkInfo = connectivityManager.getActiveNetworkInfo()!!
            return activeNetworkInfo != null && activeNetworkInfo.isConnected()
        }

        fun deviceID(context: Context): String? {
            val TelephonyMgr: TelephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null
            }
            val m_deviceId: String = TelephonyMgr.getDeviceId()
            //Toast.makeText(context, "DEVICE ID"+m_deviceId, Toast.LENGTH_SHORT).show();
            Log.v("DEVICEID : ", m_deviceId)
            return m_deviceId
        }

        fun checkLocationPermission(context: Context): Boolean {
            val result: Int =
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            return result == PackageManager.PERMISSION_GRANTED
        }

        fun closeKeyBoard(context: Context, view: View) {
            try {
                val `in` =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                `in`.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun isLocationServiceEnabled(lm: LocationManager): Boolean {
            var gps_enabled = false
            var network_enabled = false
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return gps_enabled || network_enabled
        }

        fun getFontTypeface(context: Context, fontFileName: String): Typeface? {
            var typeFace: Typeface? = TYPE_FACES[fontFileName]
            if (typeFace == null) {
                typeFace = Typeface.createFromAsset(context.resources.assets, fontFileName)
                TYPE_FACES[fontFileName] = typeFace
            }
            return typeFace
        }

        val yearFormateDate: String?
            get() {
                var dte: String? = null
                val date = Date()
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                dte = formatter.format(date)
                Log.v("## SignUp Date", "## SignUp Date : $dte")
                return dte
            }

        fun getYearMonthDayFormat(dateString: String?): String {
            if (dateString == null || dateString.trim { it <= ' ' } == EMPTY) return EMPTY
            val fmt = SimpleDateFormat("yyyy/MM/dd_HHmmss")
            var date: Date? = null
            try {
                date = fmt.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val fmtOut = SimpleDateFormat("dd MMM yyyy")
            return fmt.format(date)
        }

        // Date currentTime = Calendar.getInstance().getTime();
        val dateTime: String
            get() {
                val sdf = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault()
                )
                // Date currentTime = Calendar.getInstance().getTime();
                return sdf.format(Date())
            }

        @RequiresApi(api = Build.VERSION_CODES.O)
        fun convert(originalTime: String): String {
            val localDate: LocalDate
            localDate = if (originalTime.contains("T")) {
                OffsetDateTime.parse(originalTime).toLocalDate()
            } else {
                LocalDate.parse(originalTime)
            }
            return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }
}