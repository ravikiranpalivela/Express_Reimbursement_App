package com.tekskills.er_tekskills.utils

data class SuccessResource<out T>(
    val status: RestApiStatus,
    val data: T?,
    val message:String?
){
    companion object{

        fun <T> success(data:T?): SuccessResource<T>{
            return SuccessResource(RestApiStatus.SUCCESS, data, null)
        }

        fun <T> error(msg:String, data:T?): SuccessResource<T>{
            return SuccessResource(RestApiStatus.ERROR, data, msg)
        }

        fun <T> loading(data:T?): SuccessResource<T>{
            return SuccessResource(RestApiStatus.LOADING, data, null)
        }

    }
}