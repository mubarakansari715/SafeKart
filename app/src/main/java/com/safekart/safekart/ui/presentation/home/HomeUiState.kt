package com.safekart.safekart.ui.presentation.home

data class HomeUiState(
    val welcomeMessage: String = "Welcome!",
    val userEmail: String = "",
    val isLoading: Boolean = true
)

