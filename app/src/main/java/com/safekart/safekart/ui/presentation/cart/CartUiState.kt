package com.safekart.safekart.ui.presentation.cart

import com.safekart.safekart.data.model.CartItem

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val deliveryCharge: Double = 0.0,
    val totalAmount: Double = 0.0,
    val cartAddedEvent: Boolean = false
)
