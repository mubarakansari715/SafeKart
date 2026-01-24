package com.safekart.safekart.ui.presentation.auth.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safekart.safekart.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        val nameTrimmed = name.trim()
        var nameError: String? = null
        
        if (nameTrimmed.isNotEmpty() && nameTrimmed.length < 3) {
            nameError = "Username must be at least 3 characters"
        }
        
        _uiState.value = _uiState.value.copy(
            name = name,
            nameError = nameError,
            errorMessage = null
        )
    }

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

    fun updatePassword(password: String) {
        var passwordError: String? = null
        
        if (password.isNotEmpty() && password.length < 6) {
            passwordError = "Password must be at least 6 characters"
        }
        
        val confirmPasswordError = if (_uiState.value.confirmPassword.isNotEmpty() && 
            password != _uiState.value.confirmPassword) {
            "Passwords do not match"
        } else null
        
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError,
            errorMessage = null
        )
    }

    fun updateConfirmPassword(confirmPassword: String) {
        var confirmPasswordError: String? = null
        
        if (confirmPassword.isNotEmpty() && confirmPassword != _uiState.value.password) {
            confirmPasswordError = "Passwords do not match"
        }
        
        _uiState.value = _uiState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = confirmPasswordError,
            errorMessage = null
        )
    }

    fun validateInputs(): Boolean {
        val name = _uiState.value.name.trim()
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword

        var isValid = true
        var nameError: String? = null
        var emailError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null

        // Username validation (optional - only validate if provided)
        if (name.isNotEmpty() && name.length < 3) {
            nameError = "Username must be at least 3 characters"
            isValid = false
        }

        // Email validation
        if (email.isEmpty()) {
            emailError = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Please enter a valid email"
            isValid = false
        }

        // Password validation
        if (password.isEmpty()) {
            passwordError = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            isValid = false
        }

        // Confirm Password validation
        if (confirmPassword.isEmpty()) {
            confirmPasswordError = "Please confirm your password"
            isValid = false
        } else if (password != confirmPassword) {
            confirmPasswordError = "Passwords do not match"
            isValid = false
        }

        _uiState.value = _uiState.value.copy(
            nameError = nameError,
            emailError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError
        )

        return isValid
    }

    fun register() {
        if (!validateInputs()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val email = _uiState.value.email.trim()
            val password = _uiState.value.password
            val name = _uiState.value.name.trim().takeIf { it.isNotBlank() }  // Optional - only pass if not empty

            registerUseCase(email, password, name, null)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRegisterSuccessful = true
                    )
                }
                .onFailure { exception ->
                    val errorMessage = when {
                        exception.message?.contains("already exists", ignoreCase = true) == true -> 
                            "An account already exists with this email"
                        exception.message?.contains("Network error", ignoreCase = true) == true -> 
                            "Network error. Please check your connection"
                        exception.message?.contains("Cannot connect", ignoreCase = true) == true -> 
                            "Cannot connect to server. Please check if server is running"
                        exception.message?.contains("timeout", ignoreCase = true) == true -> 
                            "Connection timeout. Please try again"
                        exception.message?.contains("Email and password are required", ignoreCase = true) == true -> 
                            "Email and password are required"
                        else -> exception.message ?: "Registration failed"
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
        }
    }

    fun resetRegisterState() {
        _uiState.value = _uiState.value.copy(
            isRegisterSuccessful = false,
            errorMessage = null
        )
    }
}

