package com.safekart.safekart.data.model

/**
 * User data model for API responses
 */
data class User(
    val id: String,
    val email: String,
    val full_name: String? = null,
    val phone: String? = null,
    val role: String = "customer",
    val created_at: String? = null,
    val last_login: String? = null
)
