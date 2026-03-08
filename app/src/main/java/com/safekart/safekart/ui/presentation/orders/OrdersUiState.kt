package com.safekart.safekart.ui.presentation.orders

import com.safekart.safekart.data.model.Order

data class OrdersUiState(
    val orders: List<Order> = emptyList()
)
