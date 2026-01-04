package com.safekart.safekart.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user operations
 * This is part of the domain layer - defines the contract for user data
 */
interface UserRepository {
    suspend fun createUser(userId: String, email: String, username: String): Result<Unit>
    
    suspend fun getUserName(userId: String): Result<String?>
    
    suspend fun getUserEmail(userId: String): Result<String?>
}


