package com.safekart.safekart.ui.presentation.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.safekart.safekart.ui.components.WavyBackground
import com.safekart.safekart.ui.theme.Background
import com.safekart.safekart.ui.theme.SafeKartTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alpha"
    )
    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(durationMillis = 1000),
        label = "scale"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000) // Show splash for 2 seconds
        
        // Check if user is already logged in
        if (viewModel.isUserLoggedIn()) {
            onNavigateToHome()
        } else {
            onNavigateToLogin()
        }
    }

    SplashContent(
        alpha = alphaAnim.value,
        scale = scaleAnim.value
    )
}

@Composable
fun SplashContent(
    alpha: Float,
    scale: Float
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        // Wavy background pattern
        WavyBackground(
            modifier = Modifier.fillMaxSize()
        )
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .alpha(alpha)
                .scale(scale)
        ) {
            // Lottie Animation - Infinite loop
            val composition by rememberLottieComposition(
                LottieCompositionSpec.Asset("shopping_girl.json")
            )
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(200.dp)
            )
            
            Text(
                text = "SellMyProduct",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@Preview(
    name = "Splash Screen",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun SplashScreenPreview() {
    SafeKartTheme {
        SplashContent(
            alpha = 1f,
            scale = 1f
        )
    }
}

@Preview(
    name = "Splash Screen - Animated",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun SplashScreenAnimatedPreview() {
    SafeKartTheme {
        SplashContent(
            alpha = 0.7f,
            scale = 0.8f
        )
    }
}

