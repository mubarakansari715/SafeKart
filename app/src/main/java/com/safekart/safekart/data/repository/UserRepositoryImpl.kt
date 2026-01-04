package com.safekart.safekart.data.repository

import com.safekart.safekart.data.remote.user.UserRemoteDataSource
import com.safekart.safekart.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of UserRepository
 * Delegates to UserRemoteDataSource for Firestore user data operations
 */
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {
    
    override suspend fun createUser(userId: String, email: String, username: String): Result<Unit> {
        return userRemoteDataSource.createUser(userId, email, username)
    }
    
    override suspend fun getUserName(userId: String): Result<String?> {
        return userRemoteDataSource.getUserName(userId)
    }
    
    override suspend fun getUserEmail(userId: String): Result<String?> {
        return userRemoteDataSource.getUserEmail(userId)
    }
}

