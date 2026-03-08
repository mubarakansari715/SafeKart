package com.safekart.safekart.ui.presentation.home

import com.safekart.safekart.data.model.Banner
import com.safekart.safekart.data.model.HomeCategory
import com.safekart.safekart.data.model.HomeProduct
import com.safekart.safekart.data.model.OfferBanner
import com.safekart.safekart.data.model.ProductSection

data class HomeUiState(
    val welcomeMessage: String = "Welcome!",
    val userEmail: String = "",
    val banners: List<Banner> = emptyList(),
    val categories: List<HomeCategory> = emptyList(),
    val bestSelling: List<HomeProduct> = emptyList(),
    val offerBanner: OfferBanner? = null,
    val productSections: List<ProductSection> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null
)
