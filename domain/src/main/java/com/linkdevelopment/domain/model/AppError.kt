package com.linkdevelopment.domain.model

sealed class AppError : Exception() {
    object NoInternet : AppError()
    object Timeout : AppError()
    object ServerError : AppError()
    object NoCacheAvailable : AppError()
    object Unknown : AppError()

    fun getUserFriendlyMessage(): String {
        return when (this) {
            NoInternet -> "No internet connection. Please check your connection and try again."
            Timeout -> "The connection timed out. Please try again."
            ServerError -> "Something went wrong on our end. Please try again later."
            NoCacheAvailable -> "Unable to load recipes offline. Please connect to the internet and try again."
            Unknown -> "Something went wrong. Please try again."
        }
    }
}
