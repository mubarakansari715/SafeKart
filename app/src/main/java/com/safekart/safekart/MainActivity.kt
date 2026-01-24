package com.safekart.safekart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.font.FontWeight
import com.safekart.safekart.util.Constants
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.safekart.safekart.navigation.NavRoutes
import com.safekart.safekart.ui.components.BottomNavigationBar
import com.safekart.safekart.ui.components.OfflineBanner
import com.safekart.safekart.ui.components.OfflineDialog
import com.safekart.safekart.ui.presentation.auth.forgotpassword.ForgotPasswordScreen
import com.safekart.safekart.util.NetworkUtils
import javax.inject.Inject
import com.safekart.safekart.ui.presentation.auth.login.LoginScreen
import com.safekart.safekart.ui.presentation.auth.register.RegisterScreen
import com.safekart.safekart.ui.presentation.cart.CartScreen
import com.safekart.safekart.ui.presentation.home.HomeScreen
import com.safekart.safekart.ui.presentation.profile.ProfileScreen
import com.safekart.safekart.ui.presentation.search.SearchScreen
import com.safekart.safekart.ui.presentation.splash.SplashScreen
import com.safekart.safekart.ui.theme.SafeKartTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var networkUtils: NetworkUtils
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SafeKartTheme {
                val navController = rememberNavController()

                Box(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.SPLASH,
                        modifier = Modifier.fillMaxSize()
                    ) {
                    composable(NavRoutes.SPLASH) {
                        SplashScreen(onNavigateToLogin = {
                            navController.navigate(NavRoutes.LOGIN) {
                                popUpTo(NavRoutes.SPLASH) { inclusive = true }
                            }
                        }, onNavigateToHome = {
                            navController.navigate(NavRoutes.HOME) {
                                popUpTo(NavRoutes.SPLASH) { inclusive = true }
                            }
                        })
                    }

                    composable(NavRoutes.LOGIN) {
                        LoginScreen(onLoginSuccess = {
                            navController.navigate(NavRoutes.HOME) {
                                popUpTo(NavRoutes.LOGIN) { inclusive = true }
                            }
                        }, onNavigateToRegister = {
                            navController.navigate(NavRoutes.REGISTER)
                        }, onNavigateToForgotPassword = {
                            navController.navigate(NavRoutes.FORGOT_PASSWORD)
                        })
                    }

                    composable(NavRoutes.FORGOT_PASSWORD) {
                        ForgotPasswordScreen(
                            onBackToLogin = {
                                navController.popBackStack()
                            })
                    }

                    composable(NavRoutes.REGISTER) {
                        RegisterScreen(onRegisterSuccess = {
                            navController.navigate(NavRoutes.HOME) {
                                popUpTo(NavRoutes.REGISTER) { inclusive = true }
                            }
                        }, onNavigateToLogin = {
                            navController.popBackStack()
                        })
                    }

                    // Main app screens with bottom navigation
                    composable(NavRoutes.HOME) {
                        MainScreenWithBottomNav(
                            navController = navController,
                            currentRoute = NavRoutes.HOME
                        ) {
                            HomeScreen()
                        }
                    }

                    composable(NavRoutes.CART) {
                        CartScreen(
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(NavRoutes.SEARCH) {
                        MainScreenWithBottomNav(
                            navController = navController,
                            currentRoute = NavRoutes.SEARCH
                        ) {
                            SearchScreen()
                        }
                    }

                    composable(NavRoutes.PROFILE) {
                        MainScreenWithBottomNav(
                            navController = navController,
                            currentRoute = NavRoutes.PROFILE
                        ) {
                            ProfileScreen(
                                onLogout = {
                                    navController.navigate(NavRoutes.LOGIN) {
                                        popUpTo(NavRoutes.PROFILE) { inclusive = true }
                                    }
                                },
                                onNavigateToOrders = {
                                    // TODO: Navigate to orders screen
                                },
                                onNavigateToWishlist = {
                                    // TODO: Navigate to wishlist screen
                                },
                                onNavigateToAddresses = {
                                    // TODO: Navigate to addresses screen
                                },
                                onNavigateToPaymentMethods = {
                                    // TODO: Navigate to payment methods screen
                                },
                                onNavigateToReviews = {
                                    // TODO: Navigate to reviews screen
                                },
                                onNavigateToNotifications = {
                                    // TODO: Navigate to notifications screen
                                },
                                onNavigateToLanguage = {
                                    // TODO: Navigate to language screen
                                },
                                onNavigateToTheme = {
                                    // TODO: Navigate to theme screen
                                },
                                onNavigateToHelp = {
                                    // TODO: Navigate to help screen
                                },
                                onNavigateToAbout = {
                                    // TODO: Navigate to about screen
                                },
                                onNavigateToTerms = {
                                    // TODO: Navigate to terms screen
                                },
                                onNavigateToPrivacy = {
                                    // TODO: Navigate to privacy screen
                                },
                                onNavigateToEditProfile = {
                                    // TODO: Navigate to edit profile screen
                                }
                            )
                        }
                    }
                    
                    // Offline banner at the top - appears when device is offline
                    OfflineBanner(
                        networkUtils = networkUtils,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                    )
                    
                    // Offline dialog - shows immediately when device goes offline
                    OfflineDialog(
                        networkUtils = networkUtils
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWithBottomNav(
    navController: androidx.navigation.NavController,
    currentRoute: String,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            // Show TopAppBar only on Home screen
            if (currentRoute == NavRoutes.HOME) {
                HomeTopAppBar(
                    onNavigateToCart = {
                        navController.navigate(NavRoutes.CART)
                    }
                )
            }
        },
        bottomBar = {
            // Only show bottom navigation on main app screens
            if (currentRoute in listOf(NavRoutes.HOME, NavRoutes.SEARCH, NavRoutes.PROFILE)) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    onNavigateToCart: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = Constants.APP_NAME,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        actions = {
            IconButton(onClick = onNavigateToCart) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Shopping Cart"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}