package com.safekart.safekart.ui.presentation.cart

import androidx.lifecycle.ViewModel
import com.safekart.safekart.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    val uiState = cartRepository.cartItems.map { itemsMap ->
        val items = itemsMap.values.toList()
        val subtotal = items.sumOf { it.product.priceSale * it.quantity }
        val delivery = if (subtotal > 499) 0.0 else 49.0
        CartUiState(
            items = items,
            subtotal = subtotal,
            deliveryCharge = delivery,
            totalAmount = subtotal + delivery
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CartUiState()
    )

    fun increment(productId: String) {
        val current = cartRepository.cartItems.value[productId]?.quantity ?: return
        cartRepository.updateQuantity(productId, current + 1)
    }

    fun decrement(productId: String) {
        val current = cartRepository.cartItems.value[productId]?.quantity ?: return
        cartRepository.updateQuantity(productId, current - 1)
    }

    fun remove(productId: String) = cartRepository.removeFromCart(productId)
}
