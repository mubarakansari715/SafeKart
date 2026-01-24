package com.safekart.safekart.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.safekart.safekart.ui.presentation.auth.forgotpassword.ForgotPasswordScreen
import com.safekart.safekart.ui.presentation.auth.login.LoginScreen
import com.safekart.safekart.ui.presentation.auth.register.RegisterScreen
import com.safekart.safekart.ui.presentation.cart.CartScreen
import com.safekart.safekart.ui.presentation.home.HomeScreen
import com.safekart.safekart.ui.presentation.profile.ProfileScreen
import com.safekart.safekart.ui.presentation.search.SearchScreen
import com.safekart.safekart.ui.presentation.splash.SplashScreen

/**
 * Main navigation graph for the app
 * Centralized navigation setup for better maintainability
 */
@Composable
fun NavigationGraph(
    navController: NavHostController,
    onNavigateToCart: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH
    ) {
        // Auth Flow
        composable(NavRoutes.SPLASH) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(NavRoutes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(NavRoutes.REGISTER)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(NavRoutes.FORGOT_PASSWORD)
                }
            )
        }

        composable(NavRoutes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Main App Flow
        composable(NavRoutes.HOME) {
            HomeScreen()
        }

        composable(NavRoutes.SEARCH) {
            SearchScreen()
        }

        composable(NavRoutes.PROFILE) {
            ProfileScreen(
                onLogout = {
                    onLogout()
                    navController.navigate(NavRoutes.LOGIN) {
                        popUpTo(NavRoutes.PROFILE) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.CART) {
            CartScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
