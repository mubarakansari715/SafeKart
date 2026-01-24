package com.safekart.safekart.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.safekart.safekart.util.NetworkUtils
import kotlinx.coroutines.delay

/**
 * Offline dialog that appears immediately when device goes offline
 * Shows on any screen and appears as soon as offline is detected
 */
@Composable
fun OfflineDialog(
    networkUtils: NetworkUtils,
    onDismiss: () -> Unit = {}
) {
    // Initialize with current network status
    val initialStatus = networkUtils.isNetworkAvailable()
    var isOffline by remember { mutableStateOf(!initialStatus) }
    var previousStatus by remember { mutableStateOf(initialStatus) }
    
    // Check network status periodically and detect when it changes from online to offline
    LaunchedEffect(Unit) {
        while (true) {
            val currentStatus = networkUtils.isNetworkAvailable()
            
            // Show dialog immediately when going from online to offline
            if (!currentStatus && previousStatus) {
                isOffline = true
            }
            
            // Hide dialog when back online
            if (currentStatus) {
                isOffline = false
            }
            
            // Update previous status
            previousStatus = currentStatus
            
            delay(1000) // Check every second for faster detection
        }
    }
    
    if (isOffline) {
        AlertDialog(
            onDismissRequest = {
                // Don't allow dismissing - user must be online
                // But we can hide it if they come back online
                if (networkUtils.isNetworkAvailable()) {
                    isOffline = false
                    onDismiss()
                }
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.WifiOff,
                        contentDescription = "Offline",
                        tint = Color(0xFFFF6B6B),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Text(
                        text = "No Internet Connection",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "You're currently offline. Please check your internet connection and try again.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "The app will automatically reconnect when your connection is restored.",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Check if online now
                        if (networkUtils.isNetworkAvailable()) {
                            isOffline = false
                            onDismiss()
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}
