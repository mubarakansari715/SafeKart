package com.safekart.safekart.ui.components

import androidx.compose.foundation.background
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
 * Network status indicator - shows dialog when device goes offline
 * Best practice: Single component managing network status UI
 */
@Composable
fun NetworkStatusIndicator(
    networkUtils: NetworkUtils
) {
    // Initialize with current network status
    val initialStatus = networkUtils.isNetworkAvailable()
    var isOffline by remember { mutableStateOf(!initialStatus) }
    // Don't show dialog immediately - wait for splash screen to finish first
    var showOfflineDialog by remember { mutableStateOf(false) }
    var previousStatus by remember { mutableStateOf(initialStatus) }
    var userDismissedDialog by remember { mutableStateOf(false) }
    var hasCheckedAfterSplash by remember { mutableStateOf(false) }
    
    // Show dialog after splash screen duration if app starts offline
    LaunchedEffect(Unit) {
        // Wait for splash screen duration (3.5 seconds) before showing dialog
        delay(3500)
        hasCheckedAfterSplash = true
        // Only show dialog if still offline and user hasn't dismissed it
        if (!networkUtils.isNetworkAvailable() && !userDismissedDialog) {
            showOfflineDialog = true
        }
    }
    
    // Monitor network status continuously (for transitions while app is running)
    LaunchedEffect(Unit) {
        while (true) {
            val currentStatus = networkUtils.isNetworkAvailable()
            
            if (!currentStatus) {
                // Device is offline
                isOffline = true
                // Show dialog if:
                // 1. App just went from online to offline (transition) - show immediately
                // 2. App started offline - only show after splash screen finished
                if (previousStatus) {
                    // Transition from online to offline - show immediately
                    showOfflineDialog = true
                    userDismissedDialog = false
                } else if (hasCheckedAfterSplash && !userDismissedDialog && !showOfflineDialog) {
                    // App started offline and splash screen finished - show now
                    showOfflineDialog = true
                    userDismissedDialog = false
                }
            } else {
                // Device is online
                isOffline = false
                showOfflineDialog = false
                userDismissedDialog = false // Reset when back online
            }
            
            // Update previous status
            previousStatus = currentStatus
            
            delay(1000) // Check every second
        }
    }
    
    
    // Offline Dialog (shows immediately when going offline)
    if (showOfflineDialog) {
        AlertDialog(
            onDismissRequest = {
                // Allow dismissing dialog even when offline
                showOfflineDialog = false
                userDismissedDialog = true
            },
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Icon on top
                    Icon(
                        imageVector = Icons.Default.WifiOff,
                        contentDescription = "Offline",
                        tint = Color(0xFFFF6B6B),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Title below icon
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
                    Spacer(modifier = Modifier.height(8.dp))
                    // First subtitle
                    Text(
                        text = "You're currently offline. Please check your internet connection and try again.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    // Second subtitle
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
                        // Dismiss dialog when OK is clicked (even if offline)
                        showOfflineDialog = false
                        userDismissedDialog = true
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
