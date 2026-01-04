package com.safekart.safekart.data.remote.user

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Remote data source for Firestore user operations
 * Handles direct communication with Firestore
 */
@Singleton
class UserRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    
    private val usersCollection = firestore.collection("users")
    
    suspend fun createUser(userId: String, email: String, username: String): Result<Unit> {
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
            
            usersCollection
                .document(userId)
                .set(userData)
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserName(userId: String): Result<String?> {
        return try {
            val userDoc = usersCollection
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
    
    suspend fun getUserEmail(userId: String): Result<String?> {
        return try {
            val userDoc = usersCollection
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

