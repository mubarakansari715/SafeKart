package com.safekart.safekart.data.model

data class Order(
    val id: String = java.util.UUID.randomUUID().toString(),
    val items: List<CartItem> = emptyList(),
    val address: Address,
    val paymentMethod: String = "COD",
    val status: String = "Placed",
    val totalAmount: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)
