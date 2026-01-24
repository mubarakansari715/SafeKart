package com.safekart.safekart.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.safekart.safekart.ui.theme.SafeKartTheme

/**
 * Common full-screen loading indicator component
 * Shows a centered circular progress indicator on a clean background
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    progressIndicatorSize: Int = 48
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(progressIndicatorSize.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
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

