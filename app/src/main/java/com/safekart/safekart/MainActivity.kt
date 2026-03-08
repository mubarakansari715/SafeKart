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
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.safekart.safekart.ui.presentation.cart.CartViewModel
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.safekart.safekart.navigation.NavRoutes
import com.safekart.safekart.ui.components.BottomNavigationBar
import com.safekart.safekart.ui.components.NetworkStatusIndicator
import com.safekart.safekart.ui.presentation.auth.forgotpassword.ForgotPasswordScreen
import com.safekart.safekart.util.NetworkUtils
import javax.inject.Inject
import com.safekart.safekart.ui.presentation.auth.login.LoginScreen
import com.safekart.safekart.ui.presentation.auth.register.RegisterScreen
import com.safekart.safekart.ui.presentation.cart.CartScreen
import com.safekart.safekart.ui.presentation.category.CategoryScreen
import com.safekart.safekart.ui.presentation.home.HomeScreen
import com.safekart.safekart.ui.presentation.productdetail.ProductDetailScreen
import com.safekart.safekart.ui.presentation.profile.ProfileScreen
import com.safekart.safekart.ui.presentation.search.SearchScreen
import com.safekart.safekart.ui.presentation.splash.SplashScreen
import com.safekart.safekart.ui.theme.SafeKartTheme
import com.safekart.safekart.ui.presentation.address.AddressScreen
import com.safekart.safekart.ui.presentation.orders.OrdersScreen
import com.safekart.safekart.ui.presentation.ordersuccess.OrderSuccessScreen
import com.safekart.safekart.ui.presentation.payment.PaymentScreen
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
                                HomeScreen(
                                    onProductClick = { productId ->
                                        navController.navigate(NavRoutes.productDetail(productId))
                                    },
                                    onViewAllCategory = { _, type ->
                                        if (type == "categories") {
                                            navController.navigate(NavRoutes.CATEGORIES) {
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    }
                                )
                            }
                        }

                        composable(
                            route = NavRoutes.PRODUCT_DETAIL,
                            arguments = listOf(navArgument("productId") { type = NavType.StringType })
                        ) {
                            ProductDetailScreen(
                                onBack = { navController.popBackStack() },
                                onNavigateToCart = { navController.navigate(NavRoutes.CART) }
                            )
                        }

                        composable(NavRoutes.ADDRESS) {
                            AddressScreen(
                                onBack = { navController.popBackStack() },
                                onAddressSelected = { addressId ->
                                    navController.navigate(NavRoutes.payment(addressId))
                                }
                            )
                        }

                        composable(
                            route = NavRoutes.PAYMENT,
                            arguments = listOf(navArgument("addressId") { type = NavType.StringType })
                        ) {
                            PaymentScreen(
                                onBack = { navController.popBackStack() },
                                onOrderPlaced = { orderId ->
                                    navController.navigate(NavRoutes.orderSuccess(orderId)) {
                                        popUpTo(NavRoutes.CART) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(
                            route = NavRoutes.ORDER_SUCCESS,
                            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                            OrderSuccessScreen(
                                orderId = orderId,
                                onViewOrders = {
                                    navController.navigate(NavRoutes.MY_ORDERS) {
                                        popUpTo(NavRoutes.ORDER_SUCCESS) { inclusive = true }
                                    }
                                },
                                onContinueShopping = {
                                    navController.navigate(NavRoutes.HOME) {
                                        popUpTo(NavRoutes.HOME) { inclusive = false }
                                    }
                                }
                            )
                        }

                        composable(NavRoutes.MY_ORDERS) {
                            OrdersScreen(
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(NavRoutes.CATEGORIES) {
                            MainScreenWithBottomNav(
                                navController = navController,
                                currentRoute = NavRoutes.CATEGORIES
                            ) {
                                CategoryScreen()
                            }
                        }

                        composable(NavRoutes.CART) {
                            CartScreen(
                                onBack = { navController.popBackStack() },
                                onProceedToCheckout = {
                                    navController.navigate(NavRoutes.ADDRESS)
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
                                        navController.navigate(NavRoutes.MY_ORDERS)
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
                    }

                    // Network status indicator - shows dialog when offline
                    NetworkStatusIndicator(
                        networkUtils = networkUtils
                    )
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
                when (currentRoute) {
                    NavRoutes.HOME -> HomeTopAppBar(
                        onNavigateToCart = {
                            navController.navigate(NavRoutes.CART)
                        }
                    )
                    NavRoutes.CATEGORIES -> AppBarWithTitle(title = "Categories")
                    NavRoutes.SEARCH -> AppBarWithTitle(title = "Search")
                    NavRoutes.PROFILE -> AppBarWithTitle(title = "Profile")
                }
            },
            bottomBar = {
                // Only show bottom navigation on main app screens
                if (currentRoute in listOf(NavRoutes.HOME, NavRoutes.CATEGORIES, NavRoutes.SEARCH, NavRoutes.PROFILE)) {
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
        val cartViewModel: CartViewModel = hiltViewModel()
        val cartState by cartViewModel.uiState.collectAsState()
        val itemCount = cartState.items.sumOf { it.quantity }

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
                    BadgedBox(
                        badge = {
                            if (itemCount > 0) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.onPrimary,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ) {
                                    Text(
                                        text = if (itemCount > 99) "99+" else itemCount.toString(),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Shopping Cart"
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppBarWithTitle(title: String) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}