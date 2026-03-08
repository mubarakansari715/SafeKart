package com.safekart.safekart.ui.presentation.category

import com.safekart.safekart.data.model.HomeCategory

data class CategoryUiState(
    val categories: List<HomeCategory> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
