package com.linkdevelopment.presentation.signup

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
class SignUpViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.EmailOrPhoneChanged -> updateEmailOrPhone(event.value)
            is SignUpEvent.PasswordChanged -> updatePassword(event.value)
            SignUpEvent.SignUpClicked -> signUp()
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
        val hasMinLength = value.length >= 6
        val hasNumber = value.any { it.isDigit() }
        _uiState.update {
            it.copy(
                password = value,
                errorResId = null,
                hasMinLength = hasMinLength,
                hasNumber = hasNumber
            )
        }
    }

    private fun signUp() {
        val currentState = _uiState.value

        if (currentState.emailOrPhone.isBlank()) {
            _uiState.update { it.copy(errorResId = R.string.error_enter_email_or_phone) }
            return
        }

        if (!AuthValidator.isValidEmail(currentState.emailOrPhone) &&
            !AuthValidator.isValidPhone(currentState.emailOrPhone)
        ) {
            _uiState.update { it.copy(errorResId = R.string.error_invalid_email_or_phone) }
            return
        }

        if (currentState.password.isBlank()) {
            _uiState.update { it.copy(errorResId = R.string.error_enter_password) }
            return
        }

        if (!currentState.hasMinLength) {
            _uiState.update { it.copy(errorResId = R.string.error_password_min_length) }
            return
        }

        if (!currentState.hasNumber) {
            _uiState.update { it.copy(errorResId = R.string.error_password_must_contain_number) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorResId = null) }
            
            // Simulate registration
            delay(1500.milliseconds)
            
            userPreferencesRepository.setLoggedIn()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isSignUpSuccessful = true
                )
            }
        }
    }
}

sealed class SignUpEvent {
    data class EmailOrPhoneChanged(val value: String) : SignUpEvent()
    data class PasswordChanged(val value: String) : SignUpEvent()
    data object SignUpClicked : SignUpEvent()
}
