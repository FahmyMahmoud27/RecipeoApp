package com.linkdevelopment.domain.util

interface ICheckNetworkState {
    suspend fun isConnected(): Boolean
}
