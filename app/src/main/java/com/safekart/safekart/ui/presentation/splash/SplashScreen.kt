package com.safekart.safekart.ui.presentation.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
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
    var lottieStartAnimation by remember { mutableStateOf(false) }
    var textStartAnimation by remember { mutableStateOf(false) }
    
    val lottieAlphaAnim = animateFloatAsState(
        targetValue = if (lottieStartAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "lottieAlpha"
    )
    val lottieScaleAnim = animateFloatAsState(
        targetValue = if (lottieStartAnimation) 1f else 0.3f,
        animationSpec = tween(durationMillis = 800),
        label = "lottieScale"
    )
    
    val textAlphaAnim = animateFloatAsState(
        targetValue = if (textStartAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "textAlpha"
    )
    val textScaleAnim = animateFloatAsState(
        targetValue = if (textStartAnimation) 1f else 0.3f,
        animationSpec = tween(durationMillis = 300),
        label = "textScale"
    )
    
    LaunchedEffect(key1 = true) {
        // Start Lottie animation first
        delay(200)
        lottieStartAnimation = true
        
        // Start text animation after Lottie animation starts
        delay(600)
        textStartAnimation = true
    }
    
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
                .scale(scale),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Lottie Animation - Infinite loop with fade and scale animation
            val composition by rememberLottieComposition(
                LottieCompositionSpec.Asset("shopping_girl.json")
            )
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .size(200.dp)
                    .alpha(lottieAlphaAnim.value)
                    .scale(lottieScaleAnim.value)
            )
            
            // Text with similar animation style
            Text(
                text = "SellMyProduct",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                modifier = Modifier
                    .alpha(textAlphaAnim.value)
                    .scale(textScaleAnim.value)
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

