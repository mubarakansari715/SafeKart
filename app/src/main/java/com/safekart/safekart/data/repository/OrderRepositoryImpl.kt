package com.safekart.safekart.data.repository

import com.safekart.safekart.data.model.CartItemSnapshot
import com.safekart.safekart.data.model.Order
import com.safekart.safekart.data.model.OrderDto
import com.safekart.safekart.data.model.OrderListItem
import com.safekart.safekart.data.model.PlaceOrderRequest
import com.safekart.safekart.data.model.ProductSnapshot
import com.safekart.safekart.data.remote.customer.OrderRemoteDataSource
import com.safekart.safekart.domain.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val remote: OrderRemoteDataSource
) : OrderRepository {

    private val _orders = MutableStateFlow<List<OrderListItem>>(emptyList())
    override val orders: StateFlow<List<OrderListItem>> = _orders.asStateFlow()

    override fun placeOrder(order: Order): Order = order // UI calls placeOrderRemote instead

    override suspend fun placeOrderRemote(order: Order): Result<Order> {
        val snapshots = order.items.map { item ->
            CartItemSnapshot(
                productId = item.product.id,
                quantity = item.quantity,
                productSnapshot = ProductSnapshot(
                    id = item.product.id,
                    title = item.product.title,
                    brand = item.product.brand,
                    priceSale = item.product.priceSale,
                    priceMrp = item.product.priceMrp,
                    discountPercent = item.product.discountPercent,
                    imageUrls = item.product.imageUrls,
                    stock = item.product.stock
                )
            )
        }
        val request = PlaceOrderRequest(
            items = snapshots,
            addressId = order.address.id,
            paymentMethod = order.paymentMethod,
            subtotal = order.items.sumOf { it.product.priceSale * it.quantity },
            shippingFee = order.totalAmount - order.items.sumOf { it.product.priceSale * it.quantity },
            total = order.totalAmount
        )
        return remote.placeOrder(request).map { dto ->
            loadOrders()
            order.copy(id = dto.id, orderNumber = dto.orderNumber)
        }
    }

    override suspend fun loadOrders() {
        remote.getOrders().onSuccess { items ->
            _orders.value = items
        }
    }
}
