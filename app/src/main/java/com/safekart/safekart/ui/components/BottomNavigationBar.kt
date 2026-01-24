package com.safekart.safekart.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.safekart.safekart.navigation.NavRoutes

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (currentRoute == NavRoutes.HOME) {
                        Icons.Filled.Home
                    } else {
                        Icons.Default.Home
                    },
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            selected = currentRoute == NavRoutes.HOME,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                navController.navigate(NavRoutes.HOME) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (currentRoute == NavRoutes.SEARCH) {
                        Icons.Filled.Search
                    } else {
                        Icons.Default.Search
                    },
                    contentDescription = "Search"
                )
            },
            label = { Text("Search") },
            selected = currentRoute == NavRoutes.SEARCH,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                navController.navigate(NavRoutes.SEARCH) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (currentRoute == NavRoutes.PROFILE) {
                        Icons.Filled.Person
                    } else {
                        Icons.Default.Person
                    },
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile") },
            selected = currentRoute == NavRoutes.PROFILE,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                navController.navigate(NavRoutes.PROFILE) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}
