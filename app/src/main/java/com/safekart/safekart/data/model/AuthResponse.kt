package com.safekart.safekart.data.model

/**
 * API Response wrapper
 */
data class ApiResponse<T>(
    val status: String,
    val message: String? = null,
    val data: T? = null,
    val error: String? = null
)

/**
 * Auth response data (for login and register)
 */
data class AuthData(
    val user: User,
    val token: String
)
