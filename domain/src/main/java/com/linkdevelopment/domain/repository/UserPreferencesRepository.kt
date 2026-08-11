package com.linkdevelopment.domain.repository



interface UserPreferencesRepository {

    suspend fun isLoggedIn(): Boolean

    suspend fun setLoggedIn()

    suspend fun logout()
}
