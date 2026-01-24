package com.safekart.safekart.util

/**
 * Centralized error handling utility
 * Provides user-friendly error messages from exceptions
 */
object ErrorHandler {
    
    /**
     * Get user-friendly error message from exception
     */
    fun getErrorMessage(exception: Throwable): String {
        return when {
            exception.message?.contains("Network error", ignoreCase = true) == true ||
            exception.message?.contains("Unable to resolve host", ignoreCase = true) == true ->
                "Network error. Please check your connection"
            
            exception.message?.contains("Cannot connect", ignoreCase = true) == true ||
            exception.message?.contains("Failed to connect", ignoreCase = true) == true ->
                "Cannot connect to server. Please check if server is running"
            
            exception.message?.contains("timeout", ignoreCase = true) == true ->
                "Connection timeout. Please try again"
            
            exception.message?.contains("Invalid email or password", ignoreCase = true) == true ->
                "Invalid email or password"
            
            exception.message?.contains("already exists", ignoreCase = true) == true ->
                "An account already exists with this email"
            
            exception.message?.contains("Session expired", ignoreCase = true) == true ->
                "Session expired. Please login again"
            
            exception.message?.contains("User not found", ignoreCase = true) == true ->
                "User not found"
            
            exception.message?.contains("No authentication token", ignoreCase = true) == true ->
                "Please login to continue"
            
            else -> exception.message ?: "An error occurred. Please try again"
        }
    }
    
    /**
     * Get error message from HTTP status code
     */
    fun getErrorMessage(statusCode: Int): String {
        return when (statusCode) {
            400 -> "Invalid request. Please check your input"
            401 -> "Invalid email or password"
            404 -> "Resource not found"
            409 -> "An account already exists with this email"
            422 -> "Invalid data. Please check your input"
            500 -> "Server error. Please try again later"
            else -> "An error occurred. Please try again"
        }
    }
}
