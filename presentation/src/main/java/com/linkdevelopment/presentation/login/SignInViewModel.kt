package com.linkdevelopment.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linkdevelopment.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.EmailOrPhoneChanged -> updateEmailOrPhone(event.value)
            is SignInEvent.PasswordChanged -> updatePassword(event.value)
            SignInEvent.LoginClicked -> login()
        }
    }

    private fun updateEmailOrPhone(value: String) {
        _uiState.update {
            it.copy(
                emailOrPhone = value,
                error = null
            )
        }
    }

    private fun updatePassword(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                error = null
            )
        }
    }

    private fun login() {
        val currentState = _uiState.value

        if (currentState.emailOrPhone.isBlank()) {
            _uiState.update {
                it.copy(error = "Please enter your email or phone number")
            }
            return
        }

        if (currentState.password.isBlank()) {
            _uiState.update {
                it.copy(error = "Please enter your password")
            }
            return
        }

        if (currentState.password.length < 6) {
            _uiState.update {
                it.copy(error = "Password must be at least 6 characters")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            delay(1000.milliseconds)
            userPreferencesRepository.setLoggedIn()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isLoginSuccessful = true,
                    error = null
                )
            }
        }
    }
}

sealed class SignInEvent {
    data class EmailOrPhoneChanged(val value: String) : SignInEvent()
    data class PasswordChanged(val value: String) : SignInEvent()
    data object LoginClicked : SignInEvent()
}
