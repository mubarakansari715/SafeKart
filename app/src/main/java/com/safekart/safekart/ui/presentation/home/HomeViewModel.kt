package com.safekart.safekart.ui.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safekart.safekart.domain.repository.AuthRepository
import com.safekart.safekart.domain.usecase.user.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadUserData() {
        viewModelScope.launch {
            try {
                getUserInfoUseCase()
                    .onSuccess { userInfo ->
                        val welcomeMessage = if (userInfo.username?.isNotEmpty() == true) {
                            "Welcome, ${userInfo.username}!"
                        } else {
                            "Welcome!"
                        }

                        _uiState.value = _uiState.value.copy(
                            welcomeMessage = welcomeMessage,
                            userEmail = userInfo.email ?: "",
                            isLoading = false
                        )
                    }
                    .onFailure {
                        // Fallback to email if use case fails
                        val userEmail = authRepository.currentUser?.email ?: ""
                        _uiState.value = _uiState.value.copy(
                            welcomeMessage = "Welcome!",
                            userEmail = userEmail,
                            isLoading = false
                        )
                    }
            } catch (e: Exception) {
                // Fallback to email if exception occurs
                val userEmail = authRepository.currentUser?.email ?: ""
                _uiState.value = _uiState.value.copy(
                    welcomeMessage = "Welcome!",
                    userEmail = userEmail,
                    isLoading = false
                )
            }
        }
    }

    fun logout() {
        authRepository.signOut()
    }
}

