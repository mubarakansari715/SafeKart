package com.safekart.safekart.ui.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.safekart.safekart.ui.components.ShimmerBox

/**
 * Home-screen-only shimmer effect.
 * Matches the current home layout: banner, Shop By Categories grid (circular placeholders),
 * section header, and product row placeholders.
 */
@Composable
fun HomeShimmerEffect(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner shimmer
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(160.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        // "Shop By Categories" section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerBox(
                modifier = Modifier
                    .width(180.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            ShimmerBox(
                modifier = Modifier
                    .width(64.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
        }

        // Category grid: 4 columns, circular placeholders + label (matches CategoryGridItem)
        repeat(3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(4) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        ShimmerBox(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                        )
                        ShimmerBox(
                            modifier = Modifier
                                .width(56.dp)
                                .height(10.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                }
            }
        }

        // Section header (e.g. "Best selling")
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        // Product row shimmer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            repeat(3) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ShimmerBox(
                        modifier = Modifier
                            .width(140.dp)
                            .height(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    ShimmerBox(
                        modifier = Modifier
                            .width(120.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    ShimmerBox(
                        modifier = Modifier
                            .width(60.dp)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                }
            }
        }
    }
}
