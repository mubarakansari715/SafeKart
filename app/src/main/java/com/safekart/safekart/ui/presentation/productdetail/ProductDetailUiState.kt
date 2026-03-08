package com.safekart.safekart.ui.presentation.productdetail

import com.safekart.safekart.data.model.HomeProduct

data class ProductDetailUiState(
    val product: HomeProduct? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    /** Quantity of this product in the cart (0 if not in cart). */
    val cartQuantity: Int = 0
)
