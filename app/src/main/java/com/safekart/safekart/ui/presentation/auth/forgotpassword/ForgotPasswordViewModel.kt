package com.safekart.safekart.ui.presentation.auth.forgotpassword

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safekart.safekart.domain.usecase.auth.SendPasswordResetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val sendPasswordResetUseCase: SendPasswordResetUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        val emailTrimmed = email.trim()
        var emailError: String? = null
        
        if (emailTrimmed.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(emailTrimmed).matches()) {
            emailError = "Please enter a valid email"
        }
        
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = emailError,
            errorMessage = null
        )
    }

    fun validateInputs(): Boolean {
        val email = _uiState.value.email.trim()

        var isValid = true
        var emailError: String? = null

        // Email validation
        if (email.isEmpty()) {
            emailError = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Please enter a valid email"
            isValid = false
        }

        _uiState.value = _uiState.value.copy(
            emailError = emailError
        )

        return isValid
    }

    fun sendPasswordResetEmail() {
        if (!validateInputs()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val email = _uiState.value.email.trim()

            sendPasswordResetUseCase(email)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isEmailSent = true
                    )
                }
                .onFailure { exception ->
                    val errorMessage = when {
                        exception.message?.contains("Network error", ignoreCase = true) == true -> 
                            "Network error. Please check your connection"
                        exception.message?.contains("Cannot connect", ignoreCase = true) == true -> 
                            "Cannot connect to server. Please check if server is running"
                        exception.message?.contains("timeout", ignoreCase = true) == true -> 
                            "Connection timeout. Please try again"
                        exception.message?.contains("Email is required", ignoreCase = true) == true -> 
                            "Email is required"
                        else -> exception.message ?: "Failed to send reset email"
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
        }
    }

    fun resetState() {
        _uiState.value = _uiState.value.copy(
            isEmailSent = false,
            errorMessage = null
        )
    }
}

