package com.safekart.safekart.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import com.safekart.safekart.ui.theme.Primary
import com.safekart.safekart.ui.theme.PrimaryVariant
import com.safekart.safekart.ui.theme.Secondary

@Composable
fun WavyBackground(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        // Create wavy pattern using multiple waves
        val wavePath = Path().apply {
            moveTo(0f, height * 0.3f)
            
            // First wave
            cubicTo(
                width * 0.25f, height * 0.25f,
                width * 0.5f, height * 0.35f,
                width * 0.75f, height * 0.3f
            )
            cubicTo(
                width * 0.85f, height * 0.28f,
                width * 0.95f, height * 0.32f,
                width, height * 0.3f
            )
            
            lineTo(width, 0f)
            lineTo(0f, 0f)
            close()
        }
        
        // Draw gradient background
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Primary.copy(alpha = 0.9f),
                    Primary.copy(alpha = 0.7f)
                )
            )
        )
        
        // Draw wavy pattern
        drawPath(
            path = wavePath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    PrimaryVariant.copy(alpha = 0.6f),
                    Primary.copy(alpha = 0.4f)
                )
            )
        )
        
        // Add additional smaller waves for texture
        for (i in 0..2) {
            val waveOffset = i * 100f
            val waveHeight = height * (0.3f + i * 0.05f)
            
            val smallWave = Path().apply {
                moveTo(0f, waveHeight)
                cubicTo(
                    width * 0.3f, waveHeight - 20f,
                    width * 0.7f, waveHeight + 20f,
                    width, waveHeight
                )
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            
            drawPath(
                path = smallWave,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Secondary.copy(alpha = 0.2f - i * 0.05f),
                        Primary.copy(alpha = 0.1f - i * 0.02f)
                    )
                )
            )
        }
    }
}

