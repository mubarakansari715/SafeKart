package com.safekart.safekart.ui.presentation.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safekart.safekart.domain.repository.CartRepository
import com.safekart.safekart.domain.repository.HomeRepository
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
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val homeRepository: HomeRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val _cartEvent = MutableSharedFlow<CartEvent>()
    val cartEvent: SharedFlow<CartEvent> = _cartEvent.asSharedFlow()

    init {
        loadProduct()
        viewModelScope.launch {
            cartRepository.cartItems.collect { items ->
                val qty = items[productId]?.quantity ?: 0
                _uiState.update { it.copy(cartQuantity = qty) }
            }
        }
    }

    fun retry() = loadProduct()

    fun addToCart() {
        val product = _uiState.value.product ?: return
        cartRepository.addToCart(product)
        viewModelScope.launch {
            _cartEvent.emit(CartEvent.Added)
        }
    }

    fun increaseQuantity() {
        val product = _uiState.value.product ?: return
        val current = _uiState.value.cartQuantity
        if (current <= 0) {
            addToCart()
            return
        }
        if (current >= product.stock) return
        cartRepository.updateQuantity(product.id, current + 1)
    }

    fun decreaseQuantity() {
        val product = _uiState.value.product ?: return
        val current = _uiState.value.cartQuantity
        if (current <= 1) {
            cartRepository.removeFromCart(product.id)
            return
        }
        cartRepository.updateQuantity(product.id, current - 1)
    }

    private fun loadProduct() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            homeRepository.getHome().fold(
                onSuccess = { homeData ->
                    val product = homeData.bestSelling.find { it.id == productId }
                        ?: homeData.product.flatMap { it.products }.find { it.id == productId }
                    if (product != null) {
                        _uiState.update { it.copy(product = product, isLoading = false) }
                    } else {
                        _uiState.update { it.copy(error = "Product not found", isLoading = false) }
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(error = error.message ?: "Something went wrong.", isLoading = false)
                    }
                }
            )
        }
    }
}

sealed class CartEvent {
    data object Added : CartEvent()
}
