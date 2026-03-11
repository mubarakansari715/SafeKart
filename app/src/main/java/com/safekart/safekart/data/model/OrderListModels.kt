package com.safekart.safekart.data.model

import com.google.gson.annotations.SerializedName

// ── API Response DTOs ─────────────────────────────────────────────────────────

data class OrderListItemDto(
    val id: String = "",
    @SerializedName("order_number") val orderNumber: String = "",
    val status: String = "placed",
    @SerializedName("payment_method") val paymentMethod: String = "cod",
    @SerializedName("payment_status") val paymentStatus: String = "pending",
    val subtotal: Double = 0.0,
    @SerializedName("shipping_fee") val shippingFee: Double = 0.0,
    val total: Double = 0.0,
    val items: List<OrderListItemSnapshotDto> = emptyList(),
    @SerializedName("shipping_address") val shippingAddress: OrderShippingAddressDto? = null,
    @SerializedName("created_at") val createdAt: String = "",
    @SerializedName("customer_timezone") val customerTimezone: String? = null,
    @SerializedName("created_at_local") val createdAtLocal: String = ""
)

data class OrderListItemSnapshotDto(
    val quantity: Int = 1,
    @SerializedName("product_id") val productId: String = "",
    @SerializedName("product_snapshot") val productSnapshot: OrderProductSnapshotDto? = null
)

data class OrderProductSnapshotDto(
    val id: String = "",
    val brand: String = "",
    val stock: Int = 0,
    val title: String = "",
    @SerializedName("price_mrp") val priceMrp: Double = 0.0,
    @SerializedName("image_urls") val imageUrls: List<String> = emptyList(),
    @SerializedName("price_sale") val priceSale: Double = 0.0,
    @SerializedName("discount_percent") val discountPercent: Int = 0
) {
    fun firstImageUrl(): String? = imageUrls.firstOrNull()?.takeIf { it.isNotBlank() }
}

data class OrderShippingAddressDto(
    val city: String = "",
    val line1: String = "",
    val phone: String = "",
    val state: String = "",
    val country: String = "India",
    @SerializedName("full_name") val fullName: String = "",
    @SerializedName("postal_code") val postalCode: String = ""
)

// ── Domain Model (used by UI) ─────────────────────────────────────────────────

data class OrderListItem(
    val id: String,
    val orderNumber: String,
    val status: String,
    val paymentMethod: String,
    val paymentStatus: String,
    val subtotal: Double,
    val shippingFee: Double,
    val total: Double,
    val items: List<OrderProductItem>,
    val shippingAddress: OrderShippingAddress,
    val createdAt: String,
    val createdAtLocal: String
)

data class OrderProductItem(
    val quantity: Int,
    val productId: String,
    val title: String,
    val imageUrls: List<String>
) {
    fun firstImageUrl(): String? = imageUrls.firstOrNull()?.takeIf { it.isNotBlank() }
}

data class OrderShippingAddress(
    val fullName: String,
    val phone: String,
    val line1: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String
)

// ── Mapping ───────────────────────────────────────────────────────────────────

private fun parseCreatedAtToLocal(isoString: String): String {
    return try {
        val formatter = java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val zonedDateTime = java.time.ZonedDateTime.parse(isoString, formatter)
            .withZoneSameInstant(java.time.ZoneId.systemDefault())
        java.time.format.DateTimeFormatter
            .ofPattern("MMM d, yyyy, hh:mm a", java.util.Locale.getDefault())
            .format(zonedDateTime)
    } catch (e: Exception) {
        isoString
    }
}

fun OrderListItemDto.toDomain() = OrderListItem(
    id = id,
    orderNumber = orderNumber,
    status = status,
    paymentMethod = paymentMethod,
    paymentStatus = paymentStatus,
    subtotal = subtotal,
    shippingFee = shippingFee,
    total = total,
    items = items.map { snap ->
        OrderProductItem(
            quantity = snap.quantity,
            productId = snap.productId,
            title = snap.productSnapshot?.title ?: "",
            imageUrls = snap.productSnapshot?.imageUrls ?: emptyList()
        )
    },
    shippingAddress = shippingAddress?.let {
        OrderShippingAddress(
            fullName = it.fullName,
            phone = it.phone,
            line1 = it.line1,
            city = it.city,
            state = it.state,
            postalCode = it.postalCode,
            country = it.country
        )
    } ?: OrderShippingAddress("", "", "", "", "", "", "India"),
    createdAt = createdAt,
    createdAtLocal = parseCreatedAtToLocal(createdAt)
)