package com.linkdevelopment.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linkdevelopment.domain.repository.UserPreferencesRepository
import com.linkdevelopment.presentation.R
import com.linkdevelopment.presentation.util.AuthValidator
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
                errorResId = null
            )
        }
    }

    private fun updatePassword(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                errorResId = null
            )
        }
    }

    private fun login() {
        val currentState = _uiState.value

        if (currentState.emailOrPhone.isBlank()) {
            _uiState.update {
                it.copy(errorResId = R.string.error_enter_email_or_phone)
            }
            return
        }

        if (!AuthValidator.isValidEmail(currentState.emailOrPhone) &&
            !AuthValidator.isValidPhone(currentState.emailOrPhone)
        ) {
            _uiState.update {
                it.copy(errorResId = R.string.error_invalid_email_or_phone)
            }
            return
        }

        if (currentState.password.isBlank()) {
            _uiState.update {
                it.copy(errorResId = R.string.error_enter_password)
            }
            return
        }

        if (currentState.password.length < 6) {
            _uiState.update {
                it.copy(errorResId = R.string.error_password_min_length)
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorResId = null
                )
            }
            delay(1000.milliseconds)
            userPreferencesRepository.setLoggedIn()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isLoginSuccessful = true,
                    errorResId = null
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
