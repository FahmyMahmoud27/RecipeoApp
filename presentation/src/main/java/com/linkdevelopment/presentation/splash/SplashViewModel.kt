package com.linkdevelopment.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linkdevelopment.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<SplashNavigation>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        onEvent(SplashEvent.LoadSession)
    }

    fun onEvent(event: SplashEvent) {
        when (event) {
            SplashEvent.LoadSession -> checkUserSession()
        }
    }

    private fun checkUserSession() {
        viewModelScope.launch {
            delay(2000.milliseconds)

            val isLoggedIn = userPreferencesRepository.isLoggedIn()

            if (isLoggedIn) {
                _navigationEvent.emit(SplashNavigation.Home)
            } else {
                _navigationEvent.emit(SplashNavigation.Login)
            }
        }
    }
}

sealed class SplashEvent {
    data object LoadSession : SplashEvent()
}

sealed class SplashNavigation {
    data object Home : SplashNavigation()
    data object Login : SplashNavigation()
}
