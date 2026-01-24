package com.safekart.safekart.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.safekart.safekart.util.NetworkUtils
import kotlinx.coroutines.delay

/**
 * Offline banner that appears at the top of the screen when device is offline
 * Modern, non-intrusive design that doesn't block user interaction
 */
@Composable
fun OfflineBanner(
    networkUtils: NetworkUtils,
    modifier: Modifier = Modifier
) {
    var isOffline by remember { mutableStateOf(!networkUtils.isNetworkAvailable()) }
    
    // Check network status periodically
    LaunchedEffect(Unit) {
        while (true) {
            isOffline = !networkUtils.isNetworkAvailable()
            delay(2000) // Check every 2 seconds
        }
    }
    
    AnimatedVisibility(
        visible = isOffline,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF6B6B)) // Red color for offline
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WifiOff,
                    contentDescription = "Offline",
                    tint = Color.White,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "You're offline. Please check your internet connection.",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}
