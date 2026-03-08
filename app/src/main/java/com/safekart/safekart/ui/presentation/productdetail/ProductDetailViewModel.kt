package com.safekart.safekart.ui.presentation.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safekart.safekart.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        loadProduct()
    }

    fun retry() = loadProduct()

    private fun loadProduct() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            homeRepository.getHome().fold(
                onSuccess = { homeData ->
                    // Search across all product sections (best selling + product sections)
                    val product = homeData.bestSelling.find { it.id == productId }
                        ?: homeData.product.flatMap { it.products }.find { it.id == productId }

                    if (product != null) {
                        _uiState.update { it.copy(product = product, isLoading = false) }
                    } else {
                        _uiState.update { it.copy(error = "Product not found", isLoading = false) }
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Something went wrong. Please try again.",
                            isLoading = false
                        )
                    }
                }
            )
        }
    }
}
