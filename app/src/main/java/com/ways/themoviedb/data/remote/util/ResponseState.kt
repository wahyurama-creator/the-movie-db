package com.ways.themoviedb.data.remote.util

enum class ApiStatus {
    SUCCESS,
    ERROR,
}

sealed class ResponseState<out T>(val status: ApiStatus, val data: T?, val message: String?) {
    data class Success<out R>(val mData: R?) : ResponseState<R>(
        status = ApiStatus.SUCCESS,
        data = mData,
        message = null
    )

    data class Error(val exception: String) : ResponseState<Nothing>(
        status = ApiStatus.ERROR,
        data = null,
        message = exception
    )
}