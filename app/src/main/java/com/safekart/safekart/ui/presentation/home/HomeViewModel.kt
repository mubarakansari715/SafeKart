package com.safekart.safekart.ui.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safekart.safekart.domain.repository.AuthRepository
import com.safekart.safekart.domain.repository.HomeRepository
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
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHome()
        loadUserData()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            homeRepository.getHome()
                .onSuccess { homeData ->
                    _uiState.value = _uiState.value.copy(
                        banners = homeData.banners,
                        categories = homeData.categories,
                        bestSelling = homeData.bestSelling,
                        offerBanner = homeData.offerBanner,
                        productSections = homeData.product,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load home"
                    )
                }
        }
    }

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
                            userEmail = userInfo.email ?: ""
                        )
                    }
                    .onFailure {
                        val userEmail = authRepository.currentUser?.email ?: ""
                        _uiState.value = _uiState.value.copy(
                            welcomeMessage = "Welcome!",
                            userEmail = userEmail
                        )
                    }
            } catch (e: Exception) {
                val userEmail = authRepository.currentUser?.email ?: ""
                _uiState.value = _uiState.value.copy(
                    welcomeMessage = "Welcome!",
                    userEmail = userEmail
                )
            }
        }
    }

    fun retry() {
        loadHome()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, error = null)
            homeRepository.getHome()
                .onSuccess { homeData ->
                    _uiState.value = _uiState.value.copy(
                        banners = homeData.banners,
                        categories = homeData.categories,
                        bestSelling = homeData.bestSelling,
                        offerBanner = homeData.offerBanner,
                        productSections = homeData.product,
                        isRefreshing = false,
                        error = null
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        error = e.message ?: "Failed to refresh"
                    )
                }
        }
    }

    fun logout() {
        authRepository.signOut()
    }
}
