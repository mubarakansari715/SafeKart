package com.safekart.safekart.domain.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication operations
 * This is part of the domain layer - defines the contract for authentication
 */
interface AuthRepository {
    val currentUser: FirebaseUser?
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser>
    
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<FirebaseUser>
    
    fun signOut()
    
    fun getCurrentUserId(): String?
    
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
}


