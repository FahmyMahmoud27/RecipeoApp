package com.linkdevelopment.data.util

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.linkdevelopment.domain.util.ICheckNetworkState
import javax.inject.Inject

class CheckNetworkState @Inject constructor(
    private val connectivityManager: ConnectivityManager
) : ICheckNetworkState {

    override suspend fun isConnected(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
