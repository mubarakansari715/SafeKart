package com.safekart.safekart.ui.presentation.productdetail

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.safekart.safekart.ui.presentation.productdetail.CartEvent
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.safekart.safekart.data.model.HomeProduct
import com.safekart.safekart.ui.theme.SafeKartTheme
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────────────────────────────────
// Entry Point
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigateToCart: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var isWishlisted by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.cartEvent.collect { event ->
            when (event) {
                is CartEvent.Added -> {
                    val result = snackbarHostState.showSnackbar(
                        message = "Added to cart!",
                        actionLabel = "View Cart",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        onNavigateToCart()
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> LoadingState()

            uiState.error != null -> ErrorState(
                message = uiState.error!!,
                onRetry = { viewModel.retry() }
            )

            uiState.product != null -> ProductDetailContent(
                product = uiState.product!!,
                cartQuantity = uiState.cartQuantity,
                onAddToCart = { viewModel.addToCart() },
                onIncreaseQuantity = { viewModel.increaseQuantity() },
                onDecreaseQuantity = { viewModel.decreaseQuantity() },
                onBuyNow = {
                    if (uiState.cartQuantity <= 0) viewModel.addToCart()
                    onNavigateToCart()
                }
            )
        }

        // Floating back button – fixed, respects status bar
        FloatingBackButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 12.dp, top = 8.dp)
        )

        // Floating wishlist button – fixed, respects status bar
        FloatingWishlistButton(
            isWishlisted = isWishlisted,
            onClick = { isWishlisted = !isWishlisted },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(end = 12.dp, top = 8.dp)
        )

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 90.dp)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Main Content
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ProductDetailContent(
    product: HomeProduct,
    cartQuantity: Int = 0,
    onAddToCart: () -> Unit,
    onIncreaseQuantity: () -> Unit = {},
    onDecreaseQuantity: () -> Unit = {},
    onBuyNow: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // Hero image gallery
            item {
                ProductImageGallery(imageUrls = product.imageUrls)
            }

            // Product info card
            item {
                ProductInfoSection(product = product)
            }
        }

        // Sticky bottom bar
        BottomActionBar(
            isInStock = product.stock > 0,
            cartQuantity = cartQuantity,
            maxQuantity = product.stock,
            onAddToCart = onAddToCart,
            onIncreaseQuantity = onIncreaseQuantity,
            onDecreaseQuantity = onDecreaseQuantity,
            onBuyNow = onBuyNow,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Hero Image Gallery
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProductImageGallery(imageUrls: List<String>) {
    val urls = imageUrls.filter { it.isNotBlank() }.ifEmpty { listOf("") }
    val pagerState = rememberPagerState(pageCount = { urls.size })
    val scope = rememberCoroutineScope()

    Column {
        // ── Main pager ──────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(Color(0xFFF8F8F8))
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                AsyncImage(
                    model = urls[page],
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentScale = ContentScale.Fit
                )
            }

            // Bottom gradient fade
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0x22000000))
                        )
                    )
            )

            // Image counter badge (bottom-right)
            if (urls.size > 1) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 14.dp, bottom = 14.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.55f)
                ) {
                    Text(
                        text = "${pagerState.currentPage + 1} / ${urls.size}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // Dot indicators (bottom-center)
            if (urls.size > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(urls.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        val dotColor by animateColorAsState(
                            targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.7f),
                            animationSpec = tween(250),
                            label = "dot"
                        )
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .size(
                                    width = if (isSelected) 20.dp else 7.dp,
                                    height = 7.dp
                                )
                                .clip(CircleShape)
                                .background(dotColor)
                        )
                    }
                }
            }
        }

        // ── Thumbnail strip (only if multiple images) ────────────────────────
        if (urls.size > 1) {
            Spacer(modifier = Modifier.height(14.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(urls) { index, url ->
                    val isSelected = pagerState.currentPage == index
                    val borderColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        animationSpec = tween(200),
                        label = "thumb_border"
                    )
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF0F0F0))
                            .border(
                                width = 2.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                scope.launch { pagerState.animateScrollToPage(index) }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(6.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Product Info Section
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ProductInfoSection(product: HomeProduct) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Brand + verified badge
        if (product.brand.isNotBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = product.brand.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Filled.Verified,
                    contentDescription = "Verified",
                    tint = Color(0xFF1E88E5),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Title
        Text(
            text = product.title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        // Rating row
        if (product.ratingAverage > 0) {
            RatingChip(rating = product.ratingAverage, count = product.ratingCount)
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        // Price section
        PriceSection(
            priceSale = product.priceSale,
            priceMrp = product.priceMrp,
            discountPercent = product.discountPercent
        )

        // Stock status
        StockBadge(stock = product.stock)

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

        // Description
        if (!product.descriptionShort.isNullOrBlank()) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "About this product",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = product.descriptionShort,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                    lineHeight = 22.sp
                )
            }
        }

        // Offer highlight row
        if (product.discountPercent > 0) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFF3E0)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocalOffer,
                        contentDescription = null,
                        tint = Color(0xFFE65100),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "You're saving ₹${(product.priceMrp - product.priceSale).toInt()} on this order!",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = Color(0xFFE65100)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sub-components
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun RatingChip(rating: Double, count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Star + number pill
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFFFF8E1)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = rating.toString(),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF6D4C00)
                )
            }
        }

        if (count > 0) {
            Text(
                text = "$count reviews",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
            )
        }
    }
}

@Composable
private fun PriceSection(priceSale: Double, priceMrp: Double, discountPercent: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "₹${priceSale.toInt()}",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            if (priceMrp > priceSale) {
                Text(
                    text = "₹${priceMrp.toInt()}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        textDecoration = TextDecoration.LineThrough
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }
        }
        if (discountPercent > 0) {
            Text(
                text = "$discountPercent% off  •  Inclusive of all taxes",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF2E7D32)
            )
        }
    }
}

@Composable
private fun StockBadge(stock: Int) {
    val inStock = stock > 0
    val bgColor = if (inStock) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
    val textColor = if (inStock) Color(0xFF2E7D32) else Color(0xFFC62828)

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Text(
                text = if (inStock) "In Stock ($stock available)" else "Out of Stock",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                color = textColor
            )
        }
    }
}

@Composable
private fun FloatingBackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(44.dp)
            .shadow(6.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color(0xFF212121),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun FloatingWishlistButton(
    isWishlisted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val heartIconColor by animateColorAsState(
        targetValue = if (isWishlisted) Color(0xFFE53935) else Color(0xFF9E9E9E),
        animationSpec = tween(300),
        label = "heartIcon"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isWishlisted) Color(0xFFE53935) else Color(0xFFBDBDBD),
        animationSpec = tween(300),
        label = "heartBorder"
    )
    Box(
        modifier = modifier
            .size(44.dp)
            .shadow(6.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.5.dp, borderColor, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = if (isWishlisted) "Remove from wishlist" else "Add to wishlist",
            tint = heartIconColor,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun BottomActionBar(
    isInStock: Boolean,
    cartQuantity: Int = 0,
    maxQuantity: Int = Int.MAX_VALUE,
    onAddToCart: () -> Unit,
    onIncreaseQuantity: () -> Unit = {},
    onDecreaseQuantity: () -> Unit = {},
    onBuyNow: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 12.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Add to Cart OR quantity (+ / -) controls
            if (cartQuantity == 0) {
                OutlinedButton(
                    onClick = onAddToCart,
                    enabled = isInStock,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(
                        1.5.dp,
                        if (isInStock) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Add to Cart",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            } else {
                // Quantity controls: minus, count, plus
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onDecreaseQuantity,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease quantity",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = cartQuantity.toString(),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        IconButton(
                            onClick = onIncreaseQuantity,
                            enabled = cartQuantity < maxQuantity,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase quantity",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Buy Now (filled) – add to cart if needed and go to cart screen
            Button(
                onClick = onBuyNow,
                enabled = isInStock,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
            ) {
                Text(
                    text = "Buy Now",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Loading / Error states
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            TextButton(onClick = onRetry) {
                Text("Try Again")
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Preview
// ─────────────────────────────────────────────────────────────────────────────

private val previewProduct = HomeProduct(
    id = "1",
    title = "Premium Wireless Noise-Cancelling Headphones",
    brand = "SoundMax",
    descriptionShort = "Experience crystal-clear audio with active noise cancellation, " +
            "30-hour battery life, and ultra-comfortable memory-foam ear cushions. " +
            "Perfect for music lovers and professionals on the go.",
    priceMrp = 4999.0,
    priceSale = 2999.0,
    discountPercent = 40,
    stock = 15,
    imageUrls = listOf(
        "https://picsum.photos/seed/a/400/400",
        "https://picsum.photos/seed/b/400/400",
        "https://picsum.photos/seed/c/400/400"
    ),
    ratingAverage = 4.5,
    ratingCount = 1284
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProductDetailContentPreview() {
    SafeKartTheme {
        ProductDetailContent(
            product = previewProduct,
            cartQuantity = 0,
            onAddToCart = {},
            onIncreaseQuantity = {},
            onDecreaseQuantity = {},
            onBuyNow = {}
        )
    }
}
