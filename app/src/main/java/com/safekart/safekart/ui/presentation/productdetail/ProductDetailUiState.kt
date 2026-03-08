package com.safekart.safekart.ui.presentation.productdetail

import com.safekart.safekart.data.model.HomeProduct

data class ProductDetailUiState(
    val product: HomeProduct? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)
