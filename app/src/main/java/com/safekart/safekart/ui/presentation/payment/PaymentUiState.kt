package com.safekart.safekart.ui.presentation.payment

import com.safekart.safekart.data.model.Address
import com.safekart.safekart.data.model.CartItem

data class PaymentUiState(
    val items: List<CartItem> = emptyList(),
    val address: Address? = null,
    val subtotal: Double = 0.0,
    val deliveryCharge: Double = 0.0,
    val totalAmount: Double = 0.0,
    val isPlacingOrder: Boolean = false,
    val error: String? = null
)
