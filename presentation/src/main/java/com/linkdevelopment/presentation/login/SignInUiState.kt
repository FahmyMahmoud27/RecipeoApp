package com.linkdevelopment.presentation.login

import androidx.annotation.StringRes

data class SignInUiState(
    val emailOrPhone: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    @StringRes val errorResId: Int? = null
)
