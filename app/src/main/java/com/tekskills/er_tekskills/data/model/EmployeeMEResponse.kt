package com.tekskills.er_tekskills.data.model
import com.google.gson.annotations.SerializedName


data class EmployeeMEResponse(
    @SerializedName("accountNonExpired")
    val accountNonExpired: Boolean,
    @SerializedName("accountNonLocked")
    val accountNonLocked: Boolean,
    @SerializedName("authorities")
    val authorities: List<Authority>,
    @SerializedName("credentialsNonExpired")
    val credentialsNonExpired: Boolean,
    @SerializedName("emailId")
    val emailId: Any,
    @SerializedName("empDetailsId")
    val empDetailsId: Any,
    @SerializedName("employeeMaster")
    val employeeMaster: EmployeeMaster,
    @SerializedName("enabled")
    val enabled: Boolean,
    @SerializedName("fullName")
    val fullName: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String
)

data class Authority(
    @SerializedName("authority")
    val authority: String
)

data class EmployeeMaster(
    @SerializedName("contactNoOne")
    val contactNoOne: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("departmentId")
    val departmentId: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("employeeNumber")
    val employeeNumber: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("pwd")
    val pwd: String,
    @SerializedName("reportingManagerId")
    val reportingManagerId: Int,
    @SerializedName("roleId")
    val roleId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int,
    @SerializedName("userName")
    val userName: String
)