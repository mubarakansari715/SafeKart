package com.safekart.safekart.data.model

import com.google.gson.annotations.SerializedName

/**
 * API Response wrapper - matches backend response structure
 */
data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null,
    val error: String? = null
) {
    // Helper property for backward compatibility
    val status: String
        get() = if (success) "success" else "error"
}

/**
 * Tokens response from backend
 */
data class Tokens(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)

/**
 * Auth response data (for login and register) - matches backend structure
 */
data class AuthData(
    val user: User,
    val tokens: Tokens
) {
    // Helper property for backward compatibility - uses accessToken
    val token: String
        get() = tokens.accessToken
}
