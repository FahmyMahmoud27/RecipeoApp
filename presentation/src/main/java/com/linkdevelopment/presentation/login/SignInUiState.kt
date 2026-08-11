package com.linkdevelopment.presentation.login

data class SignInUiState(
    val emailOrPhone: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val error: String? = null
)
