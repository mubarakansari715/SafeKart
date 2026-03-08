package com.safekart.safekart.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.safekart.safekart.ui.theme.SafeKartTheme

/**
 * Full-screen transparent overlay with centered circular progress indicator.
 * Use for loading states (e.g. address fetch, form submit).
 */
@Composable
fun FullScreenProgressIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * Common full-screen loading indicator component.
 * Shows Facebook-style shimmer skeleton placeholders.
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    ShimmerLoadingIndicator(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    )
}

@Preview(
    name = "Loading Indicator",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoadingIndicatorPreview() {
    SafeKartTheme {
        LoadingIndicator()
    }
}

