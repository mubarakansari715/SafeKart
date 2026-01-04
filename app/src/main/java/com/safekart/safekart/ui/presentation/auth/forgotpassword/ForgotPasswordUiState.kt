package com.safekart.safekart.ui.presentation.auth.forgotpassword

import android.util.Patterns

data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val isEmailSent: Boolean = false,
    val errorMessage: String? = null
) {
    val isFormValid: Boolean
        get() = email.trim().isNotEmpty() &&
                emailError == null &&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
}

