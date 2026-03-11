package com.safekart.safekart.ui.presentation.orders

import com.safekart.safekart.data.model.OrderListItem

data class OrdersUiState(
    val orders: List<OrderListItem> = emptyList()
)
