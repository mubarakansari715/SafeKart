package com.safekart.safekart.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.safekart.safekart.util.Constants
import kotlinx.coroutines.delay

@Composable
fun AnimatedAppLabel(
    modifier: Modifier = Modifier
) {
    val text = Constants.APP_NAME
    var visibleCharCount by remember { mutableStateOf(0) }
    
    LaunchedEffect(key1 = true) {
        delay(300)
        // Animate each character appearing one by one from right to left
        // Start from the last character (rightmost) and work backwards
        for (i in 1..text.length) {
            visibleCharCount = i
            delay(150) // Delay between each character
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        text.forEachIndexed { index, char ->
            // Show characters from right to left: last character first
            val isVisible = index >= (text.length - visibleCharCount)
            val charAlpha = animateFloatAsState(
                targetValue = if (isVisible) 1f else 0f,
                animationSpec = tween(durationMillis = 300),
                label = "charAlpha_$index"
            )
            
            val charScale = animateFloatAsState(
                targetValue = if (isVisible) 1f else 0.3f,
                animationSpec = tween(durationMillis = 300),
                label = "charScale_$index"
            )

            Text(
                text = char.toString(),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                modifier = Modifier
                    .alpha(charAlpha.value)
                    .scale(charScale.value)
            )
        }
    }
}

