package com.safekart.safekart.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.safekart.safekart.ui.theme.SafeKartTheme

/**
 * Facebook-style shimmer modifier.
 * Applies a moving gradient across the composable for skeleton loading effect.
 */
fun Modifier.shimmerEffect(
    baseColor: Color = Color.LightGray.copy(alpha = 0.3f),
    highlightColor: Color = Color.White.copy(alpha = 0.5f),
    durationMillis: Int = 1200
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    drawBehind {
        val brush = Brush.linearGradient(
            colors = listOf(
                baseColor,
                highlightColor,
                baseColor
            ),
            start = Offset(size.width * (translateAnim * 2 - 1f), 0f),
            end = Offset(size.width * translateAnim * 2, 0f)
        )
        drawRect(brush = brush)
    }
}

/**
 * A single shimmer placeholder box.
 * Use for skeleton loading placeholders.
 */
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    baseColor: Color = Color.LightGray.copy(alpha = 0.3f),
    highlightColor: Color = Color.White.copy(alpha = 0.6f)
) {
    Box(
        modifier = modifier
            .background(
                baseColor,
                shape = RoundedCornerShape(4.dp)
            )
            .shimmerEffect(
                baseColor = baseColor,
                highlightColor = highlightColor
            )
    )
}

/**
 * Generic shimmer loading indicator.
 * Shows centered placeholder shapes with shimmer effect.
 */
@Composable
fun ShimmerLoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top placeholder - mimics header/card
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            // Row of small placeholders - mimics chips/list
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(4) {
                    ShimmerBox(
                        modifier = Modifier
                            .height(32.dp)
                            .fillMaxWidth(0.22f)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }
            // Bottom placeholders - mimics content cards
            repeat(3) {
                ShimmerBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

/**
 * Shimmer content for buttons when loading.
 * Shows a shimmer bar instead of text.
 */
@Composable
fun ShimmerButtonContent(
    modifier: Modifier = Modifier,
    contentColor: Color = Color.White
) {
    ShimmerBox(
        modifier = modifier
            .width(100.dp)
            .height(20.dp)
            .clip(RoundedCornerShape(4.dp)),
        baseColor = contentColor.copy(alpha = 0.4f),
        highlightColor = contentColor.copy(alpha = 0.8f)
    )
}

@Preview(name = "Shimmer Box", showBackground = true)
@Composable
private fun ShimmerBoxPreview() {
    SafeKartTheme {
        ShimmerBox(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

@Preview(name = "Shimmer Loading", showBackground = true)
@Composable
private fun ShimmerLoadingPreview() {
    SafeKartTheme {
        ShimmerLoadingIndicator()
    }
}
