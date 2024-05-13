package com.tekskills.er_tekskills.presentation.activity
//
//import android.Manifest
//import android.content.Context
//import android.content.SharedPreferences
//import android.view.View
//import android.widget.Button
//import com.tekskills.insight.Model.LoginModel
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
///**
// * Created by RK
// */
//class LoginActivity : CommonActivity(), View.OnClickListener {
//    var context: Context? = null
//    var edt_username: EditText? = null
//    var edt_password: EditText? = null
//    var btn_login: Button? = null
//    var tv_forgotPassword: TextView? = null
//    var tv_register: TextView? = null
//    var customProgressDialog: CustomProgressDialog? = null
//    var mySharedPreferences: SharedPreferences? = null
//    var editor: SharedPreferences.Editor? = null
//    var session: SessionManager? = null
//    private val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124
//    protected fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//        init()
//    }
//
//    private fun init() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            insertDummyContactWrapper()
//        }
//        try {
//            context = this
//            session = SessionManager(this@LoginActivity)
//            edt_username = findViewById(R.id.edt_username) as EditText?
//            edt_password = findViewById(R.id.edt_password) as EditText?
//            tv_forgotPassword = findViewById(R.id.tv_forgotpassword) as TextView?
//            tv_forgotPassword.setOnClickListener(this)
//            tv_register = findViewById(R.id.tv_registration) as TextView?
//            tv_register.setOnClickListener(this)
//            btn_login = findViewById(R.id.btn_login) as Button?
//            btn_login!!.setOnClickListener(this)
//            customProgressDialog = CustomProgressDialog(context)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    override fun onClick(view: View) {
//        when (view.id) {
//            R.id.btn_login -> {
//                //                if (validate()) {
//                //Toast.makeText(context, "DONE", Toast.LENGTH_SHORT).show();
////                    signInAPI();
//                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
//                session.createLoginSession(
//                    edt_username.getText().toString().trim { it <= ' ' },
//                    edt_password.getText().toString().trim { it <= ' ' })
//                finish()
//            }
//
//            R.id.tv_forgotpassword -> startActivity(
//                Intent(
//                    this@LoginActivity,
//                    ForgotPasswordActivity::class.java
//                )
//            )
//
//            R.id.tv_registration -> startActivity(
//                Intent(
//                    this@LoginActivity,
//                    SignupActivity::class.java
//                )
//            )
//        }
//    }
//
//    private fun signInAPI() {
//        if (!Common.isNetworkAvailable(this@LoginActivity)) {
//            Toast.makeText(context, "Please Check Your Network Connection", Toast.LENGTH_SHORT)
//                .show()
//            return
//        }
//        try {
//            customProgressDialog.show()
//            val userID: String = edt_username.getText().toString().trim { it <= ' ' }
//            val password: String = edt_password.getText().toString().trim { it <= ' ' }
//            val deviceId: String = getDeviceID()
//            val loginService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
//            val loginCall: Call<LoginModel> = loginService.callLoginAPi(userID, password, deviceId)
//            loginCall.enqueue(object : Callback<LoginModel> {
//                override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
//                    customProgressDialog.dismiss()
//                    val msg: String = response.body().getMsg()
//                    if (msg.equals("login sucessfully", ignoreCase = true)) {
//                        val lm: LoginModel? = response.body()
//                        val status = java.lang.Boolean.getBoolean(lm.getStatus())
//                        Toast.makeText(
//                            this@LoginActivity,
//                            response.body().getMsg(),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        startActivity(
//                            Intent(
//                                this@LoginActivity,
//                                DashBoard::class.java
//                            )
//                        ) //HomeActivity
//                        session.createLoginSession(
//                            edt_username.getText().toString().trim { it <= ' ' },
//                            edt_password.getText().toString().trim { it <= ' ' })
//                        SharedPreferences.savePreferences(
//                            this@LoginActivity,
//                            Common.USERNAME,
//                            response.body().getBDM_Name()
//                        )
//                        SharedPreferences.savePreferences(
//                            this@LoginActivity,
//                            Common.EMAIL,
//                            response.body().getEmailID()
//                        )
//                        SharedPreferences.savePreferences(
//                            this@LoginActivity,
//                            Common.MOBILE_NUMBER,
//                            response.body().getMobileNumber()
//                        )
//                        SharedPreferences.savePreferences(
//                            this@LoginActivity,
//                            Common.MANAGERNAME,
//                            response.body().getManagername()
//                        )
//                        //SharedPreferences.savePreferences(LoginActivity.this,Common.EMAIL,response.body().getEmailID());
//                        mySharedPreferences =
//                            getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE)
//                        editor = mySharedPreferences!!.edit()
//                        editor.putString("BDM_Name", response.body().getBDM_Name()) // Saving string
//                        editor.putString("User_Name", response.body().getBDM_Name())
//                        /* editor.putString("User_Password", response.body().getEmailID());
//                        editor.putString("PicturePath", profile_link);*/editor.commit() // commit changes
//                        finish()
//                        //AppPreferences.saveStringPref(LoginActivity.this, Common.USER_NAME, edt_username.getText().toString());
//                    } else {
//                        Toast.makeText(this@LoginActivity, "" + msg, Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
//                    customProgressDialog.dismiss()
//                    Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
//                }
//            })
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun validate(): Boolean {
//        if (edt_username.getText().toString().trim { it <= ' ' }.isEmpty()) {
//            edt_username.setError(getResources().getString(R.string.err_username))
//            return false
//        } else if (edt_password.getText().toString().trim { it <= ' ' }.isEmpty()) {
//            edt_password.setError(getResources().getString(R.string.err_password))
//            return false
//        }
//        return true
//    }
//
//    /**
//     * PERMISSIONS
//     */
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private fun insertDummyContactWrapper() {
//        val permissionsNeeded: MutableList<String> = ArrayList()
//        val permissionsList: MutableList<String> = ArrayList()
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.READ_PHONE_STATE
//            )
//        ) permissionsNeeded.add("READ_PHONE_STATE")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.RECORD_AUDIO
//            )
//        ) permissionsNeeded.add("RECORD_AUDIO")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.INTERNET
//            )
//        ) permissionsNeeded.add("INTERNET")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.RECEIVE_BOOT_COMPLETED
//            )
//        ) permissionsNeeded.add("RECEIVE_BOOT_COMPLETED")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.CALL_PHONE
//            )
//        ) permissionsNeeded.add("CALL_PHONE")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.READ_CALENDAR
//            )
//        ) permissionsNeeded.add("READ_CALENDAR")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.WRITE_CALENDAR
//            )
//        ) permissionsNeeded.add("WRITE_CALENDAR")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.WAKE_LOCK
//            )
//        ) permissionsNeeded.add("WAKE_LOCK")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.ACCESS_WIFI_STATE
//            )
//        ) permissionsNeeded.add("ACCESS_WIFI_STATE")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.ACCESS_NETWORK_STATE
//            )
//        ) permissionsNeeded.add("ACCESS_NETWORK_STATE")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            )
//        ) permissionsNeeded.add("WRITE_EXTERNAL_STORAGE")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.CAPTURE_VIDEO_OUTPUT
//            )
//        ) permissionsNeeded.add("CAPTURE_VIDEO_OUTPUT")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.READ_CALL_LOG
//            )
//        ) permissionsNeeded.add("READ_CALL_LOG")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//        ) permissionsNeeded.add("ACCESS_FINE_LOCATION")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.READ_SMS
//            )
//        ) permissionsNeeded.add("READ_SMS")
//        if (!addPermission(
//                permissionsList,
//                Manifest.permission.READ_CONTACTS
//            )
//        ) permissionsNeeded.add("READ_CONTACTS")
//        if (permissionsList.size > 0) {
//            if (permissionsNeeded.size > 0) {
//                var message = "You need to grant access to " + permissionsNeeded[0]
//                for (i in 1 until permissionsNeeded.size) message =
//                    message + ", " + permissionsNeeded[i]
//                requestPermissions(
//                    permissionsList.toTypedArray(),
//                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
//                )
//                return
//            }
//            requestPermissions(
//                permissionsList.toTypedArray(),
//                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS
//            )
//            return
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private fun addPermission(permissionsList: MutableList<String>, permission: String): Boolean {
//        if (checkSelfPermission(permission) !== PackageManager.PERMISSION_GRANTED) {
//            permissionsList.add(permission)
//            if (!shouldShowRequestPermissionRationale(permission)) return false
//        }
//        return true
//    }
//
//    companion object {
//        private const val PERMISSION_REQUEST_CODE = 1
//    }
//}
