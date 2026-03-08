package com.safekart.safekart.navigation

object NavRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
    const val HOME = "home"
    const val CATEGORIES = "categories"
    const val SEARCH = "search"
    const val PROFILE = "profile"
    const val CART = "cart"
    const val PRODUCT_DETAIL = "product_detail/{productId}"

    fun productDetail(productId: String) = "product_detail/$productId"
}


