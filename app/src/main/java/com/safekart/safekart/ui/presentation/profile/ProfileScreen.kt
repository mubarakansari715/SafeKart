package com.safekart.safekart.ui.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit = {},
    onNavigateToOrders: () -> Unit = {},
    onNavigateToWishlist: () -> Unit = {},
    onNavigateToAddresses: () -> Unit = {},
    onNavigateToPaymentMethods: () -> Unit = {},
    onNavigateToReviews: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToLanguage: () -> Unit = {},
    onNavigateToTheme: () -> Unit = {},
    onNavigateToHelp: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {},
    onNavigateToPrivacy: () -> Unit = {},
    onNavigateToEditProfile: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // No need to call API on every screen load - data is already loaded from cache in init
    // Screen renders immediately with cached data
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
                // Profile Header
                ProfileHeader(
                    userName = uiState.userName.ifEmpty { "User" },
                    userEmail = uiState.userEmail,
                    userPhone = uiState.userPhone,
                    onEditProfile = onNavigateToEditProfile
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Menu Items
                ProfileMenuSection(
                    onNavigateToOrders = onNavigateToOrders,
                    onNavigateToWishlist = onNavigateToWishlist,
                    onNavigateToAddresses = onNavigateToAddresses,
                    onNavigateToPaymentMethods = onNavigateToPaymentMethods,
                    onNavigateToReviews = onNavigateToReviews
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Settings Section
                SettingsSection(
                    onNavigateToNotifications = onNavigateToNotifications,
                    onNavigateToLanguage = onNavigateToLanguage,
                    onNavigateToTheme = onNavigateToTheme
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Support Section
                SupportSection(
                    onNavigateToHelp = onNavigateToHelp,
                    onNavigateToAbout = onNavigateToAbout,
                    onNavigateToTerms = onNavigateToTerms,
                    onNavigateToPrivacy = onNavigateToPrivacy
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Logout Button
                LogoutButton(
                    onLogout = {
                        viewModel.logout()
                        onLogout()
                    }
                )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProfileHeader(
    userName: String,
    userEmail: String,
    userPhone: String,
    onEditProfile: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture Placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = userName,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (userEmail.isNotEmpty()) {
                Text(
                    text = userEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (userPhone.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = userPhone,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Profile Button
            OutlinedButton(
                onClick = onEditProfile,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profile")
            }
        }
    }
}

@Composable
fun ProfileMenuSection(
    onNavigateToOrders: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    onNavigateToAddresses: () -> Unit,
    onNavigateToPaymentMethods: () -> Unit,
    onNavigateToReviews: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            ProfileMenuItem(
                icon = Icons.Default.ShoppingCart,
                title = "My Orders",
                onClick = onNavigateToOrders
            )
            Divider()
            ProfileMenuItem(
                icon = Icons.Default.Favorite,
                title = "My Wishlist",
                onClick = onNavigateToWishlist
            )
            Divider()
            ProfileMenuItem(
                icon = Icons.Default.LocationOn,
                title = "Manage Addresses",
                onClick = onNavigateToAddresses
            )
            Divider()
            ProfileMenuItem(
                icon = Icons.Default.CreditCard,
                title = "Saved Payment Methods",
                onClick = onNavigateToPaymentMethods
            )
            Divider()
            ProfileMenuItem(
                icon = Icons.Default.Star,
                title = "My Reviews",
                onClick = onNavigateToReviews
            )
        }
    }
}

@Composable
fun SettingsSection(
    onNavigateToNotifications: () -> Unit,
    onNavigateToLanguage: () -> Unit,
    onNavigateToTheme: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            ProfileMenuItem(
                icon = Icons.Default.Notifications,
                title = "Notification Settings",
                onClick = onNavigateToNotifications
            )
            Divider()
            ProfileMenuItem(
                icon = Icons.Default.Translate,
                title = "Language",
                onClick = onNavigateToLanguage
            )
            Divider()
            ProfileMenuItem(
                icon = Icons.Default.Palette,
                title = "Theme",
                onClick = onNavigateToTheme
            )
        }
    }
}

@Composable
fun SupportSection(
    onNavigateToHelp: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToTerms: () -> Unit,
    onNavigateToPrivacy: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            ProfileMenuItem(
                icon = Icons.Default.Help,
                title = "Help & Support",
                onClick = onNavigateToHelp
            )
            Divider()
            ProfileMenuItem(
                icon = Icons.Default.Info,
                title = "About",
                onClick = onNavigateToAbout
            )
            Divider()
            ProfileMenuItem(
                icon = Icons.Default.Description,
                title = "Terms & Conditions",
                onClick = onNavigateToTerms
            )
            Divider()
            ProfileMenuItem(
                icon = Icons.Default.Lock,
                title = "Privacy Policy",
                onClick = onNavigateToPrivacy
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun LogoutButton(
    onLogout: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onLogout)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Logout",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Logout",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
