package com.safekart.safekart.ui.presentation.auth.login

import android.util.Patterns

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val errorMessage: String? = null
) {
    val isFormValid: Boolean
        get() = email.trim().isNotEmpty() && 
                password.isNotEmpty() &&
                emailError == null &&
                passwordError == null &&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() &&
                password.length >= 6
}

