package com.safekart.safekart.domain.usecase.auth

import com.google.firebase.auth.FirebaseUser
import com.safekart.safekart.domain.repository.AuthRepository
import com.safekart.safekart.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Use case for user registration
 * Contains business logic for user registration and profile creation
 */
class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        username: String
    ): Result<FirebaseUser> {
        val authResult = authRepository.createUserWithEmailAndPassword(email, password)
        
        val user = authResult.getOrElse { exception ->
            return Result.failure(exception)
        }
        
        // Create user document in Firestore
        val userCreationResult = userRepository.createUser(user.uid, email, username)
        
        return if (userCreationResult.isSuccess) {
            Result.success(user)
        } else {
            Result.failure(
                Exception("Account created but failed to save user data: ${userCreationResult.exceptionOrNull()?.message}")
            )
        }
    }
}

