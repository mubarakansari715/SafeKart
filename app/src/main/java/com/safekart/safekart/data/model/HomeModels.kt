package com.safekart.safekart.data.model

import com.google.gson.annotations.SerializedName

/**
 * Response from GET /api/v1/home
 */
data class HomeApiResponse(
    val success: Boolean = false,
    val data: HomeData? = null,
    val message: String? = null
)

data class HomeData(
    val banners: List<Banner> = emptyList(),
    val categories: List<HomeCategory> = emptyList(),
    @SerializedName("best_selling") val bestSelling: List<HomeProduct> = emptyList(),
    @SerializedName("offer_banner") val offerBanner: OfferBanner? = null,
    val product: List<ProductSection> = emptyList()
)

data class Banner(
    val id: String,
    val title: String = "",
    val subtitle: String? = null,
    @SerializedName("image_url") val imageUrl: String = "",
    @SerializedName("mobile_image_url") val mobileImageUrl: String? = null,
    val position: String? = null,
    @SerializedName("link_type") val linkType: String = "none",
    @SerializedName("link_value") val linkValue: String? = null,
    @SerializedName("sort_order") val sortOrder: Int = 0
)

data class HomeCategory(
    val id: String,
    val name: String = "",
    val slug: String = "",
    @SerializedName("parent_id") val parentId: String? = null,
    @SerializedName("image_url") val imageUrl: String? = null,
    @SerializedName("sort_order") val sortOrder: Int = 0
)

data class OfferBanner(
    val id: String? = null,
    val title: String? = null,
    val subtitle: String? = null,
    @SerializedName("image_url") val imageUrl: String? = null,
    @SerializedName("mobile_image_url") val mobileImageUrl: String? = null,
    @SerializedName("link_type") val linkType: String? = null,
    @SerializedName("link_value") val linkValue: String? = null
) {
    fun isValid(): Boolean = !id.isNullOrBlank() && !imageUrl.isNullOrBlank()
}

data class ProductSection(
    val title: String = "",
    @SerializedName("category_id") val categoryId: String? = null,
    @SerializedName("category_slug") val categorySlug: String = "",
    val products: List<HomeProduct> = emptyList()
)

data class HomeProduct(
    val id: String,
    val title: String = "",
    val brand: String = "",
    @SerializedName("description_short") val descriptionShort: String? = null,
    @SerializedName("price_mrp") val priceMrp: Double = 0.0,
    @SerializedName("price_sale") val priceSale: Double = 0.0,
    @SerializedName("discount_percent") val discountPercent: Int = 0,
    val stock: Int = 0,
    @SerializedName("image_urls") val imageUrls: List<String> = emptyList(),
    @SerializedName("category_id") val categoryId: String? = null,
    @SerializedName("rating_average") val ratingAverage: Double = 0.0,
    @SerializedName("rating_count") val ratingCount: Int = 0,
    @SerializedName("created_at") val createdAt: String? = null
) {
    fun firstImageUrl(): String? = imageUrls.firstOrNull()?.takeIf { it.isNotBlank() }
}
