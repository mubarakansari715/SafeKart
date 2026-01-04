package com.safekart.safekart.data.repository

import com.google.firebase.auth.FirebaseUser
import com.safekart.safekart.data.remote.auth.AuthRemoteDataSource
import com.safekart.safekart.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of AuthRepository
 * Delegates to AuthRemoteDataSource for Firebase Authentication operations
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    
    override val currentUser: FirebaseUser?
        get() = authRemoteDataSource.currentUser

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> {
        return authRemoteDataSource.signInWithEmailAndPassword(email, password)
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return authRemoteDataSource.createUserWithEmailAndPassword(email, password)
    }

    override fun signOut() {
        authRemoteDataSource.signOut()
    }

    override fun getCurrentUserId(): String? {
        return authRemoteDataSource.getCurrentUserId()
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return authRemoteDataSource.sendPasswordResetEmail(email)
    }
}

