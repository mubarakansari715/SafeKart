package com.safekart.safekart.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.safekart.safekart.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of UserRepository
 * Handles Firestore user data operations
 */
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {
    
    override suspend fun createUser(userId: String, email: String, username: String): Result<Unit> {
        return try {
            val userData = hashMapOf(
                "email" to email,
                "username" to username,
                "uid" to userId,
                "displayName" to username, // Keep for backward compatibility
                "createdAt" to Date(),
                "updatedAt" to Date(),
                "emailVerified" to false,
                "accountStatus" to "active"
            )
            
            firestore.collection("users")
                .document(userId)
                .set(userData)
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUserName(userId: String): Result<String?> {
        return try {
            val userDoc = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            
            val username = userDoc.getString("username") 
                ?: userDoc.getString("displayName")
            
            Result.success(username)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUserEmail(userId: String): Result<String?> {
        return try {
            val userDoc = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            
            val email = userDoc.getString("email")
            Result.success(email)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

