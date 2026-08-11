package com.linkdevelopment.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.linkdevelopment.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject



private val Context.dataStore by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext
    private val context: Context
) : UserPreferencesRepository {

    private companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    override suspend fun isLoggedIn(): Boolean {
        return context.dataStore.data
            .first()[IS_LOGGED_IN] ?: false
    }

    override suspend fun setLoggedIn() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = true
        }
    }

    override suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
        }
    }
}

