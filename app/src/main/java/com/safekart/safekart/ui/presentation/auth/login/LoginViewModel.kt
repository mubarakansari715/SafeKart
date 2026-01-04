package com.safekart.safekart.ui.presentation.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.safekart.safekart.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

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
        
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = passwordError,
            errorMessage = null
        )
    }

    fun validateInputs(): Boolean {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password

        var isValid = true
        var emailError: String? = null
        var passwordError: String? = null

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

        _uiState.value = _uiState.value.copy(
            emailError = emailError,
            passwordError = passwordError
        )

        return isValid
    }

    fun login() {
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

            loginUseCase(email, password)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoginSuccessful = true
                    )
                }
                .onFailure { exception ->
                    val errorMessage = when (exception) {
                        is FirebaseAuthException -> {
                            when (exception.errorCode) {
                                "ERROR_INVALID_EMAIL" -> "Invalid email address"
                                "ERROR_WRONG_PASSWORD" -> "Wrong password"
                                "ERROR_USER_NOT_FOUND" -> "No account found with this email"
                                "ERROR_USER_DISABLED" -> "This account has been disabled"
                                "ERROR_TOO_MANY_REQUESTS" -> "Too many requests. Please try again later"
                                "ERROR_NETWORK_REQUEST_FAILED" -> "Network error. Please check your connection"
                                else -> "Login failed: ${exception.message}"
                            }
                        }
                        else -> "Login failed: ${exception.message}"
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
        }
    }

    fun resetLoginState() {
        _uiState.value = _uiState.value.copy(
            isLoginSuccessful = false,
            errorMessage = null
        )
    }
}

