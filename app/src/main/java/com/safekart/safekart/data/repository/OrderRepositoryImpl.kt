package com.safekart.safekart.data.repository

import com.safekart.safekart.data.model.Address
import com.safekart.safekart.data.model.CartItem
import com.safekart.safekart.data.model.CartItemSnapshot
import com.safekart.safekart.data.model.Order
import com.safekart.safekart.data.model.OrderDto
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

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    override val orders: StateFlow<List<Order>> = _orders.asStateFlow()

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
            val placed = order.copy(id = dto.id)
            _orders.value = listOf(placed) + _orders.value
            placed
        }
    }

    override suspend fun loadOrders() {
        remote.getOrders().onSuccess { dtos ->
            _orders.value = dtos.map { it.toDomain() }
        }
    }

    private fun OrderDto.toDomain(): Order {
        val addr = shippingAddress
        val domainAddr = if (addr != null) Address(
            id = addr.id,
            fullName = addr.fullName,
            phone = addr.phone,
            street = addr.line1,
            city = addr.city,
            state = addr.state,
            pincode = addr.postalCode,
            isDefault = addr.isDefault
        ) else Address(id = "", fullName = "")

        val domainItems = items.map { snap ->
            val s = snap.productSnapshot
            CartItem(
                product = com.safekart.safekart.data.model.HomeProduct(
                    id = snap.productId,
                    title = s?.title ?: "",
                    brand = s?.brand ?: "",
                    priceSale = s?.priceSale ?: 0.0,
                    priceMrp = s?.priceMrp ?: 0.0,
                    discountPercent = s?.discountPercent ?: 0,
                    imageUrls = s?.imageUrls ?: emptyList(),
                    stock = s?.stock ?: 0
                ),
                quantity = snap.quantity
            )
        }

        return Order(
            id = id,
            items = domainItems,
            address = domainAddr,
            paymentMethod = paymentMethod,
            status = status,
            totalAmount = total,
            createdAt = try { java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault()).parse(createdAt)?.time ?: System.currentTimeMillis() } catch (e: Exception) { System.currentTimeMillis() }
        )
    }
}
