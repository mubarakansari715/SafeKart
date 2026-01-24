package com.safekart.safekart.data.remote.api

import com.google.gson.annotations.SerializedName
import com.safekart.safekart.data.model.ApiResponse
import com.safekart.safekart.data.model.AuthData
import com.safekart.safekart.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * API service interface for authentication endpoints
 */
interface AuthApiService {
    
    /**
     * Register a new user
     */
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): ApiResponse<AuthData>
    
    /**
     * Login user
     */
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): ApiResponse<AuthData>
    
    /**
     * Send password reset email
     * Note: Response doesn't include data field, only status and message
     * After sending email, user clicks link in email to reset password on website
     */
    @POST("auth/forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): ApiResponse<Any>
    
    /**
     * Get current user profile
     */
    @GET("auth/me")
    suspend fun getCurrentUser(
        @Header("Authorization") authorization: String  // Format: "Bearer <token>"
    ): ApiResponse<User>
}

/**
 * Request models
 */
data class RegisterRequest(
    val email: String,
    val password: String,
    @SerializedName("fullName")
    val fullName: String? = null,  // Optional - backend will use email prefix if not provided
    val phone: String? = null,
    val role: String? = "customer"  // Optional: "customer" or "vendor"
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class ForgotPasswordRequest(
    val email: String
)
