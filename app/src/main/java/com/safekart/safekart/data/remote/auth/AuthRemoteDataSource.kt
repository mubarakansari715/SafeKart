package com.safekart.safekart.data.remote.auth

import android.content.SharedPreferences
import com.safekart.safekart.data.model.AuthData
import com.safekart.safekart.data.model.User
import com.safekart.safekart.data.remote.api.AuthApiService
import com.safekart.safekart.data.remote.api.ForgotPasswordRequest
import com.safekart.safekart.data.remote.api.LoginRequest
import com.safekart.safekart.data.remote.api.RegisterRequest
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Remote data source for Node.js API Authentication operations
 * Handles direct communication with backend API
 */
@Singleton
class AuthRemoteDataSource @Inject constructor(
    private val authApiService: AuthApiService,
    private val sharedPreferences: SharedPreferences
) {
    
    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
    }
    
    val currentUser: User?
        get() {
            val userId = sharedPreferences.getString(KEY_USER_ID, null)
            val email = sharedPreferences.getString(KEY_USER_EMAIL, null)
            return if (userId != null && email != null) {
                User(
                    id = userId,
                    email = email,
                    full_name = null,  // Not stored in SharedPreferences
                    phone = sharedPreferences.getString("user_phone", null),
                    role = sharedPreferences.getString("user_role", "customer") ?: "customer"
                )
            } else {
                null
            }
        }
    
    fun getCurrentUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }
    
    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            val request = LoginRequest(email = email, password = password)
            val response = authApiService.login(request)
            
            if (response.success && response.data != null) {
                val authData = response.data
                // Save token and user info
                saveAuthData(authData)
                Result.success(authData.user)
            } else {
                Result.failure(
                    Exception(response.message ?: response.error ?: "Login failed")
                )
            }
        } catch (e: HttpException) {
            // Handle HTTP errors (401, 500, etc.)
            val errorMessage = try {
                val errorBody = e.response()?.errorBody()?.string()
                // Try to parse error message from response
                errorBody ?: when (e.code()) {
                    401 -> "Invalid email or password"
                    400 -> "Invalid request"
                    500 -> "Server error. Please try again later"
                    else -> "Login failed: ${e.message()}"
                }
            } catch (ex: Exception) {
                when (e.code()) {
                    401 -> "Invalid email or password"
                    400 -> "Invalid request"
                    else -> "Login failed: ${e.message()}"
                }
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    when {
                        e.message?.contains("Unable to resolve host") == true -> "Network error. Please check your connection"
                        e.message?.contains("Failed to connect") == true -> "Cannot connect to server. Please check if server is running"
                        e.message?.contains("timeout") == true -> "Connection timeout. Please try again"
                        else -> e.message ?: "Login failed"
                    }
                )
            )
        }
    }
    
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        fullName: String? = null,  // Optional - backend will use email prefix if not provided
        phone: String? = null,
        role: String = "customer"
    ): Result<User> {
        return try {
            val request = RegisterRequest(
                email = email,
                password = password,
                fullName = fullName,  // Optional field
                phone = phone,
                role = role
            )
            val response = authApiService.register(request)
            
            if (response.success && response.data != null) {
                val authData = response.data
                // Save token and user info
                saveAuthData(authData)
                Result.success(authData.user)
            } else {
                Result.failure(
                    Exception(response.message ?: response.error ?: "Registration failed")
                )
            }
        } catch (e: HttpException) {
            // Handle HTTP errors (400, 500, etc.)
            val errorMessage = try {
                val errorBody = e.response()?.errorBody()?.string()
                errorBody ?: when (e.code()) {
                    400 -> "Invalid request. Please check your input"
                    409 -> "User with this email already exists"
                    500 -> "Server error. Please try again later"
                    else -> "Registration failed: ${e.message()}"
                }
            } catch (ex: Exception) {
                when (e.code()) {
                    400 -> "Invalid request. Please check your input"
                    409 -> "User with this email already exists"
                    else -> "Registration failed: ${e.message()}"
                }
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    when {
                        e.message?.contains("Unable to resolve host") == true -> "Network error. Please check your connection"
                        e.message?.contains("Failed to connect") == true -> "Cannot connect to server. Please check if server is running"
                        e.message?.contains("timeout") == true -> "Connection timeout. Please try again"
                        else -> e.message ?: "Registration failed"
                    }
                )
            )
        }
    }
    
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            val request = ForgotPasswordRequest(email = email)
            val response = authApiService.forgotPassword(request)
            
            // Backend returns success message at root level
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception(response.message ?: response.error ?: "Failed to send reset email")
                )
            }
        } catch (e: HttpException) {
            // Handle HTTP errors (4xx, 5xx)
            val errorMessage = try {
                val errorBody = e.response()?.errorBody()?.string()
                errorBody ?: when (e.code()) {
                    400 -> "Invalid email address"
                    500 -> "Server error. Please try again later"
                    else -> "Failed to send reset email: ${e.message()}"
                }
            } catch (ex: Exception) {
                when (e.code()) {
                    400 -> "Invalid email address"
                    500 -> "Server error. Please try again later"
                    else -> "Failed to send reset email: ${e.message()}"
                }
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    when {
                        e.message?.contains("Unable to resolve host") == true -> "Network error. Please check your connection"
                        e.message?.contains("Failed to connect") == true -> "Cannot connect to server. Please check if server is running"
                        e.message?.contains("timeout") == true -> "Connection timeout. Please try again"
                        else -> e.message ?: "Failed to send reset email"
                    }
                )
            )
        }
    }
    
    suspend fun getCurrentUser(): Result<User> {
        val token = getToken() ?: return Result.failure(Exception("No authentication token found"))
        
        return try {
            val response = authApiService.getCurrentUser("Bearer $token")
            
            if (response.success && response.data != null) {
                val user = response.data
                // Update stored user info
                sharedPreferences.edit()
                    .putString(KEY_USER_ID, user.id)
                    .putString(KEY_USER_EMAIL, user.email)
                    // full_name not stored in SharedPreferences
                    .putString("user_phone", user.phone)
                    .putString("user_role", user.role)
                    .apply()
                Result.success(user)
            } else {
                Result.failure(
                    Exception(response.message ?: response.error ?: "Failed to get user info")
                )
            }
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "Session expired. Please login again"
                404 -> "User not found"
                else -> "Failed to get user info: ${e.message()}"
            }
            Result.failure(Exception(errorMessage))
        } catch (e: Exception) {
            Result.failure(
                Exception(
                    when {
                        e.message?.contains("Unable to resolve host") == true -> "Network error. Please check your connection"
                        e.message?.contains("Failed to connect") == true -> "Cannot connect to server"
                        else -> e.message ?: "Failed to get user info"
                    }
                )
            )
        }
    }
    
    fun signOut() {
        sharedPreferences.edit()
            .remove(KEY_TOKEN)
            .remove("refresh_token")
            .remove(KEY_USER_ID)
            .remove(KEY_USER_EMAIL)
            // user_full_name not stored, so no need to remove
            .remove("user_phone")
            .remove("user_role")
            .apply()
    }
    
    private fun saveAuthData(authData: AuthData) {
        sharedPreferences.edit()
            .putString(KEY_TOKEN, authData.tokens.accessToken)  // Save access token
            .putString("refresh_token", authData.tokens.refreshToken)  // Save refresh token
            .putString(KEY_USER_ID, authData.user.id)
            .putString(KEY_USER_EMAIL, authData.user.email)
            // full_name not stored in SharedPreferences
            .putString("user_phone", authData.user.phone)
            .putString("user_role", authData.user.role)
            .apply()
    }
}
