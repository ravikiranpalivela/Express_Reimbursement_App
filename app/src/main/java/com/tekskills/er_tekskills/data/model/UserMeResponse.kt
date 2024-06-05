package com.tekskills.er_tekskills.data.model


import com.google.gson.annotations.SerializedName

data class UserMeResponse(
    @SerializedName("userAddress")
    val userAddress: UserAddressData,
    @SerializedName("securityUser")
    val securityUser: UserDataMeResponse,
)

data class UserAddressData(
    @SerializedName("city")
    val city: String,
    @SerializedName("coOrdinates")
    val coOrdinates: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lineOne")
    val lineOne: String,
    @SerializedName("lineTwo")
    val lineTwo: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("tenantId")
    val tenantId: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("zpiCode")
    val zpiCode: Int
)

data class UserDataMeResponse(
    @SerializedName("accountNonExpired")
    val accountNonExpired: Boolean,
    @SerializedName("accountNonLocked")
    val accountNonLocked: Boolean,
    @SerializedName("authorities")
    val authorities: List<AuthorityData>,
    @SerializedName("credentialsNonExpired")
    val credentialsNonExpired: Boolean,
    @SerializedName("empDetailsId")
    val empDetailsId: String,
    @SerializedName("employeeMaster")
    val employeeMaster: EmployeeMasterData,
    @SerializedName("enabled")
    val enabled: Boolean,
    @SerializedName("fullName")
    val fullName: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("password")
    val password: String,
    @SerializedName("tenantId")
    val tenantId: Int,
    @SerializedName("username")
    val username: String
)

data class AuthorityData(
    @SerializedName("authority")
    val authority: String
)

data class EmployeeMasterData(
    @SerializedName("active")
    val active: Boolean,
    @SerializedName("contactNumber")
    val contactNumber: Any,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("createdBy")
    val createdBy: Int,
    @SerializedName("departmentId")
    val departmentId: Any,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("empType")
    val empType: Any,
    @SerializedName("familyName")
    val familyName: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("middleName")
    val middleName: Any,
    @SerializedName("pwd")
    val pwd: String,
    @SerializedName("roleId")
    val roleId: Int,
    @SerializedName("tenantId")
    val tenantId: Int,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("updatedBy")
    val updatedBy: Int,
    @SerializedName("userName")
    val userName: String
)