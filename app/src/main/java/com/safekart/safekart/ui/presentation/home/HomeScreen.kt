package com.safekart.safekart.ui.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.safekart.safekart.ui.theme.SafeKartTheme

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Empty home screen content for now
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

