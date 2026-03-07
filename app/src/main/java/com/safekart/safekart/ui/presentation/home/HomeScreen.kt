package com.safekart.safekart.ui.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.safekart.safekart.ui.theme.SafeKartTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onProductClick: (String) -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onBannerOrOfferClick: (String, String?) -> Unit = { _, _ -> },
    onViewAllCategory: (String?, String) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        when {
            uiState.isLoading && uiState.banners.isEmpty() && uiState.categories.isEmpty() ->
                item { FullScreenLoading() }
            uiState.error != null && uiState.banners.isEmpty() && uiState.categories.isEmpty() ->
                item {
                    ErrorRetry(
                        message = uiState.error!!,
                        onRetry = { viewModel.retry() }
                    )
                }
            else -> {
                if (uiState.banners.isNotEmpty()) {
                    item {
                        BannersSection(
                            banners = uiState.banners,
                            onClick = onBannerOrOfferClick
                        )
                    }
                }

                if (uiState.categories.isNotEmpty()) {
                    item {
                        CategoriesSection(
                            categories = uiState.categories,
                            onClick = onCategoryClick
                        )
                    }
                }

                if (uiState.bestSelling.isNotEmpty()) {
                    item {
                        Column {
                            SectionHeader(title = "Best selling", onViewAll = null)
                            ProductRow(
                                products = uiState.bestSelling,
                                onProductClick = onProductClick
                            )
                        }
                    }
                }

                uiState.offerBanner?.takeIf { it.isValid() }?.let { offer ->
                    item {
                        OfferBannerSection(
                            offer = offer,
                            onClick = onBannerOrOfferClick
                        )
                    }
                }

                uiState.productSections.forEach { section ->
                    item(key = section.title) {
                        Column {
                            SectionHeader(
                                title = section.title,
                                onViewAll = {
                                    onViewAllCategory(section.categoryId, section.categorySlug)
                                }
                            )
                            ProductRow(
                                products = section.products,
                                onProductClick = onProductClick
                            )
                        }
                    }
                }

                val offerBanner = uiState.offerBanner
                if (
                    !uiState.isLoading &&
                    uiState.banners.isEmpty() &&
                    uiState.categories.isEmpty() &&
                    (offerBanner == null || !offerBanner.isValid()) &&
                    uiState.bestSelling.isEmpty() &&
                    uiState.productSections.isEmpty() &&
                    uiState.error == null
                ) {
                    item {
                        EmptyHomeState(onRetry = { viewModel.retry() })
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Home Screen",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun HomeScreenPreview() {
    SafeKartTheme {
        HomeScreen()
    }
}
