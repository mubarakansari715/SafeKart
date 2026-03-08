package com.safekart.safekart.domain.repository

import com.safekart.safekart.data.model.CartItem
import com.safekart.safekart.data.model.HomeProduct
import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    val cartItems: StateFlow<Map<String, CartItem>>
    fun addToCart(product: HomeProduct)
    fun removeFromCart(productId: String)
    fun updateQuantity(productId: String, quantity: Int)
    fun clearCart()
}
