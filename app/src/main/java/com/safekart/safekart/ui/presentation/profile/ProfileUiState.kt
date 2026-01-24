package com.safekart.safekart.ui.presentation.profile

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val userPhone: String = "",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)
