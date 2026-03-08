package com.safekart.safekart.ui.presentation.profile

import com.safekart.safekart.data.model.Address

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val userPhone: String = "",
    val shippingAddresses: List<Address> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false
)
