package com.linkdevelopment.presentation.util

import android.util.Patterns

object AuthValidator {

    fun isValidEmail(email: String): Boolean {
        return if (email.isBlank()) false else Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPhone(phone: String): Boolean {
        // Requirements: Accept +, spaces, -, (), length >= 7, avoid over-complication
        val phoneRegex = "^[+]*[0-9]{1,4}[-\\s./0-9()]*$".toRegex()
        return phone.length >= 7 && phoneRegex.matches(phone)
    }
}
