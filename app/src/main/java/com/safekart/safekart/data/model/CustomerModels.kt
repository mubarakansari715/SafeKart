package com.safekart.safekart.data.model

import com.google.gson.annotations.SerializedName

// ── Cart ──────────────────────────────────────────────────────────────────────

data class CartItemSnapshot(
    @SerializedName("product_id") val productId: String = "",
    val quantity: Int = 1,
    @SerializedName("product_snapshot") val productSnapshot: ProductSnapshot? = null
)

data class ProductSnapshot(
    val id: String = "",
    val title: String = "",
    val brand: String = "",
    @SerializedName("price_sale") val priceSale: Double = 0.0,
    @SerializedName("price_mrp") val priceMrp: Double = 0.0,
    @SerializedName("discount_percent") val discountPercent: Int = 0,
    @SerializedName("image_urls") val imageUrls: List<String> = emptyList(),
    val stock: Int = 0
) {
    fun firstImageUrl(): String? = imageUrls.firstOrNull()?.takeIf { it.isNotBlank() }
}

data class CartResponse(val items: List<CartItemSnapshot> = emptyList())

data class AddCartItemRequest(
    @SerializedName("product_id") val productId: String,
    val quantity: Int = 1,
    @SerializedName("product_snapshot") val productSnapshot: ProductSnapshot
)

data class UpdateCartItemRequest(
    @SerializedName("product_id") val productId: String,
    val quantity: Int
)

// ── Address ───────────────────────────────────────────────────────────────────

data class AddressDto(
    val id: String = "",
    @SerializedName("full_name") val fullName: String = "",
    val phone: String = "",
    val line1: String = "",
    val city: String = "",
    val state: String = "",
    @SerializedName("postal_code") val postalCode: String = "",
    val country: String = "India",
    @SerializedName("is_default") val isDefault: Boolean = false
)

data class SaveAddressRequest(
    @SerializedName("full_name") val fullName: String,
    val phone: String,
    val line1: String,
    val city: String,
    val state: String,
    @SerializedName("postal_code") val postalCode: String,
    val country: String = "India",
    @SerializedName("is_default") val isDefault: Boolean = false
)

// ── Order ─────────────────────────────────────────────────────────────────────

data class OrderDto(
    val id: String = "",
    @SerializedName("order_number") val orderNumber: String = "",
    val status: String = "placed",
    @SerializedName("payment_method") val paymentMethod: String = "COD",
    @SerializedName("payment_status") val paymentStatus: String = "pending",
    val subtotal: Double = 0.0,
    @SerializedName("shipping_fee") val shippingFee: Double = 0.0,
    val total: Double = 0.0,
    val items: List<CartItemSnapshot> = emptyList(),
    @SerializedName("shipping_address") val shippingAddress: AddressDto? = null,
    @SerializedName("created_at") val createdAt: String = ""
)

data class PlaceOrderRequest(
    val items: List<CartItemSnapshot>,
    @SerializedName("address_id") val addressId: String,
    @SerializedName("payment_method") val paymentMethod: String = "COD",
    val subtotal: Double,
    @SerializedName("shipping_fee") val shippingFee: Double,
    val total: Double
)

