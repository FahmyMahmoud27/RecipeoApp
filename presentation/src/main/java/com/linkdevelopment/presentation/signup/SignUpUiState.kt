package com.linkdevelopment.presentation.signup

data class SignUpUiState(
    val emailOrPhone: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSignUpSuccessful: Boolean = false,
    val error: String? = null,
    val hasMinLength: Boolean = false,
    val hasNumber: Boolean = false
)
