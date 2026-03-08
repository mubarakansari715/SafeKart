package com.safekart.safekart.ui.presentation.address

import com.safekart.safekart.data.model.Address

data class AddressUiState(
    val addresses: List<Address> = emptyList(),
    val selectedAddressId: String? = null,
    val showAddForm: Boolean = false,
    val isLoading: Boolean = true,
    // Form fields
    val fullName: String = "",
    val phone: String = "",
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val pincode: String = "",
    val formError: String? = null,
    val isSaving: Boolean = false
)
