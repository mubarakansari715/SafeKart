package com.safekart.safekart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.safekart.safekart.navigation.NavRoutes
import com.safekart.safekart.ui.presentation.auth.forgotpassword.ForgotPasswordScreen
import com.safekart.safekart.ui.theme.SafeKartTheme
import com.safekart.safekart.ui.presentation.home.HomeScreen
import com.safekart.safekart.ui.presentation.auth.login.LoginScreen
import com.safekart.safekart.ui.presentation.auth.register.RegisterScreen
import com.safekart.safekart.ui.presentation.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SafeKartTheme {

                val navController = rememberNavController()

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

                    composable(NavRoutes.HOME) {
                        HomeScreen(
                            onLogout = {
                                navController.navigate(NavRoutes.LOGIN) {
                                    popUpTo(NavRoutes.HOME) { inclusive = true }
                                }
                            })
                    }
                }
            }
        }
    }
}