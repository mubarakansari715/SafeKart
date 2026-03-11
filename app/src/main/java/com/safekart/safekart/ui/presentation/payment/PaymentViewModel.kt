package com.safekart.safekart.ui.presentation.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safekart.safekart.data.model.Order
import com.safekart.safekart.domain.repository.AddressRepository
import com.safekart.safekart.domain.repository.CartRepository
import com.safekart.safekart.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val cartRepository: CartRepository,
    private val addressRepository: AddressRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val addressId: String = checkNotNull(savedStateHandle["addressId"])

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    private val _orderPlacedEvent = MutableSharedFlow<String>() // emits orderId
    val orderPlacedEvent: SharedFlow<String> = _orderPlacedEvent.asSharedFlow()

    init {
        val items = cartRepository.cartItems.value.values.toList()
        val subtotal = items.sumOf { it.product.priceSale * it.quantity }
        val delivery = if (subtotal > 499) 0.0 else 49.0
        _uiState.update {
            it.copy(
                items = items,
                subtotal = subtotal,
                deliveryCharge = delivery,
                totalAmount = subtotal + delivery
            )
        }

        // Reactively resolve address — handles temp UUID being replaced by server UUID after API responds
        viewModelScope.launch {
            addressRepository.addresses.collect { list ->
                val address = list.find { it.id == addressId }
                    ?: list.firstOrNull { it.isDefault }
                    ?: list.firstOrNull()
                _uiState.update { it.copy(address = address) }
            }
        }
    }

    fun placeOrder() {
        val state = _uiState.value
        val address = state.address ?: return
        if (state.isPlacingOrder) return

        viewModelScope.launch {
            _uiState.update { it.copy(isPlacingOrder = true) }
            // Simulate a brief processing delay
            kotlinx.coroutines.delay(800)
            val order = Order(
                items = state.items,
                address = address,
                paymentMethod = "COD",
                totalAmount = state.totalAmount
            )
            orderRepository.placeOrderRemote(order).fold(
                onSuccess = { placed ->
                    cartRepository.clearCart()
                    _uiState.update { it.copy(isPlacingOrder = false) }
                    _orderPlacedEvent.emit(placed.orderNumber.ifBlank { placed.id })
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isPlacingOrder = false, error = error.message ?: "Failed to place order") }
                }
            )
        }
    }
}
