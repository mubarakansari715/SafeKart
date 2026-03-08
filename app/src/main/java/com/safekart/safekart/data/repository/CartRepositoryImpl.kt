package com.safekart.safekart.data.repository

import com.safekart.safekart.data.model.AddCartItemRequest
import com.safekart.safekart.data.model.CartItem
import com.safekart.safekart.data.model.CartItemSnapshot
import com.safekart.safekart.data.model.HomeProduct
import com.safekart.safekart.data.model.ProductSnapshot
import com.safekart.safekart.data.remote.customer.CartRemoteDataSource
import com.safekart.safekart.domain.repository.CartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val remote: CartRemoteDataSource
) : CartRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _cartItems = MutableStateFlow<Map<String, CartItem>>(emptyMap())
    override val cartItems: StateFlow<Map<String, CartItem>> = _cartItems.asStateFlow()

    init {
        scope.launch { loadFromApi() }
    }

    private suspend fun loadFromApi() {
        remote.getCart().onSuccess { snapshots -> _cartItems.value = snapshots.toCartItemMap() }
    }

    override fun addToCart(product: HomeProduct) {
        // Optimistic update
        _cartItems.update { current ->
            val existing = current[product.id]
            if (existing != null) current + (product.id to existing.copy(quantity = existing.quantity + 1))
            else current + (product.id to CartItem(product = product))
        }
        scope.launch {
            val request = AddCartItemRequest(
                productId = product.id,
                quantity = 1,
                productSnapshot = product.toSnapshot()
            )
            remote.addItem(request).onSuccess { snapshots -> _cartItems.value = snapshots.toCartItemMap() }
        }
    }

    override fun removeFromCart(productId: String) {
        _cartItems.update { it - productId }
        scope.launch {
            remote.removeItem(productId).onSuccess { snapshots -> _cartItems.value = snapshots.toCartItemMap() }
        }
    }

    override fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) { removeFromCart(productId); return }
        _cartItems.update { current ->
            val existing = current[productId] ?: return@update current
            current + (productId to existing.copy(quantity = quantity))
        }
        scope.launch {
            remote.updateItem(productId, quantity).onSuccess { snapshots -> _cartItems.value = snapshots.toCartItemMap() }
        }
    }

    override fun clearCart() {
        _cartItems.value = emptyMap()
        // Server clears cart automatically when order is placed
    }

    // ── Conversion helpers ────────────────────────────────────────────────────

    private fun List<CartItemSnapshot>.toCartItemMap(): Map<String, CartItem> =
        associate { snap ->
            val product = snap.toHomeProduct()
            snap.productId to CartItem(product = product, quantity = snap.quantity)
        }

    private fun CartItemSnapshot.toHomeProduct(): HomeProduct {
        val s = productSnapshot
        return HomeProduct(
            id = productId,
            title = s?.title ?: "",
            brand = s?.brand ?: "",
            priceSale = s?.priceSale ?: 0.0,
            priceMrp = s?.priceMrp ?: 0.0,
            discountPercent = s?.discountPercent ?: 0,
            imageUrls = s?.imageUrls ?: emptyList(),
            stock = s?.stock ?: 0
        )
    }

    private fun HomeProduct.toSnapshot() = ProductSnapshot(
        id = id,
        title = title,
        brand = brand,
        priceSale = priceSale,
        priceMrp = priceMrp,
        discountPercent = discountPercent,
        imageUrls = imageUrls,
        stock = stock
    )
}
