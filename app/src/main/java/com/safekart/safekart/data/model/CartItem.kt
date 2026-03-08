package com.safekart.safekart.data.model

data class CartItem(
    val product: HomeProduct,
    val quantity: Int = 1
)
