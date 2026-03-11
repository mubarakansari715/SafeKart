package com.safekart.safekart.domain.repository

import com.safekart.safekart.data.model.Order
import com.safekart.safekart.data.model.OrderListItem
import kotlinx.coroutines.flow.StateFlow

interface OrderRepository {
    val orders: StateFlow<List<OrderListItem>>
    fun placeOrder(order: Order): Order
    suspend fun placeOrderRemote(order: Order): Result<Order>
    suspend fun loadOrders()
}
