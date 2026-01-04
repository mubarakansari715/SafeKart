package com.safekart.safekart.ui.presentation.auth.register

import android.util.Patterns

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val isRegisterSuccessful: Boolean = false,
    val errorMessage: String? = null
) {
    val isFormValid: Boolean
        get() = name.trim().isNotEmpty() &&
                email.trim().isNotEmpty() &&
                password.isNotEmpty() &&
                confirmPassword.isNotEmpty() &&
                nameError == null &&
                emailError == null &&
                passwordError == null &&
                confirmPasswordError == null &&
                name.trim().length >= 3 &&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() &&
                password.length >= 6 &&
                password == confirmPassword
}

