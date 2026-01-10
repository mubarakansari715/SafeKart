package com.safekart.safekart.domain.repository

import com.safekart.safekart.data.model.User

/**
 * Repository interface for authentication operations
 * This is part of the domain layer - defines the contract for authentication
 */
interface AuthRepository {
    val currentUser: User?
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User>
    
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        fullName: String? = null,
        phone: String? = null
    ): Result<User>
    
    fun signOut()
    
    fun getCurrentUserId(): String?
    
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    
    fun getToken(): String?
}


