package com.linkdevelopment.presentation.signup

import androidx.annotation.StringRes

data class SignUpUiState(
    val emailOrPhone: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSignUpSuccessful: Boolean = false,
    @StringRes val errorResId: Int? = null,
    val hasMinLength: Boolean = false,
    val hasNumber: Boolean = false
)
