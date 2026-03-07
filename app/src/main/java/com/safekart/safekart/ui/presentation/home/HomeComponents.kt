package com.safekart.safekart.ui.presentation.home

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.safekart.safekart.data.model.Banner
import com.safekart.safekart.data.model.HomeCategory
import com.safekart.safekart.data.model.HomeProduct
import com.safekart.safekart.data.model.OfferBanner
import com.safekart.safekart.ui.theme.SafeKartTheme
import coil.compose.AsyncImage

// region — Banners

@Composable
fun BannersSection(
    banners: List<Banner>,
    onClick: (String, String?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        items(banners) { banner ->
            val imageUrl = banner.mobileImageUrl?.takeIf { it.isNotBlank() } ?: banner.imageUrl
            val context = LocalContext.current
            Surface(
                modifier = Modifier
                    .fillParentMaxWidth(0.9f)
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onClick(banner.linkType, banner.linkValue) },
                shape = RoundedCornerShape(12.dp)
            ) {
                if (imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = banner.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = banner.title.ifBlank { "Banner" },
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

// endregion

// region — Categories

@Composable
fun CategoriesSection(
    categories: List<HomeCategory>,
    onClick: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories) { cat ->
            AssistChip(
                onClick = { onClick(cat.slug) },
                label = { Text(cat.name) }
            )
        }
    }
}

// endregion

// region — Offer banner

@Composable
fun OfferBannerSection(
    offer: OfferBanner,
    onClick: (String, String?) -> Unit
) {
    val imageUrl = offer.mobileImageUrl?.takeIf { it.isNotBlank() } ?: offer.imageUrl ?: ""
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick(offer.linkType ?: "none", offer.linkValue) },
        shape = RoundedCornerShape(12.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = offer.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}

// endregion

// region — Section header

@Composable
fun SectionHeader(
    title: String,
    onViewAll: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        if (onViewAll != null) {
            TextButton(onClick = onViewAll) {
                Text("View all")
            }
        }
    }
}

// endregion

// region — Product row & card

@Composable
fun ProductRow(
    products: List<HomeProduct>,
    onProductClick: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(products) { product ->
            ProductCard(
                product = product,
                onClick = { onProductClick(product.id) }
            )
        }
    }
}

@Composable
fun ProductCard(
    product: HomeProduct,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = product.firstImageUrl(),
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "₹${product.priceSale.toInt()}",
                style = MaterialTheme.typography.titleSmall
            )
            if (product.discountPercent > 0) {
                Text(
                    text = "${product.discountPercent}% off",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// endregion

// region — Loading, error, empty

@Composable
fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorRetry(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun EmptyHomeState(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No content right now",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onRetry) {
            Text("Retry")
        }
    }
}

// endregion

// region — Preview sample data

private val previewBanners = listOf(
    Banner(id = "1", title = "Summer Sale", imageUrl = "https://picsum.photos/400/160"),
    Banner(id = "2", title = "New Arrivals", imageUrl = "https://picsum.photos/400/161")
)

private val previewCategories = listOf(
    HomeCategory(id = "1", name = "Electronics", slug = "electronics"),
    HomeCategory(id = "2", name = "Fashion", slug = "fashion"),
    HomeCategory(id = "3", name = "Home", slug = "home")
)

private val previewOfferBanner = OfferBanner(
    id = "1",
    title = "50% Off",
    imageUrl = "https://picsum.photos/800/240"
)

private val previewProducts = listOf(
    HomeProduct(
        id = "1",
        title = "Sample Product One",
        brand = "Brand",
        priceMrp = 999.0,
        priceSale = 799.0,
        discountPercent = 20,
        imageUrls = listOf("https://picsum.photos/200")
    ),
    HomeProduct(
        id = "2",
        title = "Sample Product Two",
        brand = "Brand",
        priceMrp = 599.0,
        priceSale = 599.0,
        discountPercent = 0,
        imageUrls = listOf("https://picsum.photos/201")
    )
)

// endregion

// region — Previews

@Preview(name = "Banners Section", showBackground = true)
@Composable
private fun BannersSectionPreview() {
    SafeKartTheme {
        BannersSection(banners = previewBanners, onClick = { _, _ -> })
    }
}

@Preview(name = "Categories Section", showBackground = true)
@Composable
private fun CategoriesSectionPreview() {
    SafeKartTheme {
        CategoriesSection(categories = previewCategories, onClick = { })
    }
}

@Preview(name = "Offer Banner Section", showBackground = true)
@Composable
private fun OfferBannerSectionPreview() {
    SafeKartTheme {
        OfferBannerSection(offer = previewOfferBanner, onClick = { _, _ -> })
    }
}

@Preview(name = "Section Header (with View all)", showBackground = true)
@Composable
private fun SectionHeaderWithViewAllPreview() {
    SafeKartTheme {
        SectionHeader(title = "Best selling", onViewAll = {})
    }
}

@Preview(name = "Section Header (no View all)", showBackground = true)
@Composable
private fun SectionHeaderNoViewAllPreview() {
    SafeKartTheme {
        SectionHeader(title = "Best selling", onViewAll = null)
    }
}

@Preview(name = "Product Row", showBackground = true)
@Composable
private fun ProductRowPreview() {
    SafeKartTheme {
        ProductRow(products = previewProducts, onProductClick = {})
    }
}

@Preview(name = "Product Card", showBackground = true)
@Composable
private fun ProductCardPreview() {
    SafeKartTheme {
        ProductCard(product = previewProducts.first(), onClick = {})
    }
}

@Preview(name = "Full Screen Loading", showBackground = true)
@Composable
private fun FullScreenLoadingPreview() {
    SafeKartTheme {
        FullScreenLoading()
    }
}

@Preview(name = "Error Retry", showBackground = true)
@Composable
private fun ErrorRetryPreview() {
    SafeKartTheme {
        ErrorRetry(message = "Something went wrong. Please try again.", onRetry = {})
    }
}

@Preview(name = "Empty Home State", showBackground = true)
@Composable
private fun EmptyHomeStatePreview() {
    SafeKartTheme {
        EmptyHomeState(onRetry = {})
    }
}

// endregion
