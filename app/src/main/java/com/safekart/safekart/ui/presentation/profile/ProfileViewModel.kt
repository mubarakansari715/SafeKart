package com.safekart.safekart.ui.presentation.profile

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
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    // Initialize state directly from cache - no redundant operations
    private val _uiState = MutableStateFlow(
        run {
            val currentUser = authRepository.currentUser
            ProfileUiState(
                userName = currentUser?.full_name ?: "",
                userEmail = currentUser?.email ?: "",
                userPhone = currentUser?.phone ?: "",
                isLoading = false,
                isRefreshing = false
            )
        }
    )
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    /**
     * Refreshes user data from API
     * Call this only when explicitly needed (e.g., pull-to-refresh, after profile update)
     */
    fun refreshUserData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            
            try {
                getUserInfoUseCase()
                    .onSuccess { userInfo ->
                        val currentUser = authRepository.currentUser
                        _uiState.value = _uiState.value.copy(
                            userName = userInfo.username ?: currentUser?.full_name ?: "",
                            userEmail = userInfo.email ?: currentUser?.email ?: "",
                            userPhone = currentUser?.phone ?: "",
                            isRefreshing = false
                        )
                    }
                    .onFailure {
                        // Keep existing cached data on failure
                        _uiState.value = _uiState.value.copy(isRefreshing = false)
                    }
            } catch (e: Exception) {
                // Keep existing cached data on exception
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            }
        }
    }

    /**
     * Updates user data after profile edit
     */
    fun updateUserData(userName: String? = null, userEmail: String? = null, userPhone: String? = null) {
        _uiState.value = _uiState.value.copy(
            userName = userName ?: _uiState.value.userName,
            userEmail = userEmail ?: _uiState.value.userEmail,
            userPhone = userPhone ?: _uiState.value.userPhone
        )
    }

    fun logout() {
        authRepository.signOut()
    }
}
